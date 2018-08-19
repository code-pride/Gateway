package pl.codepride.dailyadvisor.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

/**
 * Csrf filter.
 */
public class CsrfFilter extends AbstractGatewayPreFilter {

    /**
     * Csrf token cookie name.
     */
    private static String CSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";

    /**
     * Csrf header cookie name.
     */
    private static String CSRF_TOKEN_HEADER_NAME = "X-XSRF-TOKEN";

    /**
     * {@inheritDoc}
     */
    @Override
    protected Mono<Void> doFilter(ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {
        Optional csrfCookie = extractCsrfCookie(request);
        if(csrfCookie.isPresent() && csrfCookie.equals(extractCsrfHeader(request))) {
            return chain.filter(exchange.mutate().request(request).build());
        }

        storeNewCsrfToken(response);

        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * Extract CSRF token from cookie.
     * @param request Http request.
     * @return Optional of CSRF token.
     */
    private Optional<String> extractCsrfCookie(ServerHttpRequest request) {

        HttpCookie csrfTokenCookie = request.getCookies().getFirst(CSRF_TOKEN_COOKIE_NAME);
        if(csrfTokenCookie == null) {
            return Optional.empty();
        }

        String csrfToken = csrfTokenCookie.getValue();
        return csrfToken.isEmpty() ? Optional.empty() : Optional.ofNullable(csrfToken);
    }

    /**
     * Extract CSRF token from cookie.
     * @param request Http request.
     * @return Optional of CSRF token.
     */
    private Optional<String> extractCsrfHeader(ServerHttpRequest request) {

        String csrfHeader = request.getHeaders().getFirst(CSRF_TOKEN_HEADER_NAME);
        if(csrfHeader == null) {
            return Optional.empty();
        }

        return csrfHeader.isEmpty() ? Optional.empty() : Optional.ofNullable(csrfHeader);
    }

    /**
     * Store new http cookie in response.
     * @param response Http response.
     */
    private void storeNewCsrfToken(ServerHttpResponse response) {
        ResponseCookie csrfCookie = ResponseCookie
                .from(CSRF_TOKEN_COOKIE_NAME, UUID.randomUUID().toString())
                .httpOnly(false)
                .build();
    }
}
