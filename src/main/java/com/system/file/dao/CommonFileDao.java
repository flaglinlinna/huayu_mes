package com.system.file.dao;


import com.system.file.entity.CommonFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
 标准附件
 */
public interface CommonFileDao extends CrudRepository<CommonFile, Long>,JpaSpecificationExecutor<CommonFile>{

    public List<CommonFile> findAll();
    public List<CommonFile> findByDelFlagAndMIdAndBsType(Integer delFlag,Long mId,String bsType);
    public CommonFile findById(long id);
}