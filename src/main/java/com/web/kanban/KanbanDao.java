package com.web.kanban;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.system.permission.entity.SysPermission;
import com.system.role.entity.RolePermissionMap;
import com.system.role.entity.SysRole;
import com.system.user.entity.UserRoleMap;

public interface KanbanDao extends CrudRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

	@Query(value = "SELECT A.Org_Name,A.ORG_PATH,A.LEAD_BY,A.Id FROM V_SYS_ORG_TREE A", nativeQuery = true)
    public List<Map<String, Object>> getDepList();
	
	//获取刷新单个间隔时间
	@Query(value = "SELECT f_get_parameter_val('MES_KB_TIME') A FROM DUAL", nativeQuery = true)
    public List<Map<String, Object>> getIntervalTime();
	
	//获取执行轮播-跳转到下一页面的时间
	@Query(value = "SELECT f_get_parameter_val('MES_KB_TIME_CAROUSEL') R FROM DUAL", nativeQuery = true)
	public List<Map<String, Object>> getRotationTime();
}
