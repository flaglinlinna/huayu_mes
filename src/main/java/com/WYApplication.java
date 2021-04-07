package com;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * 
 * @author Ansel
 *
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class WYApplication extends SpringBootServletInitializer{
	

	public static void main(String[] args) {
		SpringApplication.run(WYApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		 
        return application.sources(WYApplication.class);
    }
	/**  
     * 文件上传配置  
     * @return  
     */  
    @Bean  
    public MultipartConfigElement multipartConfigElement() {  
        MultipartConfigFactory factory = new MultipartConfigFactory();  
        //单个文件最大  
        factory.setMaxFileSize("50MB"); //KB,MB  
        /// 设置总上传数据总大小  
        factory.setMaxRequestSize("500MB");  
        return factory.createMultipartConfig();  
    } 

//	@Bean
//	public HttpMessageConverters fastJsonHttpMessageConverters() {
//		// 1.需要定义一个Convert转换消息的对象
//		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//		// 2.添加fastjson的配置信息，比如是否要格式化返回的json数据
//		FastJsonConfig fastJsonConfig = new FastJsonConfig();
//		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//		// 3.在convert中添加配置信息
//		fastConverter.setFastJsonConfig(fastJsonConfig);
//
//		HttpMessageConverter<?> converter = fastConverter;
//		return new HttpMessageConverters(converter);
//	}
}
