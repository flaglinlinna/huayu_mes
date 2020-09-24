package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Department;

public interface DepartmentDao extends CrudRepository<Department, Long>,JpaSpecificationExecutor<Department>{
	
	public List<Department> findAll();
	public List<Department> findByDelFlag(Integer delFlag);
	public Department findById(long id);
	public int countByDelFlagAndBsCode(Integer delFlag, String bsCode);//查询deCode是否存在

    @Query(value = "select t.* from "+Department.TABLE_NAME+" t " +
            " where t.is_del=0 and (t.bs_code=?1 or t.bs_name=?1) ", nativeQuery = true)
    public List<Department> findByBsName(String bsName);
}
