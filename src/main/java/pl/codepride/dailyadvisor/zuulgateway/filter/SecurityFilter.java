package pl.codepride.dailyadvisor.zuulgateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public abstract class SecurityFilter extends ZuulFilter {

    protected void rejectRequest() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(403);
        ctx.setResponseBody(null);
        ctx.setSendZuulResponse(false);
    }
}
