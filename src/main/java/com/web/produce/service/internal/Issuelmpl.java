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
import com.web.basic.entity.Defective;
import com.web.basic.entity.DefectiveDetail;
import com.web.basic.entity.Employee;
import com.web.basic.entity.Line;
import com.web.basic.entity.Mtrial;
import com.web.produce.dao.IssueDao;
import com.web.produce.dao.DevClockDao;
import com.web.produce.entity.Issue;
import com.web.produce.entity.DevClock;
import com.web.produce.service.IssueService;

/**
 * 下发原始数据采集
 *
 */
@Service(value = "IssueService")
@Transactional(propagation = Propagation.REQUIRED)
public class Issuelmpl implements IssueService {

	@Autowired
	IssueDao issueDao;
	
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
		Specification<Issue> spec = Specification.where(BaseService.and(filters, Issue.class));
		Specification<Issue> spec1 = spec.and(BaseService.or(filters1, Issue.class));
		Page<Issue> page = issueDao.findAll(spec1, pageRequest);

		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(Issue bs:page.getContent()){ 
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("empCode",bs.getEmployee().getEmpCode());//获取关联表的数据-工号
			map.put("empName",bs.getEmployee().getEmpName());//获取关联表的数据-姓名
			map.put("devCode", bs.getDevClock().getDevCode());
			map.put("devName", bs.getDevClock().getDevName());
			map.put("devType", bs.getDevClock().getDevType());
			list.add(map);
		}
		
		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	/**
	 * 新增下发记录
	 */
	@Override
	@Transactional
	public ApiResponseResult add(Issue issue) throws Exception {
		if (issue == null) {
			return ApiResponseResult.failure("下发记录不能为空！");
		}
		issue.setCreateDate(new Date());
		issue.setCreateBy(UserUtil.getSessionUser().getId());
		issueDao.save(issue);

		return ApiResponseResult.success("下发记录添加成功！").data(issue);
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
	public ApiResponseResult getIssue(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("下发记录ID不能为空！");
		}
		Issue o = issueDao.findById((long) id);
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
		Issue o = issueDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("下发记录不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		issueDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}
	/**
	 * 获取员工数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getEmp() throws Exception {
		List<Employee> list = employeeDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}
	/**
	 * 获取卡机数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getDev() throws Exception {
		List<DevClock> list = devClockDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}
}
