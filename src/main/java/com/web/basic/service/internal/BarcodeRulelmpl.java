package com.web.basic.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.produce.service.internal.PrcUtils;
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
import com.web.basic.dao.MtrialDao;
import com.web.basic.dao.BarcodeRuleDao;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.BarcodeRule;
import com.web.basic.service.BarcodeRuleService;

@Service(value = "barcodeRuleService")
@Transactional(propagation = Propagation.REQUIRED)
public class BarcodeRulelmpl extends PrcUtils implements BarcodeRuleService {
	@Autowired
	private BarcodeRuleDao barcodeRuleDao;

	@Autowired
	private MtrialDao mtrialDao;

	@Override
	public ApiResponseResult getMtrial(String keyword,PageRequest pageRequest) throws Exception{
		List<Object> list = getReworkItemPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","成品",keyword,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	//getCustomerProc
	public ApiResponseResult getCustomer(String keyword,PageRequest pageRequest) throws Exception{
		List<Object> list = getCustomerProc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				"客户",keyword,pageRequest,"prc_mes_cof_customer_chs");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	//prc_mes_cof_bar_s_list
	@Override
	public ApiResponseResult getBarList(String type,PageRequest pageRequest) throws Exception{
		List<Object> list1 = getBarListPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				"年","",pageRequest,"prc_mes_cof_bar_s_list");
		if (!list1.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list1.get(1).toString());
		}
		List<Object> list2 = getBarListPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				"月","",pageRequest,"prc_mes_cof_bar_s_list");
		if (!list2.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list2.get(1).toString());
		}
		List<Object> list3 = getBarListPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				"日","",pageRequest,"prc_mes_cof_bar_s_list");
		if (!list3.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list3.get(1).toString());
		}
		List<Object> list4 = getBarListPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				"流水号","",pageRequest,"prc_mes_cof_bar_s_list");
		if (!list4.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list4.get(1).toString());
		}
		Map<String,Object> map =  new HashMap<>();
		map.put("fyear",list1.get(3));
		map.put("fmonth",list2.get(3));
		map.put("fday",list3.get(3));
		map.put("fserialNum",list4.get(3));
		return ApiResponseResult.success().data(map);
//		return ApiResponseResult.success().data(list.get(2).toString());
	}


	@Override
	public ApiResponseResult getFsampleByForm(String fixValue, String fyear , String fmonth,String fday,
											  String serialNum, String serialLen) throws Exception{
		List<Object> list = getFsamplePrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				fixValue,fyear,fmonth,fday,serialNum,serialLen,"prc_mes_cof_bar_s_join");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success("").data(list.get(2));
	}
