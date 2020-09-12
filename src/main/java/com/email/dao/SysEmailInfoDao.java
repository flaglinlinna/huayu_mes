package com.email.dao;

import com.email.entity.SysEmailInfo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface SysEmailInfoDao extends CrudRepository<SysEmailInfo, Long>, JpaSpecificationExecutor<SysEmailInfo> {
}
