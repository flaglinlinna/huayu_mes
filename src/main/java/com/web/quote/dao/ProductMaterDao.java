package com.web.quote.dao;

import java.util.HashSet;
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

	@Query(value = "SELECT p.* FROM PRICE_PRODUCT_MATER p WHERE p.del_flag = 0 and p.bs_agent = 0 AND p.pk_quote = ?1 and p.bs_Type <> 'out' " +
			" AND p.pk_item_type_wg IN ( SELECT wr.pk_item_type_wg  FROM BJ_BASE_ITEM_TYPE_WG_ROLE wr WHERE wr.del_flag = 0" +
			" AND wr.pk_sys_role IN ( SELECT ur.role_id FROM sys_user_role ur WHERE ur.del_flag = 0 AND ur.user_id = ?2 )) order by p.id " ,nativeQuery = true)
	public List<ProductMater> findByPkQuoteAndUser(Long pkQuote,Long userId);

	public List<ProductMater> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	public List<ProductMater> findByDelFlagAndPkQuoteOrderById(Integer delFlag,Long pkQuote);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsTypeIsNotAndBsAgent(Integer delFlag,Long pkQuote,String bsType,Integer bsAgent);

	public List<ProductMater> findByBsAgentAndDelFlagAndPkQuoteOrderById(Integer bsAgent,Integer delFlag,Long pkQuote);

	public List<ProductMater> findByPkQuoteAndPkBomId(Long pkQuote,Long pkBomId);

	public List<ProductMater> findByBsElementAndBsComponentAndPkQuoteAndBsTypeAndDelFlag(String element,String component, Long pkQuote,String bsType,Integer delFlag);

	public List<ProductMater> findByPkQuoteAndDelFlagAndBsGroups(Long pkQuote,Integer delFlag,String bsGroups);

	//损耗明细材料成本取数
	@Query(value = "select * from  PRICE_PRODUCT_MATER where  BS_MATER_NAME in( select BS_MATER_NAME from price_product_process where PK_QUOTE = ?1 and BS_LINK_NAME = ?2 and pk_proc = ?3 and bs_Element =?4) and PK_QUOTE = ?1 and bs_Element =?4",nativeQuery = true)
	public List<ProductMater> findByProcessLost(Long pkQuote,String bsLinkName,Long pkProc,String bsElement);

	public List<ProductMater> findByPkQuoteAndDelFlagAndBsMaterName(Long pkQuote,Integer delFlag,String bsMaterName);

	Integer countByDelFlagAndPkQuoteAndBsAssessIsNull(Integer delFlag,Long pkQuote);

	Integer countByDelFlagAndPkQuoteAndBsAssessIsNullAndBsTypeIsNotAndBsAgent(Integer delFlag,Long pkQuote,String bsType,Integer bsAgent);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsStatusPurchaseAndBsAgentAndBsTypeIsNot(Integer delFlag,Long pkQuote,int bsStatus,Integer bsAgent,String bsType);

	@Query(value = "SELECT count(1) FROM PRICE_PRODUCT_MATER p WHERE p.del_flag = 0 AND p.pk_quote = ?1 and p.BS_ASSESS is null and p.bs_Type <> 'out' and p.bs_agent = 0 " +
			" AND p.pk_item_type_wg IN ( SELECT wr.pk_item_type_wg  FROM BJ_BASE_ITEM_TYPE_WG_ROLE wr WHERE wr.del_flag = 0" +
			" AND wr.pk_sys_role IN ( SELECT ur.role_id FROM sys_user_role ur WHERE ur.del_flag = 0 AND ur.user_id = ?2 ))" ,nativeQuery = true)
	Integer countByPkQuoteAndUserId(Long pkQuote,Long userId);

	//根据当前角色查看关联物料类型的是否确认完成状态
	@Query(value = "SELECT count(1) FROM PRICE_PRODUCT_MATER p WHERE p.del_flag = 0 AND p.pk_quote = ?1 and p.bs_Type <> 'out' and p.bs_agent = 0 and p.BS_STATUS_PURCHASE = ?2" +
			" AND p.pk_item_type_wg IN ( SELECT wr.pk_item_type_wg  FROM BJ_BASE_ITEM_TYPE_WG_ROLE wr WHERE wr.del_flag = 0" +
			" AND wr.pk_sys_role IN ( SELECT ur.role_id FROM sys_user_role ur WHERE ur.del_flag = 0 AND ur.user_id = ?3 ))" ,nativeQuery = true)
	Integer countByPkQuoteAndBsStatusPurchase(Long pkQuote,Integer bsStatusPurchase,Long userId);

	//根据是否确认完成状态
	@Query(value = "SELECT count(1) FROM PRICE_PRODUCT_MATER p WHERE p.del_flag = 0 AND p.pk_quote = ?1 and p.BS_STATUS_PURCHASE = ?2 and p.bs_Type <> 'out' and p.bs_agent = 0" ,nativeQuery = true)
	Integer countByPkQuoteAndBsStatusPurchase(Long pkQuote,Integer bsStatusPurchase);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsType(Integer delFlag,Long pkQuote,String bsType);

	public List<ProductMater> findByDelFlagAndPkQuoteAndBsTypeAndBsSingleton(Integer delFlag,Long pkQuote,String bsType,Integer bsSingleton);


	public List<ProductMater> findByDelFlagAndPkQuoteAndBsTypeAndRetrialIsNot(Integer delFlag,Long pkQuote,String bsType,Integer retrial);

	//20210116 hjj 新增外协材料价格计算 ，20210225 hjj 修改五金计算的方式 or map.bsType ='out'
	@Query(value = "select map from ProductMater map  where map.delFlag=0 and map.pkQuote=?1 and ( map.bsType='surface' or map.bsType='packag') ")
	public  List<ProductMater> findByDelFlagAnd3Tyle(Long pkQuote);

	@Query(value = "select map from ProductMater map  where map.delFlag=0 and map.pkQuote=?1 and  (map.bsType='molding' or map.bsType='hardware' )")
	public  List<ProductMater> findByDelFlagAndMolding(Long pkQuote);

	@Modifying
	@Query(value = "update ProductMater map set  map.delFlag = 1 where map.pkQuote = ?1")
	Integer deleteByPkQuote(Long pkQuote);

	@Modifying
	@Query(value = "UPDATE PRICE_PRODUCT_MATER m set m.DEL_FLAG = 1 where m.PK_QUOTE  = ?1 and m.PK_BOM_ID in (select b.PK_BOM_ID from PRICE_QUOTE_BOM b where b.DEL_FLAG = 1 and b.PK_QUOTE = ?1)",nativeQuery = true)
	Integer deleteByPkQuoteBom(Long pkQuote);

	@Modifying
	@Query("update ProductMater t set t.bsStatus=?3 where t.pkQuote=?1 and t.bsType=?2 and t.delFlag=0")
	public void updateStatus(Long pkQuote,String bsType,int status);

	@Modifying
	@Query("update ProductMater t set t.bsStatusPurchase=?2 where t.pkQuote=?1 and t.delFlag=0")
	public void updateStatusPurchase(Long pkQuote,int status);

	@Query(value = "select wr.pk_item_type_wg from "+ItemTypeWgRole.TABLE_NAME+" wr where wr.del_flag=0 and wr.pk_sys_role in (select ur.role_id from "+UserRoleMap.TABLE_NAME+" ur where ur.del_flag=0 and ur.user_id=?1)", nativeQuery = true)
	public List<Map<String, Object>> getRoleByUid(Long uid);

	@Query(value = "select map.id from ProductMater map where map.delFlag=0 and map.pkQuote =?2 and map.bsType = ?1")
	public List<Long> getIdByTypeAndPkQuote(String bsType,Long pkQuote);

	
	@Query(value = "select A.bs_element ELEMENT,b.bs_fee FEE,nvl(c.fee_lh,0)fee_lh,nvl(c.fee_mh,0)fee_mh,nvl(c.fee_wx,0)fee_wx from (select  distinct pb.bs_element  from price_quote_bom pb where pb.del_flag=0 and pb.pk_quote=?1)A "+
					"left join (select pm.bs_element,sum(pm.bs_fee)bs_fee from price_product_mater pm where pm.del_flag=0 and pm.pk_quote=?1 group by pm.bs_element)B "+
					"on a.bs_element = b.bs_element "+
					"left join (select pp.bs_element,sum(pp.bs_fee_lh_all)fee_lh,sum(pp.bs_fee_mh_all)fee_mh,sum(pp.bs_fee_wx_all)fee_wx from price_product_process pp where pp.del_flag=0 and pp.pk_quote=?1 group by pp.bs_element)C "+
					"on a.bs_element = c.bs_element", nativeQuery = true)	
	public List<Map<String, Object>> getBomFirt(Long quoteId);
	
	@Query(value = "select a.bs_element,A.bs_component COMPONENT,b.bs_fee FEE,nvl(c.fee_lh,0)fee_lh,nvl(c.fee_mh,0)fee_mh,nvl(c.fee_wx,0)fee_wx  from (select  distinct pb.bs_element,pb.bs_component  from price_quote_bom pb where pb.del_flag=0 and pb.pk_quote=?1 and pb.bs_element=?2)A "+
					"left join (select pm.bs_element,pm.bs_component,sum(pm.bs_fee)bs_fee from price_product_mater pm where pm.del_flag=0 and pm.pk_quote=?1 and pm.bs_element=?2 group by pm.bs_element,pm.bs_component)B "+
					"on a.bs_element = b.bs_element and a.bs_component = b.bs_component "+
					"left join (select pp.bs_element,pp.bs_name,sum(pp.bs_fee_lh_all)fee_lh,sum(pp.bs_fee_mh_all)fee_mh,sum(pp.bs_fee_wx_all)fee_wx from price_product_process pp where pp.del_flag=0 and pp.pk_quote=?1 and pp.bs_element=?2 group by pp.bs_element,pp.bs_name)C "+
					"on a.bs_element = c.bs_element and a.bs_component = c.bs_name "+
					"order by a.bs_element,A.bs_component", nativeQuery = true)	
    public List<Map<String, Object>> getBomSecond(Long quoteId,String element);
	
	
	@Query(value = "select a.bs_mater_name mater_name,nvl(b.bs_fee,0) FEE,a.pk_bj_work_center wkc,a.pk_unit punit,a.bs_qty qty  from (select  distinct pb.bs_element,pb.bs_component,pb.bs_mater_name,pb.pk_bj_work_center,pb.pk_unit,pb.bs_qty  from price_quote_bom pb where pb.del_flag=0 and pb.pk_quote=?1 and pb.bs_element=?2 and pb.bs_component=?3 )A "+
					"left join (select pm.bs_element,pm.bs_component,pm.bs_mater_name,sum(pm.bs_fee)bs_fee from price_product_mater pm where pm.del_flag=0 and pm.pk_quote=?1 and pm.bs_element=?2 and pm.bs_component=?3   group by pm.bs_element,pm.bs_component,pm.bs_mater_name)B "+
					"on a.bs_element = b.bs_element and a.bs_component = b.bs_component and a.bs_mater_name = b.bs_mater_name "+
					"order by a.bs_element,A.bs_component", nativeQuery = true)	
public List<Map<String, Object>> getBomThree(Long quoteId,String element,String compent);

	@Query(value = "select BS_TYPE as type,count(1) as num from PRICE_PRODUCT_MATER where PK_QUOTE = ?1 and DEL_FLAG = 0 GROUP BY  BS_TYPE", nativeQuery = true)
	public List<Map<String, Object>> countByBsType(Long quoteId);
}
