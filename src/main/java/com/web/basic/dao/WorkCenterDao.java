package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.WorkCenter;;

public interface WorkCenterDao extends CrudRepository<WorkCenter, Long>,JpaSpecificationExecutor<WorkCenter>{
	
	public List<WorkCenter> findAll();
	public List<WorkCenter> findByIsDel(Integer isDel);
	public WorkCenter findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
