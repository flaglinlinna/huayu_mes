package com.shiro;

/*import com.wyait.manage.dao.UserMapper;
import com.wyait.manage.pojo.Permission;
import com.wyait.manage.pojo.Role;
import com.wyait.manage.pojo.User;
import com.wyait.manage.service.AuthService;
import com.wyait.manage.service.UserServiceImpl;*/

import com.system.user.dao.SysUserDao;
import com.system.user.entity.SysUser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pojo.User;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.shiro
 * @类描述：
 * @创建人：wyait
 * @创建时间：2017-12-13 13:53
 * @version：V1.0
 */
@Service
public class ShiroRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory
			.getLogger(ShiroRealm.class);

	@Autowired
	private SysUserDao sysUserDao;

	/*@Autowired
	private UserMapper userMapper;
	@Autowired
	private AuthService authService;*/

	/**
	 * 授予角色和权限
	 * @param principalCollection
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principalCollection) {
		//授权
		logger.debug("授予角色和权限");
		// 添加权限 和 角色信息
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 获取当前登陆用户
		Subject subject = SecurityUtils.getSubject();
		SysUser user = (SysUser) subject.getPrincipal();
//		if (user.getUserMobile().equals("18516596566")) {
        if (user.getUserCode().equals("ADMIN")) {
			// 超级管理员，添加所有角色、添加所有权限
			authorizationInfo.addRole("*");
			authorizationInfo.addStringPermission("*");
		} else {
			// 普通用户，查询用户的角色，根据角色查询权限
//			Long userId = user.getId();
			/*List<Role> roles = this.authService.getRoleByUser(userId);
			if (null != roles && roles.size() > 0) {
				for (Role role : roles) {
					authorizationInfo.addRole(role.getCode());
					// 角色对应的权限数据
					List<Permission> perms = this.authService.findPermsByRoleId(role
							.getId());
					if (null != perms && perms.size() > 0) {
						// 授权角色下所有权限
						for (Permission perm : perms) {
							authorizationInfo.addStringPermission(perm
									.getCode());
						}
					}
				}
			}*/
		}
		return authorizationInfo;
	}

	/**
	 * 登录认证
	 * @param authenticationToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authenticationToken)
			throws AuthenticationException {
		//TODO
		//UsernamePasswordToken用于存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
		logger.info("用户登录认证：验证当前Subject时获取到token为：" + ReflectionToStringBuilder
				.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        String userName = token.getUsername();
		// 调用数据层
        //SysUser user = new SysUser();
        SysUser user = sysUserDao.findByDelFlagAndUserCode(0, userName);
        SysUser userMD5 = new SysUser();
        try{
            userMD5.setId(user.getId());
            userMD5.setUserCode(user.getUserCode());
            userMD5.setUserName(user.getUserName());
//            userMD5.setBsPassword(DigestUtils.md5Hex(proPass(user.getBsPassword())));
            userMD5.setPassword(user.getPassword());
        }catch (Exception e){
            userMD5.setPassword(DigestUtils.md5Hex("a"));
        }
//        SysUser user = sysUserDao.findByDelFlagAndUserCode(0,userName);

		logger.debug("用户登录认证！用户信息user：" + user);
		if (user == null) {
			// 用户不存在
			return null;
		} else {
			// 密码存在
			// 第一个参数 ，登陆后，需要在session保存数据
			// 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
			// 第三个参数 ，realm名字
//			return new SimpleAuthenticationInfo(user, DigestUtils.md5Hex(user.getBsPassword()),
//					getName());
            return new SimpleAuthenticationInfo(userMD5, DigestUtils.md5Hex(userMD5.getPassword()),
                    getName());
		}
	}

	/**
	 * 清除所有缓存【实测无效】
	 */
	public void clearCachedAuth(){
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}

    //解密算法
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
}
