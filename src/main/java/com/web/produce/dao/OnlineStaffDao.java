package com.web.produce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.OnlineStaff;

import java.util.List;
import java.util.Map;


public interface OnlineStaffDao extends CrudRepository<OnlineStaff, Long>,JpaSpecificationExecutor<OnlineStaff>{

	public List<OnlineStaff> findAll();
	
	public List<OnlineStaff> findByDelFlag(Integer delFlag);
	
	public OnlineStaff findById(long id);
	
	@Query(value = "select c.class_no,c.id,c.class_name from MES_BASE_CLASS_TYPE c where c.del_flag=0 ", nativeQuery = true)
    public List<Map<String, Object>> queryClass();
	
	@Query(value = "select distinct t1.liner_name from MES_LINE_AFFIRM t left join  MES_PROD_ORDER t1 on t.task_no=t1.task_no where t.task_no=?1", nativeQuery = true)
    public List<Map<String, Object>> queryLinerName(String taskNo);
	
}
