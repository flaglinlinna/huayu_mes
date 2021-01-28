package com.system.check.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;

import com.system.check.dao.WorkflowStepDao;
import com.system.check.entity.WorkflowStep;
import com.system.check.service.WorkflowStepService;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service(value = "WorkflowStepService")
@Transactional(propagation = Propagation.REQUIRED)
public class WorkflowStepImpl implements WorkflowStepService {
    @Autowired
    private WorkflowStepDao workflowStepDao;

    /**
     * 新增流程步骤
     */
    @Override
    @Transactional
    public ApiResponseResult add(WorkflowStep workflowStep) throws Exception{
        if(workflowStep == null){
            return ApiResponseResult.failure("流程步骤不能为空！");
        }
        if(StringUtils.isEmpty(workflowStep.getBsStepName())){
            return ApiResponseResult.failure("步骤名称不能为空！");
        }
        int count = workflowStepDao.countByBsFlowIdAndDelFlagAndBsCheckGrade(workflowStep.getBsFlowId(),0, workflowStep.getBsCheckGrade());
        if(count > 0){
            return ApiResponseResult.failure("该流程序号已存在，请填写其他流程序号！");
        }
        workflowStep.setCreateDate(new Date());
        workflowStep.setCreateBy(UserUtil.getSessionUser().getId());
        workflowStepDao.save(workflowStep);
        return ApiResponseResult.success("流程添加成功！").data(workflowStep);
    }
    /**
     * 修改流程步骤
     */
    @Override
    @Transactional
    public ApiResponseResult edit(WorkflowStep workflowStep) throws Exception {
        if(workflowStep == null){
            return ApiResponseResult.failure("步骤不能为空！");
        }
        if(workflowStep.getId() == null){
            return ApiResponseResult.failure("步骤ID不能为空！");
        }
        if(StringUtils.isEmpty(workflowStep.getBsStepName())){
            return ApiResponseResult.failure("步骤名称不能为空！");
        }

        WorkflowStep o = workflowStepDao.findById((long) workflowStep.getId());
        if(o == null){
            return ApiResponseResult.failure("该步骤不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsCheckName(workflowStep.getBsCheckName());
        o.setBsStepName(workflowStep.getBsStepName());
        o.setBsCheckBy(workflowStep.getBsCheckBy());
        o.setBsCheckGrade(workflowStep.getBsCheckGrade());
        workflowStepDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }


    /**
     * 删除流程步骤
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("流程步骤ID不能为空！");
        }
        WorkflowStep o  = workflowStepDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("流程步骤不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        workflowStepDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }
    /**
     * 查询列表
     */
    @Override
    @Transactional
    public ApiResponseResult getList(Long bsFlowId, PageRequest pageRequest) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        filters1.add(new SearchFilter("bsFlowId", SearchFilter.Operator.EQ, bsFlowId));

        Specification<WorkflowStep> spec = Specification.where(BaseService.and(filters, WorkflowStep.class));
        Specification<WorkflowStep> spec1 = spec.and(BaseService.or(filters1, WorkflowStep.class));
        Page<WorkflowStep> page = workflowStepDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}
