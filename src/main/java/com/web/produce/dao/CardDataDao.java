package com.web.produce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.CardData;

import java.util.List;
import java.util.Map;


public interface CardDataDao extends CrudRepository<CardData, Long>,JpaSpecificationExecutor<CardData>{

	public List<CardData> findAll();
	
	public List<CardData> findByDelFlag(Integer delFlag);
	
	public List<CardData> findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTimeAndFstatus(Integer delFlag,Long emppId,Long devClockId,String carDate,String cardTime,Integer fstatus);
	
	public List<CardData> findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTime(Integer delFlag,Long emppId,Long devClockId,String carDate,String cardTime);
	
	public CardData findById(long id);
	
	@Query(value = "select c.class_no,c.id,c.class_name from MES_BASE_CLASS_TYPE c where c.del_flag=0 ", nativeQuery = true)
    public List<Map<String, Object>> queryClass();
}
