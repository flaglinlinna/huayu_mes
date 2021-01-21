package com.web.quote.service.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utils.BaseSql;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProdTypDao;
import com.web.basePrice.dao.ProfitProdDao;
import com.web.basePrice.entity.ProdTyp;
import com.web.basePrice.entity.ProfitProd;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.entity.QuoteItemBase;
import com.web.quote.service.QuoteService;

@Service(value = "QuoteService")
@Transactional(propagation = Propagation.REQUIRED)
public class Quotelmpl  extends BaseSql implements QuoteService {
	
	@Autowired
    private QuoteDao quoteDao;
	@Autowired
    private QuoteItemDao quoteItemDao;
	@Autowired
	private ProfitProdDao profitProdDao;
	@Autowired
    private QuoteItemBaseDao quoteItemBaseDao;
	@Autowired
    private TodoInfoService todoInfoService;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private ProdTypDao prodTypDao;
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
    	 //20201223-fyx-先校验是否维护了利润
        ApiResponseResult api = this.doCheckProfit(quote.getBsDevType(), quote.getBsProdType());
        if(!api.isResult()){
        	return ApiResponseResult.failure("请先在报价基础信息中的利润维护好数据!");
        }else{
        	quote.setBsProfitProd(getBigDecimal(api.getData()));
        }
        //---end----
    	//1:生成报价编号
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        quote.setBsCode("EQ-" + dateStr);  //编号格式：EQ-年月日时分秒
        
    	quote.setCreateDate(new Date());
    	quote.setCreateBy(UserUtil.getSessionUser().getId());

    	quote.setBsProfitNet(new BigDecimal("4.87")); //20210108-hjj-净利润设定默认值
    	quote = quoteDao.save(quote);
    	//2:建立子表-编码-项目名-代办人-开始/结束时间-进度状态【需有基础信息】
    	List<QuoteItem> lqi = new ArrayList<QuoteItem>();
    	//获取配置的待办项目
    	List<QuoteItemBase> lqb = quoteItemBaseDao.findByDelFlagAndBsStyle(0,"item");
    	for(QuoteItemBase qb:lqb){
    		QuoteItem qi = new QuoteItem();
    		qi.setPkQuote(quote.getId());
    		qi.setBsCode(qb.getBsCode());
    		qi.setBsName(qb.getBsName());
    		qi.setToDoBy(qb.getToDoBy());
    		qi.setBsPerson(qb.getBsPerson());
    		qi.setCreateDate(new Date());
    		qi.setCreateBy(UserUtil.getSessionUser().getId());
    		qi.setBsStyle(qb.getBsStyle());
    		if(qb.getBsCode().equals("A001") || qb.getBsCode().equals("A002")){
    			qi.setBsBegTime(new Date());//20201218-fyx
    			qi.setBsStatus(1);//20210106-lst设置状态“进行中”
    		}
    		lqi.add(qi);
    	}
    	quoteItemDao.saveAll(lqi);
    	//3:发送待办
    	for(QuoteItemBase qb:lqb){
    		TodoInfo todoInfo = new TodoInfo();
    		todoInfo.setBsType(1);//待办事项都是1
    		todoInfo.setBsUserId(qb.getToDoBy());
    		todoInfo.setBsTitle("新增报价-"+qb.getBsName()+"的资料待录入");
    		todoInfo.setBsContent(quote.getBsCode()+"-"+quote.getBsCustName()+ "的报价单");
    		todoInfo.setBsRouter(qb.getBsRouter()+quote.getId());
    		todoInfo.setBsReferId(quote.getId()); //关联ID
    		todoInfo.setBsModel(qb.getBsName());

    		todoInfoService.add(todoInfo);
    	}
    	
        return ApiResponseResult.success("报价单新增成功！").data(lqi);
	}
    /**
     * 获取产品类型表
     * **/
    
    public ApiResponseResult getProdType()throws Exception{
    	List<ProdTyp> list=prodTypDao.findByDelFlag(0);
    	return ApiResponseResult.success().data(list);
    }
    
