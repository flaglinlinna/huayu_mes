package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ItemTypeWgRoleDao;
import com.web.basePrice.entity.ItemTypeWgRole;
import com.web.basePrice.service.ItemTypeWgRoleService;
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


@Service(value = "ItemTypeWgRoleService")
@Transactional(propagation = Propagation.REQUIRED)
public class ItemTypeWgRoleImpl extends  BasePriceUtils implements ItemTypeWgRoleService {
	@Autowired
	private ItemTypeWgRoleDao itemTypeWgRoleDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增外购物料类型
	 */
	@Override
	@Transactional
	public ApiResponseResult add(ItemTypeWgRole itemTypeWgRole) throws Exception {
		if (itemTypeWgRole == null) {
			return ApiResponseResult.failure("外购物料类型不能为空！");
		}
		itemTypeWgRole.setCreateDate(new Date());
		itemTypeWgRole.setCreateBy(UserUtil.getSessionUser().getId());
		itemTypeWgRoleDao.save(itemTypeWgRole);
		return ApiResponseResult.success("类型关联角色添加成功！").data(itemTypeWgRole);
	}

	/**
	 * 新增外购物料类型
	 */
	@Override
	@Transactional
	public ApiResponseResult add(Long pkItemTypeWg, String roleIds) throws Exception {
		if (pkItemTypeWg == null) {
			return ApiResponseResult.failure("外购物料类型不能为空！");
		}
		Date addTime = new Date();
		if(StringUtils.isNotEmpty(roleIds)){
			//先删除,后新增
			itemTypeWgRoleDao.deleteItemTypeWgRoleByPkItemTypeWg(pkItemTypeWg);
			List<ItemTypeWgRole> itemTypeWgRoleList = new ArrayList<>();
			String[] role_id = roleIds.split(",");
			for(String roleId :role_id){
				ItemTypeWgRole itemTypeWgRole = new ItemTypeWgRole();
				itemTypeWgRole.setPkItemTypeWg(pkItemTypeWg);
				itemTypeWgRole.setPkSysRole(Long.parseLong(roleId));
				itemTypeWgRole.setCreateBy(UserUtil.getSessionUser().getId());
				itemTypeWgRole.setCreateDate(addTime);
				itemTypeWgRoleList.add(itemTypeWgRole);
			}
			itemTypeWgRoleDao.saveAll(itemTypeWgRoleList);
		}
		return ApiResponseResult.success("添加成功！");
	}

	/**
	 * 修改外购物料类型
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(ItemTypeWgRole itemTypeWgRole) throws Exception {
		if (itemTypeWgRole == null) {
			return ApiResponseResult.failure("外购物料类型不能为空！");
		}
		if (itemTypeWgRole.getId() == null) {
			return ApiResponseResult.failure("外购物料类型信息ID不能为空！");
		}
		ItemTypeWgRole o = itemTypeWgRoleDao.findById((long) itemTypeWgRole.getId());
		if (o == null) {
			return ApiResponseResult.failure("该外购物料类型信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
//		o.setItemType(itemTypeWg.getItemType());
//	    o.setFmemo(itemTypeWg.getFmemo());
		itemTypeWgRoleDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}


	@Override
	public ApiResponseResult getByWgId(Long wgId) throws Exception {
		return ApiResponseResult.success().data(itemTypeWgRoleDao.findByDelFlagAndPkItemTypeWg(0,wgId));
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
		ItemTypeWgRole o = itemTypeWgRoleDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("外购物料类型信息不存在！");
		}
		o.setDelFlag(1);
		itemTypeWgRoleDao.save(o);
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
			filters1.add(new SearchFilter("itemTypeWg.itemType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("sysRole.roleName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<ItemTypeWgRole> spec = Specification.where(BaseService.and(filters, ItemTypeWgRole.class));
		Specification<ItemTypeWgRole> spec1 = spec.and(BaseService.or(filters1, ItemTypeWgRole.class));
		Page<ItemTypeWgRole> page = itemTypeWgRoleDao.findAll(spec1, pageRequest);
		List<ItemTypeWgRole> customQsList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (ItemTypeWgRole itemTypeWg : customQsList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", itemTypeWg.getId());
			map.put("itemType", itemTypeWg.getItemTypeWg().getItemType());
			map.put("pkItemTypeWg",itemTypeWg.getPkItemTypeWg());
			map.put("roleName",itemTypeWg.getSysRole().getRoleName());
//			map.put("fmemo", itemTypeWg.getFmemo());
			map.put("createBy", sysUserDao.findById((long) itemTypeWg.getCreateBy()).getUserName());
			map.put("createDate", df.format(itemTypeWg.getCreateDate()));
			if (itemTypeWg.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) itemTypeWg.getLastupdateBy()).getUserName());
				map.put("lastupdateDate", df.format(itemTypeWg.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	


}