package com.web.quote.service.internal;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.quote.dao.QuoteDao;
import com.web.quote.entity.Quote;
import com.web.quote.service.QuoteService;

@Service(value = "QuoteService")
@Transactional(propagation = Propagation.REQUIRED)
public class Quotelmpl implements QuoteService {
	
	@Autowired
    private QuoteDao quoteDao;
	
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
    	//生成报价编号
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        quote.setBsCode("EQ-" + dateStr);  //编号格式：EQ-年月日时分秒
        
    	quote.setCreateDate(new Date());
    	quote.setCreateBy(UserUtil.getSessionUser().getId());
    	quoteDao.save(quote);
    	//建立子表-编码-项目名-代办人-开始/结束时间-进度状态【需有基础信息】
    	
        return ApiResponseResult.success("报价单新增成功！").data(quote);
	}
}
