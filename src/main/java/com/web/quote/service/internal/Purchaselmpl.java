package com.web.quote.service.internal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import com.utils.ExcelExport;
import com.utils.UserUtil;
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

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseSql;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.Quote;
import com.web.quote.service.PurchaseService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Service(value = "PurchaseService")
@Transactional(propagation = Propagation.REQUIRED)
public class Purchaselmpl extends BaseSql implements PurchaseService {
	
	@Autowired
    private ProductMaterDao productMaterDao;
	
    /**
     * 查询列表
     */
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,PageRequest pageRequest) throws Exception {
    	String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,p.bs_status2purchase col from "+Quote.TABLE_NAME+" p "
						+ " where p.del_flag=0 and p.bs_step=2  ";
		if (StringUtils.isNotEmpty(keyword)) {
			/*sql += "  and INSTR((p.line_No || p.line_Name || p.liner_Code || p.liner_Name ),  '"
					+ keyword + "') > 0 ";*/
		}
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
			
			list_new.add(map1);
		}
		
		
		return ApiResponseResult.success().data(DataGrid.create(list_new, (int) count,
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize())); 
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
				"bsGeneral","bsGear","bsRefer","bsAssess","fmemo","bsSupplier","bsExplain"};
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
			map.put("bsExplain", productMater.getBsExplain());
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
		}else return object.toString();
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

	@Override
	public ApiResponseResult doStatus(Long quoteId) throws Exception {
		if(productMaterDao.countByDelFlagAndPkQuoteAndBsAssessIsNull(0,quoteId)>0){
			return ApiResponseResult.failure("确认完成失败！请填写完所有评估价格后确认！");
		}else {
			List<ProductMater> productMaterList = productMaterDao.findByDelFlagAndPkQuote(0,quoteId);
			for(ProductMater o:productMaterList){
				o.setBsStatus(1);
				o.setLastupdateDate(new Date());
				o.setLastupdateBy(UserUtil.getSessionUser().getId());
			}
			productMaterDao.saveAll(productMaterList);
		}
		return ApiResponseResult.success("确认完成成功！");
	}

	@Override
	public ApiResponseResult doSumHouLoss(Long quoteId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
