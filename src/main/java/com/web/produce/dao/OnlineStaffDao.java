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
	
}
