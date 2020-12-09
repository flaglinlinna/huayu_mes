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
import com.web.basePrice.dao.MjProcPriceDao;
import com.web.basePrice.entity.MjProcPrice;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.MjProcPriceService;
import com.web.basic.entity.Mtrial;

/**
 *
 * @date Dec 8, 2020 5:48:46 PM
 */
@Service(value = "MjProcPriceService")
@Transactional(propagation = Propagation.REQUIRED)
public class MjProcPriceImpl extends BasePriceUtils implements MjProcPriceService {
	@Autowired
	private MjProcPriceDao mjProcPriceDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增模具加工费率信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(MjProcPrice mjProcPrice) throws Exception {
		if (mjProcPrice == null) {
			return ApiResponseResult.failure("模具加工费率信息不能为空！");
		}
		if (StringUtils.isEmpty(mjProcPrice.getProcName())) {
			return ApiResponseResult.failure("工序不能为空！");
		}
		if (StringUtils.isEmpty(mjProcPrice.getfPrice())) {
			return ApiResponseResult.failure("工序费用不能为空！");
		}
		
		mjProcPrice.setCreateDate(new Date());
		mjProcPrice.setCreateBy(UserUtil.getSessionUser().getId());
		mjProcPriceDao.save(mjProcPrice);
		return ApiResponseResult.success("模具加工费率添加成功！").data(mjProcPrice);
	}

	/**
	 * 修改模具加工费率
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(MjProcPrice mjProcPrice) throws Exception {
		if (mjProcPrice == null) {
			return ApiResponseResult.failure("模具加工费率信息不能为空！");
		}
		if (StringUtils.isEmpty(mjProcPrice.getProcName())) {
			return ApiResponseResult.failure("工序不能为空！");
		}
		if (StringUtils.isEmpty(mjProcPrice.getfPrice())) {
			return ApiResponseResult.failure("工序费用不能为空！");
		}
		MjProcPrice o = mjProcPriceDao.findById((long) mjProcPrice.getId());
		if (o == null) {
			return ApiResponseResult.failure("该模具加工费率不存在！");
		}
		
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setfPrice(mjProcPrice.getfPrice());
		o.setProcName(mjProcPrice.getProcName());
		mjProcPriceDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除模具加工费率
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("模具加工费率ID不能为空！");
		}
		MjProcPrice o = mjProcPriceDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("模具加工费率不存在！");
		}
		o.setDelFlag(1);
		mjProcPriceDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("模具加工费率ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		MjProcPrice o = mjProcPriceDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("模具加工费率不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		mjProcPriceDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询模具加工费率信息维护列表
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
			filters1.add(new SearchFilter("fPrice", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<MjProcPrice> spec = Specification.where(BaseService.and(filters, MjProcPrice.class));
		Specification<MjProcPrice> spec1 = spec.and(BaseService.or(filters1, MjProcPrice.class));
		Page<MjProcPrice> page = mjProcPriceDao.findAll(spec1, pageRequest);
		List<MjProcPrice> mjProcPriceList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (MjProcPrice mjProcPrice : mjProcPriceList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", mjProcPrice.getId());	
			map.put("procName", mjProcPrice.getProcName());
			map.put("fPrice", mjProcPrice.getfPrice());	
			map.put("enabled", mjProcPrice.getEnabled());
			map.put("createBy", sysUserDao.findById((long) mjProcPrice.getCreateBy()).getUserName());
			map.put("createDate", df.format(mjProcPrice.getCreateDate()));
			if (mjProcPrice.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) mjProcPrice.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(mjProcPrice.getLastupdateDate()));
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
}