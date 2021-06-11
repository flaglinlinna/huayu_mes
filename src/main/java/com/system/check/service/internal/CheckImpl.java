package com.system.check.service.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.basePrice.dao.PriceCommDao;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.Unit;
import com.web.basic.dao.SysParamDao;
import com.web.basic.entity.SysParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.check.dao.CheckInfoDao;
import com.system.check.dao.WorkflowStepDao;
import com.system.check.entity.CheckInfo;
import com.system.check.entity.WorkflowStep;
import com.system.check.service.CheckService;
import com.system.todo.dao.TodoInfoDao;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.dao.ProductProcessDao;
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.ProductProcess;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteBom;
import com.web.quote.entity.QuoteItem;
import com.web.quote.entity.QuoteItemBase;
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.QuoteSumService;

@Service
@Transactional(rollbackFor = Exception.class)
public class CheckImpl   implements CheckService {

	@Autowired
	private CheckInfoDao checkInfoDao;
	@Autowired
	private WorkflowStepDao workflowStepDao;
	@Autowired
	private TodoInfoService todoInfoService;
	@Autowired
	private TodoInfoDao todoInfoDao;
	@Autowired
	private QuoteDao quoteDao;
	@Autowired
	private QuoteBomDao quoteBomDao;
	@Autowired
	private ProductMaterDao productMaterDao;
	@Autowired
	private QuoteItemBaseDao quoteItemBaseDao;
	@Autowired
	private QuoteItemDao quoteItemDao;
	@Autowired
	private QuoteProcessDao quoteProcessDao;
	@Autowired
	private ProductProcessDao productProcessDao;
	@Autowired
	private QuoteSumService quoteSumService;
	@Autowired
	private PriceCommDao priceCommDao;
	@Autowired
	private SysParamDao sysParamDao;
	@Autowired
	private UnitDao unitDao;

	@Override
	public boolean checkFirst(Long id, String checkCode) throws Exception {
		// TODO Auto-generated method stub
		//List<CheckInfo> lc = checkInfoDao.findAllByRecordId(id, checkCode);
		List<CheckInfo> lc = checkInfoDao.findNotByRecordId(id, checkCode);
		if(lc.size()>0)return false;
		return true;
	}

