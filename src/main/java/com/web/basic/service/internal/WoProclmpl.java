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
import com.web.basic.dao.WoProcDao;
import com.web.basic.entity.WoProc;
import com.web.basic.service.WoProcService;


@Service(value = "woProcService")
@Transactional(propagation = Propagation.REQUIRED)
public class WoProclmpl implements WoProcService {
	@Autowired
    private WoProcDao woProcDao;
	
	 /**
     * 新增工序
     */
    @Override
    @Transactional
    public ApiResponseResult add(WoProc woProc) throws Exception{
        if(woProc == null){
            return ApiResponseResult.failure("工序不能为空！");
        }
        if(StringUtils.isEmpty(woProc.getBsCode())){
            return ApiResponseResult.failure("工序编码不能为空！");
        }
        if(StringUtils.isEmpty(woProc.getBsName())){
            return ApiResponseResult.failure("工序名称不能为空！");
        }
        int count = woProcDao.countByIsDelAndBsCode(0, woProc.getBsCode());
        if(count > 0){
            return ApiResponseResult.failure("该工序已存在，请填写其他工序编码！");
        }
        woProc.setCreatedTime(new Date());
        woProcDao.save(woProc);

        return ApiResponseResult.success("工序添加成功！").data(woProc);
    }
    /**
     * 修改工序
     */
    @Override
    @Transactional
    public ApiResponseResult edit(WoProc woProc) throws Exception {
        if(woProc == null){
            return ApiResponseResult.failure("工序不能为空！");
        }
        if(woProc.getId() == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(StringUtils.isEmpty(woProc.getBsCode())){
            return ApiResponseResult.failure("工序编码不能为空！");
        }
        if(StringUtils.isEmpty(woProc.getBsName())){
            return ApiResponseResult.failure("工序名称不能为空！");
        }
        WoProc o = woProcDao.findById((long) woProc.getId());
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        //判断工序编码是否有变化，有则修改；没有则不修改
        if(o.getBsCode().equals(woProc.getBsCode())){
        }else{
            int count = woProcDao.countByIsDelAndBsCode(0, woProc.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("工序编码已存在，请填写其他工序编码！");
            }
            o.setBsCode(woProc.getBsCode().trim());
        }
        o.setModifiedTime(new Date());
        o.setBsName(woProc.getBsName());
        woProcDao.save(o);
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
    public ApiResponseResult getWoProc(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        WoProc o = woProcDao.findById((long) id);
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
        WoProc o  = woProcDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        o.setModifiedTime(new Date());
        o.setIsDel(1);
        woProcDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }
    
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        WoProc o = woProcDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序不存在！");
        }
        o.setModifiedTime(new Date());
        o.setBsStatus(bsStatus);
        woProcDao.save(o);
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
				Specification<WoProc> spec = Specification.where(BaseService.and(filters, WoProc.class));
				Specification<WoProc> spec1 = spec.and(BaseService.or(filters1, WoProc.class));
				Page<WoProc> page = woProcDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
