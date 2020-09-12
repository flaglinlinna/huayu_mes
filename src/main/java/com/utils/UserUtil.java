package com.utils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.system.user.entity.SysUser;
import com.system.user.service.SysUserService;

@Component
public class UserUtil {
	@Autowired
    public  SysUserService sysUserService ;
	private static UserUtil userUtil;
	public void setSysUserService(SysUserService sysUserService) {
		this.sysUserService = sysUserService;
		}
	 @PostConstruct
	 public void init(){
		 userUtil = this;
		 userUtil.sysUserService = this.sysUserService;
	 }
	
	/**
	 * 获取当前登录用户
	 * @return
	 * @throws Exception 
	 */
	public static  SysUser getCurrUser() throws Exception{
/*		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        
        String token = request.getHeader("X-Token");// 从 http 请求头中取出 token
        if(token != null){
        	String userCode = JWT.decode(token).getAudience().get(0);
        	return userUtil.sysUserService.findByUserCode(userCode);
        }*/
        return null;
		
	}
	/**
	 * 返回当前登录用户
	 * fyx
	 * 2020-03-06
	 * @return
	 * @throws Exception
	 */
	public static SysUser getSessionUser()throws Exception{
		return (SysUser)SecurityUtils.getSubject().getPrincipal();
	}
	

}
