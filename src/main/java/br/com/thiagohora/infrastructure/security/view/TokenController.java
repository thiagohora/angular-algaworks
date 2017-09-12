package br.com.thiagohora.infrastructure.security.view;

import br.com.thiagohora.infrastructure.security.config.property.ApiPropertyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

@RestController
@RequestMapping("token")
public class TokenController {

    private final ApiPropertyConfiguration configuration;

    @Autowired
    public TokenController(ApiPropertyConfiguration configuration) {
        this.configuration = configuration;
    }

    @DeleteMapping("/revoke")
    public void revoke(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(configuration.isEnableHttps());
        cookie.setPath(request.getContextPath()+"/aouth/token");
        cookie.setMaxAge(0);

        response.setStatus(SC_NO_CONTENT);
        response.addCookie(cookie);
    }

}
