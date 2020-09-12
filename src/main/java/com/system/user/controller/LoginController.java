package com.system.user.controller;


import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletResponse;

import com.system.user.dao.SysUserDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.aspect.MyLog;
import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.app.base.utils.MD5Util;
import com.app.config.annotation.UserLoginToken;
import com.app.config.service.TokenService;
import com.system.role.service.SysRoleService;
import com.system.user.entity.SysUser;
import com.system.user.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;


@Api(description = "登录管理模块")
@RestController
public class LoginController extends WebController{

    @Autowired
    private SysUserDao sysUserDao;
    
    @Autowired
    private SysUserService sysUserService;
   
    @Autowired
    TokenService tokenService;
    @Autowired
    private EhCacheManager ecm;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "登录", notes = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "编码", dataType = "Long", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "Long", paramType = "query", defaultValue = "")
    })
    @MyLog(value = "登录")
    @GetMapping("/login1")
    public ApiResponseResult login(@RequestParam(value = "username",required=false) String username, @RequestParam(value = "password",required=false) String password,
                                   @RequestParam(value = "rememberMe", required = false) boolean rememberMe) {
    	String method = "/login1";String methodName ="登录";
    	try {
    		 username = username.toUpperCase();//用户名转换成大写

    		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
    			getSysLogService().error(method,methodName,"用户名或密码为空");
                return ApiResponseResult.failure("用户名或密码不能为空！");
            }
           
            //用户是否存在
            /*SysUser userForBase = sysUserDao.findByFcode(username);
    		if(userForBase == null){
    			getSysLogService().error(method,methodName,"用户不存在");
                return ApiResponseResult.failure("该用户不存在，请联系管理员！");
            }*/

            //用户登录
            try {
                // 1、 封装用户名、密码、是否记住我到token令牌对象 [支持记住我]
                AuthenticationToken token = new UsernamePasswordToken(
                        username, DigestUtils.md5Hex(password),
                        rememberMe);
                // 2、 Subject调用login
                Subject subject = SecurityUtils.getSubject();
                // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
                // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
                // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
                logger.debug("用户登录，用户验证开始！user=" + username);
                subject.login(token);
                logger.info("用户登录，用户验证通过！user=" + username);
                getSysLogService().success(method,methodName,"");
            } catch (UnknownAccountException uae) {
                logger.error("用户登录，用户验证未通过：未知用户！user=" + username, uae);
                getSysLogService().error(method,methodName,"用户不存在");
                return ApiResponseResult.failure("该用户不存在，请联系管理员！");
            } catch (IncorrectCredentialsException ice) {
                // 获取输错次数
                logger.error("用户登录，用户验证未通过：错误的凭证，密码输入错误！user=" + username, ice);
                getSysLogService().error(method,methodName,"用户验证未通过");
                return ApiResponseResult.failure("用户名或密码不正确！");
            } catch (LockedAccountException lae) {
                logger.error("用户登录，用户验证未通过：账户已锁定！user=" + username, lae);
                getSysLogService().error(method,methodName,"账户已锁定");
                return ApiResponseResult.failure("账户已锁定！");
            } catch (ExcessiveAttemptsException eae) {
                logger.error(
                        "用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user=" + username, eae);
                getSysLogService().error(method,methodName,"用户名或密码错误次数大于5次,账户已锁定");
                return ApiResponseResult.failure("用户名或密码错误次数大于5次,账户已锁定!</br><span style='color:red;font-weight:bold; '>2分钟后可再次登录，或联系管理员解锁</span>");
                // 这里结合了，另一种密码输错限制的实现，基于redis或mysql的实现；也可以直接使用RetryLimitHashedCredentialsMatcher限制5次
            } catch (DisabledAccountException sae){
		 logger.error("用户登录，用户验证未通过：帐号已经禁止登录！user=" +username, sae);
		 return ApiResponseResult.failure("帐号已经禁止登录！");
		}catch (AuthenticationException ae) {
                // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
                logger.error("用户登录，用户验证未通过：认证异常，异常信息如下！user=" + username, ae);
                getSysLogService().error(method,methodName,"用户名或密码不正确");
                return ApiResponseResult.failure("用户名或密码不正确！");
            } catch (Exception e) {
                logger.error("用户登录，用户验证未通过：操作异常，异常信息如下！user=" + username, e);
                getSysLogService().error(method,methodName,"用户登录失败");
                return ApiResponseResult.failure("用户登录失败，请您稍后再试！");
            }
            Cache<String, AtomicInteger> passwordRetryCache = ecm
                    .getCache("passwordRetryCache");
//            if (null != passwordRetryCache) {
//                int retryNum = (passwordRetryCache.get(userForBase.getMobile()) == null ? 0
//                        : passwordRetryCache.get(userForBase.getMobile())).intValue();
//                logger.debug("输错次数：" + retryNum);
//                if (retryNum > 0 && retryNum < 6) {
//                    return ApiResponseResult.failure("用户名或密码错误" + retryNum + "次,再输错" + (6 - retryNum) + "次账号将锁定");
//                }
//            }
            return ApiResponseResult.success("用户登录成功！");
        } catch (Exception e) {
        	System.out.println(e.toString());
            return ApiResponseResult.failure("用户登录失败！");
        }
