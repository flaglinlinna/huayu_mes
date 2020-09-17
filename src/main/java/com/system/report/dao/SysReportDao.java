package com.system.report.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.system.permission.entity.SysPermission;
import com.system.report.entity.SysReport;

/**
 * 菜单基础信息表
 */
public interface SysReportDao extends CrudRepository<SysReport, Long>, JpaSpecificationExecutor<SysReport> {
	
	public List<SysPermission> findByIsDel(Integer isDel);
	
}
