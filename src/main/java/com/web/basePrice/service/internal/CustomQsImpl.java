package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.CustomQsDao;
import com.web.basePrice.entity.CustomQs;
import com.web.basePrice.service.CustomQsService;
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


@Service(value = "CustomQsService")
@Transactional(propagation = Propagation.REQUIRED)
public class CustomQsImpl extends  BasePriceUtils implements CustomQsService {
	@Autowired
	private CustomQsDao customQsDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增客户品质标准信息
	 */
	@Override
	@Transactional
	public ApiResponseResult add(CustomQs customQs) throws Exception {
		if (customQs == null) {
			return ApiResponseResult.failure("客户品质标准信息不能为空！");
		}
		customQs.setCreateDate(new Date());
		customQs.setCreateBy(UserUtil.getSessionUser().getId());
		customQsDao.save(customQs);
		return ApiResponseResult.success("客户品质标准信息添加成功！").data(customQs);
	}

	/**
	 * 修改客户品质标准信息
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(CustomQs customQs) throws Exception {
		if (customQs == null) {
			return ApiResponseResult.failure("客户品质标准信息不能为空！");
		}
		if (customQs.getId() == null) {
			return ApiResponseResult.failure("客户品质标准信息ID不能为空！");
		}
		CustomQs o = customQsDao.findById((long) customQs.getId());
		if (o == null) {
			return ApiResponseResult.failure("该客户品质标准信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setQsNo(customQs.getQsNo());
	    o.setQsName(customQs.getQsName());
	    o.setFmemo(customQs.getFmemo());
	    o.setQsType(customQs.getQsType());
        o.setFftp(customQs.getFftp());
        o.setFileId(customQs.getFileId());
		customQsDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	//获取品质标准类型 搜索框
	@Override
	@Transactional
	public ApiResponseResult getQsType(String keyword,PageRequest pageRequest) throws Exception {
		List<Object> list =getSystemSubParamPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				"BJ_BASE_QS_TYPE",pageRequest);
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success().data(map);
	}

	/**
	 * 删除客户品质标准信息
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("客户品质标准信息ID不能为空！");
		}
		CustomQs o = customQsDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("客户品质标准信息不存在！");
		}
		o.setDelFlag(1);
		customQsDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 删除客户品质标准附件
	 */
	@Override
	@Transactional
	public ApiResponseResult delFile(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("客户品质标准信息ID不能为空！");
		}
		CustomQs o = customQsDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("客户品质标准信息不存在！");
		}
		o.setFileId(null);
		o.setFftp(null);
		customQsDao.save(o);
		return ApiResponseResult.success("删除附件成功！");
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
			filters1.add(new SearchFilter("custName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("qsNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("qsName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("fmemo", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("qsType", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<CustomQs> spec = Specification.where(BaseService.and(filters, CustomQs.class));
		Specification<CustomQs> spec1 = spec.and(BaseService.or(filters1, CustomQs.class));
		Page<CustomQs> page = customQsDao.findAll(spec1, pageRequest);
		List<CustomQs> customQsList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (CustomQs customQs : customQsList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", customQs.getId());
			map.put("custName", customQs.getCustName());
			map.put("qsNo", customQs.getQsNo());
			map.put("qsName", customQs.getQsName());
			map.put("fmemo",customQs.getFmemo());
			map.put("fftp",customQs.getFftp());
			map.put("fileId",customQs.getFileId());
			map.put("qsType", customQs.getQsType());
			map.put("createBy", sysUserDao.findById((long) customQs.getCreateBy()).getUserName());
			map.put("createDate", df.format(customQs.getCreateDate()));
			if (customQs.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) customQs.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(customQs.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	


}