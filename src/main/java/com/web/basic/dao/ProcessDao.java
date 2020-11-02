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
	public int countByDelFlagAndProcNo(Integer delFlag, String procNo);//查询编号是否存在
	//public List<Process> findByDelFlagAndCheckStatus(Integer delFlag,Integer checkStatus);
	
	@Query(value = "select map from Process map  where map.delFlag=?1 and checkStatus=?2 order by map.procOrder  ")
		public  List<Process> findByDelFlagAndCheckStatus(Integer delFlag,Integer checkStatus);

    @Query(value = "select t.* from "+Process.TABLE_NAME+" t " +
            " where t.del_flag=0 and (t.proc_no=?1 or t.proc_name=?1) ", nativeQuery = true)
    public List<Process> findByProcName(String procName);
}
