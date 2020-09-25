package com.web.attendance;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import java.util.*;

/**
 * @ClassName:${type_name}
 * @Description:${todo}(连接、获取考勤机数据)
 * @author: ZHOUPAN
 * @date ${date} ${time}
 * @Copyright: 2018 www.zsplat.com Inc. All rights reserved.
 * ${tags}
 */
public class ZkemSDKUtils {

    //zkemkeeper.ZKEM.1 为zkemkeeper.dll 注册成功后 在注册表可以查看：HKEY_CLASSES_ROOT最下面
    private static ActiveXComponent zkem = new ActiveXComponent("zkemkeeper.ZKEM.1");

    /**
     * 连接考勤机
     *
     * @param address 考勤机地址
     * @param port    端口号
     * @return
     */
    public static boolean connect(String address, int port) {
        boolean result = zkem.invoke("Connect_NET", address, port).getBoolean();
        return result;
    }

    /**
     * 读取考勤记录到pc缓存。配合getGeneralLogData使用
     *
     * @return
     */
    public static boolean readGeneralLogData() {
        boolean result = zkem.invoke("ReadGeneralLogData", 1).getBoolean();
        return result;
    }
    
    /**
     * 读取指纹模板相关到pc缓存。配合ReadAllTemplate使用
     *
     * @return
     */
    public static boolean readAllTemplate() {
        boolean result = zkem.invoke("ReadAllTemplate", 1).getBoolean();
        return result;
    }

    /**
     * 读取该时间之后的最新考勤数据。 配合getGeneralLogData使用。//网上说有这个方法，但是我用的开发文档没有这个方法，也调用不到，我在controller中处理获取当天数据
     *
     * @param lastest
     * @return
     */
    public static boolean readLastestLogData(Date lastest) {
        boolean result = zkem.invoke("ReadLastestLogData", 2018 - 07 - 24).getBoolean();
        return result;
    }

