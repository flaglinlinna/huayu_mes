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
import com.web.basic.dao.LineDao;
import com.web.basic.entity.Line;
import com.web.basic.service.LineService;


@Service(value = "lineService")
@Transactional(propagation = Propagation.REQUIRED)
public class Linelmpl implements LineService {
	@Autowired
    private LineDao lineDao;

	 /**
     * 新增线体
     */
    @Override
    @Transactional
    public ApiResponseResult add(Line line) throws Exception{
        if(line == null){
            return ApiResponseResult.failure("线体不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineNo())){
            return ApiResponseResult.failure("线体编号不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineName())){
            return ApiResponseResult.failure("线体名称不能为空！");
        }
        int count = lineDao.countByDelFlagAndLineNo(0, line.getLineNo());
        if(count > 0){
            return ApiResponseResult.failure("该线体已存在，请填写其他线体编号！");
        }
        line.setCreateDate(new Date());
        line.setCreateBy(UserUtil.getSessionUser().getId());
        lineDao.save(line);

        return ApiResponseResult.success("线体添加成功！").data(line);
    }
    /**
     * 修改线体
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Line line) throws Exception {
        if(line == null){
            return ApiResponseResult.failure("线体不能为空！");
        }
        if(line.getId() == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineNo())){
            return ApiResponseResult.failure("线体编号不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineName())){
            return ApiResponseResult.failure("线体名称不能为空！");
        }
        Line o = lineDao.findById((long) line.getId());
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        //判断线体编号是否有变化，有则修改；没有则不修改
        if(o.getLineNo().equals(line.getLineNo())){
        }else{
            int count = lineDao.countByDelFlagAndLineNo(0, line.getLineNo());
            if(count > 0){
                return ApiResponseResult.failure("线体编号已存在，请填写其他线体编号！");
            }
            o.setLineNo(line.getLineNo().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setLineName(line.getLineName());
        o.setLinerCode(line.getLinerCode());
        o.setLinerName(line.getLinerName());
        lineDao.save(o);
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
    public ApiResponseResult getLine(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        Line o = lineDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除线体
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        Line o  = lineDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());	
        lineDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Line o = lineDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("线体不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setCheckStatus(checkStatus);
        lineDao.save(o);
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
					filters1.add(new SearchFilter("lineNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("lineName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("linerCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("linerName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Line> spec = Specification.where(BaseService.and(filters, Line.class));
				Specification<Line> spec1 = spec.and(BaseService.or(filters1, Line.class));
				Page<Line> page = lineDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
