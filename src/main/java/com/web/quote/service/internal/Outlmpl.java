package com.web.quote.service.internal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.web.quote.dao.QuoteDao;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseSql;
import com.utils.ExcelExport;
import com.utils.UserUtil;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.Quote;
import com.web.quote.service.OutService;

@Service(value = "OutService")
@Transactional(propagation = Propagation.REQUIRED)
public class Outlmpl extends BaseSql implements OutService {
	
	@Autowired
    private ProductMaterDao productMaterDao;
	@Autowired
	private QuoteDao quoteDao;
	
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
			statusTemp = "and p.bs_status2out = " +bsStatus;
		}
    	String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,p.bs_status2out col ,p.bs_position," +
				"p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level," +
				"p.bs_Cust_Require,p.bs_proj_ver,p.bs_bade,p.bs_latest,p.bs_stage,u.USER_NAME,TO_CHAR(p.CREATE_DATE,'yyyy-mm-dd hh24:mi:ss'),p.bs_total from "+Quote.TABLE_NAME+" p "
				+ " LEFT JOIN SYS_USER u on u.id = p.create_by"
				+ " where p.del_flag=0 and p.bs_step>=2 "+statusTemp;
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
					"||p.bs_Dev_Type ||p.bs_Cust_Require || p.bs_position || p.bs_Require ||p.bs_Level ||p.bs_Dev_Type ||u.userName), '"
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
			
			list_new.add(map1);
		}
		Map map = new HashMap();
		map.put("List",DataGrid.create(list_new, (int) count,
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		map.put("Nums",quoteDao.getNumByOutAndBsStep(2));

		return ApiResponseResult.success().data(map);
    }


	@Override
	public ApiResponseResult getQuoteList(String keyword, String quoteId, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		
		String hql = "select p.* from "+ProductMater.TABLE_NAME+" p where p.del_flag=0 and p.pk_quote="+quoteId;
		
		int pn = pageRequest.getPageNumber() + 1;
		String sql = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM ( " + hql + " ) A  WHERE ROWNUM <= ("
				+ pn + ")*" + pageRequest.getPageSize() + "  )  WHERE RN > (" + pageRequest.getPageNumber() + ")*"
				+ pageRequest.getPageSize() + " ";
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		//List<Map<String, Object>> list = super.findBySql(sql, param);
		List<ProductMater> list = createSQLQuery(sql, param, ProductMater.class);
		long count = createSQLQuery(hql, param, null).size();
		
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
		o.setBsAssess(productMater.getBsAssess());
		productMaterDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 导出数据
	 */
	public void exportExcel(HttpServletResponse response, Long quoteId) throws Exception{
		String hql = "select p.* from "+ProductMater.TABLE_NAME+" p where p.del_flag=0 and p.pk_quote="+quoteId;
		Map<String, Object> param = new HashMap<String, Object>();
		List<ProductMater> list = createSQLQuery(hql, param, ProductMater.class);
        XSSFWorkbook workbook = new XSSFWorkbook();
        String filePath = "static/excelFile/采购填报价格模板.xlsx";
//		Resource resource = new ClassPathResource("static/excelFile/采购填报价格模板.xlsx");
//		InputStream in = resource.getInputStream();
		String[] map_arr = new String[]{"id","bsType","bsComponent","bsMaterName","bsModel","bsQty","bsUnit","bsRadix",
				"bsGeneral","bsGear","bsRefer","bsAssess","fmemo","bsSupplier"};
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
			map.put("bsComponent", productMater.getBsComponent());
			map.put("bsMaterName", productMater.getBsMaterName());
			map.put("bsModel", productMater.getBsModel());
			map.put("bsQty", productMater.getBsQty());
			map.put("bsUnit", productMater.getBsUnit());
			map.put("bsRadix", productMater.getBsRadix());
			if(productMater.getBsGeneral()!=null){
				map.put("bsGeneral", productMater.getBsGeneral()==1?"是":"否");
			}
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
			//五金工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、成型周期(S)、工序良率、备注
			for (int row = 2; row <= maxRow; row++) {
				String id = tranCell(sheet.getRow(row).getCell(0));
				String bsGear = tranCell(sheet.getRow(row).getCell(9));
				String bsAssess = tranCell(sheet.getRow(row).getCell(11));
				String fmemo = tranCell(sheet.getRow(row).getCell(12));
				String bsSupplier = tranCell(sheet.getRow(row).getCell(13));
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
				productMater.setPkQuote(quoteId);
				productMater.setBsGear(bsGear);
				productMater.setBsAssess(new BigDecimal(bsAssess));
				productMater.setFmemo(fmemo);
				productMater.setBsSupplier(bsSupplier);

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
}
