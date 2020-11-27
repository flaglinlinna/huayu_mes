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
import com.web.basic.dao.SysParamDao;
import com.web.basic.entity.SysParam;
import com.web.basic.service.SysParamService;


@Service(value = "sysParamService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysParamlmpl implements SysParamService {
	@Autowired
    private SysParamDao sysParamDao;

	 /**
     * 新增系统参数
     */
    @Override
    @Transactional
    public ApiResponseResult add(SysParam sysParam) throws Exception{
        if(sysParam == null){
            return ApiResponseResult.failure("系统参数不能为空！");
        }
        if(StringUtils.isEmpty(sysParam.getParamCode())){
            return ApiResponseResult.failure("系统参数编码不能为空！");
        }
        if(StringUtils.isEmpty(sysParam.getParamName())){
            return ApiResponseResult.failure("系统参数名称不能为空！");
        }
        int count = sysParamDao.countByDelFlagAndParamCode(0, sysParam.getParamCode());
        if(count > 0){
            return ApiResponseResult.failure("该系统参数已存在，请填写其他系统参数编码！");
        }
        sysParam.setCreateDate(new Date());
        sysParam.setCreateBy(UserUtil.getSessionUser().getId());
        sysParamDao.save(sysParam);

        return ApiResponseResult.success("系统参数添加成功！").data(sysParam);
    }
    /**
     * 修改系统参数
     */
    @Override
    @Transactional
    public ApiResponseResult edit(SysParam sysParam) throws Exception {
        if(sysParam == null){
            return ApiResponseResult.failure("系统参数不能为空！");
        }
        if(sysParam.getId() == null){
            return ApiResponseResult.failure("系统参数ID不能为空！");
        }
        if(StringUtils.isEmpty(sysParam.getParamCode())){
            return ApiResponseResult.failure("系统参数编码不能为空！");
        }
        if(StringUtils.isEmpty(sysParam.getParamName())){
            return ApiResponseResult.failure("系统参数名称不能为空！");
        }
        SysParam o = sysParamDao.findById((long) sysParam.getId());
        if(o == null){
            return ApiResponseResult.failure("该系统参数不存在！");
        }
        //判断系统参数编码是否有变化，有则修改；没有则不修改
        if(o.getParamCode().equals(sysParam.getParamCode())){
        }else{
            int count = sysParamDao.countByDelFlagAndParamCode(0, sysParam.getParamCode());
            if(count > 0){
                return ApiResponseResult.failure("系统参数编码已存在，请填写其他系统参数编码！");
            }
            o.setParamCode(sysParam.getParamCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        
        o.setParamType(sysParam.getParamType());
        o.setParamSort(sysParam.getParamSort());
        o.setParamCode(sysParam.getParamCode());
        o.setParamName(sysParam.getParamName());
        o.setDataType(sysParam.getDataType());          
        o.setParamValue(sysParam.getParamValue());
        o.setFmemo(sysParam.getFmemo());
        sysParamDao.save(o);
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
    public ApiResponseResult getSysParam(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("系统参数ID不能为空！");
        }
        SysParam o = sysParamDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该系统参数不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除系统参数
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("系统参数ID不能为空！");
        }
        SysParam o  = sysParamDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("系统参数不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        sysParamDao.save(o);
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
					filters1.add(new SearchFilter("paramSort", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("paramCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("paramName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("dataType", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<SysParam> spec = Specification.where(BaseService.and(filters, SysParam.class));
				Specification<SysParam> spec1 = spec.and(BaseService.or(filters1, SysParam.class));
				Page<SysParam> page = sysParamDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	@Override
	public ApiResponseResult getValueByCodeList(String paramCode) throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(sysParamDao.findByDelFlagAndParamCode(0, paramCode));
	}


}
