package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.DefectiveDetail;;

public interface DefectiveDetailDao extends CrudRepository<DefectiveDetail, Long>,JpaSpecificationExecutor<DefectiveDetail>{
	
	public List<DefectiveDetail> findAll();
	public List<DefectiveDetail> findByDelFlag(Integer delFlag);
	public DefectiveDetail findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询deCode是否存在
}
