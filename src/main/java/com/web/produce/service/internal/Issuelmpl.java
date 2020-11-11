package com.web.produce.service.internal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.attendance.ZkemSDKUtils;
import com.web.basic.dao.EmployeeDao;
import com.web.basic.entity.Employee;
import com.web.produce.dao.CardDataDao;
import com.web.produce.dao.DevClockDao;
import com.web.produce.dao.DevLogDao;
import com.web.produce.dao.EmpFingerDao;
import com.web.produce.dao.IssueDao;
import com.web.produce.entity.CardData;
import com.web.produce.entity.DevClock;
import com.web.produce.entity.DevLog;
import com.web.produce.entity.EmpFinger;
import com.web.produce.entity.Issue;
import com.web.produce.service.IssueService;

/**
 * 下发原始数据采集
 *
 */
@Service(value = "IssueService")
@Transactional(propagation = Propagation.REQUIRED)
public class Issuelmpl  implements IssueService {

	@Autowired
	IssueDao issueDao;

	@Autowired
	EmployeeDao employeeDao;

	@Autowired
	EmpFingerDao empFingerDao;

	@Autowired
	DevClockDao devClockDao;

	@Autowired
	CardDataDao cardDataDao;

	@Autowired
	DevLogDao devLogDao;
	
	 @Autowired  
	 private Environment env;
	 
