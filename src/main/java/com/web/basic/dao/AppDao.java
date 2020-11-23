package com.web.basic.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.basic.entity.AppVersion;
import com.web.basic.entity.Line;

public interface AppDao extends CrudRepository<AppVersion, Long>,JpaSpecificationExecutor<AppVersion>{

	public List<AppVersion> findAll();
	public List<AppVersion> findByDelFlag(Integer delFlag);
	public AppVersion findById(long id);
	public int countByDelFlagAndVersionNo(Integer delFlag, String versionNo);//查询编号是否存在

}
