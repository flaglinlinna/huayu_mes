package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.quote.dao.ProductFileDao;
import com.web.quote.entity.ProductFile;
import com.web.quote.entity.QuoteBom;
import com.web.quote.service.ProductFileService;
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

@Service(value = "ProductFileService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductFilelmpl implements ProductFileService {

	@Autowired
	private ProductFileDao productFileDao;


	@Override
	public ApiResponseResult add(ProductFile productFile) throws Exception {
		if(productFile == null){
			return ApiResponseResult.failure("产品资料信息不能为空！");
		}
		productFileDao.save(productFile);
		return ApiResponseResult.success("产品资料信息新增成功！").data(productFile);
	}


	/**
	 * 删除外购件清单列表
	 * **/
	public ApiResponseResult delete(Long id) throws Exception{
		if(id == null){
			return ApiResponseResult.failure("产品资料信息ID不能为空！");
		}
		ProductFile o  = productFileDao.findById((long) id);
		if(o == null){
			return ApiResponseResult.failure("产品资料信息不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		productFileDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 获取报价单列表
	 * **/
	public ApiResponseResult getList(String keyword,String pkQuote,PageRequest pageRequest) throws Exception{
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsFileName", SearchFilter.Operator.LIKE, keyword));
		}
		if (!"null".equals(pkQuote)&&pkQuote!=null) {
			filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		}
		Specification<ProductFile> spec = Specification.where(BaseService.and(filters, ProductFile.class));
		Specification<ProductFile> spec1 = spec.and(BaseService.or(filters1, ProductFile.class));
		Page<ProductFile> page = productFileDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
