# Actuator endpoints without TLS

Enabling TLS on the embedded web server also enables it for the actuator endpoints by default, because
they share the same connector. Sometimes the actuator endpoints need to stay reachable over plain HTTP
— for example for a scrape target or a health probe that cannot use the self-signed certificate.

## Expose actuators on a separate port

To disable TLS for the actuators without affecting the application's other endpoints, expose the
actuator endpoints on a **different port** using Spring Boot's management server, then disable SSL on
that port:

```yaml
management:
  server:
    port: 52873
    ssl:
      enabled: false
```

With this configuration the application's endpoints keep serving HTTPS (with HTTP/2), while the
actuator endpoints on port `52873` are served over plain HTTP. The `management.server.ssl.enabled`
property only affects the management server, so the main `server.ssl.bundle` set by the starter is
left untouched.

## Related

- [Getting started](getting-started.md)
- [Configuration reference](configuration.md)
- [How it works](how-it-works.md)
- [jeap-spring-boot-tls-starter](../README.md)
