package pl.codepride.dailyadvisor.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import pl.codepride.dailyadvisor.gateway.filter.CorsFilter;

@Configuration
public class GatewayConfiguration {

    @Value("${frontend.url.parent}")
    private String frontendUrl;

    @Bean
    public CorsFilter corsFilter() {
        return CorsFilter.builder()
                .allowedCredentials("true")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigin(frontendUrl)
                .build();
    }

    @Bean
    ReactiveRedisOperations<String, String> redisOperations(ReactiveRedisConnectionFactory factory) {

        RedisSerializationContext.RedisSerializationContextBuilder<String, String> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        return new ReactiveRedisTemplate<String, String>(factory, builder.build());
    }
}
