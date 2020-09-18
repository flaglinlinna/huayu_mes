package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Defective;;

public interface DefectiveDao extends CrudRepository<Defective, Long>,JpaSpecificationExecutor<Defective>{
	
	public List<Defective> findAll();
	public List<Defective> findByIsDel(Integer isDel);
	public List<Defective> findByIsDelAndBsStatus(Integer isDel,Integer bsStatus);
	public Defective findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
