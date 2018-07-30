package pl.codepride.dailyadvisor.zuulgateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.codepride.dailyadvisor.zuulgateway.service.HttpJwtService;
import pl.codepride.dailyadvisor.zuulgateway.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends SecurityFilter {
    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    @Autowired
    private HttpJwtService httpJwtService;

    @Autowired
    private JwtService jwtService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
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
        HttpServletRequest request = ctx.getRequest();
        httpJwtService.extractJwt(request).ifPresent(s -> {
            if(!jwtService.checkJwt(s)) {
                rejectRequest();
            }
        });
        return null;
    }
}
