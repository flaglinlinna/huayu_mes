package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Client;;

public interface ClientDao extends CrudRepository<Client, Long>,JpaSpecificationExecutor<Client>{

	public List<Client> findAll();
	public List<Client> findByDelFlag(Integer delFlag);
	public Client findById(long id);
	public int countByDelFlagAndCustNo(Integer delFlag, String custNo);//查询deCode是否存在
}
