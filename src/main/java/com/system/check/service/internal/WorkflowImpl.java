package com.system.check.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;

import com.system.check.dao.WorkflowDao;
import com.system.check.entity.Workflow;
import com.system.check.service.WorkflowService;
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


@Service(value = "WorkflowService")
@Transactional(propagation = Propagation.REQUIRED)
public class WorkflowImpl implements WorkflowService {
    @Autowired
    private WorkflowDao workflowDao;

    /**
     * 新增不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult add(Workflow workflow) throws Exception{
        if(workflow == null){
            return ApiResponseResult.failure("流程不能为空！");
        }
        if(StringUtils.isEmpty(workflow.getBsFlowCode())){
            return ApiResponseResult.failure("流程编号不能为空！");
        }
        if(StringUtils.isEmpty(workflow.getBsFlowName())){
            return ApiResponseResult.failure("流程名称不能为空！");
        }
        int count = workflowDao.countByDelFlagAndBsFlowCode(0, workflow.getBsFlowCode());
        if(count > 0){
            return ApiResponseResult.failure("该流程编码已存在，请填写其他流程编码！");
        }
        workflow.setCreateDate(new Date());
        workflow.setCreateBy(UserUtil.getSessionUser().getId());
        workflowDao.save(workflow);
        return ApiResponseResult.success("流程添加成功！").data(workflow);
    }
    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Workflow workflow) throws Exception {
        if(workflow == null){
            return ApiResponseResult.failure("流程不能为空！");
        }
        if(workflow.getId() == null){
            return ApiResponseResult.failure("流程ID不能为空！");
        }
        if(StringUtils.isEmpty(workflow.getBsFlowCode())){
            return ApiResponseResult.failure("流程编码不能为空！");
        }
        if(StringUtils.isEmpty(workflow.getBsFlowName())){
            return ApiResponseResult.failure("流程名称不能为空！");
        }
        Workflow o = workflowDao.findById((long) workflow.getId());
        if(o == null){
            return ApiResponseResult.failure("该流程不存在！");
        }
        //判断异常类别代码是否有变化，有则修改；没有则不修改
        if(o.getBsFlowCode().equals(workflow.getBsFlowCode())){
        }else{
            int count = workflowDao.countByDelFlagAndBsFlowCode(0, workflow.getBsFlowCode());
            if(count > 0){
                return ApiResponseResult.failure("流程编码已存在，请填写其他流程编码！");
            }
            o.setBsFlowCode(workflow.getBsFlowCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsFlowName(workflow.getBsFlowName());
        o.setBsFlowDescribe(workflow.getBsFlowDescribe());
        workflowDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }


    /**
     * 删除流程
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("流程ID不能为空！");
        }
        Workflow o  = workflowDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("流程信息不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        workflowDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 删除流程
     */
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id,Integer bsStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("流程ID不能为空！");
        }
        Workflow o  = workflowDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("流程信息不存在！");
        }
        o.setBsFlowStatus(bsStatus.toString());
        workflowDao.save(o);
        return ApiResponseResult.success("状态修改成功！");
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
            filters1.add(new SearchFilter("bsFlowCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsFlowName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<Workflow> spec = Specification.where(BaseService.and(filters, Workflow.class));
        Specification<Workflow> spec1 = spec.and(BaseService.or(filters1, Workflow.class));
        Page<Workflow> page = workflowDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}