//    /**
//     * 获取报价单列表
//     * **/
//    @Override
//    @Transactional
//    public ApiResponseResult getList(String keyword,String status,PageRequest pageRequest)throws Exception{
//    	// 查询条件1
//		List<SearchFilter> filters = new ArrayList<>();
//		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
//		if(!StringUtils.isEmpty(status)){
//			filters.add(new SearchFilter("bsStatus", SearchFilter.Operator.EQ, Integer.parseInt(status)));
//		}
//		// 查询2
//		List<SearchFilter> filters1 = new ArrayList<>();
//		if (StringUtils.isNotEmpty(keyword)) {
//			filters1.add(new SearchFilter("bsType", SearchFilter.Operator.LIKE, keyword));
//			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
//			filters1.add(new SearchFilter("bsProd", SearchFilter.Operator.LIKE, keyword));
//		}
//		Specification<Quote> spec = Specification.where(BaseService.and(filters, Quote.class));
//		Specification<Quote> spec1 = spec.and(BaseService.or(filters1, Quote.class));
//		Page<Quote> page = quoteDao.findAll(spec1, pageRequest);
//
//		Map map = new HashMap();
//		map.put("List", DataGrid.create(page.getContent(), (int) page.getTotalElements(),
//				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
//		map.put("Nums", quoteDao.getNumByStatus());
//		return ApiResponseResult.success().data(map);
//		/*return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
//				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));*/
//    }


    /**
     * 获取报价单列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getList(String quoteId,String keyword,String status,String bsCode,String bsType,String bsStatus,
                                     String bsFinishTime,String bsRemarks,String bsProd,String bsSimilarProd,
                                     String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
                                     String bsDevType,String bsCustName,PageRequest pageRequest)throws Exception{

        String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
                + "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,p.bs_position,p.bs_Manage_fee,  " +
                "p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level," +
                "p.bs_Cust_Require,i.bs_status bs_status_check  from "+Quote.TABLE_NAME+" p  left join (select t.pk_quote,min(t.bs_status)bs_status from "+QuoteItem.TABLE_NAME+" t where  t.bs_style='item' group by t.pk_quote) i on i.pk_quote=p.id"
                + "  where p.del_flag=0";

        if(StringUtils.isNotEmpty(quoteId)&&!("null").equals(quoteId)){
            sql += "and p.id = " + quoteId + "";
        }

        if(!StringUtils.isEmpty(status)){
            sql += "  and p.bs_Status = " + status + "";
        }
        if(StringUtils.isNotEmpty(bsType)){
            sql += "  and p.bs_Type like '%" + bsType + "%'";
        }
        if(StringUtils.isNotEmpty(bsCode)){
            sql += "  and p.bs_Code like '%" + bsCode + "%'";
        }
        if(StringUtils.isNotEmpty(bsFinishTime)){
            String[] dates = bsFinishTime.split(" - ");
            sql += " and to_date(p.bs_Finish_Time,'yyyy-MM-dd') >= to_date('"+dates[0]+"','yyyy-MM-dd')";
            sql += " and to_date(p.bs_Finish_Time,'yyyy-MM-dd') <= to_date('"+dates[1]+"','yyyy-MM-dd')";
        }
        if(StringUtils.isNotEmpty(bsRemarks)){
            sql += "  and p.bs_Remarks like '%" + bsRemarks + "%'";
        }
        if(StringUtils.isNotEmpty(bsProd)){
            sql += "  and p.bs_Prod like '%" + bsProd + "%'";
        }
        if(StringUtils.isNotEmpty(bsSimilarProd)){
            sql += "  and p.bs_Similar_Prod like '%" + bsSimilarProd + "%'";
        }
        if(StringUtils.isNotEmpty(bsPosition)){
            sql += "  and p.bs_position like '%" + bsPosition + "%'";
        }
        if(StringUtils.isNotEmpty(bsCustRequire)){
            sql += "  and p.bs_Cust_Require like '%" + bsCustRequire + "%'";
        }
        if(StringUtils.isNotEmpty(bsLevel)){
            sql += "  and p.bs_Level like '" + bsLevel + "%'";
        }
        if(StringUtils.isNotEmpty(bsRequire)){
            sql += "  and p.bs_Require like '%" + bsRequire + "%'";
        }
        if(StringUtils.isNoneEmpty(bsDevType)){
            sql += "  and p.bs_Dev_Type like '%" + bsDevType + "%'";
        }
        if(StringUtils.isNotEmpty(bsCustName)){
            sql += "  and p.bs_Cust_Name like '%" + bsCustName + "%'";
        }
        if (StringUtils.isNotEmpty(keyword)) {
			sql += "  and INSTR((p.bs_Code || p.bs_Prod ||p.bs_Similar_Prod ||p.bs_Remarks ||p.bs_Cust_Name" +
                    "||p.bs_Dev_Type ||p.bs_Cust_Require || p.bs_position || p.bs_Require ||p.bs_Level ||p.bs_Dev_Type), '"
					+ keyword + "') > 0 ";
        }
        
        sql += "  order by p.bs_code desc";

        int pn = pageRequest.getPageNumber() + 1;

        String sql_page = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM ( " + sql + " ) A  WHERE ROWNUM <= ("
                + pn + ")*" + pageRequest.getPageSize() + "  )  WHERE RN > (" + pageRequest.getPageNumber() + ")*"
                + pageRequest.getPageSize() + " ";

        Map<String, Object> param = new HashMap<String, Object>();

        List<Object[]>  list = createSQLQuery(sql_page, param);
        long count = createSQLQuery(sql, param, null).size();

        List<Map<String, Object>> list_new = new ArrayList<Map<String, Object>>();
        for (int i=0;i<list.size();i++) {
            Object[] object=(Object[]) list.get(i);
            Map<String, Object> map1 = new HashMap<>();
            map1.put("id", object[0]);
            map1.put("bsCode", object[1]);
            map1.put("bsType", object[2]);
            map1.put("bsStatus", object[3]);
            map1.put("bsFinishTime", object[4]);
            map1.put("bsRemarks", object[5]);
            map1.put("bsProd", object[6]);
            map1.put("bsSimilarProd", object[7]);
            map1.put("bsDevType", object[8]);
            map1.put("bsProdType", object[9]);
            map1.put("bsCustName", object[10]);
            map1.put("bsPosition", object[11]);
            map1.put("bsManageFee", object[12]);
            map1.put("bsMaterial", object[13]);
            map1.put("bsChkOutItem", object[14]);
            map1.put("bsChkOut", object[15]);
            map1.put("bsFunctionItem", object[16]);
            map1.put("bsFunction", object[17]);
            map1.put("bsRequire", object[18]);
            map1.put("bsLevel", object[19]);
            map1.put("bsCustRequire", object[20]);
            map1.put("bsStatusCheck", object[21]);
            list_new.add(map1);
        }

        Map map = new HashMap();
        map.put("List", DataGrid.create(list_new,  (int) count,
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
        map.put("Nums", quoteDao.getNumByStatus());
        return ApiResponseResult.success().data(map);
    }

    /**
     * 获取报价单-项目列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getItemPage(Long id,String bsStatus)throws Exception{
    	List<QuoteItem> list=quoteItemDao.findByDelFlagAndPkQuoteAndBsStyle(0,id,bsStatus);
    	return ApiResponseResult.success().data(list);
    }
    
    /**
     * 变更报价单状态
     * **/
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
    	if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置报价单状态！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsStatus(bsStatus);
        quoteDao.save(o);
        return ApiResponseResult.success("设置报价单状态成功！").data(o);
    }
    
    /**
     * 编辑报价单
     * **/
    @Override
    @Transactional
    public ApiResponseResult edit(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
        if(quote.getId() == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) quote.getId());
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        //20201223-fyx-先校验是否维护了利润
        ApiResponseResult api = this.doCheckProfit(quote.getBsDevType(), quote.getBsProdType());
        if(!api.isResult()){
        	return ApiResponseResult.failure("请先在报价基础信息中的利润维护好数据!");
        }else{
        	o.setBsProfitProd(getBigDecimal(api.getData()));
        }
        //---end----
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsType(quote.getBsType());
        o.setBsFinishTime(quote.getBsFinishTime());
        o.setBsRemarks(quote.getBsRemarks());
        o.setBsSimilarProd(quote.getBsSimilarProd());
        o.setBsCustName(quote.getBsCustName());
        o.setBsPosition(quote.getBsPosition());
        o.setBsProdType(quote.getBsProdType());
        o.setBsProdTypeId(quote.getBsProdTypeId());
        o.setBsDevType(quote.getBsDevType());
        o.setBsMaterial(quote.getBsMaterial());
        o.setBsChkOutItem(quote.getBsChkOutItem());
        o.setBsChkOut(quote.getBsChkOut());
        o.setBsManageFee(quote.getBsManageFee());
        o.setBsFunctionItem(quote.getBsFunctionItem());
        o.setBsFunction(quote.getBsFunction());
        o.setBsRequire(quote.getBsRequire());
        o.setBsLevel(quote.getBsLevel());
        o.setBsCustRequire(quote.getBsCustRequire());
        quoteDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }
    
    /**
     * 获取单张报价单部分内容
     * **/
    public ApiResponseResult getSingle(Long id)throws Exception{
    	if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        String cDate="";
        if(o.getCreateDate()!=null){
        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             cDate = sdf.format(o.getCreateDate());//格式化创建时间
        }
       
        Map<String, Object> map = new HashMap<>();
        map.put("id", o.getId());
        map.put("bsType", o.getBsType());
        map.put("bsCode", o.getBsCode());
        map.put("bsProd", o.getBsProd());
        map.put("createDate", cDate);
        map.put("createBy", sysUserDao.findById((long) o.getCreateBy()).getUserName());
        return ApiResponseResult.success().data(map);
    }

    /**
     * 根据ID获取全部内容
     */
    @Override
    @Transactional
    public ApiResponseResult getSingleAll(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    
	@Override
	public ApiResponseResult doItemFinish(String code, Long quoteId) throws Exception {
		// TODO Auto-generated method stub
		//单个项目完成后，执行
		//1.1 修改状态
		List<QuoteItem> lqi = quoteItemDao.findByDelFlagAndPkQuoteAndBsCode(0,quoteId,code);
		if(lqi.size() == 0){
			return ApiResponseResult.failure("报价单不存在!");
		}
		QuoteItem q = lqi.get(0);
		q.setBsEndTime(new Date());
		quoteItemDao.save(q);
		//2.1 查询该报价单是否都已经全部提交
		List<QuoteItem> lqii = quoteItemDao.getStatusByStype(quoteId,"item");
        if(lqii.size()== 0){
			//2.2 全部完成审批
        	List<Quote> lo = quoteDao.findByDelFlagAndId(0,quoteId);
        	if(lo.size()>0){
        		Quote o = lo.get(0);
        		o.setBsStatus(3);
        		o.setLastupdateDate(new Date());
        		o.setLastupdateBy(UserUtil.getSessionUser().getId());
        		quoteDao.save(o);
        	}
		}
		//2.3修改报价单状态（如果要自动发起审批则在这个地方触发，目前还未）
		/*List<QuoteItem> lqii = quoteItemDao.findByDelFlagAndPkQuoteAndNotBsEndTime(0,quoteId);
		if(lqii.size()>0){
			
		}*/
		
		return ApiResponseResult.success();
	}
	@Override
	public ApiResponseResult doCheckProfit(String bsDevType, String bsProdType) throws Exception {
		// TODO Auto-generated method stub
		List<ProfitProd> lpp = profitProdDao.findByDelFlagAndItemTypeAndProductTypeAndEnabled(0,bsDevType,bsProdType,1);
		
		if(lpp.size() == 0){
			return ApiResponseResult.failure();
		}
		return ApiResponseResult.success().data(lpp.get(0).getProfitRateGs());
	}

	public ApiResponseResult findUserName(Long usr_id)throws Exception{
		return ApiResponseResult.success().data(sysUserDao.findById((long) usr_id).getUserName());
	}
	
	/**
	 * 获取项目的当前状态
	 * 2021-01-4
	 * **/
	public ApiResponseResult getItemStatus(Long quoteId,String bsCode)throws Exception{
		return ApiResponseResult.success().data(quoteItemDao.getItemBsStatus(quoteId,bsCode));
	}
	
	public  BigDecimal getBigDecimal( Object value ) {  
        BigDecimal ret = null;  
        if( value != null ) {  
            if( value instanceof BigDecimal ) {  
                ret = (BigDecimal) value;  
            } else if( value instanceof String ) {  
                ret = new BigDecimal( (String) value );  
            } else if( value instanceof BigInteger ) {  
                ret = new BigDecimal( (BigInteger) value );  
            } else if( value instanceof Number ) {  
                ret = new BigDecimal( ((Number)value).doubleValue() );  
            } else {  
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");  
            }  
        }  
        return ret;  
    }  


}
