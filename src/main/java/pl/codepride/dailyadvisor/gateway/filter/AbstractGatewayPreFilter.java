package pl.codepride.dailyadvisor.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Convenience class for filter.
 */
public abstract class AbstractGatewayPreFilter extends AbstractGatewayFilterFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            return doFilter(request, response, exchange, chain);
        };
    }

    /**
     * Perform filter operation.
     *
     * @param request  Http request.
     * @param response Http response.
     * @param exchange Server exchange.
     * @param chain    Filter chain.
     * @return Empty mono.
     */
    protected abstract Mono<Void> doFilter(
            ServerHttpRequest request,
            ServerHttpResponse response,
            ServerWebExchange exchange,
            GatewayFilterChain chain
    );
}
