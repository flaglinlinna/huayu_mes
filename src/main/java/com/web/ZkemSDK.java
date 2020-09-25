package com.web;


/**
* 中控考勤机sdk函数调用类
* @author wangchao
*
*/

import com.jacob.activeX.ActiveXComponent;

/**
* @ClassName:${type_name}
* @Description:${todo}(考勤机连接测试)
* @author: ZHOUPAN
* @date ${date} ${time}
* @Copyright: 2018 www.zsplat.com Inc. All rights reserved.
* ${tags}
*/

public class ZkemSDK {

   private static ActiveXComponent zkem = new ActiveXComponent("zkemkeeper.ZKEM.1");

   /**
    * 链接考勤机
    *
    * @param address 考勤机地址
    * @param port    端口号
    * @return
    */
   public boolean connect(String address, int port) {
       boolean result = zkem.invoke("Connect_NET", address, port).getBoolean();
       return result;
   }

   /**
    * 断开考勤机链接
    */
   public void disConnect() {
       zkem.invoke("Disconnect");
   }

   public static void main(String[] args) {
       ZkemSDK sdk = new ZkemSDK();
       boolean  connFlag = sdk.connect("192.168.0.201", 4370);
       System.out.println("conn:"+connFlag);
   }
}


