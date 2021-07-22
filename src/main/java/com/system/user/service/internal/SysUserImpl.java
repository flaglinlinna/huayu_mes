package com.system.user.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.app.base.utils.MD5Util;
import com.auth0.jwt.JWT;
import com.system.role.dao.SysRoleDao;
import com.system.role.entity.SysRole;
import com.system.user.dao.SysUserDao;
import com.system.user.dao.UserOrgMapDao;
import com.system.user.dao.UserRoleMapDao;
import com.system.user.entity.SysUser;
import com.system.user.entity.UserOrgMap;
import com.system.user.entity.UserRoleMap;
import com.system.user.service.SysUserService;

import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.produce.service.internal.PrcUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

@Service(value = "sysUserService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysUserImpl extends PrcUtils implements SysUserService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private UserRoleMapDao userRoleMapDao;
    @Autowired
    private UserOrgMapDao userOrgMapDao;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 新增(编辑)用户
     * @param sysUser
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult add(SysUser sysUser) throws Exception{
        if(sysUser == null){
            return ApiResponseResult.failure("用户不能为空！");
        }
        if(StringUtils.isEmpty(sysUser.getUserCode())){
            return ApiResponseResult.failure("用户账号不能为空！");
        }

        //1.新增
        if(sysUser.getId() == null){
            //判断用户账号是否存在，存在则
            SysUser existUser = sysUserDao.findByDelFlagAndUserCode(0, sysUser.getUserCode());
            if(existUser != null){
                return ApiResponseResult.failure("该账号已存在！");
            }
            sysUser.setCreateDate(new Date());
            sysUser.setStatus(0);//状态（0：正常 / 1：禁用）
            sysUser.setPassword(DigestUtils.md5Hex("123456"));//设置密码
            sysUserDao.save(sysUser);

            // 给用户授角色
            if(StringUtils.isNotEmpty(sysUser.getRoleIds())){
                String[] arrays = sysUser.getRoleIds().split(",");
                for(String roleId : arrays){
                	if(StringUtils.isNotEmpty(roleId)){
                		UserRoleMap urItem = new UserRoleMap();
                        urItem.setCreateDate(new Date());
                        urItem.setUserId(sysUser.getId());
                        urItem.setRoleId(Long.parseLong(roleId));
                        userRoleMapDao.save(urItem);
                	}
                }
            }
            //给用户增加组织架构数据(数据权限)
            if(StringUtils.isNotEmpty(sysUser.getOrgIds())){
                String[] arrays = sysUser.getOrgIds().split(",");
                for(String orgId : arrays){
                	if(StringUtils.isNotEmpty(orgId)){
                		UserOrgMap uoItem=new UserOrgMap();
                		uoItem.setCreateDate(new Date());
                		uoItem.setCreateBy(UserUtil.getSessionUser().getId());
                		uoItem.setUserId(sysUser.getId());
                		uoItem.setOrgId(Long.parseLong(orgId));
                		userOrgMapDao.save(uoItem);
                	}
                }
            }
            
            return ApiResponseResult.success("新增成功！");
        }else{
            //1.编辑
            SysUser o = sysUserDao.findById((long) sysUser.getId());
            if(o == null){
                return ApiResponseResult.failure("用户不存在！");
            }
            int count = sysUserDao.countByDelFlagAndUserCodeAndIdNot(0, sysUser.getUserCode(), sysUser.getId());
            if(count > 0){
                return ApiResponseResult.failure("用户编号已存在，请填写其他用户编号！");
            }

            o.setLastupdateDate(new Date());
            o.setUserCode(sysUser.getUserCode().trim());
            o.setUserName(sysUser.getUserName());
            o.setRealName(sysUser.getRealName());
            o.setMobile(sysUser.getMobile());
            o.setEmail(sysUser.getEmail());
            o.setSex(sysUser.getSex());
            o.setRegisterSrc(sysUser.getRegisterSrc());
            o.setDepartment(sysUser.getDepartment());
            //删除原来的角色权限
            List<UserRoleMap> list = userRoleMapDao.findByDelFlagAndUserId(0, o.getId());
            if(list.size() > 0){
                for(UserRoleMap urItem : list){
                    urItem.setLastupdateDate(new Date());
                    urItem.setDelTime(new Date());
                    urItem.setDelFlag(1);
                }
                userRoleMapDao.saveAll(list);
            }

            // 给用户授角色权限
            if(StringUtils.isNotEmpty(sysUser.getRoleIds())){
                String[] arrays = sysUser.getRoleIds().split(",");
                for(String roleId : arrays){
                	if(StringUtils.isNotEmpty(roleId)){
                		UserRoleMap urItem = new UserRoleMap();
                        urItem.setCreateDate(new Date());
                        urItem.setUserId(o.getId());
                        urItem.setRoleId(Long.parseLong(roleId));
                        userRoleMapDao.save(urItem);
                	}
                    
                }
            }
            
            //删除原来的组织id
            List<UserOrgMap> org_list = userOrgMapDao.findByDelFlagAndUserId(0, o.getId());
            if(org_list.size() > 0){
                for(UserOrgMap uoItem : org_list){
                	uoItem.setLastupdateDate(new Date());
                	uoItem.setLastupdateBy(UserUtil.getSessionUser().getId());
                	uoItem.setDelBy(UserUtil.getSessionUser().getId());
                	uoItem.setDelTime(new Date());
                	uoItem.setDelFlag(1);
                }
                userOrgMapDao.saveAll(org_list);
            }
            //给用户分配组织
            if(StringUtils.isNotEmpty(sysUser.getOrgIds())){
                String[] arrays = sysUser.getOrgIds().split(",");
                for(String ordId : arrays){
                	if(StringUtils.isNotEmpty(ordId)){
                		UserOrgMap uoItem = new UserOrgMap();
                		uoItem.setCreateDate(new Date());
                		uoItem.setCreateBy(UserUtil.getSessionUser().getId());
                		uoItem.setUserId(o.getId());
                		uoItem.setOrgId(Long.parseLong(ordId));
                        userOrgMapDao.save(uoItem);
                	}
                }
            }
            return ApiResponseResult.success("编辑成功！");
        }
    }

    /**
     * 编辑用户
     * @param sysUser
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult edit(SysUser sysUser) throws Exception {
        if(sysUser == null || sysUser.getId() == null){
            return ApiResponseResult.failure("用户ID不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null || currUser.getId() == null){
            return ApiResponseResult.failure("当前用户不在线，请先登录！");
        }
        //获取当前用户信息
        SysUser o = sysUserDao.findById((long) sysUser.getId());
        if(o == null){
            return ApiResponseResult.failure("当前用户不存在！");
        }

        //修改用户信息
        o.setLastupdateDate(new Date());
        o.setUserName(sysUser.getUserName());
        o.setRealName(sysUser.getRealName());
        o.setMobile(sysUser.getMobile());
        o.setEmail(sysUser.getEmail());
        o.setSex(sysUser.getSex());
        sysUserDao.save(o);

        return ApiResponseResult.success("编辑成功！");
    }

    /**
     * 删除用户
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("用户ID不能为空！");
        }
        SysUser o = sysUserDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("用户不存在！");
        }
        if(o.getUserCode() != null && StringUtils.equals(o.getUserCode().toUpperCase(), "ADMIN")){
            return ApiResponseResult.failure("用户为管理员用户，无法删除！");
        }

        //删除用户
        o.setLastupdateDate(new Date());
        o.setDelFlag(1);
        sysUserDao.save(o);

        //删除用户关联角色信息
        List<UserRoleMap> list = userRoleMapDao.findByDelFlagAndUserId(0, id);
        if(list.size() > 0){
            for(UserRoleMap urItem : list){
                urItem.setLastupdateDate(new Date());
                urItem.setDelFlag(1);
            }
            userRoleMapDao.saveAll(list);
        }

        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 修改密码
     * @param password
     * @param rePassword
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult changePassword(String oldPassword, String password, String rePassword) throws Exception{
        SysUser sysUser = UserUtil.getSessionUser();
//        if(sysUser == null || sysUser.getFid() == null){
//            return ApiResponseResult.failure("没有足够的权限修改密码！");
//        }
        if(sysUser == null || sysUser.getId() == null){
            return ApiResponseResult.failure("没有足够的权限修改密码！");
        }
        if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(oldPassword.trim())){
            return ApiResponseResult.failure("原密码不能为空！");
        }
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(password.trim())){
            return ApiResponseResult.failure("新密码不能为空！");
        }
        if(StringUtils.isEmpty(rePassword) || StringUtils.isEmpty(rePassword.trim())){
            return ApiResponseResult.failure("确认新密码不能为空！");
        }
        //判断两次输入的新密码是否一致
        if(!StringUtils.equals(password.trim(), rePassword.trim())){
            return ApiResponseResult.failure("两次输入的新密码不一致！");
        }
        //密码长度校验
        ApiResponseResult result = validate(password.trim());
        if (!result.isResult()) {
            return result;
        }

        //修改用户信息
        SysUser o = sysUserDao.findById((long) sysUser.getId());
        if(o == null){
            return ApiResponseResult.failure("用户不存在！");
        }
        //判断原始密码输入是否正确
        boolean isRight = decryptPassword(oldPassword, o.getPassword());
        if(!isRight){
            return ApiResponseResult.failure("原密码输入有误！");
        }
        //修改密码
        o.setLastupdateDate(new Date());
        o.setPassword(DigestUtils.md5Hex(password.trim()));
        sysUserDao.save(o);

        return ApiResponseResult.success("密码修改成功！");
    }
    //判断原始密码输入是否正确
    private boolean decryptPassword(String passwordInput, String password){
        passwordInput = DigestUtils.md5Hex(passwordInput.trim());
        if(password.equals(passwordInput)){
            return true;
        }
        return false;
    }
    //密码长度校验
    private static ApiResponseResult validate(String password) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(password.trim())) {
            return ApiResponseResult.failure("密码不能为空");
        }
        if (password.length() < 6 || password.length() > 16) {
            return ApiResponseResult.failure("密码长度应在6~16位").data(-1);
        }

        return ApiResponseResult.success();
    }

    /**
     *
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getUserInfo() throws Exception{
        SysUser sysUser = UserUtil.getSessionUser();
        if(sysUser == null || sysUser.getId() == null){
            return ApiResponseResult.failure("当前用户不在线，请先登录！");
        }
        //获取当前用户信息
        SysUser o = sysUserDao.findById((long) sysUser.getId());
        if(o == null){
            return ApiResponseResult.failure("用户不存在！");
        }
        //获取当前用户角色
        String roleNames = "";
        List<UserRoleMap> urList = userRoleMapDao.findByDelFlagAndUserId(0, sysUser.getId());
        for(UserRoleMap item : urList){
            if(item != null && item.getRoleId() != null){
                SysRole sysRole = sysRoleDao.findById((long) item.getRoleId());
                if(sysRole != null && StringUtils.isNotEmpty(sysRole.getRoleName())){
                    if(StringUtils.isEmpty(roleNames)){
                        roleNames += sysRole.getRoleName();
                    }else{
                        roleNames += "," + sysRole.getRoleName();
                    }
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", o.getId());
        map.put("userCode", o.getUserCode());
        map.put("userName", o.getUserName());
        map.put("realName",o.getRealName());
        map.put("mobile", o.getMobile());
        map.put("email", o.getEmail());
        map.put("sex", o.getSex());
        map.put("status", o.getStatus());
        map.put("roleNames", roleNames);

        return ApiResponseResult.success().data(map);
    }

	@Override
	public ApiResponseResult getUserInfo(String token) throws Exception {
        return ApiResponseResult.success("登录成功！");
	}

    /**
     * 获取用户列表
     * @param keyword
     * @param userCode
     * @param userName
     * @param mobile
     * @param status
     * @param pageRequest
     * @return
     * @throws Exception
     */
	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, String userCode, String userName, String mobile, Integer status, PageRequest pageRequest) throws Exception {
        //查询条件1
	    List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if(StringUtils.isNotEmpty(userCode)){
            filters.add(new SearchFilter("userCode", SearchFilter.Operator.LIKE, userCode));
        }
        if(StringUtils.isNotEmpty(userName)){
            filters.add(new SearchFilter("userName", SearchFilter.Operator.LIKE, userName));
        }
        if(StringUtils.isNotEmpty(mobile)){
            filters.add(new SearchFilter("mobile", SearchFilter.Operator.LIKE, mobile));
        }
        if(status != null){
            filters.add(new SearchFilter("status", SearchFilter.Operator.EQ, status));
        }
        //查询2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("userCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("userName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("mobile", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<SysUser> spec = Specification.where(BaseService.and(filters, SysUser.class));
        Specification<SysUser> spec1 =  spec.and(BaseService.or(filters1, SysUser.class));
        Page<SysUser> page = sysUserDao.findAll(spec1, pageRequest);
        
        List<SysUser> ls = page.getContent();
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(SysUser su:ls){
        	Map<String,Object> map = new HashMap<>();
        	map.put("id", su.getId());
        	map.put("userCode", su.getUserCode());
        	map.put("mobile", su.getMobile());
        	map.put("userName",su.getUserName() );
        	map.put("email", su.getEmail());
        	map.put("sex", su.getSex());
        	map.put("status", su.getStatus());
        	map.put("createDate", su.getCreateDate());
        	map.put("realName", su.getRealName());
            map.put("department", su.getDepartment());
        	List<SysRole> lr = sysRoleDao.getRoleByUser(su.getId());
        	List mll = new ArrayList<>();
        	if(lr.size() > 0){
        		for(SysRole sr:lr){
        			mll.add(sr.getRoleName());
        		}
        	}
        	map.put("roles", mll);
        	mapList.add(map);
        }
        
        return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


    @Override
    @Transactional
    public ApiResponseResult getListByRoleId(Long roleId, PageRequest pageRequest) throws Exception {
        Page<Map<String, Object>> mapList= sysUserDao.getListByRoleId(roleId,pageRequest);
        return ApiResponseResult.success().data(DataGrid.create(mapList.getContent(), (int) mapList.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }


    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getUserAndRoles(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("用户ID不能为空！");
        }
        SysUser sysUser = sysUserDao.findById((long) id);
        if(sysUser == null){
            return ApiResponseResult.failure("用户不存在！");
        }
        

        //获取所有角色信息
        //List<SysRole> list2 = sysRoleDao.findByDelFlag(0);
        List<SysRole> list2 = sysRoleDao.findByDelFlagAndStatus(0,0);
        
        Map<String, Object> mapUser = new HashMap<>();
        mapUser.put("id",sysUser.getId());
        mapUser.put("userCode", sysUser.getUserCode());
        mapUser.put("userName", sysUser.getUserName());
        mapUser.put("realName", sysUser.getRealName());
        mapUser.put("mobile", sysUser.getMobile());
        mapUser.put("email", sysUser.getEmail());
        mapUser.put("sex", sysUser.getSex());
        mapUser.put("status", sysUser.getStatus());
        mapUser.put("department", sysUser.getDepartment());
      //获取当前用户关联角色信息
        List<Map<String, Object>> list = userRoleMapDao.getRoleIdByUserId(sysUser.getId());
        mapUser.put("userRoles", list);
        
        //fyx-20201110-获取数据权限
        List<Map<String, Object>> lorg = userOrgMapDao.queryOrgNameAndIdByUid(sysUser.getId());
        String org_name = "",org_id = "";
        for(Map<String, Object> map:lorg){
        	if(map.get("ORG_NAME") != null){
        		org_name += map.get("ORG_NAME").toString() +",";
        	}
        	if(map.get("ORG_NAME") != null){
        		org_id += map.get("ORG_ID").toString() +",";
        	}
        }
        if(!StringUtils.isEmpty(org_id)){
        	org_id = org_id.substring(0,org_id.length() - 1);
        }
        if(!StringUtils.isEmpty(org_name)){
        	org_name = org_name.substring(0,org_name.length() - 1);
        }
        //封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("roles", list2);
        map.put("user", mapUser);
        map.put("Org_name", org_name);
        map.put("Org_id", org_id);

        return ApiResponseResult.success().data(map);
    }


    /**
     * 根据ID获取用户姓名
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public String getUserNameById(Long id) throws Exception{
        if(id == null){
            return null;
        }
        SysUser sysUser = sysUserDao.findById((long) id);
        if(sysUser == null){
            return null;
        }
        return sysUser.getUserName();
    }
    /**
     * 设置正常/禁用
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer status) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("用户ID不能为空！");
        }
        if(status == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        SysUser o = sysUserDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("用户不存在！");
        }

        o.setLastupdateDate(new Date());
        o.setStatus(status);
        sysUserDao.save(o);

        return ApiResponseResult.success("设置成功！").data(o);
    }
    
    

    /**
     * 修改密码
     * @param loginName
     * @param password
     * @param rePassword
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult changePassword(String loginName, String oldPassword, String password, String rePassword) throws Exception{
        //验证，原密码和新密码都不能为空，并且原密码要求输入正确，新密码和确认密码一致
/*        if(StringUtils.isEmpty(loginName)){
            return ApiResponseResult.failure("登录名不能为空！");
        }
        if(StringUtils.isEmpty(oldPassword)){
            return ApiResponseResult.failure("原密码不能为空！");
        }
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(rePassword)){
            return ApiResponseResult.failure("密码不能为空！");
        }
        SysUser o = sysUserDao.findByDelFlagAndUserCode(BasicStateEnum.FALSE.intValue(), loginName);
        if(o == null){
            return ApiResponseResult.failure("该用户不存在或已删除！");
        }
        if(!o.getUserPassword().equals(MD5Util.MD5(oldPassword))){
            return ApiResponseResult.failure("原密码输入错误！");
        }
        if(!password.equals(rePassword)){
            return ApiResponseResult.failure("密码不一致，请确认！");
        }

        //1.修改用户密码
        //admin和super为管理员用户，无法操作
        if("admin".equals(o.getUserCode().trim()) || "super".equals(o.getUserCode().trim())){
            return ApiResponseResult.failure("此用户名为管理员用户，无法修改密码！");
        }

        SysUser currUser = UserUtil.getCurrUser();  //获取当前用户
        o.setUserPassword(MD5Util.MD5(password));
        o.setLastupdateDate(new Date());
        o.setLastupdateBy((currUser!=null) ? (currUser.getId()) : null);
        sysUserDao.save(o);
*/

        return ApiResponseResult.success("密码修改成功！");
    }

    /**
     * 重置密码（管理员修改密码使用）
     * @param id
     * @param password
     * @param rePassword
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult resetPassword(Long id, String password, String rePassword) throws Exception{
        //验证，新密码不能为空，并且要求新密码和确认密码一致
        if(id == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        if(StringUtils.isEmpty(password) || StringUtils.isEmpty(rePassword)){
            return ApiResponseResult.failure("密码不能为空！");
        }
        if(!password.equals(rePassword)){
            return ApiResponseResult.failure("密码不一致，请确认！");
        }
        SysUser o = sysUserDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该用户不存在或已删除！");
        }
        SysUser currUser = UserUtil.getCurrUser();  //获取当前用户

        //1.修改用户密码
        //admin和super为管理员用户，无法操作
        if("admin".equals(o.getUserCode().trim().toLowerCase()) || "super".equals(o.getUserCode().trim().toLowerCase())){
            return ApiResponseResult.failure("此用户名为管理员用户，无法修改密码！");
        }

        o.setLastupdateDate(new Date());
        o.setLastupdateBy((currUser!=null) ? (currUser.getId()) : null);
        o.setPassword(DigestUtils.md5Hex(password));
        sysUserDao.save(o);

        return ApiResponseResult.success("密码重置成功！");
    }


	@Override
	public List<Map<String, Object>> findByUserCode(String userCode) throws Exception {
		// TODO Auto-generated method stub
//		String sql = "select s.fcode,s.fname,s.fpassword,s.fcompany,s.ffactory from sys_user s where  upper(s.fcode) ='"
//				+ userCode.toUpperCase() +  "'";
       // List<Map<String, Object>> countList = sysUserDao.findByUserCode(userCode.toUpperCase());//this.findBySql(sql, SQLParameter.newInstance(), null);
		 List<Map<String, Object>> countList = sysUserDao.findByUserCode(userCode);
		return countList;
       //return sysUserDao.findByDelFlagAndUserCode(0, userCode);
	}

    /**
     * 获取用户列表
     * @param usercode
     * @param username
     * @param pageRequest
     * @return
     * @throws Exception
     */
	@Override
    @Transactional
	public ApiResponseResult getlist(String usercode,String username,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		/*Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<SysUser> list = sysUserDao.findAll(sort);*/
        return ApiResponseResult.success();
		/*SysUser demoBean = new SysUser();
		demoBean.setUserIsSuper(0);   //查询条件（0为普通用户，1为超级管理员，只显示普通用户）
		if(!StringUtils.isEmpty(usercode)){
			demoBean.setUserCode(usercode); //查询条件
		}
		if(!StringUtils.isEmpty(username)){
			demoBean.setUserName(username); //查询条件
		}

		//创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("userCode", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("userName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnorePaths("userType", "userStatus");
        //创建查询参数
        Example<SysUser> example = Example.of(demoBean, matcher);
		//获取排序对象
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		//创建分页对象
		PageRequest pageRequest = new PageRequest(0, 10, sort);
		//分页查询
		//List<SysUser> list  = sysUserDao.findAll(example, pageRequest).getContent();
		Page<SysUser> page = sysUserDao.findAll(example, pageRequest);
 
        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	*/
	}


	@Override
	public List<Map<String, Object>> queryVersion() throws Exception {
		// TODO Auto-generated method stub
		/*String sql = "select s.PARAM_VALUE from mes_sys_params s where s.PARAM_NAME='RF版本'";
        List<Map<String, Object>> countList = this.findBySql(sql, SQLParameter.newInstance(), null);
		return countList;*/
		return null;
	}
	@Override
	public List<Map<String, Object>> queryRunEnv() throws Exception {
		// TODO Auto-generated method stub
		/*String sql = "select s.PARAM_VALUE from mes_sys_params s where s.PARAM_NAME='运行环境'";
        List<Map<String, Object>> countList = this.findBySql(sql, SQLParameter.newInstance(), null);
		return countList;*/
		return null;
	}
	@Override
	public List queryPurview(String userno) throws Exception {
		// TODO Auto-generated method stub
		
//		 StoredProcedureQuery storedProcedureQuery = this.entityManager.createNamedStoredProcedureQuery("getContactsLikeName");
//	       storedProcedureQuery.setParameter("c_User_No", "ADMIN");
//	       storedProcedureQuery.setParameter("c_MachType", "WCE");
//	       storedProcedureQuery.execute();
//	       System.out.println(storedProcedureQuery.execute());
	       
//		Integer a = sysUserDao.createPolicy(1);
//		System.out.println(a);
		
//		Query query = entityManager.createNativeQuery("{call Prc_rf_j1_user_login(?,?,?)}");
//		query.setParameter(1, "ADMIN");
//		query.setParameter(2, "WCE");
//		query.setParameter(3, "");
//		String result = query.getSingleResult().toString();
		
		List<String> a = this.doPurview(userno);
		List arr = new ArrayList<>();
		if(a.size() > 0){
			String str = a.get(0);
			String[] strs = str.split("\\[");
			if(strs.length > 2){
				for(int i=2;i<strs.length;i++){
					String str2 = strs[i].trim();//20200423-fyx-去掉空格
					String[] s = str2.split("#");
					arr.add(s);
				}
			}
		}
		String[] tc = "退出#tuichu".split("#");
		arr.add(tc);
		return arr;
	}

	public List<String> doPurview(String userno) {
		List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
		@Override
		public CallableStatement createCallableStatement(Connection con) throws SQLException {
		String storedProc = "{call Prc_rf_j1_user_login(?,?,?)}";// 调用的sql
		CallableStatement cs = con.prepareCall(storedProc);
		cs.setString(1, userno.toUpperCase());
		cs.setString(2, "AN");
		cs.registerOutParameter(3,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
		return cs;
		}
		}, new CallableStatementCallback() {
		public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
		List<String> result = new ArrayList<String>();
		cs.execute();

		result.add(cs.getString(3));
		return result;
		}
		});


		return resultList;

		}


	@Override
	public ApiResponseResult getRfSetup(String functionName) throws Exception {
		// TODO Auto-generated method stub
		List<String> a = this.doRfSetup(functionName);
//		[001
//		 [FILED4#进货仓库#VARCHAR2#0#3#60#245#N##1##0##0#23
//		 [FILED2#加急#VARCHAR2#0#1#60#245#N##1##0##0#23
//		 [FILED3#卡板号#VARCHAR2#1#3#60#245#N##1#FILED5#0#999999#0#23
//		 [FILED5#物料条码#VARCHAR2#0#3#60#245#P#{FILED4,FILED3,FILED5,FILED1,FILED2}{FILED1,FILED6,FILED7,INFO}#0##0##0#23
//		 [FILED1#采购单号#VARCHAR2#1#3#60#245#P#{FILED1}{FILED4,FILED3}#1#FILED5#0##0#23
//		 [FILED6#物料编码#VARCHAR2#1#3#60#245#N##1##0##0#23
//		 [FILED7#进货数量#VARCHAR2#1#3#60#245#N##1##0###23
//		 [INFO#提示信息#MEMO#1#3#60#245#N##1##0###100]
		if(a.size()>0){
			String s = a.get(0).substring(0);
			String[] strs = s.split("\\[");
			if(strs.length<1){
				return ApiResponseResult.failure("返回值的格式不正确!"+a);
			}
			//判断取值是否成功
			String str = strs[0];
			if(str.equals("002")){
				return ApiResponseResult.failure("取值发生错误!"+a);
			}
			//拼接字符串
			
			List<String> list = Arrays.asList(strs);
			List arr = new ArrayList<>();
			for(int i=1;i<list.size();i++){
				String[] s1 = list.get(i).split("#");
				arr.add(s1);
			}
			return ApiResponseResult.success("功能界面成功！").data(arr);
		}else{
			return ApiResponseResult.failure("取值为空，请检测输入的参数是否正确!");
		}
		
		//return ApiResponseResult.success("功能界面成功！").data(arr);
	}
	public List<String> doRfSetup(String functionName) {
		List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
		@Override
		public CallableStatement createCallableStatement(Connection con) throws SQLException {
		String storedProc = "{call Prc_rf_setup(?,?,?)}";// 调用的sql
		CallableStatement cs = con.prepareCall(storedProc);
		cs.setString(1, functionName);
		cs.setString(2, "AN");
		cs.registerOutParameter(3,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
		return cs;
		}
		}, new CallableStatementCallback() {
		public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
		List<String> result = new ArrayList<String>();
		cs.execute();

		result.add(cs.getString(3));
		return result;
		}
		});


		return resultList;

		}


	@Override
	public ApiResponseResult queryAppVersion() throws Exception {
		// TODO Auto-generated method stub 
		Map m = new HashMap();
		 List<Map<String, Object>> l = sysUserDao.queryAppVersion();
		 if(l.size() == 0){
			 return ApiResponseResult.success().data(null);
		 }else{
			 return ApiResponseResult.success().data(l.get(0));
		 }
		/* if(l.size() > 0){
			 m.put("Version", l.get(0).get("PV"));
		 }else{
			 return ApiResponseResult.failure("未设置更新版本");
		 }
		 
		 l = sysUserDao.queryApkUrl();
		 if(l.size() > 0){
			 m.put("Url", l.get(0).get("PV"));
		 }else{
			 return ApiResponseResult.failure("未设置更新版本的下载地址");
		 }
		 
		 l = sysUserDao.queryAppSize();
		 if(l.size() > 0){
			 m.put("Size", l.get(0).get("PV"));
		 }else{
			 m.put("Size", 0);
		 }
		
		return ApiResponseResult.success().data(m);*/
	}


	@Override
	public ApiResponseResult changPsw(String userCode, String newp) throws Exception {
		// TODO Auto-generated method stub
		sysUserDao.updatePwsByUserCode(userCode, newp);
		return ApiResponseResult.success();
	}


	@Override
	public ApiResponseResult getExcProc(String functionName, String fileName, String pmachtype, String fileValue, String outFiles)
			throws Exception {
		// TODO Auto-generated method stub
		//[001[4500108372,80000123,100,物料80000123今日已收货 100]

//		List<String[]> a = new ArrayList<String[]>();
//		String str = "4500108372,80000123,100,物料80000123今日已收货 100]";
//		str = str.substring(0,str.length() - 1);
//		String[] files = outFiles.split(",");
//		String[] res = str.split(",");
//		for(int i=0;i<files.length;i++){
//			String[] temp = new String[2];
//			temp[0] = files[i];
//			temp[1] = res[i];
//			//m.put(files[i], res[i]);
//			a.add(temp);
//		}
//		return ApiResponseResult.success().data(a);
		List<String> a = this.doExcProc(functionName,fileName,pmachtype,fileValue);
		System.out.println(a);
		if(a.size()>0){
			String s = a.get(0).substring(0);
			String[] strs = s.split("\\[");
			if(strs.length<1){
				return ApiResponseResult.failure("返回值的格式不正确!"+a);
			}
			//判断取值是否成功
			String str = strs[0];
			if(str.equals("002")){
				return ApiResponseResult.failure(strs[1]);
			}else{
				if(StringUtils.isEmpty(outFiles)){
					if(strs.length < 2){
						return ApiResponseResult.success().data("");
					}
					return ApiResponseResult.success().data(strs[1]);
				}else{
					List<String[]> at = new ArrayList<String[]>();
					//[001[4500108372,80000123,100,物料80000123今日已收货 100]
					String[] files = outFiles.split(",");
					String[] res = strs[1].split(",", -1);
					for(int i=0;i<files.length;i++){
						String[] temp = new String[2];
						temp[0] = files[i];
						temp[1] = res[i];
						//m.put(files[i], res[i]);
						at.add(temp);
					}
					return ApiResponseResult.success().data(at);
				}
				
			}
			
		}
		return ApiResponseResult.success().data(a);
	}

	public List<String> doExcProc(String functionName, String fileName, String pmachtype, String fileValue) {
		List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
		@Override
		public CallableStatement createCallableStatement(Connection con) throws SQLException {
		String storedProc = "{call  Prc_rf_setup_ExcProc(?,?,?,?,?)}";// 调用的sql
		CallableStatement cs = con.prepareCall(storedProc);
		cs.setString(1, functionName);//功能名称
		cs.setString(2, fileName.trim());//字段名
		cs.setString(3,"AN");//设备类型 wince5,wmb5,wmb6
		cs.setString(4, fileValue);//参数值[第一位是用户]
		cs.registerOutParameter(5,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
		return cs;
		}
		}, new CallableStatementCallback() {
		public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
		List<String> result = new ArrayList<String>();
		cs.execute();

		result.add(cs.getString(5));
		return result;
		}
		});


		return resultList;

		}

	@Override
	public SysUser findByUserCode2(String userCode) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponseResult getListRole() throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success("").data(sysRoleDao.findByDelFlagAndStatus(0,0));
	}
	
	@Override
	public ApiResponseResult getOrgList() throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success("").data(sysUserDao.queryOrgList());
	}

	@Override
	public List<Map<String, Object>> queryTimeOut() {
		// TODO Auto-generated method stub
		return sysUserDao.queryTimeOut();
	}

    @Override
    public ApiResponseResult getDept() throws  Exception{
        List<Object> list = getDeptPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                "","用户部门", "prc_mes_cof_org_chs");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success("").data(list.get(3));
    }
	
}
