package com.msh.eurka.server;

@SpringBootApplication
@EnableEurekaServer
public class EurkaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run( EurkaServerApplication.class, args );
    }
}