//        return ApiResponseResult.failure();
    }
    
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

    @RequestMapping(value = "/console")
    public String console(){
        return "/console";
    }
    
    @ApiOperation(value = "用户登录验证", notes = "根据用户Id验证用户密码是否正确，进行登录验证; 登录成功后，置为上线")
	@ApiImplicitParam(name = "username", value = "用户Id", paramType = "Query", required = true, dataType = "String")
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult login(@RequestBody Map<String, Object> params) {		
		try {
			String username = params.get("username").toString().toUpperCase();
			String password = params.get("password").toString();
    		
    		if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
                return ApiResponseResult.failure("用户名或密码不能为空！");
            }
    		
    		List<Map<String, Object>> userForBase=sysUserService.findByUserCode(username);
            if(userForBase.size() == 0){
            	return ApiResponseResult.failure("用户不存在！");
            }else {
            	//验证密码
            	String p = userForBase.get(0).get("FPASSWORD").toString();
            	p =proPass(p);
            	if(!p.equals(password)){
            		return ApiResponseResult.failure("用户名或者密码不正确！");
            	}else{
            		return ApiResponseResult.success("登录成功！").data(userForBase.get(0));
            	}
            }
        } catch (Exception e) {
        	System.out.println(e.toString());
            return ApiResponseResult.failure("用户登录失败！"+e.toString());
        }
	}
    
