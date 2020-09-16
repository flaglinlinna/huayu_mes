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
import com.web.basic.dao.ChkBadDao;
import com.web.basic.entity.ChkBad;
import com.web.basic.service.ChkBadService;


@Service(value = "chkBadService")
@Transactional(propagation = Propagation.REQUIRED)
public class ChkBadlmpl implements ChkBadService {
	@Autowired
    private ChkBadDao chkBadDao;
	
	 /**
     * 新增不良内容
     */
    @Override
    @Transactional
    public ApiResponseResult add(ChkBad chkBad) throws Exception{
        if(chkBad == null){
            return ApiResponseResult.failure("不良内容不能为空！");
        }
        if(StringUtils.isEmpty(chkBad.getBsCode())){
            return ApiResponseResult.failure("不良编码不能为空！");
        }
        if(StringUtils.isEmpty(chkBad.getBsName())){
            return ApiResponseResult.failure("不良名称不能为空！");
        }
        int count = chkBadDao.countByIsDelAndBsCode(0, chkBad.getBsCode());
        if(count > 0){
            return ApiResponseResult.failure("该不良内容已存在，请填写其他不良编码！");
        }
        chkBad.setCreatedTime(new Date());
        chkBadDao.save(chkBad);

        return ApiResponseResult.success("不良内容添加成功！").data(chkBad);
    }
    /**
     * 修改不良内容
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ChkBad chkBad) throws Exception {
        if(chkBad == null){
            return ApiResponseResult.failure("不良内容不能为空！");
        }
        if(chkBad.getId() == null){
            return ApiResponseResult.failure("不良ID不能为空！");
        }
        if(StringUtils.isEmpty(chkBad.getBsCode())){
            return ApiResponseResult.failure("不良编码不能为空！");
        }
        if(StringUtils.isEmpty(chkBad.getBsName())){
            return ApiResponseResult.failure("不良名称不能为空！");
        }
        ChkBad o = chkBadDao.findById((long) chkBad.getId());
        if(o == null){
            return ApiResponseResult.failure("该不良内容不存在！");
        }
        //判断不良内容编码是否有变化，有则修改；没有则不修改
        if(o.getBsCode().equals(chkBad.getBsCode())){
        }else{
            int count = chkBadDao.countByIsDelAndBsCode(0, chkBad.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("不良编码已存在，请填写其他不良编码！");
            }
            o.setBsCode(chkBad.getBsCode().trim());
        }
        o.setModifiedTime(new Date());
        o.setBsName(chkBad.getBsName());
        chkBadDao.save(o);
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
    public ApiResponseResult getChkBad(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良ID不能为空！");
        }
        ChkBad o = chkBadDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该不良内容不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除不良内容
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良内容ID不能为空！");
        }
        ChkBad o  = chkBadDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("不良内容不存在！");
        }
        o.setModifiedTime(new Date());
        o.setIsDel(1);
        chkBadDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }
    
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良内容ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        ChkBad o = chkBadDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("不良内容不存在！");
        }
        o.setModifiedTime(new Date());
        o.setBsStatus(bsStatus);
        chkBadDao.save(o);
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
				filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<ChkBad> spec = Specification.where(BaseService.and(filters, ChkBad.class));
				Specification<ChkBad> spec1 = spec.and(BaseService.or(filters1, ChkBad.class));
				Page<ChkBad> page = chkBadDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
