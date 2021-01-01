package io.sumac.demo.member;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
public class AccessLogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        var start = Instant.now();
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        var finish = Instant.now();
        var method = httpServletRequest.getMethod();
        var resource = httpServletRequest.getRequestURI();
        var status = httpServletResponse.getStatus();
        var duration = Duration.between(start, finish).toNanos();
        log.info("{} {} {} {}", status, method, resource, duration);
    }
}
