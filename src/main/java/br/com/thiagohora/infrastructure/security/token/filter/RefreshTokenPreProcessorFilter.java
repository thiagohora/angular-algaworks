package br.com.thiagohora.infrastructure.security.token.filter;

import org.apache.catalina.servlet4preview.http.HttpServletRequestWrapper;
import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessorFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;

        if(isARefreshTokenCall(request)) {

            final String refreshToken = Stream.of(request.getCookies())
                                            .filter(cookie ->  "refresh_token".equals(cookie.getName()))
                                            .findFirst()
                                            .map(Cookie::getValue).orElse("");

            filterChain.doFilter(new ServletRequestWrapper(request, refreshToken), servletResponse);
        }

        filterChain.doFilter(request, servletResponse);
    }

    private boolean isARefreshTokenCall(HttpServletRequest request) {
        return "/oauth/token".equalsIgnoreCase(request.getRequestURI())
                && "refresh_token".equals(request.getParameter("grant_type"))
                && request.getCookies() != null;
    }

    static class ServletRequestWrapper extends HttpServletRequestWrapper {

        private final String refreshToken;

        public ServletRequestWrapper(final HttpServletRequest request, final String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            final ParameterMap<String, String[]> parameterMap = new ParameterMap<>(getRequest().getParameterMap());
            parameterMap.put("refresh_token", new String[]{ refreshToken });
            parameterMap.setLocked(true);
            return parameterMap;
        }
    }

}
