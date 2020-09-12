package com.web.po;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ApiUtil {
	/* Open API APP Consumer Key */
    private final static String key = "nfgULChVuii_oUJWlX86AboTEnUa";//代码执行过程中换成真实的key值
    /* Open API APP Consumer Secret */
    private final static String secury = "PGburWMuY7Q5mkEGhmemPtiSibYa";//代码执行过程中换成真实的value值
    /* SOAP Service URL */
    private final static String tokenRestURL = "https://api-beta.huawei.com:443/oauth2/token";

    public static void main(String[] args) throws Exception {

    	 StringBuilder sb = new StringBuilder();
         // sb.append(" {  student:{  \"age\": 29,    \"email\": \"openapi9@huawei.com\", \"name\": \"openAPI9\",    \"phone\": 13838457159,    \"sex\": 2 }}");
         /*sb.append("{\n" +
                 "    \"poSubType\": \"P\",\n" +
                 "    \"shipmentStatus\": \"all\",\n" +
                 "    \"poStatus\": \"before_signe_back\",\n" +
                 "    \"colTaskOrPoStatus\": \"huaweiPublishOrder\",\n" +
                 "    \"statusType\": \"COL_TASK_STATUS\",\n" +
                 "    \"includeVCICA\": -1" +
                 "}");*/
         sb.append("{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"signed_back\",\"colTaskOrPoStatus\":\"all\",\"statusType\":\"COL_TASK_STATUS\"}");

    	System.out.println(getList("https://api-beta.huawei.com:443/service/esupplier/findPoLineList/1.0.0/20/1","{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"before_signe_back\",\"colTaskOrPoStatus\":\"all\",\"statusType\":\"COL_TASK_STATUS\"}"));
       /* String token = "";
        String accessTokenOfRest = getAccessTokenOfRest(tokenRestURL, key, secury);
        System.out.println(accessTokenOfRest + "\n");
        Map<String, String> map = jsonToMap(accessTokenOfRest);
        for (Map.Entry<String, String> element : map.entrySet()) {
            System.out.println(element.getKey() + ":" + element.getValue());
            if ("access_token".equals(element.getKey())) {
                token = element.getValue();
            }
        }

        

        HttpURLConnection connection = getConnection("https://api-beta.huawei.com:443/service/esupplier/findProductionPOBoardData/1.0.0", token);
        connection.setRequestMethod("POST");// 请求方式
        // 此服务支持两种格式提交数据，一种是JSON，一种是XML
        connection.setRequestProperty("Content-Type", "application/json");
        //connection.setRequestProperty("Content-Type", "application/xml");
        StringBuilder sb = new StringBuilder();
        // sb.append(" {  student:{  \"age\": 29,    \"email\": \"openapi9@huawei.com\", \"name\": \"openAPI9\",    \"phone\": 13838457159,    \"sex\": 2 }}");
        sb.append("{\n" +
                "    \"poSubType\": \"P\",\n" +
                "    \"shipmentStatus\": \"all\",\n" +
                "    \"poStatus\": \"before_signe_back\",\n" +
                "    \"colTaskOrPoStatus\": \"huaweiPublishOrder\",\n" +
                "    \"statusType\": \"COL_TASK_STATUS\",\n" +
                "    \"includeVCICA\": -1" +
                "}");

        OutputStream output = connection.getOutputStream();
        output.write(sb.toString().getBytes("UTF-8"));
        output.flush();
        output.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
        connection.disconnect();*/
    }
    
    public static String getList(String functions,String param) throws Exception{
    	String token = "";
        String accessTokenOfRest = getAccessTokenOfRest(tokenRestURL, key, secury);
        System.out.println(accessTokenOfRest + "\n");
        Map<String, String> map = jsonToMap(accessTokenOfRest);
        for (Map.Entry<String, String> element : map.entrySet()) {
            System.out.println(element.getKey() + ":" + element.getValue());
            if ("access_token".equals(element.getKey())) {
                token = element.getValue();
            }
        }

        

        HttpURLConnection connection = getConnection(functions,token);//getConnection("https://api-beta.huawei.com:443/service/esupplier/findProductionPOBoardData/1.0.0", token);
        connection.setRequestMethod("POST");// 请求方式
        // 此服务支持两种格式提交数据，一种是JSON，一种是XML
        connection.setRequestProperty("Content-Type", "application/json");
        //connection.setRequestProperty("Content-Type", "application/xml");
        StringBuilder sb = new StringBuilder();
        // sb.append(" {  student:{  \"age\": 29,    \"email\": \"openapi9@huawei.com\", \"name\": \"openAPI9\",    \"phone\": 13838457159,    \"sex\": 2 }}");
        /*sb.append("{\n" +
                "    \"poSubType\": \"P\",\n" +
                "    \"shipmentStatus\": \"all\",\n" +
                "    \"poStatus\": \"before_signe_back\",\n" +
                "    \"colTaskOrPoStatus\": \"huaweiPublishOrder\",\n" +
                "    \"statusType\": \"COL_TASK_STATUS\",\n" +
                "    \"includeVCICA\": -1" +
                "}");*/
        sb.append("{\"poSubType\":\"P\",\"shipmentStatus\":\"all\",\"poStatus\":\"signed_back\",\"colTaskOrPoStatus\":\"all\",\"statusType\":\"COL_TASK_STATUS\"}");

        OutputStream output = connection.getOutputStream();
        //output.write(sb.toString().getBytes("UTF-8"));
        output.write(param.getBytes("UTF-8"));
        output.flush();
        output.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;String a="";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            a = line;
        }
        reader.close();
        connection.disconnect();
        System.out.println("a:"+a);
		return a;
    }

    private static HttpURLConnection getConnection(String url, String token) throws IOException {
        URL postUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "UTF-8");

        connection.addRequestProperty("Authorization", "Bearer " + token);// 有权限认证的服务必须加上认证
        return connection;
    }

    /**
     * 基于rest协议获取访问token
     *
     * @param tokenUrl
     * @param key
     * @param secret
     * @return
     */
    public static String getAccessTokenOfRest(String restTokenUrl, String key, String secret) {
        String result = "";
        URL postUrl = null;
        OutputStream output = null;
        BufferedReader reader = null;
        InputStreamReader isr = null;
        HttpsURLConnection connection = null;
        try {
            postUrl = new URL(restTokenUrl);
            connection = (HttpsURLConnection) postUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(20000);
            connection.setRequestProperty("Authorization", "Basic " + getBaseEcode64(key, secret).trim());
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            output = connection.getOutputStream();
            output.write(("grant_type=client_credentials").getBytes());// 请求输入内容,grant_type为固定值
            output.flush();
            isr = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(isr);
            String tempData;
            while ((tempData = reader.readLine()) != null) {
                result = tempData + result;
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    output = null;
                }
            }
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    isr = null;
                }
            }
            if (null != isr) {
                try {
                    isr.close();
                } catch (IOException e) {
                    isr = null;
                }
            }
            if (null != connection) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * 根据base64加密
     *
     * @param key
     * @param secury
     * @return
     */
    private static String getBaseEcode64(String key, String secury) {
        return new sun.misc.BASE64Encoder().encode((key + ":" + secury).getBytes()).trim();
    }

    private static Map<String, String> jsonToMap(String strJson) {
        Map<String, String> mapJson = new HashMap<String, String>();
        if (strJson == null || "".equals(strJson)) {
            return mapJson;
        }
        strJson = strJson.replaceFirst("^\\{", "");
        strJson = strJson.replaceFirst("}$", "");
        String[] arrStr = strJson.split(",");
        String[] arrStrKV = null;
        String strK = null, strV = null;
        for (int i = 0; i < arrStr.length; i++) {
            arrStrKV = arrStr[i].split(":");
            strK = arrStrKV[0].replaceAll("\"", "").toLowerCase();
            strV = arrStrKV[1].replaceAll("\"", "");
            mapJson.put(strK, strV);
        }
        return mapJson;
    }

}
