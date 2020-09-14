package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Department;

public interface DepartmentDao extends CrudRepository<Department, Long>,JpaSpecificationExecutor<Department>{
	
	public List<Department> findAll();
	public List<Department> findByIsDel(Integer isDel);
	public Department findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
