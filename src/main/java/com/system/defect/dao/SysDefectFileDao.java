package com.system.defect.dao;

import com.system.defect.entity.SysDefectFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SysDefectFileDao extends CrudRepository<SysDefectFile, Long>, JpaSpecificationExecutor<SysDefectFile> {

    public List<SysDefectFile> findByDelFlagAndDefectId(Integer delFlag, Long defectId);

    public List<SysDefectFile> findByDelFlagAndDefectIdAndFileId(Integer delFlag, Long defectId, Long fileId);
}
