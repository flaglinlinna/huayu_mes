package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.quote.dao.HardwareDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.HardwareMater;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.entity.QuoteItemBase;
import com.web.quote.service.HardwareService;
import com.web.quote.service.QuoteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "HardwareService")
@Transactional(propagation = Propagation.REQUIRED)
public class Hardwarelmpl implements HardwareService {
	
	@Autowired
    private HardwareDao hardwareDao;
	
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(HardwareMater hardwareMater)throws Exception{
    	if(hardwareMater == null){
            return ApiResponseResult.failure("五金材料不能为空！");
        }
		hardwareDao.save(hardwareMater);
        return ApiResponseResult.success("报价单新增成功！").data(hardwareMater);
	}

    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(HardwareMater hardwareMater) throws Exception {
        if(hardwareMater == null){
            return ApiResponseResult.failure("五金材料不能为空！");
        }
        if(hardwareMater.getId() == null){
            return ApiResponseResult.failure("五金材料ID不能为空！");
        }

        HardwareMater o = hardwareDao.findById((long) hardwareMater.getId());
        if(o == null){
            return ApiResponseResult.failure("该五金材料不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
    //
        hardwareDao.save(o);
        return ApiResponseResult.success("编辑成功！");
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
        HardwareMater o  = hardwareDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        hardwareDao.save(o);
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
        Specification<HardwareMater> spec = Specification.where(BaseService.and(filters, HardwareMater.class));
        Specification<HardwareMater> spec1 = spec.and(BaseService.or(filters1, HardwareMater.class));
        Page<HardwareMater> page = hardwareDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}
