package com.imooc.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @program: bessie-dev
 * @description: 监测service的运行时间是否过长
 * @author: Bessie
 * @create: 2021-10-20 04:53
 **/

@Aspect
@Component
public class ServiceLogAspect {

    /**

     */

    /**

     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */

    public static final Logger log = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     *  * AOP通知：
     *      * 1. 前置通知：在方法调用之前执行
     *      * 2. 后置通知：在方法正常调用之后执行
     *      * 3. 环绕通知：在方法调用之前和之后，都分别可以执行的通知     @Around()
     *      * 4. 异常通知：如果在方法调用过程中发生异常，则通知
     *      * 5. 最终通知：在方法调用之后执行
     * 切面表达式：
     * execution 代表所要执行的表达式主体
     * 第一处 * 代表方法返回类型 *代表所有类型
     * 第二处 包名代表aop监控的类所在的包
     * 第三处 .. 代表该包以及其子包下的所有类方法
     * 第四处 * 代表类名，*代表所有类
     * 第五处 *(..) *代表类中的方法名，(..)表示方法中的任何参数
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.imooc.service.impl..*.*(..))") //举例: Users com.imooc.service.impl.UserServiceImpl.createUser(UserBO)
    public Object recordExecuteTimeLog(ProceedingJoinPoint joinPoint) throws Throwable
    {
        //1. 打印: 是哪个类的哪个方法
        log.info("--------- 开始执行 {}.{} ---------",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        //2. 记录开始时间
        long begin = System.currentTimeMillis();

        //3. 开始执行目标 service
        Object result = joinPoint.proceed();

        //4. 记录结束时间
        long end = System.currentTimeMillis();
        long duration = end - begin;

        if(duration > 3000)
        {
            log.error("--------- 结束执行 {}.{}, 耗时: {} 毫秒 ---------",
                    joinPoint.getTarget().getClass(),
                    joinPoint.getSignature().getName(),
                    duration);
        }
        else if(duration > 2000)
        {
            log.warn("--------- 结束执行 {}.{}, 耗时: {} 毫秒 ---------",
                    joinPoint.getTarget().getClass(),
                    joinPoint.getSignature().getName(),
                    duration);
        }
        else
        {
            log.info("--------- 结束执行 {}.{}, 耗时: {} 毫秒 ---------",
                    joinPoint.getTarget().getClass(),
                    joinPoint.getSignature().getName(),
                    duration);
        }

        return result;

    }
}
