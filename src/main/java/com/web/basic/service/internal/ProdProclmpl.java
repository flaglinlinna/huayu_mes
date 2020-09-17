package com.web.basic.service.internal;

import java.util.ArrayList;
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
import com.mysql.fabric.xmlrpc.base.Array;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.MtrialDao;
import com.web.basic.dao.ProdProcDao;
import com.web.basic.dao.ProdProcDetailDao;
import com.web.basic.dao.WoProcDao;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.ProdProc;
import com.web.basic.entity.ProdProcDetail;
import com.web.basic.entity.WoProc;
import com.web.basic.service.ProdProcService;

/**
 * 产品工艺流程表
 *
 */
@Service(value = "prodProcService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProdProclmpl implements ProdProcService {
	@Autowired
    private ProdProcDao prodProcDao;
	@Autowired
    private ProdProcDetailDao prodProcDetailDao;
	@Autowired
	private MtrialDao mtrialDao;
	@Autowired
	private WoProcDao woProcDao;
	
	/*
	 *获取主表数据 
	 */
	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception{
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		/*if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
		}
		*/
		Specification<ProdProc> spec = Specification.where(BaseService.and(filters, ProdProc.class));
		Specification<ProdProc> spec1 = spec.and(BaseService.or(filters1, ProdProc.class));
		
		Page<ProdProc> page = prodProcDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		
	}
	/*
	 *获取主表字段ID
	 */
	@Override
    @Transactional
    public ApiResponseResult getProdProc(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("ID不能为空！");
        }
        ProdProc o = prodProcDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该内容不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
	
	/*
	 *获取附表数据 
	 */
	@Override
    @Transactional
	public ApiResponseResult getDetailList(String keyword, PageRequest pageRequest) throws Exception{
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		/*if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
		}
		*/
		Specification<ProdProcDetail> spec = Specification.where(BaseService.and(filters, ProdProcDetail.class));
		Specification<ProdProcDetail> spec1 = spec.and(BaseService.or(filters1, ProdProcDetail.class));
		
		Page<ProdProcDetail> page = prodProcDetailDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	/*
	 *获取物料、工序数据 
	 */
	public ApiResponseResult getData()throws Exception{
		Map<String, Object> map = new HashMap<>();
		List<Mtrial> list1 = mtrialDao.findByIsDel(0);
		List<WoProc> list2 = woProcDao.findByIsDel(0);
		map.put("Mtrial", list1);
		map.put("WoProc", list2);
		
		return ApiResponseResult.success().data(map);
		
	}

}
