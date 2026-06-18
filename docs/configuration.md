# Configuration reference

All starter-specific properties use the prefix `jeap.web.tls`. Sensible defaults apply, so in most
cases no configuration is needed — adding the dependency is enough (see
[Getting started](getting-started.md)).

## Properties

| Property                                  | Default                                          | Description                                                                                              |
|-------------------------------------------|--------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| `jeap.web.tls.enabled`                    | `true`                                           | Enable (`true`) or disable (`false`) the jEAP TLS starter. When `false`, no SSL properties are added.   |
| `jeap.web.tls.self-signed-cert.hostname`  | `${spring.application.name}` (else `unknown`)    | Value used as the certificate's subject common name (`CN`) and as a DNS subject alternative name (SAN). |
| `jeap.web.tls.self-signed-cert.days-valid`| `3650` (10 years)                                | Number of days the generated self-signed certificate is valid.                                          |

```yaml
jeap:
  web:
    tls:
      enabled: true
      self-signed-cert:
        hostname: jme-example-service
        days-valid: 1100
```

A non-integer value for `days-valid` causes start-up to fail with an `IllegalArgumentException`.

## Spring properties set by the starter

When enabled, the starter adds the following Spring Boot properties to the environment (in a property
source named `jeap-tls-config`). You normally do not set these yourself:

| Property                                                   | Value set                          |
|------------------------------------------------------------|-------------------------------------|
| `spring.ssl.bundle.pem.web-server.keystore.private-key`    | The generated PEM private key       |
| `spring.ssl.bundle.pem.web-server.keystore.certificate`    | The generated PEM certificate       |
| `server.ssl.bundle`                                        | `web-server`                        |
| `server.http2.enabled`                                     | `true`                              |

If `server.ssl.bundle` is already configured, the starter logs a debug message and leaves the
environment untouched, so an explicitly configured SSL bundle always wins.

## Related

- [Getting started](getting-started.md)
- [How it works](how-it-works.md)
- [Actuator endpoints without TLS](actuator-endpoints.md)
- [jeap-spring-boot-tls-starter](../README.md)
