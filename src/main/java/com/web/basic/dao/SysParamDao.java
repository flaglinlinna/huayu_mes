package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.SysParam;;

public interface SysParamDao extends CrudRepository<SysParam, Long>,JpaSpecificationExecutor<SysParam>{

	public List<SysParam> findAll();
	public List<SysParam> findByDelFlag(Integer delFlag);
	public SysParam findById(long id);
	public int countByDelFlagAndParamCode(Integer delFlag, String paramCode);//查询paramCode是否存在
}
