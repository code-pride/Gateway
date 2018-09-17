package pl.codepride.dailyadvisor.gateway.filter;

import lombok.Builder;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Cors filter.
 */
@Data
@Builder
public class CorsFilter extends AbstractGatewayPreFilter {

    /**
     * Allowed headers.
     */
    @Builder.Default()
    private String allowedHeaders = "*";

    /**
     * Allowed methods.
     */
    @Builder.Default()
    private String allowedMethods = "*";

    /**
     * Allowed origin.
     */
    @Builder.Default()
    private String allowedOrigin = "*";

    /**
     * Max age.
     */
    @Builder.Default()
    private String maxAge = "3600";

    /**
     * Allow credentials.
     */
    @Builder.Default()
    private String allowedCredentials = "true";

    /**
     * {@inheritDoc}
     */
    @Override
    protected Mono<Void> doFilter(ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {
        if (CorsUtils.isCorsRequest(request)) {
            HttpHeaders headers = response.getHeaders();
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin);
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allowedMethods);
            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, maxAge);
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowedHeaders);
            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, allowedCredentials);
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }
}
