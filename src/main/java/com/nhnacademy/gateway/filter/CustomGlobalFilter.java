package com.nhnacademy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Slf4j
public class CustomGlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.debug("global filter");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        log.info("request id : {}", request.getId());
        XForwardedRemoteAddressResolver remoteAddressResolver = XForwardedRemoteAddressResolver.maxTrustedIndex(1);
        InetSocketAddress inetSocketAddress = remoteAddressResolver.resolve(exchange);
        log.info("request ip : {}", inetSocketAddress.getAddress().getHostAddress());

        return chain.filter(exchange)
                .then(Mono.fromRunnable(()->
                        log.info("response status : {}", response.getStatusCode())));
    }

    @Override
    public int getOrder() {
        return -1;
    }

}