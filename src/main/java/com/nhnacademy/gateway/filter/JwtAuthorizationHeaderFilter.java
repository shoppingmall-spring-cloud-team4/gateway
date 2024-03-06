package com.nhnacademy.gateway.filter;

import com.nhnacademy.gateway.dto.TokenPayLoad;
import com.nhnacademy.gateway.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
@Slf4j
public class JwtAuthorizationHeaderFilter extends AbstractGatewayFilterFactory<JwtAuthorizationHeaderFilter.Config> {

    public JwtAuthorizationHeaderFilter() {
        super(Config.class);
    }


    @RequiredArgsConstructor
    public static class Config {
        private final JwtUtils jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("jwt-validation-filter");

            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                log.error("header 가 없는 사용자");
            } else {
                String accessToken = Objects.requireNonNull(request.getHeaders()
                        .get(HttpHeaders.AUTHORIZATION)).get(0).substring(7);
                if (!config.jwtUtils.isValidateToken(accessToken)) {
                    log.error("토큰이 유효하지 않습니다");
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }

                TokenPayLoad tokenPayLoad = config.jwtUtils.getPayLoad(accessToken);
                log.info("{}", tokenPayLoad);


                exchange.mutate().request(builder -> {
                    builder.header("X-USER-ID", tokenPayLoad.getUserId());
                });
            }

            return chain.filter(exchange);
        };
    }

}
