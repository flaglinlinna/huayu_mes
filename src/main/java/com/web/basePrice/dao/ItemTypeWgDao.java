package com.web.basePrice.dao;

import com.web.basePrice.entity.ItemTypeWg;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ItemTypeWgDao extends CrudRepository<ItemTypeWg, Long>,JpaSpecificationExecutor<ItemTypeWg>{

    public List<ItemTypeWg> findAll();
    public List<ItemTypeWg> findByItemTypeAndDelFlag(String itemType,Integer delFlag);
    public List<ItemTypeWg> findByDelFlag(Integer delFlag);
    public ItemTypeWg findById(long id);
}