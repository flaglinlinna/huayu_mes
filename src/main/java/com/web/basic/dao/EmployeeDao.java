package com.web.basic.dao;

import com.web.basic.entity.TodoInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Employee;

public interface EmployeeDao extends CrudRepository<Employee, Long>,JpaSpecificationExecutor<Employee>{

	public List<Employee> findAll();
	
	public List<Employee> findByDelFlag(Integer delFlag);
	
	public List<Employee> findByDelFlagAndEmpCode(Integer delFlag, String empCode);
	
	public List<Employee> findByDelFlagAndEmpStatus(Integer delFlag,Integer empStatus);//查询在职人员
	
	public Employee findById(long id);
	
	public int countByDelFlagAndEmpCode(Integer delFlag, String empCode);//查询是否存在

	@Modifying
	@Query(value = "update MES_BASE_EMPLOYEE t set t.EMP_IMG = ?2 where t.ID= ?1", nativeQuery = true)
	public int updateUrl(Integer id,String url);

}
