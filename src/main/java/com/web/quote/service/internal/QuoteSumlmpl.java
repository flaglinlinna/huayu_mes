package com.web.quote.service.internal;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.web.basic.dao.SysParamDao;
import com.web.basic.entity.SysParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseSql;
import com.utils.UserUtil;
import com.web.basePrice.dao.ProdTypDao;
import com.web.basePrice.entity.ProdTyp;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.dao.ProductProcessDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteMouldDao;
import com.web.quote.dao.QuoteSumBomDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.ProductProcess;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteMould;
import com.web.quote.entity.QuoteSumBom;
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
	@Autowired
	private QuoteSumBomDao quoteSumBomDao;
	@Autowired
	private ProdTypDao prodTypDao;
	@Autowired
	private SysParamDao sysParamDao;

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String quoteId, String keyword, String bsStatus, String bsCode, String bsType,
									 String bsFinishTime, String bsRemarks, String bsProd, String bsProdType, String bsSimilarProd,
									 String bsPosition, String bsCustRequire, String bsLevel, String bsRequire, String bsDevType,
									 String bsCustName, PageRequest pageRequest) throws Exception {
		String statusTemp = "";
		if (StringUtils.isNotEmpty(bsStatus)) {
			statusTemp = "and decode(p.bs_end_time3,null,'1','2') = " + bsStatus;
		}
		String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
				+ "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,decode(p.bs_end_time3,null,'1','2') col ,p.bs_position,"
				+ "p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level,"
				+ "p.bs_Cust_Require,p.bs_Bade,p.bs_proj_ver,p.bs_latest,p.bs_stage from " + Quote.TABLE_NAME + " p " + " where p.del_flag=0 and p.bs_step>2 "
				+ statusTemp;
		if (StringUtils.isNotEmpty(quoteId) && !("null").equals(quoteId)) {
			sql += "and p.id = " + quoteId + "";
		}
		// if(!StringUtils.isEmpty(status)){
		// sql += " and p.bs_Status = " + status + "";
		// }
		if (StringUtils.isNotEmpty(bsType)) {
			sql += "  and p.bs_Type like '%" + bsType + "%'";
		}
		if (StringUtils.isNotEmpty(bsCode)) {
			sql += "  and p.bs_Code like '%" + bsCode + "%'";
		}
		if (StringUtils.isNotEmpty(bsFinishTime)) {
			String[] dates = bsFinishTime.split(" - ");
			sql += " and to_date(p.bs_Finish_Time,'yyyy-MM-dd') >= to_date('" + dates[0] + "','yyyy-MM-dd')";
			sql += " and to_date(p.bs_Finish_Time,'yyyy-MM-dd') <= to_date('" + dates[1] + "','yyyy-MM-dd')";
		}
		if (StringUtils.isNotEmpty(bsRemarks)) {
			sql += "  and p.bs_Remarks like '%" + bsRemarks + "%'";
		}
		if (StringUtils.isNotEmpty(bsProd)) {
			sql += "  and p.bs_Prod like '%" + bsProd + "%'";
		}
		if (StringUtils.isNotEmpty(bsProdType)) {
			sql += "  and p.bs_Prod_Type like '%" + bsProdType + "%'";
		}
		if (StringUtils.isNotEmpty(bsSimilarProd)) {
			sql += "  and p.bs_Similar_Prod like '%" + bsSimilarProd + "%'";
		}
		if (StringUtils.isNotEmpty(bsPosition)) {
			sql += "  and p.bs_position like '%" + bsPosition + "%'";
		}
		if (StringUtils.isNotEmpty(bsCustRequire)) {
			sql += "  and p.bs_Cust_Require like '%" + bsCustRequire + "%'";
		}
		if (StringUtils.isNotEmpty(bsLevel)) {
			sql += "  and p.bs_Level like '" + bsLevel + "%'";
		}
		if (StringUtils.isNotEmpty(bsRequire)) {
			sql += "  and p.bs_Require like '%" + bsRequire + "%'";
		}
		if (StringUtils.isNoneEmpty(bsDevType)) {
			sql += "  and p.bs_Dev_Type like '%" + bsDevType + "%'";
		}
		if (StringUtils.isNotEmpty(bsCustName)) {
			sql += "  and p.bs_Cust_Name like '%" + bsCustName + "%'";
		}
		if (StringUtils.isNotEmpty(keyword)) {
			sql += "  and INSTR((p.bs_Code || p.bs_Prod ||p.bs_Similar_Prod ||p.bs_Remarks ||p.bs_Cust_Name"
					+ "||p.bs_Dev_Type ||p.bs_Cust_Require || p.bs_position || p.bs_Require ||p.bs_Level ||p.bs_Dev_Type), '"
					+ keyword + "') > 0 ";
		}
		sql += "  order by p.bs_code desc";
		int pn = pageRequest.getPageNumber() + 1;
		String sql_page = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM ( " + sql + " ) A  WHERE ROWNUM <= (" + pn
				+ ")*" + pageRequest.getPageSize() + "  )  WHERE RN > (" + pageRequest.getPageNumber() + ")*"
				+ pageRequest.getPageSize() + " ";

		Map<String, Object> param = new HashMap<String, Object>();

		List<Object[]> list = createSQLQuery(sql_page, param);
		long count = createSQLQuery(sql, param, null).size();

		List<Map<String, Object>> list_new = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Object[] object = (Object[]) list.get(i);
			Map<String, Object> map1 = new HashMap<>();
			map1.put("id", object[0]);
			map1.put("bsCode", object[1]);
			map1.put("bsType", object[2]);
			// map1.put("bsStatus", object[3]);
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

			map1.put("bsBade", object[21]);
			map1.put("bsProjVer",object[22]);
			map1.put("bsLatest",object[23]);
			map1.put("bsStage",object[24]);

			list_new.add(map1);
		}
		HashMap map = new HashMap();
		map.put("List",
				DataGrid.create(list_new, (int) count, pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		map.put("Nums", quoteDao.getNumBySumAndBsStep());

		return ApiResponseResult.success().data(map);
	}

	@Override
	public ApiResponseResult getQuoteList(String keyword, String quoteId, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub

		String hql = "select p.* from " + ProductMater.TABLE_NAME + " p where p.del_flag=0 and p.pk_quote=" + quoteId;

		int pn = pageRequest.getPageNumber() + 1;
		String sql = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM ( " + hql + " ) A  WHERE ROWNUM <= (" + pn + ")*"
				+ pageRequest.getPageSize() + "  )  WHERE RN > (" + pageRequest.getPageNumber() + ")*"
				+ pageRequest.getPageSize() + " ";

		Map<String, Object> param = new HashMap<String, Object>();

		// List<Map<String, Object>> list = super.findBySql(sql, param);
		List<ProductMater> list = createSQLQuery(sql, param, ProductMater.class);
		long count = createSQLQuery(hql, param, null).size();

		return ApiResponseResult.success()
				.data(DataGrid.create(list, (int) count, pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	@Override
	public ApiResponseResult getSumByQuote(String quoteId) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(quoteId)) {
			return ApiResponseResult.failure("报价单ID不能为空");
		}
		Map map = new HashMap();
		Quote quote = quoteDao.findById(Long.parseLong(quoteId));
		map.put("Quote", quote);// 报价基础信息

		// 1:材料价格汇总
		// BigDecimal cl_all = new BigDecimal(0);//材料总价格
		BigDecimal cl_hardware = new BigDecimal(0);// 五金
		BigDecimal cl_molding = new BigDecimal(0);// 注塑
		BigDecimal cl_surface = new BigDecimal(0);// 表面处理
		BigDecimal cl_packag = new BigDecimal(0);// 组装

		List<ProductMater> lpm = productMaterDao.findByDelFlagAndPkQuote(0, Long.valueOf(quoteId));
		for (ProductMater pm : lpm) {
			if (pm.getBsType().equals("hardware")) {
				cl_hardware = cl_hardware.add(pm.getBsFee());
			} else if (pm.getBsType().equals("molding")) {
				cl_molding = cl_molding.add(pm.getBsFee());
			} else if (pm.getBsType().equals("surface")) {
				cl_surface = cl_surface.add(pm.getBsFee());
			} else if (pm.getBsType().equals("packag")) {
				cl_packag = cl_packag.add(pm.getBsFee());
			}
		}

		// 2:人工成本+制造费用+外协工艺成本
		BigDecimal lh_hardware = new BigDecimal(0);// 五金
		BigDecimal lh_molding = new BigDecimal(0);// 注塑
		BigDecimal lh_surface = new BigDecimal(0);// 表面处理
		BigDecimal lh_packag = new BigDecimal(0);// 组装
		BigDecimal lw_hardware = new BigDecimal(0);// 五金
		BigDecimal lw_molding = new BigDecimal(0);// 注塑
		BigDecimal lw_surface = new BigDecimal(0);// 表面处理
		BigDecimal lw_packag = new BigDecimal(0);// 组装

		BigDecimal wx_all = new BigDecimal(0);// 外协工艺成本?

		BigDecimal lh_the_loss = new BigDecimal(0);// 五金+注塑+表面处理+组装+外协-人工-本工序损耗
		BigDecimal wh_the_loss = new BigDecimal(0);// 五金+注塑+表面处理+组装+外协-制费-本工序损耗
		BigDecimal lh_hou_loss = new BigDecimal(0);// 五金+注塑+表面处理+组装+外协-人工-后工序损耗
		BigDecimal wh_hou_loss = new BigDecimal(0);// 五金+注塑+表面处理+组装+外协-制费-后工序损耗

		BigDecimal all_lose = new BigDecimal(0);
		BigDecimal the_lose = new BigDecimal(0);

		List<ProductProcess> lpp = productProcessDao.findByDelFlagAndPkQuote(0, Long.valueOf(quoteId));
		for (ProductProcess pp : lpp) {
			if (pp.getBsType().equals("hardware")) {
				lh_hardware = lh_hardware.add(pp.getBsFeeLhAll());
				lw_hardware = lw_hardware.add(pp.getBsFeeMhAll());
			} else if (pp.getBsType().equals("molding")) {
				lh_molding = lh_molding.add(pp.getBsFeeLhAll());
				lw_molding = lw_molding.add(pp.getBsFeeMhAll());
			} else if (pp.getBsType().equals("surface")) {
				lh_surface = lh_surface.add(pp.getBsFeeLhAll());
				lw_surface = lw_surface.add(pp.getBsFeeMhAll());
			} else if (pp.getBsType().equals("packag")) {
				lh_packag = lh_packag.add(pp.getBsFeeLhAll());
				lw_packag = lw_packag.add(pp.getBsFeeMhAll());
			} else if (pp.getBsType().equals("out")) {
				wx_all = wx_all.add(pp.getBsFeeWxAll());
			}

//			lh_the_loss = lh_the_loss.add(pp.getBsLossTheLh());
//			wh_the_loss = wh_the_loss.add(pp.getBsLossTheMh());
//			lh_hou_loss = lh_hou_loss.add(pp.getBsLossHouLh()); //后工序损耗(人工)
//			wh_hou_loss = wh_hou_loss.add(pp.getBsLossHouMh()); //后工序损耗(制费)

			//20210318-hjj工序损耗
			all_lose = all_lose.add(pp.getBsTheLoss()==null?the_lose:pp.getBsTheLoss());

		}
		// 3：小计
		BigDecimal hardware_all = cl_hardware.add(lh_hardware).add(lw_hardware);// 五金小计
		BigDecimal molding_all = cl_molding.add(lh_molding).add(lw_molding);// 注塑小计
		BigDecimal surface_all = cl_surface.add(lh_surface).add(lw_surface);// 表面处理小计
		BigDecimal packag_all = cl_packag.add(lh_packag).add(lw_packag);// 组装小计

		BigDecimal hou_loss_all = lh_hou_loss.add(wh_hou_loss);// 后工序损料

		// 4.模具费用
		List<QuoteMould> lqm = quoteMouldDao.findByDelFlagAndPkQuote(0, Long.valueOf(quoteId));
		BigDecimal mould_all = new BigDecimal(0);
		for (QuoteMould qm : lqm) {
			mould_all = mould_all.add(qm.getBsActQuote());// 实际报价
		}

		// 5.生产成本=五金小计+注塑小计+表面处理小计+组装小计+后工序损料
		BigDecimal p_cb = hardware_all.add(molding_all).add(surface_all).add(packag_all).add(wx_all).add(hou_loss_all);

		// 6.生产管理费-管理费用的计算=管理费率*产品生产成本
		BigDecimal gl = quote.getBsManageFee().multiply(p_cb).divide(new BigDecimal(100), 5, 5);

		// 7.利润

		// 净利润PROFIT_NET：手工维护录入。 (注意：修改净利润后其他数据需联动变化)
		BigDecimal profitNet = quote.getBsProfitNet();
		if (profitNet != null) {
			// 系统报价：生产成本FEE_PROD_NET+管理费用FEE_MANAGE+净利润PROFIT_NET
			BigDecimal bj_all = p_cb.add(gl).add(profitNet);
			// 毛利：管理费用FEE_MANAGE+净利润PROFIT_NET
			BigDecimal ml = gl.add(profitNet);
			// 毛利率：毛利/系统报价
			BigDecimal ml_rate = ml.multiply(new BigDecimal("100")).divide(bj_all, 5, 5);
			// 净利率：净利润/系统报价
			BigDecimal profit_gs = profitNet.multiply(new BigDecimal("100")).divide(bj_all, 5, 5);

			map.put("bj_all", bj_all); // 系统报价
			map.put("ml", ml); // 毛利
			map.put("ml_rate", ml_rate + "%"); // 毛利率
			map.put("profit_gs", profit_gs + "%"); // 净利率

		}

		map.put("cl_hardware", cl_hardware);// 五金材料
		map.put("cl_molding", cl_molding);// 注塑材料
		map.put("cl_surface", cl_surface);// 表面处理材料
		map.put("cl_packag", cl_packag);// 组装材料

		map.put("lh_hardware", lh_hardware);// 五金人工
		map.put("lh_molding", lh_molding);// 注塑人工
		map.put("lh_surface", lh_surface);// 表面处理人工
		map.put("lh_packag", lh_packag);// 组装人工

		map.put("lw_hardware", lw_hardware);// 五金制费
		map.put("lw_molding", lw_molding);// 注塑制费
		map.put("lw_surface", lw_surface);// 表面处理制费
		map.put("lw_packag", lw_packag);// 组装制费

		map.put("hardware_all", hardware_all);// 五金小计
		map.put("molding_all", molding_all);// 注塑小计
		map.put("surface_all", surface_all);// 表面处理小计
		map.put("packag_all", packag_all);// 组装小计

		map.put("wx_all", wx_all);// 外协加工

		map.put("hou_loss_all", all_lose);// 后工序损料

		map.put("mould_all", mould_all);// 模具费用

		map.put("gl", gl);// 管理费用
		map.put("p_cb", p_cb); // 生产成本
		map.put("profitNet", profitNet); // 净利润

		// map.put("", quote.getBsProfitProd());//保底毛利率

		return ApiResponseResult.success().data(map);
	}

	/**
	 * 第二步全部审批通过后，计算物料的价格，工序的人工费和制费
	 * 五金计算公式修改成和注塑一样 20210225-hjj
	 * 材料关联工序，计算损耗
	 */
	@Override
	public ApiResponseResult countMeterAndProcess(String quoteId) throws Exception {
		// TODO Auto-generated method stub
		List<SysParam> sysParams = sysParamDao.findByDelFlagAndParamCode(0, "BJ_YIELD");
		BigDecimal bsYield =  sysParams.size()>0?new BigDecimal(sysParams.get(0).getParamValue()):new BigDecimal("100");
		// 表面，组装的材料总价格(未税)计算公式-单价*用量/基数 20210225去除了五金
		List<ProductMater> lpm3 = productMaterDao.findByDelFlagAnd3Tyle(Long.valueOf(quoteId));
		for (ProductMater pm : lpm3) {
//			BigDecimal bsRadix = new BigDecimal("1");// 基数
//			if (!StringUtils.isEmpty(pm.getBsRadix())) {
//				if (!"1".equals(pm.getBsRadix())) {
//					bsRadix = new BigDecimal(pm.getBsRadix());
//				}
//			}
			BigDecimal bsAssess = new BigDecimal("0");// 采购价
			if (pm.getBsAssess() != null) {
				bsAssess = pm.getBsAssess();
			}
			List<ProductProcess> processList = productProcessDao.findByBsNameAndBsElementAndPkQuoteAndBsTypeAndDelFlagAndBsMaterNameOrderByBsOrderDesc(
					pm.getBsComponent(),pm.getBsElement(),pm.getPkQuote(),pm.getBsType(),0,pm.getBsMaterName());
			pm.setBsYield(processList.size()>0?processList.get(0).getBsYield():bsYield);

			pm.setBsFee(bsAssess.multiply(pm.getBsQty()));
			pm.setBsFee((pm.getBsFee().multiply(new BigDecimal("100"))).divide(pm.getBsYield(),5,5));
			if(("surface").equals(pm.getBsType())){
				pm.setBsFee(pm.getBsFee().divide(new BigDecimal("1000"),5,5));
			}
		}
		productMaterDao.saveAll(lpm3);
		// 注塑的材料总价格(未税)计算公式-材料单价*(制品重(g)+水口重/穴数)/基数  20210225增加了五金
		List<ProductMater> lpm1 = productMaterDao.findByDelFlagAndMolding(Long.valueOf(quoteId));
		for (ProductMater pm : lpm1) {
			BigDecimal bsRadix = new BigDecimal("1");// 基数
			if (!StringUtils.isEmpty(pm.getBsRadix())) {
				if (!"1".equals(pm.getBsRadix())) {
					bsRadix = new BigDecimal(pm.getBsRadix());
				}
			}
			BigDecimal bsCave = new BigDecimal(pm.getBsCave());// 穴数

			BigDecimal bsWaterGap = new BigDecimal("1");// 水口量
			if (pm.getBsWaterGap() != null) {
				bsWaterGap = new BigDecimal(pm.getBsWaterGap());
			}
			BigDecimal qty = bsWaterGap.divide(bsCave, 5, 5).add(pm.getBsProQty());// 水口重/穴数+制品重(g)
			BigDecimal bsAssess = new BigDecimal("0");// 采购价
			if (pm.getBsAssess() != null) {
				bsAssess = pm.getBsAssess();
			}
			List<ProductProcess> processList = productProcessDao.findByBsNameAndBsElementAndPkQuoteAndBsTypeAndDelFlagAndBsMaterNameOrderByBsOrderDesc(
					pm.getBsComponent(),pm.getBsElement(),pm.getPkQuote(),pm.getBsType(),0,pm.getBsMaterName());
			pm.setBsYield(processList.size()>0?processList.get(0).getBsYield():bsYield);
			pm.setBsFee(bsAssess.multiply(qty).divide(bsRadix, 5, 5));
			pm.setBsFee((pm.getBsFee().multiply(new BigDecimal("100"))).divide(pm.getBsYield(),5,5).divide(new BigDecimal("1000"),5,5));
		}
		productMaterDao.saveAll(lpm1);
		// 五金-人工工时费（元/H）*人数*成型周期(S）/3600 /基数；制费工时费（元/H）*成型周期(S）/3600/ /基数
		List<ProductProcess> lpp_hardware = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, Long.valueOf(quoteId),
				"hardware");
		for (ProductProcess pp : lpp_hardware) {
			// System.out.println(pp.getId());
			BigDecimal bsRadix = new BigDecimal("1");// 基数
			if (pp.getBsRadix() != null) {
				if (!"1".equals(pp.getBsRadix())) {
					bsRadix = pp.getBsRadix();
				}
			}
			pp.setBsFeeLhAll(pp.getBsFeeLh().multiply(pp.getBsUserNum()).multiply(pp.getBsCycle())
					.divide(new BigDecimal("3600"), 5, 5).divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));

			pp.setBsFeeMhAll(pp.getBsFeeMh().multiply(pp.getBsCycle()).divide(new BigDecimal("3600"), 5, 5)
					.divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));

			// 本工序损耗
//			pp.setBsLossTheLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossTheMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
			// 后工序损耗
//			pp.setBsLossHouLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossHouMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
		}
		productProcessDao.saveAll(lpp_hardware);
		// 注塑-人工工时费（元/H）*人数*成型周期(S）/3600/ 穴数/基数;制费工时费（元/H）*成型周期(S）/3600/穴数/ 基数
		List<ProductProcess> lpp_molding = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, Long.valueOf(quoteId),
				"molding");
		for (ProductProcess pp : lpp_molding) {
			BigDecimal bsRadix = new BigDecimal("1");// 基数
			if (pp.getBsRadix() != null) {
				if (!"1".equals(pp.getBsRadix())) {
					bsRadix = pp.getBsRadix();
				}
			}
			BigDecimal bsCave = new BigDecimal(pp.getBsCave());// 穴数
			pp.setBsFeeLhAll(pp.getBsFeeLh().multiply(pp.getBsUserNum()).multiply(pp.getBsCycle())
					.divide(new BigDecimal("3600"), 5, 5).divide(bsCave, 5, 5).divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));

			pp.setBsFeeMhAll(pp.getBsFeeMh().multiply(pp.getBsCycle()).divide(new BigDecimal("3600"), 5, 5)
					.divide(bsCave, 5, 5).divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));
			// 本工序损耗
