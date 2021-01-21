package com.web.basePrice.service.internal;

import java.text.SimpleDateFormat;
import java.util.*;

import com.system.user.dao.SysUserDao;
import com.system.user.entity.SysUser;
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
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.UnitService;

/**
 *
 * @date Nov 4, 2020 4:27:53 PM
 */
@Service(value = "UnitService")
@Transactional(propagation = Propagation.REQUIRED)
public class UnitImpl implements UnitService {
    @Autowired
    private UnitDao unitDao;

    @Autowired
    private SysUserDao sysUserDao;
    /**
    * 新增基本单位维护
    */
    @Override
    @Transactional
    public ApiResponseResult add(Unit unit) throws Exception{
    if(unit == null){
        return ApiResponseResult.failure("基本单位维护不能为空！");
    }
    if(StringUtils.isEmpty(unit.getUnitCode())){
        return ApiResponseResult.failure("基本单位编码不能为空！");
    }
    if(StringUtils.isEmpty(unit.getUnitName())){
        return ApiResponseResult.failure("基本单位维护名称不能为空！");
    }
    int count = unitDao.countByDelFlagAndUnitCode(0,unit.getUnitCode());
    if(count > 0){
        return ApiResponseResult.failure("该基本单位维护编码已存在，请填写其他基本单位维护编码！");
    }
    unit.setCreateDate(new Date());
    unit.setCreateBy(UserUtil.getSessionUser().getId());
    unitDao.save(unit);
    return ApiResponseResult.success("基本单位维护添加成功！").data(unit);
    }

    /**
    * 修改基本单位维护
    */
    @Override
    @Transactional
    public ApiResponseResult edit(Unit unit) throws Exception {
    if(unit == null){
        return ApiResponseResult.failure("基本单位维护不能为空！");
      }
    if(unit.getId() == null){
        return ApiResponseResult.failure("基本单位维护ID不能为空！");
    }
    if(StringUtils.isEmpty(unit.getUnitCode())){
        return ApiResponseResult.failure("基本单位维护编码不能为空！");
    }
    if(StringUtils.isEmpty(unit.getUnitName())){
        return ApiResponseResult.failure("基本单位维护名称不能为空！");
    }
    Unit o = unitDao.findById((long) unit.getId());
    if(o == null){
        return ApiResponseResult.failure("该基本单位维护不存在！");
    }
    //判断基本单位维护编码是否有变化，有则修改；没有则不修改
    if(o.getUnitCode().equals(unit.getUnitCode())){
   }else{
        int count = unitDao.countByDelFlagAndUnitCode(0, unit.getUnitCode());
        if(count > 0){
        return ApiResponseResult.failure("基本单位维护编码已存在，请填写其他基本单位维护编码！");
        }
        o.setUnitCode(unit.getUnitCode().trim());
    }
    o.setLastupdateDate(new Date());
    o.setLastupdateBy(UserUtil.getSessionUser().getId());
    o.setUnitName(unit.getUnitName());
    o.setUnitCode(unit.getUnitCode());
    unitDao.save(o);
    return ApiResponseResult.success("编辑成功！");
}

    /**
    * 根据ID获取基本单位维护详情
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    @Transactional
    public ApiResponseResult getUnit(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("基本单位维护ID不能为空！");
        }
        Unit o = unitDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该基本单位维护不存在！");
        }
            return ApiResponseResult.success().data(o);
    }

    /**
    * 删除基本单位维护
    */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
    if(id == null){
        return ApiResponseResult.failure("基本单位维护ID不能为空！");
    }
    Unit o  = unitDao.findById((long) id);
    if(o == null){
        return ApiResponseResult.failure("基本单位维护不存在！");
    }
    o.setDelFlag(1);
    unitDao.save(o);
    return ApiResponseResult.success("删除成功！");
    }

    /**
    * 查询基本单位维护列表
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
            filters1.add(new SearchFilter("unitCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("unitName", SearchFilter.Operator.LIKE, keyword));
            }
            Specification<Unit> spec = Specification.where(BaseService.and(filters, Unit.class));
            Specification<Unit> spec1 = spec.and(BaseService.or(filters1, Unit.class));
            Page<Unit> page = unitDao.findAll(spec1, pageRequest);
            List<Unit> unitList = page.getContent();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String,Object>> mapList = new ArrayList<>();
            for(Unit unit:unitList){
                Map<String,Object> map = new HashMap<>();
                map.put("id",unit.getId());
                map.put("unitCode",unit.getUnitCode());
                map.put("unitName",unit.getUnitName());
                map.put("createBy",sysUserDao.findById((long)unit.getCreateBy()).getUserName());
                map.put("createDate",df.format(unit.getCreateDate()));
                if(unit.getLastupdateBy()!=null){
                    map.put("lastupdateBy",sysUserDao.findById((long)unit.getLastupdateBy()).getUserName());
                    map.put("lastupdateDate",df.format(unit.getLastupdateDate()));
                }
                mapList.add(map);
            }
            return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
            pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
        }
}