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
import com.web.basic.dao.MtrialDao;
import com.web.basic.entity.Mtrial;
import com.web.basic.service.MtrialService;


@Service(value = "mtrialService")
@Transactional(propagation = Propagation.REQUIRED)
public class Mtriallmpl implements MtrialService {
	@Autowired
    private MtrialDao mtrialDao;

	 /**
     * 新增物料
     */
    @Override
    @Transactional
    public ApiResponseResult add(Mtrial mtrial) throws Exception{
        if(mtrial == null){
            return ApiResponseResult.failure("物料不能为空！");
        }
        if(StringUtils.isEmpty(mtrial.getBsCode())){
            return ApiResponseResult.failure("物料编号不能为空！");
        }
        if(StringUtils.isEmpty(mtrial.getBsName())){
            return ApiResponseResult.failure("物料名称不能为空！");
        }
        int count = mtrialDao.countByDelFlagAndBsCode(0, mtrial.getBsCode());
        if(count > 0){
            return ApiResponseResult.failure("该物料已存在，请填写其他物料编号！");
        }
        mtrial.setCreateDate(new Date());
        mtrialDao.save(mtrial);

        return ApiResponseResult.success("物料添加成功！").data(mtrial);
    }
    /**
     * 修改物料
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Mtrial mtrial) throws Exception {
        if(mtrial == null){
            return ApiResponseResult.failure("物料不能为空！");
        }
        if(mtrial.getId() == null){
            return ApiResponseResult.failure("物料ID不能为空！");
        }
        if(StringUtils.isEmpty(mtrial.getBsCode())){
            return ApiResponseResult.failure("物料编号不能为空！");
        }
        if(StringUtils.isEmpty(mtrial.getBsName())){
            return ApiResponseResult.failure("物料名称不能为空！");
        }
        Mtrial o = mtrialDao.findById((long) mtrial.getId());
        if(o == null){
            return ApiResponseResult.failure("该物料不存在！");
        }
        //判断物料编号是否有变化，有则修改；没有则不修改
        if(o.getBsCode().equals(mtrial.getBsCode())){
        }else{
            int count = mtrialDao.countByDelFlagAndBsCode(0, mtrial.getBsCode());
            if(count > 0){
                return ApiResponseResult.failure("物料编号已存在，请填写其他物料编号！");
            }
            o.setBsCode(mtrial.getBsCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setBsName(mtrial.getBsName());
        o.setBsType(mtrial.getBsType());
        o.setBsUnit(mtrial.getBsUnit());
        mtrialDao.save(o);
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
    public ApiResponseResult getMtrial(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("物料ID不能为空！");
        }
        Mtrial o = mtrialDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该物料不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除物料
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("物料ID不能为空！");
        }
        Mtrial o  = mtrialDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该物料不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setDelFlag(1);
        mtrialDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("物料ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Mtrial o = mtrialDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("物料不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setBsStatus(bsStatus);
        mtrialDao.save(o);
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
					filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("bsType", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Mtrial> spec = Specification.where(BaseService.and(filters, Mtrial.class));
				Specification<Mtrial> spec1 = spec.and(BaseService.or(filters1, Mtrial.class));
				Page<Mtrial> page = mtrialDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
