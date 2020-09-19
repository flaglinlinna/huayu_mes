package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.ClientProcessMap;;

public interface ClientProcessMapDao extends CrudRepository<ClientProcessMap, Long>,JpaSpecificationExecutor<ClientProcessMap>{
	
	public List<ClientProcessMap> findAll();
	public List<ClientProcessMap> findByIsDel(Integer isDel);
	public ClientProcessMap findById(long id);
	public List<ClientProcessMap> findByIsDelAndPkClient(Integer isDel,Long pkClient);//查找物料及其工序集合
	//public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
