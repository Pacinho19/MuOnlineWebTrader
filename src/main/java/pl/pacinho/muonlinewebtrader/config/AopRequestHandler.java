package pl.pacinho.muonlinewebtrader.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Aspect
@Configuration
public class AopRequestHandler {

    @Around("within(@org.springframework.stereotype.Controller *)")
    public Object logRequest(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = point.proceed();
        long stop = System.currentTimeMillis();
        System.out.println(point.getSignature().getName() + " duration " + (stop - start));
        return proceed;
    }
}