	@Override
	public boolean addCheckFirst(CheckInfo checkInfo) throws Exception {
		// TODO Auto-generated method stub
		List<CheckInfo> lc = new ArrayList<CheckInfo>();
		List<WorkflowStep>  lw = workflowStepDao.findAllByCheckCode(1,checkInfo.getBsCheckCode());
		SysUser user = UserUtil.getSessionUser();
		if(lw.size()>0){
			CheckInfo sr1 = this.getNewCheck(checkInfo);
			WorkflowStep w = lw.get(0);
			sr1.setBsCheckGrade(w.getBsCheckGrade());
			sr1.setBsStepName(w.getBsStepName());
			if(StringUtils.equals("UNIT", checkInfo.getBsCheckCode())){
				//新注册的还未登录的用户
				sr1.setBsCheckBy(checkInfo.getBsCheckBy());
				sr1.setBsCheckId(checkInfo.getBsCheckId());
				sr1.setBsCheckName(checkInfo.getBsCheckName());
			}else{
				sr1.setBsCheckBy(user.getUserCode());
				sr1.setBsCheckId(user.getId());
				sr1.setBsCheckName(user.getUserName());
			}
			sr1.setCreateDate(new Date());
			sr1.setLastupdateDate(new Date());
			lc.add(sr1);
		}
		//添加下一步审批人
		List<WorkflowStep>  lw2 = workflowStepDao.findAllByCheckCode(2,checkInfo.getBsCheckCode());
		if(lw2.size()>0){
			WorkflowStep w2 = lw2.get(0);
			CheckInfo sr = this.getNewCheck(checkInfo);
			sr.setBsCheckGrade(w2.getBsCheckGrade());
			sr.setBsStepName(w2.getBsStepName());
			sr.setBsCheckComments("");
			sr.setBsCheckDes("");
			sr.setBsCheckBy("");
			sr.setBsCheckId(w2.getBsCheckId());
			lc.add(sr);
			//20200615-待办
			this.sendTodo(sr);
		}
		checkInfoDao.saveAll(lc);
		
		//20210121-fyx-修改列表状态
		if(StringUtils.equals("QUOTE_NEW", checkInfo.getBsCheckCode())){//新增报价单
			Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
			if(quote != null){
				quote.setBsStatus(4);//审批中
				quoteDao.save(quote);
			}
		}else if(StringUtils.equals("out", checkInfo.getBsCheckCode())){//外协
			Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
			if(quote != null){
				quote.setBsStatus2Out(4);//审批中
				quoteDao.save(quote);
			}
		}else if(StringUtils.equals("hardware", checkInfo.getBsCheckCode())){//五金
			Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
			if(quote != null){
				quote.setBsStatus2Hardware(4);//审批中
				quoteDao.save(quote);
			}
		}else if(StringUtils.equals("molding", checkInfo.getBsCheckCode())){//注塑
			Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
			if(quote != null){
				quote.setBsStatus2Molding(4);//审批中
				quoteDao.save(quote);
			}
		}else if(StringUtils.equals("surface", checkInfo.getBsCheckCode())){//表面处理
			Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
			if(quote != null){
				quote.setBsStatus2Surface(4);//审批中
				quoteDao.save(quote);
			}
		}else if(StringUtils.equals("packag", checkInfo.getBsCheckCode())){//组装
			Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
			if(quote != null){
				quote.setBsStatus2Packag(4);//审批中
				quoteDao.save(quote);
			}
		}else if(StringUtils.equals("QUOTE_PUR", checkInfo.getBsCheckCode())){//采购部
			Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
			if(quote != null){
				quote.setBsStatus2Purchase(4);//审批中
				quoteDao.save(quote);
			}
		}
		//--end
		return true;
	}
	/**
	 * 关闭待办
	 * @param bsRouter
	 * @param bsRecordId
	 * @throws Exception
	 */
	private void closeTodo(String bsRouter,Long bsRecordId) throws Exception{
		List<TodoInfo> tl = todoInfoDao.findByDelFlagAndBsRouterAndBsReferIdAndBsStatusOrderByCreateDateDesc(0, bsRouter, bsRecordId, 0);
		if(tl.size()>0){
			//假如正常情况下只有一条记录
			if(tl.size() == 1){
				TodoInfo td = tl.get(0);
				td.setBsStatus(1);
				td.setLastupdateDate(new Date());
				todoInfoDao.save(td);
			}else{
				for(int i=0;i<tl.size();i++){
					TodoInfo td = tl.get(i);
					td.setBsStatus(1);
					td.setLastupdateDate(new Date());
					todoInfoDao.save(td);
				}
			}
		}
	}
	private void sendTodo(CheckInfo ci) throws Exception{
		TodoInfo tf = new TodoInfo();
		tf.setBsUserId(ci.getBsCheckId());
		Quote quote = quoteDao.findById((long) ci.getBsRecordId());
		String bsCode = "";
		if(quote!=null){
			bsCode = "(" +quote.getBsCode() +")";
		}
		tf.setBsTitle("您有"+ci.getBsStepName()+"待审批"+bsCode);
		tf.setBsReferId(ci.getBsRecordId());
		tf.setBsStartTime(new Date());
		tf.setBsType(2);//审批类型
		tf.setBsRouter(ci.getBsCheckCode());
		todoInfoService.add(tf);
	}
	private CheckInfo getNewCheck(CheckInfo c){
		CheckInfo sr = new CheckInfo();
		sr.setBsCheckCode(c.getBsCheckCode());
		sr.setBsCheckComments(c.getBsCheckComments());
		sr.setBsCheckDes(c.getBsCheckDes());
		sr.setCreateDate(new Date());
		sr.setBsRecordId(c.getBsRecordId());
		if(sr.getBsCheckComments().equals("退回")){
			sr.setBsStepCheckStatus(2);
		}else{
			sr.setBsStepCheckStatus(1);
		}
		return sr;
	}

	@Override
	public ApiResponseResult getInfo(Long id, String checkCode) throws Exception {
		// TODO Auto-generated method stub
		List<CheckInfo> lc = checkInfoDao.findAllByRecordId(id, checkCode);
		//结束时间为空,即未审批
		List<CheckInfo> lc1 = checkInfoDao.findNotByRecordId(id, checkCode);
		int g = 1;
		if(lc1.size()>0){
			g = lc1.get(0).getBsCheckGrade();
		}
		Map map = new HashMap();
		map.put("data", lc);
		if(lc.size()== 0){
			map.put("isLast", false);
		}else{
			map.put("isLast", lc1.size()>0?false:true);
		}

		map.put("grade", g);
		//获取下一步审批人
		List<WorkflowStep>  lw2 = workflowStepDao.findAllByCheckCode(2,checkCode);
		if(lw2.size()>0){
			map.put("checkby", lw2.get(0).getBsCheckBy());
		}else{
			map.put("checkby", "");
		}
		map.put("curBy", UserUtil.getSessionUser().getUserName());

		//获取流程信息步骤20201218-fyx
		List<WorkflowStep>  lw = workflowStepDao.findByCheckCode(checkCode);
		map.put("Lw", lw);
		map.put("Lc",lc);
		return ApiResponseResult.success().data(map);
	}

