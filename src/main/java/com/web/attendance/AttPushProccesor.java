package com.web.attendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    public final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IssueService issueService;

    private static Map<String, List<String>> cmdMap = new HashMap<>();

    private static String cmd_info = "C:1:CHECK";

    private static Map<String, Date> snMap = new HashMap<>();//存放卡机心跳请求时间
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
        //1.将卡机编号和心跳时间存到snMap中
        snMap.put(SN, new Date());

        //2.
        System.out.println(request.getParameter("SN"));
        StringBuffer sb = new StringBuffer("OK");
        //System.out.println(cmdMap);
        List<String> cmds =  new ArrayList<String>();//cmdMap.get(SN);

        // if(cmds==null){//等于空说明从来没加载过，如果初始化加载过了，此时应该不为Null 只是size为0
        if(cmdMap.get(SN)==null){
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
        String test1 = "C:8802:DATA UPDATE USERINFO PIN=515203	Name=黄婵秀	Pri=0	Passwd=	Grp=0";
        String cmd_tmp1 = "C:8802:DATA UPDATE FINGERTMP PIN=515203	FID=1	Pri=0	TMP=Ss1TUzIxAAADjo0ECAUHCc7QAAAvj5EBAABRg7MaoI7sAIdkxwARAInqaAAiAW0wLwAnjxhklQCAAExkUY6YAIFkbQC4AIvq1wBsAZcXFgCGjxhGggD9ALxkhY69AIFkoABZAITqzQBEAZFkzAEXjxlF/AA2AV9db45eAVxklgC7ARrqSABSAIZkSQDPjgdktQCtAENk8o4MAZBkgwCfAQfqigBfAXZkvwB4jghkeABvAExkTY52AAhkahFnguFxpwdeB3+HK/dQnUJrNYqJgpMBgg0rBI4Bvnkn+uT38YPVAYKA6IKhhMb/LYbvA+KL9XtjD9N1MXwb/vRyFAOqAFYK4/7N9eb59YrWAX+Bigy3hZ4PHVrj6DzLG6QWFgO984EQcB4CLYSiggKNlo0r/28EPXo4eiV1lZL1qqruZXaNkj9cMIPwg0SDMw1UAQ4BgoBziTUL7v9vAiA4xAI3k/cHAGc3BpBlCY5bOhDBXcEFwMOCAUo/DGLABVNdgwFGRwxWwK7+wc0MAE9TDMAE/2dPNsAQAC1ZxktfTsFRwEUHAOxhAE7+/8DBEQDgbP6wTP5QRf/+xgBK+YfCEQArfsb/WnFUwf9SXwrFc32dwltUwgoA4IgDysFXwgoAJVUAR05XwAQAJZjFSgSOpJ0J/m3AyQAmLgHBS8JDRskAISL1/VZEUwbFu6yH/3ATACKzOFPDycBFL8JDCMWEug3BbXALAIx5APywWE4HAIS+uP94TgQAj8sGwI8GAwnPgGnACwBK0ArXNv9QEgAzFfdJ20f/UFQRAIbf9E5KwDr+wFSkEQPG5/fAwf3/Ov/DT/5GwVoIAGXpivtsCACo7Qb7wDuGAaDug8DABsBYiwGB+4DCd8kAinMBwP8+wMA6/8OLAYH/en4Q1VMXfjzA/v/AOJ7+DY5K//TAO3M7RsKDEVEc7f8+/lf8ihHuIxNDDtVUIn5KwP//NcD6EBPdKO3/Pjgrj8ETnlY16cAzO4P/WIURy0GXkJYHdxOeVD7pTzPAOERJhRHKR5N1wgfCweIEENJIGlnVEFfG5f/A/v08/sFWiRGFWvf+/u4EEwxeXpAPEFat3vzF/P1AN8AL1dZpEMPAksTClsoQXeTfQCP+//6fwQCe3XIawgMQXHwQcQgQ0YSXwAWaq4IR6IWWwMAFgMVMowgQh4rJP/n4zVNCABJDAcQBAxIAhiIAABKAUg==";
        String cmd_tmp2 = "C:8802:DATA UPDATE FINGERTMP PIN=515203	FID=0	Pri=0	TMP=S7NTUzIxAAAC8PUECAUHCc7QAAAu8ZEBAABPgh0Zp/DcAHhk1gDLAZeU8QD0AJdkIwDA8BJkiwCaAMdkgvBJAeNk6QCKAZ6UrABvAXRkNgB48YxBtwDIAEJkwPAdAYlkfQAOAP2U1QChAI1kWACM8IFkbgCfAKpkjfB8AANk7ACjAZO+2gDgABdkTgDP8Htk8QDXAFdcX/ACAelg1QCLAZeUtQCCAIVkZgB08IBkgABqASRk5wOjAJp7VQ4LAbFwiIOtB0oFXIK8DBuKaYZFAzyX3PW+c0v/aQnkDhzjk5fz4auTYP2oA575WRMSc7MP4wYq9dv3iYRTizVgI36WC08IrIKnjswCDW9HcDZsc3WghsUDIQVs9pAMWIaJgzoESHwc9AP3nYJhfvT/bR92c8cL7fnLBFHRlAVpB4KF3P1zcUgCYgGmb1JPVIunASAtAQFnEz32AZ5sDMBKxgCCgALABQCpeMlRBvCgeYNtAwBUfgIPCgBoh/T/OVz/twcAm4uDwQTBwPUBo44JwDvDAJlgfMB4BgCOXf0tDwkAjp4DRJENAqS460EvPlfMALE0jYh3wg4Al9DmwzH/wEbBBMWCyg1FBgC7ygyA/wzwV8LpOy//OsDCygUAes1wdskA1C6RfIbAgwTF3tzjQw4A7vaWrGeRMYUPEF4B5Dop/LH+RMIPEGPB6f0P/y/9////oQsSJQqawouSb9UAWg7jwP3//v0H/USl/gsQ0xCTBZCVcQQQ2hMa/6wKEjEdjMHCw8IHw0T4EZslAP8ohQUSYyprxGsHEGUsBA8wwQMQmTCuwxLgYyrc//8nOy/9DsFvEBBfPR/Awgz+/v3//jKfCBJ2SeL//Pv/9AcSa0r3/P7+cNUQZaDbXv79//wF/f0PwW8KENRRVsDDMsfE/8LACNWkUPYucwQQ3FPSZQHgnFdrwwMQbnB4MwsQ8HaPVgXBxDPACRCKduc6+vwwWwYQnHz0OP8zokMAC0MBAMULR6I=";

        //String cmd_tmp3 = "C:6:DATA UPDATE FINGERTMP PIN=365662  FID=2	Size =1044	Valid=1	TMP=Sk1TUzIxAAADDg8ECAUHCc7QAAAvD5EBAAA+gjMZfQ7OADJdhgA8ACVqlACUAIdkXgAWD6ZkygAFAWRk1A6JAB1kXgCiAHBqdwBGAH9knAAlDnpklgDLAORkpQ60AJJkdwDTAS1qdAAiAbNk9AC8Dk9CWwA4AWhkvQ5nAI9kagCgAZFfgQDDAFFOoAACDzNUawCYAK5eog4kARpkiwDvAaZsvwA6AZdbVgBaD5BVoQBDANVkF+1IEooPrvgX9YGPJ/V2H085yIw2CYOBU/1Hie4OCZ3Gj9+b6vaCCcz1Xw4Cc6YH7/9EBRZv7dLZ8vyTSn2fDWJCmH8w/3aFlYfdEjIJ2uc8ETb/uv2yg84cGXmXg1ML5gNm5MThdSM9MiLCtAdFA3+BEudHHAr5GnrcdoKF1Y6s9rYNh32P8VMJowOo4tb97pOWgxqf83XaASApAQEFF54LAXYSDFwJxVIaDVXBQggAX+MAT/HBwQwAw0JVhWnPwFwFAKNCw/79zwcAdEOAbwUJA5VCjHDCcwXFokQe/k8KAHxIxjXDX/4HAHRJeqNxBQ5aZXfBgA3Fu2aeZnh1ZwoApmb+8f/AwD3+BsVcaH/BhBEAInQi//0zOUX//koDxdeIKsAPAJOTj1KAwc/BiP8UAGpTcZKGa8HAwsCROsMADnOX/f4JAFyZFEr+wEcWAOoWosLxwcF4hsN3ScBzGwFqnGmMwkVn/Jx0jRQAHJ0nwDtP/v3ARjtw0ACgvZKPxMHCw7LC/M3Bi5QKADIESWWKwcAOAHzEm8WehW+aFADpt2fB/M/Awv+Dk3WZCANwyFPGwXnBwgCaxB03wP8VAN6+1TD//sEu/j47SloIAZbPJP/BoQsDcNU0i1nDwAUDE2gFNMETEOXJnsPxW4bBxcjBBsPCzMAXEE0Yt4TB/fAo/fz9wPsFwfzxwFUcAOb/ZcBSjsHDwseXwb3EcmHDBhCxTYw4/1YHEdsfk/5PBD4JDofVK8HA/wd8wg0RoSkiwwXVXz4sg1JCABJDxAECDoUA2CwAANdFUQ4=";

        //cmds.add(test1);
        //cmds.add(cmd_tmp1);
        //cmds.add(cmd_tmp2);
        if(cmds!=null&&cmds.size()>0){
            sb.setLength(0);//如果有命令就不返回OK了
            cmds.stream().forEach(cmd->sb.append(cmd).append("\r\n\r\n"));

        }
        System.out.println("心跳命令为...."+sb);
        try {
            //cmdMap.get(SN).clear();//处理完以后立刻将集合清空，实际开发中应该是在/devicecmd这个请求里完成
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

    /**
     * 定时器-更新卡机在线状态
     * 说明：1.每5分钟执行一次
     * 2.首先从snMap筛选出5分钟内在线、离线的卡机
     * 3.然后执行更新在线状态的方法（包含在线、离线、长时间离线更新）
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void onLineTimer() {
        try{
            Date dateNow = new Date();
            List<String> onList = new ArrayList<>();//在线卡机编号集合
            List<String> offList = new ArrayList<>();//离线卡机编号集合

            //从snMap中筛选出在线卡机、离线卡机（此处以10秒为标准，超过10秒则视为离线）
            for(Map.Entry<String, Date> entry : snMap.entrySet()){
                String sn = entry.getKey();
                Date dateSn = entry.getValue();
                long sub = this.getSeconds(dateNow, dateSn);//计算相差秒数
                if(sub > 10){
                    //超过10秒，添加到离线卡机编号集合
                    offList.add(sn);
                }else{
                    //添加到在线卡机编号集合
                    onList.add(sn);
                }
            }

            boolean flag = issueService.updateIsOnLine(onList, offList, dateNow);
            if(flag){
                logger.info("AttPushProcessor类的onLineTimer定时器执行成功");
            }else{
                logger.error("AttPushProcessor类的onLineTimer定时器执行失败");
            }
            snMap.clear();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("AttPushProcessor类的onLineTimer定时器执行失败" + e);
        }
    }


    //计算相差秒数
    public long getSeconds(Date start, Date end) {
        try{
            long startTime = start.getTime();
            long endTime = end.getTime();
            long diff=(startTime - endTime)/1000;
            return diff;
        }catch (Exception e){
        }
        return 0;
    }
}
