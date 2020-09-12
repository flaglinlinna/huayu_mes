package com.app.base.control;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.system.log.service.SysLogService;

public abstract class BaseController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationContext applicationContext;
    /*@Autowired
    private ResourceBundleMessageSource messageSource;*/

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}
	
	@Autowired
	private SysLogService sysLogService;
	public SysLogService getSysLogService(){
		return this.sysLogService;
	}

	/**
	 * 获取servlet属性
	 * @return
	 */
	protected final ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

	/**
	 * 获取request
	 * @return
	 */
	protected HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

	/**
	 * 获取response
	 * @return
	 */
	protected HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

	protected String getHeader(String key) {
		return getRequest().getHeader(key);
	}

	protected PageRequest getPageRequest() {
		return this.getPageRequest(null);
	}

	protected PageRequest getPageRequest(Sort sort) {
		HttpServletRequest request = getRequest();
		int page = 1;
		int rows = 10;
		if (null != request.getParameter("page")) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		if(null!=request.getParameter("rows")) {
			rows = Integer.parseInt(request.getParameter("rows"));
		}
		//Paginator paginator = new Paginator();
		PageRequest pageRequest = new PageRequest(page-1, rows, sort);
		
		return pageRequest;
	}
	
	protected PageRequest getPageRequestK3(Sort sort) {
		HttpServletRequest request = getRequest();
		int page = 1;
		int rows = 10;
		if (null != request.getParameter("pageK3")) {
			page = Integer.parseInt(request.getParameter("pageK3"));
		}
		if(null!=request.getParameter("rowsK3")) {
			rows = Integer.parseInt(request.getParameter("rowsK3"));
		}
		//Paginator paginator = new Paginator();
		PageRequest pageRequest = new PageRequest(page-1, rows, sort);
		
		return pageRequest;
	}

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        registerCustomEidtorsForWebDataBinder(binder);
    }

    /**
     * 日期类型转换
     * 
     */
    protected void registerCustomEidtorsForWebDataBinder(WebDataBinder binder) {
        // 日期格式化
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 国际化
     * @param code	编码
     * @return
     */
    protected String getText(String code) {
    	return getText(code, new Object[]{}, "UnKnown", Locale.CHINA);
    }

    /**
     * 国际化
     * @param code	编码
     * @param args	占位符
     * @return
     */
    protected String getText(String code, Object[] args) {
    	return getText(code, args, code, Locale.CHINA);
    }

    /**
     * 国际化
     * @param code	编码
     * @param args	占位符
     * @param defaultMessage	默认值
     * @return
     */
    protected String getText(String code, Object[] args, String defaultMessage) {
    	return getText(code, args, defaultMessage, Locale.CHINA);
    }

    /**
     * 国际化
     * @param code	编码
     * @param args	占位符
     * @param defaultMessage	默认值
     * @param locale	语言区域
     * @return
     */
    protected String getText(String code, Object[] args, String defaultMessage, Locale locale) {
    	return "";//messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
