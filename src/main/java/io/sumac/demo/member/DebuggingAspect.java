package io.sumac.demo.member;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@Slf4j
@Profile("debug")
public class DebuggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller() throws Throwable {
        // no-op
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.ControllerAdvice *)")
    public void controllerAdvice() throws Throwable {
        // no-op
    }

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void service() throws Throwable {
        // no-op
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repository() throws Throwable {
        // no-op
    }

    @Before("controller() || controllerAdvice() || service() || repository()")
    public void enterController(JoinPoint joinPoint) {
        log.debug("Entering {}", joinPoint.toString());
    }

    @AfterReturning("controller() || controllerAdvice() || service() || repository()")
    public void leaveController(JoinPoint joinPoint) {
        log.debug("Leaving {}", joinPoint.toString());
    }

    @AfterThrowing("controller() || controllerAdvice() || service() || repository()")
    public void leaveControllerException(JoinPoint joinPoint) {
        log.debug("Exception {}", joinPoint.toString());
    }

}
