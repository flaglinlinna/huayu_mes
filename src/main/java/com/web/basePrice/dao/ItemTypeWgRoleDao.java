package com.web.basePrice.dao;

import com.web.basePrice.entity.ItemTypeWgRole;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ItemTypeWgRoleDao extends CrudRepository<ItemTypeWgRole, Long>,JpaSpecificationExecutor<ItemTypeWgRole>{

    public List<ItemTypeWgRole> findAll();
    public List<ItemTypeWgRole> findByDelFlag(Integer delFlag);
    public List<ItemTypeWgRole> findByDelFlagAndPkItemTypeWg(Integer delFlag,Long pkItemTypeWg);
    @Modifying
    @Query("update ItemTypeWgRole t set t.delFlag=1 where t.pkItemTypeWg=?1 and t.delFlag=0")
    public void deleteItemTypeWgRoleByPkItemTypeWg(Long pkItemTypeWg);
    public ItemTypeWgRole findById(long id);

}