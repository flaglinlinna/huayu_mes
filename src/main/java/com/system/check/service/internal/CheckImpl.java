package com.system.check.service.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.basePrice.dao.PriceCommDao;
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
		tf.setBsTitle("您有"+ci.getBsStepName()+"待审批");
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
					List<QuoteBom> lql = quoteBomDao.findByDelFlagAndPkQuote(0, c.getBsRecordId());
					if(lql.size() > 0){
						List<ProductMater> lpm = new ArrayList<ProductMater>();
						for(QuoteBom qb:lql){
							ProductMater pm = new ProductMater();
							pm.setBsType(qb.getWc().getBsCode());//类型
							pm.setBsComponent(qb.getBsComponent());
							pm.setBsMaterName(qb.getBsMaterName());
							pm.setBsModel(qb.getBsModel());
							pm.setBsQty(qb.getBsQty());
							pm.setPkUnit(qb.getPkUnit());
							if(qb.getUnit()!=null){
								pm.setBsUnit(qb.getUnit().getUnitName()); //hjj-20210120-补充单位名称
							}
							pm.setBsCave(qb.getBsCave()); //hjj-20210121-模板导入新增水口重和穴数
							pm.setBsWaterGap(qb.getBsWaterGap());
							if(qb.getBsAgent()==1){
								pm.setBsAssess(BigDecimal.ZERO);
							}else {
								//hjj-20210122 不是待采,先查询物料通用价格
								//待确认

							}
							pm.setBsRadix(qb.getBsRadix());
							pm.setBsExplain(qb.getBsExplain());//lst-20210107-增采购说明字段
							pm.setBsProQty(qb.getBsProQty());
							pm.setPkQuote(c.getBsRecordId());
							pm.setPkItemTypeWg(qb.getPkItemTypeWg());//fyx-20210114-物料类型
							pm.setBsElement(qb.getBsElement());//fyx-20210115-组件名称
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
							pp.setPkQuote(c.getBsRecordId());
							pp.setBsFeeMh(qb.getBsFeeMh());
							pp.setBsFeeLh(qb.getBsFeeLh());
							lpp.add(pp);
						}
						productProcessDao.saveAll(lpp);
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


}
