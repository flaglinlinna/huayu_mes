package provider;

import java.text.MessageFormat;
import java.util.Locale;

//import com.unind.base.dbconnection.dao.internal.JdbcDao;
import org.apache.camel.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.support.ResourceBundleMessageSource;
/**
 * 基础业务类
 * <p>封装spring jdbc、spring上下文信息、国际化资源操作方法</p>
 * @author tanxiang
 *
 */
public class BaseService  {
	/**
	 * 业务层日志处理类封装
	 */
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ResourceBundleMessageSource messageSource;



	public void isRequired(String value, Object...args) throws ValidationException {
		isRequired(value, "{0}不能为空", args);
	}

	public void isRequired(String value, String message, Object...args) throws ValidationException {
		if(StringUtils.isEmpty(value) || StringUtils.isEmpty(value.trim())) {
			throw new ValidationException(null, MessageFormat.format(message, args));
		}
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
    	return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
