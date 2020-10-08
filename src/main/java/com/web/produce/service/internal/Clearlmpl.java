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
import com.web.basic.dao.LineDao;
import com.web.basic.entity.Client;
import com.web.basic.entity.ClientProcessMap;
import com.web.basic.entity.Defective;
import com.web.basic.entity.DefectiveDetail;
import com.web.basic.entity.Employee;
import com.web.basic.entity.Line;
import com.web.basic.entity.Mtrial;
import com.web.produce.dao.ClearDao;
import com.web.produce.dao.DevClockDao;
import com.web.produce.entity.Clear;
import com.web.produce.entity.DevClock;
import com.web.produce.service.ClearService;

/**
 * 下发原始数据采集
 *
 */
@Service(value = "ClearService")
@Transactional(propagation = Propagation.REQUIRED)
public class Clearlmpl implements ClearService {

	@Autowired
	ClearDao clearDao;

	@Autowired
	EmployeeDao employeeDao;

	@Autowired
	DevClockDao devClockDao;

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

		}
		Specification<Clear> spec = Specification.where(BaseService.and(filters, Clear.class));
		Specification<Clear> spec1 = spec.and(BaseService.or(filters1, Clear.class));
		Page<Clear> page = clearDao.findAll(spec1, pageRequest);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Clear bs : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("empCode", bs.getEmployee().getEmpCode());// 获取关联表的数据-工号
			map.put("empName", bs.getEmployee().getEmpName());// 获取关联表的数据-姓名
			map.put("devCode", bs.getDevClock().getDevCode());
			map.put("devName", bs.getDevClock().getDevName());
			map.put("devType", bs.getDevClock().getDevType());
			list.add(map);
		}

		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 新增下发记录（会覆盖原有记录）
	 */
	@Override
	@Transactional
	public ApiResponseResult add(String dev, String emp) throws Exception {

		if (dev == null || emp == null) {
			return ApiResponseResult.failure("下发记录必须含有卡机和员工信息！");
		}
		// 数据格式转换-卡机设备
		String[] devIdArray = dev.split(";");
		List<Long> devList = new ArrayList<Long>();
		for (int i = 0; i < devIdArray.length; i++) {
			if (StringUtils.isNotEmpty(devIdArray[i])) {
				devList.add(Long.parseLong(devIdArray[i]));
			}
		}
		// 数据格式转换-员工
		String[] empIdArray = emp.split(";");
		List<Long> empList = new ArrayList<Long>();
		for (int i = 0; i < empIdArray.length; i++) {
			if (StringUtils.isNotEmpty(empIdArray[i])) {
				empList.add(Long.parseLong(empIdArray[i]));
			}
		}
		List<Clear> listNew = new ArrayList<>();
		if (devList.size() > 0) {
			for (Long devId : devList) {
				// 1.删除原有的记录
				List<Clear> listOld = clearDao.findByDelFlagAndDevClockId(0, devId);
				if (listOld.size() > 0) {
					for (Clear item : listOld) {
						item.setDelTime(new Date());
						item.setDelFlag(1);
						item.setDelBy(UserUtil.getSessionUser().getId());
					}
					clearDao.saveAll(listOld);
				}
				// 2.添加新指纹下发记录
				if (empList.size() > 0) {
					for (Long empId : empList) {
						Clear item = new Clear();
						item.setCreateDate(new Date());
						item.setCreateBy(UserUtil.getSessionUser().getId());
						item.setDevClockId(devId);
						item.setEmpId(empId);
						listNew.add(item);
					}
					clearDao.saveAll(listNew);
				}
			}
		}
		return ApiResponseResult.success("下发记录添加成功！");
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
	public ApiResponseResult getClear(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("下发记录ID不能为空！");
		}
		Clear o = clearDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该下发记录不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("下发记录ID不能为空！");
		}
		Clear o = clearDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("下发记录不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		clearDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 获取员工数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getEmp(String empKeyword, PageRequest pageRequest) throws Exception {
		
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("empStatus", SearchFilter.Operator.EQ, BasicStateEnum.TRUE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(empKeyword)) {
			filters1.add(new SearchFilter("empCode", SearchFilter.Operator.LIKE, empKeyword));
			filters1.add(new SearchFilter("empName", SearchFilter.Operator.LIKE, empKeyword));
			filters1.add(new SearchFilter("empType", SearchFilter.Operator.LIKE, empKeyword));
		}
		Specification<Employee> spec = Specification.where(BaseService.and(filters, Employee.class));
		Specification<Employee> spec1 = spec.and(BaseService.or(filters1, Employee.class));
		Page<Employee> page = employeeDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 获取卡机数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getDev(String devKeyword, PageRequest pageRequest) throws Exception {
		
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("enabled", SearchFilter.Operator.EQ, BasicStateEnum.TRUE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(devKeyword)) {
			filters1.add(new SearchFilter("devCode", SearchFilter.Operator.LIKE, devKeyword));
			filters1.add(new SearchFilter("devName", SearchFilter.Operator.LIKE, devKeyword));
			filters1.add(new SearchFilter("devIp", SearchFilter.Operator.LIKE, devKeyword));
		}
		Specification<DevClock> spec = Specification.where(BaseService.and(filters, DevClock.class));
		Specification<DevClock> spec1 = spec.and(BaseService.or(filters1, DevClock.class));
		Page<DevClock> page = devClockDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}
