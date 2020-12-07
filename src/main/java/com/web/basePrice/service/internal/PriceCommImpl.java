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
import com.web.basePrice.dao.PriceCommDao;
import com.web.basePrice.entity.PriceComm;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.PriceCommService;
import com.web.basic.entity.Mtrial;

/**
 *
 * @date Dec 7, 2020 5:11:13 PM
 */
@Service(value = "PriceCommService")
@Transactional(propagation = Propagation.REQUIRED)
public class PriceCommImpl implements PriceCommService {
	@Autowired
	private PriceCommDao priceCommDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增物料通用价格信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(PriceComm priceComm) throws Exception {
		if (priceComm == null) {
			return ApiResponseResult.failure("物料通用价格信息不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getItemName())) {
			return ApiResponseResult.failure("物料名称不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getPriceUn())) {
			return ApiResponseResult.failure("单价不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getUnitId())) {
			return ApiResponseResult.failure("单位不能为空！");
		}
		
		priceComm.setCreateDate(new Date());
		priceComm.setCreateBy(UserUtil.getSessionUser().getId());
		priceCommDao.save(priceComm);
		return ApiResponseResult.success("物料通用价格添加成功！").data(priceComm);
	}

	/**
	 * 修改物料通用价格
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(PriceComm priceComm) throws Exception {
		if (priceComm == null) {
			return ApiResponseResult.failure("物料通用价格不能为空！");
		}
		if (priceComm.getId() == null) {
			return ApiResponseResult.failure("物料通用价格ID不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getItemName())) {
			return ApiResponseResult.failure("物料名称不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getPriceUn())) {
			return ApiResponseResult.failure("单价不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getUnitId())) {
			return ApiResponseResult.failure("单位不能为空！");
		}
		PriceComm o = priceCommDao.findById((long) priceComm.getId());
		if (o == null) {
			return ApiResponseResult.failure("该物料通用价格不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(priceComm.getEnabled());
		priceCommDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除物料通用价格
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("物料通用价格ID不能为空！");
		}
		PriceComm o = priceCommDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("物料通用价格不存在！");
		}
		o.setDelFlag(1);
		priceCommDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("物料通用价格ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		PriceComm o = priceCommDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("此物料通用价格信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		priceCommDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询物料通用价格信息维护列表
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
			filters1.add(new SearchFilter("itemName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("rangePrice", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("alternativeSuppliers", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<PriceComm> spec = Specification.where(BaseService.and(filters, PriceComm.class));
		Specification<PriceComm> spec1 = spec.and(BaseService.or(filters1, PriceComm.class));
		Page<PriceComm> page = priceCommDao.findAll(spec1, pageRequest);
		List<PriceComm> priceCommList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (PriceComm priceComm : priceCommList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", priceComm.getId());
			map.put("itemName", priceComm.getItemName());
			map.put("rangePrice", priceComm.getRangePrice());
			map.put("priceUn", priceComm.getPriceUn());
			map.put("unit", priceComm.getUnit().getUnitName());
			map.put("alternativeSuppliers", priceComm.getAlternativeSuppliers());
			map.put("enabled", priceComm.getEnabled());
			map.put("createBy", sysUserDao.findById((long) priceComm.getCreateBy()).getUserName());
			map.put("createDate", df.format(priceComm.getCreateDate()));
			if (priceComm.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) priceComm.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(priceComm.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}