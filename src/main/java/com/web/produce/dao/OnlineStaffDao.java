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
	
}
