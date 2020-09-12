package com.web.basic.dao;

import com.web.basic.entity.SyncLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 数据同步日志表
 * <p>用于记录同步时间做增量更新</p>
 *
 */
public interface SyncLogDao extends CrudRepository<SyncLog, Long>, JpaSpecificationExecutor<SyncLog> {

    public SyncLog findByIsDelAndBsCode(Integer isDel, String bsCode);
}
