package dev.ta2khu75.shopapp.component.aspects;

import java.util.logging.Logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
@Aspect
public class UserActivityLogger {
  private Logger logger=Logger.getLogger(getClass().getName());
  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void controllerMethods(){}
  @Around("controllerMethods() &&  execution(* dev.ta2khu75.shopapp.controller.ControllerUser.*(..))")
  public Object LogUserActivity(ProceedingJoinPoint joinPoint) throws Throwable{
    String methodName=joinPoint.getSignature().getName();
    String remoteAddress=((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
    //Ghi log trước khi thực hiện method
    logger.info("User activity started: "+methodName+", IP address: "+remoteAddress);
    //Thực hiện method gốc
    Object result=joinPoint.proceed();
    //Ghi log sau khi thực hiện method
    logger.info("User activity finished: "+methodName);
    return result;
  }
}
