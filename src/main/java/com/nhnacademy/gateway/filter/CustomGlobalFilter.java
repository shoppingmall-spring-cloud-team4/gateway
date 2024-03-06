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

    //TODO#2 global filter입니다.
    //특정 url에 상관없이 전역적으로 동작합니다.

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //TODO#2-1 모든 요청에 대해서 global filter로그가 출력됩니다.
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
        //TODO#2-2 Ordered interface를 구현함으로써 filter의 우선순위를 설정할 수 있습니다.
        return -1;
    }

}