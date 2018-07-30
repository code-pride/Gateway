package pl.codepride.dailyadvisor.zuulgateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import pl.codepride.dailyadvisor.zuulgateway.service.HttpJwtService;
import pl.codepride.dailyadvisor.zuulgateway.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    @Autowired
    private HttpJwtService httpJwtService;

    @Autowired
    private JwtService jwtService;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();

        if ((ctx.get("proxy") != null) && ctx.get("proxy").equals("api")) {
            return true;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        httpJwtService.extractJwt(response).ifPresent(s -> jwtService.registerJwt(s));
        return null;
    }
}
