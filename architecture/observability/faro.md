# Grafana Faro

Frontend observability: **Real User Monitoring (RUM)**, Web Vitals, JS errors,
browser-side traces, session data. The only signal source that doesn't go
through Alloy — browsers ship directly to a Grafana Cloud collector endpoint.

## Why it exists

Server-side metrics tell you nothing about actual user experience. A service
returning 200 OK in 50ms says nothing about whether the user's browser
rendered the page in 5 seconds because of a 3 MB JS bundle, a blocked main
thread, or a CDN stall halfway across the world.

Frontend observability captures what actually happens in the browser:

- **Web Vitals** — LCP, CLS, FCP, TTFB, INP (Google's perceived-performance metrics).
- **JS errors** — uncaught exceptions with stack traces and breadcrumbs.
- **Sessions** — grouped user interactions over time.
- **Browser traces** — fetch/XHR calls propagated with `traceparent` so you
  can correlate frontend action → backend span in Tempo.

Faro is Grafana's answer. The alternative space is Sentry, Datadog RUM,
New Relic Browser, etc.

## How it's used

1. `npm install @grafana/faro-react @grafana/faro-web-tracing`.
2. Call `initializeFaro({ url, app: { name, version } })` at app startup
   (before React renders).
3. Add `withFaroRouterInstrumentation(router)` (Vite + React Router) or
   `faro.api.setView({ name })` on route changes (Next.js App Router).
4. Optional: build-time plugin (`@grafana/faro-rollup-plugin` or
   `-webpack-plugin`) uploads source maps so errors show readable stacks.
5. In Grafana Cloud → Frontend Observability, you see Web Vitals, errors,
   sessions, all correlated with backend traces.

The browser posts directly to:

```
https://faro-collector-prod-<region>.grafana.net/collect/<ingest-key>
```

No agent in between. That's why **CORS allowlisting** of your production
origins in the Grafana Cloud Frontend app settings matters — otherwise the
browser blocks the POST.

## When to use it

- **User-facing web app** — if users suffer, you need to know without waiting
  for a support ticket.
- **SPA with client-side routing** — backend access logs can't tell you about
  view changes or stuck renders.
- **Trace-correlated debugging** — wire `TracingInstrumentation` so a slow
  user click links to the backend trace that served it. This is the killer
  correlation feature.

Don't bother for:

- Purely internal back-office tooling where you have direct access to
  affected users.
- Static sites with no JS interaction to measure beyond Web Vitals (those
  alone don't justify a SDK — use RUM only if you act on the data).

## Alternatives

| Option | Why pick it |
|---|---|
| **Sentry** | Best-in-class error tracking and stack traces, generous free tier, mature UX |
| **Datadog RUM** | Deep integration with Datadog APM, session replay, expensive |
| **New Relic Browser** | Same positioning as Datadog; strong if already on NR |
| **LogRocket / FullStory** | Session replay-first products; privacy-heavy |
| **Google Analytics 4 + custom Web Vitals shipping** | Free, covers basics, no error tracking |
| **Self-hosted OpenTelemetry browser SDKs** | Vendor-neutral; less polished UX, more integration work |

If the priority is **error tracking with quality stack traces and
release management**, Sentry is usually better than Faro. If the priority is
**unified platform with backend observability and trace correlation**, Faro
wins because it's already integrated with the Grafana Cloud tenant holding
your Tempo/Loki/Mimir data.

Most mature setups end up using both: Sentry for errors, Faro for RUM +
Web Vitals + trace correlation.

## Tradeoffs

- **+** Direct integration with the rest of your Grafana Cloud stack — one
  bill, shared labels, one-click from frontend session to backend trace.
- **+** Web Vitals are captured out of the box with correct edge cases
  (BFCache, Soft Navigation).
- **+** Open-source SDK, no vendor lock at the collection layer.
- **−** Error-tracking UX is behind Sentry's in polish (grouping, regressions,
  issue management, release notifications).
- **−** Source-map uploads require extra build tooling and a `glc_*` token.
  Miss it and errors show minified gibberish.
- **−** Docker build-arg + empty-ENV footgun: `ARG` + `ENV = $ARG` with no
  `--build-arg` silently disables Faro (see notes below).
- **−** CORS setup per origin; forgettable step that surfaces only in prod.

## How it works — general

Browser SDK captures Web Vitals via the Web Performance API, hooks
`window.onerror` / `unhandledrejection` for JS errors, instruments `fetch` /
`XMLHttpRequest` for traces, and batches events to a collector endpoint.
Collector fans events out into Grafana Cloud's Mimir/Loki/Tempo.

```
  Browser (React/Next.js)
    │ @grafana/faro-web-sdk + faro-web-tracing
    ▼
  Events (errors, Web Vitals, view changes, fetch spans)
    │ batch + POST (beacon API on pagehide)
    ▼
  Faro Collector (grafana.net)
    │ fan-out
    ▼
  Tempo (spans) + Loki (logs/errors) + Mimir (RUM metrics)
```

## How it works — detailed

### Initialization

`initializeFaro()` creates a singleton Faro instance that:

- Attaches a unique `session.id` (persists in `sessionStorage` for the session).
- Attaches `user.id` if supplied.
- Reads `app.name`, `app.version`, `app.environment` and attaches them to
  every event.

Must run **before React renders** to catch initial-load Web Vitals and any
early synchronous errors.

### Instrumentations

SDK is a collection of **instrumentations**:

- `WebVitalsInstrumentation` — listens to PerformanceObserver entries
  (LCP, CLS, FCP, TTFB, INP).
- `ErrorsInstrumentation` — hooks `window.onerror`, `unhandledrejection`,
  React error boundaries if opted in.
- `ConsoleInstrumentation` — captures `console.error` / `warn` as events.
- `TracingInstrumentation` — wraps `fetch` / `XHR` to start a browser span
  and inject `traceparent` on outgoing requests.
- `SessionInstrumentation` — session lifecycle and visibility events.
- `ViewInstrumentation` — page views + SPA navigations.

Each instrumentation pushes events to a central transport.

### Transport

The default transport batches events and POSTs them as JSON to the
collector URL. Uses the Beacon API on `pagehide` to flush remaining events
even if the user navigates away.

Backpressure: bounded in-memory queue; drops under extreme pressure.

### Source maps

In production, JS is minified (`main-abc123.js`). Stack traces look like
`at a (main-abc123.js:2:1234)` — useless. Source maps (`.map` files)
invert the minification.

Faro's build plugins:
- Generate **hidden** source maps (`sourcemap: "hidden"`) — `.map` files
  exist, but the bundle doesn't reference them (keeps source private from
  casual viewers).
