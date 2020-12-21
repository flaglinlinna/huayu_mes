package com.web.quote.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseSql;
import com.web.quote.dao.QuoteDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.service.QuoteProductService;

@Service(value = "QuoteProductService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteProductlmpl extends BaseSql implements QuoteProductService {
	
	@Autowired
    private QuoteDao quoteDao;
    
    /**
     * 获取报价单列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,String style,PageRequest pageRequest)throws Exception{
    	// 查询条件1
		/*List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("bsStep", SearchFilter.Operator.EQ, 2));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsProd", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Quote> spec = Specification.where(BaseService.and(filters, Quote.class));
		Specification<Quote> spec1 = spec.and(BaseService.or(filters1, Quote.class));
		Page<Quote> page = quoteDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));*/
    	
    	String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,decode(i.bs_end_time,null,'0','1') col from "+Quote.TABLE_NAME+" p "
						+ " left join price_quote_item i on p.id=i.pk_quote  where p.del_flag=0 and p.bs_step=2 ";
		if (StringUtils.isNotEmpty(keyword)) {
			/*sql += "  and INSTR((p.line_No || p.line_Name || p.liner_Code || p.liner_Name ),  '"
					+ keyword + "') > 0 ";*/
		}
		//checkStatus--需要转移的类型
		if(StringUtils.isNotEmpty(style)){
			if(style.equals("hardware")){
				sql += "  and i.bs_code in ('B001','C001') ";
			}else if(style.equals("molding")){
				sql += "  and i.bs_code in ('B002','C002') ";
			}else if(style.equals("surface")){
				sql += "  and i.bs_code in ('B003','C003') ";
			}else if(style.equals("packag")){
				sql += "  and i.bs_code in ('B004','C004') ";
			}
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
    
    /**
     * 获取报价单-项目列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getItemPage(Long quoteId,String style)throws Exception{
    	//List<QuoteItem> list=quoteItemDao.findByDelFlagAndPkQuoteAndBsStyle(0,id,bsStatus);
    	String sql = "select a.* from "+QuoteItem.TABLE_NAME+" a" + " where 1=1 and del_Flag=0  ";
    	sql += "and a.pk_quote="+quoteId;
    	if(StringUtils.isNotEmpty(style)){
			if(style.equals("hardware")){
				sql += "  and a.bs_code in ('B001','C001') ";
			}else if(style.equals("molding")){
				sql += "  and a.bs_code in ('B002','C002') ";
			}else if(style.equals("surface")){
				sql += "  and a.bs_code in ('B003','C003') ";
			}else if(style.equals("packag")){
				sql += "  and a.bs_code in ('B004','C004') ";
			}
		}
    	Map<String, Object> param = new HashMap<String, Object>();
    	List<QuoteItem> list = createSQLQuery(sql, param, QuoteItem.class);
    	return ApiResponseResult.success().data(list);
    }

}
