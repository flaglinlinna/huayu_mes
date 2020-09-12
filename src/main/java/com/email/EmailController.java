package com.email;


import com.email.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;

@Api(value = "邮箱发送测试")
@CrossOrigin
@ControllerAdvice
@RestController
@RequestMapping(value = "/email")
public class EmailController extends WebController {

//	@Autowired
//	JavaMailSender jms;
//
//    @ApiOperation(value = "发送测试", notes = "发送测试")
//    @PostMapping("/send")
//    public ApiResponseResult send() {
//        try{
//        	//boolean isSend = EmailUtils.sendEmail("这是一封测试邮件", new String[]{"244148556@qq.com","xuwei.s@plee.com.cn"}, null, "<h3><a href='http://www.baidu.com'>百度一下，你就知道</a></h3>", null);
//        	//建立邮件消息
//    		SimpleMailMessage mainMessage = new SimpleMailMessage();
//    		//发送者
//    		mainMessage.setFrom("yuanxiu.f@plee.com.cn");
//    		//接收者
//    		mainMessage.setTo("xuwei.s@plee.com.cn");
//    		//发送的标题
//    		mainMessage.setSubject("测试邮件");
//    		//发送的内容
//    		mainMessage.setText("hello world");
//    		jms.send(mainMessage);
//        	return ApiResponseResult.success(""+"");
//            //return supplierInfoService.add(supplierInfo);
//        }catch (Exception e){
//            logger.error(e.getMessage(), e);
//            e.printStackTrace();
//            return ApiResponseResult.failure("新增供应商失败！");
//        }
//    }

    @Autowired
    private EmailService emailService;

    @ApiOperation(value = "发送邮件测试", notes = "发送邮件测试")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public ApiResponseResult test(){
        try{
            return emailService.test();
        }catch (Exception e){
            logger.error(e.toString(), e);
            e.printStackTrace();
            return ApiResponseResult.failure("邮件发送失败！");
        }
    }
}
