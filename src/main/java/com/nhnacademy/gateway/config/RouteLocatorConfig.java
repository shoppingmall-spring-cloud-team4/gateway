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
import com.nhnacademy.gateway.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RouteLocatorConfig {

    private final JwtAuthorizationHeaderFilter jwtAuthorizationHeaderFilter;
    private final JwtUtils jwtUtils;


    @Bean
    public RouteLocator myRoute(RouteLocatorBuilder builder ) {


        return builder.routes()
                .route("login",p->p.path("/login")
                        .uri("http://localhost:8100")
                )
                .route("refresh",p-> p.path("/auth/refresh")
                        .uri("http://localhost:8100")
                )
                .route("account-api", p->p.path("/api/account/**")
                        .filters(f->f.filter(jwtAuthorizationHeaderFilter.apply(new JwtAuthorizationHeaderFilter.Config(jwtUtils))))
                        .uri("http://localhost:8100")
                )
                .route("shoppingmall-api", p->p.path("/api/shop/**")
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
