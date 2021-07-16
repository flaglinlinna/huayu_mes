package com.web.project.dao;

import com.web.project.entity.ProjectDuty;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProjectDutyDao extends CrudRepository<ProjectDuty, Long>,JpaSpecificationExecutor<ProjectDuty>{

	public List<ProjectDuty> findAll();
	public List<ProjectDuty> findByDelFlag(Integer delFlag);
	public ProjectDuty findById(long id);
//	public int countByDelFlagAndCustNo(Integer delFlag, String custNo);//查询deCode是否存在
}
