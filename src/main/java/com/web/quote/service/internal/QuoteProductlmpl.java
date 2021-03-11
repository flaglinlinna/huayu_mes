package com.web.quote.service.internal;

import java.util.ArrayList;
import java.util.Date;
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
import com.utils.UserUtil;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.service.QuoteProductService;

@Service(value = "QuoteProductService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteProductlmpl extends BaseSql implements QuoteProductService {

	@Autowired
    private QuoteItemDao quoteItemDao;
	@Autowired
    private QuoteDao quoteDao;
    /**
     * 获取报价单列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,String style,String status,String bsCode,String bsType,String bsStatus,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsSimilarProd,
									 String bsPosition ,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,String quoteId,PageRequest pageRequest)throws Exception{
    	String temp = "";
    	String statusTemp ="";
    	if(StringUtils.isNotEmpty(style)){
			if(style.equals("hardware")){
				temp = "  p.bs_status2hardware ";
			}else if(style.equals("molding")){
				temp = "  p.bs_status2molding ";
			}else if(style.equals("surface")){
				temp = "  p.bs_status2surface ";
			}else if(style.equals("packag")){
				temp = "  p.bs_status2packag ";
			}
		}
    	if(StringUtils.isNotEmpty(status)){
			if(style.equals("hardware")){
				statusTemp = " and p.bs_status2hardware ="+status;
			}else if(style.equals("molding")){
				statusTemp = " and p.bs_status2molding ="+status;
			}else if(style.equals("surface")){
				statusTemp = "and  p.bs_status2surface ="+status;
			}else if(style.equals("packag")){
				statusTemp = "and  p.bs_status2packag ="+status;
			}
		}
    	String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_cust_name,"+temp+" col ,p.bs_position," +
				"  p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level," +
				"  p.bs_Cust_Require,p.bs_proj_ver,p.bs_bade,p.bs_latest,p.bs_stage  from "+Quote.TABLE_NAME+" p "
						+ "  where p.del_flag=0 and p.bs_step>=2 "+statusTemp;

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
			map1.put("bsQuoteStatus", object[3]);
			map1.put("bsFinishTime", object[4]);
			map1.put("bsRemarks", object[5]);
			map1.put("bsProd", object[6]);
			map1.put("bsSimilarProd", object[7]);
			map1.put("bsDevType", object[8]);
			map1.put("bsProdType", object[9]);
			map1.put("bsCustName", object[10]);
			map1.put("bsStatus", object[11]);
			//[5403, EQ-20210104140202, XPBJ, 1, 2021-01-06, 流程测试2, AAA, null, CNC1, 穿戴, 1, 3]

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

			list_new.add(map1);
		}
		Map map = new HashMap();

		String sql_num = "select count(p.id) as nums,"+temp+" as status from "+Quote.TABLE_NAME+" p where p.del_flag=0 and p.bs_step >=2 group by "+temp+" ";
		List<Object[]>  list_num = createSQLQuery(sql_num, param);
		List<Map<String, Object>> list_num2 = new ArrayList<Map<String, Object>>();
		for(int i = 0;i<list_num.size();i++){
			Object[] object=(Object[]) list_num.get(i);
			Map<String, Object> map1 = new HashMap<>();
			map1.put("NUMS",object[0]);
			map1.put("STATUS",object[1]);
			list_num2.add(map1);
		}

		map.put("List", DataGrid.create(list_new,  (int) count,
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		map.put("Nums", list_num2);
		return ApiResponseResult.success().data(map);
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

	@Override
	public ApiResponseResult doCheckBefore(String keyword, String quoteId, String bsType) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(quoteId)){
			return ApiResponseResult.failure("报价单ID为空");
		}
		if(StringUtils.isEmpty(bsType)){
			return ApiResponseResult.failure("报价单类型为空");
		}
		int m=0,p=0;
		if(bsType.equals("hardware")){
			m = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"B001",1);
			p = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"C001",1);
		}else if(bsType.equals("molding")){
			m = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"B002",1);
			p = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"C002",1);
		}else if(bsType.equals("surface")){
			m = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"B003",1);
			p = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"C003",1);
		}else if(bsType.equals("packag")){
			m = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"B004",1);
			p = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.valueOf(quoteId),"C004",1);
		}
		if((m+p) == 0){
			return ApiResponseResult.success();
		}else{
			return ApiResponseResult.failure("存在未确认完成的项目!");
		}
	}

	@Override
	public ApiResponseResult doItemFinish(String code, Long quoteId,Integer status) throws Exception {
		// TODO Auto-generated method stub
		//2.1 查询大类是否都已经全部提交
		List<Quote> lo = quoteDao.findByDelFlagAndId(0, quoteId);
		if(code.equals("B001") || code.equals("C001")){//五金
			if(status==3) {
				List<QuoteItem> lqii = quoteItemDao.getStatusByHardware(quoteId);
				if (lqii.size() == 0) {
					//2.2 全部完成审批
					if (lo.size() > 0) {
						Quote o = lo.get(0);
						o.setBsStatus2Hardware(status);
						quoteDao.save(o);
					}
				}
			}else {
				if (lo.size() > 0) {
					Quote o = lo.get(0);
					o.setBsStatus2Hardware(status);
					quoteDao.save(o);
				}
			}
		}else if(code.equals("B002") || code.equals("C002")){//注塑
			if(status==3) {
				List<QuoteItem> lqii = quoteItemDao.getStatusByMolding(quoteId);
				if(lqii.size()== 0){
					//2.2 全部完成审批
					if(lo.size()>0){
						Quote o = lo.get(0);
						o.setBsStatus2Molding(status);
						o.setLastupdateDate(new Date());
						o.setLastupdateBy(UserUtil.getSessionUser().getId());
						quoteDao.save(o);
					}
				}
			}else {
				if(lo.size()>0){
					Quote o = lo.get(0);
					o.setBsStatus2Molding(status);
					o.setLastupdateDate(new Date());
					o.setLastupdateBy(UserUtil.getSessionUser().getId());
					quoteDao.save(o);
				}
			}
		}else if(code.equals("B003") || code.equals("C003")){//表面处理
				if(status==3) {
					List<QuoteItem> lqii = quoteItemDao.getStatusBySurface(quoteId);
					if (lqii.size() == 0) {
						//2.2 全部完成审批
						if (lo.size() > 0) {
							Quote o = lo.get(0);
							o.setBsStatus2Surface(status);
							o.setLastupdateDate(new Date());
							o.setLastupdateBy(UserUtil.getSessionUser().getId());
							quoteDao.save(o);
						}
					}
				}
				else {
					if (lo.size() > 0) {
						Quote o = lo.get(0);
						o.setBsStatus2Surface(status);
						o.setLastupdateDate(new Date());
						o.setLastupdateBy(UserUtil.getSessionUser().getId());
						quoteDao.save(o);
					}
				}
		}else if(code.equals("B004") || code.equals("C004")){//组装
			if(status==3) {
				List<QuoteItem> lqii = quoteItemDao.getStatusByPackag(quoteId);
				if (lqii.size() == 0) {
					//2.2 全部完成审批
					if (lo.size() > 0) {
						Quote o = lo.get(0);
						o.setBsStatus2Packag(status);
						o.setLastupdateDate(new Date());
						o.setLastupdateBy(UserUtil.getSessionUser().getId());
						quoteDao.save(o);
					}
				}
			}else {
				if (lo.size() > 0) {
					Quote o = lo.get(0);
					o.setBsStatus2Packag(status);
					o.setLastupdateDate(new Date());
					o.setLastupdateBy(UserUtil.getSessionUser().getId());
					quoteDao.save(o);
				}
			}
		}
		return ApiResponseResult.success();
	}

}
