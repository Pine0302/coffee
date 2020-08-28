package geektime.spring.data.datasourcedemo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    //@Around("execution(* geektime.spring.data.datasourcedemo.repository..*(..))")
    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {
        Long starttime = System.currentTimeMillis();
        String name = "-";
        String result = "Y";
        try {
            name = pjp.getSignature().toShortString();
            return pjp.proceed();
        } catch (Throwable t) {
            result = "N";
            return t;
        } finally {
            Long endtime = System.currentTimeMillis();
            log.info("{}:{};{}:ms", name, result, endtime - starttime);
        }
    }

    @Pointcut("execution(* geektime.spring.data.datasourcedemo.repository..*(..))")
    public void repositoryOps(){

    }
}