    /**
     * 获取缓存中的考勤数据。配合readGeneralLogData / readLastestLogData使用。
     *
     * @return 返回的map中，包含以下键值：
     * "EnrollNumber"   人员编号
     * "Time"           考勤时间串，格式: yyyy-MM-dd HH:mm:ss
     * "VerifyMode"
     * "InOutMode"
     * "Year"          考勤时间：年
     * "Month"         考勤时间：月
     * "Day"           考勤时间：日
     * "Hour"            考勤时间：时
     * "Minute"        考勤时间：分
     * "Second"        考勤时间：秒
     */
    public static List<Map<String, Object>> getGeneralLogData() {
        Variant dwMachineNumber = new Variant(1, true);//机器号

        Variant dwEnrollNumber = new Variant("", true);
        Variant dwVerifyMode = new Variant(0, true);
        Variant dwInOutMode = new Variant(0, true);
        Variant dwYear = new Variant(0, true);
        Variant dwMonth = new Variant(0, true);
        Variant dwDay = new Variant(0, true);
        Variant dwHour = new Variant(0, true);
        Variant dwMinute = new Variant(0, true);
        Variant dwSecond = new Variant(0, true);
        Variant dwWorkCode = new Variant(0, true);
        List<Map<String, Object>> strList = new ArrayList<Map<String, Object>>();
        boolean newresult = false;
        do {
            Variant vResult = Dispatch.call(zkem, "SSR_GetGeneralLogData", dwMachineNumber, dwEnrollNumber, dwVerifyMode, dwInOutMode, dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond, dwWorkCode);
            newresult = vResult.getBoolean();
            if (newresult) {
                String enrollNumber = dwEnrollNumber.getStringRef();

                //如果没有编号，则跳过。
                if (enrollNumber == null || enrollNumber.trim().length() == 0)
                    continue;
                String month = dwMonth.getIntRef() + "";
                String day = dwDay.getIntRef() + "";
                if (dwMonth.getIntRef() < 10) {
                    month = "0" + dwMonth.getIntRef();
                }
                if (dwDay.getIntRef() < 10) {
                    day = "0" + dwDay.getIntRef();
                }
                String validDate = dwYear.getIntRef() + "-" + month + "-" + day;
                String currentDate = "2020-09-25";//DateUtils.getCurrentTime("yyyy-MM-dd");
                if (currentDate.equals(validDate)) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    //Map<String, Object> user = getUserInfoByNumber(enrollNumber);
                    m.put("EnrollNumber", enrollNumber);
                    m.put("Time", dwYear.getIntRef() + "-" + dwMonth.getIntRef() + "-" + dwDay.getIntRef() + " " + dwHour.getIntRef() + ":" + dwMinute.getIntRef() + ":" + dwSecond.getIntRef());
                    m.put("VerifyMode", dwVerifyMode.getIntRef());
                    m.put("InOutMode", dwInOutMode.getIntRef());
                    m.put("Year", dwYear.getIntRef());
                    m.put("Month", dwMonth.getIntRef());
                    m.put("Day", dwDay.getIntRef());
                    m.put("Hour", dwHour.getIntRef());
                    m.put("Minute", dwMinute.getIntRef());
                    m.put("Second", dwSecond.getIntRef());
                    strList.add(m);
                }
            }
        } while (newresult == true);
        return strList;
    }

    /**
     * 获取用户信息
     *
     * @return 返回的Map中，包含以下键值:
     * "EnrollNumber"  人员编号
     * "Name"          人员姓名
     * "Password"      人员密码
     * "Privilege"
     * "Enabled"       是否启用
     */
    public static List<Map<String, Object>> getUserInfo() {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //将用户数据读入缓存中。
        boolean result = zkem.invoke("ReadAllUserID", 1).getBoolean();

        Variant v0 = new Variant(1);
        Variant sdwEnrollNumber = new Variant("", true);
        Variant sName = new Variant("", true);
        Variant sPassword = new Variant("", true);
        Variant iPrivilege = new Variant(0, true);
        Variant bEnabled = new Variant(false, true);

        while (result) {
            //从缓存中读取一条条的用户数据
            result = zkem.invoke("SSR_GetAllUserInfo", v0, sdwEnrollNumber, sName, sPassword, iPrivilege, bEnabled).getBoolean();

            //如果没有编号，跳过。
            String enrollNumber = sdwEnrollNumber.getStringRef();
            if (enrollNumber == null || enrollNumber.trim().length() == 0)
                continue;

            //由于名字后面会产生乱码，所以这里采用了截取字符串的办法把后面的乱码去掉了，以后有待考察更好的办法。
            //只支持2位、3位、4位长度的中文名字。
            String name = sName.getStringRef();
            if (sName.getStringRef().length() > 4) {
                name = sName.getStringRef().substring(0, 4);
            }
            //如果没有名字，跳过。
           /* if (name.trim().length() == 0)
                continue;*/
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("EnrollNumber", enrollNumber);
            m.put("Name", name);
            m.put("Password", sPassword.getStringRef());
            m.put("Privilege", iPrivilege.getIntRef());
            m.put("Enabled", bEnabled.getBooleanRef());

            resultList.add(m);
        }
        return resultList;
    }


    /**
     * 设置用户信息
     *
     * @param number
     * @param name
     * @param password
     * @param isPrivilege  0為普通用戶,3為管理員;
     * @param enabled 是否啟用
     * @return
     */
    public static boolean setUserInfo(String number, String name, String password, int isPrivilege, boolean enabled) {
        Variant v0 = new Variant(1);
        Variant sdwEnrollNumber = new Variant(number, true);
        Variant sName = new Variant(name, true);
        Variant sPassword = new Variant(password, true);
        Variant iPrivilege = new Variant(isPrivilege, true);
        Variant bEnabled = new Variant(enabled, true);

        boolean result = zkem.invoke("SSR_SetUserInfo", v0, sdwEnrollNumber, sName, sPassword, iPrivilege, bEnabled).getBoolean();
        return result;
    }
    /**
     * 删除用户;
     */
    public static Boolean delectUserById(String dwEnrollNumber){
        Variant v0 = new Variant(1);
        Variant sdwEnrollNumber = new Variant(dwEnrollNumber, true);
        /**
         * sdwBackupNumber：
         * 一般范围为 0-9，同时会查询该用户是否还有其他指纹和密码，如都没有，则删除该用户
         * 当为 10 是代表删除的是密码，同时会查询该用户是否有指纹数据，如没有，则删除该用户
         * 11 和 13 是代表删除该用户所有指纹数据，
         * 12 代表删除该用户（包括所有指纹和卡号、密码数据）
         */
        Variant sdwBackupNumber = new Variant(12);
        /**
         * 删除登记数据，和 SSR_DeleteEnrollData 不同的是删除所有指纹数据可用参数 13 实现，该函数具有更高效率
         */
        return zkem.invoke("SSR_DeleteEnrollDataExt", v0, sdwEnrollNumber, sdwBackupNumber).getBoolean();
    }

    /**
     * 获取用户信息
     *
     * @param number 考勤号码
     * @return
     */
    public static Map<String, Object> getUserInfoByNumber(String number) {
        Variant v0 = new Variant(1);
        Variant sdwEnrollNumber = new Variant(number, true);
        Variant sName = new Variant("", true);
        Variant sPassword = new Variant("", true);
        Variant iPrivilege = new Variant(0, true);
        Variant bEnabled = new Variant(false, true);
        boolean result = zkem.invoke("SSR_GetUserInfo", v0, sdwEnrollNumber, sName, sPassword, iPrivilege, bEnabled).getBoolean();
        if (result) {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("EnrollNumber", number);
            m.put("Name", sName.getStringRef());
            m.put("Password", sPassword.getStringRef());
            m.put("Privilege", iPrivilege.getIntRef());
            m.put("Enabled", bEnabled.getBooleanRef());
            return m;
        }
        return null;
    }
    
    /**
     * 获取用户信息
     *
     * @param number 考勤号码
     * @return
     */
    public static Map<String, Object> getUserTmpStr() {
    	 
    	int isMachineNumber = 1;
    	
    	
      /*  boolean result = //zkem.invoke("SSR_GetUserTmpStr", v0, sdwEnrollNumber, sName, sPassword, iPrivilege, bEnabled).getBoolean();
        		zkem.invoke("SSR_GetUserTmpStr",  sdwEnrollNumber, "1").getBean();
        */
        Variant dwMachineNumber = new Variant(0, true);//机器号

        Variant dwEnrollNumber = new Variant("", true);

        
        Variant TmpData = new Variant("", true);
        Variant TmpLength = new Variant(0, true);
        
       Dispatch.call(zkem, "EnableDevice", isMachineNumber, false);//使机器至于不可以状态
    	
       boolean result = zkem.invoke("ReadAllUserID", 1).getBoolean();

       Variant v0 = new Variant(1);
       Variant sName = new Variant("", true);
       Variant sPassword = new Variant("", true);
       Variant iPrivilege = new Variant(0, true);
       Variant bEnabled = new Variant(false, true);

       while (result) {
           //从缓存中读取一条条的用户数据
           result = zkem.invoke("SSR_GetAllUserInfo", v0, dwEnrollNumber, sName, sPassword, iPrivilege, bEnabled).getBoolean();
          
           System.out.println(dwEnrollNumber.getStringRef());
           
           for(int i=0;i<10;i++){
           	Variant vResult = Dispatch.call(zkem, "SSR_GetUserTmpStr", dwMachineNumber, dwEnrollNumber,i,TmpData,TmpLength);
               
               //Variant vResult = Dispatch.call(zkem, "SSR_GetUserTmp", dwMachineNumber, dwEnrollNumber,1,TmpData,TmpLength);
               
               
               boolean newresult = vResult.getBoolean();
               
               if(newresult){
            	   System.out.println("i:"+i+";newresult:"+newresult);
            	   System.out.println("TmpData:"+TmpData.getStringRef());
            	   System.out.println("TmpLength:"+TmpLength.getIntRef());
               }
               
               
               
           }
       }
    	

        return null;
    }
    
    /**
     * 获取用户信息
     *
     * @param number 考勤号码
     * @return
     */
    public static Map<String, Object> setUserTmpStr() {
    	 
        Variant dwMachineNumber = new Variant(0, true);//机器号

        Variant dwEnrollNumber = new Variant("3", true);
        
        
        
        Variant TmpData = new Variant("Ss9TUzIxAAADjJEECAUHCc7QAAAbjWkBAAAAg7EakYxlAJcPfQC1ABKDWACDAIwNBwCKjJoPfwCNAMsPPYyXANAMMABYAMGCRwCdAPMOUQCnjIMP5gCkAOgPIoynAL8OfwBjAASDLgCoANMOJgC7jKsPzADEAOoPm4zTADEP8wAeACyDxgDeAK8PGADjjK4PVgDiAIgPZ4zjAEgP3wA0ADSDiAALAcEPnQAXjccP2gAXAfsPnowhAUkOVYr7iwRzWH6tB96Gi5hdMAOCAnNTGcrytoYw/5aMzN/YEA5yAfOt9CkE6ScOvvwifX2idPpQmgmzAu6Xyexo/K18dIU5+qfZtRM2mIgGnX1Ff8N+PguYg36AD7CehPyhyIKy+AKGEAKaD+94DXhVAgyCNohyiVOPufxGhSyWBIgqCOaIJHs4cWJ6twV7eUKBCYYGkveT2Yv2D8ALRT87ASA6xAIxl+AGAMc4HPPBB4zVOCTA/wnFwD+swUzBVQoAcEEZslpgDAClQtb+T+xUAwCTRzAGBQPGSEPChgwARlAd+MErwSsDAIRsMnAHADBYScUE//6JAVdePcLNBAgD4GEA/MBH/8AAMO0x/8D9BwBsYxTMWAwAZGr6ODY9uQIAY3AA+9YAgv0WbTVMNluQBAOncjfB+xYAmIL5tDD//lT/UIFGAIwlhT3BDgBHjgVwTv7A/cDB6gQDmpRAPQoAeV19wUzDfJENAEte8DC9wf07CgB4Wnp0C4gLAHuldwSAwkzDhgYAmKbTKcOJAeqmK1URxTOkbsH9/zD//u7+V4kB5agwwVrXAIElCP/9/v7/hEdXuwoA/7CpwARaeE0NAJ5MJMKW//xN/kYDABXFhf8HjMzHLVoMAFzVKHL+MsD/wDbOAHpVY8hFSkAExYHZpSMLAHzdNPNAw6YQAFne3P45/Ph3/8D8wP/+B/80gwFm3t76+j8hWMj/BQDy3jAFPgmMed9DaFMuzwDKbjU+/8BGCcVS4NvFYnIEAGAhSW+FAVfmTHn+gQoD6+dGwGDC/pwHA2/1N8BT/wbF3vaxcsADEBgHjMIFnNkbQFTABNXdGLFOBhCcJkkF/yiVEbUrw2vCScLAT8DEwsHCwAbAwXPDZR8QHy4GREO5Mif9/f/BOP/8TP7AQf8FEHIzTNsdEDU/wEaRMf1x//z8//3/O/7Cc/7B/jdSQsULQI0BAAtFUgAAAAAAAAA=", true);
        
        Variant vResult = Dispatch.call(zkem, "SSR_SetUserTmpStr", dwMachineNumber, dwEnrollNumber,6,TmpData);
        
        boolean newresult = vResult.getBoolean();
        
        System.out.println("newresult:"+newresult);
        
        /*if(newresult){
     	   System.out.println("i:"+i+";newresult:"+newresult);
     	   System.out.println("TmpData:"+TmpData.getStringRef());
     	   System.out.println("TmpLength:"+TmpLength.getIntRef());
        }*/
    
    	

        return null;
    }
    
    /**
     * 查詢所有/指定ID的打卡信息;
     * @param userNumber
     * @return
     */
    public static List<Map<String, Object>> getUserOneDayInfo(Object userNumber){
        ZkemSDKUtils sdk = new ZkemSDKUtils();
        List<Map<String, Object>> strList = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> generalLogDataAll = ZkemSDKUtils.getGeneralLogData();
            for (int i = 0; i < generalLogDataAll.size(); i++) {
                //System.out.println(generalLogDataAll.get(i));
                String Year =String.valueOf(generalLogDataAll.get(i).get("Year"));
                String Hour = String.valueOf(generalLogDataAll.get(i).get("Hour"));
                String InOutMode = String.valueOf(generalLogDataAll.get(i).get("InOutMode"));
                String Time = String.valueOf(generalLogDataAll.get(i).get("Time"));
                String Second = String.valueOf(generalLogDataAll.get(i).get("Second"));
                String Minute = String.valueOf(generalLogDataAll.get(i).get("Minute"));
                String EnrollNumber = String.valueOf(generalLogDataAll.get(i).get("EnrollNumber"));
                String Day = String.valueOf(generalLogDataAll.get(i).get("Day"));
                String Month = String.valueOf(generalLogDataAll.get(i).get("Month"));
                String VerifyMode = String.valueOf(generalLogDataAll.get(i).get("VerifyMode"));
                
                if(EnrollNumber.equals(userNumber)&&userNumber!=null){
                	strList.add(generalLogDataAll.get(i));
                }
            }
            return strList;

    }

    public static void main(String[] args) {
        ZkemSDKUtils sdk = new ZkemSDKUtils();
        Map<String, Object> map = new HashMap<String, Object>();
        boolean connFlag = sdk.connect("192.168.0.201", 4370);
        System.out.println(connFlag);
        if (connFlag) {
            /*boolean flag = sdk.readGeneralLogData();
            System.out.println("flag:" + flag);*/
           
            /*List<Map<String, Object>> strList = sdk.getGeneralLogData();
            map.put("strList", strList);
            System.out.println("flag:" + flag);
            System.out.println(strList.toString());*/
           /*List<Map<String, Object>> userList = sdk.getUserInfo();//获取所有人员信息
            System.out.println(userList.toString());*/
            //[{Enabled=true, EnrollNumber=2, Privilege=0, Name=, Password=}]
            /*boolean f= sdk.setUserInfo("3", "test1", "", 0, true);//新增人员,如果编号一样则修改信息
            System.out.println(f);*/
            /*boolean f_d= sdk.delectUserById("1");
            System.out.println(f_d);*/
        	/*boolean flag = sdk.readAllTemplate();
            System.out.println("flag:" + flag);*/
           
            //sdk.getUserTmpStr();
        	
        	sdk.setUserTmpStr();
            
        }
    }

}
