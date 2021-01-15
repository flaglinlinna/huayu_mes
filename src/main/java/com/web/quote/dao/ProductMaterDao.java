package com.web.quote.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.user.entity.UserRoleMap;
import com.web.basePrice.entity.ItemTypeWgRole;
import com.web.quote.entity.ProductMater;


public interface ProductMaterDao extends CrudRepository<ProductMater, Long>,JpaSpecificationExecutor<ProductMater>{

	public List<ProductMater> findAll();
	ProductMater findById(long id);
	public List<ProductMater> findByDelFlag(Integer delFlag);

	@Query(value = "SELECT p.* FROM PRICE_PRODUCT_MATER p WHERE p.del_flag = 0 AND p.pk_quote = ?1 and p.BS_ASSESS is not null" +
			" AND p.pk_item_type_wg IN ( SELECT wr.pk_item_type_wg  FROM BJ_BASE_ITEM_TYPE_WG_ROLE wr WHERE wr.del_flag = 0" +
			" AND wr.pk_sys_role IN ( SELECT ur.role_id FROM sys_user_role ur WHERE ur.del_flag = 0 AND ur.user_id = ?2 ))" ,nativeQuery = true)
	public List<ProductMater> findByPkQuoteAndUser(Long pkQuote,Long userId);

	public List<ProductMater> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsStatus(Integer delFlag,Long pkQuote,int bsStatus);

	@Query(value = "SELECT count(1) FROM PRICE_PRODUCT_MATER p WHERE p.del_flag = 0 AND p.pk_quote = ?1 and p.BS_ASSESS is null" +
			" AND p.pk_item_type_wg IN ( SELECT wr.pk_item_type_wg  FROM BJ_BASE_ITEM_TYPE_WG_ROLE wr WHERE wr.del_flag = 0" +
			" AND wr.pk_sys_role IN ( SELECT ur.role_id FROM sys_user_role ur WHERE ur.del_flag = 0 AND ur.user_id = ?2 ))" ,nativeQuery = true)
	Integer countByPkQuoteAndBsAssess(Long pkQuote,Long userId);

	//根据当前角色查看关联物料类型的是否确认完成状态
	@Query(value = "SELECT count(1) FROM PRICE_PRODUCT_MATER p WHERE p.del_flag = 0 AND p.pk_quote = ?1 and p.BS_STATUS_PURCHASE = ?2" +
			" AND p.pk_item_type_wg IN ( SELECT wr.pk_item_type_wg  FROM BJ_BASE_ITEM_TYPE_WG_ROLE wr WHERE wr.del_flag = 0" +
			" AND wr.pk_sys_role IN ( SELECT ur.role_id FROM sys_user_role ur WHERE ur.del_flag = 0 AND ur.user_id = ?3 ))" ,nativeQuery = true)
	Integer countByPkQuoteAndBsStatusPurchase(Long pkQuote,Integer bsStatusPurchase,Long userId);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsType(Integer delFlag,Long pkQuote,String bsType);
	
	@Query(value = "select map from ProductMater map  where map.delFlag=0 and map.pkQuote=?1 and ( map.bsType='hardware' or  map.bsType='surface' or map.bsType='packag') ")
	 public  List<ProductMater> findByDelFlagAnd3Tyle(Long pkQuote);
	
	@Query(value = "select map from ProductMater map  where map.delFlag=0 and map.pkQuote=?1 and  map.bsType='molding' ")
	 public  List<ProductMater> findByDelFlagAndMolding(Long pkQuote);
	
	
	@Modifying
    @Query("update ProductMater t set t.bsStatus=?3 where t.pkQuote=?1 and t.bsType=?2 and t.delFlag=0")
    public void updateStatus(Long pkQuote,String bsType,int status);
	
	@Modifying
    @Query("update ProductMater t set t.bsStatusPurchase=?2 where t.pkQuote=?1 and t.delFlag=0")
    public void updateStatusPurchase(Long pkQuote,int status);
	
	@Query(value = "select wr.pk_item_type_wg from "+ItemTypeWgRole.TABLE_NAME+" wr where wr.del_flag=0 and wr.pk_sys_role in (select ur.role_id from "+UserRoleMap.TABLE_NAME+" ur where ur.del_flag=0 and ur.user_id=?1)", nativeQuery = true)	
	public List<Map<String, Object>> getRoleByUid(Long uid);
	
	

}
