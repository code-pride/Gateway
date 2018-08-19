package pl.codepride.dailyadvisor.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

/**
 * Jwt filter which passes requests with invalid JWT.
 */
public class PassingJwtFilter extends JwtFilter{

    @Override
    protected Mono<Void> reject(ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        eraseJwt(response);
        return response.setComplete();
    }

    /**
     * Decorate http request with erased JWT cookie.
     * @param request Http request.
     * @return Decorated http request.
     */
    protected ServerHttpRequest eraseJwt(ServerHttpRequest request) {
        String cookieHeader = request.getHeaders().getFirst(HttpHeaders.COOKIE);

        if(cookieHeader == null || cookieHeader.isEmpty()) {
            return request;
        }

        return request.mutate().header(HttpHeaders.COOKIE, eraseJwtFromCookieHeader(cookieHeader)).build();
    }

    /**
     * Remove JWT cookie from http request.
     * @param cookieHeader Cookies header.
     * @return Cleaned cookies header.
     */
    private String eraseJwtFromCookieHeader(String cookieHeader) {
        Optional<String> jwtCookieValueString = Arrays.asList(cookieHeader.split(","))
                .stream()
                .filter(s -> s.startsWith(this.getJwtCookieName()))
                .findFirst();
        if(jwtCookieValueString.isPresent()) {
            cookieHeader.replace(jwtCookieValueString.get(), "");
        }

        return cookieHeader;
    }
}
