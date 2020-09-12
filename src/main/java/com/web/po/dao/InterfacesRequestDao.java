package com.web.po.dao;

import com.web.po.entity.InterfacesRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 接口信息配置-请求参数表
 *
 */
public interface InterfacesRequestDao extends CrudRepository<InterfacesRequest, Long>, JpaSpecificationExecutor<InterfacesRequest> {

    public InterfacesRequest findById(long id);

    public List<InterfacesRequest> findByIdIn(long[] idArray);
}
