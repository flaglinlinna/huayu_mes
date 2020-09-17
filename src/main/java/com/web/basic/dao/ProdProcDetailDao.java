package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.ProdProcDetail;;

public interface ProdProcDetailDao extends CrudRepository<ProdProcDetail, Long>,JpaSpecificationExecutor<ProdProcDetail>{
	
	public List<ProdProcDetail> findAll();
	//public List<ProdProcDetai> findByIsDel(Integer isDel);
	public ProdProcDetail findById(long id);
	//public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