	@Override
	public boolean doCheck(CheckInfo checkInfo) throws Exception {
		// TODO Auto-generated method stub
		//20210112-fyx-关闭待办
		closeTodo(checkInfo.getBsCheckCode(),checkInfo.getBsRecordId());
		
		if(checkInfo.getBsStepCheckStatus() != 1){
			return this.doBack(checkInfo);
		}else{
			return this.doGoNext(checkInfo);
		}
	}
	/**
	 * 驳回
	 * @param c
	 * @return
	 * @throws Exception
	 */
	private boolean doBack(CheckInfo c) throws Exception{
		List<CheckInfo> lcr = new ArrayList<CheckInfo>();
		//1:保存当前步骤的信息
		List<CheckInfo> lc = checkInfoDao.findNotByRecordId(c.getBsRecordId(), c.getBsCheckCode());
		List<CheckInfo> lcc1 = checkInfoDao.findByRecordIdAndStep(c.getBsRecordId(), c.getBsCheckCode(), 1);
		if(lc.size()>0){
			CheckInfo ci = lc.get(0);
			ci.setBsCheckComments(c.getBsCheckComments());
			ci.setBsStepCheckStatus(c.getBsStepCheckStatus());
			ci.setBsCheckDes(c.getBsCheckDes());
			ci.setLastupdateDate(new Date());
			ci.setBsStepCheckStatus(2);
			ci.setBsCheckPerson(UserUtil.getSessionUser().getUserCode());
			ci.setBsCheckBy(UserUtil.getSessionUser().getUserCode());
			ci.setBsCheckName(UserUtil.getSessionUser().getUserName());
			ci.setBsCheckId(UserUtil.getSessionUser().getId());
			lcr.add(ci);
			//checkInfoDao.save(ci);
			//2：插入待审批
			int g = ci.getBsCheckGrade()-1;
			List<CheckInfo> lcc = checkInfoDao.findByRecordIdAndStep(c.getBsRecordId(), c.getBsCheckCode(), g);
			if(lcc.size()>0){
				CheckInfo cr = new CheckInfo();
				CheckInfo s = lcc.get(0);
				cr.setBsCheckBy(s.getBsCheckBy());
				cr.setBsCheckCode(s.getBsCheckCode());
				cr.setBsStepId(s.getBsStepId());
				cr.setBsStepName(s.getBsStepName());
				cr.setBsCheckGrade(s.getBsCheckGrade());
				cr.setBsRecordId(s.getBsRecordId());
				cr.setBsCheckGrade(s.getBsCheckGrade());
				cr.setCreateDate(new Date());
				cr.setBsCheckId(s.getBsCheckId());
				lcr.add(cr);

				//20200615-待办
				this.sendTodo(cr);

				Quote quote = quoteDao.findById((long) s.getBsRecordId());
				//20210121-fyx-修改列表状态
				if(StringUtils.equals("QUOTE_NEW", s.getBsCheckCode())){//新增报价单
					if(quote != null){
						quote.setBsStatus(3);//审批中
						quoteDao.save(quote);
					}
				}else if(StringUtils.equals("out", s.getBsCheckCode())){//外协
					if(quote != null){
						quote.setBsStatus2Out(3);//审批中
						quoteDao.save(quote);
					}
				}else if(StringUtils.equals("hardware", s.getBsCheckCode())){//五金
					if(quote != null){
						quote.setBsStatus2Hardware(3);//审批中
						quoteDao.save(quote);
					}
				}else if(StringUtils.equals("molding", s.getBsCheckCode())){//注塑
					if(quote != null){
						quote.setBsStatus2Molding(3);//审批中
						quoteDao.save(quote);
					}
				}else if(StringUtils.equals("surface", s.getBsCheckCode())){//表面处理
					if(quote != null){
						quote.setBsStatus2Surface(3);//审批中
						quoteDao.save(quote);
					}
				}else if(StringUtils.equals("packag", s.getBsCheckCode())){//组装
//					Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
					if(quote != null){
						quote.setBsStatus2Packag(4);//审批中
						quoteDao.save(quote);
					}
				}else if(StringUtils.equals("QUOTE_PUR", s.getBsCheckCode())){//采购部
//					Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
					if(quote != null){
						quote.setBsStatus2Purchase(4);//审批中
						quoteDao.save(quote);
					}
				}
			}
			checkInfoDao.saveAll(lcr);
			return true;
		}
		return false;
	}
	private boolean doGoNext(CheckInfo c) throws Exception{
		List<CheckInfo> lcr = new ArrayList<CheckInfo>();
		//1:保存当前步骤的信息
		List<CheckInfo> lc = checkInfoDao.findNotByRecordId(c.getBsRecordId(), c.getBsCheckCode());
		if(lc.size()>0){
			CheckInfo ci = lc.get(0);
			ci.setBsCheckComments(c.getBsCheckComments());
			ci.setBsStepCheckStatus(c.getBsStepCheckStatus());
			ci.setBsCheckDes(c.getBsCheckDes());
			ci.setLastupdateDate(new Date());
			ci.setBsStepCheckStatus(1);
			ci.setBsCheckPerson(UserUtil.getSessionUser().getUserCode());
			ci.setBsCheckBy(UserUtil.getSessionUser().getUserCode());
			ci.setBsCheckName(UserUtil.getSessionUser().getUserName());
			ci.setBsCheckId(UserUtil.getSessionUser().getId());
			lcr.add(ci);
			List<WorkflowStep>  lw2 = workflowStepDao.findAllByCheckCode(ci.getBsCheckGrade()+1,ci.getBsCheckCode());
			if(lw2.size()>0){
				//添加下一步审批人
				WorkflowStep w2 = lw2.get(0);
				CheckInfo sr = this.getNewCheck(c);
				sr.setBsCheckGrade(w2.getBsCheckGrade());
				sr.setBsStepName(w2.getBsStepName());
				sr.setBsCheckComments("");
				sr.setBsCheckDes("");
				sr.setBsCheckBy(w2.getBsCheckBy());
				sr.setBsCheckId(w2.getBsCheckId());
				lcr.add(sr);
				//20200615-待办
				//20200615-fyx-关闭待办
				//todoInfoDao.closeByBsReferIdAndBsRouter(ci.getBsRecordId(), ci.getBsCheckCode());
				this.sendTodo(sr);
			}else{
				//流程结束
				//1.如果是“QUOTE_NEW”第一步，业务部流程审批
				//2021-03-09-hjj- 新增默认确认完成
				if(c.getBsCheckCode().equals("QUOTE_NEW") && c.getBsRecordId() != null){
					//1.1获取报价单，修改状态为“已完成”
					Quote quote = quoteDao.findById((long) c.getBsRecordId());
					if(quote != null){
						quote.setLastupdateDate(new Date());
						quote.setBsStatus(1);
						quote.setBsStep(2);
						quote.setBsEndTime1(new Date());
						quote.setBsStatus2Hardware(1);//把第二个流程的置为进行中
						quote.setBsStatus2Molding(1);
						quote.setBsStatus2Out(1);
						quote.setBsStatus2Purchase(1);
						quote.setBsStatus2Packag(1);
						quote.setBsStatus2Surface(1);
						quoteDao.save(quote);
					}
					//2.1下发制造部待办项目-材料+工序
					List<QuoteItemBase> lqb = quoteItemBaseDao.findByDelFlagAndStyles(0);
					List<QuoteItem> lqi = new ArrayList<QuoteItem>();
					for(QuoteItemBase qb:lqb){
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
					quoteItemDao.saveAll(lqi);
					//2.2根据工作中心下发BOM-材料
					//先判断是否为复制单，复制单情况更新，非复制单新增
					List<QuoteBom> lql = quoteBomDao.findByDelFlagAndPkQuoteAndBsMaterNameIsNotNullOrderById(0, c.getBsRecordId());
					productMaterDao.deleteByPkQuoteBom(c.getBsRecordId());
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
								pm.setPkBomId(quote.getBsCopyId()==null?qb.getId():qb.getPkBomId());
								pm.setBsQty(qb.getBsQty());
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
									List<Map<String, Object>> lm = priceCommDao.findByDelFlagAndItemName(qb.getBsMaterName());
									if (lm.size() > 0) {
										String priceUn = lm.get(0).get("PRICE_UN").toString();
										String rangePrice = lm.get(0).get("RANGE_PRICE").toString();
										if (StringUtils.isNotEmpty(priceUn)) {
											pm.setBsGear(rangePrice);
											pm.setBsRefer(new BigDecimal(priceUn));
											pm.setBsAssess(new BigDecimal(priceUn));
											pm.setBsGeneral(1);
										}
									} else {
										pm.setBsGeneral(0);
									}
								}
//								pm.setBsRadix(qb.getBsRadix());
								pm.setBsExplain(qb.getBsExplain());//lst-20210107-增采购说明字段
//								pm.setBsProQty(qb.getBsProQty());
								pm.setPkQuote(c.getBsRecordId());
								pm.setPkItemTypeWg(qb.getPkItemTypeWg());//fyx-20210114-物料类型
								pm.setBsElement(qb.getBsElement());//fyx-20210115-组件名称
								pm.setBsGroups(qb.getBsGroups());//hjj-20210415-损耗分组
								lpm.add(pm);
							}
							productMaterDao.saveAll(lpm);
						}


					//2.3根据工作中心下发BOM-工序
					List<QuoteProcess> lpd = quoteProcessDao.findByDelFlagAndPkQuote(0, c.getBsRecordId());
					if(lpd.size() > 0){
						List<ProductProcess> lpp = new ArrayList<ProductProcess>();
						for(QuoteProcess qb:lpd){
							ProductProcess pp = new ProductProcess();
							pp.setBsName(qb.getBsName());
							pp.setBsElement(qb.getBsElement());
							pp.setBsType(qb.getProc().getBjWorkCenter().getBsCode());//类型
							pp.setBsOrder(qb.getBsOrder());
							pp.setPkProc(qb.getPkProc());
							pp.setBsLinkName(qb.getBsLinkName());
							pp.setPkBomId(qb.getPkQuoteBom());
							pp.setPkQuote(c.getBsRecordId());
							pp.setBsMaterName(qb.getBsMaterName());
							pp.setBsFeeMh(qb.getBsFeeMh());
							pp.setBsFeeLh(qb.getBsFeeLh());
							pp.setBsGroups(qb.getBsGroups());
							lpp.add(pp);
						}
						productProcessDao.saveAll(lpp);
					}

					//2021-03-09-hjj- 制造部和外协自动确认完成及自动审批
					List<SysParam> sysParams = sysParamDao.findByDelFlagAndParamCode(0, "BJ_AUTO_COMPLETE");
					String isAuto =  sysParams.size()>0?sysParams.get(0).getParamValue():"0";
					if(("1").equals(isAuto)||("是").equals(isAuto)){
						autoDoStatus(quote.getId());
					}
					//4。发送待办消息--fyx-?
				}

				//2.制造部-审批完成
				/**
				 * 类型
				 * 五金:hardware
				 * 注塑:molding
				 * 表面处理:surface
				 * 组装:packag
				 */
				if(c.getBsRecordId() != null){
					if(c.getBsCheckCode().equals("hardware") || c.getBsCheckCode().equals("molding") ||
							c.getBsCheckCode().equals("surface") || c.getBsCheckCode().equals("packag")){
						//1.1获取报价单，修改状态为“已完成”
						Quote quote = quoteDao.findById((long) c.getBsRecordId());
						if(quote != null){
							quote.setLastupdateDate(new Date());
							if(c.getBsCheckCode().equals("hardware")){
								quote.setBsStatus2Hardware(2);
							}else if(c.getBsCheckCode().equals("molding")){
								quote.setBsStatus2Molding(2);
							}else if(c.getBsCheckCode().equals("surface")){
								quote.setBsStatus2Surface(2);
							}else if(c.getBsCheckCode().equals("packag")){
								quote.setBsStatus2Packag(2);
							}
							quoteDao.save(quote);

							//判断制造部+采购部+外协部 是否全部审批完成
							List<Quote> lq = quoteDao.findByDelFlagAndStatus2AndId(quote.getId());
							if(lq.size()>0){
								quote.setBsStep(3);
								quote.setBsStatus2(2);
								quote.setBsEndTime2(new Date());
								quote.setBsStatus3(1);
								quoteDao.save(quote);

								quoteSumService.countMeterAndProcess(c.getBsRecordId()+"");
							}
						}
					}
				}

				//2.1采购部审批完成
				if(c.getBsCheckCode().equals("QUOTE_PUR") && c.getBsRecordId() != null){
					//1.1获取报价单，修改状态为“已完成”
					Quote quote = quoteDao.findById((long) c.getBsRecordId());
					if(quote != null){
						quote.setLastupdateDate(new Date());
						quote.setBsStatus2Purchase(2);
						quoteDao.save(quote);
					}
					//判断 采购部+外协部 是否全部审批完成
					List<Quote> lq = quoteDao.findByDelFlagAndStatus2AndId(quote.getId());
					if(lq.size()>0){
						quote.setBsStep(3);
						quote.setBsStatus2(2);
						quote.setBsEndTime2(new Date());
						quote.setBsStatus3(1);
						quoteDao.save(quote);
						quoteSumService.countMeterAndProcess(c.getBsRecordId()+"");
					}
				}
				//3.2外协部审批完成
				if(c.getBsCheckCode().equals("out") && c.getBsRecordId() != null){
					//1.1获取报价单，修改状态为“已完成”
					Quote quote = quoteDao.findById((long) c.getBsRecordId());
					if(quote != null){
						quote.setLastupdateDate(new Date());
						quote.setBsStatus2Out(2);
						quoteDao.save(quote);
					}
					//判断 采购部+外协部 是否全部审批完成
					List<Quote> lq = quoteDao.findByDelFlagAndStatus2AndId(quote.getId());
					if(lq.size()>0){
						quote.setBsStep(3);
						quote.setBsStatus2(2);
						quote.setBsEndTime2(new Date());
						quote.setBsStatus3(1);
						quoteDao.save(quote);
						quoteSumService.countMeterAndProcess(c.getBsRecordId()+"");
					}
				}
				
				//4.报价总流程审批完
				if(c.getBsCheckCode().equals("QUOTE") && c.getBsRecordId() != null){
					//1.1获取报价单，修改状态为“已完成”
					doEndQuote(c);
				}
			}
			checkInfoDao.saveAll(lcr);

			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean checkSecond(Long id, String checkCode) throws Exception {
		// TODO Auto-generated method stub
		//1.获取当前登录人信息
		SysUser user = UserUtil.getSessionUser();
		Long userId = user != null ? user.getId() : 0;

		//2.获取当前步骤审批人的信息
		List<CheckInfo> list = checkInfoDao.findNotByRecordId(id, checkCode);
		if(list.size() > 0){
			CheckInfo o = list.get(0);
			if(o.getBsCheckGrade() == 1){
				//20200326-fyx-如果为空则表示都可以审批
				if((o.getBsCheckBy()+"").equals("null") || (o.getBsCheckBy()+"").equals("")){
					return true;
				}
				if(userId.equals(o.getBsCheckId())){
					return true;
				}
			}else{
				if(userId.equals(o.getBsCheckId())){
					return true;
				}
			}

		}
		return false;
	}

