package com.web.po.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.po.dao.InterfacesDao;
import com.web.po.dao.InterfacesRequestDao;
import com.web.po.entity.Interfaces;
import com.web.po.entity.InterfacesRequest;
import com.web.po.service.InterfacesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 接口信息配置表
 *
 */
@Service(value = "InterfacesService")
@Transactional(propagation = Propagation.REQUIRED)
public class InterfacesImpl implements InterfacesService {

    @Autowired
    private InterfacesDao interfacesDao;
    @Autowired
    private InterfacesRequestDao interfacesRequestDao;

    @Override
    @Transactional
    public ApiResponseResult add(Interfaces interfaces) throws Exception {
        if(interfaces == null){
            return ApiResponseResult.failure("接口配置信息不能为空！");
        }
        if(StringUtils.isEmpty(interfaces.getBsCode())){
            return ApiResponseResult.failure("接口编号不能为空！");
        }
        if(StringUtils.isEmpty(interfaces.getBsName())){
            return ApiResponseResult.failure("接口名称不能为空！");
        }
        if(StringUtils.isEmpty(interfaces.getBsUrl())){
            return ApiResponseResult.failure("请求地址不能为空！");
        }

        interfaces.setCreatedTime(new Date());
        interfacesDao.save(interfaces);

        return ApiResponseResult.success("新增成功！").data(interfaces);
    }

