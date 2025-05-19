## jEAP Spring Boot TLS Starter
jEAP Spring Boot TLS Starter is a Spring Boot starter for TLS configuration.
It activates TLS on the Spring Boot webserver, and provides the option to generate a
certificate at startup (suitable for encrytion beetn AWS ALB and the Spring Boot app).

## Changes

This library is versioned using [Semantic Versioning](http://semver.org/) and all changes are documented in
[CHANGELOG.md](./CHANGELOG.md) following the format defined in [Keep a Changelog](http://keepachangelog.com/).

## Note

This repository is part the open source distribution of jEAP. See [github.com/jeap-admin-ch/jeap](https://github.com/jeap-admin-ch/jeap)
for more information.

## Attributions

This project includes code from the following open-source projects:

1. **[Spring Framework]**  
   Link: [https://https://github.com/spring-projects](https://github.com/spring-projects)  
   License: Apache 2.0
   Included Code: jEAP is based on the Spring Framework and Spring Boot. Small code snippets from the Spring Framework
   are included in this project, namely in the jeap-spring-boot-config-starter.            
   Changes: Minor modifications to fit project requirements.
2. **[Spring Cloud AWS]**  
   Repository: [https://github.com/awspring/spring-cloud-aws](https://github.com/awspring/spring-cloud-aws)  
   License: Apache 2.0
   Included Code: The AppConfig and Secrets Manager integrations are partially based on Spring Cloud AWS code.

## License

This repository is Open Source Software licensed under the [Apache License 2.0](./LICENSE).
