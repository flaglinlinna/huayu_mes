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
import com.web.basic.dao.DefectiveDao;
import com.web.basic.entity.Defective;
import com.web.basic.service.DefectiveService;


@Service(value = "defectiveService")
@Transactional(propagation = Propagation.REQUIRED)
public class Defectivelmpl implements DefectiveService {
	@Autowired
    private DefectiveDao defectiveDao;

	 /**
     * 新增不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult add(Defective defective) throws Exception{
        if(defective == null){
            return ApiResponseResult.failure("不良类别不能为空！");
        }
        if(StringUtils.isEmpty(defective.getDefectTypeCode())){
            return ApiResponseResult.failure("不良类别编码不能为空！");
        }
        if(StringUtils.isEmpty(defective.getDefectTypeName())){
            return ApiResponseResult.failure("不良类别名称不能为空！");
        }
        int count = defectiveDao.countByDelFlagAndDefectTypeCode(0, defective.getDefectTypeCode());
        if(count > 0){
            return ApiResponseResult.failure("该不良类别编号已存在，请填写其他不良类别编码！");
        }
        defective.setCreateDate(new Date());
        defective.setCreateBy(UserUtil.getSessionUser().getId());
        defectiveDao.save(defective);
        return ApiResponseResult.success("不良类别添加成功！").data(defective);
    }
    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Defective defective) throws Exception {
        if(defective == null){
            return ApiResponseResult.failure("不良类别不能为空！");
        }
        if(defective.getId() == null){
            return ApiResponseResult.failure("不良类别ID不能为空！");
        }
        if(StringUtils.isEmpty(defective.getDefectTypeCode())){
            return ApiResponseResult.failure("不良类别编码不能为空！");
        }
        if(StringUtils.isEmpty(defective.getDefectTypeName())){
            return ApiResponseResult.failure("不良类别名称不能为空！");
        }
        Defective o = defectiveDao.findById((long) defective.getId());
        if(o == null){
            return ApiResponseResult.failure("该不良类别不存在！");
        }
        //判断不良类别编码是否有变化，有则修改；没有则不修改
        if(o.getDefectTypeCode().equals(defective.getDefectTypeCode())){
        }else{
            int count = defectiveDao.countByDelFlagAndDefectTypeCode(0, defective.getDefectTypeCode());
            if(count > 0){
                return ApiResponseResult.failure("不良类别编码已存在，请填写其他不良类别编码！");
            }
            o.setDefectTypeCode(defective.getDefectTypeCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setDefectTypeName(defective.getDefectTypeName());
        o.setDefectClass(defective.getDefectClass());
        defectiveDao.save(o);
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
    public ApiResponseResult getDefective(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良类别ID不能为空！");
        }
        Defective o = defectiveDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该不良类别不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良类别ID不能为空！");
        }
        Defective o  = defectiveDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("不良类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        defectiveDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良类别ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Defective o = defectiveDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("不良类别不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setCheckStatus(checkStatus);
        defectiveDao.save(o);
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
					filters1.add(new SearchFilter("defectTypeCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("defectTypeName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Defective> spec = Specification.where(BaseService.and(filters, Defective.class));
				Specification<Defective> spec1 = spec.and(BaseService.or(filters1, Defective.class));
				Page<Defective> page = defectiveDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
