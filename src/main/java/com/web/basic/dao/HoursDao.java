package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Hours;;

public interface HoursDao extends CrudRepository<Hours, Long>,JpaSpecificationExecutor<Hours>{
	
	public List<Hours> findAll();
	public List<Hours> findByDelFlag(Integer delFlag);
	public Hours findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询Code是否存在
}
