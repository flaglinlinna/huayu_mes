package com.web.basic.dao;

import com.web.basic.entity.SysParamSub;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SysParamSubDao extends CrudRepository<SysParamSub, Long>,JpaSpecificationExecutor<SysParamSub>{

//	public List<SysParamSub> findByDelFlagAndMid(Integer delFlag,Long mid);

	Integer countByMidAndDelFlagAndSubCode(Long mid,Integer delFlag,String subCode);

	Integer countByMidAndDelFlagAndSubName(Long mid,Integer delFlag,String subName);

	public SysParamSub findById(long id);

//	public List<SysParamSub> findByDelFlagAndParamCode(Integer delFlag, String paramCode);
	
}
