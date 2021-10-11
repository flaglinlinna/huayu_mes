package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.*;
import com.utils.enumeration.BasicStateEnum;
import com.web.quote.dao.*;
import com.web.quote.entity.*;
import com.web.quote.service.QuoteSumBomService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service(value = "QuoteSumBomService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteSumBomlmpl extends BaseSql implements QuoteSumBomService {

	@Autowired
	private QuoteDao quoteDao;
	@Autowired
	private QuoteSumBomDao quoteSumBomDao;
	@Autowired
	QuoteProcessDao quoteProcessDao;

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String quoteId,String keyword,String bsStatus,String bsCode,String bsType,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsProdType,String bsSimilarProd,
									 String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,PageRequest pageRequest) throws Exception {

		String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,decode(p.bs_end_time3,null,'1','2') col ,p.bs_position," +
				"p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level," +
				"p.bs_Cust_Require from "+Quote.TABLE_NAME+" p "
				+ " where p.del_flag=0 and p.bs_step>2 and p.bs_end_time3 is not null ";
		if(StringUtils.isNotEmpty(quoteId)&&!("null").equals(quoteId)){
			sql += "and p.id = " + quoteId + "";
		}
		if(!StringUtils.isEmpty(bsStatus)){
			sql += "  and p.bs_Status_item = " + bsStatus + "";
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
			//map1.put("bsStatus", object[3]);
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

			list_new.add(map1);
		}
		HashMap map = new HashMap();
		map.put("List",DataGrid.create(list_new, (int) count,
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		map.put("Nums",quoteDao.getNumByStatusItemAndBsStep());

		return ApiResponseResult.success().data(map);
	}

	@Override
	public ApiResponseResult getItemList(Long quoteId,String keyword,PageRequest pageRequest) throws Exception {
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, quoteId));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
//			filters1.add(new SearchFilter("itemName", SearchFilter.Operator.LIKE, keyword));
//			filters1.add(new SearchFilter("rangePrice", SearchFilter.Operator.LIKE, keyword));
//			filters1.add(new SearchFilter("alternativeSuppliers", SearchFilter.Operator.LIKE, keyword));
		}
//		Specification<QuoteSumBom> spec = Specification.where(BaseService.and(filters, QuoteSumBom.class));
//		Specification<QuoteSumBom> spec1 = spec.and(BaseService.or(filters1, QuoteSumBom.class));
//		Page<QuoteSumBom> page = quoteSumBomDao.findAll(spec1, pageRequest);
		List<QuoteSumBom> quoteSumBomList  =quoteSumBomDao.findByDelFlagAndPkQuote(0,quoteId);
//		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
//				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		return ApiResponseResult.success().data(quoteSumBomList);
	}

	public void exportExcel2(HttpServletResponse response, Long pkQuote) throws Exception {
		//按3层的树状结构导出
		List<QuoteSumBom> quoteBomList = new ArrayList<>();
		List<QuoteSumBom> quoteSumBomList = quoteSumBomDao.findByDelFlagAndPkQuoteAndParenId(0,pkQuote,0L);
		for(QuoteSumBom o:quoteSumBomList){
			quoteBomList.add(o);
			List<QuoteSumBom> quoteSumBomList2 = quoteSumBomDao.findByDelFlagAndPkQuoteAndParenId(0,pkQuote,o.getId());
			for(QuoteSumBom o2:quoteSumBomList2){
				quoteBomList.add(o2);
				List<QuoteSumBom> quoteSumBomList3 = quoteSumBomDao.findByDelFlagAndPkQuoteAndParenId(0,pkQuote,o2.getId());
				quoteBomList.addAll(quoteSumBomList3);
			}
		}
		String excelPath = "static/excelFile/";
		String fileName = "虚拟料号导入模板.xlsx";
		String[] map_arr = new String[]{"id","bsItemCode","bsItemCodeReal","bsMaterName","wcName"};
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(QuoteSumBom quoteSumBom :quoteBomList){
			Map<String, Object> map = new HashMap<>();
			map.put("id", quoteSumBom.getId());
			map.put("bsItemCode", quoteSumBom.getBsItemCode());
			map.put("bsItemCodeReal",quoteSumBom.getBsItemCodeReal());
			map.put("bsMaterName",quoteSumBom.getBsMaterName());
			if(quoteSumBom.getWc()!=null) {
				map.put("wcName", quoteSumBom.getWc().getWorkcenterName());
			}
			list.add(map);
		}
		ExcelExport.export(response,list,workbook,map_arr,excelPath+fileName,fileName);
	}

	@Override
	public void exportExcel(HttpServletResponse response, Long pkQuote) throws Exception {
		String excelPath = "static/excelFile/";
		String fileName = "";
		String[] map_arr = null;
		fileName = "虚拟料号对应.xlsx";
		map_arr = new String[]{"id","gradation","bsItemCodeReal","bsElement", "bsName", "bsLinkName", "itemType", "workCenter", "pkProc", "bsOrder", "bsGroups","bsMaterName","bsModel","fmemo"};
		XSSFWorkbook workbook = new XSSFWorkbook();
		Resource resource = new ClassPathResource(excelPath + fileName);
//			InputStream in = resource.getInputStream();
		List<QuoteProcess> productProcessesList = quoteProcessDao.findByDelFlagAndPkQuote(0,pkQuote);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (QuoteProcess bs : productProcessesList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("gradation", bs.getGradation());
			map.put("bsElement", bs.getBsElement());
			map.put("bsItemCodeReal", bs.getBsItemCodeReal());
			map.put("bsName", bs.getBsName());
			map.put("bsOrder", bs.getBsOrder());
			if (bs.getProc() != null) {
				map.put("pkProc", bs.getProc().getProcName());
			} else {
				map.put("pkProc", "");
			}
//            map.put("bsModelType", bs.getBsModelType());
			if (bs.getBjWorkCenter() != null) {
				map.put("workCenter", bs.getBjWorkCenter().getWorkcenterName());
			}
			map.put("bsLinkName", bs.getBsLinkName());
			map.put("itemType", bs.getItemType());
			map.put("bsGroups", bs.getBsGroups());
			map.put("fmemo", bs.getFmemo());
			map.put("bsMaterName", bs.getBsMaterName());
			map.put("bsModel", bs.getBsModel());
			map.put("fmemo", bs.getFmemo());
			list.add(map);
		}
		ExcelExport.export(response, list, workbook, map_arr, excelPath + fileName, fileName);
	}


	//防止读取Excel为null转String 报空指针异常
	public String tranCell(Object object)
	{
		if(object==null||object==""||("").equals(object)){
			return null;
		}else return object.toString().trim().trim();
	}

	//导入模板
