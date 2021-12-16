package com.web.quote.service.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

import com.utils.BaseSql;
import com.web.basePrice.dao.*;
import com.web.basePrice.entity.BaseFee;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.*;
import com.web.quote.entity.*;
import com.web.quote.service.QuoteProcessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
import com.web.basePrice.entity.ProdTyp;
import com.web.basePrice.entity.ProfitProd;
import com.web.quote.service.QuoteService;

@Service(value = "QuoteService")
@Transactional(propagation = Propagation.REQUIRED)
public class Quotelmpl  extends BaseSql implements QuoteService {
	
	@Autowired
    private QuoteDao quoteDao;
    @Autowired
    private QuoteBomDao quoteBomDao;
    @Autowired
    private QuoteFileDao quoteFileDao;
    @Autowired
    private QuoteProcessDao quoteProcessDao;
	@Autowired
    private QuoteItemDao quoteItemDao;
    @Autowired
    private QuoteMouldDao quoteMouldDao;
	@Autowired
	private ProfitProdDao profitProdDao;
	@Autowired
    private QuoteItemBaseDao quoteItemBaseDao;
	@Autowired
    private TodoInfoService todoInfoService;
    @Autowired
    private QuoteProcessService quoteProcessService;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private ProdTypDao prodTypDao;
    @Autowired
    private ProductMaterDao productMaterDao;
    @Autowired
    private ProductProcessDao productProcessDao;

    @Autowired
    private ProcDao procDao;

    @Autowired
    private BaseFeeDao baseFeeDao;
    @Autowired
    private UnitDao unitDao;

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
        if(quoteDao.countByDelFlagAndBsProjVerAndBsProdAndBsCustName(0,quote.getBsProjVer(),quote.getBsProd(),quote.getBsCustName())>0){
            return ApiResponseResult.failure("存在产品型号、客户、版本组合相同的报价单,请修改后保存!");
        }
        //---end----
    	//1:生成报价编号
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        quote.setBsCode("EQ" + dateStr);  //编号格式：EQ-年月日时分秒
        
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
//    		qi.setToDoBy(qb.getToDoBy());
//    		qi.setBsPerson(qb.getBsPerson());

