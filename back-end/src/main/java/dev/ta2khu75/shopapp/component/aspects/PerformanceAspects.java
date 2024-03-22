package dev.ta2khu75.shopapp.component.aspects;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Component
@Aspect
public class PerformanceAspects {
  private Logger logger=Logger.getLogger(getClass().getName());
  //@Pointcut("within(dev.ta2khu75.shopapp.controller.*)")
  //@Pointcut("within(dev.ta2khu75.shopapp.controller.ControllerCategory)")
  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void controllerMethods(){}
  private String getMethodName(JoinPoint joinPoint){
    return joinPoint.getSignature().getName();
  }
  @Before("controllerMethods()")
  public void beforeMethodExecution(JoinPoint joinPoint){
    logger.info("Starting execution of "+getMethodName(joinPoint));
  }
  @After("controllerMethods()")
  public void afterMethodExecution(JoinPoint joinPoint){
    logger.info("finished execution of "+getMethodName(joinPoint));
  }
  @Around("controllerMethods()")
  public Object measureControllerMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
    long start=System.nanoTime();
    Object returnValue=joinPoint.proceed();
    long end=System.nanoTime();
    String methodName=joinPoint.getSignature().getName();
    logger.info("Execution of "+methodName+" took "+TimeUnit.NANOSECONDS.toMillis(end-start)+" ms");
    return returnValue;
  }
}