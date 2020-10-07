package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.EmpFinger;

import java.util.List;


public interface EmpFingerDao extends CrudRepository<EmpFinger, Long>,JpaSpecificationExecutor<EmpFinger>{

	public List<EmpFinger> findAll();
	public List<EmpFinger> findByDelFlag(Integer delFlag);
	public EmpFinger findById(long id);
	public int countByDelFlagAndTemplateStr(Integer delFlag, String templateStr);//查询指纹是否存在
	
}
