package com.nhnacademy.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthorizationHeaderFilter extends AbstractGatewayFilterFactory<JwtAuthorizationHeaderFilter.Config> {

    public JwtAuthorizationHeaderFilter(){
        super(Config.class);
    }

    public static class Config {
        // application.properties 파일에서 지정한 filer의 Argument값을 받는 부분
    }

    @Override
    public GatewayFilter apply(Config config) {
       return  (exchange, chain)->{
           //TODO#3 JWT 검증 필터입니다.
           log.debug("jwt-validation-filter");
           ServerHttpRequest request = exchange.getRequest();

           if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
               //TODO#3-1 Header에 Authorization 존재하지 않는다면 적절한 예외처리를 합니다.
           }else{

               //TODO#3-2 AccessToken jjwt 라이브러리를 사용하여 검증 구현하기
               //이미 Token이 만료되었는지?
               //Token의 signature 값 검증(HMAC)
               //이미 로그아웃된 Token 인지? - Black List 관리
               //account-api의 JwtProperties를 참고하여 구현합니다.

               String accessToken = request.getHeaders().get(HttpHeaders.AUTHORIZATION).toString();
               log.debug("accessToken:{}",accessToken);

               //TODO#3-3 검증이 완료되면  Request header에 X-USER-ID를 등록합니다.
               //exchange.getRequest().getHeaders(); <-- imutable 합니다. 즉 수정 할 수 없습니다.
               //exchage.mutate()를 이용해야 합니다. 아래 코드를 참고하세요.

               exchange.mutate().request(builder -> {
                   builder.header("X-USER-ID","nhnacademy");
               });
           }

           return chain.filter(exchange);
        };
    }

}