//			pp.setBsLossTheLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossTheMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
			// 后工序损耗
//			pp.setBsLossHouLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossHouMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
		}
		productProcessDao.saveAll(lpp_molding);
		// 更新表面处理-人数*费率/产能/基数;费率/产能/基数
		List<ProductProcess> lpp_surface = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, Long.valueOf(quoteId),
				"surface");
		for (ProductProcess pp : lpp_surface) {
			BigDecimal bsRadix = new BigDecimal("1");// 基数
			if (pp.getBsRadix() != null) {
				if (!"1".equals(pp.getBsRadix())) {
					bsRadix = pp.getBsRadix();
				}
			}
			BigDecimal bsCapacity = new BigDecimal(pp.getBsCapacity());// 产能
			pp.setBsFeeLhAll(
					pp.getBsFeeLh().multiply(pp.getBsUserNum()).divide(bsCapacity, 5, 5).divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));

			pp.setBsFeeMhAll(pp.getBsFeeMh().divide(bsCapacity, 5, 5).divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));
			// 本工序损耗
//			pp.setBsLossTheLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossTheMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
			// 后工序损耗
//			pp.setBsLossHouLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossHouMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
		}
		productProcessDao.saveAll(lpp_surface);
		// 组装工艺成本-人数*费率/产能/基数;费率/产能/基数
		List<ProductProcess> lpp_packag = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, Long.valueOf(quoteId),
				"packag");
		for (ProductProcess pp : lpp_packag) {
			BigDecimal bsRadix = new BigDecimal("1");// 基数
			if (pp.getBsRadix() != null) {
				if (!"1".equals(pp.getBsRadix())) {
					bsRadix = pp.getBsRadix();
				}
			}
			BigDecimal bsCapacity = new BigDecimal(pp.getBsCapacity());// 产能
			pp.setBsFeeLhAll(
					pp.getBsFeeLh().multiply(pp.getBsUserNum()).divide(bsCapacity, 5, 5).divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));

			pp.setBsFeeMhAll(pp.getBsFeeMh().divide(bsCapacity, 5, 5).divide(bsRadix, 5, 5).multiply(new BigDecimal("100")).divide(pp.getBsYield(),5,5));
			// 本工序损耗
