package pl.codepride.dailyadvisor.zuulgateway.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {

    public static String TOKEN_COOKIE_NAME = "_secu";

    public void registerJwt(String token);

    public boolean checkJwt(String token);

    public void dismissJwt(String token);
}
