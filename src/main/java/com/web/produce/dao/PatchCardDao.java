package com.web.produce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.CardData;
import com.web.produce.entity.PatchCard;

import java.util.List;
import java.util.Map;


public interface PatchCardDao extends CrudRepository<PatchCard, Long>,JpaSpecificationExecutor<PatchCard>{

	public List<PatchCard> findAll();
	
	public List<PatchCard> findByDelFlag(Integer delFlag);
	
	public PatchCard findById(long id);
	
	public int countByDelFlagAndEmpIdAndSignTimeAndSignDate(Integer delFlag,Long empId,String signTime,String signDate);
	
	@Query(value = "select c.class_no,c.id,c.class_name from MES_BASE_CLASS_TYPE c where c.del_flag=0 ", nativeQuery = true)
    public List<Map<String, Object>> queryClass();
}