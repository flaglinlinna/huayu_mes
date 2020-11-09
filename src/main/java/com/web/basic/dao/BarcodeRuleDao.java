package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.BarcodeRule;

public interface BarcodeRuleDao extends CrudRepository<BarcodeRule, Long>,JpaSpecificationExecutor<BarcodeRule>{

	public List<BarcodeRule> findAll();
	
	public List<BarcodeRule> findByDelFlag(Integer delFlag);
	
	public BarcodeRule findById(long id);
	
	//public int countByDelFlagAndCustNo(Integer delFlag, String custNo);//查询deCode是否存在
}
