package pl.codepride.dailyadvisor.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import pl.codepride.dailyadvisor.gateway.service.RedisService;
import reactor.core.publisher.Mono;

/**
 * Security filter which provides JWT token validation.
 */
public class JwtFilter extends AbstractGatewayPreFilter {

    /**
     * JWT token cookie name.
     */
    @Value("${jwt.cookie}")
    @Getter
    @Setter
    private String jwtCookieName;

    /**
     * JWT token secret key.
     */
    @Value("${jwt.secret}")
    @Getter
    @Setter
    private String jwtSecret;

    /**
     * Redis service.
     */
    @Autowired
    private RedisService redisService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected Mono<Void> doFilter(ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpCookie jwtCookie = request.getCookies().getFirst(jwtCookieName);

        if(jwtCookie == null) {
            reject(request ,response, exchange, chain);
        }

        return checkJwt("").flatMap(aBoolean -> {
            if(aBoolean) {
                return accept(request, exchange, chain);
            }

            return reject(request, response, exchange, chain);
        });
    }

    /**
     * Validate JWT token with redis store.
     * @param token Token string value.
     * @return Mono with true boolean(valid) or false(invalid).
     */
    protected Mono<Boolean> checkJwt(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .parseClaimsJws(token);
            return redisService.checkIfKeyExists(jws.getBody().getId());
        } catch (JwtException | IllegalArgumentException e) {
            return Mono.just(false);
        }
    }

    /**
     * Accept request.
     * @param request Http request.
     * @param exchange Http exchange.
     * @param chain Filter chain.
     * @return Processing mono.
     */
    protected Mono<Void> accept(ServerHttpRequest request, ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate().request(request).build());
    }

    /**
     * Reject request.
     * @param request Http request.
     * @param response Http response.
     * @param exchange Http exchange.
     * @param chain Filter chain.
     * @return Processing mono.
     */
    protected Mono<Void> reject(ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        eraseJwt(response);
        return response.setComplete();
    }

    /**
     * Erase JWT cookie from http response.
     * @param response Http response.
     */
    protected void eraseJwt(ServerHttpResponse response) {
        response.addCookie(ResponseCookie.from(jwtCookieName, "").maxAge(0).build());
    }
}
