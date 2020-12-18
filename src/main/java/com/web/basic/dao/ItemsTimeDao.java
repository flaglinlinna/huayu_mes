package com.web.basic.dao;

import com.web.basic.entity.ItemsTime;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ItemsTimeDao extends CrudRepository<ItemsTime, Long>,JpaSpecificationExecutor<ItemsTime>{

	public List<ItemsTime> findAll();
	public List<ItemsTime> findByDelFlag(Integer delFlag);
	public ItemsTime findById(long id);
	Integer countByDelFlagAndItemNo(Integer delFlag, String itemNo);
}
