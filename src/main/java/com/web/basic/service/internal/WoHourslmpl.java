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
import com.web.basic.dao.WoHoursDao;
import com.web.basic.entity.WoHours;
import com.web.basic.service.WoHoursService;


@Service(value = "woHoursService")
@Transactional(propagation = Propagation.REQUIRED)
public class WoHourslmpl implements WoHoursService {
	@Autowired
    private WoHoursDao woHoursDao;
	
	 /**
     * 新增产品标准工时
     */
    @Override
    @Transactional
    public ApiResponseResult add(WoHours woHours) throws Exception{
        if(woHours == null){
            return ApiResponseResult.failure("产品标准工时不能为空！");
        }
        if(StringUtils.isEmpty(woHours.getBsCode())){
            return ApiResponseResult.failure("产品编码不能为空！");
        }
        if(StringUtils.isEmpty(woHours.getBsStdHrs())){
            return ApiResponseResult.failure("标准工时不能为空！");
        }
        int count = woHoursDao.countByIsDelAndBsCode(0, woHours.getBsCode());
        if(count > 0){
            return ApiResponseResult.failure("该产品编码已存在，请填写其他产品编码！");
        }
        woHours.setCreatedTime(new Date());
        woHoursDao.save(woHours);

        return ApiResponseResult.success("产品标准工时添加成功！").data(woHours);
    }
    /**
     * 修改工作中心
     */
    @Override
    @Transactional
    public ApiResponseResult edit(WoHours woHours) throws Exception {
        if(woHours == null){
            return ApiResponseResult.failure("产品标准工时不能为空！");
        }
        if(woHours.getId() == null){
            return ApiResponseResult.failure("工作中心ID不能为空！");
        }
        if(StringUtils.isEmpty(woHours.getBsCode())){
            return ApiResponseResult.failure("产品编码不能为空！");
        }
        if(StringUtils.isEmpty(woHours.getBsStdHrs())){
            return ApiResponseResult.failure("标准工时不能为空！");
        }
        WoHours o = woHoursDao.findById((long) woHours.getId());
        if(o == null){
            return ApiResponseResult.failure("该产品编码不存在！");
        }
        //判断工作中心编号是否有变化，有则修改；没有则不修改
        if(o.getBsCode().equals(woHours.getBsCode())){
        }else{
            int count = woHoursDao.countByIsDelAndBsCode(0, woHours.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("产品编码已存在，请填写其他产品编码！");
            }
            o.setBsCode(woHours.getBsCode().trim());
        }
        o.setModifiedTime(new Date());
        o.setBsStdHrs(woHours.getBsStdHrs());
        woHoursDao.save(o);
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
    public ApiResponseResult getWoHours(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("产品编码ID不能为空！");
        }
        WoHours o = woHoursDao.findById((long) id);
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
        WoHours o  = woHoursDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该产品编码不存在！");
        }
        o.setModifiedTime(new Date());
        o.setIsDel(1);
        woHoursDao.save(o);
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
				filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsStdHrs", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<WoHours> spec = Specification.where(BaseService.and(filters, WoHours.class));
				Specification<WoHours> spec1 = spec.and(BaseService.or(filters1, WoHours.class));
				Page<WoHours> page = woHoursDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
