# AGENTS.md

Guidance for AI coding agents working **in this repository**. For how to *use* the library in a
consuming service, read [README.md](README.md) and the [docs/](docs/) folder instead.

## Project

jEAP Spring Boot TLS Starter is a small, multi-module Maven library that enables TLS on the embedded
web server of a Spring Boot application. When the starter is on the classpath it generates a
self-signed key/certificate pair at start-up, registers it as a Spring SSL bundle named `web-server`,
points `server.ssl.bundle` at it and enables HTTP/2. It is meant for encrypting already-authenticated
traffic (e.g. between an AWS ALB and its targets); it does not support externally provided certificates.

## Repository layout

```
pom.xml                                          # Parent POM (packaging=pom); declares the modules below
jeap-spring-boot-tls-starter/                    # The starter
  src/main/java/ch/admin/bit/jeap/tls/
    EnableTlsEnvPostprocessor.java               # EnvironmentPostProcessor: adds SSL bundle + http2 properties
    PemKeyCertPairFactory.java                   # Creates a self-signed RSA cert via BouncyCastle, PEM-encoded
    PemKeyCertPair.java                          # record(String key, String cert)
  src/main/resources/META-INF/
    spring.factories                             # Registers EnableTlsEnvPostprocessor
    additional-spring-configuration-metadata.json# Metadata for jeap.web.tls.enabled
jeap-spring-boot-tls-starter-it-webmvc/          # Integration tests on the Spring MVC stack (HTTPS succeeds, HTTP fails)
Jenkinsfile, publiccode.yml, CHANGELOG.md, LICENSE
```

## Build & test

```bash
./mvnw -pl jeap-spring-boot-tls-starter -am install   # build the starter and its dependencies
./mvnw verify                                          # full build incl. integration tests
```

- Parent: `ch.admin.bit.jeap:jeap-internal-spring-boot-parent` (Spring Boot 4 aligned).
- The starter is wired through an `EnvironmentPostProcessor` (registered in
  `META-INF/spring.factories`), **not** through `@AutoConfiguration` — there are no beans, only
  environment properties added before the context refreshes.
- Unit tests use `MockEnvironment`; integration tests in `jeap-spring-boot-tls-starter-it-webmvc` boot
  a real server on a random port and call it over HTTP (must fail) and HTTPS (must succeed).

## jEAP conventions

- Java packages live under `ch.admin.bit.jeap.tls`.
- Configuration properties use the prefix `jeap.web.tls.*`.
- The starter sets, but does not own, the Spring properties `spring.ssl.bundle.pem.web-server.*`,
  `server.ssl.bundle` and `server.http2.enabled`. It backs off if `server.ssl.bundle` is already set.
- Certificate generation uses BouncyCastle (`bcpkix-jdk18on`): RSA-2048, `SHA256withRSA`, with
  `serverAuth` extended key usage and the hostname as both CN and DNS subject alternative name.

## Docs

When changing public behaviour, update the matching focused file under [docs/](docs/) (one topic per
file) and the documentation index in the README.

## Versioning

- Semantic Versioning; all changes documented in [CHANGELOG.md](./CHANGELOG.md) (Keep a Changelog format).
- `setPomVersions.sh` updates the version across all module POMs.
- When working on a feature branch, increase the version to `x.y.z-SNAPSHOT` in the POMs and keep the
  `-SNAPSHOT` postfix; CI removes it when releasing. Do not use `-SNAPSHOT` elsewhere (CHANGELOG,
  publiccode.yml).
- Keep changelog entries concise and follow existing patterns.
- Use the JIRA ID from the branch name as the commit message prefix (e.g. `JEAP-1234 Added feature X`);
  do not use conventional commits.
- When bumping the version, also update the changelog and the version/date in `publiccode.yml`.