//	 解密算法
	private String proPass(String src) throws Exception {
		String result = "";
		int first = new Integer(src.substring(0, 1)).intValue();
		String src_tem = src.substring(1);
		byte[] b = src_tem.getBytes("iso8859-1");
		byte[] temp = b;
		int i = 0;
		for (; i < b.length; i++) {
			temp[i] = new Integer(new Integer(temp[i]).intValue() ^ (first + 18))
			.byteValue();
		}
		result = new String(temp);
		return result;
	}
	
	
	@ApiOperation(value = "查询版本号", notes = "查询版本号")
	@RequestMapping(value = "/queryVersion", method = RequestMethod.GET, produces = "application/json")
	public ApiResponseResult queryVersion() {		
     try {
   		List<Map<String, Object>> userForBase=sysUserService.queryVersion();
           if(userForBase.size() == 0){
           	return ApiResponseResult.failure("版本号不存在！");
           }else {
           	return ApiResponseResult.success("查询版本号成功！").data(userForBase);
           }
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("查询版本号失败！");
       }
	}
	
	@ApiOperation(value = "查询运行环境", notes = "查询运行环境")
	@RequestMapping(value = "/queryRunEnv", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult queryRunEnv() {		
     try {
   		List<Map<String, Object>> userForBase=sysUserService.queryRunEnv();
           if(userForBase.size() == 0){
           	return ApiResponseResult.failure("运行环境不存在！");
           }else {
           	return ApiResponseResult.success("查询运行环境成功！").data(userForBase);
           }
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("查询运行环境失败！");
       }
	}
	
	@ApiOperation(value = "查询权限", notes = "查询权限")
	@ApiImplicitParam(name = "userno", value = "用户Id", paramType = "Query", required = true, dataType = "String")
	@RequestMapping(value = "/queryPurview", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult queryPurview(@RequestParam(value = "userno") String userno) {		
     try {
   		List<Map<String, Object>> userForBase=sysUserService.queryPurview(userno);
           if(userForBase.size() == 0){
           	return ApiResponseResult.failure("权限不存在！");
           }else {
           	return ApiResponseResult.success("查询权限成功！").data(userForBase);
           }
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("查询权限失败！");
       }
	}
	
	@ApiOperation(value = "功能界面", notes = "功能界面")
	@ApiImplicitParam(name = "functionName", value = "方法名称", paramType = "Query", required = true, dataType = "String")
	@RequestMapping(value = "/getRfSetup", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult  getRfSetup(@RequestParam(value = "functionName") String functionName) {		
     try {
   	  System.out.println(functionName);
   	  String username = URLDecoder.decode(functionName,"UTF-8");
   	  return sysUserService.getRfSetup(functionName);
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("查询功能界面失败！");
       }
	}
	
	@ApiOperation(value = "功能执行存储过程 ", notes = "功能执行存储过程 ")
	@ApiImplicitParam(name = "functionName", value = "方法名称", paramType = "Query", required = true, dataType = "String")
	@RequestMapping(value = "/getExcProc", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult  getExcProc(@RequestParam(value = "functionName") String functionName,
			@RequestParam(value = "fileName") String fileName,@RequestParam(value = "pmachtype") String pmachtype,
			@RequestParam(value = "fileValue") String fileValue,@RequestParam(value = "outFiles") String outFiles) {		
     try {
   	  System.out.println(functionName);
   	  functionName = URLDecoder.decode(functionName,"UTF-8");
   	  return sysUserService.getExcProc(functionName,fileName,pmachtype,fileValue,outFiles);
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("查询功能界面失败！");
       }
	}
	
	
	@ApiOperation(value = "查询app版本号", notes = "查询app版本号")
	@RequestMapping(value = "/queryAppVersion", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult queryAppVersion() {		
     try {
   		return sysUserService.queryAppVersion();
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("查询版本号失败！");
       }
	}
	
	
	@ApiOperation(value = "修改密码", notes = "修改密码")
	@RequestMapping(value = "/changPsw", method = RequestMethod.POST, produces = "application/json")
	public ApiResponseResult changPsw(@RequestParam(value = "usercode") String usercode,@RequestParam(value = "oldp") String oldp,@RequestParam(value = "newp") String newp) {		
		try {

   		if(StringUtils.isEmpty(usercode) || StringUtils.isEmpty(oldp) || StringUtils.isEmpty(newp)){
               return ApiResponseResult.failure("用户名或密码不能为空！");
           }
   		
   		List<Map<String, Object>> userForBase=sysUserService.findByUserCode(usercode);
           if(userForBase.size() == 0){
           	return ApiResponseResult.failure("用户不存在！");
           }else {
           	//验证密码
           	String p = userForBase.get(0).get("FPASSWORD").toString();
           	p =proPass(p);
           	if(!p.equals(oldp)){
           		return ApiResponseResult.failure("用户名或者密码不正确！");
           	}else{
           		return sysUserService.changPsw(usercode, encryptPass(newp));
           		//return ApiResponseResult.success("登录成功！").data(userForBase.get(0));
           	}
           }
       } catch (Exception e) {
       	System.out.println(e.toString());
           return ApiResponseResult.failure("用户登录失败！"+e.toString());
       }
	}
   
//	 解密算法
	private String encryptPass(String str) throws Exception {
		//byte[] temp = new byte[255] ;
		byte[] b = str.getBytes("iso8859-1");
		byte[] temp =b;
		int i = 0;
		for (; i < b.length; i++) {
			temp[i] = new Integer(new Integer(b[i]).intValue() ^ (8 + 18))
					.byteValue();
		}
		String result = 8 + new String(temp);
		return result;
	}
}
