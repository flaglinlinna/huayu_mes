package com.web.basic.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
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
import com.web.basic.entity.Mtrial;
import com.web.basic.service.EmployeeService;


@Service(value = "EmployeeService")
@Transactional(propagation = Propagation.REQUIRED)
public class Employeelmpl implements EmployeeService {
	@Autowired
    private EmployeeDao employeeDao;

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
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
        employee.setCreateBy(UserUtil.getSessionUser().getId());
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
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
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
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        employeeDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 删除员工
     */
    @Override
    @Transactional
    public ApiResponseResult deleteImg(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("员工ID不能为空！");
        }
        Employee o  = employeeDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("员工不存在！");
        }

        employeeDao.deleteImgUrl(id);
        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 查询列表
     */
	@Override
    @Transactional
	public ApiResponseResult getList(String keyword,String empStatus, PageRequest pageRequest) throws Exception {
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
				if (StringUtils.isNotEmpty(empStatus)) {
					filters.add(new SearchFilter("empStatus", SearchFilter.Operator.EQ, empStatus));
				}
				Specification<Employee> spec = Specification.where(BaseService.and(filters, Employee.class));
				Specification<Employee> spec1 = spec.and(BaseService.or(filters1, Employee.class));
				Page<Employee> page = employeeDao.findAll(spec1, pageRequest);



				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	@Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer empStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("员工ID不能为空！");
        }
        if(empStatus == null){
            return ApiResponseResult.failure("请正确设置在职或离职！");
        }
        Employee o = employeeDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("员工不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setEmpStatus(empStatus);
        employeeDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
    }
	
	/**
	 * 同步数据
	 * */
	@Override
	public ApiResponseResult getUpdateData() throws Exception{
		List<Object> list = getUpdateDataPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getId() + "");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success(list.get(1).toString());
	}
	
	public List getUpdateDataPrc(String company,String facoty,String user_id) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_download (?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setInt(3, 1);
				cs.setString(4, "员工信息");
				cs.setString(5, user_id);
				cs.registerOutParameter(6, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(7, java.sql.Types.VARCHAR);// 输出参数 返回标识
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				
				cs.execute();
				result.add(cs.getInt(6));
				result.add(cs.getString(7));
				return result;
			}

		});
		return resultList;
	}

    @Override
    public ApiResponseResult updateImgUrl(Integer id, String url) throws Exception {
        Integer flag =  employeeDao.updateUrl(id,url);
        if(flag==1){
            return ApiResponseResult.success();
        }
        return ApiResponseResult.failure("更新失败");
    }
}
