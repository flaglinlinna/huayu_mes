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
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.entity.Proc;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.ProcService;
import com.web.basic.entity.Mtrial;

/**
 *
 * @date Dec 4, 2020 3:23:25 PM
 */
@Service(value = "ProcService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProcImpl implements ProcService {
	@Autowired
	private ProcDao procDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增工序基础信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(Proc proc) throws Exception {
		if (proc == null) {
			return ApiResponseResult.failure("工序信息不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcNo())) {
			return ApiResponseResult.failure("工序信息编码不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcName())) {
			return ApiResponseResult.failure("工序信息名称不能为空！");
		}
		int count = procDao.countByDelFlagAndProcNo(0, proc.getProcNo());
		if (count > 0) {
			return ApiResponseResult.failure("该工序信息编码已存在，请填写其他工序信息编码！");
		}
		proc.setCreateDate(new Date());
		proc.setCreateBy(UserUtil.getSessionUser().getId());
		procDao.save(proc);
		return ApiResponseResult.success("工序信息添加成功！").data(proc);
	}

	/**
	 * 修改工序信息
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(Proc proc) throws Exception {
		if (proc == null) {
			return ApiResponseResult.failure("工序信息不能为空！");
		}
		if (proc.getId() == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcNo())) {
			return ApiResponseResult.failure("工序信息编码不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcName())) {
			return ApiResponseResult.failure("工序信息名称不能为空！");
		}
		Proc o = procDao.findById((long) proc.getId());
		if (o == null) {
			return ApiResponseResult.failure("该工序信息不存在！");
		}
		// 判断工序信息编码是否有变化，有则修改；没有则不修改
		if (o.getProcNo().equals(proc.getProcNo())) {
		} else {
			int count = procDao.countByDelFlagAndProcNo(0, proc.getProcNo());
			if (count > 0) {
				return ApiResponseResult.failure("工序信息编码已存在，请填写其他工序信息编码！");
			}
			o.setProcNo(proc.getProcNo().trim());
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setProcName(proc.getProcName());
		o.setCheckStatus(proc.getCheckStatus());
		procDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 根据ID获取工序信息详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getProc(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		Proc o = procDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该工序信息不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除工序信息
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		Proc o = procDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("工序信息不存在！");
		}
		o.setDelFlag(1);
		procDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		if (checkStatus == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		Proc o = procDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("工序信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setCheckStatus(checkStatus);
		procDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询工序基础信息维护列表
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
			filters1.add(new SearchFilter("procNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Proc> spec = Specification.where(BaseService.and(filters, Proc.class));
		Specification<Proc> spec1 = spec.and(BaseService.or(filters1, Proc.class));
		Page<Proc> page = procDao.findAll(spec1, pageRequest);
		List<Proc> procList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (Proc proc : procList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", proc.getId());
			map.put("procNo", proc.getProcNo());
			map.put("procName", proc.getProcName());
			if(proc.getBjWorkCenter()!=null){
				map.put("workCenter", proc.getBjWorkCenter().getWorkcenterName());
			}
			map.put("checkStatus", proc.getCheckStatus());
			map.put("createBy", sysUserDao.findById((long) proc.getCreateBy()).getUserName());
			map.put("createDate", df.format(proc.getCreateDate()));
			if (proc.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) proc.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(proc.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}