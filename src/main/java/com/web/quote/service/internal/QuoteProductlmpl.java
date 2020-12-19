package com.web.quote.service.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProfitProdDao;
import com.web.basePrice.entity.ProfitProd;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.entity.QuoteItemBase;
import com.web.quote.service.QuoteProductService;

@Service(value = "QuoteProductService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteProductlmpl implements QuoteProductService {
	
	@Autowired
    private QuoteDao quoteDao;
    
    /**
     * 获取报价单列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,PageRequest pageRequest)throws Exception{
    	// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("bsStep", SearchFilter.Operator.EQ, 2));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsProd", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Quote> spec = Specification.where(BaseService.and(filters, Quote.class));
		Specification<Quote> spec1 = spec.and(BaseService.or(filters1, Quote.class));
		Page<Quote> page = quoteDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

}
