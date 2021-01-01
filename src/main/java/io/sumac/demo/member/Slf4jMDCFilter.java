package io.sumac.demo.member;

import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(0)
public class Slf4jMDCFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token;
            if (!StringUtils.isEmpty(httpServletRequest.getHeader(ApplicationConstants.REQUEST_CORROLATION_HEADER))) {
                token = httpServletRequest.getHeader(ApplicationConstants.REQUEST_CORROLATION_HEADER);
            } else {
                token = UUID.randomUUID().toString();
            }
            MDC.put(ApplicationConstants.REQUEST_CORROLATION_HEADER, token);
            httpServletResponse.addHeader(ApplicationConstants.REQUEST_CORROLATION_HEADER, token);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            MDC.remove(ApplicationConstants.REQUEST_CORROLATION_HEADER);
        }
    }
}
