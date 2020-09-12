package com.app.aspect;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.system.log.entity.SysLog;
import com.system.log.service.SysLogService;
import com.system.user.entity.SysUser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志：切面处理类
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    //定义切点 @Pointcut
    //在注解的位置切入代码
   // @Pointcut("@annotation( com.app.aspect.MyLog)")
    @Pointcut("execution(public * com.*.*.controller..*.*(..))")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        System.out.println("访问日志。。。。。。" + new Date());
        //保存日志
        SysLog sysLog = new SysLog();

        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();

        //获取操作
      /*  MyLog myLog = method.getAnnotation(MyLog.class);
        if (myLog != null) {
            String value = myLog.value();
            sysLog.setOperation(value);//保存获取的操作
        }*/

        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLog.setMethod(className + "." + methodName);

        //请求的参数
        Object[] args = joinPoint.getArgs();
        //将参数所在的数组转换成json
        String params = JSON.toJSONString(args);
        
        //System.out.println("String length: " + params.length());
        //System.out.println("Byte array length: " + params.getBytes().length);
        System.out.println("String: " + params);
     
        try {
			params = new String(params.getBytes("ISO8859-1"), "GBK");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //System.out.println(params);
        
        //20181225-fyx-参数过大(一般是文件)
        if(params.length()>=255){
        	params = params.substring(0, 254);
        }
        sysLog.setParams(params);

        sysLog.setCreatedTime(new Date());
        
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        
        String token = request.getHeader("X-Token");// 从 http 请求头中取出 token
        if(token != null){
        	String userCode = JWT.decode(token).getAudience().get(0);
            //获取用户名
            sysLog.setUsername(userCode);
        }
        
        //获取用户ip地址
        sysLog.setIp(IpUtil.getIpAddr(request));

        //调用service保存SysLog实体类到数据库
        try {
			//sysLogService.add(sysLog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
    }

}