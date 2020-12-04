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
import com.web.basePrice.dao.ProdTypDao;
import com.web.basePrice.entity.ProdTyp;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.ProdTypService;
import com.web.basic.entity.Mtrial;

/**
 *
 * @date Dec 4, 2020 5:31:25 PM
 */
@Service(value = "ProdTypService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProdTypImpl implements ProdTypService {
	@Autowired
	private ProdTypDao prodTypDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增产品类型信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(ProdTyp prodTyp) throws Exception {
		if (prodTyp == null) {
			return ApiResponseResult.failure("产品类型信息不能为空！");
		}
		if (StringUtils.isEmpty(prodTyp.getProductType())) {
			return ApiResponseResult.failure("产品类型不能为空！");
		}
		
		int count = prodTypDao.countByDelFlagAndProductType(0, prodTyp.getProductType());
		if (count > 0) {
			return ApiResponseResult.failure("该产品类型已存在，请填写其他产品类型！");
		}
		prodTyp.setCreateDate(new Date());
		prodTyp.setCreateBy(UserUtil.getSessionUser().getId());
		prodTypDao.save(prodTyp);
		return ApiResponseResult.success("产品类型添加成功！").data(prodTyp);
	}

	/**
	 * 修改产品类型
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(ProdTyp prodTyp) throws Exception {
		if (prodTyp == null) {
			return ApiResponseResult.failure("产品类型不能为空！");
		}
		if (prodTyp.getId() == null) {
			return ApiResponseResult.failure("产品类型ID不能为空！");
		}
		if (StringUtils.isEmpty(prodTyp.getProductType())) {
			return ApiResponseResult.failure("产品类型不能为空！");
		}
		ProdTyp o = prodTypDao.findById((long) prodTyp.getId());
		if (o == null) {
			return ApiResponseResult.failure("该产品类型不存在！");
		}
		// 判断产品类型是否有变化，有则修改；没有则不修改
		if (o.getProductType().equals(prodTyp.getProductType())) {
		} else {
			int count = prodTypDao.countByDelFlagAndProductType(0, prodTyp.getProductType());
			if (count > 0) {
				return ApiResponseResult.failure("产品类型已存在，请填写其他产品类型！");
			}
			o.setProductType(prodTyp.getProductType().trim());
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(prodTyp.getEnabled());
		prodTypDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除产品类型
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("产品类型ID不能为空！");
		}
		ProdTyp o = prodTypDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("产品类型不存在！");
		}
		o.setDelFlag(1);
		prodTypDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("产品类型ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		ProdTyp o = prodTypDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("产品类型不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		prodTypDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询产品类型信息维护列表
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
			filters1.add(new SearchFilter("productType", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<ProdTyp> spec = Specification.where(BaseService.and(filters, ProdTyp.class));
		Specification<ProdTyp> spec1 = spec.and(BaseService.or(filters1, ProdTyp.class));
		Page<ProdTyp> page = prodTypDao.findAll(spec1, pageRequest);
		List<ProdTyp> prodTypList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (ProdTyp prodTyp : prodTypList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", prodTyp.getId());
			map.put("productType", prodTyp.getProductType());
			map.put("enabled", prodTyp.getEnabled());
			map.put("createBy", sysUserDao.findById((long) prodTyp.getCreateBy()).getUserName());
			map.put("createDate", df.format(prodTyp.getCreateDate()));
			if (prodTyp.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) prodTyp.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(prodTyp.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}