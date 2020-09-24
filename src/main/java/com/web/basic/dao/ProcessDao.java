package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Process;;

public interface ProcessDao extends CrudRepository<Process, Long>,JpaSpecificationExecutor<Process>{
	
	public List<Process> findAll();
	public List<Process> findByDelFlag(Integer delFlag);
	public Process findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询deCode是否存在
	public List<Process> findByDelFlagAndBsStatus(Integer delFlag,Integer bsStatus);

    @Query(value = "select t.* from "+Process.TABLE_NAME+" t " +
            " where t.is_del=0 and (t.bs_code=?1 or t.bs_name=?1) ", nativeQuery = true)
    public List<Process> findByBsName(String bsName);
}
