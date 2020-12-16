package com.web.quote.service.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.entity.TodoInfo;
import com.web.basic.service.TodoInfoService;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.entity.QuoteItemBase;
import com.web.quote.service.QuoteService;

@Service(value = "QuoteService")
@Transactional(propagation = Propagation.REQUIRED)
public class Quotelmpl implements QuoteService {
	
	@Autowired
    private QuoteDao quoteDao;
	@Autowired
    private QuoteItemDao quoteItemDao;
	@Autowired
    private QuoteItemBaseDao quoteItemBaseDao;
	@Autowired
    private TodoInfoService todoInfoService;
	
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
    	//1:生成报价编号
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        quote.setBsCode("EQ-" + dateStr);  //编号格式：EQ-年月日时分秒
        
    	quote.setCreateDate(new Date());
    	quote.setCreateBy(UserUtil.getSessionUser().getId());
    	quote = quoteDao.save(quote);
    	//2:建立子表-编码-项目名-代办人-开始/结束时间-进度状态【需有基础信息】
    	List<QuoteItem> lqi = new ArrayList<QuoteItem>();
    	//获取配置的待办项目
    	List<QuoteItemBase> lqb = quoteItemBaseDao.findByDelFlag(0);
    	for(QuoteItemBase qb:lqb){
    		QuoteItem qi = new QuoteItem();
    		qi.setPkQuote(quote.getId());
    		qi.setBsCode(qb.getBsCode());
    		qi.setBsName(qb.getBsName());
    		qi.setToDoBy(qb.getToDoBy());
    		qi.setBsPerson(qb.getBsPerson());
    		qi.setCreateDate(new Date());
    		qi.setCreateBy(UserUtil.getSessionUser().getId());
    		lqi.add(qi);
    	}
    	quoteItemDao.saveAll(lqi);
    	//3:发送待办
    	for(QuoteItemBase qb:lqb){
    		TodoInfo todoInfo = new TodoInfo();
    		todoInfo.setBsType(1);//待办事项都是1
    		todoInfo.setBsUserId(qb.getToDoBy());
    		todoInfo.setBsTitle("新增报价-"+qb.getBsName()+"的资料待录入");
    		todoInfo.setBsContent(quote.getBsCode()+"-"+quote.getBsProject()+ "的报价单");
    		todoInfo.setBsRouter("/quote/toQuoteAdd?id="+quote.getId());
    		todoInfo.setBsReferId(quote.getId()); //关联ID

    		todoInfoService.add(todoInfo);
    	}
    	
    	
        return ApiResponseResult.success("报价单新增成功！").data(lqi);
	}
}
