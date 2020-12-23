package com.web.basic.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.SysParamSubDao;
import com.web.basic.entity.SysParamSub;
import com.web.basic.service.SysParamSubService;
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


@Service(value = "sysParamSubService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysParamSublmpl implements SysParamSubService {
	@Autowired
    private SysParamSubDao sysParamSubDao;

	 /**
     * 新增系统参数
     */
    @Override
    @Transactional
    public ApiResponseResult add(SysParamSub sysParamSub) throws Exception{
        if(sysParamSub == null){
            return ApiResponseResult.failure("系统子参数不能为空！");
        }
        int count = sysParamSubDao.countByMidAndDelFlagAndSubCode(sysParamSub.getMid(),0, sysParamSub.getSubCode());
        if(count > 0){
            return ApiResponseResult.failure("该系统下快码编码已存在，请填写其他快码编码！");
        }
        count = sysParamSubDao.countByMidAndDelFlagAndSubName(sysParamSub.getMid(),0, sysParamSub.getSubName());
        if(count > 0){
            return ApiResponseResult.failure("该系统下快码名称已存在，请填写其他快码名称！");
        }
        sysParamSub.setCreateDate(new Date());
        sysParamSub.setCreateBy(UserUtil.getSessionUser().getId());
        sysParamSubDao.save(sysParamSub);
        return ApiResponseResult.success("系统参数添加成功！").data(sysParamSub);
    }
    /**
     * 修改系统参数
     */
    @Override
    @Transactional
    public ApiResponseResult edit(SysParamSub sysParamSub) throws Exception {
        if(sysParamSub == null){
            return ApiResponseResult.failure("系统参数不能为空！");
        }
        if(sysParamSub.getId() == null){
            return ApiResponseResult.failure("系统参数ID不能为空！");
        }
        if(sysParamSub.getMid()==null){
            return ApiResponseResult.failure("系统参数主表编码不能为空！");
        }
        SysParamSub o = sysParamSubDao.findById((long) sysParamSub.getId());
        if(o == null){
            return ApiResponseResult.failure("该系统参数不存在！");
        }
        //判断系统子参数编码是否有变化，有则修改；没有则不修改
        if(o.getSubCode().equals(sysParamSub.getSubCode())){
        }else{
            int count = sysParamSubDao.countByMidAndDelFlagAndSubCode(sysParamSub.getMid(),0, sysParamSub.getSubCode());
            if(count > 0){
                return ApiResponseResult.failure("系统子参数快码编码已存在，请填写其他快码编码！");
            }
            o.setSubCode(sysParamSub.getSubCode().trim());
        }
        //判断系统子参数编码是否有变化，有则修改；没有则不修改
        if(o.getSubName().equals(sysParamSub.getSubName())){
        }else{
            int count = sysParamSubDao.countByMidAndDelFlagAndSubName(sysParamSub.getMid(),0, sysParamSub.getSubName());
            if(count > 0){
                return ApiResponseResult.failure("系统子参数快码名称已存在，请填写其他快码名称！");
            }
            o.setSubName(sysParamSub.getSubName().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setForder(sysParamSub.getForder());
        sysParamSubDao.save(o);
        return ApiResponseResult.success("编辑成功！");
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
        SysParamSub o  = sysParamSubDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("系统子参数不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        sysParamSubDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 查询列表
     */
	@Override
    @Transactional
	public ApiResponseResult getList(Long mid, PageRequest pageRequest) throws Exception {
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				filters.add(new SearchFilter("mid", SearchFilter.Operator.EQ, mid));
				Specification<SysParamSub> spec = Specification.where(BaseService.and(filters, SysParamSub.class));
				Page<SysParamSub> page = sysParamSubDao.findAll(spec, pageRequest);
				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	@Override
	public ApiResponseResult getListByMCode(String mcode) throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(sysParamSubDao.findByDelFlagAndCode(mcode));
	}



}
