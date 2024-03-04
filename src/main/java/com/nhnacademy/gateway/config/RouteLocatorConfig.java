/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.filter.JwtAuthorizationHeaderFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : marco@nhnacademy.com
 * @Date : 25/05/2023
 */
@RequiredArgsConstructor
@Configuration
public class RouteLocatorConfig {

    private final JwtAuthorizationHeaderFilter jwtAuthorizationHeaderFilter;


    @Bean
    public RouteLocator myRoute(RouteLocatorBuilder builder ) {
        //TODO#1 router설정, gateway는 모든 요청의 진입점 입니다.


        return builder.routes()
                //TODO#1-1 localhost:8000/api/account/** 요청은 -> localhost:8100/api/account/** 라우팅 됩니다.
                .route("account-api", p->p.path("/api/account/**")
                        //TODO#1-3 jwt를 검증할 Filter를 등록합니다
                        //해당 필터는 account-api 서비스에만 적용됩니다.
                        .filters(f->f.filter(jwtAuthorizationHeaderFilter.apply(new JwtAuthorizationHeaderFilter.Config())))
                        .uri("http://localhost:8100")
                )
                //TODO#1-2 shoppingmall-api 서버는 포트{8200,8300} 라운드로빈 방식으로(50:50 비율로) 로드밸런싱 됩니다.
                .route("shoppingmall-api", p->p.path("/api/shop/**")
                        //TODO#1-4 shoppingmall-api 서버에 jwt 검증이 필요하다면 설정해주세요.
                        .and()
                        .weight("shoppingmall-api",50)
                        .uri("http://localhost:8200")
                )
                .route("shoppingmall-api", p->p.path("/api/shop/**").
                        and()
                        .weight("shoppingmall-api",50)
                        .uri("http://localhost:8300"))
                .build();

    }
}
