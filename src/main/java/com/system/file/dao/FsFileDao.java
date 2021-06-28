package com.system.file.dao;

import java.util.List;


import com.system.file.entity.CommonFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.file.entity.FsFile;
import org.springframework.data.repository.query.Param;


public interface FsFileDao extends CrudRepository<FsFile, Long>, JpaSpecificationExecutor<FsFile> {
    int countByPkFsCatalogAndBsFileName(Long var1, String var2);

    @Query("from FsFile o where o.bsFileName like ?1")
    List<FsFile> findByBsFileName(String var1);

    @Query("select o from FsFile o where o.bsName like ?1")
    List<FsFile> findAllByBsFolder(String var1);

    @Modifying
    @Query("update FsFile set bsIsDel = 1 where id = ?1")
    void deleteRefrenceById(Long var1);

    public FsFile findById(long id);

    @Query(value = "select c from CommonFile c where c.mId=:mid and c.bsType =:bsType and c.delFlag = 0",
    countQuery = "select count(c.fileId) from CommonFile c where c.mId=:mid and c.bsType =:bsType and c.delFlag = 0")
    public Page<CommonFile> getFileListByBs(@Param("mid") Long mid, @Param("bsType") String bsType, Pageable pageable);
}