            //建单保存时，代办人都默认为建单人
            qi.setToDoBy(UserUtil.getSessionUser().getId());
            qi.setBsPerson(UserUtil.getSessionUser().getUserName());

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
     * 新增报价单
     */
    @Override
    @Transactional
    public ApiResponseResult copy(Quote quote)throws Exception{
        if(quote==null){
            return ApiResponseResult.failure("报价单复制内容不能为空！");
        }
        //1.新建报价单
        quote.setBsCopyId(quote.getId());
        if(quoteDao.countByDelFlagAndBsProjVerAndBsProdAndBsCustName(0,quote.getBsProjVer(),quote.getBsProd(),quote.getBsCustName())>0){
            return ApiResponseResult.failure("存在产品型号、客户、版本组合相同的报价单,请修改后保存!");
        }
        quote.setId(null);
        this.add(quote);
        //2.复制报价项目清单 (1 bom,2产品资料 3模具清单 4.工艺流程)
        List<QuoteBom> copyQuoteBomList =  quoteBomDao.findByDelFlagAndPkQuoteOrderById(0,quote.getBsCopyId());
        List<QuoteBom> newQuoteBomList = new ArrayList<>();
        for (QuoteBom o: copyQuoteBomList){
            QuoteBom quoteBom = new QuoteBom();
            BeanUtils.copyProperties(o,quoteBom);
            //存入复制的单关联的bomId或者Id
            quoteBom.setPkBomId(o.getPkBomId()!=null?o.getPkBomId():o.getId());
            quoteBom.setPkBomId2(o.getId());
            quoteBom.setPkQuote(quote.getId());
            quoteBom.setId(null);
            quoteBom.setBsStatus(0);
            newQuoteBomList.add(quoteBom);
        }
        quoteBomDao.saveAll(newQuoteBomList);


        List<QuoteFile> copyQuoteFileList =  quoteFileDao.findByDelFlagAndPkQuote(0,quote.getBsCopyId());
        List<QuoteFile> newQuoteFileList = new ArrayList<>();
        for (QuoteFile o: copyQuoteFileList){
            QuoteFile quoteFile = new QuoteFile();
            BeanUtils.copyProperties(o,quoteFile);
            quoteFile.setId(null);
            quoteFile.setPkQuote(quote.getId());
            quoteFile.setBsStatus(0);
            newQuoteFileList.add(quoteFile);
        }
        quoteFileDao.saveAll(newQuoteFileList);

        //2.3 工艺需要判断工序 是否是检验测试包装
        List<String> newName =Arrays.asList("测试","检验","包装");

        List<QuoteProcess> copyQuoteProcessList = quoteProcessDao.findByDelFlagAndPkQuote(0,quote.getBsCopyId());
        List<QuoteProcess> newQuoteProcessList = new ArrayList<>();
        for (QuoteProcess o: copyQuoteProcessList){

            if(o.getPkProc()!=null&&o.getProc()!=null){
                if(newName.contains(o.getProc().getProcName())){
//                    break;
                    continue;
                }
            }

            QuoteProcess quoteProcess = new QuoteProcess();
            BeanUtils.copyProperties(o,quoteProcess);

            quoteProcess.setId(null);
            quoteProcess.setPkQuote(quote.getId());
            //将旧的关联bom先复制过去(BOM与工艺流程关联)
            quoteProcess.setPkQuoteBom(o.getPkQuoteBom());
            //将旧的ID做为下发ID关联起来(工艺流程与制造部工艺关联)
            quoteProcess.setCopyId(o.getCopyId()!=null?o.getCopyId():o.getId());
            quoteProcess.setBsStatus(0);
            quoteProcess.setBsLinkName(null);
            newQuoteProcessList.add(quoteProcess);
        }
        quoteProcessDao.saveAll(newQuoteProcessList);

        List<QuoteMould> copyQuoteMouldList = quoteMouldDao.findByDelFlagAndPkQuote(0,quote.getBsCopyId());
        List<QuoteMould> newQuoteMouldList = new ArrayList<>();
        for (QuoteMould o: copyQuoteMouldList){
            QuoteMould quoteMould = new QuoteMould();
            BeanUtils.copyProperties(o,quoteMould);
            quoteMould.setId(null);
            quoteMould.setPkQuote(quote.getId());
            quoteMould.setBsStatus(0);
            newQuoteMouldList.add(quoteMould);
        }
        quoteMouldDao.saveAll(newQuoteMouldList);

        //2 复制制造部材料，采购部内容(无需重审条件下自动审批及完成)
        List<ProductMater> productMaterList = productMaterDao.findByDelFlagAndPkQuoteOrderById(0,quote.getBsCopyId());
        List<ProductMater> newProductMaterList = new ArrayList<>();
        for(ProductMater o:productMaterList){
            ProductMater productMater = new ProductMater();
            BeanUtils.copyProperties(o,productMater);
            productMater.setId(null);
            productMater.setPkQuote(quote.getId());
            productMater.setBsStatus(0);
            productMater.setBsStatusPurchase(0);
            newProductMaterList.add(productMater);
        }
        productMaterDao.saveAll(newProductMaterList);
        List<ProductProcess> productProcessList = productProcessDao.findByDelFlagAndPkQuote(0,quote.getBsCopyId());
        List<ProductProcess> newProductProcess = new ArrayList<>();
        for(ProductProcess o:productProcessList){
            if(o.getPkProc()!=null&&o.getProc()!=null){
                if(newName.contains(o.getProc().getProcName())){
                    break;
                }
            }
            ProductProcess productProcess = new ProductProcess();
            BeanUtils.copyProperties(o,productProcess);
            productProcess.setId(null);
            productProcess.setPkQuote(quote.getId());
            productProcess.setBsStatus(0);
            newProductProcess.add(productProcess);
        }
        productProcessDao.saveAll(newProductProcess);
        return ApiResponseResult.success("报价单复制成功！");
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
                                     String bsDevType,String bsCustName,String userName,PageRequest pageRequest)throws Exception{

        String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
                + "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,p.bs_position,p.bs_Manage_fee,  " +
                "p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level," +
                "p.bs_Cust_Require,i.bs_status bs_status_check,p.bs_proj_ver,p.bs_bade,p.bs_latest,p.bs_stage ,u.USER_NAME,TO_CHAR(p.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss'),p.bs_cust_manage from "+Quote.TABLE_NAME+" p " +
                " left join (select t.pk_quote,min(t.bs_status)bs_status from "+QuoteItem.TABLE_NAME+" t where " +
                " t.bs_style='item' group by t.pk_quote) i on i.pk_quote=p.id"
                + " LEFT JOIN SYS_USER u on u.id = p.create_by"
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

        if(StringUtils.isNotEmpty(userName)){
            sql += "  and u.USER_NAME like '%" + userName + "%'";
        }

        if (StringUtils.isNotEmpty(keyword)) {
			sql += "  and INSTR((p.bs_Code || p.bs_Prod ||p.bs_Similar_Prod ||p.bs_Remarks ||p.bs_Cust_Name" +
                    "||p.bs_Dev_Type ||p.bs_Cust_Require || p.bs_position || p.bs_Require ||p.bs_Level ||p.bs_Dev_Type ||u.user_name), '"
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

            map1.put("bsProjVer",object[22]);
            map1.put("bsBade",object[23]);
            map1.put("bsLatest",object[24]);
            map1.put("bsStage",object[25]);
            map1.put("userName",object[26]);
            map1.put("createDate",object[27]);
            map1.put("bsCustManage",object[28]);
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
        if(99==bsStatus){
            //关闭报价单时 关闭待办事项
            todoInfoService.close(null,null,null,o.getId());
        }
        quoteDao.save(o);
        return ApiResponseResult.success("设置报价单状态成功！").data(o);
    }

    /**
     * 变更报价单状态
     * **/
    @Override
    @Transactional
    public ApiResponseResult doBsBade(Long id, Integer bsBade) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsBade(bsBade);
        quoteDao.save(o);
        return ApiResponseResult.success("设置报价单中标状态成功！").data(o);
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
        //产品型号、客户，检查版本号不能重复 ,修改其中一个值就去查询
        if(!(o.getBsProjVer().equals(quote.getBsProjVer())&&o.getBsProd().equals(quote.getBsProd())&&o.getBsCustName().equals(quote.getBsCustName()))){
            if(quoteDao.countByDelFlagAndBsProjVerAndBsProdAndBsCustName(0,quote.getBsProjVer(),quote.getBsProd(),quote.getBsCustName())>0){
                return ApiResponseResult.failure("存在产品型号、客户、版本组合相同的报价单,请修改后保存!");
            }
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

        o.setBsTotal(quote.getBsTotal());
//        o.setBsBade(quote.getBsBade());
        o.setBsProjVer(quote.getBsProjVer());
        o.setBsLatest(quote.getBsLatest());
        o.setBsStage(quote.getBsStage());
        o.setBsCustManage(quote.getBsCustManage());
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
        Integer completed = 0;
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
                completed=1;
        	}
		}else {
            //取消完成修改状态
            List<Quote> lo = quoteDao.findByDelFlagAndId(0, quoteId);
            if (lo.size() > 0) {
                Quote o = lo.get(0);
                o.setBsStatus(0);
                o.setLastupdateDate(new Date());
                o.setLastupdateBy(UserUtil.getSessionUser().getId());
                quoteDao.save(o);
            }
        }
		//2.3修改报价单状态（如果要自动发起审批则在这个地方触发，目前还未）
		/*List<QuoteItem> lqii = quoteItemDao.findByDelFlagAndPkQuoteAndNotBsEndTime(0,quoteId);
		if(lqii.size()>0){
			
		}*/
		
