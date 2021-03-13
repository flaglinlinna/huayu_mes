package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.service.TodoInfoService;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteFileDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteFile;
import com.web.quote.service.QuoteFileService;
import com.web.quote.service.QuoteService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "ProductFileService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteFilelmpl implements QuoteFileService {

	@Autowired
	private QuoteFileDao quoteFileDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private QuoteService quoteService;
	@Autowired
	private QuoteItemDao quoteItemDao;
	@Autowired
	TodoInfoService todoInfoService;
	@Autowired
	private QuoteDao quoteDao;

	@Override
	public ApiResponseResult add(QuoteFile productFile) throws Exception {
		if(productFile == null){
			return ApiResponseResult.failure("产品资料信息不能为空！");
		}
		productFile.setCreateDate(new Date());
		productFile.setCreateBy(UserUtil.getSessionUser().getId());
		quoteFileDao.save(productFile);
		return ApiResponseResult.success("产品资料信息新增成功！").data(productFile);
	}


	/**
	 * 删除外购件清单列表
	 * **/
	public ApiResponseResult delete(Long id) throws Exception{
		if(id == null){
			return ApiResponseResult.failure("产品资料信息ID不能为空！");
		}
        QuoteFile o  = quoteFileDao.findById((long) id);
		if(o == null){
			return ApiResponseResult.failure("产品资料信息不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		quoteFileDao.save(o);
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
		}else {
			List<QuoteFile> quoteBomList = new ArrayList<>();
			return ApiResponseResult.success().data(DataGrid.create(quoteBomList, 0,
					1, 10));
		}
		Specification<QuoteFile> spec = Specification.where(BaseService.and(filters, QuoteFile.class));
		Specification<QuoteFile> spec1 = spec.and(BaseService.or(filters1, QuoteFile.class));
		Page<QuoteFile> page = quoteFileDao.findAll(spec1, pageRequest);
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<QuoteFile> productFileList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (QuoteFile productFile : productFileList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id",productFile.getId());
			map.put("pkQuote",productFile.getPkQuote());
			map.put("bsFileName",productFile.getBsFileName());
			map.put("pkFileId",productFile.getPkFileId());
			map.put("createBy", sysUserDao.findById((long) productFile.getCreateBy()).getUserName());
			map.put("createDate", df.format(productFile.getCreateDate()));
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 确认完成
	 * **/
	 public ApiResponseResult doStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交
		 int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
		 if(i>0){
			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
		 }
		 quoteFileDao.saveQuoteFileByQuoteId(1,Long.parseLong(quoteId));
		 //项目状态设置-状态 2：已完成
		 quoteItemDao.switchStatus(2, Long.parseLong(quoteId), code);
		 Object data =  quoteService.doItemFinish(code, Long.parseLong(quoteId)).getData();
		 
		//20210112-fyx-关闭待办
		 todoInfoService.closeByIdAndModel(Long.parseLong(quoteId), "产品资料");
		 return ApiResponseResult.success("提交成功！").data(data);
	 }


	/**
	 * 取消完成
	 * **/
	public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交
//		int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
//		if(i==0){
//			return ApiResponseResult.failure("此项目未完成，请不要重复确认提交。");
//		}
		Quote quote = quoteDao.findById(Long.parseLong(quoteId));
		if(quote.getBsStatus()==1||quote.getBsStatus()==4){
			return ApiResponseResult.failure("报价单已提交审批，不能取消完成。");
		}
		//修改取消状态
		quoteFileDao.saveQuoteFileByQuoteId(0,Long.parseLong(quoteId));
		//项目状态设置-状态 2：已完成，1未完成
		quoteItemDao.switchStatus(1, Long.parseLong(quoteId), code);
		quoteService.doItemFinish(code, Long.parseLong(quoteId));


		todoInfoService.openByIdAndModel(Long.parseLong(quoteId), "产品资料");
		return ApiResponseResult.success("提交成功！");
	}
}
