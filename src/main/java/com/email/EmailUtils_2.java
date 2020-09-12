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
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送工具类
 * 说明：1.这里的邮件服务的接口JavaMailSender采取自动注入的方式，在配置文件中一定需要配置spring.mail.host、spring.mail.port、
 *         spring.mail.username、spring.mail.password，否则无法自动注入而程序报错；
 *       2.在MimeMessageHelper中设置发件人邮箱地址时，一定要与配置文件中的spring.mail.username的邮箱地址一致，否则无法发送。
 * @author sxw
 * @ClassName com.email.EmailUtils_2
 */
@Component
public class EmailUtils_2 {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtils_2.class);
    @Autowired
    private Environment env;
    @Autowired
    private JavaMailSender mailSender;
    //@Autowired
    //private JavaMailSenderImpl mailSender;
    //发件人邮箱
    private static String authName;

    //初始化配置
    @PostConstruct
    public void initParam () {
        authName = env.getProperty("spring.mail.username");
    }

    /**
     * 发送邮件
     * @param subject 主题
     * @param toUsers 收件人
     * @param ccUsers 抄送人
     * @param content 邮件内容
     * @param attachfiles 附件列表  List<Map<String, String>> key: name && file
     */
    public boolean sendEmail(String subject, String[] toUsers, String[] ccUsers, String content, List<Map<String, String>> attachfiles) {
        boolean flag = true;
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
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
            mailSender.send(mailMessage);
        } catch (Exception e) {
            logger.error("发送邮件失败!", e);
            flag = false;
        }
        return flag;
    }

}

