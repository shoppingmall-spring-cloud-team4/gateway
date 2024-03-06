package com.nhnacademy.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class TokenPayLoad {
    private String userId;
    private String roles;
}
