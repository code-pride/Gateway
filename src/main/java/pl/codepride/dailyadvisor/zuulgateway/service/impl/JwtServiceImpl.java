package pl.codepride.dailyadvisor.zuulgateway.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import pl.codepride.dailyadvisor.zuulgateway.service.JwtService;
import pl.codepride.dailyadvisor.zuulgateway.service.RedisService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private RedisService redisService;

    @Override
    public void registerJwt(String token) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(jwtSecret.getBytes())
                .parseClaimsJws(token);
        redisService.saveKey(jws.getBody().getId());
    }

    @Override
    public boolean checkJwt(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token);
            return redisService.checkIfKeyExists(jws.getBody().getId());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void dismissJwt(String token) {
        redisService.deleteKey(token);
    }
}