//	getFsampleString


	/**
	 * 新增校验规则
	 */
	@Override
	@Transactional
	public ApiResponseResult add(BarcodeRule barcodeRule) throws Exception {
		if (barcodeRule == null) {
			return ApiResponseResult.failure("校验规则不能为空！");
		}
		if (StringUtils.isEmpty(barcodeRule.getItemNo())) {
			return ApiResponseResult.failure("物料编码不能为空！");
		}
		barcodeRule.setCreateDate(new Date());
		barcodeRule.setCreateBy(UserUtil.getSessionUser().getId());
		barcodeRuleDao.save(barcodeRule);
		return ApiResponseResult.success("校验规则添加成功！").data(barcodeRule);
	}

	@Override
	public ApiResponseResult addByProc(BarcodeRule barcodeRule) throws Exception{
		List<Object> list = addRuleByProc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				barcodeRule.getItemNo(),barcodeRule.getItemId()+"",barcodeRule.getItemNoCus(),barcodeRule.getCustId(),
				barcodeRule.getFmemo(), barcodeRule.getFixValue(),barcodeRule.getFyear(),barcodeRule.getFmonth(),
				barcodeRule.getFday(), barcodeRule.getSerialNum(),barcodeRule.getSerialLen(),
				barcodeRule.getFsample(),"prc_mes_cof_bar_s_role_save");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success("");
	}

	/**
	 * 修改校验规则
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(BarcodeRule barcodeRule) throws Exception {
		if (barcodeRule == null) {
			return ApiResponseResult.failure("校验规则不能为空！");
		}
		if (barcodeRule.getId() == null) {
			return ApiResponseResult.failure("校验规则ID不能为空！");
		}
		 if(StringUtils.isEmpty(barcodeRule.getItemNo())){
		 return ApiResponseResult.failure("物料编码不能为空！");
		 }
		// if(StringUtils.isEmpty(barcodeRule.getDefectName())){
		// return ApiResponseResult.failure("不良名称不能为空！");
		// }
		BarcodeRule o = barcodeRuleDao.findById((long) barcodeRule.getId());
		if (o == null) {
			return ApiResponseResult.failure("该校验规则不存在！");
		}

		o.setItemId(barcodeRule.getItemId());
		o.setItemNo(barcodeRule.getItemNo());
		o.setItemNoCus(barcodeRule.getItemNoCus());
		o.setCustId(barcodeRule.getCustId());
		o.setFday(barcodeRule.getFday());
		o.setFmonth(barcodeRule.getFmonth());
		o.setFday(barcodeRule.getFday());
		o.setFixValue(barcodeRule.getFixValue());
		o.setFsample(barcodeRule.getFsample());
		o.setSerialNum(barcodeRule.getSerialNum());
		o.setSerialLen(barcodeRule.getSerialLen());
		o.setFmemo(barcodeRule.getFmemo());
		o.setFsample(barcodeRule.getFsample());

		
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		barcodeRuleDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 根据ID获取
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getBarcodeRule(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("校验规则ID不能为空！");
		}
		BarcodeRule o = barcodeRuleDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该校验规则不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除校验规则
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(String ids) throws Exception {
		if(StringUtils.isEmpty(ids)){
			return ApiResponseResult.failure("线体ID不能为空！");
		}
		String[] id_s = ids.split(",");
		List<BarcodeRule> ll = new ArrayList<>();
		for(String id:id_s){
			BarcodeRule o  = barcodeRuleDao.findById(Long.parseLong(id));
			if(o != null){
				o.setDelTime(new Date());
				o.setDelFlag(1);
				o.setDelBy(UserUtil.getSessionUser().getId());
				ll.add(o);
			}
		}
		barcodeRuleDao.saveAll(ll);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getListByPrc(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<Object> list1 = getBarListPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				"列表",keyword,pageRequest,"prc_mes_cof_bar_s_list");
		if (!list1.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list1.get(1).toString());
		}
		Map<String,Object> map =  new HashMap<>();
		map.put("total",list1.get(2));
		map.put("rows",list1.get(3));
		return ApiResponseResult.success().data(map);
	}

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("itemNoCus", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("itemNoInside", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mtrial.itemNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mtrial.itemName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<BarcodeRule> spec = Specification.where(BaseService.and(filters, BarcodeRule.class));
		Specification<BarcodeRule> spec1 = spec.and(BaseService.or(filters1, BarcodeRule.class));
		Page<BarcodeRule> page = barcodeRuleDao.findAll(spec1, pageRequest);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (BarcodeRule bs : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			if(bs.getMtrial()!=null) {
				map.put("itemNo", bs.getMtrial().getItemNo());//获取关联表的数据
				map.put("itemName", bs.getMtrial().getItemName());
			}
			map.put("itemNoCus", bs.getItemNoCus());
//			map.put("itemNoInside", bs.getItemNoInside());
//			map.put("positionBegin", bs.getPositionBegin());
//			map.put("positionEnd", bs.getPositionEnd());
//			map.put("chkString", bs.getChkString());
//			map.put("barcodeLen", bs.getBarcodeLen());
			map.put("lastupdateDate", bs.getLastupdateDate());
			map.put("createDate", bs.getCreateDate());
			list.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 获取物料信息
	 */
	@Override
	@Transactional
	public ApiResponseResult getMtrialList() throws Exception {
		List<Mtrial> list = mtrialDao.findByDelFlagAndCheckStatus(0, 1);
		return ApiResponseResult.success().data(list);
	}


	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getMtrialList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("checkStatus", SearchFilter.Operator.EQ, 1));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("itemNo", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Mtrial> spec = Specification.where(BaseService.and(filters, Mtrial.class));
		Specification<Mtrial> spec1 = spec.and(BaseService.or(filters1, Mtrial.class));
		Page<Mtrial> page = mtrialDao.findAll(spec1, pageRequest);
		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}
