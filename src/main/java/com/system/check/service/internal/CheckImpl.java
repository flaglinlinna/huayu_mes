//package com.system.check.service.internal;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.app.base.data.ApiResponseResult;
//import com.system.check.dao.CheckInfoDao;
//import com.system.check.dao.WorkflowStepDao;
//import com.system.check.entity.CheckInfo;
//import com.system.check.entity.WorkflowStep;
//import com.system.check.service.CheckService;
//import com.system.role.dao.SysRoleDao;
//import com.system.role.entity.SysRole;
//import com.system.user.dao.SysUserDao;
//import com.system.user.dao.UserRoleMapDao;
//import com.system.user.entity.SysUser;
//import com.system.user.entity.UserRoleMap;
//import com.utils.UserService;
//import com.utils.UserUtil;
//import com.web.Implement.dao.DrillDao;
//import com.web.Implement.dao.InspectionDao;
//import com.web.Implement.dao.OverhaulDao;
//import com.web.Implement.dao.RectifyDao;
//import com.web.Implement.dao.TrainDao;
//import com.web.Implement.dao.TroubleShootDao;
//import com.web.Implement.entity.Drill;
//import com.web.Implement.entity.Inspection;
//import com.web.Implement.entity.Overhaul;
//import com.web.Implement.entity.Rectify;
//import com.web.Implement.entity.Train;
//import com.web.Implement.entity.TroubleShoot;
//import com.web.basic.dao.TodoInfoDao;
//import com.web.basic.entity.TodoInfo;
//import com.web.basic.service.TodoInfoService;
//import com.web.security.dao.DefenseFileDao;
//import com.web.security.dao.DeviceDao;
//import com.web.security.dao.HandleDao;
//import com.web.security.dao.ReportDao;
//import com.web.security.dao.RuleFileDao;
//import com.web.security.dao.SpecialDao;
//import com.web.security.dao.SpecialFileDao;
//import com.web.security.dao.UnitDao;
//import com.web.security.entity.DefenseFile;
//import com.web.security.entity.Device;
//import com.web.security.entity.Handle;
//import com.web.security.entity.Report;
//import com.web.security.entity.RuleFile;
//import com.web.security.entity.Special;
//import com.web.security.entity.Unit;
//
//@Service
//@Transactional(rollbackFor = Exception.class)
//public class CheckImpl  extends UserService  implements CheckService {
//
//	@Autowired
//    private CheckInfoDao checkInfoDao;
//	@Autowired
//    private WorkflowStepDao workflowStepDao;
//    @Autowired
//	private UnitDao unitDao;
//    @Autowired
//    private SysUserDao sysUserDao;
//    @Autowired
//    private UserRoleMapDao userRoleMapDao;
//    @Autowired
//    private SysRoleDao sysRoleDao;
//    @Autowired
//    private RuleFileDao ruleFileDao;
//    @Autowired
//    private DefenseFileDao defenseFileDao;
//    @Autowired
//    private ReportDao reportDao;
//    @Autowired
//    private HandleDao handleDao;
//    @Autowired
//    private DrillDao drillDao;
//    @Autowired
//    private TrainDao trainDao;
//    @Autowired
//    private InspectionDao inspectionDao;
//    @Autowired
//    private TroubleShootDao troubleShootDao;
//    @Autowired
//    private RectifyDao rectifyDao;
//    @Autowired
//    private OverhaulDao overhaulDao;
//    @Autowired
//    private DeviceDao deviceDao;
//    @Autowired
//    private SpecialFileDao specialFileDao;
//    @Autowired
//    private TodoInfoService todoInfoService;
//    @Autowired
//    private TodoInfoDao todoInfoDao;
//    @Autowired
//    private SpecialDao specialDao;
//
//
//	@Override
//	public boolean checkFirst(Long id, String checkCode) throws Exception {
//		// TODO Auto-generated method stub
//		//List<CheckInfo> lc = checkInfoDao.findAllByRecordId(id, checkCode);
//		List<CheckInfo> lc = checkInfoDao.findNotByRecordId(id, checkCode);
//		if(lc.size()>0)return false;
//		return true;
//	}
//
//	@Override
//	public boolean addCheckFirst(CheckInfo checkInfo) throws Exception {
//		// TODO Auto-generated method stub
//		List<CheckInfo> lc = new ArrayList<CheckInfo>();
//		List<WorkflowStep>  lw = workflowStepDao.findAllByCheckCode(1,checkInfo.getBsCheckCode());
//		SysUser user = UserUtil.getSessionUser();
//		if(user.getBsType() !=2){
//			return false;
//		}
//		Unit unit = unitDao.findByUserId(user.getId());
//		if(lw.size()>0){
//			CheckInfo sr1 = this.getNewCheck(checkInfo);
//			WorkflowStep w = lw.get(0);
//			sr1.setBsCheckGrade(w.getBsCheckGrade());
//			sr1.setBsStepName(w.getBsStepName());
//			if(StringUtils.equals("UNIT", checkInfo.getBsCheckCode())){
//			    //新注册的还未登录的用户
//                sr1.setBsCheckBy(checkInfo.getBsCheckBy());
//                sr1.setBsCheckId(checkInfo.getBsCheckId());
//                sr1.setBsCheckName(checkInfo.getBsCheckName());
//            }else{
//                sr1.setBsCheckBy(user.getBsCode());
//                sr1.setBsCheckId(user.getId());
//                sr1.setBsCheckName(user.getBsName());
//            }
//			sr1.setCreatedTime(new Date());
//			sr1.setModifiedTime(new Date());
//			sr1.setBsCheckOrg(user.getUnitName());
//			sr1.setUnitArea(unit.getArea());
//			lc.add(sr1);
//		}
//		//添加下一步审批人
//		List<WorkflowStep>  lw2 = workflowStepDao.findAllByCheckCode(2,checkInfo.getBsCheckCode());
//		if(lw2.size()>0){
//			WorkflowStep w2 = lw2.get(0);
//			CheckInfo sr = this.getNewCheck(checkInfo);
//			sr.setBsCheckGrade(w2.getBsCheckGrade());
//			sr.setBsStepName(w2.getBsStepName());
//			sr.setBsCheckComments("");
//			sr.setBsCheckDes("");
//			sr.setBsCheckBy("");
//			sr.setUnitArea(unit.getArea());
//			lc.add(sr);
//			//20200615-待办
//			this.sendTodo(sr);
//		}
//		checkInfoDao.saveAll(lc);
//		return true;
//	}
//	private void sendTodo(CheckInfo ci) throws Exception{
//		TodoInfo tf = new TodoInfo();
//		tf.setBsArea(ci.getUnitArea());
//		tf.setBsUserCode(ci.getBsCheckBy());
//		tf.setBsTitle("您有"+ci.getBsStepName()+"待审批"+ci.getBsCheckOrg());
//		tf.setBsReferId(ci.getBsRecordId());
//		tf.setBsStartTime(new Date());
//		tf.setBsType(1);
//		tf.setBsRouter(ci.getBsCheckCode());
//		todoInfoService.add(tf);
//	}
//	private CheckInfo getNewCheck(CheckInfo c){
//		CheckInfo sr = new CheckInfo();
//		sr.setBsCheckCode(c.getBsCheckCode());
//		sr.setBsCheckComments(c.getBsCheckComments());
//		sr.setBsCheckDes(c.getBsCheckDes());
//		sr.setCreatedTime(new Date());
//		sr.setBsRecordId(c.getBsRecordId());
//		if(sr.getBsCheckComments().equals("退回")){
//			sr.setBsStepCheckStatus(2);
//		}else{
//			sr.setBsStepCheckStatus(1);
//		}
//		return sr;
//	}
//
//	@Override
//	public ApiResponseResult getInfo(Long id, String checkCode) throws Exception {
//		// TODO Auto-generated method stub
//		List<CheckInfo> lc = checkInfoDao.findAllByRecordId(id, checkCode);
//		List<CheckInfo> lc1 = checkInfoDao.findNotByRecordId(id, checkCode);
//		int g = 1;
//		if(lc1.size()>0){
//			g = lc1.get(0).getBsCheckGrade();
//		}
//		Map map = new HashMap();
//		map.put("data", lc);
//		if(lc.size()== 0){
//			map.put("isLast", false);
//		}else{
//			map.put("isLast", lc1.size()>0?false:true);
//		}
//
//		map.put("grade", g);
//		//获取下一步审批人
//		List<WorkflowStep>  lw2 = workflowStepDao.findAllByCheckCode(2,checkCode);
//		if(lw2.size()>0){
//			map.put("checkby", lw2.get(0).getBsCheckBy());
//		}else{
//			map.put("checkby", "");
//		}
//		map.put("curBy", UserUtil.getSessionUser().getBsName());
//
//		return ApiResponseResult.success().data(map);
//	}
//
//	@Override
//	public boolean doCheck(CheckInfo checkInfo) throws Exception {
//		// TODO Auto-generated method stub
//		if(checkInfo.getBsStepCheckStatus() != 1){
//			return this.doBack(checkInfo);
//		}else{
//			return this.doGoNext(checkInfo);
//		}
//	}
//	/**
//	 * 驳回
//	 * @param c
//	 * @return
//	 * @throws Exception
//	 */
//	private boolean doBack(CheckInfo c) throws Exception{
//		List<CheckInfo> lcr = new ArrayList<CheckInfo>();
//		//1:保存当前步骤的信息
//		List<CheckInfo> lc = checkInfoDao.findNotByRecordId(c.getBsRecordId(), c.getBsCheckCode());
//		List<CheckInfo> lcc1 = checkInfoDao.findByRecordIdAndStep(c.getBsRecordId(), c.getBsCheckCode(), 1);
//		if(lc.size()>0){
//			CheckInfo ci = lc.get(0);
//			ci.setBsCheckComments(c.getBsCheckComments());
//			ci.setBsStepCheckStatus(c.getBsStepCheckStatus());
//			ci.setBsCheckDes(c.getBsCheckDes());
//			ci.setModifiedTime(new Date());
//			ci.setBsStepCheckStatus(2);
//			ci.setBsCheckPerson(UserUtil.getSessionUser().getBsCode());
//			ci.setBsCheckBy(UserUtil.getSessionUser().getBsCode());
//			ci.setBsCheckName(UserUtil.getSessionUser().getBsName());
//			ci.setBsCheckOrg(UserUtil.getSessionUser().getUnitName()!=null ? UserUtil.getSessionUser().getUnitName() : UserUtil.getSessionUser().getBsName());
//			lcr.add(ci);
//			//checkInfoDao.save(ci);
//			//2：插入待审批
//			int g = ci.getBsCheckGrade()-1;
//			List<CheckInfo> lcc = checkInfoDao.findByRecordIdAndStep(c.getBsRecordId(), c.getBsCheckCode(), g);
//			if(lcc.size()>0){
//				CheckInfo cr = new CheckInfo();
//				CheckInfo s = lcc.get(0);
//				cr.setBsCheckBy(s.getBsCheckBy());
//				cr.setBsCheckCode(s.getBsCheckCode());
//				cr.setBsStepId(s.getBsStepId());
//				cr.setBsStepName(s.getBsStepName());
//				cr.setBsCheckGrade(s.getBsCheckGrade());
//				cr.setBsRecordId(s.getBsRecordId());
//				cr.setBsCheckGrade(s.getBsCheckGrade());
//				cr.setCreatedTime(new Date());
//				cr.setUnitArea(s.getUnitArea());
//				lcr.add(cr);
//
//				//修改审核状态
//                //如果是“SPECIAL”专项或重点检查
//                if(c.getBsCheckCode().equals("SPECIAL") && c.getBsRecordId() != null){
//                    Special special = specialDao.findById((long) c.getBsRecordId());
//                    if(special != null){
//                        special.setModifiedTime(new Date());
//                        special.setBsStatus(1);
//                        specialDao.save(special);
//
//                        //添加待办事项
//                        TodoInfo todoInfo = new TodoInfo();
//                        todoInfo.setCreatedTime(new Date());
//                        todoInfo.setPkCreatedBy(UserUtil.getSessionUser() != null ? UserUtil.getSessionUser().getId() : null);//创建人
//                        todoInfo.setBsUserCode(special.getUnit()!=null&&special.getUnit().getUserId()!=null ? special.getUnit().getUserId().toString() : null);//重点单位用户
//                        todoInfo.setBsRouter("/specialFile/toSpecialFile");
//                        todoInfo.setBsStatus(0);
//                        todoInfo.setBsTitle("专项或重点检查-驳回待提交");
//                        todoInfo.setBsStartTime(new Date());
//                        todoInfo.setBsReferId(special.getId());//检查ID
//                        todoInfoDao.save(todoInfo);
//                    }
//                }
//
//				//20200615-待办
//				this.sendTodo(cr);
//			}
//			checkInfoDao.saveAll(lcr);
//			return true;
//		}
//		return false;
//	}
//	private boolean doGoNext(CheckInfo c) throws Exception{
//		List<CheckInfo> lcr = new ArrayList<CheckInfo>();
//		//1:保存当前步骤的信息
//		List<CheckInfo> lc = checkInfoDao.findNotByRecordId(c.getBsRecordId(), c.getBsCheckCode());
//		if(lc.size()>0){
//			CheckInfo ci = lc.get(0);
//			ci.setBsCheckComments(c.getBsCheckComments());
//			ci.setBsStepCheckStatus(c.getBsStepCheckStatus());
//			ci.setBsCheckDes(c.getBsCheckDes());
//			ci.setModifiedTime(new Date());
//			ci.setBsStepCheckStatus(1);
//			ci.setBsCheckPerson(UserUtil.getSessionUser().getBsCode());
//			ci.setBsCheckBy(UserUtil.getSessionUser().getBsCode());
//			ci.setBsCheckName(UserUtil.getSessionUser().getBsName());
//			ci.setBsCheckOrg(StringUtils.isNotEmpty(UserUtil.getSessionUser().getUnitName()) ? UserUtil.getSessionUser().getUnitName() : UserUtil.getSessionUser().getBsName());
//			lcr.add(ci);
//			List<WorkflowStep>  lw2 = workflowStepDao.findAllByCheckCode(ci.getBsCheckGrade()+1,ci.getBsCheckCode());
//			if(lw2.size()>0){
//				//添加下一步审批人
//				WorkflowStep w2 = lw2.get(0);
//				CheckInfo sr = this.getNewCheck(c);
//				sr.setBsCheckGrade(w2.getBsCheckGrade());
//				sr.setBsStepName(w2.getBsStepName());
//				sr.setBsCheckComments("");
//				sr.setBsCheckDes("");
//				sr.setBsCheckBy(w2.getBsCheckBy());
//				sr.setUnitArea(ci.getUnitArea());
//				lcr.add(sr);
//				//20200615-待办
//				this.sendTodo(sr);
//			}else{
//				//流程结束
//                //1.如果是“UNIT”单位审核
//                if(c.getBsCheckCode().equals("UNIT") && c.getBsRecordId() != null){
//                    //1.1获取单位信息，修改状态为“审核通过”
//                    Unit unit = unitDao.findById((long) c.getBsRecordId());
//                    if(unit != null){
//                        unit.setModifiedTime(new Date());
//                        unit.setBsStatus(2);
//                        unitDao.save(unit);
//
//                        //1.2获取用户角色信息，角色由“UNIT-NEW”改为“UNIT-APPROVAL”
//                        SysUser sysUser = sysUserDao.findById((long) unit.getUserId());
//                        //删除原来的角色权限
//                        if(sysUser != null){
//                            List<UserRoleMap> list = userRoleMapDao.findByIsDelAndUserId(0, sysUser.getId());
//                            if(list.size() > 0){
//                                for(UserRoleMap urItem : list){
//                                    urItem.setModifiedTime(new Date());
//                                    urItem.setIsDel(1);
//                                }
//                                userRoleMapDao.saveAll(list);
//                            }
//
//                            // 给用户添加新的角色
//                            SysRole sysRole = sysRoleDao.findByIsDelAndBsCode(0, "UNIT-APPROVAL");
//                            UserRoleMap urItem = new UserRoleMap();
//                            urItem.setCreatedTime(new Date());
//                            urItem.setUserId(sysUser.getId());
//                            urItem.setRoleId(sysRole != null ? sysRole.getId() : null);
//                            userRoleMapDao.save(urItem);
//                        }
//                    }
//                }
//
//                //2.如果是“RULE”防御工作制度审核
//                if(c.getBsCheckCode().equals("RULE") && c.getBsRecordId() != null){
//                    //2.1获取信息，修改状态为“审核通过”
//                    RuleFile ruleFile = ruleFileDao.findById((long) c.getBsRecordId());
//                    if(ruleFile != null){
//                        ruleFile.setModifiedTime(new Date());
//                        ruleFile.setBsStatus(2);
//                        ruleFileDao.save(ruleFile);
//                    }
//                }
//
//                //3.如果是“DEFENSE”雷电防御资料
//                if(c.getBsCheckCode().equals("DEFENSE") && c.getBsRecordId() != null){
//                    //3.1获取信息，修改状态为“审核通过”
//                    DefenseFile defenseFile = defenseFileDao.findById((long) c.getBsRecordId());
//                    if(defenseFile != null){
//                        defenseFile.setModifiedTime(new Date());
//                        defenseFile.setBsStatus(2);
//                        defenseFileDao.save(defenseFile);
//                    }
//                }
//
//                //4.如果是“REPORT”气象灾害发生报告
//                if(c.getBsCheckCode().equals("REPORT") && c.getBsRecordId() != null){
//                    Report report = reportDao.findById((long) c.getBsRecordId());
//                    if(report != null){
//                        report.setModifiedTime(new Date());
//                        report.setBsStatus(2);
//                        reportDao.save(report);
//                    }
//                }
//
//                //5.如果是“HANDLE”气象灾害应急处置
//                if(c.getBsCheckCode().equals("HANDLE") && c.getBsRecordId() != null){
//                    Handle handle = handleDao.findById((long) c.getBsRecordId());
//                    if(handle != null){
//                        handle.setModifiedTime(new Date());
//                        handle.setBsStatus(2);
//                        handleDao.save(handle);
//                    }
//                }
//
//                //6.如果是“DRILL”应急演练记录
//                if(c.getBsCheckCode().equals("DRILL") && c.getBsRecordId() != null){
//                    Drill drill = drillDao.findById((long) c.getBsRecordId());
//                    if(drill != null){
//                        drill.setModifiedTime(new Date());
//                        drill.setBsStatus(2);
//                        drillDao.save(drill);
//                    }
//                }
//
//                //7.如果是“TRAIN”防御知识培训记录
//                if(c.getBsCheckCode().equals("TRAIN") && c.getBsRecordId() != null){
//                    Train train = trainDao.findById((long) c.getBsRecordId());
//                    if(train != null){
//                        train.setModifiedTime(new Date());
//                        train.setBsStatus(2);
//                        trainDao.save(train);
//                    }
//                }
//
//                //8.如果是“INSPECT”定期巡查记录
//                if(c.getBsCheckCode().equals("INSPECT") && c.getBsRecordId() != null){
//                    Inspection inspection = inspectionDao.findById((long) c.getBsRecordId());
//                    if(inspection != null){
//                        inspection.setModifiedTime(new Date());
//                        inspection.setBsStatus(2);
//                        inspectionDao.save(inspection);
//                    }
//                }
//
//                //9.如果是“SHOOT”隐患排查记录
//                if(c.getBsCheckCode().equals("SHOOT") && c.getBsRecordId() != null){
//                    TroubleShoot shoot = troubleShootDao.findById((long) c.getBsRecordId());
//                    if(shoot != null){
//                        shoot.setModifiedTime(new Date());
//                        shoot.setBsStatus(2);
//                        troubleShootDao.save(shoot);
//                    }
//                }
//
//                //10.如果是“RECTIFY”隐患整改记录
//                if(c.getBsCheckCode().equals("RECTIFY") && c.getBsRecordId() != null){
//                    Rectify rectify = rectifyDao.findById((long) c.getBsRecordId());
//                    if(rectify != null){
//                        rectify.setModifiedTime(new Date());
//                        rectify.setBsStatus(2);
//                        rectifyDao.save(rectify);
//                    }
//                }
//
//                //11.如果是“OVERHAUL”检修记录
//                if(c.getBsCheckCode().equals("OVERHAUL") && c.getBsRecordId() != null){
//                    Overhaul overhaul = overhaulDao.findById((long) c.getBsRecordId());
//                    if(overhaul != null){
//                        overhaul.setModifiedTime(new Date());
//                        overhaul.setBsStatus(2);
//                        overhaulDao.save(overhaul);
//                    }
//                }
//
//                //12.如果是“DEVICE”预报预警信息接收终端情况
//                if(c.getBsCheckCode().equals("DEVICE") && c.getBsRecordId() != null){
//                    Device device = deviceDao.findById((long) c.getBsRecordId());
//                    if(device != null){
//                        device.setModifiedTime(new Date());
//                        device.setBsStatus(2);
//                        deviceDao.save(device);
//                    }
//                }
//
//                //13.如果是“SPECIAL”专项或重点检查
//                if(c.getBsCheckCode().equals("SPECIAL") && c.getBsRecordId() != null){
//                    Special special = specialDao.findById((long) c.getBsRecordId());
//                    if(special != null){
//                        special.setModifiedTime(new Date());
//                        special.setBsStatus(3);
//                        specialDao.save(special);
//                    }
//                }
//
//			}
//			checkInfoDao.saveAll(lcr);
//
//			//20200615-fyx-关闭待办
//			todoInfoDao.closeByBsReferIdAndBsRouter(ci.getBsRecordId(), ci.getBsCheckCode());
//
//			return true;
//		}else{
//			return false;
//		}
//	}
//
//	@Override
//	public boolean checkSecond(Long id, String checkCode) throws Exception {
//		// TODO Auto-generated method stub
//		//1.获取当前登录人信息
//        SysUser user = UserUtil.getSessionUser();
//        String userCode = user != null ? user.getBsCode() : "";
//
//        //2.获取当前步骤审批人的信息
//        List<CheckInfo> list = checkInfoDao.findNotByRecordId(id, checkCode);
//        if(list.size() > 0){
//            CheckInfo o = list.get(0);
//            if(o.getBsCheckGrade() == 1){
//            	//20200326-fyx-如果为空则表示都可以审批
//                if((o.getBsCheckBy()+"").equals("null") || (o.getBsCheckBy()+"").equals("")){
//                	return true;
//                }
//                if(userCode.equals(o.getBsCheckBy())){
//                    return true;
//                }
//            }else{
//            	//第二步，根据角色判断,气象局审批所有,各地区的只能审批各地区的信息
//                if(user.getBsType() != 1){
//                    return false;
//                }
//            	return true;
//            }
//
//        }
//		return false;
//	}
//
//	@Override
//	public ApiResponseResult getCheckByRecordId(Long id) throws Exception {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public ApiResponseResult getUnCheckList() throws Exception {
//		// TODO Auto-generated method stub
//		SysUser sysUser = UserUtil.getSessionUser();
//        String area = null;
//        Long unitId = null;
//        if(sysUser != null && sysUser.getBsType() == 1){
//            //获取登录用户角色区域
//            area = super.getArea(sysUser);
//        }else{
//            //获取登录用户的重点单位ID
//            unitId = super.getUnitId(sysUser);
//        }
//
//		return null;
//	}
//
//}