//	@Override
	public ApiResponseResult doExcel2(MultipartFile[] file, Long pkQuote) throws Exception{
		try {

			Date doExcleDate = new Date();
			Long userId = UserUtil.getSessionUser().getId();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
			List<QuoteSumBom> quoteSumBomList = new ArrayList<>();
			Integer fail = 0;
			//前两行为标题
			for (int row = 2; row <= maxRow; row++) {
				QuoteSumBom quoteSumBom = new QuoteSumBom();
				String ids =  tranCell(sheet.getRow(row).getCell(0));
				String bsItemCode =  tranCell(sheet.getRow(row).getCell(1));
				String bsItemCodeReal =  tranCell(sheet.getRow(row).getCell(2));
				String bsMaterName = tranCell(sheet.getRow(row).getCell(3));
				String wcName = tranCell(sheet.getRow(row).getCell(4));
				if(StringUtils.isNotEmpty(ids)){
					quoteSumBom = quoteSumBomDao.findById(Long.parseLong(ids));
				}else {
					fail++;
					continue;
				}
				quoteSumBom.setBsItemCodeReal(bsItemCodeReal);
				quoteSumBom.setLastupdateDate(doExcleDate);
				quoteSumBom.setLastupdateBy(userId);
				quoteSumBomList.add(quoteSumBom);
			}
			quoteSumBomDao.saveAll(quoteSumBomList);
			return ApiResponseResult.success("导入成功,共导入"+quoteSumBomList.size()+";条,失败:"+fail);
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}

	@Override
	public ApiResponseResult doExcel(MultipartFile[] file, Long pkQuote) throws Exception{
		try {
			Date doExcleDate = new Date();
			Long userId = UserUtil.getSessionUser().getId();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
			List<QuoteProcess> quoteProcessList = new ArrayList<>();
			Integer fail = 0;
			//前两行为标题
			for (int row = 2; row <= maxRow; row++) {
				QuoteProcess quoteProcess = new QuoteProcess();
				String ids =  tranCell(sheet.getRow(row).getCell(0));
//				String gradation =  tranCell(sheet.getRow(row).getCell(1));
				String bsItemCodeReal =  tranCell(sheet.getRow(row).getCell(2));

				XSSFCell xssfCell1 = sheet.getRow(row).getCell(1);
				xssfCell1.setCellType(xssfCell1.CELL_TYPE_STRING);
				String gradation = tranCell(xssfCell1);

				if(StringUtils.isNotEmpty(ids)){
					quoteProcess = quoteProcessDao.findById(Long.parseLong(ids));
				}else {
					fail++;
					continue;
				}
				quoteProcess.setBsItemCodeReal(bsItemCodeReal);
				quoteProcess.setLastupdateDate(doExcleDate);
				quoteProcess.setGradation(gradation);
				quoteProcess.setLastupdateBy(userId);
				quoteProcessList.add(quoteProcess);
			}
			quoteProcessDao.saveAll(quoteProcessList);
			return ApiResponseResult.success("导入成功,共导入"+quoteProcessList.size()+";条,失败:"+fail);
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}

	@Override
	public ApiResponseResult editBsItemCodeReal(Long id, String bsItemCodeReal) throws Exception {
		QuoteSumBom o = quoteSumBomDao.findById((long) id);
		if(o==null){
			return ApiResponseResult.failure("编辑失败！");
		}else {
			o.setBsItemCodeReal(bsItemCodeReal);
			o.setLastupdateBy(UserUtil.getSessionUser().getId());
			o.setLastupdateDate(new Date());
			quoteSumBomDao.save(o);
		}
		return ApiResponseResult.success("编辑成功");
	}
}
