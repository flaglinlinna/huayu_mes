package com.web.quote.service.internal;

import java.math.BigDecimal;
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
import com.system.todo.service.TodoInfoService;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.BaseFeeDao;
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.entity.BaseFee;
import com.web.basePrice.entity.Proc;
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.QuoteProcessService;
import com.web.quote.service.QuoteService;

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
	
	@Autowired
	private QuoteItemDao quoteItemDao;
	
	@Autowired
	QuoteService quoteService;
	@Autowired
	BaseFeeDao baseFeeDao;
	@Autowired
	TodoInfoService todoInfoService;

	/**
	 * 获取bom列表-下拉选择
	 **/
	@Override
	public ApiResponseResult getBomList(String keyword, Long quoteId,PageRequest pageRequest) throws Exception {
		Page<Map<String, Object>> page=quoteProcessDao.getBomNameByPage(quoteId,pageRequest);
//		return ApiResponseResult.success().data(list);
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
		filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		/*if (!"null".equals(pkQuote)&&pkQuote!=null) {
			
		}else {
			List<QuoteProcess> quoteProcessList = new ArrayList<>();
			return ApiResponseResult.success().data(DataGrid.create(quoteProcessList, (int) quoteProcessList.size(),
					pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		}*/
		Specification<QuoteProcess> spec = Specification.where(BaseService.and(filters, QuoteProcess.class));
		Specification<QuoteProcess> spec1 = spec.and(BaseService.or(filters1, QuoteProcess.class));
		Page<QuoteProcess> page = quoteProcessDao.findAll(spec1, pageRequest);
		//20201222-fyx
		updateLwAndHw(Long.valueOf(pkQuote));
		//--end
		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));

	}
	
	private void updateLwAndHw(Long pkQuote){
		//获取该报价单的所有的未提交的数据，更新一下 人工和制费
		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsStatus(0,pkQuote,0);
		for(QuoteProcess qp:lqp){
			String[] strs = this.getLhBy(qp.getProc().getWorkcenterId(), qp.getPkProc());
			if(!StringUtils.isEmpty(strs[0])){
				qp.setBsFeeLh(new BigDecimal(strs[0]));
			}
			if(!StringUtils.isEmpty(strs[1])){
				qp.setBsFeeMh(new BigDecimal(strs[1]));
			}
		}
		quoteProcessDao.saveAll(lqp);
	}

	/**
	 * 获取工序列表
	 **/
	@Override
	public ApiResponseResult getAddList(Long workcenterId,PageRequest pageRequest) throws Exception {
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		if(workcenterId!=null){
			filters.add(new SearchFilter("workcenterId", SearchFilter.Operator.EQ, workcenterId));
		}
		Specification<Proc> spec = Specification.where(BaseService.and(filters, Proc.class));
		Page<Proc> page = procDao.findAll(spec, pageRequest);
		List<Proc> list =  page.getContent();
		/*return ApiResponseResult.success().data(list);*/
		List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
		for(Proc proc:list){
			String[] strs = this.getLhBy(proc.getWorkcenterId(), Long.valueOf(proc.getId()));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ID", proc.getId());
			map.put("PROC_NO", proc.getProcNo());
			map.put("PROC_NAME", proc.getProcName());
			map.put("WORKCENTER_NAME", proc.getBjWorkCenter().getWorkcenterName());
			if(strs[0]==""||strs[1]==""){//判断人工制费是否有维护	
				map.put("STATUS", "0");
			}else{
				map.put("STATUS", "1");
			}

			if(("out").equals(proc.getBjWorkCenter().getBsCode())){
				//如果是外协
				map.put("STATUS", "out");
			}
			lm.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(lm, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
//		return ApiResponseResult.success().data(lm);
	}

	/**
	 * 增加工艺流程记录
	 **/
	@Override
	public ApiResponseResult add(String proc, String itemId,String quoteId,String bsElement) throws Exception {
		// TODO Auto-generated method stub
		String[] procs = proc.split("\\,");
		List<QuoteProcess> lp = new ArrayList<QuoteProcess>();
		// 20201218-先删除后在新增
		//quoteProcessDao.delteQuoteProcessByPkQuoteBom(Long.parseLong(itemId));//使用id
		//20210113-fyx-先不删除
		//quoteProcessDao.delteQuoteProcessByBsNameAndPkQuote(itemId,Long.valueOf(quoteId));//使用零件名字
		int j = 1;
		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(0,Long.valueOf(quoteId),itemId);
		j += lqp.size() ;
		for (String pro : procs) {
			if (!StringUtils.isEmpty(pro)) {
				// Proc procItem = procDao.findById(Long.parseLong(pro));
				QuoteProcess pd = new QuoteProcess();
				//pd.setPkQuoteBom(Long.valueOf(itemId));//bomID
				pd.setBsName(itemId);//bom零件名称
				pd.setBsElement(bsElement);
				pd.setPkProc(Long.valueOf(pro));//工序id
				pd.setBsOrder((j) * 10);//工序顺序
				pd.setCreateBy(UserUtil.getSessionUser().getId());
				pd.setCreateDate(new Date());
				pd.setPkQuote(Long.valueOf(quoteId));//报价单主表
				//--20201222-fyx-获取人工和制费
				Proc pp1 = procDao.findById(Long.parseLong(pro));
				// 外协不查询人工制费信息
				if(!("out").equals(pp1.getBjWorkCenter().getBsCode())) {
					if (pp1 != null) {
						String[] strs = this.getLhBy(pp1.getWorkcenterId(), Long.valueOf(pro));

						if (strs[0] == "" || strs[1] == "") {//--20201228-lst-判断人工制费是否有维护
							return ApiResponseResult.failure("有未维护的人工制费,请先维护");
						}
						pd.setBsFeeLh(new BigDecimal(strs[0]));
						pd.setBsFeeMh(new BigDecimal(strs[1]));
					}
				}
				//--end
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
        List<QuoteProcess> lpd = quoteProcessDao.findByDelFlagAndPkQuoteAndBsNameAndBsOrder(0, o.getPkQuote(),o.getBsName(), procOrder);
        if(lpd.size()>0){
        	 return ApiResponseResult.failure("工序序号重复,请重新填写！");
        }
        //---end
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsOrder(procOrder);
        quoteProcessDao.save(o);
        return ApiResponseResult.success("修改工序顺序成功！").data(o);
	}
	
	/**
	 * 增加备注
	 * **/
	@Override
	public ApiResponseResult doFmemo(Long id, String  fmemo) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        QuoteProcess o = quoteProcessDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序记录不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setFmemo(fmemo);
        quoteProcessDao.save(o);
        return ApiResponseResult.success("修改备注成功！").data(o);
	}
	
	/**
	 * 删除
	 * **/
	@Override
	public ApiResponseResult delete(String ids) throws Exception {
		String[] id_s = ids.split(",");
		Date deleteTime = new Date();
		List<QuoteProcess> quoteProcessList = new ArrayList<>();
		for(String id :id_s){
			if(id == null){
				return ApiResponseResult.failure("ID不能为空！");
			}
				QuoteProcess o  = quoteProcessDao.findById(Long.parseLong(id));
			if(o == null){
				return ApiResponseResult.failure("该记录不存在！");
			}
				o.setDelTime(deleteTime);
				o.setDelFlag(1);
				o.setDelBy(UserUtil.getSessionUser().getId());
				quoteProcessList.add(o);
		}
	        quoteProcessDao.saveAll(quoteProcessList);
	        return ApiResponseResult.success("删除成功！");
	}
	/**
	 * 确认完成
	 * **/
	 public ApiResponseResult doStatus(String quoteId,String code)throws Exception{
		 //判断状态是否已执行过确认提交-lst-20210112
		 int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
		 if(i>0){
			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
		 }
		 //20201223-fyx-先判断是否维护了人工和制费
		//获取该报价单的所有的未提交的数据，更新一下 人工和制费
			List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsStatus(0,Long.valueOf(quoteId),0);
			for(QuoteProcess qp:lqp){
				if(!("out").equals(qp.getProc().getBjWorkCenter().getBsCode())){
					if(qp.getBsFeeLh() == null || qp.getBsFeeMh() == null){
						return ApiResponseResult.failure("有未维护的人工制费,请先维护!");
					}
				}
			}
		 
		 //项目状态设置-状态 2：已完成
		 quoteItemDao.switchStatus(2, Long.parseLong(quoteId), code);
		 quoteProcessDao.saveQuoteProcessByQuoteId(1,Long.parseLong(quoteId));
		 
		//20210112-fyx-关闭待办
		 todoInfoService.closeByIdAndModel(Long.parseLong(quoteId), "工艺流程");
		 
		 quoteService.doItemFinish(code, Long.parseLong(quoteId));
		 
		 return ApiResponseResult.success("提交成功！");
	 }

	/**
	 * 取消完成
	 * **/
	public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交-lst-20210112
//		int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
//		if(i>0){
//			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
//		}
		//20201223-fyx-先判断是否维护了人工和制费
		//获取该报价单的所有的未提交的数据，更新一下 人工和制费
//		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsStatus(0,Long.valueOf(quoteId),0);
//		for(QuoteProcess qp:lqp){
//			if(qp.getBsFeeLh() == null || qp.getBsFeeMh() == null){
//				return ApiResponseResult.failure("有未维护的人工制费,请先维护!");
//			}
//		}

		//项目状态设置-状态 2：已完成,1 未完成
		quoteItemDao.switchStatus(1, Long.parseLong(quoteId), code);
		quoteProcessDao.saveQuoteProcessByQuoteId(0,Long.parseLong(quoteId));


		todoInfoService.openByIdAndModel(Long.parseLong(quoteId), "工艺流程");

		quoteService.doItemFinish(code, Long.parseLong(quoteId));

		return ApiResponseResult.success("提交成功！");
	}

	 /**
	  * 根据工作中心id和工序id查询人工和制费
	  * @param w_id
	  * @param p_id
	  * @return
	  */
	 private String[] getLhBy(Long w_id,Long p_id ){
		 String[] strs = new String[2];
		 strs[0]="";strs[1]="";
		 List<BaseFee> lbl = baseFeeDao.findByDelFlagAndWorkcenterIdAndProcId(0, w_id, p_id);
		 if(lbl.size() == 0){
			 lbl = baseFeeDao.findByDelFlagAndWorkcenterIdAndProcIdIsNull(0, w_id);
			 if(lbl.size()>0){
				 strs[0] = lbl.get(0).getFeeLh();
				 strs[1] = lbl.get(0).getFeeMh();
			 }
		 }else{
			 strs[0] = lbl.get(0).getFeeLh();
			 strs[1] = lbl.get(0).getFeeMh();
		 }
		 return strs;
	 }

	@Override
	public ApiResponseResult getListByQuoteAndName(String quoteId, String name) throws Exception {
		// TODO Auto-generated method stub
		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(0,Long.valueOf(quoteId),name);
		return ApiResponseResult.success().data(lqp);
	}
}
