package pl.codepride.dailyadvisor.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Jwt filter which passes requests with invalid JWT.
 */
@Component
public class PassingJwtFilter extends JwtFilter{

    @Override
    protected Mono<Void> reject(ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {
        return accept(eraseJwt(request), exchange, chain);
    }

    /**
     * Decorate http request with erased JWT cookie.
     * @param request Http request.
     * @return Decorated http request.
     */
    protected ServerHttpRequest eraseJwt(ServerHttpRequest request) {
        return request.mutate().headers(headers -> {
            if(headers == null) {
                return;
            }

            String cookieHeader = headers.getFirst(HttpHeaders.COOKIE);

            if(cookieHeader == null || cookieHeader.isEmpty()) {
                return;
            }

            headers.set(HttpHeaders.COOKIE, eraseJwtFromCookieHeader(cookieHeader));
        }).build();
    }

    /**
     * Remove JWT cookie from http request.
     * @param cookieHeader Cookies header.
     * @return Cleaned cookies header.
     */
    private String eraseJwtFromCookieHeader(String cookieHeader) {
        List<String> jwtCookieValues = Arrays.asList(cookieHeader.split(";"))
                .stream()
                .filter(s -> s.trim().startsWith(this.getJwtCookieName())).collect(Collectors.toList());
        if(jwtCookieValues != null) {
            for(String jwtCookieValueString : jwtCookieValues) {
                cookieHeader = cookieHeader.replace(jwtCookieValueString, "");
            }
        }

        return cookieHeader;
    }
}
