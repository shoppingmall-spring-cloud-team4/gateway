package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.filter.CustomGlobalFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FilterConfig {

    //TODO#2-3 global filter를 bean으로 등록합니다.
    @Bean
    public GlobalFilter customFilter() {
        return new CustomGlobalFilter();
    }

}
