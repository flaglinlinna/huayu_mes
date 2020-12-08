package com.web.basePrice.service.internal;

import java.text.SimpleDateFormat;
import java.util.*;

import com.system.user.dao.SysUserDao;

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
import com.web.basePrice.dao.BaseFeeDao;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.BaseFee;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.BaseFeeService;
import com.web.basic.dao.WorkCenterDao;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.WorkCenter;

/**
 *
 * @date Dec 8, 2020 11:57:13 AM
 */
@Service(value = "BaseFeeService")
@Transactional(propagation = Propagation.REQUIRED)
public class BaseFeeImpl extends BasePriceUtils implements BaseFeeService {
	@Autowired
	private BaseFeeDao baseFeeDao;

	@Autowired
	private SysUserDao sysUserDao;
		
	/**
	 * 新增人工制费信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(BaseFee baseFee) throws Exception {
		if (baseFee == null) {
			return ApiResponseResult.failure("人工制费信息不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getWorkcenterId().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getProcName())) {
			return ApiResponseResult.failure("工序不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeLh())) {
			return ApiResponseResult.failure("人工费率不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeMh())) {
			return ApiResponseResult.failure("制费费率不能为空！");
		}
		baseFee.setCreateDate(new Date());
		baseFee.setCreateBy(UserUtil.getSessionUser().getId());
		baseFeeDao.save(baseFee);
		return ApiResponseResult.success("人工制费信息添加成功！").data(baseFee);
	}

	/**
	 * 修改人工制费
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(BaseFee baseFee) throws Exception {
		if (baseFee == null) {
			return ApiResponseResult.failure("人工制费信息不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getWorkcenterId().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getProcName())) {
			return ApiResponseResult.failure("工序不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeLh())) {
			return ApiResponseResult.failure("人工费率不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeMh())) {
			return ApiResponseResult.failure("制费费率不能为空！");
		}
		BaseFee o = baseFeeDao.findById((long) baseFee.getId());
		if (o == null) {
			return ApiResponseResult.failure("该人工制费不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		//o.setEnabled(baseFee.getEnabled());
		o.setWorkcenterId(baseFee.getWorkcenterId());
	    o.setProcName(baseFee.getProcName());
	    o.setMhType(baseFee.getMhType());
	    o.setFeeLh(baseFee.getFeeLh());
	    o.setFeeMh(baseFee.getFeeMh());
		baseFeeDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除人工制费
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("人工制费ID不能为空！");
		}
		BaseFee o = baseFeeDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("人工制费不存在！");
		}
		o.setDelFlag(1);
		baseFeeDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("人工制费ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		BaseFee o = baseFeeDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("此人工制费信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		baseFeeDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询人工制费信息维护列表
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
			filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mhType", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<BaseFee> spec = Specification.where(BaseService.and(filters, BaseFee.class));
		Specification<BaseFee> spec1 = spec.and(BaseService.or(filters1, BaseFee.class));
		Page<BaseFee> page = baseFeeDao.findAll(spec1, pageRequest);
		List<BaseFee> baseFeeList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BaseFee baseFee : baseFeeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", baseFee.getId());
			map.put("workcenterId", baseFee.getWorkcenterId());
			//map.put("workcenter", baseFee.getWorkCenter().getWorkcenterName());
			map.put("enabled", baseFee.getEnabled());
			map.put("procName", baseFee.getProcName());
			map.put("mhType", baseFee.getMhType());
			
			map.put("feeLh", baseFee.getFeeLh());
			map.put("feeMh", baseFee.getFeeMh());
			
			map.put("createBy", sysUserDao.findById((long) baseFee.getCreateBy()).getUserName());
			map.put("createDate", df.format(baseFee.getCreateDate()));
			if (baseFee.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) baseFee.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(baseFee.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	@Override
	public ApiResponseResult getProcList(String type, String condition,PageRequest pageRequest)throws Exception{
				List<Object> list = getBJProcPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,condition,pageRequest);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Total", list.get(2));
				map.put("List", list.get(3));
				return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getType(String keyword ,PageRequest pageRequest)throws Exception{
		List<Object> list = getSystemSubParamPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getId() + "",keyword,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Total", list.get(2));
		map.put("List", list.get(3));
		return ApiResponseResult.success().data(map);
	}
}