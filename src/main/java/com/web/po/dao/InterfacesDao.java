package com.web.po.dao;

import com.web.po.entity.Interfaces;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * 接口信息配置表
 *
 */
public interface InterfacesDao extends CrudRepository<Interfaces, Long>, JpaSpecificationExecutor<Interfaces> {

    public Interfaces findById(long id);
}
