package pl.codepride.dailyadvisor.zuulgateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.servlet.http.HttpServletRequest;

public class SimpleFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();

        return true;
    }

    @Override
    public Object run() {
        /*RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        redisTemplate.opsForValue().set("zuul","nothing");
        String z = redisTemplate.opsForValue().get("zuul");
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        if(redisTemplate.hasKey("zuul")) {
            log.info("Jest");
        }
        redisTemplate.delete("zuul");
        if(!redisTemplate.hasKey("zuul")) {
            log.info("Nie ma");
        }*/
        return null;
    }

}
