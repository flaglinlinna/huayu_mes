package com.web.quote.dao;

import com.web.quote.entity.HardwareMater;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface HardwareDao extends CrudRepository<HardwareMater, Long>,JpaSpecificationExecutor<HardwareMater>{

	public List<HardwareMater> findAll();
	HardwareMater findById(long id);
	public List<HardwareMater> findByDelFlag(Integer delFlag);
}
