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
import com.system.role.entity.SysRole;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.DepartmentDao;
import com.web.basic.entity.Department;
import com.web.basic.service.DepartmentService;


@Service(value = "departmentService")
@Transactional(propagation = Propagation.REQUIRED)
public class Departmentlmpl implements DepartmentService {
	@Autowired
    private DepartmentDao departmentDao;
	
	 /**
     * 新增部门
     */
    @Override
    @Transactional
    public ApiResponseResult add(Department department) throws Exception{
        if(department == null){
            return ApiResponseResult.failure("部门不能为空！");
        }
        if(StringUtils.isEmpty(department.getDeCode())){
            return ApiResponseResult.failure("部门编号不能为空！");
        }
        if(StringUtils.isEmpty(department.getDeName())){
            return ApiResponseResult.failure("部门名称不能为空！");
        }
        int count = departmentDao.countByIsDelAndDeCode(0, department.getDeCode());
        if(count > 0){
            return ApiResponseResult.failure("该部门已存在，请填写其他部门编号！");
        }
        department.setCreatedTime(new Date());
        departmentDao.save(department);

        return ApiResponseResult.success("部门添加成功！").data(department);
    }
    /**
     * 修改部门
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Department department) throws Exception {
        if(department == null){
            return ApiResponseResult.failure("部门不能为空！");
        }
        if(department.getId() == null){
            return ApiResponseResult.failure("部门ID不能为空！");
        }
        if(StringUtils.isEmpty(department.getDeCode())){
            return ApiResponseResult.failure("部门编号不能为空！");
        }
        if(StringUtils.isEmpty(department.getDeName())){
            return ApiResponseResult.failure("部门名称不能为空！");
        }
        Department o = departmentDao.findById((long) department.getId());
        if(o == null){
            return ApiResponseResult.failure("该部门不存在！");
        }
        //判断部门编号是否有变化，有则修改；没有则不修改
        String originalCode = o.getDeCode();
        if(o.getDeCode().equals(department.getDeCode())){
        }else{
            int count = departmentDao.countByIsDelAndDeCode(0, department.getDeCode());
            if(count > 0){
                return ApiResponseResult.failure("部门编号已存在，请填写其他部门编号！");
            }
            o.setDeCode(department.getDeCode().trim());
        }
        o.setModifiedTime(new Date());
        o.setDeName(department.getDeName());
        o.setDeManager(department.getDeManager());
        o.setDeManagerTel(department.getDeManagerTel());
        departmentDao.save(o);
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
    public ApiResponseResult getDepartment(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("部门ID不能为空！");
        }
        Department o = departmentDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该部门不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除部门
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("部门ID不能为空！");
        }
        Department o  = departmentDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该部门不存在！");
        }
        o.setModifiedTime(new Date());
        o.setIsDel(1);
        departmentDao.save(o);

        return ApiResponseResult.success("删除成功！");
    }
    
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer deStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("部门ID不能为空！");
        }
        if(deStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Department o = departmentDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("部门不存在！");
        }
        o.setModifiedTime(new Date());
        o.setDeStatus(deStatus);
        departmentDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
    }
    
    /**
     * 查询列表
     */
	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("deCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("deName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("deManager", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Department> spec = Specification.where(BaseService.and(filters, Department.class));
				Specification<Department> spec1 = spec.and(BaseService.or(filters1, Department.class));
				Page<Department> page = departmentDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
