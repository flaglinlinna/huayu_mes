package com.web.basePrice.dao;

import com.web.basePrice.entity.CustomQsFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
 客户品质标准附件
 */
public interface CustomQsFileDao extends CrudRepository<CustomQsFile, Long>,JpaSpecificationExecutor<CustomQsFile>{

    public List<CustomQsFile> findAll();
    public List<CustomQsFile> findByDelFlagAndCustomId(Integer delFlag,Long customId);
    public CustomQsFile findById(long id);
}