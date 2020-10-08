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
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.WorkCenterDao;
import com.web.basic.entity.WorkCenter;
import com.web.basic.service.WorkCenterService;


@Service(value = "workCenterService")
@Transactional(propagation = Propagation.REQUIRED)
public class WorkCenterlmpl implements WorkCenterService {
	@Autowired
    private WorkCenterDao workCenterDao;
	
	 /**
     * 新增工作中心
     */
    @Override
    @Transactional
    public ApiResponseResult add(WorkCenter workCenter) throws Exception{
        if(workCenter == null){
            return ApiResponseResult.failure("工作中心不能为空！");
        }
        if(StringUtils.isEmpty(workCenter.getBsCode())){
            return ApiResponseResult.failure("工作中心编号不能为空！");
        }
        if(StringUtils.isEmpty(workCenter.getBsName())){
            return ApiResponseResult.failure("工作中心名称不能为空！");
        }
        int count = workCenterDao.countByDelFlagAndBsCode(0, workCenter.getBsCode());
        if(count > 0){
            return ApiResponseResult.failure("该工作中心已存在，请填写其他工作中心编号！");
        }
        workCenter.setCreateDate(new Date());
        workCenter.setCreateBy(UserUtil.getSessionUser().getId());
        workCenterDao.save(workCenter);

        return ApiResponseResult.success("工作中心添加成功！").data(workCenter);
    }
    /**
     * 修改工作中心
     */
    @Override
    @Transactional
    public ApiResponseResult edit(WorkCenter workCenter) throws Exception {
        if(workCenter == null){
            return ApiResponseResult.failure("工作中心不能为空！");
        }
        if(workCenter.getId() == null){
            return ApiResponseResult.failure("工作中心ID不能为空！");
        }
        if(StringUtils.isEmpty(workCenter.getBsCode())){
            return ApiResponseResult.failure("工作中心编号不能为空！");
        }
        if(StringUtils.isEmpty(workCenter.getBsName())){
            return ApiResponseResult.failure("工作中心名称不能为空！");
        }
        WorkCenter o = workCenterDao.findById((long) workCenter.getId());
        if(o == null){
            return ApiResponseResult.failure("该工作中心不存在！");
        }
        //判断工作中心编号是否有变化，有则修改；没有则不修改
        if(o.getBsCode().equals(workCenter.getBsCode())){
        }else{
            int count = workCenterDao.countByDelFlagAndBsCode(0, workCenter.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("工作中心编号已存在，请填写其他工作中心编号！");
            }
            o.setBsCode(workCenter.getBsCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsName(workCenter.getBsName());
        workCenterDao.save(o);
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
    public ApiResponseResult getWorkCenter(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工作中心ID不能为空！");
        }
        WorkCenter o = workCenterDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工作中心不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除工作中心
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工作中心ID不能为空！");
        }
        WorkCenter o  = workCenterDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工作中心不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        workCenterDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }
    
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工作中心ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        WorkCenter o = workCenterDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工作中心不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsStatus(bsStatus);
        workCenterDao.save(o);
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
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<WorkCenter> spec = Specification.where(BaseService.and(filters, WorkCenter.class));
				Specification<WorkCenter> spec1 = spec.and(BaseService.or(filters1, WorkCenter.class));
				Page<WorkCenter> page = workCenterDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
