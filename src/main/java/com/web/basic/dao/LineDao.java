package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Line;;

public interface LineDao extends CrudRepository<Line, Long>,JpaSpecificationExecutor<Line>{

	public List<Line> findAll();
	public List<Line> findByDelFlag(Integer delFlag);
	public Line findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询deCode是否存在
}
