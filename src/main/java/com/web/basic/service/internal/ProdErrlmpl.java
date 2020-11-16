package com.web.basic.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.ProdErrDao;
import com.web.basic.entity.ProdErr;
import com.web.basic.service.ProdErrService;
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


@Service(value = "ProdErrService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProdErrlmpl implements ProdErrService {
	@Autowired
    private ProdErrDao prodErrDao;

	 /**
     * 新增不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult add(ProdErr prodErr) throws Exception{
        if(prodErr == null){
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        if(StringUtils.isEmpty(prodErr.getErrCode())){
            return ApiResponseResult.failure("异常代码不能为空！");
        }
        if(StringUtils.isEmpty(prodErr.getErrName())){
            return ApiResponseResult.failure("异常类别不能为空！");
        }
        int count = prodErrDao.countByDelFlagAndErrCode(0, prodErr.getErrCode());
        if(count > 0){
            return ApiResponseResult.failure("该不良类别编号已存在，请填写其他不良类别编码！");
        }
        prodErr.setCreateDate(new Date());
        prodErr.setCreateBy(UserUtil.getSessionUser().getId());
        prodErrDao.save(prodErr);
        return ApiResponseResult.success("异常类别添加成功！").data(prodErr);
    }
    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ProdErr prodErr) throws Exception {
        if(prodErr == null){
            return ApiResponseResult.failure("异常类别不能为空！");
        }
        if(prodErr.getId() == null){
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        if(StringUtils.isEmpty(prodErr.getErrCode())){
            return ApiResponseResult.failure("异常类别编码不能为空！");
        }
        if(StringUtils.isEmpty(prodErr.getErrName())){
            return ApiResponseResult.failure("异常类别不能为空！");
        }
        ProdErr o = prodErrDao.findById((long) prodErr.getId());
        if(o == null){
            return ApiResponseResult.failure("该异常类别不存在！");
        }
        //判断异常类别代码是否有变化，有则修改；没有则不修改
        if(o.getErrCode().equals(prodErr.getErrCode())){
        }else{
            int count = prodErrDao.countByDelFlagAndErrCode(0, prodErr.getErrCode());
            if(count > 0){
                return ApiResponseResult.failure("异常类别代码已存在，请填写其他不良类别代码！");
            }
            o.setErrCode(prodErr.getErrCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setErrName(prodErr.getErrName());
        prodErrDao.save(o);
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
    public ApiResponseResult getProdErr(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        ProdErr o = prodErrDao.findById((long) id);
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
        ProdErr o  = prodErrDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        prodErrDao.save(o);
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
					filters1.add(new SearchFilter("errCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("errName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<ProdErr> spec = Specification.where(BaseService.and(filters, ProdErr.class));
				Specification<ProdErr> spec1 = spec.and(BaseService.or(filters1, ProdErr.class));
				Page<ProdErr> page = prodErrDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
}
