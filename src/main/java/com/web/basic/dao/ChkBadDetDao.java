package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.ChkBadDet;;

public interface ChkBadDetDao extends CrudRepository<ChkBadDet, Long>,JpaSpecificationExecutor<ChkBadDet>{
	
	public List<ChkBadDet> findAll();
	public List<ChkBadDet> findByIsDel(Integer isDel);
	public ChkBadDet findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
