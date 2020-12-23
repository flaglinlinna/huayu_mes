package com.web.basic.dao;

import com.web.basic.entity.SysParamSub;
import com.web.quote.entity.QuoteItemBase;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface SysParamSubDao extends CrudRepository<SysParamSub, Long>,JpaSpecificationExecutor<SysParamSub>{

//	public List<SysParamSub> findByDelFlagAndMid(Integer delFlag,Long mid);

	Integer countByMidAndDelFlagAndSubCode(Long mid,Integer delFlag,String subCode);

	Integer countByMidAndDelFlagAndSubName(Long mid,Integer delFlag,String subName);

	public SysParamSub findById(long id);

//	public List<SysParamSub> findByDelFlagAndParamCode(Integer delFlag, String paramCode);
	
	@Query(value = "select map from SysParamSub map left join SysParam s on s.id=map.mid where map.delFlag=0 and s.delFlag=0 and s.paramCode=?1 ")
	public  List<SysParamSub> findByDelFlagAndCode(String paramCode);
	
}
