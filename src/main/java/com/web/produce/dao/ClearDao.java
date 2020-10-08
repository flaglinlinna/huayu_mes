package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.Clear;

import java.util.List;


public interface ClearDao extends CrudRepository<Clear, Long>,JpaSpecificationExecutor<Clear>{

	public List<Clear> findAll();
	public List<Clear> findByDelFlag(Integer delFlag);
	public Clear findById(long id);
	public  List<Clear> findByDelFlagAndDevClockId(Integer delFlag,long devClockId);
}
