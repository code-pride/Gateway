package pl.codepride.dailyadvisor.zuulgateway.service.impl;

import pl.codepride.dailyadvisor.zuulgateway.service.HttpJwtService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class HttpJwtServiceImpl implements HttpJwtService {

    private static final String TOKEN_COOKIE_NAME = "_secu";
    private static final String COOKIE_HEADER = "Set-Cookie";

    @Override
    public Optional<String> extractJwt(HttpServletResponse response) {
        String cookieHeader = response.getHeader(COOKIE_HEADER);
        if(cookieHeader == null || cookieHeader.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(Arrays.asList(cookieHeader.split(","))
                .stream()
                 .filter(s -> s.startsWith(TOKEN_COOKIE_NAME))
                 .findFirst().map(s -> s.split("="))
                 .get()[1]);
    }

    @Override
    public Optional<String> extractJwt(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return Optional.empty();
        }
        Optional<Cookie> cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(TOKEN_COOKIE_NAME))
                .findFirst();
        if(cookie.isPresent()) {
            return Optional.ofNullable(cookie.get().getValue());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteJwt(HttpServletResponse response) {
        Cookie cookieToDelete = new Cookie(TOKEN_COOKIE_NAME,"");
        cookieToDelete.setMaxAge(0);
        response.addCookie(cookieToDelete);
    }
}
