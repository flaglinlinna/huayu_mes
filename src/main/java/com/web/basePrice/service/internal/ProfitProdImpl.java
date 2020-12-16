package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProfitProdDao;
import com.web.basePrice.entity.ProfitProd;
import com.web.basePrice.service.ProfitProdService;
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


@Service(value = "ProfitProdService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProfitProdImpl extends BasePriceUtils implements ProfitProdService {
	@Autowired
	private ProfitProdDao profitProdDao;

	@Autowired
	private SysUserDao sysUserDao;
	


	/**
	 * 新增产品利润率信息
	 */
	@Override
	@Transactional
	public ApiResponseResult add(ProfitProd profitProd) throws Exception {
		if (profitProd == null) {
			return ApiResponseResult.failure("产品利润率信息不能为空！");
		}
		if (StringUtils.isEmpty(profitProd.getItemType())) {
			return ApiResponseResult.failure("机种类型不能为空！");
		}
		if (StringUtils.isEmpty(profitProd.getProductType())) {
			return ApiResponseResult.failure("产品类型为空！");
		}
		profitProd.setCreateDate(new Date());
		profitProd.setCreateBy(UserUtil.getSessionUser().getId());
		profitProdDao.save(profitProd);
		return ApiResponseResult.success("产品利润率信息添加成功！").data(profitProd);
	}

	/**
	 * 修改产品利润率信息
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(ProfitProd profitProd) throws Exception {
		if (profitProd == null) {
			return ApiResponseResult.failure("产品利润率信息不能为空！");
		}
		if (profitProd.getId() == null) {
			return ApiResponseResult.failure("产品利润率信息ID不能为空！");
		}
		if (StringUtils.isEmpty(profitProd.getItemType())) {
			return ApiResponseResult.failure("机种类型不能为空！");
		}
		if (StringUtils.isEmpty(profitProd.getProductType())) {
			return ApiResponseResult.failure("产品类型为空！");
		}

		ProfitProd o = profitProdDao.findById((long) profitProd.getId());
		if (o == null) {
			return ApiResponseResult.failure("该产品利润率信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setItemType(profitProd.getItemType());
	    o.setProductType(profitProd.getProductType());
	    o.setFmemo(profitProd.getFmemo());
	    o.setProfitRateGs(profitProd.getProfitRateGs());
		profitProdDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除产品利润率信息
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("产品利润率信息ID不能为空！");
		}
		ProfitProd o = profitProdDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("产品利润率信息不存在！");
		}
		o.setDelFlag(1);
		profitProdDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("产品利润率信息ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		ProfitProd o = profitProdDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("此产品利润率信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		profitProdDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}


	@Override
	public ApiResponseResult getProdTypeList(String condition,PageRequest pageRequest)throws Exception{
		List<Object> list = getBJProdType(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getId() + "","01",condition,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success().data(map);
	}

	/**
	 * 查询产品利润率信息维护列表
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
			filters1.add(new SearchFilter("itemType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("fmemo", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<ProfitProd> spec = Specification.where(BaseService.and(filters, ProfitProd.class));
		Specification<ProfitProd> spec1 = spec.and(BaseService.or(filters1, ProfitProd.class));
		Page<ProfitProd> page = profitProdDao.findAll(spec1, pageRequest);
		List<ProfitProd> priceCommList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (ProfitProd profitProd : priceCommList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", profitProd.getId());
			map.put("productType", profitProd.getProductType());
			map.put("profitRateGs", profitProd.getProfitRateGs());
			map.put("itemType", profitProd.getItemType());
			map.put("fmemo",profitProd.getFmemo());
			map.put("enabled", profitProd.getEnabled());
			map.put("createBy", sysUserDao.findById((long) profitProd.getCreateBy()).getUserName());
			map.put("createDate", df.format(profitProd.getCreateDate()));
			if (profitProd.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) profitProd.getLastupdateBy()).getUserName());
				map.put("lastupdateDate", df.format(profitProd.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	


}