package com.web.basic.dao;

import com.web.basic.entity.LinerImg;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LinerImgDao extends CrudRepository<LinerImg, Long>,JpaSpecificationExecutor<LinerImg>{

	public List<LinerImg> findAll();
	public List<LinerImg> findByDelFlag(Integer delFlag);
	public LinerImg findById(long id);

}
