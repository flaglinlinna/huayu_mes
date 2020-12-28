package com.web.quote.service.internal;

import java.math.BigDecimal;
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
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.dao.ProductProcessDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteMouldDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.ProductProcess;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteMould;
import com.web.quote.service.QuoteSumService;

@Service(value = "QuoteSumService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteSumlmpl extends BaseSql implements QuoteSumService {
	
	@Autowired
    private ProductMaterDao productMaterDao;
	@Autowired
    private QuoteDao quoteDao;
	@Autowired
    private ProductProcessDao productProcessDao;
	@Autowired
    private QuoteMouldDao quoteMouldDao;
	
	
    /**
     * 查询列表
     */
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,PageRequest pageRequest) throws Exception {
    	String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,decode(p.bs_end_time3,null,'0','1') col from "+Quote.TABLE_NAME+" p "
						+ " where p.del_flag=0 and p.bs_step>3 ";
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

	@Override
	public ApiResponseResult getSumByQuote(String quoteId) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(quoteId)){
			return ApiResponseResult.failure("报价单ID不能为空");
		}
		Map map = new HashMap();
		Quote quote = quoteDao.findById(Long.parseLong(quoteId));
		map.put("Quote", quote);//报价基础信息
		
		//1:材料价格汇总
		//BigDecimal cl_all = new BigDecimal(0);//材料总价格
		BigDecimal cl_hardware = new BigDecimal(0);//五金
		BigDecimal cl_molding = new BigDecimal(0);//注塑
		BigDecimal cl_surface = new BigDecimal(0);//表面处理
		BigDecimal cl_packag = new BigDecimal(0);//组装
		
		List<ProductMater> lpm = productMaterDao.findByDelFlagAndPkQuote(0,Long.valueOf(quoteId));
		for(ProductMater pm:lpm){
			if(pm.getBsType().equals("hardware")){
				cl_hardware = cl_hardware.add(pm.getBsFee());
			}else if(pm.getBsType().equals("molding")){
				cl_molding = cl_molding.add(pm.getBsFee());
			}else if(pm.getBsType().equals("surface")){
				cl_surface = cl_surface.add(pm.getBsFee());
			}else if(pm.getBsType().equals("packag")){
				cl_packag = cl_packag.add(pm.getBsFee());
			}
		}
		
		//2:人工成本+制造费用
		BigDecimal lh_hardware = new BigDecimal(0);//五金
		BigDecimal lh_molding = new BigDecimal(0);//注塑
		BigDecimal lh_surface = new BigDecimal(0);//表面处理
		BigDecimal lh_packag = new BigDecimal(0);//组装
		BigDecimal lw_hardware = new BigDecimal(0);//五金
		BigDecimal lw_molding = new BigDecimal(0);//注塑
		BigDecimal lw_surface = new BigDecimal(0);//表面处理
		BigDecimal lw_packag = new BigDecimal(0);//组装
		List<ProductProcess> lpp = productProcessDao.findByDelFlagAndPkQuote(0,Long.valueOf(quoteId));
		for(ProductProcess pp:lpp){
			if(pp.getBsType().equals("hardware")){
				lh_hardware = lh_hardware.add(pp.getBsFeeLhAll());
				lw_hardware = lw_hardware.add(pp.getBsFeeMhAll());
			}else if(pp.getBsType().equals("molding")){
				lh_molding = lh_molding.add(pp.getBsFeeLhAll());
				lw_molding = lw_molding.add(pp.getBsFeeMhAll());
			}else if(pp.getBsType().equals("surface")){
				lh_surface = lh_surface.add(pp.getBsFeeLhAll());
				lw_surface = lw_surface.add(pp.getBsFeeMhAll());
			}else if(pp.getBsType().equals("packag")){
				lh_packag = lh_packag.add(pp.getBsFeeLhAll());
				lw_packag = lw_packag.add(pp.getBsFeeMhAll());
			}
		}
		//3：小计
		BigDecimal hardware_all = cl_hardware.add(lh_hardware).add(lw_hardware);//五金小计
		BigDecimal molding_all = cl_molding.add(lh_molding).add(lw_molding);//注塑小计
		BigDecimal surface_all = cl_surface.add(lh_surface).add(lw_surface);//表面处理小计
		BigDecimal packag_all = cl_packag.add(lh_packag).add(lw_packag);//组装小计
		
		//4.模具费用
		List<QuoteMould> lqm = quoteMouldDao.findByDelFlagAndPkQuote(0, Long.valueOf(quoteId));
		BigDecimal mould_all = new BigDecimal(0);
		for(QuoteMould qm:lqm){
			mould_all = mould_all.add(qm.getBsActQuote());//实际报价
		}
		
		//5.生产成本
		BigDecimal p_cb = new BigDecimal(0);//生产成本
		BigDecimal wx_all = new BigDecimal(0);//外协工艺成本?
		
		//6.生产管理费-管理费用的计算=管理费率*产品生产成本
		BigDecimal gl = quote.getBsManageFee().multiply(p_cb).divide(new BigDecimal(100));
		
		//7.利润
		
		return ApiResponseResult.success().data(map);
	}
	/**
	 * 第二步全部审批通过后，计算物料的价格，工序的人工费和制费
	 */
	@Override
	public ApiResponseResult countMeterAndProcess(String quoteId) throws Exception {
		// TODO Auto-generated method stub
		//五金，表面，组装的材料总价格(未税)计算公式-单价*用量/基数
		List<ProductMater> lpm3 = productMaterDao.findByDelFlagAnd3Tyle(Long.valueOf(quoteId));
		for(ProductMater pm:lpm3){
			BigDecimal bsRadix = new BigDecimal("1");//基数
			if(!StringUtils.isEmpty(pm.getBsRadix())){
				if(!"1".equals(pm.getBsRadix())){
					bsRadix = new BigDecimal(pm.getBsRadix());
				}
			}
			pm.setBsFee(pm.getBsAssess().multiply(pm.getBsQty().divide(bsRadix)));
		}
		productMaterDao.saveAll(lpm3);
		//注塑的材料总价格(未税)计算公式-材料单价*(制品重+水口重/穴数)/用量基数
		List<ProductMater> lpm1 = productMaterDao.findByDelFlagAndMolding(Long.valueOf(quoteId));
		for(ProductMater pm:lpm1){
			BigDecimal bsRadix = new BigDecimal("1");//基数
			if(!StringUtils.isEmpty(pm.getBsRadix())){
				if(!"1".equals(pm.getBsRadix())){
					bsRadix = new BigDecimal(pm.getBsRadix());
				}
			}
			BigDecimal bsCave = new BigDecimal(pm.getBsCave());//穴数
			BigDecimal qty = new BigDecimal(pm.getBsWaterGap()).divide(bsCave).add(pm.getBsProQty());//水口重/穴数+制品重
			pm.setBsFee(pm.getBsAssess().multiply(qty).divide(bsRadix));
		}
		productMaterDao.saveAll(lpm1);
		//五金-人工工时费（元/H）*人数*成型周期(S）/3600 /基数；制费工时费（元/H）*成型周期(S）/3600/ /基数
		List<ProductProcess> lpp_hardware = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, Long.valueOf(quoteId), "hardware");
		for(ProductProcess pp:lpp_hardware){
			pp.setBsFeeLhAll(pp.getBsFeeLh().multiply(pp.getBsUserNum()).multiply(pp.getBsCycle()).divide(new BigDecimal("3600")).divide(pp.getBsRadix()));
		    
			pp.setBsFeeMhAll(pp.getBsFeeMh().multiply(pp.getBsCycle()).divide(new BigDecimal("3600")).divide(pp.getBsRadix()));
		}
		productProcessDao.saveAll(lpp_hardware);
		//注塑-人工工时费（元/H）*人数*成型周期(S）/3600/ 穴数/基数;制费工时费（元/H）*成型周期(S）/3600/穴数/ 基数
		List<ProductProcess> lpp_molding = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, Long.valueOf(quoteId), "molding");
		for(ProductProcess pp:lpp_molding){
			BigDecimal bsCave = new BigDecimal(pp.getBsCave());//穴数
			pp.setBsFeeLhAll(pp.getBsFeeLh().multiply(pp.getBsUserNum()).multiply(pp.getBsCycle()).divide(new BigDecimal("3600")).divide(bsCave).divide(pp.getBsRadix()));
		    
			pp.setBsFeeMhAll(pp.getBsFeeMh().multiply(pp.getBsCycle()).divide(new BigDecimal("3600")).divide(bsCave).divide(pp.getBsRadix()));
		}
		productProcessDao.saveAll(lpp_molding);
		//更新表面处理-
		return null;
	}
}
