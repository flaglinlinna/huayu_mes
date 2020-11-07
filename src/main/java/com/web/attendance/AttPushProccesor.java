package com.web.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.web.produce.service.IssueService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 所有的设备请求都会在url参数里携带SN，这是设备序列号(serial number的缩写)，每个设备唯一标识
 */
@Controller
@RequestMapping("/iclock")//http://ip:port/iclock/cdata?options=all&lanuage=83&pushver=2.2
public class AttPushProccesor {
	
	@Autowired
	 private IssueService issueService;

    private static Map<String, List<String>> cmdMap = new HashMap<>();
    
    private static String cmd_info = "C:1:CHECK";
    /**
     * 1，设备通完电以后第一个发送到后台的请求
     * 格式为 /iclock/cdata?options=all&language=xxxx&pushver=xxxx
     */
    @RequestMapping(value="/cdata",params = {"options","language","pushver"},method = RequestMethod.GET)
    public void init(String SN, String options, String language, String pushver, @RequestParam(required = false) String PushOptionsFlag, HttpServletResponse response){
        System.out.println("options="+options+"....language="+language+"....pushver="+pushver);
        try {
            System.out.println("考勤机初始化请求进来了......");
            loadCmd(SN);//加载命令
            String initOptions = initOptions(SN,PushOptionsFlag);
            System.out.println("返回给考勤机的初始化参数为...."+initOptions);
            response.getWriter().write(initOptions);//返回成功以后设备会发送心跳请求
            //response.getWriter().write("UNKNOWN DEVICE");当返回这个的时候，设备会每2秒重新发本请求，直到返回OK为止
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 2，心跳请求，会从服务器拿到命令返回   给设备
     */
    @RequestMapping("/getrequest")
    public void heartBeat(String SN, HttpServletResponse response, HttpServletRequest request){
        System.out.println(request.getParameter("SN"));
       StringBuffer sb = new StringBuffer("OK");
       //System.out.println(cmdMap);
       List<String> cmds =  cmdMap.get(SN);
       
        if(cmds==null){//等于空说明从来没加载过，如果初始化加载过了，此时应该不为Null 只是size为0
            loadCmd(SN);
            cmds = cmdMap.get(SN);
        }
       List<String> undo_cmds = issueService.getCmdBySn(SN);
      // cmds.add(cmd_tmp);
       for(String param:undo_cmds){
    	   //String cmd_test = "C:"+param[0]+":DATA UPDATE USERINFO PIN="+param[1]+"	Name="+param[2]+"	Pri=0	Passwd=	Grp=0";
    	   System.out.println(param);
    	   cmds.add(param);
       }
       /*String test1 = "C:3:DATA UPDATE USERINFO PIN=514259	Name=张小碎	Pri=0	Passwd=	Grp=0";
       String cmd_tmp = "C:4:DATA UPDATE FINGERTMP PIN=514259	fid=0	Pri=0	TMP=TctTUzIxAAAEiIwECAUHCc7QAAAoiZEBAABkhLUugIgJAUdkWAA5AFTsegDKAPlkUAA2iclkrAApAZ5kMYgZAVhkZQBwAFjshgBSAedkWQCriAVkPgDEAJlki4ijAGxktACWAdfsgwBrAflkEwC2iJRkmgBrADJkjohaAPlkmgA5ADHsmAAiAcRkmwDWiFlkigA4AXlkOogUAdJktgDxAVHsSAA6AVNkFwD4iDhknQBQAbBkuYhFAVJkOwB/AOXsUABeAVxkkgBmidVkWABxATFkhYhlAGpkfQDhAUTshADMAG9kngAuiUVkVwDOABtkPIjrAFlkbgB3AOjswgDTACJk7AD5iNtkZgBVAe1k0ojjAKFkzwDxAU/skgBnAXlk6gCqiN5VcgB7AblkoYhlAHNkJRZAC1oMk4HG+fb7VY8xh/+gJA+FBGxuTYC0mZWWHIofgFKOGXJ5g24EUHbB+uDyHZnalScP3PfMfzWDNWlHgVp3hIA1YnKAuIDt9lAWQnYv9KqTdA8oAhWP6JNsBpEDuPKOcn/czPzJ4RQLvH7y/NtgtH9sfgnXIH6X+M4ZzPxdBC0O1BJiceOCDZv6Gw4GjRoQpl3+3fxAgBWC1YdBBLoF+YpR4RQ2RHrp4FStTag8mZ17+HPkdx5xaIZVez1/XXuyDe9tofI2BRN1vAIwh3mHhYL7Cep2CZLB97p9vIIyBpPrgYLWhA8Iud3Izq40fYLga6eEmPq1+sLxuIC1HtgGwQDJh3oB4Q6Ui6apdH9Yf4qB7e71PK895TsFij0aTgQAYphiQ4EBhVRtccAEZgCIjF/3NQkAuGFp8GrACwCjYr+QduEIAH9mZ8A6wmBKDACVbnFwBMDFSf7BVwQAnav9K4YBznqG/8MFw8RJwHhkDgDSQYzFBMDAwnBZBsWUpXn8IwMAjKK1wheI1JCXl3x+scP5SV0GAI6mawWGA4ieq/39/jDOAJM4bHx+wWYUxdylFsOSiXFxSrMLBOGw4jPAIyzNAJs5/Cf+MAsAEbOX9Zf/iQUAYXBgxeEKAGW4Xm0GNzqMATbAWv//wQA4S1tKCwB/xSz++nYmMQgAhsku/vl1Kw0Ad81gB2XHd1RHCgCCz6KJxUn+exMAvs9Ww8dKmINiwcL76gkE0dRcwmVUBsVf0t9jwAYAxNfnNcSNATrtVv/ABQ8EWfaww8DEqEbBxvsEAJ77KfrvCQTQ/lNzS8AFxdH7tcBCAwDW//LAAJiXAUyXDRBAAtN0/Pb8/f3+Bf76dAgQgApMdTvFzEAEEIQKRofAEDiTUUwEEDUcklsAmKosVioHEJ0uQkkr/QUQXS6G/8V3EhC4MMyIBsLDTcLEwsDBwQfA+08MEIw1vcE7/fhy9Pr9//zAwxC2sFL+wCAFEH04VLsCEM85Sf7BEEG2UkMFEEo+jPw5nRG8QsxvjAHEqgHDZsAIELGSXsR1/P0wAxBlnTHDlxFeWsbAwvf/MXb8/Pz8//w6/8Sj/////v/8BQMU22FW/VJCANxDBYoAAKIBAkPFAQQCAD7TAAAZgFI=";
       cmds.add(test1);
       cmds.add(cmd_tmp);*/
       if(cmds!=null&&cmds.size()>0){
           sb.setLength(0);//如果有命令就不返回OK了
          cmds.stream().forEach(cmd->sb.append(cmd).append("\r\n\r\n"));
          
       }
       System.out.println("心跳命令为...."+sb);
        try {
            cmdMap.get(SN).clear();//处理完以后立刻将集合清空，实际开发中应该是在/devicecmd这个请求里完成
            response.setCharacterEncoding("gbk");
            System.out.println("返回的命令...."+sb.toString());
            response.getWriter().write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 3，候补心跳请求，正常情况下设备不发此请求，有大量数据上传的时候，不发上面的心跳，发这个请求，
     * 这个请求，服务器只能返回OK，不可以返回命令
     */
    @RequestMapping("/ping")
    public void ping(HttpServletResponse response){
        System.out.println("考勤机心跳请求进来了......ping"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
        try {
            response.getWriter().write("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 4，设备端处理完命令以后会发送该请求，告诉服务器命令的处理结果
     */
    @RequestMapping("/devicecmd")
    public void handleCmd(String SN,@RequestBody  String data,HttpServletResponse response){
        System.out.println("设备处理完命令以后的返回结果..."+data);
        try {
        	issueService.updateDevicecmd(SN, data);
            response.getWriter().write("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping("/fdata")
    public String handleFdata(){
        System.out.println("进入.......................................");
        return "OK";
    }

    @RequestMapping(value = "/cdata")
    public void handleRtData(HttpServletRequest request,HttpServletResponse response,String SN,String table) throws UnsupportedEncodingException {
        //System.out.println("设备上传上来的数据...."+data+"....table..."+table);
        request.setCharacterEncoding("gbk");
        String data = "";

            System.out.println("设备的实时记录是......."+data+"....table名字为:"+table);

        ByteArrayOutputStream bos = null;
        byte[] b= new byte[1024];
        try {
            InputStream is = request.getInputStream();
            bos = new ByteArrayOutputStream();
            int len = 0;
            while((len=is.read(b))!=-1){
                bos.write(b,0,len);
            }
            data = new String(bos.toByteArray(),"gbk");
        } catch (IOException e) {
            e.printStackTrace();
        }

            System.out.println("设备的实时记录是......."+data);
            
            if("ATTLOG".equals(table)){
            	//保存考勤记录-fyx
            	issueService.uploadAttLog(SN, data);
            }
        try {
            response.getWriter().write("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCmd(String sn){
        try{
            /*List<String> cmdList = new ArrayList<>();
            File file = ResourceUtils.getFile("classpath:cmd.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String cmd = "";
            while((cmd = br.readLine())!=null){
                if(!cmd.startsWith("#")){
                    System.out.println("加载进内存的命令是...."+cmd);
                    cmdList.add(cmd);
                }
            }
            cmdMap.put(sn,cmdList);*/
        	
        	List<String> cmdList = new ArrayList<>();
        	cmdList.add(cmd_info);
        	cmdMap.put(sn,cmdList);
        	
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 设备通电以后连接到服务器，需要返回的初始化参数
     * @param sn
     * @param PushOptionsFlag
     * @return
     */
    private String initOptions(String sn,String PushOptionsFlag) {
        StringBuffer devOptions = new StringBuffer();
        devOptions.append("GET OPTION FROM: "+sn);
        // + "\nStamp=" + devInfo.devInfoJson.getString("Stamp")
        devOptions.append("\nATTLOGStamp=0");
        devOptions.append("\nOPERLOGStamp=0");
        devOptions.append("\nBIODATAStamp=0");
        devOptions.append("\nATTPHOTOStamp=0");
        devOptions.append("\nErrorDelay=10");//断网重连
        devOptions.append("\nDelay=1");//心跳间隔
        devOptions.append("\nTimeZone=8");//时区
        devOptions.append("\nRealtime=1");//实时上传
        devOptions.append("\nServerVer=3.0.1");//这个必须填写
//        1 考勤记录
//        2 操作日志
//        3 考勤照片
//        4 登记新指纹
//        5 登记新用户
//        6 指纹图片
//        7 修改用户信息
//        8 修改指纹
//        9 新登记人脸
//        10 用户照片
//        11 工作号码
//        12 比对照片
        devOptions.append("\nTransFlag=100000000000");//  1-12二进制位 分别代表以上含义
        System.out.println("PushOptionsFlag============="+PushOptionsFlag);
        if (PushOptionsFlag!=null&&PushOptionsFlag.equals("1"))
        {
            // 支持参数单独获取的才要把需要获取的参数回传给设备 modifeid by max 20170926
            devOptions.append("\nPushOptions=RegDeviceType,FingerFunOn,FaceFunOn,FPVersion,FaceVersion,NetworkType,HardwareId3,HardwareId5,HardwareId56,LicenseStatus3,LicenseStatus5,LicenseStatus56");
        }
        devOptions.append("\n");
        return devOptions.toString();
    }


}
