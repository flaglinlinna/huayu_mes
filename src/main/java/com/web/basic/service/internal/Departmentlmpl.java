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
        if(StringUtils.isEmpty(department.getBsCode())){
            return ApiResponseResult.failure("部门编号不能为空！");
        }
        if(StringUtils.isEmpty(department.getBsName())){
            return ApiResponseResult.failure("部门名称不能为空！");
        }
        int count = departmentDao.countByIsDelAndBsCode(0, department.getBsCode());
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
        if(StringUtils.isEmpty(department.getBsCode())){
            return ApiResponseResult.failure("部门编号不能为空！");
        }
        if(StringUtils.isEmpty(department.getBsName())){
            return ApiResponseResult.failure("部门名称不能为空！");
        }
        Department o = departmentDao.findById((long) department.getId());
        if(o == null){
            return ApiResponseResult.failure("该部门不存在！");
        }
        //判断部门编号是否有变化，有则修改；没有则不修改
        if(o.getBsCode().equals(department.getBsCode())){
        }else{
            int count = departmentDao.countByIsDelAndBsCode(0, department.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("部门编号已存在，请填写其他部门编号！");
            }
            o.setBsCode(department.getBsCode().trim());
        }
        o.setModifiedTime(new Date());
        o.setBsName(department.getBsName());
        o.setBsManager(department.getBsManager());
        o.setBsManagerTel(department.getBsManagerTel());
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
    public ApiResponseResult getDepart(Long id) throws Exception{
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
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("部门ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Department o = departmentDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("部门不存在！");
        }
        o.setModifiedTime(new Date());
        o.setBsStatus(bsStatus);
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
					filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsManager", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Department> spec = Specification.where(BaseService.and(filters, Department.class));
				Specification<Department> spec1 = spec.and(BaseService.or(filters1, Department.class));
				Page<Department> page = departmentDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
