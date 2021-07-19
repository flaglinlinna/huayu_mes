package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProcRoleDao;
import com.web.basePrice.entity.ProcRole;
import com.web.basePrice.service.ProcRoleService;
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


@Service(value = "ProcRoleService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProcRoleImpl extends  BasePriceUtils implements ProcRoleService {
	@Autowired
	private ProcRoleDao procRoleDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增外购物料类型
	 */
	@Override
	@Transactional
	public ApiResponseResult add(ProcRole procRole) throws Exception {
		if (procRole == null) {
			return ApiResponseResult.failure("关联信息不能为空！");
		}
		procRole.setCreateDate(new Date());
		procRole.setCreateBy(UserUtil.getSessionUser().getId());
		procRoleDao.save(procRole);
		return ApiResponseResult.success("类型关联角色添加成功！").data(procRole);
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
			procRoleDao.deleteByPkProc(pkItemTypeWg);
			List<ProcRole> itemTypeWgRoleList = new ArrayList<>();
			String[] role_id = roleIds.split(",");
			for(String roleId :role_id){
				ProcRole itemTypeWgRole = new ProcRole();
				itemTypeWgRole.setPkProc(pkItemTypeWg);
				itemTypeWgRole.setPkSysRole(Long.parseLong(roleId));
				itemTypeWgRole.setCreateBy(UserUtil.getSessionUser().getId());
				itemTypeWgRole.setCreateDate(addTime);
				itemTypeWgRoleList.add(itemTypeWgRole);
			}
			procRoleDao.saveAll(itemTypeWgRoleList);
		}
		return ApiResponseResult.success("添加成功！");
	}

	/**
	 * 修改外购物料类型
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(ProcRole procRole) throws Exception {
		if (procRole == null) {
			return ApiResponseResult.failure("外购物料类型不能为空！");
		}
		if (procRole.getId() == null) {
			return ApiResponseResult.failure("外购物料类型信息ID不能为空！");
		}
		ProcRole o = procRoleDao.findById((long) procRole.getId());
		if (o == null) {
			return ApiResponseResult.failure("该外购物料类型信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
//		o.setItemType(itemTypeWg.getItemType());
//	    o.setFmemo(itemTypeWg.getFmemo());
		procRoleDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}


	@Override
	public ApiResponseResult getByWgId(Long wgId) throws Exception {
		return ApiResponseResult.success().data(procRoleDao.findByDelFlagAndPkProc(0,wgId));
	}

	/**
	 * 删除客户品质标准信息
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("关联信息ID不能为空！");
		}
		ProcRole o = procRoleDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("关联信息不存在！");
		}
		o.setDelFlag(1);
		procRoleDao.save(o);
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
			filters1.add(new SearchFilter("proc.procName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("sysRole.roleName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<ProcRole> spec = Specification.where(BaseService.and(filters, ProcRole.class));
		Specification<ProcRole> spec1 = spec.and(BaseService.or(filters1, ProcRole.class));
		Page<ProcRole> page = procRoleDao.findAll(spec1, pageRequest);
		List<ProcRole> customQsList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (ProcRole itemTypeWg : customQsList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", itemTypeWg.getId());
			map.put("procName", itemTypeWg.getProc().getProcName());
			map.put("pkProc",itemTypeWg.getProc().getId());
			map.put("roleName",itemTypeWg.getSysRole().getRoleName());
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