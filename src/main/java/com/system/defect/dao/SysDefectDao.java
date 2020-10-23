package com.system.defect.dao;

import com.system.defect.entity.SysDefect;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 缺陷记录
 */
public interface SysDefectDao extends CrudRepository<SysDefect, Long>, JpaSpecificationExecutor<SysDefect> {

    public SysDefect findById(long id);
}
