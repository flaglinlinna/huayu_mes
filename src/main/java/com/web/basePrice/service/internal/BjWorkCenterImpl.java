package com.web.basePrice.service.internal;

import java.text.SimpleDateFormat;
import java.util.*;

import com.system.user.dao.SysUserDao;

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
import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.BjWorkCenterService;
import com.web.basic.entity.Mtrial;

/**
 *
 * @date Nov 4, 2020 4:50:15 PM
 */
@Service(value = "BjWorkCenterService")
@Transactional(propagation = Propagation.REQUIRED)
public class BjWorkCenterImpl implements BjWorkCenterService {
    @Autowired
    private BjWorkCenterDao workCenterDao;

    @Autowired
    private SysUserDao sysUserDao;

    /**
    * 新增工作中心维护
    */
    @Override
    @Transactional
    public ApiResponseResult add(BjWorkCenter workCenter) throws Exception{
        if(workCenter == null){
            return ApiResponseResult.failure("工作中心维护不能为空！");
        }
    if(StringUtils.isEmpty(workCenter.getWorkcenterCode())){
        return ApiResponseResult.failure("工作中心维护编码不能为空！");
    }
    if(StringUtils.isEmpty(workCenter.getWorkcenterName())){
        return ApiResponseResult.failure("工作中心维护名称不能为空！");
    }
    int count = workCenterDao.countByDelFlagAndWorkcenterCode(0, workCenter.getWorkcenterCode());
    if(count > 0){
   return ApiResponseResult.failure("该工作中心维护已存在，请填写其他工作中心维护编码！");
    }
    workCenter.setCreateDate(new Date());
    workCenter.setCreateBy(UserUtil.getSessionUser().getId());
    workCenterDao.save(workCenter);
    return ApiResponseResult.success("工作中心维护添加成功！").data(workCenter);
    }

    /**
    * 修改工作中心维护
    */
    @Override
    @Transactional
    public ApiResponseResult edit(BjWorkCenter workCenter) throws Exception {
        if(workCenter == null){
            return ApiResponseResult.failure("工作中心维护不能为空！");
      }
    if(workCenter.getId() == null){
        return ApiResponseResult.failure("工作中心维护ID不能为空！");
    }
    if(StringUtils.isEmpty(workCenter.getWorkcenterCode())){
        return ApiResponseResult.failure("工作中心维护编码不能为空！");
    }
    if(StringUtils.isEmpty(workCenter.getWorkcenterName())){
        return ApiResponseResult.failure("工作中心维护名称不能为空！");
    }
    BjWorkCenter o = workCenterDao.findById((long) workCenter.getId());
    if(o == null){
        return ApiResponseResult.failure("该工作中心维护不存在！");
    }
    //判断工作中心维护编码是否有变化，有则修改；没有则不修改
    if(o.getWorkcenterCode().equals(workCenter.getWorkcenterCode())){
   }else{
        int count = workCenterDao.countByDelFlagAndWorkcenterCode(0, workCenter.getWorkcenterCode());
        if(count > 0){
        return ApiResponseResult.failure("工作中心维护编码已存在，请填写其他工作中心维护编码！");
        }
        o.setWorkcenterCode(workCenter.getWorkcenterCode().trim());
    }
   o.setLastupdateDate(new Date());
   o.setLastupdateBy(UserUtil.getSessionUser().getId());
   o.setFmemo(workCenter.getFmemo());
   o.setWorkcenterName(workCenter.getWorkcenterName());
   o.setCheckStatus(workCenter.getCheckStatus());
    workCenterDao.save(o);
    return ApiResponseResult.success("编辑成功！");
}

    /**
    * 根据ID获取工作中心维护详情
    * @param id
    * @return
    * @throws Exception
    */
    @Override
    @Transactional
    public ApiResponseResult getWorkCenter(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工作中心维护ID不能为空！");
        }
        BjWorkCenter o = workCenterDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工作中心维护不存在！");
        }
            return ApiResponseResult.success().data(o);
    }

    /**
    * 删除工作中心维护
    */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
    if(id == null){
        return ApiResponseResult.failure("工作中心维护ID不能为空！");
    }
    BjWorkCenter o  = workCenterDao.findById((long) id);
    if(o == null){
        return ApiResponseResult.failure("工作中心维护不存在！");
    }
    o.setDelFlag(1);
    workCenterDao.save(o);
    return ApiResponseResult.success("删除成功！");
    }
    
    /**
     * 有效状态切换
     * */
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工作中心ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        BjWorkCenter o = workCenterDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工作中心不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setCheckStatus(checkStatus);
        workCenterDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
    }

    /**
    * 查询工作中心维护列表
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
            filters1.add(new SearchFilter("workcenterCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("workcenterName", SearchFilter.Operator.LIKE, keyword));
           }
            Specification<BjWorkCenter> spec = Specification.where(BaseService.and(filters, BjWorkCenter.class));
            Specification<BjWorkCenter> spec1 = spec.and(BaseService.or(filters1, BjWorkCenter.class));
            Page<BjWorkCenter> page = workCenterDao.findAll(spec1, pageRequest);
        List<BjWorkCenter> bjWorkCenterList = page.getContent();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> mapList = new ArrayList<>();
        for(BjWorkCenter bjWorkCenter:bjWorkCenterList){
            Map<String,Object> map = new HashMap<>();
            map.put("id",bjWorkCenter.getId());
            map.put("workcenterCode",bjWorkCenter.getWorkcenterCode());
            map.put("workcenterName",bjWorkCenter.getWorkcenterName());
            map.put("fmemo",bjWorkCenter.getFmemo());
            map.put("checkStatus",bjWorkCenter.getCheckStatus());
            map.put("createBy",sysUserDao.findById((long)bjWorkCenter.getCreateBy()).getUserName());
            map.put("createDate",df.format(bjWorkCenter.getCreateDate()));
            if(bjWorkCenter.getLastupdateBy()!=null){
                map.put("lastupdateBy",sysUserDao.findById((long)bjWorkCenter.getCreateBy()).getUserName());
                map.put("lastupdateDate",df.format(bjWorkCenter.getLastupdateDate()));
            }
            mapList.add(map);
        }
            return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
            pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}