package com.web.produce.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.web.basic.dao.EmployeeDao;
import com.web.basic.entity.Employee;
import com.web.produce.dao.EmpFingerDao;
import com.web.produce.entity.EmpFinger;
import com.web.produce.service.EmpFingerService;

/**
 * 指纹登记
 *
 */
@Service(value = "EmpFingerService")
@Transactional(propagation = Propagation.REQUIRED)
public class EmpFingerlmpl implements EmpFingerService {

	@Autowired
	EmpFingerDao empFingerDao;

	@Autowired
	EmployeeDao employeeDao;

	/**
	 * 新增指纹记录
	 */
	@Override
	@Transactional
	public ApiResponseResult add(EmpFinger empFinger) throws Exception {
		if (empFinger == null) {
			return ApiResponseResult.failure("指纹记录不能为空！");
		}
		int count = empFingerDao.countByDelFlagAndTemplateStr(0, empFinger.getTemplateStr());
		if (count > 0) {
			return ApiResponseResult.failure("该指纹模板已存在，请加入其他指纹模板！");
		}
		int empCount=empFingerDao.countByDelFlagAndEmpId(0, empFinger.getEmpId());
		if (empCount >=2) {
			return ApiResponseResult.failure("每位员工只需登记两枚指纹！");
		}
		int empFIndxCount = empFingerDao.countByDelFlagAndEmpIdAndFingerIdx(0,empFinger.getEmpId(),empFinger.getFingerIdx());
		if (empFIndxCount > 0) {
			return ApiResponseResult.failure("该员工的此项手指序号已存在，请选择其他手指序号！");
		}
		empFinger.setCreateDate(new Date());
		empFinger.setCreateBy(UserUtil.getSessionUser().getId());
		empFingerDao.save(empFinger);
		return ApiResponseResult.success("指纹记录添加成功！").data(empFinger);
	}

	/**
	 * 修改指纹记录
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(EmpFinger empFinger) throws Exception {
		if (empFinger == null) {
			return ApiResponseResult.failure("指纹记录不能为空！");
		}
		if (empFinger.getId() == null) {
			return ApiResponseResult.failure("指纹记录ID不能为空！");
		}
		if (empFinger.getEmpId() == null) {
			return ApiResponseResult.failure("员工信息不能为空！");
		}
		if (StringUtils.isEmpty(empFinger.getTemplateStr())) {
			return ApiResponseResult.failure("指纹模板不能为空！");
		}
		if (StringUtils.isEmpty(empFinger.getFingerIdx())) {
			return ApiResponseResult.failure("手指序号不能为空！");
		}
		EmpFinger o = empFingerDao.findById((long) empFinger.getId());
		if (o == null) {
			return ApiResponseResult.failure("该指纹记录不存在！");
		}
		// 判断指纹模板否有变化，有则修改；没有则不修改
		if (o.getTemplateStr().equals(empFinger.getTemplateStr())) {
		} else {
			int count = empFingerDao.countByDelFlagAndTemplateStr(0, empFinger.getTemplateStr());
			if (count > 0) {
				return ApiResponseResult.failure("指纹模板已存在，请加入其他指纹模板！");
			}
			o.setTemplateStr(empFinger.getTemplateStr());
		}
		int empFIndxCount = empFingerDao.countByDelFlagAndEmpIdAndFingerIdx(0,empFinger.getEmpId(),empFinger.getFingerIdx());
		if (empFIndxCount > 0) {
			return ApiResponseResult.failure("该员工的此项手指序号已存在，请选择其他手指序号！");
		}else{
			o.setFingerIdx(empFinger.getFingerIdx());
			o.setEmpId(empFinger.getEmpId());
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		empFingerDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 根据ID获取 
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getEmpFinger(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("指纹记录ID不能为空！");
		}
		EmpFinger o = empFingerDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该指纹记录不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除指纹记录 
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("指纹记录ID不能为空！");
		}
		EmpFinger o = empFingerDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("指纹记录不存在！");
		}
		o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());	
		empFingerDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 查询列表
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
			filters1.add(new SearchFilter("emp.empName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<EmpFinger> spec = Specification.where(BaseService.and(filters, EmpFinger.class));
		Specification<EmpFinger> spec1 = spec.and(BaseService.or(filters1, EmpFinger.class));
		Page<EmpFinger> page = empFingerDao.findAll(spec1, pageRequest);

		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(EmpFinger bs:page.getContent()){ 
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("empCode",bs.getEmp().getEmpCode());//获取关联表的数据-工号
			map.put("empName",bs.getEmp().getEmpName());//获取关联表的数据-姓名
			map.put("templateStr", bs.getTemplateStr());
			map.put("fingerIdx", bs.getFingerIdx());
			map.put("lastupdateDate",bs.getLastupdateDate());
			map.put("createDate", bs.getCreateDate());
			list.add(map);
		}	
		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 获取人员数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getEmpList() throws Exception {
		List<Employee> list = employeeDao.findByDelFlagAndEmpStatus(0,1);//查找在职员工数据
		return ApiResponseResult.success().data(list);
	}

}
