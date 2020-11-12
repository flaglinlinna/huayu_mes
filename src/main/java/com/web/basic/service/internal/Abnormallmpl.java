package com.web.basic.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.AbnormalDao;
import com.web.basic.entity.Abnormal;
import com.web.basic.service.AbnormalService;
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


@Service(value = "abnormalService")
@Transactional(propagation = Propagation.REQUIRED)
public class Abnormallmpl implements AbnormalService {
	@Autowired
    private AbnormalDao abnormalDao;

	 /**
     * 新增不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult add(Abnormal abnormal) throws Exception{
        if(abnormal == null){
            return ApiResponseResult.failure("异常类别不能为空！");
        }
        if(StringUtils.isEmpty(abnormal.getAbnormalCode())){
            return ApiResponseResult.failure("异常代码不能为空！");
        }
        if(StringUtils.isEmpty(abnormal.getAbnormalType())){
            return ApiResponseResult.failure("异常类别不能为空！");
        }
        int count = abnormalDao.countByDelFlagAndAbnormalCode(0, abnormal.getAbnormalCode());
        if(count > 0){
            return ApiResponseResult.failure("该不良类别编号已存在，请填写其他不良类别编码！");
        }
        abnormal.setCreateDate(new Date());
        abnormal.setCreateBy(UserUtil.getSessionUser().getId());
        abnormalDao.save(abnormal);
        return ApiResponseResult.success("异常类别添加成功！").data(abnormal);
    }
    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Abnormal abnormal) throws Exception {
        if(abnormal == null){
            return ApiResponseResult.failure("异常类别不能为空！");
        }
        if(abnormal.getId() == null){
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        if(StringUtils.isEmpty(abnormal.getAbnormalCode())){
            return ApiResponseResult.failure("异常类别编码不能为空！");
        }
        if(StringUtils.isEmpty(abnormal.getAbnormalType())){
            return ApiResponseResult.failure("异常类别不能为空！");
        }
        Abnormal o = abnormalDao.findById((long) abnormal.getId());
        if(o == null){
            return ApiResponseResult.failure("该异常类别不存在！");
        }
        //判断异常类别代码是否有变化，有则修改；没有则不修改
        if(o.getAbnormalCode().equals(abnormal.getAbnormalCode())){
        }else{
            int count = abnormalDao.countByDelFlagAndAbnormalCode(0, abnormal.getAbnormalCode());
            if(count > 0){
                return ApiResponseResult.failure("异常类别代码已存在，请填写其他不良类别代码！");
            }
            o.setAbnormalCode(abnormal.getAbnormalCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setAbnormalType(abnormal.getAbnormalType());
        abnormalDao.save(o);
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
    public ApiResponseResult getAbnormal(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        Abnormal o = abnormalDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该异常类别不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除异常类别
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        Abnormal o  = abnormalDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        abnormalDao.save(o);
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
					filters1.add(new SearchFilter("abnormalCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("abnormalType", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Abnormal> spec = Specification.where(BaseService.and(filters, Abnormal.class));
				Specification<Abnormal> spec1 = spec.and(BaseService.or(filters1, Abnormal.class));
				Page<Abnormal> page = abnormalDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
