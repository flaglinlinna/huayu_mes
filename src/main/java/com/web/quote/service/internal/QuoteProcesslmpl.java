package com.web.quote.service.internal;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.web.quote.dao.QuoteDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteBom;
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
	@Autowired
	private QuoteDao quoteDao;

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
		List<QuoteProcess> quoteProcessList = page.getContent();
		List<Proc> packagList = procDao.findByDelFlagAndProcName(0,"组装");
		for(QuoteProcess o:quoteProcessList) {
			List<Map<String, Object>> componentList = quoteBomDao.getBsComponent(Long.parseLong(pkQuote),o.getBsElement());
			List<Map<String, Object>> procList = new ArrayList<>();
			if(!("out").equals(o.getBjWorkCenter().getBsCode())){
				procList = quoteProcessDao.getProcByWorkCenter(o.getPkWorkCenter());
			}else {
				procList = quoteProcessDao.getProcByWorkCenterAndOut(o.getPkWorkCenter());
			}
//			List<Map<String, Object>> mapList = quoteBomDao.getBsMaterName(o.getPkQuote(), o.getBsElement(), o.getBsName(), o.getPkWorkCenter());
//			if(mapList.size()==1){
//				o.setBsMaterName(mapList.get(0).get("BSMATERNAME").toString());
//			}
//			List<Map<String, Object>> groupsList = quoteBomDao.getBsGroups(o.getPkQuote(), o.getBsElement(), o.getBsName(), o.getPkWorkCenter());

//			if (StringUtils.isNotEmpty(o.getBsMaterName())) {
//				//根据材料名称找到所属分组及对应的bom
//				// 材料名称可以为空(去除这个逻辑)
//				if (o.getPkQuoteBom() == null) {
//					for (Map<String, Object> map : mapList) {
//						if (o.getBsMaterName().equals(map.get("BSMATERNAME"))) {
//							o.setPkQuoteBom(Long.parseLong(map.get("ID").toString()));
//							o.setBsGroups(map.get("BSGROUPS") == null ? "" : map.get("BSGROUPS").toString());
//						}
//					}
//				}
//			}
			//材料下拉选择
//			if (mapList.size() > 0) {
//				o.setBsMaterNameList(JSON.toJSONString(mapList));
//			}
			//损耗分组下拉选择
//			if (groupsList.size() > 0) {
//				o.setBsGroupsList(JSON.toJSONString(groupsList));
//			}

			//关联到bom，如果材料是辅料，则查询关联的零件名称
			if(o.getPkQuoteBom()!=null) {
//				if(StringUtils.isEmpty(o.getItemType())){
//					o.setItemType(o.getQuoteBom().getItp().getItemType());
//				}

				if (o.getItemType().startsWith("辅料")){
//				if (("辅料").equals(o.getQuoteBom().getItp().getItemType())) {
					//2021-05-19 物料类型为 辅料 的，工序名称默认为 组装（如果存在组装工序）
					if(o.getPkProc()==null&&packagList.size()>0){
						o.setPkProc(packagList.get(0).getId());
					}

					//关联第一个关联零件并返回下拉选择
						if(componentList.size()>0){
							o.setBsComponentList(JSON.toJSONString(componentList));
							if(StringUtils.isEmpty(o.getBsLinkName())) {
								o.setBsLinkName(componentList.get(0).get("BSCOMPONENT").toString());
							}
						}else {
							o.setBsLinkName(o.getBsName());
						}

				}else if(StringUtils.isEmpty(o.getItemType())) {
					//新增的时候为空
					o.setBsComponentList(JSON.toJSONString(componentList));
					if(StringUtils.isEmpty(o.getBsLinkName())) {
						o.setBsLinkName(componentList.get(0).get("BSCOMPONENT").toString());
					}
				}else {
					if(StringUtils.isEmpty(o.getBsLinkName())){
						o.setBsLinkName(o.getBsName());
					}
					//非辅料 (关联零件为自身零件)
					o.setBsComponentList(JSON.toJSONString(componentList));
				}
			}else {
				//关联不到bom，则可能是外协工艺，默认非辅料(关联零件为自身零件)
//				if(o.getBjWorkCenter().getBsCode())
				Map<String, Object> map = new HashMap<>();
				map.put("BSCOMPONENT",o.getBsName());
				componentList.add(map);
				o.setBsComponentList(JSON.toJSONString(componentList));
				o.setBsLinkName(o.getBsName());
			}

			if(procList.size() >0){
				//工序下拉框
				o.setBsProcList(JSON.toJSONString(procList));
			}
		}
		//20201222-fyx
		updateLwAndHw(Long.valueOf(pkQuote));
		//--end
		return ApiResponseResult.success().data(DataGrid.create(quoteProcessList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));

	}
	
	private void updateLwAndHw(Long pkQuote){
		//获取该报价单的所有的未提交的数据，更新一下 人工和制费
		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsStatusOrderByBsOrder(0,pkQuote,0);
		for(QuoteProcess qp:lqp){
			if(qp.getProc()!=null) {
				String[] strs = this.getLhBy(qp.getProc().getWorkcenterId(), qp.getPkProc());
				if (!StringUtils.isEmpty(strs[0])) {
					qp.setBsFeeLh(new BigDecimal(strs[0]));
				}
				if (!StringUtils.isEmpty(strs[1])) {
					qp.setBsFeeMh(new BigDecimal(strs[1]));
				}
			}
		}
		quoteProcessDao.saveAll(lqp);
	}

	//对每个组件在最后增加3行：检验、测试、包装
	private void updateProcess(Long pkQuote){
		List<Map<String, Object>> bsNameList = quoteProcessDao.getBsNameGroupByElement(pkQuote);
		List<String> newName =Arrays.asList("测试","检验","包装");
		List<QuoteProcess> quoteProcessList = new ArrayList<>();
		try {
		for (Map<String,Object> map: bsNameList){
			List<String> nameList = Arrays.asList(map.get("BSNAME").toString().split(","));
			if(!nameList.containsAll(newName)){
				for(Integer a= 0;a<newName.size();a++){
					QuoteProcess quoteProcess = new QuoteProcess();
					quoteProcess.setBsElement(map.get("BSELEMENT").toString());
					quoteProcess.setBsName(newName.get(a));
					quoteProcess.setBsLinkName(newName.get(a));
					List<Proc> procList = procDao.findByDelFlagAndProcName(0,newName.get(a));
					if(procList.size()>0){
						quoteProcess.setPkProc(procList.get(0).getId());
						quoteProcess.setPkWorkCenter(procList.get(0).getWorkcenterId());
					}
					quoteProcess.setPkQuote(pkQuote);
					quoteProcess.setCreateBy(UserUtil.getSessionUser().getId());
					quoteProcess.setCreateDate(new Date());
					quoteProcess.setBsOrder(Integer.parseInt(map.get("BSORDER").toString())+a+1);
					quoteProcessList.add(quoteProcess);
				}
			}
		}
		quoteProcessDao.saveAll(quoteProcessList);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void addProcess(List<QuoteProcess> quoteProcessList){

//		quoteProcessDao.saveAll(lqp);
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
	public ApiResponseResult add(String proc, String itemId,String quoteId,String bsElement,String bsBomId) throws Exception {
		// TODO Auto-generated method stub
		String[] procs = proc.split("\\,");
		List<QuoteProcess> lp = new ArrayList<QuoteProcess>();
		// 20201218-先删除后在新增
		//quoteProcessDao.delteQuoteProcessByPkQuoteBom(Long.parseLong(itemId));//使用id
		//20210113-fyx-先不删除
		//quoteProcessDao.delteQuoteProcessByBsNameAndPkQuote(itemId,Long.valueOf(quoteId));//使用零件名字
//		int j = 1;
		QuoteBom quoteBom = quoteBomDao.findById(Long.parseLong(bsBomId));
		Integer a = quoteProcessDao.findMaxBsOrder(0,Long.valueOf(quoteId));
		int j = (a==null?0:a)+10;
//		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(0,Long.valueOf(quoteId),itemId);
//		j += lqp.size() ;
		for (String pro : procs) {
			if (!StringUtils.isEmpty(pro)) {
				// Proc procItem = procDao.findById(Long.parseLong(pro));
				QuoteProcess pd = new QuoteProcess();
				Proc proc1 = procDao.findById((long) Long.valueOf(pro));
				//pd.setPkQuoteBom(Long.valueOf(itemId));//bomID
				pd.setBsName(itemId);//bom零件名称
				pd.setBsElement(bsElement);
				pd.setPkProc(Long.valueOf(pro));//工序id
				pd.setBsOrder(j);//工序顺序
				pd.setCreateBy(UserUtil.getSessionUser().getId());
				pd.setPkWorkCenter(proc1.getWorkcenterId());
				pd.setCreateDate(new Date());
				pd.setPkQuote(Long.valueOf(quoteId));//报价单主表
				//--20201222-fyx-获取人工和制费
				Proc pp1 = procDao.findById(Long.parseLong(pro));

//				pd.setBsMaterName(quoteBom.getBsMaterName());
//				pd.setBsGroups(quoteBom.getBsGroups());
				pd.setPkQuoteBom(quoteBom.getId());
//				pd.setItemType(quoteBom.getItp().getItemType());

//				List<Map<String,Object>> mapList =quoteBomDao.getBsMaterName(pd.getPkQuote(),pd.getBsElement(),pd.getBsName(),proc1.getWorkcenterId());
//				if(mapList.size()==1){
//					pd.setBsMaterName(mapList.get(0).get("BSMATERNAME")==null?"":mapList.get(0).get("BSMATERNAME").toString());
//					pd.setBsGroups(mapList.get(0).get("BSGROUPS")==null?"":mapList.get(0).get("BSGROUPS").toString());
//					pd.setPkQuoteBom(Long.parseLong(mapList.get(0).get("ID").toString()));
//				}

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
		for(QuoteProcess qp:lp){
			qp.setCopyId(qp.getId());
			qp.setBsLinkName(qp.getBsName());
		}
		
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
        List<QuoteProcess> lpd = quoteProcessDao.findByDelFlagAndPkQuoteAndBsOrderAndIdIsNot(0, o.getPkQuote(), procOrder,o.getId());
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
	 * 增加备注
	 * **/
	@Override
	public ApiResponseResult doBsMaterName(Long id, Long  bomId) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
			return ApiResponseResult.failure("工序ID不能为空！");
		}
		QuoteProcess o = quoteProcessDao.findById((long) id);
		if(o == null){
			return ApiResponseResult.failure("工序记录不存在！");
		}
		if(bomId!=null) {
			QuoteBom quoteBom = quoteBomDao.findById((long) bomId);
			if (quoteBom == null) {
				return ApiResponseResult.failure("外购件清单中不存在此材料！");
			}
			if (!bomId.equals(o.getPkQuoteBom()) && bomId != null) {
				if (quoteProcessDao.countByDelFlagAndPkQuoteAndPkQuoteBom(0, o.getPkQuote(), bomId) > 0) {
					return ApiResponseResult.failure("该工艺流程下已存在该材料！");
				}
			}
			o.setBsMaterName(quoteBom.getBsMaterName());
			o.setPkQuoteBom(bomId);
		}else {
			o.setBsMaterName(null);
			o.setPkQuoteBom(null);
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		quoteProcessDao.save(o);
		return ApiResponseResult.success("修改材料成功！").data(o);
	}

	/**
	 * 设置损耗分组
	 * **/
	@Override
	public ApiResponseResult doBsGroups(Long id, String bsGroups) throws Exception {
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
		o.setBsGroups(bsGroups);
		quoteProcessDao.save(o);
		return ApiResponseResult.success("修改损耗分组成功！").data(o);
	}

	/**
	 * 设置损耗分组
	 * **/
	@Override
	public ApiResponseResult doProc(Long id, Long procId) throws Exception {
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
		o.setPkProc(procId);
		quoteProcessDao.save(o);
		return ApiResponseResult.success("修改工艺成功！").data(o);
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
	 public ApiResponseResult doStatus(String quoteId,String code,List<QuoteProcess> quoteProcessList)throws Exception{
		 //判断状态是否已执行过确认提交-lst-20210112
		 int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
		 if(i>0){
			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
		 }
		 quoteProcessDao.saveAll(quoteProcessList);
//		 if(quoteProcessDao.getPkQuoteBomNum(Long.parseLong(quoteId)).size()>0){
//			 return ApiResponseResult.failure("存在相同的材料名称,请检查。");
//		 }
		 if(quoteProcessDao.countByDelFlagAndPkQuoteAndPkProcIsNull(0,Long.parseLong(quoteId))>0){
			 return ApiResponseResult.failure("请填写完所有工序");
		 }
//		 if(quoteProcessDao.getBsGroupsNum(Long.parseLong(quoteId)).size()>0){
//			 return ApiResponseResult.failure("存在相同损耗合计分组名称,请检查。");
//		 }
		 //20201223-fyx-先判断是否维护了人工和制费
		//获取该报价单的所有的未提交的数据，更新一下 人工和制费
		 List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsStatusOrderByBsOrder(0,Long.valueOf(quoteId),0);
		 String[] bsGroupsArray = new String[lqp.size()];
		 for(Integer q = 0;q<=lqp.size()-1;q++){
		 		QuoteProcess qp = lqp.get(q);
			 if(qp.getProc()==null){
			 	qp.setProc(procDao.findById((long) qp.getPkProc()));
			 }
				if(!("out").equals(qp.getProc().getBjWorkCenter().getBsCode())){
					if(qp.getBsFeeLh() == null || qp.getBsFeeMh() == null){
						String[] strs = this.getLhBy(qp.getProc().getWorkcenterId(), qp.getPkProc());
						if (!StringUtils.isEmpty(strs[0])) {
							qp.setBsFeeLh(new BigDecimal(strs[0]));
						}
						if (!StringUtils.isEmpty(strs[1])) {
							qp.setBsFeeMh(new BigDecimal(strs[1]));
						}
//						return ApiResponseResult.failure("有未维护的人工制费,请先维护!");
					}
				}

//				if(StringUtils.isNotEmpty(qp.getBsMaterName())){
//					if(quoteBomDao.findByDelFlagAndBsMaterName(0,qp.getBsMaterName()).size()==0){
//						return ApiResponseResult.failure("外购件清单中不存在 "+qp.getBsMaterName()+" 的材料名称");
//					}
//				}
				bsGroupsArray[q]= qp.getBsGroups();
			}

		 for(Integer k = 0;k<= bsGroupsArray.length-1;k++){
			 List<Integer> list = new ArrayList<>();
			 List<String> bsGroupsString = new ArrayList<>();
		 	for(Integer j = 0;j<=bsGroupsArray.length-1;j++){
		 		if(StringUtils.isNotEmpty(bsGroupsArray[k])){
		 			if(bsGroupsArray[k].equals(bsGroupsArray[j])){
						list.add(j);
						bsGroupsString.add(bsGroupsArray[k]);
					}
				}
			}
//		 	if(list.size()==1){
//				return ApiResponseResult.failure("损耗分组不能只存在一条!");
//			}
//			for(int m = 0;m<list.size()-1;m++){
//				if(list.get(m+1)-list.get(m)!=1){
//					return ApiResponseResult.failure("相同的损耗分组("+bsGroupsString.get(m)+")必须相邻!");
//				}
//			}
		 }


		 
		 //项目状态设置-状态 2：已完成
		 quoteItemDao.switchStatus(2, Long.parseLong(quoteId), code);
		 quoteProcessDao.saveQuoteProcessByQuoteId(1,Long.parseLong(quoteId));
		 
		//20210112-fyx-关闭待办
		 todoInfoService.closeByIdAndModel(Long.parseLong(quoteId), "工艺流程");

		 Object data = quoteService.doItemFinish(code, Long.parseLong(quoteId)).getData();
		 
		 return ApiResponseResult.success("提交成功！").data(data);
	 }



	/**
	 * 取消完成
	 * **/
	public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception{
		Quote quote = quoteDao.findById(Long.parseLong(quoteId));
		if(quote.getBsStatus()==1||quote.getBsStatus()==4){
			return ApiResponseResult.failure("报价单已提交审批，不能取消完成。");
		}

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
		//hjj-2021-06-11 查询出所属零件已选工艺
		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsLinkNameOrderByBsOrder(0,Long.valueOf(quoteId),name);
		return ApiResponseResult.success().data(lqp);
	}

	@Override
	public ApiResponseResult editProcessList(List<QuoteProcess> quoteProcessList) throws Exception {
		// TODO Auto-generated method stub
//		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(0,Long.valueOf(quoteId),name);
		for(QuoteProcess qp:quoteProcessList){
			if(StringUtils.isNotEmpty(qp.getBsMaterName())){
				if(quoteBomDao.findByDelFlagAndBsMaterName(0,qp.getBsMaterName()).size()==0){
					return ApiResponseResult.failure("外购件清单中不存在 "+qp.getBsMaterName()+" 的材料名称");
				}
			}
		}
		quoteProcessDao.saveAll(quoteProcessList);
		return ApiResponseResult.success();
	}

	@Override
	public ApiResponseResult addProcessByBom(Long quoteId) {
	 	//根据bom下发工艺
	 	List<QuoteBom> quoteBomList = quoteBomDao.findByDelFlagAndPkQuoteOrderById(0,quoteId);
	 	List<QuoteProcess> quoteProcessList = new ArrayList<>();
	 	try {
			Long userId = UserUtil.getSessionUser().getId();
			Date date = new Date();
			for(Integer i  =0;i<quoteBomList.size();i++){
				QuoteBom o = quoteBomList.get(i);
				QuoteProcess quoteProcess = new QuoteProcess();
				quoteProcess.setBsGroups(o.getBsGroups());
				quoteProcess.setPkQuote(quoteId);
				quoteProcess.setPkQuoteBom(o.getId());
//				quoteProcess.setQuoteBom(o);
				quoteProcess.setBsMaterName(o.getBsMaterName());
				quoteProcess.setBsElement(o.getBsElement());
				quoteProcess.setItemType(o.getItp().getItemType());
				quoteProcess.setBsName(o.getBsComponent());
				quoteProcess.setBsSingleton(o.getBsSingleton());
				quoteProcess.setCreateBy(userId);
				quoteProcess.setCreateDate(date);
				quoteProcess.setPkWorkCenter(o.getPkBjWorkCenter());
				quoteProcess.setBsOrder((i+1)*10);
				quoteProcess.setPurchaseUnit(o.getPurchaseUnit()); //单位为PCS 不参与人工和制费计算
				quoteProcessList.add(quoteProcess);
			}

			quoteProcessDao.saveAll(quoteProcessList);
			updateProcess(quoteId);
		}catch (Exception e){
	 		e.printStackTrace();
		}
		return ApiResponseResult.success();
	}

	@Override
	public ApiResponseResult editProcessByBom(List<QuoteProcess> quoteProcessList,Long quoteId) {
		List<String> newName =Arrays.asList("测试","检验","包装");
	 	//复制后的确认完成，1.更新工艺的bom关联关系(可能已删除)，2.根据新增bom(如有)下发工艺
		Boolean sameQuote = false;
		//查出对应的bom是否为同一个标价单下,相同则根据bomID查询 ，不同则根据bomID2查询
		if(quoteBomDao.findById((long) quoteProcessList.get(0).getPkQuoteBom()).getPkQuote().equals(quoteId)){
			sameQuote = true;
		}else {
			Quote quote = quoteDao.findById((long)quoteId);
			if(quote.getBsCopyId()==null){
				sameQuote = true;
			}
		}
		for(QuoteProcess o :quoteProcessList) {
			if (o.getPkQuoteBom()!=null) {
				QuoteBom quoteBom = new QuoteBom();
				if (sameQuote) {
					quoteBom = quoteBomDao.findById((long) o.getPkQuoteBom());
				} else {
					quoteBom = quoteBomDao.findByPkBomId2AndPkQuote(o.getPkQuoteBom(), quoteId);
					o.setBsMaterName(quoteBom.getBsMaterName());
					o.setPurchaseUnit(quoteBom.getPurchaseUnit()); //单位为PCS 不参与人工和制费计算
				}
				o.setDelFlag(quoteBom.getDelFlag());
				o.setPkQuoteBom(quoteBom.getId());
				o.setBsElement(quoteBom.getBsElement());
				o.setBsName(quoteBom.getBsComponent());
//				if(quoteBom.getBsSingleton().equals(o.getBsSingleton())){
//
//				}
				o.setBsSingleton(quoteBom.getBsSingleton());
				o.setPkWorkCenter(quoteBom.getPkBjWorkCenter());
				o.setItemType(quoteBom.getItp().getItemType());
				o.setBsGroups(quoteBom.getBsGroups());
			} else if(newName.contains(o.getBsName())){

			}
		}
//		quoteProcessDao.saveAll(quoteProcessList);

		List<QuoteBom> quoteBomList = quoteBomDao.findByDelFlagAndPkQuoteAndPkBomIdIsNull(0,quoteId);
//		List<QuoteProcess> quoteProcessList2 = new ArrayList<>();
		try {
			Long userId = UserUtil.getSessionUser().getId();
			Date date = new Date();
			for(Integer i  =0;i<quoteBomList.size();i++){
				QuoteBom o = quoteBomList.get(i);
				QuoteProcess quoteProcess = new QuoteProcess();
				quoteProcess.setBsGroups(o.getBsGroups());
				quoteProcess.setPkQuote(quoteId);
				quoteProcess.setPkQuoteBom(o.getId());
				quoteProcess.setBsMaterName(o.getBsMaterName());
				quoteProcess.setBsElement(o.getBsElement());
				quoteProcess.setItemType(o.getItp().getItemType());
				quoteProcess.setBsName(o.getBsComponent());
				quoteProcess.setBsSingleton(o.getBsSingleton());
				quoteProcess.setCreateBy(userId);
				quoteProcess.setCreateDate(date);
				quoteProcess.setPkWorkCenter(o.getPkBjWorkCenter());
				quoteProcess.setBsOrder((i+1)*10);
				quoteProcess.setPurchaseUnit(o.getPurchaseUnit()); //单位为PCS 不参与人工和制费计算
				quoteProcessList.add(quoteProcess);
			}
			quoteProcessDao.saveAll(quoteProcessList);
			updateProcess(quoteId);
			quoteProcessDao.deletByBsSingleton(quoteId,newName);
		}catch (Exception e){
			e.printStackTrace();
		}

		return ApiResponseResult.success();
	}


	@Override
	public ApiResponseResult getSumList(Long quoteId,PageRequest pageRequest) throws Exception {
		Page<Map<String, Object>> mapList= quoteProcessDao.getSumList(quoteId,pageRequest);
//		DataGrid.create(mapList.getContent(), (int) mapList.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize())
		List<Map<String,Object>> maps =  new ArrayList<>();
		List<Map<String,Object>> mapOld = mapList.getContent();
		HashSet<String> groupSet = new HashSet<>();
		for (int i = mapOld.size()-1; i >= 0; i--) {
			Map<String, Object> subtotal = new HashMap<>();
			Map<String, Object> one = mapOld.get(i);
			Map<String, Object> deepCopy = new HashMap<>();
			//根据组件和所属零件判断是否有小结
			if(groupSet.add(one.get("BS_ELEMENT").toString()+one.get("BS_LINK_NAME")+one.get("BS_ORDERS"))){
				//新增小结
				subtotal.put("BS_ELEMENT",one.get("BS_ELEMENT"));
				subtotal.put("BS_LINK_NAME",one.get("BS_LINK_NAME"));
//			  subtotal.put("BS_ORDERS",one.get("BS_ORDERS"));
				maps.add(subtotal);
			}
			deepCopy.putAll(one);
			maps.add(deepCopy);
			//根据组件和所属零件判断是否新增小计计算
		}
		Collections.reverse(maps);
		return ApiResponseResult.success().data(DataGrid.create(maps, (int) mapList.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}
