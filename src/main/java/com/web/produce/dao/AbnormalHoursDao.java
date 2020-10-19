package com.web.produce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.CardData;
import com.web.produce.entity.AbnormalHours;

import java.util.List;
import java.util.Map;


public interface AbnormalHoursDao extends CrudRepository<AbnormalHours, Long>,JpaSpecificationExecutor<AbnormalHours>{

	public List<AbnormalHours> findAll();
	
	public List<AbnormalHours> findByDelFlag(Integer delFlag);
	
	public AbnormalHours findById(long id);
}
