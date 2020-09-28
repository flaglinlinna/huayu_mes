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
import com.web.basic.dao.ProcessDao;
import com.web.basic.entity.Process;
import com.web.basic.service.ProcessService;


@Service(value = "processService")
@Transactional(propagation = Propagation.REQUIRED)
public class Processlmpl implements ProcessService {
	@Autowired
    private ProcessDao processDao;

	 /**
     * 新增工序
     */
    @Override
    @Transactional
    public ApiResponseResult add(Process process) throws Exception{
        if(process == null){
            return ApiResponseResult.failure("工序不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcNo())){
            return ApiResponseResult.failure("工序编码不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcName())){
            return ApiResponseResult.failure("工序名称不能为空！");
        }
        int count = processDao.countByDelFlagAndProcNo(0, process.getProcNo());
        if(count > 0){
            return ApiResponseResult.failure("该工序已存在，请填写其他工序编码！");
        }
        process.setCreateDate(new Date());
        processDao.save(process);

        return ApiResponseResult.success("工序添加成功！").data(process);
    }
    /**
     * 修改工序
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Process process) throws Exception {
        if(process == null){
            return ApiResponseResult.failure("工序不能为空！");
        }
        if(process.getId() == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcNo())){
            return ApiResponseResult.failure("工序编码不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcName())){
            return ApiResponseResult.failure("工序名称不能为空！");
        }
        Process o = processDao.findById((long) process.getId());
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        //判断工序编码是否有变化，有则修改；没有则不修改
        if(o.getProcNo().equals(process.getProcNo())){
        }else{
            int count = processDao.countByDelFlagAndProcNo(0, process.getProcNo());
            if(count > 0){
                return ApiResponseResult.failure("工序编码已存在，请填写其他工序编码！");
            }
            o.setProcNo(process.getProcNo().trim());
        }
        o.setLastupdateDate(new Date());
        o.setProcName(process.getProcName());
        o.setProcOrder(process.getProcOrder());
        processDao.save(o);
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
    public ApiResponseResult getProcess(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        Process o = processDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除工序
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        Process o  = processDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setDelFlag(1);
        processDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Process o = processDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setCheckStatus(checkStatus);
        processDao.save(o);
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
					filters1.add(new SearchFilter("procNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("procOrder", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Process> spec = Specification.where(BaseService.and(filters, Process.class));
				Specification<Process> spec1 = spec.and(BaseService.or(filters1, Process.class));
				Page<Process> page = processDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
