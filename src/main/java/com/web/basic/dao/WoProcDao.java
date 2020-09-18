package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.WoProc;;

public interface WoProcDao extends CrudRepository<WoProc, Long>,JpaSpecificationExecutor<WoProc>{
	
	public List<WoProc> findAll();
	public List<WoProc> findByIsDel(Integer isDel);
	public WoProc findById(long id);
	public int countByIsDelAndBsCode(Integer isDel, String bsCode);//查询deCode是否存在

    @Query(value = "select t.* from "+WoProc.TABLE_NAME+" t " +
            " where t.is_del=0 and (t.bs_code=?1 or t.bs_name=?1) ", nativeQuery = true)
    public List<WoProc> findByBsName(String bsName);
}