	@Override
	public ApiResponseResult getCheckByRecordId(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponseResult getUnCheckList() throws Exception {
		// TODO Auto-generated method stub
		SysUser sysUser = UserUtil.getSessionUser();
		String area = null;
		Long unitId = null;

		return null;
	}

	@Override
	public ApiResponseResult doCheckQuote(CheckInfo checkInfo) throws Exception {
		// TODO Auto-generated method stub
		//判断是否是第一次发起审批
		/*if(checkService.checkFirst(checkInfo.getBsRecordId(),checkInfo.getBsCheckCode())){
			//1.首次发起审批
			//新增流程信息
			checkService.addCheckFirst(checkInfo);
		}else{
			//2.判断此用户是否有审核权限
			if(checkService.checkSecond(checkInfo.getBsRecordId(),checkInfo.getBsCheckCode())){
                //2.1 审批
				checkService.doCheck(checkInfo);
            }else{
			    //2.2 无审批权限，返回提示信息
                return ApiResponseResult.failure("当前用户在该步骤无审批权限");
            }
		}*/
		SysUser user = UserUtil.getSessionUser();
		Long userId = user != null ? user.getId() : 0;
		//判断是否是第一次发起审批
		if(this.checkFirst(checkInfo.getBsRecordId(),checkInfo.getBsCheckCode())){
			if(checkInfo.getBsStepCheckStatus() != 1){
				//驳回
				//1： 保存审批记录
				List<WorkflowStep>  lw = workflowStepDao.findAllByCheckCode(1,checkInfo.getBsCheckCode());
				WorkflowStep w = lw.get(0);
				checkInfo.setBsCheckGrade(w.getBsCheckGrade());
				checkInfo.setBsStepName(w.getBsStepName());
				checkInfo.setBsCheckBy(user.getUserCode());
				checkInfo.setBsCheckId(user.getId());
				checkInfo.setBsCheckName(user.getUserName());
				checkInfo.setCreateDate(new Date());
				checkInfo.setLastupdateDate(new Date());
				checkInfoDao.save(checkInfo);
				//2：修改报价状态
				if(StringUtils.isEmpty(checkInfo.getBsCheckDes())){
					return ApiResponseResult.failure("请先选中驳回需要操作的部门!");
				}
				String[] strs = checkInfo.getBsCheckDes().split(",");
				Quote quote = quoteDao.findById((long) checkInfo.getBsRecordId());
				if(quote != null){
					quote.setLastupdateDate(new Date());
					for(String str:strs){
						if(str.equals("1")){//制造部-五金
							quote.setBsStatus2Hardware(1);
							productMaterDao.updateStatus(checkInfo.getBsRecordId(), "hardware", 0);
						}else if(str.equals("2")){//制造部-注塑
							quote.setBsStatus2Molding(1);
							productMaterDao.updateStatus(checkInfo.getBsRecordId(), "molding", 0);
						}else if(str.equals("3")){//制造部-表面处理
							quote.setBsStatus2Surface(1);
							productMaterDao.updateStatus(checkInfo.getBsRecordId(), "surface", 0);
						}else if(str.equals("4")){//制造部-组装
							quote.setBsStatus2Packag(1);
							productMaterDao.updateStatus(checkInfo.getBsRecordId(), "packag", 0);
						}else if(str.equals("5")){//采购部
							quote.setBsStatus2Purchase(1);
							productMaterDao.updateStatusPurchase(checkInfo.getBsRecordId(), 0);
						}else if(str.equals("6")){//外协部
							quote.setBsStatus2Out(1);
							productMaterDao.updateStatus(checkInfo.getBsRecordId(), "out", 0);
						}
					}
					quote.setBsStep(2);
					quote.setLastupdateBy(userId);
					quoteDao.save(quote);
				}
				return ApiResponseResult.success("操作成功!");
				
			}else{
				if(this.addCheckFirst(checkInfo)){
					return ApiResponseResult.success("操作成功!");
				}else{
					return ApiResponseResult.failure("首次发起审批失败");
				}
			}
		}else{
			//2.判断此用户是否有审核权限
			if(this.checkSecond(checkInfo.getBsRecordId(),checkInfo.getBsCheckCode())){
				//同意并且结束流程
				if(checkInfo.getBsStepCheckStatus() == 3){
					//1:保存当前步骤的信息
					List<CheckInfo> lc = checkInfoDao.findNotByRecordId(checkInfo.getBsRecordId(), checkInfo.getBsCheckCode());
					if(lc.size()>0){
						CheckInfo ci = lc.get(0);
						ci.setBsCheckComments(checkInfo.getBsCheckComments());
						ci.setBsStepCheckStatus(checkInfo.getBsStepCheckStatus());
						ci.setBsCheckDes(checkInfo.getBsCheckDes());
						ci.setLastupdateDate(new Date());
						ci.setBsStepCheckStatus(1);
						ci.setBsCheckPerson(UserUtil.getSessionUser().getUserCode());
						ci.setBsCheckBy(UserUtil.getSessionUser().getUserCode());
						ci.setBsCheckName(UserUtil.getSessionUser().getUserName());
						ci.setBsCheckId(UserUtil.getSessionUser().getId());
						checkInfoDao.save(ci);
					}
					doEndQuote(checkInfo);
				}else{
					//2.1 审批
					this.doCheck(checkInfo);
				}
				
			}else{
				//2.2 无审批权限，返回提示信息
				return ApiResponseResult.failure("当前用户在该步骤无审批权限");
			}
		}
		return ApiResponseResult.success("操作成功!");
	}
	
	public void doEndQuote(CheckInfo c) throws Exception{
		Quote quote = quoteDao.findById((long) c.getBsRecordId());
		if(quote != null){
			quote.setLastupdateDate(new Date());
			quote.setBsStatus3(2);
			quote.setBsStatus4(2);
			quote.setBsEndTime3(new Date());
			quoteDao.save(quote);
		}
		
		quoteSumService.countQuoteTreeBom(Long.valueOf(c.getBsRecordId()));
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
			quoteItemDao.switchStatus(2, quoteId, "C001");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C001");
		}if(!hashMap.containsKey("surface")){
			quoteItemDao.switchStatus(2, quoteId, "C003");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C003");
		}if(!hashMap.containsKey("packag")){
			quoteItemDao.switchStatus(2, quoteId, "C004");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C004");
		}if(!hashMap.containsKey("molding")){
			quoteItemDao.switchStatus(2, quoteId, "C002");
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
			autoCheck(quoteId,"out");
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
			quoteItemDao.switchStatus(2, quoteId, "B001");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B001");
		}if(!materMap.containsKey("surface")){
			quoteItemDao.switchStatus(2, quoteId, "B003");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B003");
		}if(!materMap.containsKey("packag")){
			quoteItemDao.switchStatus(2, quoteId, "B004");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B004");
		}if(!materMap.containsKey("molding")){
			quoteItemDao.switchStatus(2, quoteId, "B002");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B002");
		}

		//材料和工艺自动完成情况下(即是该工作中心下无需填写的数据)则自动审批
		if((!materMap.containsKey("hardware")&&!hashMap.containsKey("hardware"))||((("0").equals(retrialMap.get("hardware"))||!retrialMap.containsKey("hardware"))&&isCopy)){
			quoteItemDao.switchStatus(2, quoteId, "C001");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C001");
			quoteItemDao.switchStatus(2, quoteId, "B001");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B001");

			autoCheck(quoteId,"hardware");

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
			quoteItemDao.switchStatus(2, quoteId, "B003");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B003");
			quoteItemDao.switchStatus(2, quoteId, "C003");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C003");
			if(lo.size()>0){
				Quote o = lo.get(0);
				o.setBsStatus2Surface(2);
				quoteDao.save(o);
			}
			autoCheck(quoteId,"surface");

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
			quoteItemDao.switchStatus(2, quoteId, "B004");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B004");
			quoteItemDao.switchStatus(2, quoteId, "C004");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C004");
			if(lo.size()>0){
				Quote o = lo.get(0);
				o.setBsStatus2Packag(2);
				quoteDao.save(o);
			}
			autoCheck(quoteId,"packag");

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
			quoteItemDao.switchStatus(2, quoteId, "B002");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "B002");
			quoteItemDao.switchStatus(2, quoteId, "C002");
			quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, "C002");
			if(lo.size()>0){
				Quote o = lo.get(0);
				o.setBsStatus2Molding(2);
				quoteDao.save(o);
			}
			autoCheck(quoteId,"molding");

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

