package com.web.quote.service.internal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.Unit;
import com.web.quote.service.QuoteSumService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.entity.SysUser;
import com.system.user.entity.UserRoleMap;
import com.utils.BaseSql;
import com.utils.ExcelExport;
import com.utils.UserUtil;
import com.web.basePrice.dao.PriceCommDao;
import com.web.basePrice.entity.ItemTypeWgRole;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.Quote;
import com.web.quote.service.PurchaseService;

@Service(value = "PurchaseService")
@Transactional(propagation = Propagation.REQUIRED)
public class Purchaselmpl extends BaseSql implements PurchaseService {

	@Autowired
	private ProductMaterDao productMaterDao;
	@Autowired
	private QuoteDao quoteDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private PriceCommDao priceCommDao;
	@Autowired
	private QuoteSumService quoteSumService;
	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String quoteId,String keyword,String bsStatus,String bsCode,String bsType,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsProdType,String bsSimilarProd,
									 String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,String userName,PageRequest pageRequest) throws Exception {
		String statusTemp = "";
		if(StringUtils.isNotEmpty(bsStatus)){
			statusTemp = "and p.bs_status2purchase = " + bsStatus;
		}
		String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,p.bs_status2purchase col ,p.bs_position," +
				"p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level," +
				" p.bs_Cust_Require,p.bs_proj_ver,p.bs_bade,p.bs_latest,p.bs_stage ,u.USER_NAME,TO_CHAR(p.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss'),p.bs_total,p.bs_status2 from "+Quote.TABLE_NAME+" p "
				+ " LEFT JOIN SYS_USER u on u.id = p.create_by"
				+ " where p.del_flag=0 and p.bs_step>=2  "+statusTemp;
		if(StringUtils.isNotEmpty(quoteId)&&!("null").equals(quoteId)){
			sql += "and p.id = " + quoteId + "";
		}

//		if(!StringUtils.isEmpty(status)){
//			sql += "  and p.bs_Status = " + status + "";
//		}
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
		if(StringUtils.isNotEmpty(bsProdType)){
			sql += "  and p.bs_Prod_Type like '%" + bsProdType + "%'";
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
					"||p.bs_Dev_Type ||p.bs_Cust_Require || p.bs_position || p.bs_Require ||p.bs_Level ||p.bs_Dev_Type||u.user_name), '"
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
			map1.put("bsQuoteStatus", object[3]);
			map1.put("bsFinishTime", object[4]);
			map1.put("bsRemarks", object[5]);
			map1.put("bsProd", object[6]);
			map1.put("bsSimilarProd", object[7]);
			map1.put("bsDevType", object[8]);
			map1.put("bsProdType", object[9]);
			map1.put("bsCustName", object[10]);
			map1.put("bsStatus", object[11]);

			map1.put("bsPosition", object[12]);
			map1.put("bsMaterial", object[13]);
			map1.put("bsChkOutItem", object[14]);
			map1.put("bsChkOut", object[15]);
			map1.put("bsFunctionItem", object[16]);
			map1.put("bsFunction", object[17]);
			map1.put("bsRequire", object[18]);
			map1.put("bsLevel", object[19]);
			map1.put("bsCustRequire", object[20]);

			map1.put("bsProjVer",object[21]);
			map1.put("bsBade",object[22]);
			map1.put("bsLatest",object[23]);
			map1.put("bsStage",object[24]);

			map1.put("userName",object[25]);
			map1.put("createDate",object[26]);
			map1.put("bsTotal",object[27]);
			map1.put("bsStatus2", object[28]);


			list_new.add(map1);
		}
		Map map = new HashMap();
		map.put("List", DataGrid.create(list_new,  (int) count,
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		map.put("Nums",quoteDao.getNumByPurchaseAndBsStep(2));

		return ApiResponseResult.success().data(map);
	}


	@Override
	public ApiResponseResult getQuoteList(String keyword, String quoteId,String bsAgent, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub

		//代采的材料不显示  bs_agent = 0
		String hql = "select p.* from "+ProductMater.TABLE_NAME+" p where p.del_flag=0 and p.pk_quote="+quoteId;
		//20210113-fyx-去掉外协--?
		hql += " and p.bs_Type <> 'out' " ;

		if(bsAgent==null||("0").equals(bsAgent)||("null").equals(bsAgent)){
			hql += " and p.bs_agent = " +0;
		}else if(("1").equals(bsAgent)) {
			hql += " and p.bs_agent = " +1;
		}

		if (StringUtils.isNotEmpty(keyword)) {
			hql += "  and INSTR((p.bs_component || p.bs_mater_name ||p.bs_model ||p.fmemo ||p.bs_unit" +
					"||p.bs_color ||p.bs_machining_type || p.bs_supplier ), '"
					+ keyword + "') > 0 ";
		}

		//根据角色过滤可以查看的物料类型
		//1：如果设置了角色过滤的则过滤物料，否则认为是管理员可以查看全部方便测试
//		SysUser user = UserUtil.getSessionUser();
//		List<Map<String, Object>> lmp = productMaterDao.getRoleByUid(user.getId());
//		if(lmp.size()>0){
//			hql += " and p.pk_item_type_wg in (select wr.pk_item_type_wg from "+ItemTypeWgRole.TABLE_NAME+" wr where wr.del_flag=0 and wr.pk_sys_role in (select ur.role_id from "+UserRoleMap.TABLE_NAME+" ur where ur.del_flag=0 and ur.user_id="+user.getId()+")) ";
//		}



		Map<String, Object> param = new HashMap<String, Object>();
		long count = createSQLQuery(hql, param, null).size();

		//List<Map<String, Object>> list = super.findBySql(sql, param);

		int pn = pageRequest.getPageNumber() + 1;
		hql +="order by id";
		String sql = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM ( " + hql + " ) A  WHERE ROWNUM <= ("
				+ pn + ")*" + pageRequest.getPageSize() + "  )  WHERE RN > (" + pageRequest.getPageNumber() + ")*"
				+ pageRequest.getPageSize() + " ";
		List<ProductMater> list = createSQLQuery(sql, param, ProductMater.class);
		List<Unit> unitList = unitDao.findByDelFlag(0);

		for(ProductMater pm:list){
			List<Map<String, Object>> lm = priceCommDao.findByDelFlagAndItemName(pm.getBsMaterName());
			if(lm.size()>0){
				String str1 = JSON.toJSONString(lm); //此行转换
				String str = "";
				for(Map<String, Object> map:lm){
					str += map.get("RANGE_PRICE").toString()+",";
				}
				str = str.substring(0, str.length()-1);
				pm.setBsPriceList(str1);
			}
			if(unitList.size()>0){
				String str1 = JSON.toJSONString(unitList); //此行转换
				pm.setBsUnitList(str1);
			}
		}

		return ApiResponseResult.success().data(DataGrid.create(list, (int) count,
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 编辑
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(ProductMater productMater) throws Exception {
		if(productMater == null){
			return ApiResponseResult.failure("制造部材料信息不能为空！");
		}
		if(productMater.getId() == null){
			return ApiResponseResult.failure("制造部材料信息ID不能为空！");
		}
		ProductMater o = productMaterDao.findById((long) productMater.getId());
		if(o == null){
			return ApiResponseResult.failure("该制造部材料信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setFmemo(productMater.getFmemo());
		o.setBsGear(productMater.getBsGear());
		o.setBsSupplier(productMater.getBsSupplier());
		o.setPurchaseUnit(productMater.getPurchaseUnit());
		o.setBsAssess(productMater.getBsAssess());
		productMaterDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}


	@Override
	public ApiResponseResult cancelStatus(Long quoteId) throws Exception {
		Quote quote = quoteDao.findById((long) quoteId);
		quote.setBsStatus2Purchase(1);
		quoteDao.save(quote);
//		if(quote.getBsStatus2Purchase() ==2 ||quote.getBsStatus2Purchase() ==4){
//			return ApiResponseResult.failure("发起审批后不能取消确认");
//		}
//		List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuote(0,quoteId);
		Long userId = UserUtil.getSessionUser().getId();
		List<Map<String, Object>> lmp = productMaterDao.getRoleByUid(userId);
		List<ProductMater> productMaterList = new ArrayList<>();
		if(lmp.size()>0) {
			productMaterList = productMaterDao.selectPurchaseByUserId(quoteId, UserUtil.getSessionUser().getId());
		}else {
			 productMaterList = productMaterDao.findByDelFlagAndPkQuote(0,quoteId);
		}
		for(ProductMater o : productMaterList){
			//hjj-修改所有材料为未完成,只改成取消对应用户的材料单
			o.setBsStatusPurchase(0);
			o.setLastupdateDate(new Date());
			o.setLastupdateBy(UserUtil.getSessionUser().getId());
		}
		productMaterDao.saveAll(productMaterList);
		return ApiResponseResult.success("取消完成成功");
	}

	@Override
	public ApiResponseResult getStatus(Long pkQuote, Integer bsStatusPurchase) throws Exception {
		Long userId = UserUtil.getSessionUser().getId();
		List<Map<String, Object>> lmp = productMaterDao.getRoleByUid(userId);
		Integer number = 0;
		if(lmp.size()>0){
			number = productMaterDao.countByPkQuoteAndBsStatusPurchase(pkQuote,bsStatusPurchase,userId);
		}else {
			number = productMaterDao.countByPkQuoteAndBsStatusPurchase(pkQuote,bsStatusPurchase);
		}
		return ApiResponseResult.success("").data(number);
	}

	/**
	 * 导出数据
	 */
	public void exportExcel(HttpServletResponse response, Long quoteId,String bsAgent) throws Exception{
//		String hql = "select p.* from "+ProductMater.TABLE_NAME+" p where p.del_flag=0 and p.pk_quote="+quoteId;
//		Map<String, Object> param = new HashMap<String, Object>();
//		List<ProductMater> list = createSQLQuery(hql, param, ProductMater.class);



		List<ProductMater> list = new ArrayList<>();
		List<Map<String, Object>> lmp = productMaterDao.getRoleByUid(UserUtil.getSessionUser().getId());
		if(lmp.size()>0){
			list =productMaterDao.findByPkQuoteAndUser(quoteId,UserUtil.getSessionUser().getId());
		}else {
			list = productMaterDao.findByBsAgentAndDelFlagAndPkQuoteOrderById(0,0,quoteId);
		}

		list = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeIsNot(0,quoteId,"out");

		XSSFWorkbook workbook = new XSSFWorkbook();
		String filePath = "static/excelFile/采购填报价格模板.xlsx";
		Resource resource = new ClassPathResource("static/excelFile/采购填报价格模板.xlsx");
		InputStream in = resource.getInputStream();
		String[] map_arr = new String[]{"id","bsType","bsComponent","bsMaterName","bsModel","bsQty","bsUnit","bsAssess","purchaseUnit",
				"fmemo","mjPrice","bsGeneral","bsRefer","bsGear"};
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for(ProductMater productMater : list){
			Map<String, Object> map = new HashMap<>();
			map.put("id", productMater.getId());
			String bsType = productMater.getBsType();
			if(("hardware").equals(bsType)){
				map.put("bsType", "五金");
			}else if(("molding").equals(bsType)){
				map.put("bsType", "注塑");
			}else if(("surface").equals(bsType)){
				map.put("bsType", "表面处理");
			}else if(("packag").equals(bsType)){
				map.put("bsType", "组装");
			}
			map.put("purchaseUnit",productMater.getPurchaseUnit());
			map.put("bsComponent", productMater.getBsComponent());
			map.put("bsMaterName", productMater.getBsMaterName());
			map.put("bsModel", productMater.getBsModel());
			map.put("bsQty", productMater.getBsQty());
			if(productMater.getUnit()!=null){
				map.put("bsUnit", productMater.getUnit().getUnitCode());
			}
//			map.put("bsRadix", productMater.getBsRadix());
			map.put("bsExplain", productMater.getBsExplain());
//			if(productMater.getBsGeneral()!=null){
			map.put("bsGeneral", productMater.getBsGeneral()==1?"是":"否");
			map.put("mjPrice",productMater.getMjPrice());
			map.put("bsGear", productMater.getBsGear());
			map.put("bsRefer", productMater.getBsRefer());
			map.put("bsAssess", productMater.getBsAssess());
			map.put("fmemo", productMater.getFmemo());
			map.put("bsSupplier", productMater.getBsSupplier());
			listMap.add(map);
		}
		ExcelExport.export(response,listMap,workbook,map_arr,filePath,"采购填报价格模板.xlsx");
	}

	//防止读取Excel为null转String 报空指针异常
	public String tranCell(Object object)
	{
		if(object==null||object==""||("").equals(object)){
			return null;
		}else return object.toString().trim();
	}

	//    导入模板
	public ApiResponseResult doExcel(MultipartFile[] file,Long quoteId) throws Exception{
		try {
			Date doExcleDate = new Date();
			Long userId = UserUtil.getSessionUser().getId();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
			List<ProductMater> hardwareMaterList = new ArrayList<>();

			for (int row = 2; row <= maxRow; row++) {
				String id = tranCell(sheet.getRow(row).getCell(0));
//				String bsGear = tranCell(sheet.getRow(row).getCell(9));
				String bsAssess = tranCell(sheet.getRow(row).getCell(7));
				String purchaseUnit = tranCell(sheet.getRow(row).getCell(8));
				String fmemo = tranCell(sheet.getRow(row).getCell(9));
				String mjPrice = tranCell(sheet.getRow(row).getCell(10));
//				String bsSupplier = tranCell(sheet.getRow(row).getCell(13));
				ProductMater productMater = new ProductMater();
				if(StringUtils.isNotEmpty(id)){
					productMater = productMaterDao.findById(Long.parseLong(id));
					productMater.setId(Long.parseLong(id));
					productMater.setLastupdateBy(userId);
					productMater.setLastupdateDate(doExcleDate);
				}else {
					productMater.setCreateBy(userId);
					productMater.setCreateDate(doExcleDate);
				}
				productMater.setPurchaseUnit(purchaseUnit);
				productMater.setPkQuote(quoteId);
//				productMater.setBsGear(bsGear);
				productMater.setBsAssess(new BigDecimal(bsAssess));
				productMater.setFmemo(fmemo);
				productMater.setMjPrice(mjPrice);
//				productMater.setBsSupplier(bsSupplier);

				hardwareMaterList.add(productMater);
			}
			productMaterDao.saveAll(hardwareMaterList);
			return ApiResponseResult.success("导入成功");
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}

	@Override
	public ApiResponseResult doStatus(Long quoteId,List<ProductMater> productMaterList2) throws Exception {
		//未填写的数目
		Integer notFilled = 0;
		//判断是否全部完成标识
		boolean allFinished = true;
		//判断该条数据完成标识
		boolean thisFinished = true;
		//待确认的采购信息
		List<ProductMater> productMaterList = new ArrayList<>();
//		List<Map<String, Object>> lmp = productMaterDao.getRoleByUid(UserUtil.getSessionUser().getId());
//		if(lmp.size()>0){
//			notFilled = productMaterDao.countByPkQuoteAndUserId(quoteId,UserUtil.getSessionUser().getId());
//			productMaterList =productMaterDao.findByPkQuoteAndUser(quoteId,UserUtil.getSessionUser().getId());
//		}else {
//			notFilled =productMaterDao.countByDelFlagAndPkQuoteAndBsAssessIsNullAndBsTypeIsNotAndBsAgent(0,quoteId,"out",0);
//			productMaterList = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeIsNotAndBsAgent(0,quoteId,"out",0);
//		}
		productMaterList = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeIsNotAndBsAgent(0,quoteId,"out",0);
		if(productMaterList.size()==0){
			return ApiResponseResult.failure("确认完成失败！当前报价单无采购信息！");
		}
		productMaterDao.saveAll(productMaterList2);
		//判断是否该用户下的物料类型下的采购单是否评估价格存在空值
//		if(notFilled>0){
//			return ApiResponseResult.failure("确认完成失败！当前其他用户还有 "+notFilled +"条信息未填写评估价格");
//		}else {
			for(ProductMater o:productMaterList){
				if(o.getBsAssess()==null){
					allFinished = false;
//					thisFinished = false;
				}else {
//				o.setBsStatusPurchase(1);
					o.setLastupdateDate(new Date());
					o.setLastupdateBy(UserUtil.getSessionUser().getId());
				}
			}
			productMaterDao.saveAll(productMaterList);
//		}
		List<Quote> lo = quoteDao.findByDelFlagAndId(0, quoteId);
		//20210121-fyx-确认完成修改状态
		if(allFinished) {
			if (lo.size() > 0) {
				Quote o = lo.get(0);
				o.setBsStatus2Purchase(2);
				quoteDao.save(o);
			}
		}

		//2021/11/19 全部确认完成后开始计算价格
		//判断是否开始计算价格
		//判断制造部+采购部+外协部 是否全部审批完成
		if(allFinished) {
			List<Quote> lq = quoteDao.findByDelFlagAndStatus2AndId(quoteId);
			if (lq.size() > 0) {
				Quote quote = lq.get(0);
				quote.setBsStep(3);
				quote.setBsStatus2(2);
				quote.setBsEndTime2(new Date());
				quote.setBsStatus3(1);
				quoteDao.save(quote);
			}
		}
		quoteSumService.countMeterAndProcess(quoteId + "");
		return ApiResponseResult.success("确认完成成功！");
	}

	@Override
	public ApiResponseResult doSumHouLoss(Long quoteId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ApiResponseResult doGear(String id, String gear, String price) throws Exception {
		// TODO Auto-generated method stub
		ProductMater o = productMaterDao.findById(Long.parseLong(id));
		if(o == null){
			return ApiResponseResult.failure("该材料信息不存在！");
		}
		o.setBsGear(gear);
		if(!StringUtils.isEmpty(price)){
			o.setBsRefer(new BigDecimal(price));
		}
		o.setLastupdateDate(new Date());
		productMaterDao.save(o);
		return ApiResponseResult.success("操作成功!");
	}


	@Override
	public ApiResponseResult doCheckBefore(String keyword, String quoteId) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(quoteId)){
			return ApiResponseResult.failure("报价单ID为空!");
		}
		//查询未完成的价格
		List<ProductMater> lpm = productMaterDao.findByDelFlagAndPkQuoteAndBsStatusPurchaseAndBsAgentAndBsTypeIsNot(0, Long.parseLong(quoteId), 0,0,"out");
		if(lpm.size()>0){
			return ApiResponseResult.failure("存在未报价的物料信息，不能发起审批!");
		}else{
			return ApiResponseResult.success();
		}
	}

	/**
	 * 修改单位
	 */
	@Override
	@Transactional
	public ApiResponseResult updateUnit(Long id,String unitCode) throws Exception{
		if(id == null){
			return ApiResponseResult.failure("制造材料ID不能为空！");
		}
		ProductMater o = productMaterDao.findById((long) id);
		if(o == null){
			return ApiResponseResult.failure("制造材料不存在！");
		}
		o.setPurchaseUnit(unitCode);
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setLastupdateDate(new Date());
		productMaterDao.save(o);
		return ApiResponseResult.success("更新单位成功！");
	}
}
