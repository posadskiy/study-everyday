# Maven

## POM structure

### General project information

This section contains the project name, URL, sponsors, license information, developers, and contributors.

### Build settings

This section customizes behavior declared in the [Super POM](#super-pom). It describes locations of sources and tests.
You can also declare and configure custom plugins here, and bind plugin goals to the build lifecycle.

### Build environment

Build configuration settings.

### POM relationships

Dependencies, parent, and modules.

## POM

### POM inheritance

A project `pom.xml` inherits from the Maven Super POM by default. It contains declarations for:

- Maven Central repository (`repository`)
- plugin repositories (`pluginRepository`)
- source/test/output directories (`sourceDirectory`, `testSourceDirectory`, `outputDirectory`, etc.)
- plugin versions (`pluginManagement`)
- plugins bound to build phases (`plugins`)

### Super POM

The effective POM (current POM + all parents + root) can be viewed with:

`mvn help:effective-pom`

## Phases

| Phase | Plugin:goal |
| ------------- | ------------- |
| process-resources | resources:resources |
| compile | compiler:compile |
| process-test-resources | resources:testResources |
| test-compile | compiler:testCompile |
| test | surefire:test |
| package | jar:jar |
| install | install:install |
| deploy | deploy:deploy |

All phases: validate, initialize, generate-sources, process-sources, generate-resources, process-resources, compile,
process-classes, generate-test-sources, process-test-sources, generate-test-resources, process-test-resources,
test-compile, process-test-classes, test, prepare-package, package, pre-integration-test, integration-test,
post-integration-test, verify, install, deploy, pre-clean, clean, post-clean, pre-site, site, post-site, site-deploy.

### Profiles

Command with profile: `mvn <phase> -P <profile>`

## Dependencies

### Dependency scope

- **compile** — used for compilation and runtime (will be placed under `/lib`)
- **runtime** — used only at runtime (e.g., a JDBC driver)
- **provided** — used only at compile-time; expected to be provided at runtime (e.g., `servlet-api`)
- **test** — used only for compiling and running tests

### Dependency management

This is a way to manage both direct and transitive dependency versions. Use the `<dependencyManagement>` block: declare
dependencies **with versions** there. After that, you typically shouldn’t specify versions again in `<dependency>` blocks.

#### Dependency mediation

If `A -> B -> D:1.0` and `A -> C -> D:2.0`, Maven resolves the conflict by choosing the dependency that is closer to the
root of the dependency tree. If multiple candidates are at the same depth, the first declared wins.

#### IntelliJ Dependencies Diagram

In `pom.xml`, choose **Diagram → Show Dependencies** (`Ctrl+Alt+Shift+U`) to open the dependency diagram. Red arrows show
the paths to conflicting dependencies that will not be used.

#### Project dependencies

- `mvn dependency:resolve` prints dependencies as `[groupId:artifactId:version:phase]`
- `mvn dependency:tree` prints the same information in a tree form

#### Exclusion

To exclude a transitive dependency, add an `<exclusion>` block inside `<dependency>` with `<groupId>` and `<artifactId>`
(without version) of the dependency to exclude.

#### Optional

You can mark a dependency as optional. Example: `A -> B -> C` where `C` is optional. Then `C` will be pulled into `A`
only if you also declare `C` directly in `A`. Set `<optional>true</optional>`. This pattern is generally discouraged
because it can be confusing; splitting into separate modules is often clearer.

## Plugins

A Maven plugin provides goals.

### Exec Maven Plugin

**exec-maven-plugin** lets you run an external program or a jar by configuring it in `pom.xml` (e.g., `mainClass`).

## Other

### Tests

- Compile tests and put them into test output, but don’t run them: `-DskipTests`
- Skip tests entirely: `-Dmaven.test.skip=true`

### Tips and tricks

In multi-module projects, it’s best to inherit `groupId` and `version` from the parent:
`${groupId}` and `${version}`.

You can access all current project properties via `${project.*}` where `*` is the property name.

If Maven pulls artifacts from a remote repository instead of using local ones, delete the `_remote.repositories` file
in the local artifact directory.


