package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.WoCenter;;

public interface WoCenterDao extends CrudRepository<WoCenter, Long>,JpaSpecificationExecutor<WoCenter>{
	
	public List<WoCenter> findAll();
	public List<WoCenter> findByIsDel(Integer isDel);
	public WoCenter findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
