package com.app.config.interceptor;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.app.aspect.IpUtil;
import com.app.config.annotation.PassToken;
import com.app.config.annotation.UserLoginToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.system.log.entity.SysLog;
import com.system.log.service.SysLogService;
import com.system.user.dao.SysUserDao;
import com.system.user.entity.SysUser;


/**
 * @author jinbin
 * @date 2018-07-08 20:41
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
	@Autowired
    private SysUserDao sysUserDao;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
       try{
    	   String token = httpServletRequest.getHeader("X-Token");// 从 http 请求头中取出 token
           // 如果不是映射到方法直接通过
           if(!(object instanceof HandlerMethod)){
               return true;
           }
           HandlerMethod handlerMethod=(HandlerMethod)object;
           Method method=handlerMethod.getMethod();
           //检查是否有passtoken注释，有则跳过认证
           if (method.isAnnotationPresent(PassToken.class)) {
               PassToken passToken = method.getAnnotation(PassToken.class);
               if (passToken.required()) {
                   return true;
               }
           }
           //检查有没有需要用户权限的注解
           if (method.isAnnotationPresent(UserLoginToken.class)) {
               UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
               if (userLoginToken.required()) {
                   // 执行认证
                   if (token == null) {
                       throw new RuntimeException("无token，请重新登录");
                   }
                   // 获取 token 中的 user id
                   String userCode;
                   try {
                   	userCode = JWT.decode(token).getAudience().get(0);
                   } catch (JWTDecodeException j) {
                       throw new RuntimeException("401");
                   }
                   SysUser user = sysUserDao.findByIsDelAndBsCode(0, userCode);
//                   SysUser user = sysUserDao.findByFcode(userCode);
                   if (user == null) {
                       throw new RuntimeException("用户不存在，请重新登录");
                   }
                   // 验证 token
                   JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getBsPassword())).build();
//                   JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getFpassword())).build();
                   try {
                       jwtVerifier.verify(token);
                   } catch (JWTVerificationException e) {
                       throw new RuntimeException("401");
                   }
   /*                
                   //开始保存请求日志
                   //保存日志
                   SysLog sysLog = new SysLog();
                   //获取请求的类名
                  // String className = joinPoint.getTarget().getClass().getName();

                   //获取请求的方法名
                   String methodName = method.getName();
                   sysLog.setMethod( methodName);

                   //请求的参数
                  // Object[] args = joinPoint.getArgs();
                   Map<String,String[]> args = httpServletRequest.getParameterMap();
                   //将参数所在的数组转换成json
                   String params = JSON.toJSONString(args);
                   sysLog.setParams(params);

                   sysLog.setCreatedTime(new Date());
                   
                   //获取用户名
                   sysLog.setUsername(userCode);
                   //获取用户ip地址
                   sysLog.setIp(IpUtil.getIpAddr(httpServletRequest));

                   //调用service保存SysLog实体类到数据库
                   try {
           			sysLogService.add(sysLog);
           		} catch (Exception e) {
           			// TODO Auto-generated catch block
           			e.printStackTrace();
           			System.out.println(e.toString());
           		}
                   //--end
   */                return true;
               }
           }
           return true;
       }catch(Exception e){
    	   return true;
       }
    	
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
