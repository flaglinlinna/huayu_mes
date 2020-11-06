package com.system.role.service.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.system.permission.dao.SysPermissionDao;
import com.system.permission.entity.SysPermission;
import com.system.role.dao.RolePermissionMapDao;
import com.system.role.entity.RolePermissionMap;
import com.system.user.dao.UserRoleMapDao;
import com.system.user.entity.UserRoleMap;
import com.utils.BaseService;
import com.utils.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.role.dao.SysRoleDao;
import com.system.role.entity.SysRole;
import com.system.role.service.SysRoleService;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;

/**
 * 角色
 *
 */
@Service(value = "sysRoleService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysRoleImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private RolePermissionMapDao rolePermissionMapDao;
    @Autowired
    private UserRoleMapDao userRoleMapDao;
    @Autowired
    private SysPermissionDao sysPermissionDao;

    /**
     * 新增角色
     * @param sysRole
     * @return
     * @author fyx
     * @serialData 2018-11-21
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult add(SysRole sysRole) throws Exception{
        if(sysRole == null){
            return ApiResponseResult.failure("角色不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getRoleCode())){
            return ApiResponseResult.failure("角色编号不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getRoleName())){
            return ApiResponseResult.failure("角色名称不能为空！");
        }
        int count = sysRoleDao.countByDelFlagAndRoleCode(0, sysRole.getRoleCode());
        if(count > 0){
            return ApiResponseResult.failure("该角色已存在，请填写其他角色编号！");
        }

        sysRole.setCreateDate(new Date());
        sysRoleDao.save(sysRole);

        return ApiResponseResult.success("角色添加成功！").data(sysRole);
    }

	@Override
    @Transactional
    public ApiResponseResult edit(SysRole sysRole) throws Exception {
        if(sysRole == null){
            return ApiResponseResult.failure("角色不能为空！");
        }
        if(sysRole.getId() == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getRoleCode())){
            return ApiResponseResult.failure("角色编号不能为空！");
        }
        if(StringUtils.isEmpty(sysRole.getRoleName())){
            return ApiResponseResult.failure("角色名称不能为空！");
        }
        SysRole o = sysRoleDao.findById((long) sysRole.getId());
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }

        //判断角色编号是否有变化，有则修改；没有则不修改
        String originalCode = o.getRoleCode();
        if(o.getRoleCode().equals(sysRole.getRoleCode())){
        }else{
            int count = sysRoleDao.countByDelFlagAndRoleCode(0, sysRole.getRoleCode());
            if(count > 0){
                return ApiResponseResult.failure("角色编号已存在，请填写其他角色编号！");
            }
            o.setRoleCode(sysRole.getRoleCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setRoleName(sysRole.getRoleName());
        o.setDescription(sysRole.getDescription());
        sysRoleDao.save(o);

        return ApiResponseResult.success("编辑成功！");
	}

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        SysRole o  = sysRoleDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }

        o.setLastupdateDate(new Date());
        o.setDelFlag(1);
        sysRoleDao.save(o);

        return ApiResponseResult.success("删除成功！");
    }

	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, String roleCode,String roleName, Date createdTimeStart, Date createdTimeEnd, Integer status, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if(StringUtils.isNotEmpty(roleCode)){
            filters.add(new SearchFilter("roleCode", SearchFilter.Operator.LIKE, roleCode));
        }
        if(StringUtils.isNotEmpty(roleName)){
            filters.add(new SearchFilter("roleName", SearchFilter.Operator.LIKE, roleName));
        }
        if(createdTimeStart != null){
            filters.add(new SearchFilter("createDate", SearchFilter.Operator.GTE, createdTimeStart));
        }
        if(createdTimeEnd != null){
            filters.add(new SearchFilter("createDate", SearchFilter.Operator.LTE, createdTimeEnd));
        }
        if(status != null){
            filters.add(new SearchFilter("status", SearchFilter.Operator.EQ, status));
        }
        //查询2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("roleCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("roleName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("description", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<SysRole> spec = Specification.where(BaseService.and(filters, SysRole.class));
        Specification<SysRole> spec1 =  spec.and(BaseService.or(filters1, SysRole.class));
        Page<SysRole> page = sysRoleDao.findAll(spec1, pageRequest);
        List<SysRole> sysRoleList = page.getContent();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(SysRole sysRole:sysRoleList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",sysRole.getId());
            map.put("roleName",sysRole.getRoleName());
            map.put("roleCode",sysRole.getRoleCode());
            map.put("description",sysRole.getDescription());
            map.put("status",sysRole.getStatus());
            map.put("createDate",df.format(sysRole.getCreateDate()));
            //map.put("lastupdateDate",df.format(sysRole.getLastupdateDate()));
            map.put("userCount",userRoleMapDao.countByRoleIdAndAndDelFlag(sysRole.getId(),0));
            mapList.add(map);
        }

        return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	@Override
	public ApiResponseResult getCheckedRoles(long userId) throws Exception {
		Map map = new HashMap();
		return ApiResponseResult.success().data(map);
	}
	@Override
    @Transactional
	public ApiResponseResult saveUserRoles(long userId,String roles) throws Exception {
		return ApiResponseResult.success("修改成功！");
	}

	@Override
	public ApiResponseResult addRouter(String rolecode, String roles) throws Exception {
		return ApiResponseResult.success();
	}

	@Override
	public ApiResponseResult getRouter(String rolecode) throws Exception {
		return null;
	}

    /**
     * 获取当前角色的操作权限
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponseResult getPermission() throws Exception{
        SysUser currUser = UserUtil.getCurrUser();  //获取当前用户
        if(currUser == null || currUser.getId() == null){
            return ApiResponseResult.failure("当前用户不存在！");
        }
//        if(currUser == null || currUser.getFid() == null){
//            return ApiResponseResult.failure("当前用户不存在！");
//        }

        //1.初始化
        int isSuper = 0;
        String perm = "";

        //2.判断是否为管理员用户，是则无需获取操作权限；否则获取操作权限
//        if(currUser.getUserIsSuper() == 1){
//            //2.1
//            isSuper = 1;
//        }else{
//            //2.2获取当前用户所属角色
//            List<UserRolesMap> mapList = userRolesMapDao.findByDelFlagAndUserId(0, currUser.getId());
//            if(mapList != null && mapList.size() > 0){
//                for(UserRolesMap map : mapList){
//                    if(map != null && map.getUserId() != null){
//                        //获取角色
//                        SysRole role = sysRoleDao.findById((long) map.getRoleId());
//                        if(role != null && StringUtils.isNotEmpty(role.getBsPermission())){
//                            perm = StringUtils.isNotEmpty(perm) ? (perm+","+role.getBsPermission()) : role.getBsPermission();
//                        }
//                    }
//                }
//            }
//        }

        //3.封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("isSuper", isSuper);
        map.put("perm", perm);

        return ApiResponseResult.success().data(map);
    }

    /**
     * 根据ID获取角色
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getRole(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        SysRole o = sysRoleDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }
        return ApiResponseResult.success().data(o);
    }

    /**
     * 获取所有角色
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponseResult getRoles() throws Exception{
        List<SearchFilter> filters = new ArrayList<>();
        Specification<SysRole> spec = Specification.where(BaseService.and(filters, SysRole.class));
        List<SysRole> list = sysRoleDao.findAll(spec);
        return ApiResponseResult.success().data(list);
    }

    /**
     * 根据角色ID获取权限信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getRolePerm(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        SysRole o = sysRoleDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该角色不存在！");
        }

        //获取当前角色下角色权限关联信息
        List<RolePermissionMap> list = rolePermissionMapDao.findByDelFlagAndAndRoleId(0, id);

        //获取所有权限信息
        List<SysPermission> list2 = sysPermissionDao.findByDelFlag(0);

        List<Map<String, Object>> mapList = new ArrayList<>();
        for(SysPermission permItem : list2) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", permItem.getId()!=null ? permItem.getId().toString() : "");
            map.put("code", permItem.getMenuCode()!=null ? permItem.getMenuCode().toString() : "");
            map.put("name", permItem.getMenuName()!=null ? permItem.getMenuName().toString() : "");
            map.put("pId", permItem.getParentId()!=null ? permItem.getParentId().toString() : "");
            map.put("zindex", permItem.getZindex()!=null ? permItem.getZindex().toString() : "");
            map.put("istype", permItem.getIstype()!=null ? permItem.getIstype().toString() : "");
            map.put("descpt", permItem.getDescription()!=null ? permItem.getDescription().toString() : "");
            map.put("icon", permItem.getMenuIcon()!=null ? permItem.getMenuIcon().toString() : "");
            map.put("page", permItem.getPageUrl()!=null ? permItem.getPageUrl().toString() : "");
            map.put("checked", false);
            map.put("open", true);
            //判断是否有权限
            for(RolePermissionMap rolePermItem : list){
                if(rolePermItem.getPermitId()!=null && rolePermItem.getPermitId().equals(permItem.getId())){
                    map.put("checked", true);
                }
            }
            mapList.add(map);
        }

        return ApiResponseResult.success().data(mapList);
    }

    /**
     * 设置权限
     * @param roleId
     * @param permIds
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doRolePerm(Long roleId, String permIds) throws Exception{
        if(roleId == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        //转换
        String[] permIdArray = permIds.split(",");
        List<Long> permIdList = new ArrayList<Long>();
        for(int i = 0; i < permIdArray.length; i++){
            if(StringUtils.isNotEmpty(permIdArray[i])) {
                permIdList.add(Long.parseLong(permIdArray[i]));
            }
        }

        //1.删除角色原权限信息
        List<RolePermissionMap> listOld = rolePermissionMapDao.findByDelFlagAndAndRoleId(0, roleId);
        if(listOld.size() > 0){
            for(RolePermissionMap item : listOld){
                item.setLastupdateDate(new Date());
                item.setDelFlag(1);
            }
            rolePermissionMapDao.saveAll(listOld);
        }

        //2.添加角色新权限信息
        List<RolePermissionMap> listNew = new ArrayList<>();
        if(permIdList.size() > 0){
            for(Long permId : permIdList){
                RolePermissionMap item = new RolePermissionMap();
                item.setCreateDate(new Date());
                item.setRoleId(roleId);
                item.setPermitId(permId);
                listNew.add(item);
            }
            rolePermissionMapDao.saveAll(listNew);
        }

        return ApiResponseResult.success("设置权限成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer status) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("角色ID不能为空！");
        }
        if(status == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        SysRole o = sysRoleDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("角色不存在！");
        }

        o.setLastupdateDate(new Date());
        o.setStatus(status);
        sysRoleDao.save(o);

        return ApiResponseResult.success("设置成功！").data(o);
    }
}
