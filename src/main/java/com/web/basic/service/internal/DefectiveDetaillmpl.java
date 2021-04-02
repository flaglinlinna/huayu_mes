package com.web.basic.service.internal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.web.basic.dao.DefectiveDao;
import com.web.basic.dao.DefectiveDetailDao;
import com.web.basic.entity.Defective;
import com.web.basic.entity.DefectiveDetail;
import com.web.basic.entity.Process;
import com.web.basic.service.DefectiveDetailService;


@Service(value = "defectiveDetailService")
@Transactional(propagation = Propagation.REQUIRED)
public class DefectiveDetaillmpl implements DefectiveDetailService {
	@Autowired
    private DefectiveDetailDao defectiveDetailDao;

	@Autowired
    private DefectiveDao defectiveDao;
	 /**
     * 新增不良内容
     */
    @Override
    @Transactional
    public ApiResponseResult add(DefectiveDetail defectiveDetail) throws Exception{
        if(defectiveDetail == null){
            return ApiResponseResult.failure("不良内容不能为空！");
        }
        if(StringUtils.isEmpty(defectiveDetail.getDefectCode())){
            return ApiResponseResult.failure("不良 编码不能为空！");
        }
        if(StringUtils.isEmpty(defectiveDetail.getDefectName())){
            return ApiResponseResult.failure("不良 名称不能为空！");
        }
        int count = defectiveDetailDao.countByDelFlagAndDefectCode(0, defectiveDetail.getDefectCode());
        if(count > 0){
            return ApiResponseResult.failure("该不良内容编号已存在，请填写其他不良内容编码！");
        }
        defectiveDetail.setCreateDate(new Date());
        defectiveDetail.setCreateBy(UserUtil.getSessionUser().getId());
        defectiveDetailDao.save(defectiveDetail);
        return ApiResponseResult.success("不良内容添加成功！").data(defectiveDetail);
    }
    /**
     * 修改不良内容
     */
    @Override
    @Transactional
    public ApiResponseResult edit(DefectiveDetail defectiveDetail) throws Exception {
        if(defectiveDetail == null){
            return ApiResponseResult.failure("不良内容不能为空！");
        }
        if(defectiveDetail.getId() == null){
            return ApiResponseResult.failure("不良内容ID不能为空！");
        }
        if(StringUtils.isEmpty(defectiveDetail.getDefectCode())){
            return ApiResponseResult.failure("不良编码不能为空！");
        }
        if(StringUtils.isEmpty(defectiveDetail.getDefectName())){
            return ApiResponseResult.failure("不良名称不能为空！");
        }
        DefectiveDetail o = defectiveDetailDao.findById((long) defectiveDetail.getId());
        if(o == null){
            return ApiResponseResult.failure("该不良内容不存在！");
        }
        //判断不良内容编码是否有变化，有则修改；没有则不修改
        if(o.getDefectCode().equals(defectiveDetail.getDefectCode())){
        }else{
            int count = defectiveDetailDao.countByDelFlagAndDefectCode(0, defectiveDetail.getDefectCode());
            if(count > 0){
                return ApiResponseResult.failure("不良内容编码已存在，请填写其他不良内容编码！");
            }
            o.setDefectCode(defectiveDetail.getDefectCode().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setDefectTypeId(defectiveDetail.getDefectTypeId());
        o.setDefectName(defectiveDetail.getDefectName());
        o.setDefectCode(defectiveDetail.getDefectCode());
        
        defectiveDetailDao.save(o);
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
    public ApiResponseResult getDefectiveDetail(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良内容ID不能为空！");
        }
        DefectiveDetail o = defectiveDetailDao.findById((long) id);
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
        DefectiveDetail o  = defectiveDetailDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("不良内容不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        defectiveDetailDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("不良内容ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        DefectiveDetail o = defectiveDetailDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("不良内容不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setCheckStatus(checkStatus);
        defectiveDetailDao.save(o);
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
					filters1.add(new SearchFilter("defectCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("defectName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("defective.defectTypeCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("defective.defectTypeName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<DefectiveDetail> spec = Specification.where(BaseService.and(filters, DefectiveDetail.class));
				Specification<DefectiveDetail> spec1 = spec.and(BaseService.or(filters1, DefectiveDetail.class));
				Page<DefectiveDetail> page = defectiveDetailDao.findAll(spec1, pageRequest);

				List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
				for(DefectiveDetail bs:page.getContent()){
					Map<String, Object> map = new HashMap<>();
					map.put("defectTypeCode",bs.getDefective().getDefectTypeCode());//获取关联表的数据
                    map.put("defectClass",bs.getDefective().getDefectClass());
					map.put("defectTypeName", bs.getDefective().getDefectTypeName());
					map.put("id", bs.getId());
					map.put("defectCode", bs.getDefectCode());
					map.put("defectName", bs.getDefectName());
					map.put("checkStatus", bs.getCheckStatus());
					map.put("lastupdateDate",bs.getLastupdateDate());
					map.put("createDate", bs.getCreateDate());
					list.add(map);
				}
				return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	/**
     * 获取不良类别
     */
	@Override
    @Transactional
	public ApiResponseResult getDefectiveList() throws Exception {
		List<Defective> list = defectiveDao.findByDelFlagAndCheckStatus(0,1);
		return ApiResponseResult.success().data(list);
	}
}
