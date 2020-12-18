package com.web.quote.service.internal;

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
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.entity.Proc;
import com.web.basic.entity.ProdProcDetail;
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.QuoteBom;
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

	@Autowired
	QuoteBomDao quoteBomDao;

	@Autowired
	ProcDao procDao;

	/**
	 * 获取bom列表-下拉选择
	 **/
	@Override
	public ApiResponseResult getBomList(String keyword, PageRequest pageRequest) throws Exception {
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsItemCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsElement", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsComponent", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsMaterName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<QuoteBom> spec = Specification.where(BaseService.and(filters, QuoteBom.class));
		Specification<QuoteBom> spec1 = spec.and(BaseService.or(filters1, QuoteBom.class));
		Page<QuoteBom> page = quoteBomDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	public ApiResponseResult getBomList2(String quoteId)throws Exception{
		List<Map<String, Object>> list=quoteProcessDao.getBomName(quoteId);
		return ApiResponseResult.success().data(list);
	}
	
	/**
	 * 工艺流程表
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
			filters1.add(new SearchFilter("proc.procName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("proc.procNo", SearchFilter.Operator.LIKE, keyword));
		}
		if (!"null".equals(pkQuote)&&pkQuote!=null) {
			filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		}else {
			List<QuoteProcess> quoteProcessList = new ArrayList<>();
			return ApiResponseResult.success().data(DataGrid.create(quoteProcessList, (int) quoteProcessList.size(),
					pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		}
		Specification<QuoteProcess> spec = Specification.where(BaseService.and(filters, QuoteProcess.class));
		Specification<QuoteProcess> spec1 = spec.and(BaseService.or(filters1, QuoteProcess.class));
		Page<QuoteProcess> page = quoteProcessDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));

	}

	/**
	 * 获取工序列表
	 **/
	@Override
	public ApiResponseResult getAddList() throws Exception {
		// TODO Auto-generated method stub
		List<Proc> list = procDao.findByDelFlagAndCheckStatus(0, 1);
		return ApiResponseResult.success().data(list);
	}

	/**
	 * 增加工艺流程记录
	 **/
	@Override
	public ApiResponseResult add(String proc, String itemId,String quoteId) throws Exception {
		// TODO Auto-generated method stub
		String[] procs = proc.split("\\,");
		List<QuoteProcess> lp = new ArrayList<QuoteProcess>();
		// 20201218-先删除后在新增
		//quoteProcessDao.delteQuoteProcessByPkQuoteBom(Long.parseLong(itemId));//使用id
		quoteProcessDao.delteQuoteProcessByBsName(itemId);//使用零件名字
		int j = 1;
		for (String pro : procs) {
			if (!StringUtils.isEmpty(pro)) {
				// Proc procItem = procDao.findById(Long.parseLong(pro));
				QuoteProcess pd = new QuoteProcess();
				//pd.setPkQuoteBom(Long.valueOf(itemId));//bomID
				pd.setBsName(itemId);//bom零件名称
				pd.setPkProc(Long.valueOf(pro));//工序id
				pd.setBsOrder((j) * 10);//工序顺序
				pd.setCreateBy(UserUtil.getSessionUser().getId());
				pd.setCreateDate(new Date());
				pd.setPkQuote(Long.valueOf(quoteId));//报价单主表
				lp.add(pd);
				j++;
			}
		}
		quoteProcessDao.saveAll(lp);
		return ApiResponseResult.success("新增成功!");
	}
	
	/**
	 * 改变工序顺序
	 * **/
	@Override
	public ApiResponseResult doProcOrder(Long id, Integer procOrder) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(procOrder == null){
            return ApiResponseResult.failure("请填写正确的数字！");
        }
        QuoteProcess o = quoteProcessDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序记录不存在！");
        }
        //判断顺序是否存在
        List<QuoteProcess> lpd = quoteProcessDao.findByDelFlagAndPkQuoteBomAndBsOrder(0, o.getPkQuoteBom(), procOrder);
        if(lpd.size()>0){
        	 return ApiResponseResult.failure("工序序号重复,请重新填写！");
        }
        //---end
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsOrder(procOrder);
        quoteProcessDao.save(o);
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
		 QuoteProcess o  = quoteProcessDao.findById((long) id);
	        if(o == null){
	            return ApiResponseResult.failure("该记录不存在！");
	        }
	        o.setDelTime(new Date());
	        o.setDelFlag(1);
	        o.setDelBy(UserUtil.getSessionUser().getId());
	        quoteProcessDao.save(o);
	        return ApiResponseResult.success("删除成功！");
	}
	/**
	 * 提交工序维护清单
	 * **/
	 public ApiResponseResult doStatus(String quoteId)throws Exception{
		 
		 quoteProcessDao.saveQuoteProcessByQuoteId(Long.parseLong(quoteId));
		 
		 return ApiResponseResult.success("提交成功！");
	 }
}
