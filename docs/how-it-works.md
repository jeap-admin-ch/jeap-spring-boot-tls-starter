# How it works

The starter does its work very early in the Spring Boot lifecycle, before the application context is
refreshed. It is not an `@AutoConfiguration` and contributes no beans — it only adds properties to the
environment so that Spring Boot's existing SSL support configures the embedded web server for TLS.

## The environment post-processor

`EnableTlsEnvPostprocessor` implements Spring Boot's `EnvironmentPostProcessor` and is registered in
`META-INF/spring.factories`:

```properties
org.springframework.boot.EnvironmentPostProcessor=\
  ch.admin.bit.jeap.tls.EnableTlsEnvPostprocessor
```

On `postProcessEnvironment` it:

1. checks `jeap.web.tls.enabled` (default `true`); if disabled, it does nothing;
2. resolves the hostname from `jeap.web.tls.self-signed-cert.hostname`, falling back to
   `spring.application.name`, then to `unknown`;
3. resolves the validity from `jeap.web.tls.self-signed-cert.days-valid` (default `3650` days);
4. generates a self-signed key/certificate pair (`PemKeyCertPair`);
5. adds an `spring.ssl.bundle.pem.web-server.*` keystore, sets `server.ssl.bundle=web-server` and
   `server.http2.enabled=true` in a new property source `jeap-tls-config`.

The pair is generated only once per post-processor instance, and the whole property source is added
only if `server.ssl.bundle` is not already set, so an explicitly configured SSL bundle takes
precedence.

## Certificate generation

`PemKeyCertPairFactory` builds the self-signed certificate with BouncyCastle (`bcpkix-jdk18on`):

- an RSA-2048 key pair (`SecureRandom`);
- a self-signed `X509v3` certificate signed with `SHA256withRSA`;
- the hostname as subject `CN` and as a `dNSName` subject alternative name;
- extensions: `basicConstraints` (CA = false), `keyUsage` (digital signature + key encipherment) and
  `extendedKeyUsage` (`serverAuth`).

Both the private key and the certificate are returned PEM-encoded in the `PemKeyCertPair` record
(`key`, `cert`), ready to feed into the PEM SSL bundle.

## HTTP/2

TLS is a precondition for HTTP/2 in practice (browsers only support HTTP/2 over TLS). Because the
starter enables TLS anyway, it also sets `server.http2.enabled=true` so the service benefits from the
more efficient HTTP/2 protocol.

## Related

- [Getting started](getting-started.md)
- [Configuration reference](configuration.md)
- [Actuator endpoints without TLS](actuator-endpoints.md)
- [jeap-spring-boot-tls-starter](../README.md)
