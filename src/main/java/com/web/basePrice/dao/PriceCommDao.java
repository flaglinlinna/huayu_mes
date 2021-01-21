package com.web.basePrice.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basePrice.entity.PriceComm;

/**
 * @author lst
 * @date Dec 7, 2020 5:02:33 PM
 */
public interface PriceCommDao extends CrudRepository<PriceComm, Long>,JpaSpecificationExecutor<PriceComm>{

    public List<PriceComm> findAll();
    public List<PriceComm> findByDelFlag(Integer delFlag);
    public PriceComm findById(long id);
    Integer countByDelFlagAndItemNo(Integer delFlag,String itemNo);
    
    @Query(value = "select p.item_Name,p.range_Price,p.price_Un from "+PriceComm.TABLE_NAME+" p  where p.del_Flag=0 and p.item_Name=?1",nativeQuery = true)
	 public  List<Map<String, Object>> findByDelFlagAndItemName(String itemName);
}