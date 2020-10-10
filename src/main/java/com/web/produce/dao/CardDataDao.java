package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.CardData;

import java.util.List;


public interface CardDataDao extends CrudRepository<CardData, Long>,JpaSpecificationExecutor<CardData>{

	public List<CardData> findAll();
	
	public List<CardData> findByDelFlag(Integer delFlag);
	
	public List<CardData> findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTime(Integer delFlag,Long emppId,Long devClockId,String carDate,String cardTime);
	
	public CardData findById(long id);
}
