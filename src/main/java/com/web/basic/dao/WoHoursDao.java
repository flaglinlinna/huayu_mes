package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.WoHours;;

public interface WoHoursDao extends CrudRepository<WoHours, Long>,JpaSpecificationExecutor<WoHours>{
	
	public List<WoHours> findAll();
	public List<WoHours> findByIsDel(Integer isDel);
	public WoHours findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询Code是否存在
}
