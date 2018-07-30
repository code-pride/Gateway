package pl.codepride.dailyadvisor.zuulgateway.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface HttpJwtService {
    public Optional<String> extractJwt(HttpServletResponse response);

    public Optional<String> extractJwt(HttpServletRequest request);

    public void deleteJwt(HttpServletResponse response);
}
