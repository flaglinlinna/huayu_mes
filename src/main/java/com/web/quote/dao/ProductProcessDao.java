package com.web.quote.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.web.quote.entity.QuoteProcess;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.web.basePrice.entity.BjModelType;
import com.web.basePrice.entity.Proc;
import com.web.quote.entity.ProductProcess;


public interface ProductProcessDao extends CrudRepository<ProductProcess, Long>,JpaSpecificationExecutor<ProductProcess>{

	public List<ProductProcess> findAll();
	public ProductProcess findById(long id);
	public List<ProductProcess> findByDelFlag(Integer delFlag);

	public List<ProductProcess> findByBsTypeAndPkProcAndDelFlagAndPkQuote(String bsType,Long pkProc,Integer delFlag,Long pkQuote);
	
	public List<ProductProcess> findByDelFlagAndPkQuote(Integer delFlag,Long pkQuote);

	@Query(value = "select map.id from ProductProcess map where map.delFlag=0 and map.pkQuote =?2 and map.bsType = ?1")
	public List<Long> getIdByTypeAndPkQuote(String bsType,Long pkQuote);

	@Modifying
	@Query(value = "update ProductProcess p set p.bsStatus = 1 ,p.lastupdateBy =?1 ,p.lastupdateDate =?2 where p.pkQuote =?3 and p.bsType =?4 and p.delFlag = 0 ")
	Integer doProcessStatusByType(Long userId, Date date,Long pkQuote,String bsType);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsType(Integer delFlag,Long pkQuote,String bsType);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsTypeAndBsSingletonIsNot(Integer delFlag,Long pkQuote,String bsType,Integer bsSingleton);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsTypeAndBsSingletonIsNotAndPurchaseUnitIsNot(Integer delFlag,Long pkQuote,String bsType,Integer bsSingleton,String unit);

	public List<ProductProcess> findByPkQuoteAndCopyId(Long pkQuote,Long copyId);

	@Modifying
	@Query(value = "UPDATE PRICE_PRODUCT_PROCESS m set m.DEL_FLAG = 1 where m.PK_QUOTE  = ?1 and m.COPY_ID in (select b.COPY_ID from PRICE_QUOTE_PROCESS b where b.DEL_FLAG = 1 and b.PK_QUOTE = ?1)",nativeQuery = true)
	Integer deleteByPkQuoteBom(Long pkQuote);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsGroups(Integer delFlag,Long pkQuote,String bsGroups);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsTypeOrderByBsOrder(Integer delFlag,Long pkQuote,String bsType);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsTypeAndBsNameOrderByBsOrderDesc(Integer delFlag,Long pkQuote,String bsType,String bsname);
	
	@Query(value = "select DISTINCT M.BS_NAME from price_product_process M WHERE M.DEL_FLAG=0 AND M.BS_TYPE=?1 AND M.PK_QUOTE=?2 AND M.BS_NAME IS NOT NULL", nativeQuery = true)	
	public List<Map<String, Object>> getBomName(String bsType,Long quoteid);
	
	 @Query(value = "select p.model_Code,p.model_Name  from "+BjModelType.TABLE_NAME+" p  where p.del_Flag=0 and p.pk_Workcenter=?1",nativeQuery = true)
	 public  List<Map<String, Object>> findByDelFlagAndWorkcenter(Long pkWorkcenter);

	@Query(value = "select DISTINCT f.model_Code,f.MH_TYPE as MODEL_NAME from BJ_BASE_FEE f  where f.DEL_FLAG = 0 and f.MH_TYPE is not null and f.PROC_ID = ?1 and  f.WORKCENTER_ID = ?2  ORDER BY MODEL_NAME ",nativeQuery = true)
	public  List<Map<String, Object>> findByWorkcenter(Long procId,Long pkWorkcenter);
	 
	 @Query(value = "select p from Proc p  where p.delFlag=0 and p.workcenterId in (select bj.id from BjWorkCenter bj where bj.delFlag=0 and bj.bsCode=?1)")
	 public  List<Proc> getListByType(String code);

	public List<ProductProcess> findByDelFlagAndPkQuoteOrderByBsOrderDesc(Integer delFlag,Long pkQuote);

	public List<ProductProcess> findByDelFlagAndPkQuoteOrderByBsElementDescBsLinkNameDescBsOrderDesc(Integer delFlag,Long pkQuote);

//	@Query(value = "select a.* from (select p from ProductProcess p  where p.delFlag=0 and p.pkQuote =?1 and p.bsGroups is empty " +
//			"UNION select c from ProductProcess c where c.id in (select max(d.id) from ProductProcess d  where  d.defFlag=0  and d.pkQuote =?1 and d.bsGroups is not null group by d.bsGroups)) a")
//	public List<ProductProcess> findSumList2(Long pkQuote);

