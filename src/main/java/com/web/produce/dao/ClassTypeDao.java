package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.ClassType;

import java.util.List;


public interface ClassTypeDao extends CrudRepository<ClassType, Long>,JpaSpecificationExecutor<ClassType>{

	public List<ClassType> findAll();
	public List<ClassType> findByDelFlag(Integer delFlag);
}
