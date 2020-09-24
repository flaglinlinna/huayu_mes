package com.system.organization.service.internal;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.organization.dao.OrganizationDao;
import com.system.organization.entity.SysOrganization;
import com.system.organization.service.OrganizationService;
import com.system.permission.entity.SysPermission;

/**
 * 菜单
 */
@Service(value = "OrganizationService")
@Transactional(propagation = Propagation.REQUIRED)
public class OrganizationImpl implements OrganizationService {

    @Autowired
    private OrganizationDao organizationDao;

	@Override
	public List<SysOrganization> permList() {
		// TODO Auto-generated method stub
		/*Iterable<SysPermission> geted = sysPermissionDao.findAll();
		List<SysPermission> list =  Lists.newArrayList(geted);
		return list;*/
		return organizationDao.findByDelFlag(0);
	}

	@Override
	public ApiResponseResult delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		SysOrganization o = organizationDao.findByIdAndDelFlag((long)id,0);
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除").status("error1");
        }
		
		List<SysOrganization> list = organizationDao.findByDelFlagAndParentId(0, id);
		if(list.size() >0){
			return  ApiResponseResult.failure("删除失败，请您先删除该权限的子节点");
		}

        o.setDelFlag(1);
        o.setCreateDate(new Date());
        o.setLastupdateDate(new Date());
        organizationDao.save(o);
        return ApiResponseResult.success("删除成功！");

	}

	@Override
	public ApiResponseResult getPermission(Long id) throws Exception {
		// TODO Auto-generated method stub
		SysOrganization o = organizationDao.findByIdAndDelFlag((long)id,0);
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除").status("error1");
        }
		return ApiResponseResult.success().data(o);
	}

	@Override
	public ApiResponseResult savePerm(SysOrganization perm) throws Exception {
		// TODO Auto-generated method stub
		if(perm.getId() == null){
			//新增
			organizationDao.save(perm);
		}else{
			//修改
			SysOrganization s = organizationDao.findByIdAndDelFlag(perm.getId(), 0);
			s.setBsCode(perm.getBsCode());
			s.setBsName(perm.getBsName());
			s.setBsLevel(perm.getBsLevel());
			s.setBsPrincipal(perm.getBsPrincipal());
			s.setBsMobile(perm.getBsMobile());
			s.setBsZindex(perm.getBsZindex());
			s.setDescpt(perm.getDescpt());
			s.setLastupdateDate(new Date());
			organizationDao.save(s);
		}
		
		return ApiResponseResult.success("操作成功");
	}

	@Override
	public ApiResponseResult getUserPerms(Long id) throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(organizationDao.getUserPerms(id));
	}
    
	
    

}