	public List<ProductProcess> findByBsNameAndBsElementAndPkQuoteAndBsTypeAndDelFlagAndBsMaterNameOrderByBsOrderDesc(String bsName,String element,Long pkQuote,String bsType,Integer delFlag,String bsMaterName);

	@Query(value = "select * from (select pp.id,pp.bs_linK_name,pp.bs_groups,pp.BS_ELEMENT,pp.BS_ORDER,bs_Name,bs_Mater_Cost,bs_Fee_Lh_All,bs_Fee_Mh_All,bs_Fee_Wx_All,bs_Yield," +
			"bs_The_Loss,pp.BS_COST,pp.BS_ALL_LOSS,bp.PROC_NAME,bw.workcenter_Name from PRICE_PRODUCT_PROCESS pp left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id" +
			" LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id  where pp.PK_QUOTE = ?1 and pp.BS_GROUPS is null  and pp.DEL_FLAG = 0 " +
			"UNION" +
			" select pp.id,pp.bs_linK_name,pp.bs_groups,pp.BS_ELEMENT,pp.BS_ORDER,bs_Name,bs_Mater_Cost,bs_Fee_Lh_All,bs_Fee_Mh_All,bs_Fee_Wx_All,bs_Yield,bs_The_Loss,pp.BS_COST,pp.BS_ALL_LOSS,bp.PROC_NAME," +
			"bw.workcenter_Name from PRICE_PRODUCT_PROCESS pp  left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id  where pp.PK_QUOTE = ?1" +
			" and pp.BS_GROUPS is not null and pp.DEL_FLAG = 0 and pp.bs_order in (select max(p.bs_order) from PRICE_PRODUCT_PROCESS p " +
			" where p.PK_QUOTE = ?1 and p.BS_GROUPS is not null and p.DEL_FLAG = 0 GROUP BY p.BS_ELEMENT,p.bs_link_name,p.BS_GROUPS)  " +
			" ) ORDER BY  BS_ELEMENT,bs_linK_name,BS_ORDER" +
			" ",nativeQuery = true,
			countQuery = "select count(id) from (select pp.id from PRICE_PRODUCT_PROCESS pp left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id" +
					" LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id  where pp.PK_QUOTE = ?1 and pp.BS_GROUPS is null  and pp.DEL_FLAG = 0" +
					" UNION"  +
					" select max(pp.id) from PRICE_PRODUCT_PROCESS pp" +
					" left join BJ_BASE_PROC bp on pp.PK_PROC = bp.id LEFT JOIN BJ_BASE_WORKCENTER bw on bw.id = bp.workcenter_Id where pp.PK_QUOTE = ?1" +
					" and pp.BS_GROUPS is not null  and pp.DEL_FLAG = 0 GROUP BY pp.BS_ELEMENT,pp.bs_link_name,pp.BS_GROUPS)")
	public  Page<Map<String, Object>> getSumList(Long pkQuote,Pageable pageable);

	@Query(value = "select" +
//			" sum(bs_Mater_Cost) as bs_Mater_Cost," +
			" sum(bs_Fee_Lh_All) as bs_Fee_Lh_All," +
			" sum(bs_Fee_Mh_All) as bs_Fee_Mh_All," +
			" sum(bs_Fee_Wx_All) as bs_Fee_Wx_All" +
			" from PRICE_PRODUCT_PROCESS pp  where pp.PK_QUOTE = ?1 and pp.BS_GROUPS =?2 and pp.bs_element = ?3 and pp.bs_link_name =?4 and pp.DEL_FLAG = 0",nativeQuery = true)
	public List<Map<String,Object>> getSumByBsGroups(Long pkQuote,String bsGroups,String bsElement,String bsLinkName);

	@Query(value = "select * from PRICE_PRODUCT_PROCESS where id in (select max(p.id) as id from PRICE_PRODUCT_PROCESS p  where p.PK_QUOTE = ?1 and p.DEL_FLAG = 0 GROUP BY p.BS_ELEMENT) ",nativeQuery = true,
			countQuery= "select count(max(p.id)) from PRICE_PRODUCT_PROCESS p  where p.PK_QUOTE = ?1 and p.DEL_FLAG = 0 GROUP BY p.BS_ELEMENT")
	public Page<ProductProcess> findFreightList(Long pkQuote,Pageable pageable);

