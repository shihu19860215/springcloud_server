package com.msh.gateway.server.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.PrefixPathGatewayFilterFactory;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * @version 1.0
 * @author: wangqiaobin
 * @date : 2018/8/3
 */
@Configuration
public class PrefixPathGatewayFilter extends PrefixPathGatewayFilterFactory {
    @Override
    public String name() {
        return "global_prefix_path";
    }

    @Override
    public GatewayFilter apply(Config config) {
        config.setPrefix("/api");
        return super.apply(config);
    }

    @Override
    public GatewayFilter apply(Consumer<Config> consumer) {
        return super.apply(consumer);
    }
}
