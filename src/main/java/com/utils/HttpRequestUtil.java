package com.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequestUtil {

    public static void main(String[] args) throws IOException {
        // 发送 GET 请求
//        String s = HttpRequestUtil.sendGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken", "corpid=ww69b0e0e28870bba6&" +
//                "corpsecret=zzuOsL86jArCoJNH9s_87xT67HtpwUbvXEMVC7Xz0H8");
//        System.out.println(s);

        JSONObject sendDate = new JSONObject();
        JSONObject content =  new JSONObject();
        String msg = "<font color=\"info\">"+"梁工"+"</font><font color=\"red\">驳回了报价单，驳回原因:xxxxxxxxxxxxx,请您及时审批。</font>" +
                "\n>客户名称："+"华勤-三星"+
                "\n>产品型号："+"P653S11"+
                "\n>版本："+"20210715"+
                "\n>报价单号："+"EQ20210722100206";
        content.put("content",msg);
        sendDate.put("agentid",1000006);
        sendDate.put("msgtype","markdown");
        sendDate.put("markdown",content);
        sendDate.put("touser","7139A91B-96B4-4D64-91B5-10126E81C2B8");

        //表示是否开启重复消息检查，0表示否，1表示是，默认0
        sendDate.put("enable_duplicate_check",1);
        //表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时
        sendDate.put("duplicate_check_interval",1800);

        String requestDate = HttpRequestUtil.sendJsonPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+"Y7uZ5quGgAFSS7UtVYvq2QtRbVvxy-05AEMA6v5LK9a4psQDeV-KezRS8k5heDgTWsrSpug6YYUyJMOzNYJy9NHfhP_mdRL4Pxtg0l5cadGFvueKosnlVANv7gRLJ9xzZg2b9s5I7oxBfk62TkB6Cya-uTT2V4FOq-SSyCOw1YaFRCTz3ZMmsUgTtqtNts-R5BD5VazgOzWFtkI3eecxsw",sendDate,null);
        System.out.println(requestDate);
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性(请求头信息)
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    public static String sendJsonPost(String url, JSONObject jsonObject, String encoding) throws IOException {
        String body = "";
        if(StringUtils.isEmpty(encoding)){
            encoding = "UTF-8";
        }
        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        //设置参数到请求对象中
        httpPost.setEntity(s);
//        System.out.println("请求地址："+url);
//        System.out.println("请求参数："+nvps.toString());

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
//        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Content-type", "application/json");
//        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }

}