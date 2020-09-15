package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.WoLine;;

public interface WoLineDao extends CrudRepository<WoLine, Long>,JpaSpecificationExecutor<WoLine>{
	
	public List<WoLine> findAll();
	public List<WoLine> findByIsDel(Integer isDel);
	public WoLine findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在
}
