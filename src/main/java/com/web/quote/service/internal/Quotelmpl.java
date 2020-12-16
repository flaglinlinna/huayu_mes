package com.web.quote.service.internal;

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
    	quote.setCreateDate(new Date());
    	quote.setCreateBy(UserUtil.getSessionUser().getId());
    	quoteDao.save(quote);
        return ApiResponseResult.success("报价单新增成功！").data(quote);
	}
}
