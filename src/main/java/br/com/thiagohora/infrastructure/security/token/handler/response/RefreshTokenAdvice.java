package br.com.thiagohora.infrastructure.security.token.handler.response;

import br.com.thiagohora.infrastructure.security.config.property.ApiPropertyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RefreshTokenAdvice implements ResponseBodyAdvice<OAuth2AccessToken> {

    private final ApiPropertyConfiguration configuration;

    @Autowired
    public RefreshTokenAdvice(ApiPropertyConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken oAuth2AccessToken, MethodParameter methodParameter,
                                             MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                             ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        final HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        final HttpServletResponse response = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
        final String refreshToken = oAuth2AccessToken.getRefreshToken().getValue();

        createCookie(request, response, refreshToken);

        final DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) oAuth2AccessToken;

        accessToken.setRefreshToken(null);

        return accessToken;
    }

    private void createCookie(final HttpServletRequest request, final HttpServletResponse response, final String refreshToken) {
        final Cookie cookie = new Cookie("refresh_token", refreshToken);
        
        cookie.setHttpOnly(true);
        cookie.setSecure(configuration.isEnableHttps());
        cookie.setPath(String.format("%s%s", request.getContextPath(),"/oauth/token"));
        cookie.setMaxAge(86400);

        response.addCookie(cookie);
    }
}
