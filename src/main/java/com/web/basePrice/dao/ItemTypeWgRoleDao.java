package com.web.basePrice.dao;

import com.web.basePrice.entity.ItemTypeWgRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ItemTypeWgRoleDao extends CrudRepository<ItemTypeWgRole, Long>,JpaSpecificationExecutor<ItemTypeWgRole>{

    public List<ItemTypeWgRole> findAll();
//    public List<ItemTypeWgRole> findByItemTypeAndDelFlag(String itemType, Integer delFlag);
    public List<ItemTypeWgRole> findByDelFlag(Integer delFlag);
    public ItemTypeWgRole findById(long id);
}