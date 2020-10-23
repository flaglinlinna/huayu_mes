package com.web.produce.service.internal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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
import com.web.basic.entity.Mtrial;
import com.web.produce.dao.CardDataDao;
import com.web.produce.dao.DevClockDao;
import com.web.produce.entity.CardData;
import com.web.produce.entity.DevClock;
import com.web.produce.service.CardDataService;

/**
 * 卡点原始数据采集
 *
 */
@Service(value = "CardDataService")
@Transactional(propagation = Propagation.REQUIRED)
public class CardDatalmpl implements CardDataService {

	@Autowired
	CardDataDao cardDataDao;
	
	@Autowired
	EmployeeDao employeeDao;
	
	@Autowired
	DevClockDao devClockDao;
	
	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("employee.empCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("employee.empName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("employee.empType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devClock.devCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devClock.devName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devClock.devIp", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<CardData> spec = Specification.where(BaseService.and(filters, CardData.class));
		Specification<CardData> spec1 = spec.and(BaseService.or(filters1, CardData.class));
		Page<CardData> page = cardDataDao.findAll(spec1, pageRequest);

		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(CardData bs:page.getContent()){ 
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("empCode",bs.getEmployee().getEmpCode());//获取关联表的数据-工号
			map.put("empName",bs.getEmployee().getEmpName());//获取关联表的数据-姓名
			map.put("devIp",bs.getDevClock().getDevIp());//获取关联表的数据-卡机IP
			map.put("cardDate", bs.getCardDate());
			map.put("cardTime", bs.getCardTime());
			map.put("fstatus", bs.getFstatus());;
			map.put("fmemo", bs.getFmemo());
			list.add(map);
		}
		
		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	/**
	 * 新增卡点记录
	 */
	@Override
	@Transactional
	public ApiResponseResult add(CardData cardData) throws Exception {
		if (cardData == null) {
			return ApiResponseResult.failure("卡点记录不能为空！");
		}
		
		List<CardData> cc = cardDataDao.findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTimeAndFstatus(0, cardData.getEmpId(), cardData.getDevClockId(),
				cardData.getCardDate(), cardData.getCardTime(),1);
        if(cc.size()>0){
        	return ApiResponseResult.failure("该数据已存在!不允许重复添加!");
        }
        
		cardData.setCreateDate(new Date());
		cardData.setCreateBy(UserUtil.getSessionUser().getId());
		cardData.setDelFlag(0);
		cardDataDao.save(cardData);

		return ApiResponseResult.success("卡点记录添加成功！").data(cardData);
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
	public ApiResponseResult getCardData(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("卡点记录ID不能为空！");
		}
		CardData o = cardDataDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该卡点记录不存在！");
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
			return ApiResponseResult.failure("卡点记录ID不能为空！");
		}
		CardData o = cardDataDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("卡点记录不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		cardDataDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}
	/**
	 * 获取员工数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getEmp() throws Exception {
		List<Employee> list = employeeDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}
	/**
	 * 获取卡机数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getDev() throws Exception {
		List<DevClock> list = devClockDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}

	@Override
	public ApiResponseResult updateData(String devIds,String stype) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(devIds)){
			return ApiResponseResult.failure("请先选择卡机");
		}
		String[] dev_ids = devIds.split(";");
		String msg = "";
		for(String did:dev_ids){
			if(!StringUtils.isEmpty(did)){
				DevClock dc = devClockDao.findById(Long.parseLong(did));
				if(dc != null){
					ApiResponseResult api = this.saveCardData(dc);
					if(!api.isResult()){
						msg += api.getMsg()+";";
					}
					if(stype.equals("2")){
						Date date = new Date(); 
						DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
						api = this.delCardData(dc, format.format(date));
						if(!api.isResult()){
							msg += api.getMsg()+";";
						}
					}
				}
			}
		}
		return ApiResponseResult.success("同步成功!"+msg);
	}
	
	/**
	 * 根据卡机获取卡机上的考勤记录
	 * @param DevClock
	 * @throws Exception 
	 */
	private ApiResponseResult saveCardData(DevClock devClock) throws Exception{
		List<CardData> lc = new ArrayList<CardData>();
		ZkemSDKUtils sdk = new ZkemSDKUtils();
        boolean connFlag = sdk.connect(devClock.getDevIp(), 4370);
        System.out.println(connFlag);
        if (connFlag) {
        	
            boolean flag = sdk.readGeneralLogData();
            
            if(flag){
            	List<Map<String, Object>> strList = sdk.getGeneralLogData();

                for(Map<String, Object> map:strList){
                	//先判断员工信息是否存在
                	String user_code = map.get("EnrollNumber")==null?"":map.get("EnrollNumber").toString();//获取用户账号
                	List<Employee> le = employeeDao.findByDelFlagAndEmpCode(0,user_code);
                	if(le.size() == 0){
                		continue ;
                	}
                	//日期转换
                	 SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd") ;        // 实例化模板对象    
                     SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss") ;        // 实例化模板对象
                     
                	//判断信息是否已经保存过
                    String d = map.get("Year")+"-"+map.get("Month")+"-"+map.get("Day");
                	String t = map.get("Hour")+":"+map.get("Minute")+":"+map.get("Second");
                	String carDate=sdf1.format(sdf1.parse(d));//LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String cardTime=sdf2.format(sdf2.parse(t));//LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                    List<CardData> cc = cardDataDao.findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTimeAndFstatus(0, le.get(0).getId(), devClock.getId(),
                    		carDate, cardTime,1);
                    if(cc.size()>0){
                    	continue ;
                    }
                    CardData cd = new CardData();
                    cd.setCardDate(carDate);
                    cd.setCardTime(cardTime);
                    cd.setDevClockId(devClock.getId());
                    cd.setEmpId(le.get(0).getId());
                    cd.setCreateDate(new Date());
                    cd.setCompany(le.get(0).getCompany());
                    cd.setFactory(le.get(0).getFactory());

                    lc.add(cd);
                }
                cardDataDao.saveAll(lc);
            }else{
            	return ApiResponseResult.failure("读取卡机IP"+devClock.getDevIp()+"内容失败");
            }
        }else{
        	return ApiResponseResult.failure("连接卡机IP"+devClock.getDevIp()+"失败");
        }
		return ApiResponseResult.success();
	}
	
	/**
	 * 根据卡机删除卡机上的所有考勤记录
	 * @param DevClock
	 * @param time(YYYY-MM-DD hh:mm:ss )
	 */
	public ApiResponseResult delCardData(DevClock devClock,String time){
		List<CardData> lc = new ArrayList<CardData>();
		ZkemSDKUtils sdk = new ZkemSDKUtils();
        boolean connFlag = sdk.connect(devClock.getDevIp(), 4370);
        System.out.println(connFlag);
        if (connFlag) {
            boolean f = sdk.deleteGeneralLogData(time);
            if(f){
            	return ApiResponseResult.success();
            }else{
            	return ApiResponseResult.failure("删除卡机"+devClock.getDevIp()+"数据失败");
            }
        }else{
        	return ApiResponseResult.failure("连接卡机"+devClock.getDevIp()+"失败");
        }

	}

	@Override
	public ApiResponseResult updateDataByLine(String line_ids) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(line_ids)){
			return ApiResponseResult.failure("无产线信息");
		}
		String[] line_id = line_ids.split(";");
		String msg = "";
		for(String lid:line_id){
			List<DevClock> ld = devClockDao.findByDelFlagAndLineIdAndDevType(0,Long.parseLong(lid),"上线机");
			if(ld.size() > 0){
				for(DevClock dc:ld){
					ApiResponseResult api = this.saveCardData(dc);
					if(!api.isResult()){
						msg += api.getMsg()+";";
					}
				}
			}else{
				return ApiResponseResult.failure("该产线未配置上线卡机!");
			}
			
		}
		
		return ApiResponseResult.success("同步数据成功！"+msg);
	}
	
	@Override 
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer fstatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        if(fstatus == null){
            return ApiResponseResult.failure("请正确设置状态！");
        }
        CardData o = cardDataDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("记录不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setFstatus(fstatus);
        cardDataDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
	}
}
