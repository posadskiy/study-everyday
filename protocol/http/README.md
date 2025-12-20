### HTTP

#### POST and PUT

Both methods are used for creating or updating entities. The difference is that **PUT is idempotent**. In other words,
no matter how many times you apply PUT operations to the same entity, the result should be the same.

**POST** is best when the back end encapsulates logic that can change the entity differently from call to call. For
example, creating an entity where the server generates the `id` — that is POST.

**PUT** is best for updating entities when there’s no encapsulated back-end logic involved. For example, updating an
entity when you already know its `id`.

#### Response codes

##### 1xx — informational

##### 2xx — success

- `200 OK` — general success response
- `201 Created` — the request created a resource; its address can be returned in the body or in the `Location` header
- `202 Accepted` — the request was accepted; the client doesn’t have to wait for completion (processing may be long)
- `203 Non-Authoritative Information` — OK, but the response may be stale because it came from a third party (e.g. another server)
- `204 No Content` — no content in the response, only headers

##### 3xx — redirection

- `301 Moved Permanently` — resource moved permanently to the URL in `Location`
- `302 Moved Temporarily` — resource moved temporarily to the URL in `Location`

##### 4xx — client error

- `400 Bad Request` — request syntax error
- `401 Unauthorized` — only authorized clients can access; response should include `WWW-Authenticate` describing auth requirements.
  A subsequent request may include `Authorization` with the required credentials.
- `403 Forbidden` — client is authenticated but not allowed to access the resource
- `404 Not Found` — resource not found
- `405 Method Not Allowed` — method not allowed for the resource. Server should list allowed methods in the `Allow` header.

##### 5xx — server error

- `500 Internal Server Error` — generic server error that doesn’t fit other categories
- `501 Not Implemented` — server does not understand the method. If the method is understood but not supported for the resource, return `405`.
- `502 Bad Gateway` — gateway/proxy received an invalid response from an upstream server
- `503 Service Unavailable` — server temporarily cannot process requests; may include `Retry-After` with a suggested retry time
- `504 Gateway Timeout` — gateway/proxy did not receive a response from an upstream server in time


