package com.system.check.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteBom;

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
		return true;
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
				todoInfoDao.closeByBsReferIdAndBsRouter(ci.getBsRecordId(), ci.getBsCheckCode());
				//this.sendTodo(sr);
			}else{
				//流程结束
                //1.如果是“QUOTE_NEW”第一步，业务部流程审批
                if(c.getBsCheckCode().equals("QUOTE_NEW") && c.getBsRecordId() != null){
                    //1.1获取报价单，修改状态为“已完成”
                	Quote quote = quoteDao.findById((long) c.getBsRecordId());
                    if(quote != null){
                    	quote.setLastupdateDate(new Date());
                    	quote.setBsStatus(1);
                    	quoteDao.save(quote);
                    }
                    //2.1根据工作中心下发BOM
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
                    		pm.setBsRadix(qb.getBsRadix());
                    		pm.setBsProQty(qb.getBsProQty());
                    		lpm.add(pm);
                        }
                    	productMaterDao.saveAll(lpm);
                    }
                    
                    //3。发送待办消息
                    
                    
                }

                //2.如果是“RULE”防御工作制度审核
                /*if(c.getBsCheckCode().equals("RULE") && c.getBsRecordId() != null){
                    //2.1获取信息，修改状态为“审核通过”
                    RuleFile ruleFile = ruleFileDao.findById((long) c.getBsRecordId());
                    if(ruleFile != null){
                        ruleFile.setModifiedTime(new Date());
                        ruleFile.setBsStatus(2);
                        ruleFileDao.save(ruleFile);
                    }
                }*/

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

}