//			pp.setBsLossTheLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossTheMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
			// 后工序损耗
//			pp.setBsLossHouLh(pp.getBsFeeLhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeLhAll()));
//			pp.setBsLossHouMh(pp.getBsFeeMhAll().multiply(new BigDecimal("100")).divide(pp.getBsHouYield(), 5, 5)
//					.subtract(pp.getBsFeeMhAll()));
		}
		productProcessDao.saveAll(lpp_packag);

		// 外协-计算损耗
		List<ProductProcess> lpp_out = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, Long.valueOf(quoteId),
				"out");
		for (ProductProcess pp : lpp_out) {
			// 本工序损耗
			pp.setBsLossTheLh(pp.getBsFeeWxAll().multiply(new BigDecimal("100")).subtract(pp.getBsLoss()));
			pp.setBsLossTheMh(new BigDecimal("0"));
			// 后工序损耗
//			pp.setBsLossHouLh(new BigDecimal("0"));
//			pp.setBsLossHouMh(new BigDecimal("0"));
		}
		productProcessDao.saveAll(lpp_out);
		List<ProductProcess> processAllList = productProcessDao.findByDelFlagAndPkQuoteOrderByBsElementDescBsLinkNameDescBsOrderDesc(0,Long.parseLong(quoteId));
		List<ProductProcess> processList = new ArrayList<>();
		HashSet<String> groupSet = new HashSet<>();
		for(ProductProcess o :processAllList){
			if(StringUtils.isNotEmpty(o.getBsGroups())){
				//根据分组和零件名称和组件判断是否加入损耗计算
				if(groupSet.add(o.getBsGroups()+o.getBsLinkName()+o.getBsElement())){
					processList.add(o);
				}
			}else {
				processList.add(o);
			}
		}
