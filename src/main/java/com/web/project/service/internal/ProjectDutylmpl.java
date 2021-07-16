package com.web.project.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.project.dao.ProjectDutyDao;
import com.web.project.entity.ProjectDuty;
import com.web.project.service.ProjectDutyService;
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


@Service(value = "ProjectDutyService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProjectDutylmpl implements ProjectDutyService {
	@Autowired
    private ProjectDutyDao projectDutyDao;

	 /**
     * 新增客户
     */
    @Override
    @Transactional
    public ApiResponseResult add(ProjectDuty projectDuty) throws Exception{
        if(projectDuty == null){
            return ApiResponseResult.failure("信息不能为空！");
        }
        if(StringUtils.isEmpty(projectDuty.getProjectCode())){
            return ApiResponseResult.failure("客户编码不能为空！");
        }
        if(StringUtils.isEmpty(projectDuty.getProjectName())){
            return ApiResponseResult.failure("客户名称不能为空！");
        }
//        int count = projectDutyDao.countByDelFlagAndCustNo(0, client.getCustNo());
//        if(count > 0){
//            return ApiResponseResult.failure("该客户已存在，请填写其他客户编码！");
//        }
        projectDuty.setCreateDate(new Date());
        projectDuty.setCreateBy(UserUtil.getSessionUser().getId());
        projectDutyDao.save(projectDuty);

        return ApiResponseResult.success("添加成功！").data(projectDuty);
    }
    /**
     * 修改客户
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ProjectDuty projectDuty) throws Exception {
        if(projectDuty == null){
            return ApiResponseResult.failure("信息不能为空！");
        }
        if(projectDuty.getId() == null){
            return ApiResponseResult.failure("信息ID不能为空！");
        }
        if(StringUtils.isEmpty(projectDuty.getProjectCode())){
            return ApiResponseResult.failure("项目编码不能为空！");
        }
        if(StringUtils.isEmpty(projectDuty.getProjectName())){
            return ApiResponseResult.failure("项目名称不能为空！");
        }
        ProjectDuty o = projectDutyDao.findById((long) projectDuty.getId());
        if(o == null){
            return ApiResponseResult.failure("该信息ID不存在！");
        }

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setProjectCode(projectDuty.getProjectCode());
        o.setProjectName(projectDuty.getProjectName());
        o.setManageName(projectDuty.getManageName());
        o.setStartTime1(projectDuty.getStartTime1());
        o.setEndTime1(projectDuty.getEndTime1());
        o.setStartTime2(projectDuty.getStartTime2());
        o.setEndTime2(projectDuty.getEndTime2());
        o.setStartTime3(projectDuty.getStartTime3());
        o.setEndTime3(projectDuty.getEndTime3());
        o.setStartTime4(projectDuty.getStartTime4());
        o.setEndTime4(projectDuty.getEndTime4());
        o.setLatestShow(projectDuty.getLatestShow());
        projectDutyDao.save(o);
        return ApiResponseResult.success("编辑成功！");
	}

//    /**
//     * 根据ID获取
//     * @param id
//     * @return
//     * @throws Exception
//     */
//    @Override
//    @Transactional
//    public ApiResponseResult getClient(Long id) throws Exception{
//        if(id == null){
//            return ApiResponseResult.failure("客户ID不能为空！");
//        }
//        Client o = clientDao.findById((long) id);
//        if(o == null){
//            return ApiResponseResult.failure("该客户不存在！");
//        }
//        return ApiResponseResult.success().data(o);
//    }
    /**
     * 删除客户
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("信息ID不能为空！");
        }
        ProjectDuty o  = projectDutyDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("信息不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        projectDutyDao.save(o);
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
					filters1.add(new SearchFilter("projectCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("projectName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("manageName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<ProjectDuty> spec = Specification.where(BaseService.and(filters, ProjectDuty.class));
				Specification<ProjectDuty> spec1 = spec.and(BaseService.or(filters1, ProjectDuty.class));
				Page<ProjectDuty> page = projectDutyDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
