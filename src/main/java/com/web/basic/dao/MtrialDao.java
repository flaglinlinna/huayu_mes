package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Mtrial;;

public interface MtrialDao extends CrudRepository<Mtrial, Long>,JpaSpecificationExecutor<Mtrial>{
	
	public List<Mtrial> findAll();
	public List<Mtrial> findByDelFlag(Integer delFlag);
	public List<Mtrial> findByDelFlagAndBsStatus(Integer delFlag,Integer bsStauts);
	public Mtrial findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询Code是否存在
    public List<Mtrial> findByDelFlagAndBsCode(Integer delFlag, String bsCode);
}
