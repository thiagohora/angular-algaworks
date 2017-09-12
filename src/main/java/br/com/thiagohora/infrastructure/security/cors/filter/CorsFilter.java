package br.com.thiagohora.infrastructure.security.cors.filter;

import br.com.thiagohora.infrastructure.security.config.property.ApiPropertyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends GenericFilterBean {

    private final ApiPropertyConfiguration configuration;

    @Autowired
    public CorsFilter(ApiPropertyConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String originPermitted = configuration.getAllowedOrigins();

        response.addHeader("Access-Control-Allow-Origin", originPermitted);
        response.addHeader("Access-Control-Allow-Credentials", "true");

        if("OPTIONS".equals(request.getMethod()) && originPermitted.equals(request.getHeader("Origin"))) {
            response.addHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.addHeader("Access-Control-Max-Age", "3600");
            response.setStatus(SC_OK);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