	//自动审批
	public void autoCheck(Long bsRecordId,String checkCode)throws  Exception {
		List<WorkflowStep> lw = workflowStepDao.findAllByCheckCode(1, checkCode);
		SysUser user = UserUtil.getSessionUser();
		List<CheckInfo> lce = new ArrayList<CheckInfo>();
		if (lw.size() > 0) {
			CheckInfo sr1 = new CheckInfo();
			WorkflowStep w = lw.get(0);
			sr1.setBsCheckGrade(w.getBsCheckGrade());
			sr1.setBsStepName(w.getBsStepName());
			sr1.setBsCheckBy(user.getUserCode());
			sr1.setBsStepCheckStatus(1); //同意
			sr1.setBsCheckId(user.getId());
			sr1.setBsRecordId(bsRecordId);
			sr1.setBsCheckName(user.getUserName());
			sr1.setCreateDate(new Date());
			sr1.setLastupdateDate(new Date());
			sr1.setBsCheckCode(checkCode);
			sr1.setBsCheckComments("自动审批");
			sr1.setLastupdateDate(new Date());
			lce.add(sr1);
		}
		List<WorkflowStep> workflowSteps2 = workflowStepDao.findAllByCheckCode(2, checkCode);
		if (workflowSteps2.size() > 0) {
			WorkflowStep w2 = workflowSteps2.get(0);
			CheckInfo sr = new CheckInfo();
			sr.setBsCheckGrade(w2.getBsCheckGrade());
			sr.setBsStepName(w2.getBsStepName());
			sr.setBsCheckCode(checkCode);
			sr.setBsCheckComments("自动审批");
			sr.setBsCheckDes("");
			sr.setBsStepCheckStatus(1); //同意
			sr.setBsRecordId(bsRecordId);
			sr.setLastupdateDate(new Date());
			sr.setBsCheckBy(user.getUserCode());
			sr.setBsCheckName(user.getUserName());
			sr.setBsCheckId(w2.getBsCheckId());
			lce.add(sr);
		}
		checkInfoDao.saveAll(lce);
	}


}
