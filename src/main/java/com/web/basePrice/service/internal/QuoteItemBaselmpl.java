package com.web.basePrice.service.internal;


import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.BaseSql;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.service.QuoteItemBaseService;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.entity.QuoteItemBase;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service(value = "QuoteItemBaseService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteItemBaselmpl extends BaseSql implements QuoteItemBaseService {
	
	@Autowired
    private QuoteItemBaseDao quoteItemBaseDao;

	@Override
	public ApiResponseResult getList(String keyword,  PageRequest pageRequest) throws Exception {
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsPerson", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));

		}
		Specification<QuoteItemBase> spec = Specification.where(BaseService.and(filters, QuoteItemBase.class));
		Specification<QuoteItemBase> spec1 = spec.and(BaseService.or(filters1, QuoteItemBase.class));
		Page<QuoteItemBase> page = quoteItemBaseDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	@Override
	public ApiResponseResult edit(QuoteItemBase quoteItemBase) throws Exception {
		if(quoteItemBase == null){
			return ApiResponseResult.failure("报价项目设置不能为空");
		}else if(quoteItemBase.getToDoBy()==null||quoteItemBase.getBsPerson()==null){
			return ApiResponseResult.failure("代办人不能为空");
		}
		QuoteItemBase o = quoteItemBaseDao.findById( (long) quoteItemBase.getId());
		if(o!=null){
			o.setToDoBy(quoteItemBase.getToDoBy());
			o.setBsPerson(quoteItemBase.getBsPerson());
			o.setLastupdateBy(UserUtil.getSessionUser().getId());
			o.setLastupdateDate(new Date());
			quoteItemBaseDao.save(o);
		}else {
			return ApiResponseResult.failure("报价项目设置不能为空");
		}
		return ApiResponseResult.success().message("保存成功!");
	}
}
