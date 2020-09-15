package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.WoProc;;

public interface WoProcDao extends CrudRepository<WoProc, Long>,JpaSpecificationExecutor<WoProc>{
	
	public List<WoProc> findAll();
	public List<WoProc> findByIsDel(Integer isDel);
	public WoProc findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
