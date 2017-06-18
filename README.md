# Membrane Discovery Spring Boot Starter

A Spring Boot Starter that adds service discovery and client-side load balancing to [Membrane Spring Boot Starter](https://github.com/membrane/membrane-spring-boot-starter).

## Usage

```java
@EnableMembrane
@EnableDiscoveryClient
@SpringBootApplication
public class MembraneEurekaApplication {
    @Bean
    public Proxies proxies() {
        return p -> p
            .serviceProxy(s -> s
                .matches(m -> m
                    .method(GET)
                    .pathPrefix("/jokes/"))
            .target(t -> t.url("discovery://chuck-norris")));
    }

    public static void main(String[] args) {
        SpringApplication.run(MembraneEurekaApplication.class, args);
    }
}
```
