package com.web.quote.service.internal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.system.todo.service.TodoInfoService;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.MjProcFeeDao;
import com.web.basePrice.entity.MjProcFee;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.dao.QuoteMouldDao;
import com.web.quote.entity.QuoteMould;
import com.web.quote.service.QuoteMouldService;
import com.web.quote.service.QuoteService;

/**
 * 模具清单维护
 *
 */
@Service(value = "QuoteMouldService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteMouldlmpl implements QuoteMouldService{

	@Autowired
	QuoteMouldDao quoteMouldDao;
	
	@Autowired
	MjProcFeeDao mjProcFeeDao;
	
	@Autowired
	QuoteItemDao quoteItemDao;
	
	@Autowired
	QuoteService quoteService;
	@Autowired
	TodoInfoService todoInfoService;
	
	/**
	 * 获取Bom清单的组件下拉列表
	 * **/
	public ApiResponseResult getBomList(String quoteId)throws Exception{
		List<Map<String, Object>> list=quoteMouldDao.getBomName(quoteId);
		return ApiResponseResult.success().data(list);
	}
	
	/**
	 * 获取模具成本清单
	 * **/
	@Override
	public ApiResponseResult getMouldList() throws Exception {
		// TODO Auto-generated method stub
		List<MjProcFee> list = mjProcFeeDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}
	
	/**
	 * 模具清单维护表
	 **/
	@Override
	public ApiResponseResult getList(String keyword,String pkQuote, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mjProcFee.productName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mjProcFee.productCode", SearchFilter.Operator.LIKE, keyword));
		}
		if (!"null".equals(pkQuote)&&pkQuote!=null) {
			filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		}else {
			List<QuoteMould> quoteMouldList = new ArrayList<>();
			return ApiResponseResult.success().data(DataGrid.create(quoteMouldList, (int) quoteMouldList.size(),
					pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		}
		Specification<QuoteMould> spec = Specification.where(BaseService.and(filters, QuoteMould.class));
		Specification<QuoteMould> spec1 = spec.and(BaseService.or(filters1, QuoteMould.class));
		Page<QuoteMould> page = quoteMouldDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));

	}
	
	/**
	 * 增加模具清单记录
	 * mould:被选中的模具信息id串
	 * itemId:被选择的组件信息
	 * quoteId:报价单主表ID
	 **/
	@Override
	public ApiResponseResult add(String mould, String itemId,String quoteId) throws Exception {
		// TODO Auto-generated method stub
		String[] mouldIds = mould.split("\\,");
		List<QuoteMould> lp = new ArrayList<QuoteMould>();
		// 20201218-先删除后在新增
		quoteMouldDao.delteQuoteMouldByBsNameAndPkQuote(itemId,Long.valueOf(quoteId));//使用组件名字做标识
		int j = 1;
		for (String m : mouldIds) {
			if (!StringUtils.isEmpty(m)) {
				// Proc procItem = procDao.findById(Long.parseLong(pro));
				QuoteMould pd = new QuoteMould();
				//pd.setPkQuoteBom(Long.valueOf(itemId));//bomID
				pd.setBsName(itemId);//bom组件名称
				pd.setPkProcFee(Long.valueOf(m));//模具信息-模具成本id
				pd.setCreateBy(UserUtil.getSessionUser().getId());
				pd.setCreateDate(new Date());
				pd.setPkQuote(Long.valueOf(quoteId));//报价单主表
				lp.add(pd);
				j++;
			}
		}
		quoteMouldDao.saveAll(lp);
		return ApiResponseResult.success("新增成功!");
	}
	
	/**
	 * 编辑实际报价
	 * **/
	@Override
	public ApiResponseResult doActQuote(Long id, BigDecimal bsActQuote) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
            return ApiResponseResult.failure("模具成本信息ID不能为空！");
        }
        if(bsActQuote == null){
            return ApiResponseResult.failure("请填写正确的数字！");
        }
        QuoteMould o = quoteMouldDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("模具成本信息记录不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsActQuote(bsActQuote);
        quoteMouldDao.save(o);
        return ApiResponseResult.success("修改成功！").data(o);
	}
	
	/**
	 * 删除
	 * **/
	@Override
	public ApiResponseResult delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		 if(id == null){
	            return ApiResponseResult.failure("ID不能为空！");
	        }
		 QuoteMould o  = quoteMouldDao.findById((long) id);
	        if(o == null){
	            return ApiResponseResult.failure("该记录不存在！");
	        }
	        o.setDelTime(new Date());
	        o.setDelFlag(1);
	        o.setDelBy(UserUtil.getSessionUser().getId());
	        quoteMouldDao.save(o);
	        return ApiResponseResult.success("删除成功！");
	}
	
	/**
	 * 提交模具维护清单
	 * **/
	 public ApiResponseResult doStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交-lst-20210112
		 int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
		 if(i>0){
			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
		 }
		 int count =quoteMouldDao.countByDelFlagAndPkQuoteAndBsActQuote(0,Long.parseLong(quoteId),null);
		 if(count>0){
			 return ApiResponseResult.failure("提交失败：实际报价不可为空，请检查数据");
		 }
		 quoteMouldDao.saveQuoteMouldByQuoteId(Long.parseLong(quoteId));
		 //项目状态设置-状态 2：已完成
		 quoteItemDao.switchStatus(2, Long.parseLong(quoteId), code);
		 quoteService.doItemFinish(code, Long.parseLong(quoteId));
		 //写个一个根据quoteId获取其所有记录，并批量修改修改人，修改时间字段 的DAO
		 
		 //20210112-fyx-关闭待办
		 todoInfoService.closeByIdAndModel(Long.parseLong(quoteId), "模具清单");
		 return ApiResponseResult.success("提交成功！");
	 }
	 
	 /**
	  * 不需要报价状态设置
	  * **/
	 public ApiResponseResult doNoNeed(String quoteId,String bsCode)throws Exception{
		 if(quoteId==null||quoteId==""){
			 return ApiResponseResult.failure("报价单不可为空");
		 }
		 if(bsCode==null||bsCode==""){
			 return ApiResponseResult.failure("报价单项目编码不可为空");
		 }
		 //状态 3：不需要填写
		 quoteItemDao.switchStatus(3, Long.parseLong(quoteId), bsCode);
		 return ApiResponseResult.success("操作成功！");
	 }
}