		return ApiResponseResult.success().data(completed);
	}
	@Override
	public ApiResponseResult doCheckProfit(String bsDevType, String bsProdType) throws Exception {
		// TODO Auto-generated method stub
//		List<ProfitProd> lpp = profitProdDao.findByDelFlagAndItemTypeAndProductTypeAndEnabled(0,bsDevType,bsProdType,1);

        //hjj-20210122-毛利率查询去除机种型号
        List<ProfitProd> lpp = profitProdDao.findByDelFlagAndProductTypeAndEnabled(0,bsProdType,1);
		
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

    @Override
    public ApiResponseResult getOutStatus(Long id) throws Exception {
        Quote o = quoteDao.findById((long) id);
        return ApiResponseResult.success().data(o.getBsStatus2Out());
    }

    @Override
    public ApiResponseResult getStatus2(Long id) throws Exception {
        Quote o = quoteDao.findById((long) id);
        return ApiResponseResult.success().data(o.getBsStatus2());
    }

    @Override
    public ApiResponseResult getFreightStatus(Long id) throws Exception {
        Quote o = quoteDao.findById((long) id);
        return ApiResponseResult.success().data(""+o.getBsStatus2Hardware()+o.getBsStatus2Molding()+o.getBsStatus2Packag()+o.getBsStatus2Surface());
    }

    @Override
    public ApiResponseResult getMonBadeList(PageRequest pageRequest) throws Exception {
        Page<Map<String, Object>> list = quoteDao.getMonBade(pageRequest);
        return ApiResponseResult.success().data(DataGrid.create(list.getContent(), (int) list.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
//        return null;
    }


     /**
     * 1.检验数据(去掉了确认完成和审批，所以需要工艺和模具清单数据检查)
     * 2.下推数据 （根据已存在的工艺和材料进行更新，不存在的进行新增，需要考虑复制情况)
     * **/
    @Override
    public ApiResponseResult doPush(Long id) throws Exception {
        Quote quote = quoteDao.findById((long) id);
        if(quote==null){
            return ApiResponseResult.failure("报价单不能为空");
        }else if(quote.getBsStatus()==1){
            quote.setBsStatus(0);
            quoteDao.save(quote);
            return ApiResponseResult.success("取消下发成功");
        }
        //校验数据
//        if(quoteProcessDao.countByDelFlagAndPkQuoteAndPkProcIsNull(0,id)>0){
//            return ApiResponseResult.failure("请填写完所有工序");
//        }
        List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsStatusOrderByBsOrder(0,id,0);

        String[] bsGroupsArray = new String[lqp.size()];
        for(Integer q = 0;q<=lqp.size()-1;q++){
            QuoteProcess qp = lqp.get(q);
            if(qp.getPkProc()==null){
                return ApiResponseResult.failure("组件为:"+qp.getBsElement()+"工序不能为空，请填写完所有工序");
            }
            if(qp.getProc()==null){
                qp.setProc(procDao.findById((long) qp.getPkProc()));
            }
            if(!("out").equals(qp.getProc().getBjWorkCenter().getBsCode())){
                if(qp.getBsFeeLh() == null || qp.getBsFeeMh() == null){
                    String[] strs = this.getLhBy(qp.getProc().getWorkcenterId(), qp.getPkProc());
                    if (!StringUtils.isEmpty(strs[0])) {
                        qp.setBsFeeLh(new BigDecimal(strs[0]));
                    }
                    if (!StringUtils.isEmpty(strs[1])) {
                        qp.setBsFeeMh(new BigDecimal(strs[1]));
                    }
//						return ApiResponseResult.failure("有未维护的人工制费,请先维护!");
                }
            }

            bsGroupsArray[q]= qp.getBsGroups();

            if(StringUtils.isNotEmpty(qp.getBsMaterName())){
                if(quoteBomDao.findByDelFlagAndBsMaterNameAndPkQuote(0,qp.getBsMaterName(),qp.getPkQuote()).size()==0){
                    return ApiResponseResult.failure("外购件清单中不存在 "+qp.getBsMaterName()+" 的材料名称，无法进行损耗计算");
                }
            }
        }

        int count =quoteMouldDao.countByDelFlagAndPkQuoteAndBsActQuote(0,id,null);
        if(count>0){
            return ApiResponseResult.failure("提交失败：实际报价不可为空，请检查数据");
        }


        //2.1下发制造部待办项目-材料+工序
        List<QuoteItem> lqi = quoteItemDao.findByDelFlagAndStyles(0,id);
        if(lqi.size()>0){
            for(QuoteItem qi :lqi){
//							qi.setBsStatus(1);//lst-20210106-状态变更：进行中
            }
        }else {
            List<QuoteItemBase> lqb = quoteItemBaseDao.findByDelFlagAndStyles(0);
            for (QuoteItemBase qb : lqb) {
                QuoteItem qi = new QuoteItem();
                qi.setPkQuote(quote.getId());
                qi.setBsCode(qb.getBsCode());
                qi.setBsName(qb.getBsName());
                qi.setToDoBy(qb.getToDoBy());
                qi.setBsPerson(qb.getBsPerson());
                qi.setCreateDate(new Date());
                qi.setCreateBy(UserUtil.getSessionUser().getId());
                qi.setBsStatus(1);//lst-20210106-状态变更：进行中
                qi.setBsBegTime(new Date());
                qi.setBsStyle(qb.getBsStyle());
                lqi.add(qi);
            }
        }
        quoteItemDao.saveAll(lqi);
        //2.2根据工作中心下发BOM-材料
        //先判断是否为复制单，复制单情况更新，非复制单新增
        List<QuoteBom> lql = quoteBomDao.findByDelFlagAndPkQuoteAndBsMaterNameIsNotNullOrderById(0, id);
        productMaterDao.deleteByPkQuoteBom(id);
        if (lql.size() > 0) {
            List<ProductMater> lpm = new ArrayList<ProductMater>();
            for (QuoteBom qb : lql) {
                ProductMater pm = new ProductMater();
                if (qb.getPkBomId() != null) {
                    List<ProductMater> productMaterList = productMaterDao.findByPkQuoteAndPkBomId(quote.getId(), qb.getPkBomId());
                    if (productMaterList.size() > 0) {
                        pm = productMaterList.get(0);
                    }
                }
                pm.setBsType(qb.getWc().getBsCode());//类型
                pm.setBsComponent(qb.getBsComponent());
                pm.setBsMaterName(qb.getBsMaterName());
                pm.setBsModel(qb.getBsModel());
                //如果是复制单,下发材料关联的bomId与bom关联的bomId要相同(两者关联同一个复制源ID用于关联)
                //不是复制的单，下发材料关联的bomId则为bom的Id
                pm.setPkBomId(quote.getBsCopyId()==null?qb.getId():qb.getPkBomId());
                pm.setBsQty(qb.getBsQty());
                pm.setBsSingleton(qb.getBsSingleton());
//								if(pm.getBsSingleton()==1){
//									pm.setBsQty(qb.getBsQty());
//								}
                pm.setRetrial(qb.getProductRetrial());
                if (qb.getUnit() != null) {
                    if(pm.getBsType().equals("surface")){
                        if(StringUtils.isNotEmpty(qb.getPurchaseUnit())){
                            List<Unit> unitList = unitDao.findByUnitNameAndDelFlag(qb.getPurchaseUnit(),0);
                            if(unitList.size()>0){
                                pm.setPkUnit(unitList.get(0).getId());
                                pm.setBsUnit(unitList.get(0).getUnitName());
                            }
                        }
                    }else {
                        pm.setPkUnit(qb.getPkUnit());
                        pm.setBsUnit(qb.getUnit().getUnitName()); //hjj-20210120-补充单位名称
                    }
                }
                pm.setPurchaseUnit(qb.getPurchaseUnit());
                pm.setBsInjection(qb.getBsInjection());
                pm.setBsAgent(qb.getBsAgent());
//							pm.setBsGeneral();
//							pm.setBsCave(qb.getBsCave()); //hjj-20210121-模板导入新增水口重和穴数
//							pm.setBsWaterGap(qb.getBsWaterGap()); hjj-20210220 去掉水口和穴数，
                if ((qb.getBsAgent() == 1)) {
                    //客户代采的评估价格为0
                    pm.setBsAssess(BigDecimal.ZERO);
                    pm.setBsGeneral(0);
                } else {
                    //hjj-20210122 不是代采,先查询物料通用价格
                    //hjj-20210723 不是代采，采购价格取bom中的通用价格
//									pm.setBsAssess(qb.getPriceComm());
//									pm.setBsRefer(qb.getPriceComm());
                    pm.setBsGeneral(1);
//									List<Map<String, Object>> lm = priceCommDao.findByDelFlagAndItemName(qb.getBsMaterName());
//									if (lm.size() > 0) {
//										String priceUn = lm.get(0).get("PRICE_UN").toString();
//										String rangePrice = lm.get(0).get("RANGE_PRICE").toString();
//										if (StringUtils.isNotEmpty(priceUn)) {
//											pm.setBsGear(rangePrice);
//											pm.setBsRefer(new BigDecimal(priceUn));
//											pm.setBsAssess(new BigDecimal(priceUn));
//											pm.setBsGeneral(1);
//										}
//									} else {
//										pm.setBsGeneral(0);
//									}
                }
//								pm.setBsRadix(qb.getBsRadix());
                pm.setBsExplain(qb.getBsExplain());//lst-20210107-增采购说明字段
//								pm.setBsProQty(qb.getBsProQty());
                pm.setPkQuote(id);
                pm.setPkItemTypeWg(qb.getPkItemTypeWg());//fyx-20210114-物料类型
                pm.setBsElement(qb.getBsElement());//fyx-20210115-组件名称
                pm.setBsGroups(qb.getBsGroups());//hjj-20210415-损耗分组
                lpm.add(pm);
            }
            productMaterDao.saveAll(lpm);
        }


        //2.3根据工作中心下发BOM-工序
        List<QuoteProcess> lpd = quoteProcessDao.findByDelFlagAndPkQuote(0,id);
        productProcessDao.deleteByPkQuoteBom(id);

        Integer num = productProcessDao.countByPkQuoteAndDelFlag( id,0);
        //表示已下发过，找出没有下发过的工艺
//					if(num>0){
//
//					}


        if(lpd.size() > 0){
            List<ProductProcess> lpp = new ArrayList<ProductProcess>();
            Set<String> procSet = new HashSet<String>();
            for(QuoteProcess qb:lpd){
                ProductProcess pp = new ProductProcess();
                if (qb.getCopyId() != null) {
                    List<ProductProcess> productProcessList = productProcessDao.findByPkQuoteAndCopyId(quote.getId(), qb.getCopyId());
                    if (productProcessList.size() > 0) {
                        pp = productProcessList.get(0);
                    }
                }else if(num>0){
                    List<ProductProcess> productProcessList = productProcessDao.findByPkQuoteAndCopyId(quote.getId(), qb.getId());
                    if (productProcessList.size() > 0) {
                        pp = productProcessList.get(0);
//									pp.setDelFlag(qb);
                    }
                }
                if(qb.getProc().getProcName().equals("喷涂")){
                    //同个工作中心下，只有一个喷涂
                    if(!procSet.add("喷涂"+qb.getBjWorkCenter().getWorkcenterName()+qb.getBsElement())){
                        continue;
                    }
                }
                pp.setBsName(qb.getBsName());
                pp.setBsElement(qb.getBsElement());
                pp.setBsModel(qb.getBsModel());
//							pp.setBsType(qb.getProc().getBjWorkCenter().getBsCode());//类型
                pp.setBsType(qb.getBjWorkCenter().getBsCode());
                pp.setBsOrder(qb.getBsOrder());
                pp.setPkProc(qb.getPkProc());
                pp.setPurchaseUnit(qb.getPurchaseUnit());
                pp.setBsInjection(qb.getBsInjection());
                pp.setBsLinkName(qb.getBsLinkName());
                pp.setBsSingleton(qb.getBsSingleton());
                pp.setPkBomId(qb.getPkQuoteBom());
                pp.setPkQuote(id);
                pp.setCopyId(qb.getCopyId()!=null? qb.getCopyId() : qb.getId());
                pp.setBsMaterName(qb.getBsMaterName());
                pp.setBsFeeMh(qb.getBsFeeMh());
                pp.setBsFeeLh(qb.getBsFeeLh());
                pp.setBsGroups(qb.getBsGroups());
//							pp.setPkQuoteProcessId(qb.getId());
                lpp.add(pp);
            }
            productProcessDao.saveAll(lpp);
        }


        //修改报价状态
        quote.setLastupdateDate(new Date());
        quote.setBsStatus(1);
        //直接报价汇总总显示单据，并显示为草稿状态
        quote.setBsStep(3);
        quote.setBsStatus3(0);
        quote.setBsEndTime1(new Date());
        quote.setBsStatus2(0);
        quoteDao.save(quote);

        //自动审批
        autoDoStatus(id);
        return  ApiResponseResult.success().message("下推成功");
    }

    /**
     * 根据工作中心id和工序id查询人工和制费
     * @param w_id
     * @param p_id
     * @return
     */
    private String[] getLhBy(Long w_id,Long p_id ){
        String[] strs = new String[2];
        strs[0]="";strs[1]="";
        List<BaseFee> lbl = baseFeeDao.findByDelFlagAndWorkcenterIdAndProcId(0, w_id, p_id);
        if(lbl.size() == 0){
            lbl = baseFeeDao.findByDelFlagAndWorkcenterIdAndProcIdIsNull(0, w_id);
            if(lbl.size()>0){
                strs[0] = lbl.get(0).getFeeLh();
                strs[1] = lbl.get(0).getFeeMh();
            }
        }else{
            strs[0] = lbl.get(0).getFeeLh();
            strs[1] = lbl.get(0).getFeeMh();
        }
        return strs;
    }

    public void autoDoStatus(Long quoteId)throws  Exception{
        List<Map<String,Object>> mapList = quoteProcessDao.countByBsType(quoteId);
        List<Map<String,Object>> materList = productMaterDao.countByBsType(quoteId);

        //查找需要重审的类型
        List<Map<String,Object>> retrialList = quoteBomDao.getRetrial(quoteId);
        List<Quote> lo = quoteDao.findByDelFlagAndId(0, quoteId);
        //判断是否为复制单审批
        Boolean isCopy = false;
        if(lo.size()>0){
            Quote o = lo.get(0);
            isCopy = (o.getBsCopyId()!=null);
        }
        HashMap hashMap = new HashMap();
        HashMap materMap = new HashMap();
        HashMap retrialMap = new HashMap();
        for(Map map :mapList){
            hashMap.put(map.get("TYPE"),map.get("NUM"));
        }
        for(Map map : materList){
            materMap.put(map.get("TYPE"),map.get("NUM"));
        }
        for(Map map : retrialList){
            retrialMap.put(map.get("BSCODE"),map.get("RETRIAL"));
        }
        //项目状态设置-状态 2：已完成
        //增加处理人-20210112-lst-param(用户名,用户id,报价单ID,项目编码)
        // 不设置结束时间,为空则为自动完成

        //工艺自动完成
        if(!hashMap.containsKey("hardware")){
            quoteItemDao.switchStatus(3, quoteId, "C001");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C001");
        }if(!hashMap.containsKey("surface")){
            quoteItemDao.switchStatus(3, quoteId, "C003");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C003");
        }if(!hashMap.containsKey("packag")){
            quoteItemDao.switchStatus(3, quoteId, "C004");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C004");
        }if(!hashMap.containsKey("molding")){
            quoteItemDao.switchStatus(3, quoteId, "C002");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C002");
        }
        //外协为工艺  1.无外协的，2.外协不需要重审 自动完成及审批
        //2021-04-09 暂时取消外协和采购的自动确认完成
        //||quoteBomDao.findByDelFlagAndOutRetrial(0,1).size()<=0
        if(!hashMap.containsKey("out")){
            if(lo.size()>0){
                Quote o = lo.get(0);
                o.setBsStatus2Out(2);
                quoteDao.save(o);
            }
//            autoCheck(quoteId,"out");
        }

//		if(quoteBomDao.findByDelFlagAndPurchaseRetrial(0,1).size()<=0&&isCopy){
//			List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuote(0,quoteId);
//			for(ProductMater o : productMaterList) {
//				o.setBsStatusPurchase(1);
//			}
//			if(lo.size()>0){
//				Quote o = lo.get(0);
//				o.setBsStatus2Purchase(2);
//				quoteDao.save(o);
//			}
//			productMaterDao.saveAll(productMaterList);
//			autoCheck(quoteId,"QUOTE_PUR");
//		}

        //材料自动完成(材料为空的情况下)
        if(!materMap.containsKey("hardware")){
            quoteItemDao.switchStatus(3, quoteId, "B001");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B001");
        }if(!materMap.containsKey("surface")){
            quoteItemDao.switchStatus(3, quoteId, "B003");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B003");
        }if(!materMap.containsKey("packag")){
            quoteItemDao.switchStatus(3, quoteId, "B004");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B004");
        }if(!materMap.containsKey("molding")){
            quoteItemDao.switchStatus(3, quoteId, "B002");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B002");
        }

        //材料和工艺自动完成情况下(即是该工作中心下无需填写的数据)则自动审批
        if((!materMap.containsKey("hardware")&&!hashMap.containsKey("hardware"))||((("0").equals(retrialMap.get("hardware"))||!retrialMap.containsKey("hardware"))&&isCopy)){
            quoteItemDao.switchStatus(3, quoteId, "C001");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C001");
            quoteItemDao.switchStatus(3, quoteId, "B001");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B001");

//            autoCheck(quoteId,"hardware");

            //更新完成状态
            productMaterDao.updateStatus(quoteId,"hardware",1);
            if(lo.size()>0){
                Quote o = lo.get(0);
                o.setBsStatus2Hardware(2);
                quoteDao.save(o);
            }
        }
//		else if(isCopy) {
//			List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeAndRetrialIsNot(0,quoteId,"hardware",1);
//			for(ProductMater o : productMaterList) {
//				o.setBsStatus(1);
//			}
//			productMaterDao.saveAll(productMaterList);
//		}
        if((!materMap.containsKey("surface")&&!hashMap.containsKey("surface"))||((("0").equals(retrialMap.get("surface"))||!retrialMap.containsKey("surface"))&&isCopy)){
            quoteItemDao.switchStatus(3, quoteId, "B003");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B003");
            quoteItemDao.switchStatus(3, quoteId, "C003");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C003");
            if(lo.size()>0){
                Quote o = lo.get(0);
                o.setBsStatus2Surface(2);
                quoteDao.save(o);
            }
//            autoCheck(quoteId,"surface");

            productMaterDao.updateStatus(quoteId,"surface",1);
        }
//		else if(isCopy) {
//			List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeAndRetrialIsNot(0,quoteId,"surface",1);
//			for(ProductMater o : productMaterList) {
//				o.setBsStatus(1);
//			}
//			productMaterDao.saveAll(productMaterList);
//		}
        if((!materMap.containsKey("packag")&&!hashMap.containsKey("packag"))||((("0").equals(retrialMap.get("packag"))||!retrialMap.containsKey("packag"))&isCopy)){
            quoteItemDao.switchStatus(3, quoteId, "B004");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B004");
            quoteItemDao.switchStatus(3, quoteId, "C004");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C004");
            if(lo.size()>0){
                Quote o = lo.get(0);
                o.setBsStatus2Packag(2);
                quoteDao.save(o);
            }
//            autoCheck(quoteId,"packag");

            productMaterDao.updateStatus(quoteId,"packag",1);
        }
//		else if(isCopy) {
//			List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeAndRetrialIsNot(0,quoteId,"packag",1);
//			for(ProductMater o : productMaterList) {
//				o.setBsStatus(1);
//			}
//			productMaterDao.saveAll(productMaterList);
//		}
        if((!materMap.containsKey("molding")&&!hashMap.containsKey("molding"))||((("0").equals(retrialMap.get("molding"))||!retrialMap.containsKey("molding"))&&isCopy)){
            quoteItemDao.switchStatus(3, quoteId, "B002");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B002");
            quoteItemDao.switchStatus(3, quoteId, "C002");
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C002");
            if(lo.size()>0){
                Quote o = lo.get(0);
                o.setBsStatus2Molding(2);
                quoteDao.save(o);
            }
//            autoCheck(quoteId,"molding");

            productMaterDao.updateStatus(quoteId,"molding",1);
        }
//		else if(isCopy) {
//			List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeAndRetrialIsNot(0,quoteId,"molding",1);
//			for(ProductMater o : productMaterList) {
//				o.setBsStatus(1);
//			}
//			productMaterDao.saveAll(productMaterList);
//		}
    }
}
