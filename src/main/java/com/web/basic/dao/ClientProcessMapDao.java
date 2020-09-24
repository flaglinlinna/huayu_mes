package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.ClientProcessMap;;

public interface ClientProcessMapDao extends CrudRepository<ClientProcessMap, Long>,JpaSpecificationExecutor<ClientProcessMap>{
	
	public List<ClientProcessMap> findAll();
	public List<ClientProcessMap> findByDelFlag(Integer delFlag);
	public ClientProcessMap findById(long id);
	public List<ClientProcessMap> findByDelFlagAndPkClient(Integer delFlag,Long pkClient);//查找物料及其工序集合
	//public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询deCode是否存在
}
