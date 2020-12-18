package com.web.basic.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.ItemsTimeDao;
import com.web.basic.entity.ItemsTime;
import com.web.basic.service.ItemTimeService;
import com.web.produce.service.internal.PrcUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service(value = "ItemTimeService")
@Transactional(propagation = Propagation.REQUIRED)
public class ItemsTimelmpl extends PrcUtils implements ItemTimeService {
	@Autowired
    private ItemsTimeDao itemsTimeDao;

	 /**
     * 新增不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult add(ItemsTime itemsTime) throws Exception{
        if(itemsTime == null){
            return ApiResponseResult.failure("静默时间ID不能为空！");
        }
        int count = itemsTimeDao.countByDelFlagAndItemNo(0, itemsTime.getItemNo());
        if(count > 0){
            return ApiResponseResult.failure("该物料编码已存在，请填写其他物料编码！");
        }
        itemsTime.setCreateDate(new Date());
        itemsTime.setCreateBy(UserUtil.getSessionUser().getId());
        itemsTimeDao.save(itemsTime);
        return ApiResponseResult.success("添加成功！").data(itemsTime);
    }
    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ItemsTime itemsTime) throws Exception {
        if(itemsTime == null){
            return ApiResponseResult.failure("静默时间不能为空！");
        }
        if(itemsTime.getId() == null){
            return ApiResponseResult.failure("静默时间ID不能为空！");
        }
        if(StringUtils.isEmpty(itemsTime.getItemNo())){
            return ApiResponseResult.failure("物料编码不能为空！");
        }

        ItemsTime o = itemsTimeDao.findById((long) itemsTime.getId());
        if(o == null){
            return ApiResponseResult.failure("该静默时间信息不存在！");
        }
        //判断异常类别代码是否有变化，有则修改；没有则不修改
        if(o.getItemNo().equals(itemsTime.getItemNo())){
        }else{
            int count = itemsTimeDao.countByDelFlagAndItemNo(0, itemsTime.getItemNo());
            if(count > 0){
                return ApiResponseResult.failure("物料编码已存在，请填写其他物料编码信息！");
            }
            o.setItemNo(itemsTime.getItemNo().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setItemTime(itemsTime.getItemTime());
        itemsTimeDao.save(o);
        return ApiResponseResult.success("编辑成功！");
	}


    /**
     * 删除异常类别
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("ID不能为空！");
        }
        ItemsTime o  = itemsTimeDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("静默时间信息不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        itemsTimeDao.save(o);
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
					filters1.add(new SearchFilter("itemNo", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<ItemsTime> spec = Specification.where(BaseService.and(filters, ItemsTime.class));
				Specification<ItemsTime> spec1 = spec.and(BaseService.or(filters1, ItemsTime.class));
				Page<ItemsTime> page = itemsTimeDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

    @Override
    public ApiResponseResult getItemSelect(String keyword,PageRequest pageRequest) throws Exception{
        List<Object> list = getReworkItemPrc(UserUtil.getSessionUser().getCompany()+"",
                UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","半成品",keyword,pageRequest);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);

    }
}
