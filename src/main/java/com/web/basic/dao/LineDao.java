package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Line;;

public interface LineDao extends CrudRepository<Line, Long>,JpaSpecificationExecutor<Line>{
	
	public List<Line> findAll();
	public List<Line> findByIsDel(Integer isDel);
	public Line findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
