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
import com.web.basic.dao.HoursDao;
import com.web.basic.entity.Hours;
import com.web.basic.service.HoursService;


@Service(value = "hoursService")
@Transactional(propagation = Propagation.REQUIRED)
public class Hourslmpl implements HoursService {
	@Autowired
    private HoursDao hoursDao;
	
	 /**
     * 新增产品标准工时
     */
    @Override
    @Transactional
    public ApiResponseResult add(Hours hours) throws Exception{
        if(hours == null){
            return ApiResponseResult.failure("产品标准工时不能为空！");
        }
        if(StringUtils.isEmpty(hours.getBsCode())){
            return ApiResponseResult.failure("产品编码不能为空！");
        }
        if(StringUtils.isEmpty(hours.getBsStdHrs())){
            return ApiResponseResult.failure("标准工时不能为空！");
        }
        int count = hoursDao.countByDelFlagAndBsCode(0, hours.getBsCode());
        if(count > 0){
            return ApiResponseResult.failure("该产品编码已存在，请填写其他产品编码！");
        }
        hours.setCreateDate(new Date());
        hours.setCreateBy(UserUtil.getSessionUser().getId());
        hoursDao.save(hours);

        return ApiResponseResult.success("产品标准工时添加成功！").data(hours);
    }
    /**
     * 修改工作中心
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Hours hours) throws Exception {
        if(hours == null){
            return ApiResponseResult.failure("产品标准工时不能为空！");
        }
        if(hours.getId() == null){
            return ApiResponseResult.failure("工作中心ID不能为空！");
        }
        if(StringUtils.isEmpty(hours.getBsCode())){
            return ApiResponseResult.failure("产品编码不能为空！");
        }
        if(StringUtils.isEmpty(hours.getBsStdHrs())){
            return ApiResponseResult.failure("标准工时不能为空！");
        }
        Hours o = hoursDao.findById((long) hours.getId());
        if(o == null){
            return ApiResponseResult.failure("该产品编码不存在！");
        }
        //判断工作中心编号是否有变化，有则修改；没有则不修改
        if(o.getBsCode().equals(hours.getBsCode())){
        }else{
            int count = hoursDao.countByDelFlagAndBsCode(0, hours.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("产品编码已存在，请填写其他产品编码！");
            }
            o.setBsCode(hours.getBsCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsStdHrs(hours.getBsStdHrs());
        hoursDao.save(o);
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
    public ApiResponseResult getHours(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("产品编码ID不能为空！");
        }
        Hours o = hoursDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该产品编码不存在！");
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
            return ApiResponseResult.failure("产品编码ID不能为空！");
        }
        Hours o  = hoursDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该产品编码不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        hoursDao.save(o);
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
					filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsStdHrs", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Hours> spec = Specification.where(BaseService.and(filters, Hours.class));
				Specification<Hours> spec1 = spec.and(BaseService.or(filters1, Hours.class));
				Page<Hours> page = hoursDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