	//查找各个组件下的总人数和最小产能
	@Query(value = "select BS_ELEMENT ,sum(BS_USER_NUM) as allUser,sum(bs_Fee_Lh_All) as bsFeeLhAll from PRICE_PRODUCT_PROCESS where PK_QUOTE = ?1 and DEL_FLAG = 0 and bs_TYPE =?2 GROUP BY BS_ELEMENT",nativeQuery = true)
	public  List<Map<String,Object>> getSumByBsElement(Long pkQuote,String bsType);

	//查找各个组件下的总人数和最小产能
	@Query(value = "select BS_ELEMENT,min(BS_CAPACITY) as minCapacity from PRICE_PRODUCT_PROCESS where PK_QUOTE = ?1 and DEL_FLAG = 0 and BS_CAPACITY !=0 and bs_TYPE =?2 GROUP BY BS_ELEMENT",nativeQuery = true)
	public  List<Map<String,Object>> getMinCapaCityGroupBy(Long pkQuote,String bsType);

	//新增制造工艺的下拉组件零件选择
	@Query(value = "select max(t.id) as id , max(t.BS_ELEMENT)as BS_ELEMENT ,max(t.BS_COMPONENT) as BS_COMPONENT ,max(t.bs_mater_Name) as bs_mater_Name ,max(c.WORKCENTER_NAME)" +
			" as WORKCENTER_NAME from" +
			" price_quote_bom t " +
			" LEFT JOIN BJ_BASE_WORKCENTER c on t.PK_BJ_WORK_CENTER = c.ID " +
			" where t.pk_quote= ?1  and t.del_Flag= 0 and  c.bs_code = ?2 GROUP BY t.PK_BJ_WORK_CENTER,t.BS_ELEMENT,t.BS_COMPONENT ",
			countQuery =  " select count(*)from(select max(t.id) from price_quote_bom t  left join BJ_BASE_WORKCENTER w on w.id = t.PK_BJ_WORK_CENTER where t.pk_quote= ?1 " +
					" and t.del_Flag= 0  and w.bs_code = ?2 GROUP BY t.PK_BJ_WORK_CENTER,t.BS_ELEMENT,t.BS_COMPONENT)",
			nativeQuery = true)
	Page<Map<String, Object>> getBomNameByPage(Long quoteId,String bsType, Pageable pageable);

	@Query(value = "select * from PRICE_PRODUCT_PROCESS p  where p.PK_QUOTE = ?1 and p.DEL_FLAG = 0 and p.bs_Type ='out' " +
			" and p.pk_proc in (select wr.pk_proc from BJ_BASE_PROC_ROLE wr where wr.del_flag=0 and wr.pk_sys_role in (select ur.role_id from sys_user_role ur where ur.del_flag=0 and ur.user_id=?2)) order by p.bs_order",nativeQuery = true,
			countQuery="select count(*) from PRICE_PRODUCT_PROCESS p  where p.PK_QUOTE = ?1 and p.DEL_FLAG = 0 and p.bs_Type ='out' " +
					" and p.pk_proc in (select wr.pk_proc from BJ_BASE_PROC_ROLE wr where wr.del_flag=0 and wr.pk_sys_role in (select ur.role_id from sys_user_role ur where ur.del_flag=0 and ur.user_id=?2)) ")
	public Page<ProductProcess> findOutListByUserId(long pkQuote, Long userId,Pageable pageable);


	@Query(value = "select * from PRICE_PRODUCT_PROCESS p  where p.PK_QUOTE = ?1 and p.DEL_FLAG = 0 and p.bs_Type ='out' " +
			" and p.pk_proc in (select wr.pk_proc from BJ_BASE_PROC_ROLE wr where wr.del_flag=0 and wr.pk_sys_role in (select ur.role_id from sys_user_role ur where ur.del_flag=0 and ur.user_id=?2)) order by p.bs_order",nativeQuery = true)
	public List<ProductProcess> findAllOutListByUserId(long pkQuote, Long userId);

	public Integer countByPkQuoteAndDelFlagAndBsTypeAndBsStatus(Long pkQuote,Integer delFlag,String bsType,Integer bsStatus);

	public List<ProductProcess> findByDelFlagAndPkQuoteAndBsElementAndBsGroupsAndBsLinkName(Integer delFlag, Long pkQuote, String bsElement, String bsGroups, String bsLineName);
}
