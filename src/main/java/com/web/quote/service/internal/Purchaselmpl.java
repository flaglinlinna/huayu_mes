package com.web.quote.service.internal;

import java.io.InputStream;
import java.util.*;

import com.utils.ExcelExport;
import com.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
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
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,decode(p.bs_end_time3,null,'0','1') col from "+Quote.TABLE_NAME+" p "
						+ " where p.del_flag=0 and p.bs_step=3 ";
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
		Resource resource = new ClassPathResource("static/excelFile/采购填报价格模板.xlsx");
		InputStream in = resource.getInputStream();
		String[] map_arr = new String[]{"id","bsType","bsComponent","bsMaterName","bsModel","bsQty","bsUnit","bsRadix",
				"bsGeneral","bsGear","bsRefer","bsAssess","fmemo","bsSupplier"};
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for(ProductMater productMater : list){
			Map<String, Object> map = new HashMap<>();
			map.put("id", productMater.getId());
			map.put("bsType", productMater.getBsType());
			map.put("bsComponent", productMater.getBsComponent());
			map.put("bsMaterName", productMater.getBsMaterName());
			map.put("bsModel", productMater.getBsModel());
			map.put("bsQty", productMater.getBsQty());
			map.put("bsUnit", productMater.getBsUnit());
			map.put("bsRadix", productMater.getBsRadix());
			map.put("bsGeneral", productMater.getBsGeneral());
			map.put("bsGear", productMater.getBsGear());
			map.put("bsRefer", productMater.getBsRefer());
			map.put("bsAssess", productMater.getBsAssess());
			map.put("fmemo", productMater.getFmemo());
			map.put("bsSupplier", productMater.getBsSupplier());
			listMap.add(map);
		}
		ExcelExport.export(response,listMap,workbook,in,map_arr,"采购填报价格模板.xlsx");
	}
}
