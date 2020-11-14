package com.web.basic.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basic.entity.ClientProcessMap;

public interface ClientProcessMapDao extends CrudRepository<ClientProcessMap, Long>,JpaSpecificationExecutor<ClientProcessMap>{
	
	public List<ClientProcessMap> findAll();
	public List<ClientProcessMap> findByDelFlag(Integer delFlag);
	public ClientProcessMap findById(long id);
	public List<ClientProcessMap> findByDelFlagAndFdemoName(Integer delFlag,String fdemoName);//查找物料及其工序集合
	//public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询deCode是否存在

	public  List<ClientProcessMap> findByFdemoName(String fdemoName);

	public List<ClientProcessMap> findByDelFlagAndFdemoNameAndProcOrder(Integer delFlag,String fdemoName,Integer order);
	/**
     * 获取已经配置了的客户信息
     */
    @Query(value = "select  map.fdemoName, map.fdemoName from ClientProcessMap map  where map.delFlag = 0  group by map.fdemoName")
	public  List<ClientProcessMap> findClient();
}
