package com.web.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.web.basic.dao.EmployeeDao;
import com.web.basic.entity.Employee;
import com.web.produce.dao.CardDataDao;
import com.web.produce.entity.CardData;
import com.web.produce.entity.DevClock;

public class AttenDanceUtils {

	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	CardDataDao cardDataDao;

	/**
	 * 根据卡机获取卡机上的考勤记录
	 * @param DevClock
	 */
	public List<CardData> getCardData(DevClock devClock){
		List<CardData> lc = new ArrayList<CardData>();
		ZkemSDKUtils sdk = new ZkemSDKUtils();
        boolean connFlag = sdk.connect(devClock.getDevIp(), 4370);
        System.out.println(connFlag);
        if (connFlag) {
            boolean flag = sdk.readGeneralLogData();
            System.out.println("flag:" + flag);

            List<Map<String, Object>> strList = sdk.getGeneralLogData();
            System.out.println(strList.toString());

            for(Map<String, Object> map:strList){
            	//先判断员工信息是否存在
            	String user_code = map.get("EnrollNumber")==null?"":map.get("EnrollNumber").toString();//获取用户账号
            	List<Employee> le = employeeDao.findByDelFlagAndEmpCode(0,user_code);
            	if(le.size() == 0){
            		continue ;
            	}
            	//判断信息是否已经保存过
            	String carDate=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String cardTime=LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                System.out.println("当前时间为:"+carDate);
                System.out.println("当前时间为:"+cardTime);
                List<CardData> cc = cardDataDao.findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTime(0, le.get(0).getId(), devClock.getId(),
                		carDate, cardTime);
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
        }
		return lc;
	}

	/**
	 * 根据卡机删除卡机上的所有考勤记录
	 * @param DevClock
	 * @param time(YYYY-MM-DD hh:mm:ss )
	 */
	public boolean delCardData(DevClock devClock,String time){
		List<CardData> lc = new ArrayList<CardData>();
		ZkemSDKUtils sdk = new ZkemSDKUtils();
        boolean connFlag = sdk.connect(devClock.getDevIp(), 4370);
        System.out.println(connFlag);
        if (connFlag) {
            return sdk.deleteGeneralLogData(time);
        }
		return connFlag;
	}


}