	 private static Map<String, List<String>> cmdMap = new HashMap<>();

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword,String ptype, PageRequest pageRequest) throws Exception {
		/*// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {

		}
		Specification<Issue> spec = Specification.where(BaseService.and(filters, Issue.class));
		Specification<Issue> spec1 = spec.and(BaseService.or(filters1, Issue.class));
		Page<Issue> page = issueDao.findAll(spec1, pageRequest);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Issue bs : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("empCode", bs.getEmployee().getEmpCode());// 获取关联表的数据-工号
			map.put("empName", bs.getEmployee().getEmpName());// 获取关联表的数据-姓名
			map.put("devCode", bs.getDevClock().getDevCode());
			map.put("devName", bs.getDevClock().getDevName());
			map.put("devType", bs.getDevClock().getDevType());
			list.add(map);
		}

		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));*/
		if(ptype.equals("1")){
			ptype = "指纹下发";
		}else{
			ptype = "指纹删除";
		}
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				filters.add(new SearchFilter("description", SearchFilter.Operator.EQ, ptype));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("emp.empName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("emp.empCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("devClock.devName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("devIp", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("createUser.userCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("description", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("fmemo", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<DevLog> spec = Specification.where(BaseService.and(filters, DevLog.class));
				Specification<DevLog> spec1 = spec.and(BaseService.or(filters1, DevLog.class));
				Page<DevLog> page = devLogDao.findAll(spec1, pageRequest);
				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 新增下发记录（指纹下发命令）
	 */
	@Override
	@Transactional
	public ApiResponseResult add(String dev, String emp) throws Exception {

		if (dev == null || emp == null) {
			return ApiResponseResult.failure("下发记录必须含有卡机和员工信息！");
		}
		// 数据格式转换-卡机设备
		String[] devIdArray = dev.split(";");
		List<Long> devList = new ArrayList<Long>();
		for (int i = 0; i < devIdArray.length; i++) {
			if (StringUtils.isNotEmpty(devIdArray[i])) {
				devList.add(Long.parseLong(devIdArray[i]));
			}
		}
		// 数据格式转换-员工
		String[] empIdArray = emp.split(";");
		List<Long> empList = new ArrayList<Long>();
		for (int i = 0; i < empIdArray.length; i++) {
			if (StringUtils.isNotEmpty(empIdArray[i])) {
				empList.add(Long.parseLong(empIdArray[i]));
			}
		}
		//记录有就忽略，没有就增加
		String msg = "";//记录信息
		List<Issue> listNew = new ArrayList<>();

		List<DevLog> listLog = new ArrayList<DevLog>();

		if (devList.size() > 0) {
			for (Long devId : devList) {
				DevClock devClock = devClockDao.findById((long)devId);
				if(devClock != null){
					if (empList.size() > 0) {
						for (Long empId : empList) {
							Employee em = employeeDao.findById((long)empId);
							if(em != null){
								//日记
								DevLog devLog = new DevLog();
								devLog.setCreateBy(UserUtil.getSessionUser().getId());
								devLog.setCreateDate(new Date());
								devLog.setDevId(devClock.getId());
								devLog.setDevIp(devClock.getDevIp());
								devLog.setDevCode(devClock.getDevCode());
								devLog.setEmpId(em.getId());
								devLog.setDescription("指纹下发");
								devLog.setFmemo("操作中");
								List<EmpFinger> le = empFingerDao.findByDelFlagAndEmpId(0, empId);
								if(le.size() >0){
									if( env.getProperty("envi").equals("windows")){
										if(doIssuedByUser(devClock.getDevIp(),le)){
											devLog.setFmemo("成功");
											int count =issueDao.countByDelFlagAndEmpIdAndDevClockId(0,empId,devId);
											if(count==0){
												Issue item = new Issue();
												item.setCreateDate(new Date());
												item.setCreateBy(UserUtil.getSessionUser().getId());
												item.setDevClockId(devId);
												item.setEmpId(empId);
												listNew.add(item);
											}
										}else{
											msg += em.getEmpName()+"下发指纹失败"+",";
										}
									}
								}
								listLog.add(devLog);
							}
						}
						devLogDao.saveAll(listLog);
						issueDao.saveAll(listNew);
					}
				}

			}
		}
		return ApiResponseResult.success("下发记录添加成功！"+msg);
	}
	
	/**
	 * 新增下发记录（会覆盖原有记录）
	 */
	@Transactional
	public ApiResponseResult add_bak(String dev, String emp) throws Exception {

		if (dev == null || emp == null) {
			return ApiResponseResult.failure("下发记录必须含有卡机和员工信息！");
		}
		// 数据格式转换-卡机设备
		String[] devIdArray = dev.split(";");
		List<Long> devList = new ArrayList<Long>();
		for (int i = 0; i < devIdArray.length; i++) {
			if (StringUtils.isNotEmpty(devIdArray[i])) {
				devList.add(Long.parseLong(devIdArray[i]));
			}
		}
		// 数据格式转换-员工
		String[] empIdArray = emp.split(";");
		List<Long> empList = new ArrayList<Long>();
		for (int i = 0; i < empIdArray.length; i++) {
			if (StringUtils.isNotEmpty(empIdArray[i])) {
				empList.add(Long.parseLong(empIdArray[i]));
			}
		}
		//记录有就忽略，没有就增加
		String msg = "";//记录信息
		List<Issue> listNew = new ArrayList<>();

		List<DevLog> listLog = new ArrayList<DevLog>();

		if (devList.size() > 0) {
			for (Long devId : devList) {
				DevClock devClock = devClockDao.findById((long)devId);
				if(devClock != null){
					if (empList.size() > 0) {
						for (Long empId : empList) {
							Employee em = employeeDao.findById((long)empId);
							if(em != null){
								//日记
								DevLog devLog = new DevLog();
								devLog.setCreateBy(UserUtil.getSessionUser().getId());
								devLog.setCreateDate(new Date());
								devLog.setDevId(devClock.getId());
								devLog.setDevIp(devClock.getDevIp());
								devLog.setEmpId(em.getId());
								devLog.setDescription("指纹下发");
								devLog.setFmemo("失败");
								List<EmpFinger> le = empFingerDao.findByDelFlagAndEmpId(0, empId);
								if(le.size() >0){
									if(doIssuedByUser(devClock.getDevIp(),le)){
										devLog.setFmemo("成功");
										int count =issueDao.countByDelFlagAndEmpIdAndDevClockId(0,empId,devId);
										if(count==0){
											Issue item = new Issue();
											item.setCreateDate(new Date());
											item.setCreateBy(UserUtil.getSessionUser().getId());
											item.setDevClockId(devId);
											item.setEmpId(empId);
											listNew.add(item);
										}
									}else{
										msg += em.getEmpName()+"下发指纹失败"+",";
									}
								}
								listLog.add(devLog);
							}
						}
						devLogDao.saveAll(listLog);
						issueDao.saveAll(listNew);
					}
				}

			}
		}
		return ApiResponseResult.success("下发记录添加成功！"+msg);
	}

	/**
	 * 根据ID获取
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getIssue(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("下发记录ID不能为空！");
		}
		Issue o = issueDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该下发记录不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("下发记录ID不能为空！");
		}
		Issue o = issueDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("下发记录不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		issueDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}


	/**
	 * 获取员工数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getEmp(String empKeyword, PageRequest pageRequest) throws Exception {
		/*List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("empStatus", SearchFilter.Operator.EQ, BasicStateEnum.TRUE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(empKeyword)) {
			filters1.add(new SearchFilter("empName", SearchFilter.Operator.LIKE, empKeyword));
			filters1.add(new SearchFilter("empCode", SearchFilter.Operator.LIKE, empKeyword));
			filters1.add(new SearchFilter("empType", SearchFilter.Operator.LIKE, empKeyword));
		}
		Specification<Employee> spec = Specification.where(BaseService.and(filters, Employee.class));
		Specification<Employee> spec1 = spec.and(BaseService.or(filters1, Employee.class));
		Page<Employee> page = employeeDao.findAll(spec1, pageRequest);
		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));*/
		// 排序方式
        Sort sort = pageRequest.getSort();  // 记住一定要是实体类的属性，而不能是数据库的字段
        Pageable pageable = new PageRequest(pageRequest.getPageNumber() , pageRequest.getPageSize(), sort); // （当前页， 每页记录数， 排序方式）
		Page<Map<String, Object>> page = null;
		if(StringUtils.isNotEmpty(empKeyword)){
			page =issueDao.findByKeyword(empKeyword, pageable);
		}else{
			page =issueDao.findpage(empKeyword, pageable);
		}
		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));

	}

	/**
	 * 获取卡机数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getDev(String devKeyword, PageRequest pageRequest) throws Exception {

		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("enabled", SearchFilter.Operator.EQ, BasicStateEnum.TRUE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(devKeyword)) {
			filters1.add(new SearchFilter("devCode", SearchFilter.Operator.LIKE, devKeyword));
			filters1.add(new SearchFilter("devName", SearchFilter.Operator.LIKE, devKeyword));
			filters1.add(new SearchFilter("devIp", SearchFilter.Operator.LIKE, devKeyword));
		}
		Specification<DevClock> spec = Specification.where(BaseService.and(filters, DevClock.class));
		Specification<DevClock> spec1 = spec.and(BaseService.or(filters1, DevClock.class));
		Page<DevClock> page = devClockDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 下发指纹
	 * fyx
	 * @param devIp
	 * @param empFinger
	 * @return
	 */
	private boolean doIssuedByUser(String devIp,List<EmpFinger> empFingers){
		ZkemSDKUtils sdk = new ZkemSDKUtils();
        boolean connFlag = sdk.connect(devIp, 4370);
        System.out.println(connFlag);
        if (connFlag) {
        	for(EmpFinger empFinger:empFingers){
        		//新增人员
        		boolean f= sdk.setUserInfo(empFinger.getEmp().getEmpCode(), empFinger.getEmp().getEmpName(),"", 0, true);//新增人员,如果编号一样则修改信息
        		if(!f){
        			continue;
        		}
        		//新增指纹
        		return ZkemSDKUtils.setUserTmpStr(empFinger.getEmp().getEmpCode(),Integer.parseInt(empFinger.getFingerIdx()), empFinger.getTemplateStr().trim());
        	}
        	return true;
        }else{
        	return false;
        }

	}

	/**
	 * 删除指纹
	 * fyx
	 * @param devIp
	 * @param empFinger
	 * @return
	 */
	private boolean deleteTmpByUser(String devIp,Long dev_id,List<EmpFinger> empFingers){
		ZkemSDKUtils sdk = new ZkemSDKUtils();
        boolean connFlag = sdk.connect(devIp, 4370);
        System.out.println(connFlag);
        if (connFlag) {

        	boolean flag = sdk.readGeneralLogData();
            System.out.println("flag:" + flag);

        	for(EmpFinger empFinger:empFingers){
        		//先保存改人员的考勤数据
                List<Map<String, Object>> strList = sdk.getUserOneDayInfo(empFinger.getEmp().getEmpCode());

                for(Map<String, Object> map:strList){
                	//判断信息是否已经保存过
                	DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                	DateFormat format2= new SimpleDateFormat("HH:mm:ss");

                	String carDate=format1.format(map.get("Time").toString());
                    String cardTime=format2.format(map.get("Time").toString());

                    List<CardData> cc = cardDataDao.findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTime(0, empFinger.getEmpId(), dev_id,
                    		carDate, cardTime);
                    if(cc.size()>0){
                    	continue ;
                    }
                    CardData cd = new CardData();
                    cd.setCardDate(carDate);
                    cd.setCardTime(cardTime);
                    cd.setDevClockId(dev_id);
                    cd.setEmpId(empFinger.getEmpId());
                    cd.setCreateDate(new Date());
                    cd.setCompany(empFinger.getCompany());
                    cd.setFactory(empFinger.getFactory());
                    cardDataDao.save(cd);
                }
              //删除指纹
                return sdk.delectUserById(empFinger.getEmp().getEmpCode());

        	}
        }
        return false;
	}

	@Override
	public ApiResponseResult clear(String dev, String emp) throws Exception {
		// TODO Auto-generated method stub
		if (dev == null || emp == null) {
			return ApiResponseResult.failure("删除记录必须含有卡机和员工信息！");
		}
		// 数据格式转换-卡机设备
		String[] devIdArray = dev.split(";");
		List<Long> devList = new ArrayList<Long>();
		for (int i = 0; i < devIdArray.length; i++) {
			if (StringUtils.isNotEmpty(devIdArray[i])) {
				devList.add(Long.parseLong(devIdArray[i]));
			}
		}
		// 数据格式转换-员工
		String[] empIdArray = emp.split(";");
		List<Long> empList = new ArrayList<Long>();
		for (int i = 0; i < empIdArray.length; i++) {
			if (StringUtils.isNotEmpty(empIdArray[i])) {
				empList.add(Long.parseLong(empIdArray[i]));
			}
		}
		//记录有就忽略，没有就增加
		String msg = "";//记录信息
		List<Issue> listNew = new ArrayList<>();
		List<DevLog> listLog = new ArrayList<DevLog>();
		if (devList.size() > 0) {
			for (Long devId : devList) {
				DevClock devClock = devClockDao.findById((long)devId);
				if(devClock != null){
					if (empList.size() > 0) {
						for (Long empId : empList) {
							Employee em = employeeDao.findById((long)empId);
							if(em != null){
								List<EmpFinger> le = empFingerDao.findByDelFlagAndEmpId(0, empId);
								if(le.size() >0){
									//日记
									DevLog devLog = new DevLog();
									devLog.setCreateBy(UserUtil.getSessionUser().getId());
									devLog.setCreateDate(new Date());
									devLog.setDevId(devClock.getId());
									devLog.setDevIp(devClock.getDevIp());
									devLog.setDevCode(devClock.getDevCode());
									devLog.setEmpId(em.getId());
									devLog.setDescription("指纹删除");
									devLog.setFmemo("操作中");
									if( env.getProperty("envi").equals("windows")){
										if(deleteTmpByUser(devClock.getDevIp(),devId,le)){
											devLog.setFmemo("成功");
											int count =issueDao.countByDelFlagAndEmpIdAndDevClockId(0,empId,devId);
											if(count==0){
												Issue item = new Issue();
												item.setCreateDate(new Date());
												item.setCreateBy(UserUtil.getSessionUser().getId());
												item.setDevClockId(devId);
												item.setEmpId(empId);
												listNew.add(item);
											}
										}else{
											msg += em.getEmpName()+"，但删除指纹失败"+",";
										}
									}
									listLog.add(devLog);
								}
							}
						}
						devLogDao.saveAll(listLog);
						issueDao.saveAll(listNew);
					}
				}

			}
		}
		return ApiResponseResult.success("删除记录操作成功！"+msg);
	}
	
	public ApiResponseResult clear_bak(String dev, String emp) throws Exception {
		// TODO Auto-generated method stub
		if (dev == null || emp == null) {
			return ApiResponseResult.failure("删除记录必须含有卡机和员工信息！");
		}
		// 数据格式转换-卡机设备
		String[] devIdArray = dev.split(";");
		List<Long> devList = new ArrayList<Long>();
		for (int i = 0; i < devIdArray.length; i++) {
			if (StringUtils.isNotEmpty(devIdArray[i])) {
				devList.add(Long.parseLong(devIdArray[i]));
			}
		}
		// 数据格式转换-员工
		String[] empIdArray = emp.split(";");
		List<Long> empList = new ArrayList<Long>();
		for (int i = 0; i < empIdArray.length; i++) {
			if (StringUtils.isNotEmpty(empIdArray[i])) {
				empList.add(Long.parseLong(empIdArray[i]));
			}
		}
		//记录有就忽略，没有就增加
		String msg = "";//记录信息
		List<Issue> listNew = new ArrayList<>();
		List<DevLog> listLog = new ArrayList<DevLog>();
		if (devList.size() > 0) {
			for (Long devId : devList) {
				DevClock devClock = devClockDao.findById((long)devId);
				if(devClock != null){
					if (empList.size() > 0) {
						for (Long empId : empList) {
							Employee em = employeeDao.findById((long)empId);
							if(em != null){
								List<EmpFinger> le = empFingerDao.findByDelFlagAndEmpId(0, empId);
								if(le.size() >0){
									//日记
									DevLog devLog = new DevLog();
									devLog.setCreateBy(UserUtil.getSessionUser().getId());
									devLog.setCreateDate(new Date());
									devLog.setDevId(devClock.getId());
									devLog.setDevIp(devClock.getDevIp());
									devLog.setEmpId(em.getId());
									devLog.setDescription("指纹删除");
									devLog.setFmemo("操作中");
									if(deleteTmpByUser(devClock.getDevIp(),devId,le)){
										devLog.setFmemo("成功");
										int count =issueDao.countByDelFlagAndEmpIdAndDevClockId(0,empId,devId);
										if(count==0){
											Issue item = new Issue();
											item.setCreateDate(new Date());
											item.setCreateBy(UserUtil.getSessionUser().getId());
											item.setDevClockId(devId);
											item.setEmpId(empId);
											listNew.add(item);
										}
									}else{
										msg += em.getEmpName()+"，但删除指纹失败"+",";
									}
									listLog.add(devLog);
								}
							}
						}
						devLogDao.saveAll(listLog);
						issueDao.saveAll(listNew);
					}
				}

			}
		}
		return ApiResponseResult.success("删除记录操作成功！"+msg);
	}

	@Override
	public List<String> getCmdBySn(String sn) {
		// TODO Auto-generated method stub
		//cmdMap.put(sn,cmdList);
		/*List<DevLog> ld_del = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescription(0, 0, sn, "指纹删除");
		List<String> ls = new ArrayList();
		for(DevLog dl:ld_del){
			ls.add("C:"+dl.getId()+":DATA DELETE USERINFO PIN="+dl.getEmp().getEmpCode());
		}
		
		List<DevLog> ld_0 = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescriptionAndCmdFlagFinger1AndCmdFlagFinger2(0, 0, sn, "指纹下发",0,0);
		if(ld_0.size()>0){
			for(DevLog dl:ld_0){
				
				ls.add("C:"+dl.getId()+":DATA UPDATE USERINFO PIN="+dl.getEmp().getEmpCode()+"	Name="+dl.getEmp().getEmpName()+"	Pri=0	Passwd=	Grp=0");

			}
		}else{
			List<DevLog> ld_1 = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescriptionAndCmdFlagFinger1AndCmdFlagFinger2(0, 1, sn, "指纹下发",0,0);
			if(ld_1.size()>0){
				for(DevLog dl:ld_1){
					
					//ls.add("C:"+dl.getId()+":DATA UPDATE USERINFO PIN="+dl.getEmp().getEmpCode()+"	Name="+dl.getEmp().getEmpName()+"	Pri=0	Passwd=	Grp=0");
					
					//List<EmpFinger> fl = empFingerDao.findByDelFlagAndEmpId(0, dl.getEmpId());
	            	List<EmpFinger> fl = empFingerDao.findByDelFlagAndEmpIdAndFingerIdx(0, dl.getEmpId(), "0");
					for(EmpFinger ef:fl){
						ls.add("C:"+dl.getId()+"_0"+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
					    System.out.println("C:"+dl.getId()+"_0"+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"		TMP="+ef.getTemplateStr().trim());
					}
				}
			}else{
				List<DevLog> ld_2 = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescriptionAndCmdFlagFinger1AndCmdFlagFinger2(0, 1, sn, "指纹下发",1,0);
                for(DevLog dl:ld_2){
					
					//ls.add("C:"+dl.getId()+":DATA UPDATE USERINFO PIN="+dl.getEmp().getEmpCode()+"	Name="+dl.getEmp().getEmpName()+"	Pri=0	Passwd=	Grp=0");
					
					//List<EmpFinger> fl = empFingerDao.findByDelFlagAndEmpId(0, dl.getEmpId());
	            	List<EmpFinger> fl = empFingerDao.findByDelFlagAndEmpIdAndFingerIdx(0, dl.getEmpId(), "1");
					for(EmpFinger ef:fl){
						ls.add("C:"+dl.getId()+"_1"+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
					    System.out.println("C:"+dl.getId()+"_1"+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"		TMP="+ef.getTemplateStr().trim());
					}
				}
			}
			
		}
		
		return ls;*/
		List<String> ls = new ArrayList();
		System.out.println(cmdMap.get(sn));
		if(cmdMap.get(sn)==null || cmdMap.get(sn).size()==0){
			List<DevLog> ld = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescription(0, 0, sn, "指纹下发");
			List<DevLog> ld_del = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescription(0, 0, sn, "指纹删除");
			int i=0;
			for(DevLog dl:ld){
				
				if(i>10){
					List<String> ls1 = new ArrayList<String>();
					ls1.add("in");
					cmdMap.put(sn, ls1);
					break;
				}
				
				ls.add("C:"+dl.getId()+":DATA UPDATE USERINFO PIN="+dl.getEmp().getEmpCode()+"	Name="+dl.getEmp().getEmpName()+"	Pri=0	Passwd=	Grp=0");
				
				List<EmpFinger> fl = empFingerDao.findByDelFlagAndEmpId(0, dl.getEmpId());
				for(EmpFinger ef:fl){
					ls.add("C:"+dl.getId()+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
				    System.out.println("C:"+dl.getId()+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
				i++;
				}
			}
			for(DevLog dl:ld_del){
				ls.add("C:"+dl.getId()+":DATA DELETE USERINFO PIN="+dl.getEmp().getEmpCode());
			}
			
        }
		return ls;
		/*List<DevLog> ld = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescription(0, 0, sn, "指纹下发");
		List<DevLog> ld_del = devLogDao.findByDelFlagAndCmdFlagAndDevCodeAndDescription(0, 0, sn, "指纹删除");
		List<String> ls = new ArrayList();
		for(DevLog dl:ld){
			
			ls.add("C:"+dl.getId()+":DATA UPDATE USERINFO PIN="+dl.getEmp().getEmpCode()+"	Name="+dl.getEmp().getEmpName()+"	Pri=0	Passwd=	Grp=0");
			
			List<EmpFinger> fl = empFingerDao.findByDelFlagAndEmpId(0, dl.getEmpId());
			for(EmpFinger ef:fl){
				ls.add("C:"+dl.getId()+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
			    System.out.println("C:"+dl.getId()+":DATA UPDATE FINGERTMP PIN="+dl.getEmp().getEmpCode()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
			}
		}
		for(DevLog dl:ld_del){
			ls.add("C:"+dl.getId()+":DATA DELETE USERINFO PIN="+dl.getEmp().getEmpCode());
		}
		return ls;*/
		//Sort sort = new Sort(Direction.ASC, "sort");//排序规则   多条件
		/*Sort sort = new Sort(Sort.Direction.DESC, "id");
		Pageable pageable = new PageRequest(1, 50, sort);

		Page<Map<String, Object>> ld = devLogDao.findpage(sn, "指纹下发", pageable);
		Page<Map<String, Object>> ld_del = devLogDao.findpage(sn, "指纹删除", pageable);
		List<String> ls = new ArrayList();
		for(Map<String, Object> dl:ld.getContent()){
			
			ls.add("C:"+dl.get("ID").toString()+":DATA UPDATE USERINFO PIN="+dl.get("EMP_CODE").toString()+"	Name="+dl.get("EMP_NAME").toString()+"	Pri=0	Passwd=	Grp=0");
			
			System.out.print(Long.parseLong(dl.get("EMP_ID").toString()));
			List<EmpFinger> fl = empFingerDao.findByDelFlagAndEmpId(0, Long.parseLong(dl.get("EMP_ID").toString()));
			for(EmpFinger ef:fl){
				ls.add("C:"+dl.get("ID")+":DATA UPDATE FINGERTMP PIN="+dl.get("EMP_CODE").toString()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
			    System.out.println("C:"+dl.get("ID").toString()+":DATA UPDATE FINGERTMP PIN="+dl.get("EMP_CODE").toString()+"	FID="+ef.getFingerIdx()+"	Pri=0	TMP="+ef.getTemplateStr().trim());
			}
		}
		for(Map<String, Object> dl:ld_del.getContent()){
			ls.add("C:"+dl.get("ID").toString()+":DATA DELETE USERINFO PIN="+dl.get("EMP_CODE").toString());
		}
		return ls;*/
	}

	@Override
	public void updateDevicecmd(String sn, String data) {
		// TODO Auto-generated method stub
		if(!StringUtils.isEmpty(data)){
			String[] list = data.split("\n");
			for(String da:list){
				String[] das = da.split("&");
				String id = das[0].replace("ID=", "");
				String res = das[1].replace("Return=", "");
				String fmemo = "失败";
				if(res.equals("0")){
					fmemo = "成功";
				}
				try{
					if(id.contains("_")){
						String[] ids = id.split("_");
						if(ids[1].endsWith("0")){
							devLogDao.updateDelFlagAndF1BySn(fmemo,sn,Long.valueOf(ids[0]));
						}else{
							devLogDao.updateDelFlagAndF2BySn(fmemo,sn,Long.valueOf(ids[0]));
						}
						
					}else{
						devLogDao.updateDelFlagBySn(fmemo,sn,Long.valueOf(id));
					}
					
				}catch(Exception e){
					System.out.println(e.toString());
				}
				
			}
			cmdMap.get(sn).clear();
		}
	}

	@Override
	public void uploadAttLog(String sn, String data) {
		// TODO Auto-generated method stub
		if(!StringUtils.isEmpty(data)){
			String[] list = data.split("\n");
			for(String da:list){
				String[] b = da.split("	");
				String[] times = b[1].split(" ");
				System.out.println("日期:"+times[0]);
				System.out.println("时间:"+times[1]);
				System.out.println("账号:"+b[0]);
				//判断数据是否重复
				List<Map<String, Object>> lm =  cardDataDao.findBySnAndTime(times[0], times[1], b[0], sn);
				//保存数据
				if(lm.size() == 0){
					List<DevClock> ld =  devClockDao.findByDelFlagAndDevCode(0, sn);
					List<Employee> le = employeeDao.findByDelFlagAndEmpCode(0, b[0]);
					if(ld.size()>0 && le.size()>0){
						CardData cd = new CardData();
		                cd.setCardDate(times[0]);
		                cd.setCardTime(times[1]);
		                cd.setDevClockId(ld.get(0).getId());
		                cd.setEmpId(le.get(0).getId());
		                cd.setCreateDate(new Date());
		                cd.setCompany(le.get(0).getCompany());
		                cd.setFactory(le.get(0).getFactory());
		                try{
		                	cardDataDao.save(cd);
		                }catch(Exception e){
		                	System.out.println(e.toString());
		                }
					}
				}
			}
			
			
		}
		
	}

}
