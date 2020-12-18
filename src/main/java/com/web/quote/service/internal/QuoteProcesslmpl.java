package com.web.quote.service.internal;

import java.util.ArrayList;
import java.util.List;

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
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.entity.ProdProcDetail;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.QuoteProcessService;

/**
 * 报价工艺流程表
 *
 */
@Service(value = "QuoteProcessService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteProcesslmpl implements QuoteProcessService {

	@Autowired
	QuoteProcessDao quoteProcessDao;
	
	public ApiResponseResult getList(String keyword, PageRequest pageRequest)  throws Exception{
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsExplain", SearchFilter.Operator.LIKE, keyword));
			//filters1.add(new SearchFilter("mtrial.itemName", SearchFilter.Operator.LIKE, keyword));	
		}
		Specification<QuoteProcess> spec = Specification.where(BaseService.and(filters, QuoteProcess.class));
		Specification<QuoteProcess> spec1 = spec.and(BaseService.or(filters1, QuoteProcess.class));
		Page<QuoteProcess> page = quoteProcessDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		
	}
}
