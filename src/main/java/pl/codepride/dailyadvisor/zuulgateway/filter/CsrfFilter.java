package pl.codepride.dailyadvisor.zuulgateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.util.UrlUtils;
import pl.codepride.dailyadvisor.zuulgateway.service.HttpJwtService;
import pl.codepride.dailyadvisor.zuulgateway.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CsrfFilter extends SecurityFilter {

    private static Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    private CsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

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
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        CsrfToken csrfToken = this.tokenRepository.loadToken(request);
        final boolean missingToken = csrfToken == null;
        if (missingToken) {
            csrfToken = this.tokenRepository.generateToken(request);
            this.tokenRepository.saveToken(csrfToken, request, response);
        }

        String actualToken = request.getHeader(csrfToken.getHeaderName());
        if (actualToken == null) {
            actualToken = request.getParameter(csrfToken.getParameterName());
        }
        if (!csrfToken.getToken().equals(actualToken)) {
            rejectRequest();
        }
        return null;
    }
}
