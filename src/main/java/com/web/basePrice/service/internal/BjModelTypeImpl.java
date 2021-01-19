package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;

import com.web.basePrice.dao.BjModelTypeDao;
import com.web.basePrice.entity.BjModelType;
import com.web.basePrice.service.BjModelTypeService;
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

/**
 *
 */
@Service(value = "BjModelTypeService")
@Transactional(propagation = Propagation.REQUIRED)
public class BjModelTypeImpl extends BasePriceUtils implements BjModelTypeService {
	@Autowired
	private BjModelTypeDao bjModelTypeDao;

	@Autowired
	private SysUserDao sysUserDao;
		
	/**
	 * 新增人工制费信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(BjModelType bjModelType) throws Exception {
		if (bjModelType == null) {
			return ApiResponseResult.failure("人工制费信息不能为空！");
		}
		if (StringUtils.isEmpty(bjModelType.getPkWorkcenter().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}

		bjModelType.setCreateDate(new Date());
		bjModelType.setCreateBy(UserUtil.getSessionUser().getId());
		bjModelTypeDao.save(bjModelType);
		return ApiResponseResult.success("人工制费信息添加成功！").data(bjModelType);
	}

	/**
	 * 修改人工制费
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(BjModelType bjModelType) throws Exception {
		if (bjModelType == null) {
			return ApiResponseResult.failure("人工制费信息不能为空！");
		}
		if (StringUtils.isEmpty(bjModelType.getPkWorkcenter().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}

		BjModelType o = bjModelTypeDao.findById((long) bjModelType.getId());
		if (o == null) {
			return ApiResponseResult.failure("该人工制费不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setPkWorkcenter(bjModelType.getPkWorkcenter());
	    o.setModelCode(bjModelType.getModelCode());
	    o.setModelName(bjModelType.getModelName());
		bjModelTypeDao.save(o);
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
		BjModelType o = bjModelTypeDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("人工制费不存在！");
		}
		o.setDelFlag(1);
		bjModelTypeDao.save(o);
		return ApiResponseResult.success("删除成功！");
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
			filters1.add(new SearchFilter("modelCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("modelName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<BjModelType> spec = Specification.where(BaseService.and(filters, BjModelType.class));
		Specification<BjModelType> spec1 = spec.and(BaseService.or(filters1, BjModelType.class));
		Page<BjModelType> page = bjModelTypeDao.findAll(spec1, pageRequest);
		List<BjModelType> baseFeeList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BjModelType baseFee : baseFeeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", baseFee.getId());
			map.put("workcenterId", baseFee.getPkWorkcenter());
			map.put("workcenter", baseFee.getWorkCenter().getWorkcenterName());
			map.put("modelCode", baseFee.getModelCode());
			map.put("modelName", baseFee.getModelName());
			map.put("createBy", sysUserDao.findById((long) baseFee.getCreateBy()).getUserName());
			map.put("createDate", df.format(baseFee.getCreateDate()));
			if (baseFee.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) baseFee.getLastupdateBy()).getUserName());
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
	
	@Override
	public ApiResponseResult getWorkCenterList(String type, String condition,PageRequest pageRequest)throws Exception{
				List<Object> list = getBJWorkCenterPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
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
	public ApiResponseResult doCheckInfo(String type,String input1,String input2,
			String input3,String input4)throws Exception{
				List<Object> list = chkCenterAndProcPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,input1,input2,input3,input4);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				return ApiResponseResult.success();
	}
}