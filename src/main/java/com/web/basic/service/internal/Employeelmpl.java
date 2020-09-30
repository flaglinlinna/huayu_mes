package com.web.basic.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.EmployeeDao;
import com.web.basic.entity.Employee;
import com.web.basic.service.EmployeeService;


@Service(value = "EmployeeService")
@Transactional(propagation = Propagation.REQUIRED)
public class Employeelmpl implements EmployeeService {
	@Autowired
    private EmployeeDao employeeDao;

	 /**
     * 新增员工
     */
    @Override
    @Transactional
    public ApiResponseResult add(Employee employee) throws Exception{
        if(employee == null){
            return ApiResponseResult.failure("员工不能为空！");
        }
        if(StringUtils.isEmpty(employee.getEmpCode())){
            return ApiResponseResult.failure("员工编码不能为空！");
        }
        if(StringUtils.isEmpty(employee.getEmpName())){
            return ApiResponseResult.failure("员工名称不能为空！");
        }
        int count = employeeDao.countByDelFlagAndEmpCode(0, employee.getEmpCode());
        if(count > 0){
            return ApiResponseResult.failure("该员工已存在，请填写其他员工编码！");
        }
        employee.setCreateDate(new Date());
        employeeDao.save(employee);

        return ApiResponseResult.success("员工添加成功！").data(employee);
    }
    /**
     * 修改员工
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Employee employee) throws Exception {
        if(employee == null){
            return ApiResponseResult.failure("员工不能为空！");
        }
        if(employee.getId() == null){
            return ApiResponseResult.failure("员工ID不能为空！");
        }
        if(StringUtils.isEmpty(employee.getEmpCode())){
            return ApiResponseResult.failure("员工编码不能为空！");
        }
        if(StringUtils.isEmpty(employee.getEmpName())){
            return ApiResponseResult.failure("员工名称不能为空！");
        }
        Employee o = employeeDao.findById((long) employee.getId());
        if(o == null){
            return ApiResponseResult.failure("该员工不存在！");
        }
        //判断员工编码是否有变化，有则修改；没有则不修改
        if(o.getEmpCode().equals(employee.getEmpCode())){
        }else{
            int count = employeeDao.countByDelFlagAndEmpCode(0, employee.getEmpCode());
            if(count > 0){
                return ApiResponseResult.failure("员工编码已存在，请填写其他员工编码！");
            }
            o.setEmpCode(employee.getEmpCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setEmpName(employee.getEmpName());
        o.setEmpIdNo(employee.getEmpIdNo());
        o.setEmpType(employee.getEmpType());
        o.setJoinDate(employee.getJoinDate());
        o.setLeaveDate(employee.getLeaveDate());
        employeeDao.save(o);
        return ApiResponseResult.success("编辑成功！");
	}

    /**
     * 根据ID获取
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getEmployee(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("员工ID不能为空！");
        }
        Employee o = employeeDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该员工不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除员工
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("员工ID不能为空！");
        }
        Employee o  = employeeDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("员工不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setDelFlag(1);
        employeeDao.save(o);
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
					filters1.add(new SearchFilter("empCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("empName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("empType", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Employee> spec = Specification.where(BaseService.and(filters, Employee.class));
				Specification<Employee> spec1 = spec.and(BaseService.or(filters1, Employee.class));
				Page<Employee> page = employeeDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
