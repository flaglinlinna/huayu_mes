package com.system.defect.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.defect.dao.SysDefectDao;
import com.system.defect.entity.SysDefect;
import com.system.defect.service.SysDefectService;
import com.system.user.entity.SysUser;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.service.DefectiveService;
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

/**
 * 缺陷记录
 */
@Service(value = "SysDefectService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysDefectImpl implements SysDefectService {

    @Autowired
    private SysDefectDao sysDefectDao;

    @Override
    @Transactional
    public ApiResponseResult add(SysDefect sysDefect) throws Exception {
        if(sysDefect == null){
            return ApiResponseResult.failure("缺陷记录不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();
        
        sysDefect.setOfferName(currUser!=null ? currUser.getUserName() : null);
        sysDefect.setOfferDate(new Date());
        sysDefect.setCreateDate(new Date());
        sysDefect.setCreateBy(currUser!=null ? currUser.getId() : null);
        sysDefectDao.save(sysDefect);

        return ApiResponseResult.success("新增成功！").data(sysDefect);
    }

    @Override
    @Transactional
    public ApiResponseResult edit(SysDefect sysDefect) throws Exception {
        if(sysDefect == null || sysDefect.getId() == null){
            return ApiResponseResult.failure("缺陷记录ID不能为空！");
        }
        SysDefect o = sysDefectDao.findById((long) sysDefect.getId());
        if(o == null){
            return ApiResponseResult.failure("缺陷记录不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser!=null ? currUser.getId() : null);
        o.setModuleName(sysDefect.getModuleName());
        o.setPriority(sysDefect.getPriority());
        o.setStatus(sysDefect.getStatus());
        o.setDescript(sysDefect.getDescript());
        
        o.setHandlerName(currUser!=null ? currUser.getUserName() : null);
        o.setHandlerDate(new Date());
        
        o.setRemark(sysDefect.getRemark());
        sysDefectDao.save(o);

        return ApiResponseResult.success("编辑成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
        if(id == null){
            return ApiResponseResult.failure("缺陷记录ID不能为空！");
        }
        SysDefect o = sysDefectDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("缺陷记录不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setDelTime(new Date());
        o.setDelBy(currUser!=null ? currUser.getId() : null);
        o.setDelFlag(1);
        sysDefectDao.save(o);

        return ApiResponseResult.success("删除成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult getList(String keyword, Integer priority, String status, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if(priority != null){
            filters.add(new SearchFilter("priority", SearchFilter.Operator.EQ, priority));
        }
        if(StringUtils.isNotEmpty(status)){
            filters.add(new SearchFilter("status", SearchFilter.Operator.EQ, status));
        }
        //查询条件2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("moduleName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("descript", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("offerName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("handlerName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("remark", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<SysDefect> spec = Specification.where(BaseService.and(filters, SysDefect.class));
        Specification<SysDefect> spec1 =  spec.and(BaseService.or(filters1, SysDefect.class));
        Page<SysDefect> page = sysDefectDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}
