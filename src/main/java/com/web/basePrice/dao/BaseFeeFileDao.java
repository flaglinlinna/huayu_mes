package com.web.basePrice.dao;

import com.web.basePrice.entity.BaseFeeFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
 标准附件
 */
public interface BaseFeeFileDao extends CrudRepository<BaseFeeFile, Long>,JpaSpecificationExecutor<BaseFeeFile>{

    public List<BaseFeeFile> findAll();
    public List<BaseFeeFile> findByDelFlagAndMId(Integer delFlag,Long mId);
    public BaseFeeFile findById(long id);
}