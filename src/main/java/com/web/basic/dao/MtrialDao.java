package com.web.basic.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basic.entity.Mtrial;

public interface MtrialDao extends CrudRepository<Mtrial, Long>,JpaSpecificationExecutor<Mtrial>{
	
	public List<Mtrial> findAll();
	public List<Mtrial> findByDelFlag(Integer delFlag);
	public List<Mtrial> findByDelFlagAndCheckStatus(Integer delFlag,Integer bsStauts);
	public Mtrial findById(long id);
	public int countByDelFlagAndItemNo(Integer delFlag, String bsCode);//查询Code是否存在
    public List<Mtrial> findByDelFlagAndItemNo(Integer delFlag, String bsCode);
    
    /**
     * 获取成品或者半成品的物料
     */
    @Query(value = "select mtrial from Mtrial mtrial  where mtrial.delFlag=0 and mtrial.checkStatus=1 and (mtrial.itemType='成品' or mtrial.itemType='半成品') ")
	public List<Mtrial> findProd();
}
