# jEAP Spring Boot TLS Starter

jEAP Spring Boot TLS Starter is a Spring Boot starter that enables TLS on the embedded web server of
a jEAP service simply by being on the classpath. At application start-up it generates a self-signed
key/certificate pair on the fly, wires it into Spring Boot via an SSL bundle, and enables HTTP/2. It
is intended for use cases where already-authenticated traffic must additionally be encrypted, for
example between an AWS Application Load Balancer (ALB) and its targets. It provides:

* TLS activation on the embedded web server with no application code changes
* A self-signed certificate created at start-up (no externally provided certificate needed)
* Automatic HTTP/2 support once TLS is enabled
* A small set of `jeap.web.tls.*` properties to tune hostname, validity and enablement
* The option to keep actuator endpoints reachable without TLS on a separate port

## Documentation

Start with [Getting started](docs/getting-started.md), then follow the links below.

| Topic                                                  | File                                                       |
|--------------------------------------------------------|------------------------------------------------------------|
| Getting started (add the dependency, run over HTTPS)   | [docs/getting-started.md](docs/getting-started.md)         |
| Configuration reference (`jeap.web.tls.*`)             | [docs/configuration.md](docs/configuration.md)             |
| How it works (environment post-processor, SSL bundle)  | [docs/how-it-works.md](docs/how-it-works.md)               |
| Actuator endpoints without TLS                         | [docs/actuator-endpoints.md](docs/actuator-endpoints.md)   |

## Modules

Group id for all modules is `ch.admin.bit.jeap`; the version is managed by the jEAP Spring Boot
parent. Consumers depend on `jeap-spring-boot-tls-starter`.

| Module                                  | Purpose                                                                   |
|-----------------------------------------|---------------------------------------------------------------------------|
| `jeap-spring-boot-tls-starter`          | The starter: TLS environment post-processor, self-signed certificate factory |
| `jeap-spring-boot-tls-starter-it-webmvc`| Integration tests verifying TLS on the Spring MVC web stack               |

## Changes

This library is versioned using [Semantic Versioning](http://semver.org/) and all changes are documented in
[CHANGELOG.md](./CHANGELOG.md) following the format defined in [Keep a Changelog](http://keepachangelog.com/).

## Note

This repository is part the open source distribution of jEAP. See [github.com/jeap-admin-ch/jeap](https://github.com/jeap-admin-ch/jeap)
for more information.

## License

This repository is Open Source Software licensed under the [Apache License 2.0](./LICENSE).
