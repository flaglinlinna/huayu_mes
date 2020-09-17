package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.ChkBad;;

public interface ChkBadDao extends CrudRepository<ChkBad, Long>,JpaSpecificationExecutor<ChkBad>{
	
	public List<ChkBad> findAll();
	public List<ChkBad> findByIsDel(Integer isDel);
	public List<ChkBad> findByIsDelAndBsStatus(Integer isDel,Integer bsStatus);
	public ChkBad findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
