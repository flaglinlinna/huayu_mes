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
import com.web.basePrice.dao.MjProcFeeDao;
import com.web.basePrice.dao.MjProcFeeDao;
import com.web.basePrice.entity.MjProcFee;
import com.web.basePrice.service.MjProcFeeService;

/**
 *
 * @date Dec 21, 2020 4:27:53 PM
 */
@Service(value = "MjProcFeeService")
@Transactional(propagation = Propagation.REQUIRED)
public class MjProcFeeImpl implements MjProcFeeService {
    @Autowired
    private MjProcFeeDao mjProcFeeDao;

    @Autowired
    private SysUserDao sysUserDao;
   
    
    @Override
    @Transactional
    public ApiResponseResult add(MjProcFee mjProcFee) throws Exception{
    if(mjProcFee == null){
    	return ApiResponseResult.failure("模具成本信息不可为空");
    }
    
   //生成模具编号
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String dateStr = sdf.format(new Date());
    mjProcFee.setProductCode("MJ-" + dateStr);  //编号格式：MJ-年月日时分秒
    
    mjProcFee.setCreateDate(new Date());
    mjProcFee.setCreateBy(UserUtil.getSessionUser().getId());
    mjProcFeeDao.save(mjProcFee);
    return ApiResponseResult.success("添加模具成本信息成功").data(mjProcFee);
    }

    /**
    * 修改
    */
    @Override
    @Transactional
    public ApiResponseResult edit(MjProcFee mjProcFee) throws Exception {
    if(mjProcFee == null){
    	return ApiResponseResult.failure("模具成本信息不可为空");
      }
    if(mjProcFee.getId() == null){
    	return ApiResponseResult.failure("模具成本信息ID不可为空");
    }
    MjProcFee o= mjProcFeeDao.findById((long)mjProcFee.getId());
    o.setLastupdateDate(new Date());
    o.setLastupdateBy(UserUtil.getSessionUser().getId());
    
    o.setFimg(mjProcFee.getFimg());//图示
	o.setProductName(mjProcFee.getProductName());//产品名称
	o.setStructureMj(mjProcFee.getStructureMj());//模具结构
	o.setMjPrice(mjProcFee.getMjPrice());// mo ju jia ge
	o.setNumHole(mjProcFee.getNumHole());//穴数
	o.setFeeProc(mjProcFee.getFeeProc());//工序费用（元/小时）
	o.setFeeType1(mjProcFee.getFeeType1());//
	o.setFeeType2(mjProcFee.getFeeType2());//
	o.setFeeType3(mjProcFee.getFeeType3());//
	o.setFeeType4(mjProcFee.getFeeType4());//
	o.setFeeAll(mjProcFee.getFeeAll());//评估总费用（含税）
    
    mjProcFeeDao.save(o);
    return ApiResponseResult.success("编辑成功！");
}

    /**
    * 删除
    */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
    if(id == null){
    	return ApiResponseResult.failure("模具成本信息id不可为空");
    }
    MjProcFee o  = mjProcFeeDao.findById((long) id);
    if(o == null){
    	return ApiResponseResult.failure("模具成本信息不存在");
    }
    o.setDelFlag(1);
    mjProcFeeDao.save(o);
    return ApiResponseResult.success("删除成功！");
    }

    /**
    * 查询
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
            filters1.add(new SearchFilter("productName", SearchFilter.Operator.LIKE, keyword));
            }
            Specification<MjProcFee> spec = Specification.where(BaseService.and(filters, MjProcFee.class));
            Specification<MjProcFee> spec1 = spec.and(BaseService.or(filters1, MjProcFee.class));
            Page<MjProcFee> page = mjProcFeeDao.findAll(spec1, pageRequest);
            List<MjProcFee> mjProcFeeList = page.getContent();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String,Object>> mapList = new ArrayList<>();
            for(MjProcFee mjProcFee:mjProcFeeList){
                Map<String,Object> map = new HashMap<>();
                map.put("id",mjProcFee.getId());
                map.put("productCode",mjProcFee.getProductCode());
                map.put("fimg",mjProcFee.getFimg());
                map.put("productName",mjProcFee.getProductName());
                map.put("structureMj",mjProcFee.getStructureMj());
                map.put("numHole",mjProcFee.getNumHole());
                map.put("feeProc",mjProcFee.getFeeProc());
                map.put("mjPrice",mjProcFee.getMjPrice());
                
                map.put("feeType1",mjProcFee.getFeeType1());
                map.put("feeType2",mjProcFee.getFeeType2());
                map.put("feeType3",mjProcFee.getFeeType3());
                map.put("feeType4",mjProcFee.getFeeType4());
                
                map.put("feeAll",mjProcFee.getFeeAll());
                map.put("fmemo",mjProcFee.getFmemo());
                map.put("createBy",sysUserDao.findById((long)mjProcFee.getCreateBy()).getUserName());
                map.put("createDate",df.format(mjProcFee.getCreateDate()));
                if(mjProcFee.getLastupdateBy()!=null){
                    map.put("lastupdateBy",sysUserDao.findById((long)mjProcFee.getCreateBy()).getUserName());
                    map.put("lastupdateDate",df.format(mjProcFee.getLastupdateDate()));
                }
                mapList.add(map);
            }
            return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
            pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
        }
}