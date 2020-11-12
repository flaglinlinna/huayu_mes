package com.web.basic.dao;

import com.web.basic.entity.Abnormal;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;



public interface AbnormalDao extends CrudRepository<Abnormal, Long>,JpaSpecificationExecutor<Abnormal>{

	public List<Abnormal> findAll();
//	public List<Abnormal> findByDelFlag(Integer delFlag);
	public Abnormal findById(long id);
	public int countByDelFlagAndAbnormalCode(Integer delFlag, String Code);//查询Code是否存在
}
