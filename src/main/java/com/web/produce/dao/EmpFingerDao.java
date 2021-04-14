package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.permission.entity.SysPermission;
import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserRoleMap;
import com.web.produce.entity.EmpFinger;

import java.util.List;
import java.util.Map;


public interface EmpFingerDao extends CrudRepository<EmpFinger, Long>,JpaSpecificationExecutor<EmpFinger>{

	public List<EmpFinger> findAll();
	
	public List<EmpFinger> findByDelFlag(Integer delFlag);
	
	public List<EmpFinger> findByDelFlagAndEmpId(Integer delFlag, Long empId);
	
	
	public List<EmpFinger> findByDelFlagAndEmpIdAndFingerIdx(Integer delFlag, Long empId,String fg);
	
	public EmpFinger findById(long id);
	
	public int countByDelFlagAndTemplateStr(Integer delFlag, String templateStr);//查询指纹是否存在
	
	public int countByDelFlagAndEmpId(Integer delFlag, Long empId);//查询员工指纹记录数
	
	public int countByDelFlagAndEmpIdAndFingerIdx(Integer delFlag, Long empId,String fingerIdx);//员工+手指序号保证唯一


//	public List<Map<String,Object>> findByEmpStatus(Integer status);

}
