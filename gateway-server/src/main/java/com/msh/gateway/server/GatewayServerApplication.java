package com.msh.gateway.server;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class GatewayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run( EntoreGatewayApplication.class, args );
    }
}
