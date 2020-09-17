package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.ProdProc;;

public interface ProdProcDao extends CrudRepository<ProdProc, Long>,JpaSpecificationExecutor<ProdProc>{
	
	public List<ProdProc> findAll();
	//public List<ProdProc> findByIsDel(Integer isDel);
	public ProdProc findById(long id);
	//public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
