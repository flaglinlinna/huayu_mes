package com.system.permission.service.internal;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.base.data.ApiResponseResult;
import com.google.common.collect.Lists;
import com.system.permission.dao.SysPermissionDao;
import com.system.permission.entity.SysPermission;
import com.system.permission.service.SysPermissionService;

import io.swagger.annotations.ApiOperation;

/**
 * 菜单
 */
@Service(value = "SysPermissionService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysPermissionImpl implements SysPermissionService {

    @Autowired
    private SysPermissionDao sysPermissionDao;

	@Override
	public List<SysPermission> permList() {
		// TODO Auto-generated method stub
		/*Iterable<SysPermission> geted = sysPermissionDao.findAll();
		List<SysPermission> list =  Lists.newArrayList(geted);
		return list;*/
		return sysPermissionDao.findByDelFlag(0);
	}

	@Override
	public ApiResponseResult delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		SysPermission o = sysPermissionDao.findByIdAndDelFlag((long)id,0);
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除").status("error1");
        }
		
		List<SysPermission> list = sysPermissionDao.findByDelFlagAndParentId(0, id);
		if(list.size() >0){
			return  ApiResponseResult.failure("删除失败，请您先删除该权限的子节点");
		}

        o.setDelFlag(1);
        o.setCreateDate(new Date());
        o.setLastupdateDate(new Date());
        sysPermissionDao.save(o);
        return ApiResponseResult.success("删除成功！");

	}

	@Override
	public ApiResponseResult getPermission(Long id) throws Exception {
		// TODO Auto-generated method stub
		SysPermission o = sysPermissionDao.findByIdAndDelFlag((long)id,0);
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除").status("error1");
        }
		return ApiResponseResult.success().data(o);
	}

	@Override
	public ApiResponseResult savePerm(SysPermission perm) throws Exception {
		// TODO Auto-generated method stub
		if(perm.getId() == null){
			//新增
			sysPermissionDao.save(perm);
		}else{
			//修改
			SysPermission s = sysPermissionDao.findByIdAndDelFlag(perm.getId(), 0);
			s.setBsCode(perm.getBsCode());
			s.setBsIcon(perm.getBsIcon());
			s.setPageUrl(perm.getPageUrl());
			s.setBsName(perm.getBsName());
			s.setZindex(perm.getZindex());
			s.setDescpt(perm.getDescpt());
			s.setLastupdateDate(new Date());
			sysPermissionDao.save(s);
		}
		
		return ApiResponseResult.success("操作成功");
	}

	@Override
	public ApiResponseResult getUserPerms(Long id) throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(sysPermissionDao.getUserPerms(id));
	}
    
	
    

}