    @Override
    @Transactional
    public ApiResponseResult edit(Interfaces interfaces) throws Exception {
        if(interfaces == null || interfaces.getId() == null){
            return ApiResponseResult.failure("接口配置信息不能为空！");
        }
        if(StringUtils.isEmpty(interfaces.getBsCode())){
            return ApiResponseResult.failure("接口编号不能为空！");
        }
        if(StringUtils.isEmpty(interfaces.getBsName())){
            return ApiResponseResult.failure("接口名称不能为空！");
        }
        if(StringUtils.isEmpty(interfaces.getBsUrl())){
            return ApiResponseResult.failure("请求地址不能为空！");
        }
        Interfaces o = interfacesDao.findById((long) interfaces.getId());
        if(o == null){
            return ApiResponseResult.failure("接口配置信息不存在！");
        }

        o.setModifiedTime(new Date());
        o.setBsCode(interfaces.getBsCode());
        o.setBsName(interfaces.getBsName());
        o.setBsStatus(interfaces.getBsStatus());
        o.setBsMethod(interfaces.getBsMethod());
        o.setBsUrl(interfaces.getBsUrl());
        o.setBsParam(interfaces.getBsParam());
        o.setBsType(interfaces.getBsType());
        o.setStartDate(interfaces.getStartDate());
        o.setEndDate(interfaces.getEndDate());
        o.setTimingCode(interfaces.getTimingCode());
        o.setBsRemark(interfaces.getBsRemark());
        interfacesDao.save(o);

        return ApiResponseResult.success("编辑成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
        if(id == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        Interfaces o = interfacesDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("接口配置信息不存在！");
        }

        o.setModifiedTime(new Date());
        o.setIsDel(1);
        interfacesDao.save(o);

        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponseResult getList(String keyword, String bsCode, String bsName, Integer bsStatus, Date createdTimeStart, Date createdTimeEnd, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if(StringUtils.isNotEmpty(bsCode)){
            filters.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, bsCode));
        }
        if(StringUtils.isNotEmpty(bsName)){
            filters.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, bsName));
        }
        if(bsStatus != null){
            filters.add(new SearchFilter("bsStatus", SearchFilter.Operator.EQ, bsStatus));
        }
        if(createdTimeStart != null){
            filters.add(new SearchFilter("createdTime", SearchFilter.Operator.GTE, createdTimeStart));
        }
        if(createdTimeEnd != null){
            filters.add(new SearchFilter("createdTime", SearchFilter.Operator.LTE, createdTimeEnd));
        }
        //查询2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsUrl", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsRemark", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<Interfaces> spec = Specification.where(BaseService.and(filters, Interfaces.class));
        Specification<Interfaces> spec1 =  spec.and(BaseService.or(filters1, Interfaces.class));
        Page<Interfaces> page = interfacesDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 根据ID获取接口配置信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getInterfaces(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("接口ID不能为空！");
        }
        Interfaces o = interfacesDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该接口不存在！");
        }
        return ApiResponseResult.success().data(o);
    }

    /**
     * 根据ID设置启用/禁用
     * @param id
     * @param bsStatus
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("接口ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Interfaces o = interfacesDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("接口不存在！");
        }

        o.setModifiedTime(new Date());
        o.setBsStatus(bsStatus);
        interfacesDao.save(o);

        return ApiResponseResult.success("设置成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult addRequest(InterfacesRequest request) throws Exception {
        if(request == null){
            return ApiResponseResult.failure("请求参数信息不能为空！");
        }
        if(request.getInterId() == null){
            return ApiResponseResult.failure("接口ID不能为空！");
        }
        if(StringUtils.isEmpty(request.getBsName())){
            return ApiResponseResult.failure("参数名称不能为空！");
        }

        request.setCreatedTime(new Date());
        interfacesRequestDao.save(request);

        return ApiResponseResult.success("新增成功").data(request);
    }

    @Override
    @Transactional
    public ApiResponseResult editRequest(InterfacesRequest request) throws Exception {
        if(request == null || request.getId() == null){
            return ApiResponseResult.failure("请求参数信息不能为空！");
        }
        if(StringUtils.isEmpty(request.getBsName())){
            return ApiResponseResult.failure("参数名称不能为空！");
        }
        InterfacesRequest o = interfacesRequestDao.findById((long) request.getId());
        if(o == null){
            return ApiResponseResult.failure("请求参数信息不存在！");
        }

        o.setModifiedTime(new Date());
        o.setBsName(request.getBsName());
        o.setBsType(request.getBsType());
        o.setBsDescpt(request.getBsDescpt());
        o.setBsValue(request.getBsValue());
        o.setIsRequired(request.getIsRequired());
        interfacesRequestDao.save(o);

        return ApiResponseResult.success("编辑成功").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult deleteRequest(Long id) throws Exception {
        if(id == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        InterfacesRequest o = interfacesRequestDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("请求参数信息不存在！");
        }

        o.setModifiedTime(new Date());
        o.setIsDel(1);
        interfacesRequestDao.save(o);

        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponseResult getRequestList(String keyword, Long interId, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("interId", SearchFilter.Operator.EQ, interId));
        //查询2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsType", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsDescpt", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsValue", SearchFilter.Operator.LIKE, keyword));
        }

        Specification<InterfacesRequest> spec = Specification.where(BaseService.and(filters, InterfacesRequest.class));
        Specification<InterfacesRequest> spec1 =  spec.and(BaseService.or(filters1, InterfacesRequest.class));
        Page<InterfacesRequest> page = interfacesRequestDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 根据ID获取请求参数
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getRequest(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        InterfacesRequest o = interfacesRequestDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("请求参数不存在！");
        }

        return ApiResponseResult.success().data(o);
    }

    /**
     * 批量删除请求参数
     * @param idsStr
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult deleteRequestAll(String idsStr) throws Exception{
        if(StringUtils.isEmpty(idsStr)){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        //转换数据
        long[] idsArray = null;
        try{
            String[] ids = idsStr.split(",");
            idsArray = Arrays.stream(ids).mapToLong(s -> Long.valueOf(s)).toArray();
        }catch (Exception e){
        }
        if(idsArray == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }

        //删除
        List<InterfacesRequest> list = interfacesRequestDao.findByIdIn(idsArray);
        if(list.size() > 0){
            for(InterfacesRequest item : list){
                if(item != null){
                    item.setModifiedTime(new Date());
                    item.setIsDel(1);
                }
            }
            interfacesRequestDao.saveAll(list);
        }

        return ApiResponseResult.success("删除成功！");
    }
}
