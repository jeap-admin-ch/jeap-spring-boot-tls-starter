# Getting started

This page shows how to enable TLS on the embedded web server of a Spring Boot service by adding the
`jeap-spring-boot-tls-starter`. For the underlying mechanism see [How it works](how-it-works.md); for
the available properties see the [Configuration reference](configuration.md).

## 1. Add the dependency

```xml
<dependency>
    <groupId>ch.admin.bit.jeap</groupId>
    <artifactId>jeap-spring-boot-tls-starter</artifactId>
</dependency>
```

The version is managed by the jEAP Spring Boot parent. The starter requires an embedded web server,
so the service must already depend on a web stack such as `spring-boot-starter-webmvc`.

## 2. That is it

No code or configuration is required. Once the starter is on the classpath, TLS is enabled by
default. At start-up the service:

- generates a self-signed RSA certificate for its hostname,
- registers it as the Spring SSL bundle `web-server` and points `server.ssl.bundle` at it,
- enables HTTP/2 (`server.http2.enabled=true`).

The service is then reachable over `https://` only; plain `http://` requests are rejected.

## 3. When to use it

The generated certificate is **self-signed**: it provides encryption but not trust, because it is not
issued by a trusted certificate authority. Restrict the starter to use cases where traffic is already
authenticated by other means and only needs to be additionally encrypted — for example traffic
between an AWS Application Load Balancer (ALB) and its targets.

Externally provided certificates are not supported. If you need a CA-issued certificate, configure
Spring Boot's `server.ssl.*` / SSL bundle support directly instead of using this starter (the starter
backs off when `server.ssl.bundle` is already set).

## 4. Tune the certificate (optional)

```yaml
jeap:
  web:
    tls:
      enabled: true
      self-signed-cert:
        hostname: jme-example-service
        days-valid: 1100
```

See the [Configuration reference](configuration.md) for defaults and descriptions.

## Related

- [Configuration reference](configuration.md)
- [How it works](how-it-works.md)
- [Actuator endpoints without TLS](actuator-endpoints.md)
- [jeap-spring-boot-tls-starter](../README.md)