//		processList = processList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProductProcess::getBsGroups))), ArrayList::new));
		Collections.reverse(processList);
		for(Integer i=0;i<processList.size();i++){
			ProductProcess o = processList.get(i);
			//成本 = 人工制费 + 制造费用 + 材料费用
			// 外协损耗改成工序良率计算
			if(o.getBsType().equals("out")){
				o.setBsCost(o.getBsFeeWxAll());
//				o.setBsYield(new BigDecimal("100").subtract(o.getBsLoss()));
				o.setBsYield(o.getBsLoss());
			}else {
				List<ProductMater> pmList = new ArrayList<>();
				if(StringUtils.isNotEmpty(o.getBsGroups())){
					pmList= productMaterDao.findByPkQuoteAndDelFlagAndBsGroups(o.getPkQuote(),0,o.getBsGroups());
				}else {
//					pmList = productMaterDao.findByBsElementAndBsComponentAndPkQuoteAndBsTypeAndDelFlag(
//							o.getBsElement(), o.getBsName(), o.getPkQuote(), o.getBsType(), 0);
					pmList = productMaterDao.findByPkQuoteAndDelFlagAndBsMaterName(o.getPkQuote(),0,o.getBsMaterName());
				}
				BigDecimal materCost = BigDecimal.ZERO;
				for(ProductMater pm:pmList){
					materCost = materCost.add(pm.getBsFee());
				}
				o.setBsMaterCost(materCost);
				o.setBsCost(o.getBsFeeLhAll().add(o.getBsFeeMhAll()).add(o.getBsMaterCost()));
			}
			//第一条不用加上成本累计(含损耗) //2021-5-18-hjj 各小计分组的损耗独立计算
			if(i==0||!(o.getBsGroups()+o.getBsLinkName()+o.getBsElement()).equals(processList.get(i-1).getBsGroups()+processList.get(i-1).getBsLinkName()+processList.get(i-1).getBsElement())) {
				//本工序损耗
				o.setBsTheLoss(o.getBsCost().divide(o.getBsYield(),5,5).multiply(new BigDecimal("100")).subtract(o.getBsCost()));
				//成本累计(含损耗)
				o.setBsAllLoss(o.getBsCost().add(o.getBsTheLoss()));
			}else {
				//本工序损耗
				o.setBsTheLoss((processList.get(i-1).getBsAllLoss()).divide(o.getBsYield(),5,5).multiply(new BigDecimal("100")).subtract((processList.get(i-1).getBsAllLoss())));
				//成本累计(含损耗)
				o.setBsAllLoss(o.getBsCost().add(o.getBsTheLoss()).add(processList.get(i-1).getBsAllLoss()));
			}
//			processList.add(o);
		}
		productProcessDao.saveAll(processList);
		return ApiResponseResult.success();
	}

	@Override
	public ApiResponseResult updateProfitNet(long quoteId, BigDecimal profitNet) throws Exception {
		Quote o = quoteDao.findById(quoteId);
		if (o == null) {
			return ApiResponseResult.failure("没有这个报价单");
		} else {
			o.setBsProfitNet(profitNet);
			quoteDao.save(o);
		}
		return ApiResponseResult.success("修改净利润成功!");
	}

	@Override
	public ApiResponseResult updateBsManageFee(long quoteId, BigDecimal bsManageFee) throws Exception {
		Quote o = quoteDao.findById(quoteId);
		if (o == null) {
			return ApiResponseResult.failure("没有这个报价单");
		} else {
			o.setBsManageFee(bsManageFee);
			quoteDao.save(o);
		}
		return ApiResponseResult.success("修改管理费率成功!");
	}

	@Override
	public ApiResponseResult getQuoteBomByQuote(String quoteId) throws Exception {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(quoteId)) {
			return ApiResponseResult.failure("报价单号为空!");
		}
		// 生产树形表格
		/*
		 * ApiResponseResult ar = this.countQuoteTreeBom(Long.valueOf(quoteId));
		 * if(ar.isResult()){ return
		 * ApiResponseResult.success().data(quoteSumBomDao.
		 * findByDelFlagAndPkQuote(0, Long.valueOf(quoteId))); }else{ return ar;
		 * }
		 */
		return ApiResponseResult.success().data(quoteSumBomDao.findByDelFlagAndPkQuote(0, Long.valueOf(quoteId)));
	}

	@Override
	public ApiResponseResult countQuoteTreeBom(Long quoteId) throws Exception {
		// 获取产品类型
		String prod_type = "";
		Quote quote = quoteDao.findById((long) quoteId);
		if (quote != null) {
			if (quote.getBsProdTypeId() != null) {
				ProdTyp pt = prodTypDao.findById((long) quote.getBsProdTypeId());
				if (pt != null) {
					prod_type = pt.getProductType();
				}
			}

		}

		List<Map<String, Object>> lm1 = productMaterDao.getBomFirt(quoteId);
		if (lm1.size() > 0) {
			List<QuoteSumBom> lqb1 = new ArrayList<QuoteSumBom>();
			for (Map<String, Object> map1 : lm1) {
				QuoteSumBom qb1 = new QuoteSumBom();
				qb1.setPkQuote(quoteId);
				qb1.setBsElement(map1.get("ELEMENT").toString());
				qb1.setBsMaterName(map1.get("ELEMENT").toString());
				qb1.setBsFeeItemAll(new BigDecimal(map1.get("FEE").toString()));// 材料总费用
				qb1.setBsFeeLhAll(new BigDecimal(map1.get("FEE_LH").toString()));
				qb1.setBsFeeMhAll(new BigDecimal(map1.get("FEE_MH").toString()));
				qb1.setBsFeeOut(new BigDecimal(map1.get("FEE_WX").toString()));
				qb1.setBsFeeAll(
						qb1.getBsFeeItemAll().add(qb1.getBsFeeLhAll()).add(qb1.getBsFeeMhAll()).add(qb1.getBsFeeOut()));
				qb1.setParenId(new Long((long) 0));
				qb1.setCreateDate(new Date());
				qb1.setBsProdType(prod_type);
				lqb1.add(qb1);
			}
			quoteSumBomDao.saveAll(lqb1);
			// 填写虚拟编号
			for (QuoteSumBom qsb : lqb1) {
				qsb.setBsItemCode("XN" + qsb.getId());
				quoteSumBomDao.save(qsb);

				// --第二层
				List<Map<String, Object>> lm2 = productMaterDao.getBomSecond(quoteId, qsb.getBsElement());
				if (lm2.size() > 0) {
					List<QuoteSumBom> lqb2 = new ArrayList<QuoteSumBom>();
					for (Map<String, Object> map2 : lm2) {
						QuoteSumBom qb2 = new QuoteSumBom();
						qb2.setPkQuote(quoteId);
						qb2.setCreateDate(new Date());
						qb2.setBsMaterName(map2.get("COMPONENT").toString());
						qb2.setParenId(qsb.getId());
						qb2.setBsComponent(map2.get("COMPONENT").toString());
						qb2.setBsFeeItemAll(new BigDecimal(map2.get("FEE")==null?"0":map2.get("FEE").toString()));// 材料总费用
						qb2.setBsFeeLhAll(new BigDecimal(map2.get("FEE_LH").toString()));
						qb2.setBsFeeMhAll(new BigDecimal(map2.get("FEE_MH").toString()));
						qb2.setBsFeeOut(new BigDecimal(map2.get("FEE_WX").toString()));
						qb2.setBsFeeAll(qb2.getBsFeeItemAll().add(qb2.getBsFeeLhAll()).add(qb2.getBsFeeMhAll())
								.add(qb2.getBsFeeOut()));
						qb2.setBsProdType(prod_type);

						qb2 = quoteSumBomDao.save(qb2);

						// 填写虚拟编号
						qb2.setBsItemCode("XN" + qb2.getId());
						quoteSumBomDao.save(qb2);

						// 填写第三层
						List<Map<String, Object>> lm3 = productMaterDao.getBomThree(quoteId, qsb.getBsElement(),
								qb2.getBsComponent());
						if (lm3.size() > 0) {
							for (Map<String, Object> map3 : lm3) {
								QuoteSumBom qb3 = new QuoteSumBom();
								qb3.setPkQuote(quoteId);
								qb3.setCreateDate(new Date());
								qb3.setBsMaterName(map3.get("MATER_NAME")==null?"null":map3.get("MATER_NAME").toString());
								qb3.setBsFeeItemAll(new BigDecimal(map3.get("FEE").toString()));// 材料总费用
								qb3.setPkBjWorkCenter(Long.valueOf(map3.get("WKC").toString()));
								qb3.setBsProdType(prod_type);
								String pkUnit = map3.get("PUNIT") == null ? "" : map3.get("PUNIT").toString();
								if (!StringUtils.isEmpty(pkUnit)) {
									qb3.setPkUnit(Long.valueOf(pkUnit));
								}
								qb3.setParenId(qb2.getId());

								qb3.setBsFeeAll(qb3.getBsFeeItemAll());

								qb3 = quoteSumBomDao.save(qb3);

								// 填写虚拟编号
								qb3.setBsItemCode("XN" + qb3.getId());
								quoteSumBomDao.save(qb3);
							}
						}
					}

				}
			}
		}
		return ApiResponseResult.success();
	}

	/**
	 * 设置中标-lst 
	 * 2021-01-23
	 **/
	public ApiResponseResult setBade(Long quoteId,Integer bsBade) throws Exception {
		if (quoteId == null) {
			return ApiResponseResult.failure("报价单为空");
		}
		Quote o = quoteDao.findById((long) quoteId);
		if (o == null) {
			return ApiResponseResult.failure("该报价单不存在！");
		}
//		o.setBsBade(1);//是否中标(0:否 /1:是)
		o.setBsBade(bsBade);
		o.setLastupdateDate(new Date());//修改人
		o.setLastupdateBy(UserUtil.getSessionUser().getId());//修改时间
		quoteDao.save(o);
		return ApiResponseResult.success("设置中标成功！");
	}

	public ApiResponseResult getSumList(Long quoteId,PageRequest pageRequest) throws Exception {
		Page<Map<String, Object>> mapList= productProcessDao.getSumList(quoteId,pageRequest);
//		DataGrid.create(mapList.getContent(), (int) mapList.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize())
		List<Map<String,Object>> maps =  new ArrayList<>();
		List<Map<String,Object>> mapOld = mapList.getContent();
		HashSet<String> groupSet = new HashSet<>();
		for (int i = mapOld.size()-1; i >= 0; i--) {
		  Map<String, Object> subtotal = new HashMap<>();
		  Map<String, Object> one = mapOld.get(i);
		  Map<String, Object> deepCopy = new HashMap<>();
		  //根据组件和所属零件判断是否有小结
		  if(groupSet.add(one.get("BS_ELEMENT").toString()+one.get("BS_LINK_NAME")+one.get("BS_ORDERS"))){
		  	//新增小结
		  	subtotal.put("BS_ELEMENT",one.get("BS_ELEMENT"));
		  	subtotal.put("BS_LINK_NAME",one.get("BS_LINK_NAME"));
//			  subtotal.put("BS_ORDERS",one.get("BS_ORDERS"));
			maps.add(subtotal);
		  }
		  deepCopy.putAll(one);
		  maps.add(deepCopy);
		  //根据组件和所属零件判断是否新增小计计算
		}
		Collections.reverse(maps);
		for(Map<String,Object> map :maps){
			//分组统计
			if(map.get("BS_GROUPS")!=null){
				List<Map<String,Object>> sumList = productProcessDao.getSumByBsGroups(quoteId,map.get("BS_GROUPS").toString()
						,map.get("BS_ELEMENT").toString(),map.get("BS_LINK_NAME").toString());
//				map.put("BS_MATER_COST",sumList.get(0).get("BS_MATER_COST"));
				map.put("BS_FEE_LH_ALL",sumList.get(0).get("BS_FEE_LH_ALL"));
				map.put("BS_FEE_MH_ALL",sumList.get(0).get("BS_FEE_MH_ALL"));
				map.put("BS_FEE_WX_ALL",sumList.get(0).get("BS_FEE_WX_ALL"));
			}
			else if(map.get("BS_ORDER")==null){
				//根据所属零件统计
				BigDecimal BS_MATER_COST = BigDecimal.ZERO;
				BigDecimal BS_FEE_LH_ALL = BigDecimal.ZERO;
				BigDecimal BS_FEE_MH_ALL = BigDecimal.ZERO;
				BigDecimal BS_FEE_WX_ALL = BigDecimal.ZERO;
				BigDecimal BS_COST = BigDecimal.ZERO;
				BigDecimal BS_YIELD = new BigDecimal(100);
				BigDecimal BS_THE_LOSS = BigDecimal.ZERO;
//				BigDecimal bsCostLoss = BigDecimal.ZERO;
				for(Map<String,Object> map2 :maps){
					if(map2.get("BS_ELEMENT").equals(map.get("BS_ELEMENT"))&&map2.get("BS_LINK_NAME").equals(map.get("BS_LINK_NAME"))){
						BS_MATER_COST = BS_MATER_COST.add(map2.get("BS_MATER_COST")==null?BigDecimal.ZERO:new BigDecimal(map2.get("BS_MATER_COST").toString()));
						BS_FEE_LH_ALL = BS_FEE_LH_ALL.add(map2.get("BS_FEE_LH_ALL")==null?BigDecimal.ZERO:new BigDecimal(map2.get("BS_FEE_LH_ALL").toString()));
						BS_FEE_MH_ALL = BS_FEE_MH_ALL.add(map2.get("BS_FEE_MH_ALL")==null?BigDecimal.ZERO:new BigDecimal(map2.get("BS_FEE_MH_ALL").toString()));
						BS_FEE_WX_ALL = BS_FEE_WX_ALL.add(map2.get("BS_FEE_WX_ALL")==null?BigDecimal.ZERO:new BigDecimal(map2.get("BS_FEE_WX_ALL").toString()));
						BS_COST = BS_COST.add(map2.get("BS_COST")==null?BigDecimal.ZERO:new BigDecimal(map2.get("BS_COST").toString()));
						BS_THE_LOSS = BS_THE_LOSS.add(map2.get("BS_THE_LOSS")==null?BigDecimal.ZERO:new BigDecimal(map2.get("BS_THE_LOSS").toString()));
						BS_YIELD = BS_YIELD.multiply(map2.get("BS_YIELD")==null?new BigDecimal(100):new BigDecimal(map2.get("BS_YIELD").toString())).divide(new BigDecimal(100));
					}
				}
				map.put("BS_MATER_COST",BS_MATER_COST);
				map.put("BS_FEE_LH_ALL",BS_FEE_LH_ALL);
				map.put("BS_FEE_MH_ALL",BS_FEE_MH_ALL);
				map.put("BS_FEE_WX_ALL",BS_FEE_WX_ALL);
				map.put("BS_COST",BS_COST);
				map.put("BS_THE_LOSS",BS_THE_LOSS);
				map.put("BS_YIELD",BS_YIELD);
			}
		}
		return ApiResponseResult.success().data(DataGrid.create(maps, (int) mapList.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}
