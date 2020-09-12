package com.email;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送工具类
 * 说明：1.这里的邮件服务的接口JavaMailSender采取的是普通新建对象的方式实体化，即new一个JavaMailSenderImpl对象；
 *       2.需要在JavaMailSender中设置好所有相关的邮箱参数，否则无法发送。
 * @author sxw
 * @ClassName com.email.EmailUtils
 * @Description
 */
@Component
public class EmailUtils {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtils.class);

    @Autowired
    private Environment env;

    private static String auth;
    private static String host;
    private static String protocol;
    private static int port;
    private static String authName;
    private static String password;
    private static boolean isSSL;
    private static String charset ;
    private static String timeout;

    //初始化配置
    @PostConstruct
    public void initParam () {
        auth = env.getProperty("mail.smtp.auth") != null ? env.getProperty("mail.smtp.auth") : "false";
        host = env.getProperty("mail.host");
        protocol = env.getProperty("mail.transport.protocol");
        port = env.getProperty("mail.smtp.port", Integer.class) != null ? env.getProperty("mail.smtp.port", Integer.class) : 25;
        authName = env.getProperty("mail.auth.name");
        password = env.getProperty("mail.auth.password");
        charset = env.getProperty("mail.send.charset");
        isSSL = env.getProperty("mail.is.ssl", Boolean.class) != null ? env.getProperty("mail.is.ssl", Boolean.class) : false;
        timeout = env.getProperty("mail.smtp.timeout") != null ? env.getProperty("mail.smtp.timeout") : "50000";
    }

    /**
     * 发送邮件
     * @param subject 主题
     * @param toUsers 收件人
     * @param ccUsers 抄送人
     * @param content 邮件内容
     * @param attachfiles 附件列表  List<Map<String, String>> key: name && file
     */
    public static boolean sendEmail(String subject, String[] toUsers, String[] ccUsers, String content, List<Map<String, String>> attachfiles) {
        boolean flag = true;
        try {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(host);
            javaMailSender.setUsername(authName);
            javaMailSender.setPassword(password);
            javaMailSender.setDefaultEncoding(charset);
            javaMailSender.setProtocol(protocol);
            javaMailSender.setPort(port);

            Properties properties = new Properties();
            properties.setProperty("mail.smtp.auth", auth);
            properties.setProperty("mail.smtp.timeout", timeout);
            if(isSSL){
                MailSSLSocketFactory sf = null;
                try {
                    sf = new MailSSLSocketFactory();
                    sf.setTrustAllHosts(true);
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.ssl.socketFactory", sf);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                }
            }
            javaMailSender.setJavaMailProperties(properties);

            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true);
            messageHelper.setTo(toUsers);
            if (ccUsers != null && ccUsers.length > 0) {
                messageHelper.setCc(ccUsers);
            }
            messageHelper.setFrom(authName);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);

            if (attachfiles != null && attachfiles.size() > 0) {
                for (Map<String, String> attachfile : attachfiles) {
                    String attachfileName = attachfile.get("name");
                    File file = new File(attachfile.get("file"));
                    messageHelper.addAttachment(attachfileName, file);
                }
            }
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            logger.error("发送邮件失败!", e);
            flag = false;
        }
        return flag;
    }
    @Autowired
	static
	JavaMailSender jms;

	public static String send(){
		//建立邮件消息
		SimpleMailMessage mainMessage = new SimpleMailMessage();
		//发送者
		mainMessage.setFrom("fuyuanxiu@126.com");
		//接收者
		mainMessage.setTo("yuanxiu.f@plee.com.cn");
		//发送的标题
		mainMessage.setSubject("嗨喽");
		//发送的内容
		mainMessage.setText("hello world");
		jms.send(mainMessage);
		return "1";
	}

	/**
     * 发送邮件
     * @param subject
     * 				邮件主题
     * @param sendHtml
     * 				邮件内容
     * @param toUser
     * 				收件人  多个时参数形式  ：  "xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param ccUser
     * 				抄送人   同上
     * @param bccUser
     * 				密送人   同上
     * @param attachment
     * 				附件
     */
	public void doSendHtmlEmail(String subject, String sendHtml,
    		String  toUser, String ccUser, String bccUser, File [] attachment){
    	try {

    		MimeMessage mimeMessage = jms.createMimeMessage();
    		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            System.out.println("发送成功！");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	 /**
     * 发送带附件的邮件
     * @param to 接受者
     * @param subject 主题
     * @param content 内容
     * @param filePath 文件路径
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message=jms.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            FileSystemResource file=new FileSystemResource(new File(filePath));
            String fileName=filePath.substring(filePath.lastIndexOf(File.separator));

            helper.addAttachment(fileName,file);

            jms.send(message);
            System.out.println("带附件的邮件发送成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
        }
    }


}

