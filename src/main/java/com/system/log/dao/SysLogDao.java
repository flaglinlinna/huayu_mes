package com.system.log.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.system.log.entity.SysLog;

public interface SysLogDao  extends CrudRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog>{


}
