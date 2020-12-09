package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ItemTypeWgDao;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.service.ItemTypeWgService;
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


@Service(value = "ItemTypeWgService")
@Transactional(propagation = Propagation.REQUIRED)
public class ItemTypeWgImpl extends  BasePriceUtils implements ItemTypeWgService {
	@Autowired
	private ItemTypeWgDao itemTypeWgDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增外购物料类型
	 */
	@Override
	@Transactional
	public ApiResponseResult add(ItemTypeWg itemTypeWg) throws Exception {
		if (itemTypeWg == null) {
			return ApiResponseResult.failure("外购物料类型不能为空！");
		}
		itemTypeWg.setCreateDate(new Date());
		itemTypeWg.setCreateBy(UserUtil.getSessionUser().getId());
		itemTypeWgDao.save(itemTypeWg);
		return ApiResponseResult.success("外购物料类型添加成功！").data(itemTypeWg);
	}

	/**
	 * 修改外购物料类型
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(ItemTypeWg itemTypeWg) throws Exception {
		if (itemTypeWg == null) {
			return ApiResponseResult.failure("外购物料类型不能为空！");
		}
		if (itemTypeWg.getId() == null) {
			return ApiResponseResult.failure("外购物料类型信息ID不能为空！");
		}
		ItemTypeWg o = itemTypeWgDao.findById((long) itemTypeWg.getId());
		if (o == null) {
			return ApiResponseResult.failure("该外购物料类型信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setItemType(itemTypeWg.getItemType());
	    o.setFmemo(itemTypeWg.getFmemo());
		itemTypeWgDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}



	/**
	 * 删除客户品质标准信息
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("外购物料类型信息ID不能为空！");
		}
		ItemTypeWg o = itemTypeWgDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("外购物料类型信息不存在！");
		}
		o.setDelFlag(1);
		itemTypeWgDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}



	/**
	 * 查询客户品质标准信息列表
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
			filters1.add(new SearchFilter("itemType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("fmemo", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<ItemTypeWg> spec = Specification.where(BaseService.and(filters, ItemTypeWg.class));
		Specification<ItemTypeWg> spec1 = spec.and(BaseService.or(filters1, ItemTypeWg.class));
		Page<ItemTypeWg> page = itemTypeWgDao.findAll(spec1, pageRequest);
		List<ItemTypeWg> customQsList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (ItemTypeWg itemTypeWg : customQsList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", itemTypeWg.getId());
			map.put("itemType", itemTypeWg.getItemType());
			map.put("fmemo", itemTypeWg.getFmemo());
			map.put("createBy", sysUserDao.findById((long) itemTypeWg.getCreateBy()).getUserName());
			map.put("createDate", df.format(itemTypeWg.getCreateDate()));
			if (itemTypeWg.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) itemTypeWg.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(itemTypeWg.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	


}