- Upload the maps to Grafana Cloud (tagged with `app.version`) during
  `vite build` / `next build`.
- Grafana Cloud resolves frames server-side at display time — your source
  isn't shipped to users but you see readable traces.

Requires `GRAFANA_OBSERVABILITY_FARO_TOKEN` (a `glc_*` access policy token
with `sourcemaps:read` + `sourcemaps:write`) at build time.

### Trace correlation

With `TracingInstrumentation` enabled:

- Every `fetch` gets a W3C `traceparent` header injected.
- Backend (Micronaut OTel) reads it, continues the trace with the same
  `trace_id`.
- The browser span and the server span share `trace_id`.
- In Tempo, a slow trace shows browser → network → server → DB in one view.

### Web Vitals captured

- **LCP** (Largest Contentful Paint) — perceived load speed. Target < 2.5s.
- **CLS** (Cumulative Layout Shift) — visual stability. Target < 0.1.
- **FCP** (First Contentful Paint) — first paint. Target < 1.8s.
- **TTFB** (Time To First Byte) — server responsiveness. Target < 0.8s.
- **INP** (Interaction to Next Paint) — responsiveness. Target < 200ms.

Grafana Cloud ships 7 prebuilt alerts matching these (enable them, they cost
nothing and catch real regressions).

## How it works — under the hood

### Event shape

Each event is a JSON object with a common envelope:

```json
{
  "meta": {
    "app": { "name": "costy-web", "version": "1.4.2" },
    "session": { "id": "...", "attributes": { "country": "US" } },
    "browser": { "name": "Chrome", "version": "131", "os": "macOS" },
    "page": { "url": "https://costy.app/dashboard" }
  },
  "payload": { /* kind-specific: error, measurement, log, span, event */ }
}
```

Collector dispatches by payload kind:

- Measurements → Mimir (as `web_vitals_*` metrics).
- Errors → Loki (structured log stream) + Tempo if traceparent present.
- Spans → Tempo.

### Docker footgun (worth memorising)

```dockerfile
ARG VITE_FARO_URL
ENV VITE_FARO_URL=$VITE_FARO_URL
```

If `--build-arg VITE_FARO_URL=...` isn't passed, the ARG is empty (not
unset). `ENV FOO=""` is "FOO is the empty string." In JS, `process.env.FOO
?? fallback` is `""` because `??` only replaces null/undefined.

Bundler then tree-shakes the initialization code (it sees `if (!faroUrl)
return`), and production bundles contain zero references to
`faro-collector`. "We deployed Faro, nothing appears in Grafana." To fix:
hardcode the collector URL in source, or treat empty strings as unset, or
ensure every build always gets the arg.

Verify: `docker run --rm <image> grep -r faro-collector /usr/share/nginx/html`.

### CORS

Browsers enforce same-origin on cross-origin POSTs. The Faro collector
responds with `Access-Control-Allow-Origin: <origin>` only for origins
allowlisted in the Frontend app's settings in Grafana Cloud. Missing that
allowlist = silent failure. Debug in DevTools Network tab → CORS errors on
`/collect/*` requests.

### Release versioning

`app.version` at init time (from `VITE_APP_VERSION` or `package.json`) is
what ties errors to a release. The source map upload is tagged with the
same version. Without a consistent version string, the uploaded map and
the error stack never reconcile.

Use the git SHA or release tag; bake it into the build env.

### Nginx access logs (for containerised frontends)

Orthogonal to Faro but complementary: configure Nginx `log_format` as JSON,
ship to Loki via Alloy. Gives you server-side access logs
(`{service="costy-web"}`) alongside browser-side RUM data. Lets you see e.g.
"50% of requests 5xx at the edge" while Faro shows "failed fetch errors"
from the browser's perspective.
