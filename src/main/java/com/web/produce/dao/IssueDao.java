package com.web.produce.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basic.entity.Employee;
import com.web.produce.entity.Issue;


public interface IssueDao extends CrudRepository<Issue, Long>,JpaSpecificationExecutor<Issue>{

	public List<Issue> findAll();
	public List<Issue> findByDelFlag(Integer delFlag);
	public Issue findById(long id);
	public  List<Issue> findByDelFlagAndDevClockId(Integer delFlag,long devClockId);
	public int countByDelFlagAndEmpIdAndDevClockId(Integer delFlag, Long empId,Long devClockId);
	
	@Query(value = " select distinct f.emp_id id,a.emp_code,a.emp_name,a.emp_type,f.create_date,a.DEPT_NAME from MES_BASE_EMP_FINGER f left join MES_BASE_EMPLOYEE a on a.id = f.emp_id and a.del_flag=0 where f.del_flag=0 and INSTR((a.emp_code || a.emp_name || a.emp_type),  ?1) > 0 ",
		    countQuery = "select count(distinct f.emp_id id) from MES_BASE_EMP_FINGER f left join MES_BASE_EMPLOYEE a on a.id = f.emp_id and a.del_flag=0 where f.del_flag=0 and INSTR((a.emp_code || a.emp_name || a.emp_type), ?1) > 0",
		    nativeQuery = true)
		  Page<Map<String, Object>> findByKeyword(String keyword, Pageable pageable);
	@Query(value = " select distinct f.emp_id id,a.emp_code,a.emp_name,a.emp_type, max(f.create_date) create_date,a.DEPT_NAME from MES_BASE_EMP_FINGER f left join MES_BASE_EMPLOYEE a on a.id = f.emp_id and a.del_flag=0 where f.del_flag=0 group by f.emp_id ,a.emp_code,a.emp_name,a.emp_type,a.DEPT_NAME order by  max(f.create_date) ",
		    countQuery = "select count(distinct f.emp_id ) from MES_BASE_EMP_FINGER f left join MES_BASE_EMPLOYEE a on a.id = f.emp_id and a.del_flag=0 where f.del_flag=0 ",
		    nativeQuery = true)
		  Page<Map<String, Object>> findpage(String keyword, Pageable pageable);

	@Modifying
	@Query(value = "update mes_base_emp_finger f set f.del_flag = 1, f.del_by =?1 where f.emp_id =?2 ",nativeQuery = true)
	Integer updateDelFlagByClear(Long userId,Long empId);

	@Query(value = "select distinct f.emp_id id from MES_BASE_EMP_FINGER f left join MES_BASE_EMPLOYEE a on a.id = f.emp_id and a.del_flag=0 where f.del_flag=0 and a.emp_status = 0 and f.EMP_ID not in(select op.EMP_ID from MES_DEV_OPER_LOG op where op.DESCRIPTION = '指纹删除' and op.CMD_FLAG = 0)",nativeQuery = true)
	List<Map<String,Object>> getLeaveEmpId();
}
