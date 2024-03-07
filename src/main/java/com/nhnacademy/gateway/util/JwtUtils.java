package com.nhnacademy.gateway.util;

import com.nhnacademy.gateway.dto.TokenPayLoad;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String jwtValue;

    public Claims getPayLoadValue(String accessToken) {
        Claims result = null;
        try {
            result = Jwts.parser()
                    .setSigningKey(jwtValue)
                    .parseClaimsJws(accessToken)
                    .getBody();

        } catch (SignatureException |
                 MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (
                ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (
                UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (
                IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return result;
    }

    public TokenPayLoad getPayLoad(String accessToken) {
        String userId = getPayLoadValue(accessToken)
                .get("userId", String.class);

        String roles = getPayLoadValue(accessToken)
                .get("roles", String.class);

        return new TokenPayLoad(userId, roles);
    }

    public boolean isValidateToken(String token) {
        log.info("{}",token);

        try {
            Jwts.parser().setSigningKey(jwtValue).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

}
