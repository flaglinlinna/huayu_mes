package com.web.basic.service.internal;

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
import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.fabric.xmlrpc.base.Data;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.ClientProcessMapDao;
import com.web.basic.dao.MtrialDao;
import com.web.basic.dao.ProdProcDetailDao;
import com.web.basic.dao.ProcessDao;
import com.web.basic.entity.ClientProcessMap;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.ProdProcDetail;
import com.web.basic.entity.Process;
import com.web.basic.service.ProdProcService;

/**
 * 产品工艺流程表
 *
 */
@Service(value = "prodProcService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProdProclmpl implements ProdProcService {

	@Autowired
    private ProdProcDetailDao prodProcDetailDao;
	@Autowired
	ClientProcessMapDao clientProcessMapDao;
	@Autowired
	private MtrialDao mtrialDao;
	@Autowired
	private ProcessDao processDao;
	@Override
	public ApiResponseResult getAddList() throws Exception {
		// TODO Auto-generated method stub
		Map map =  new HashMap();
		//map.put("Prod", mtrialDao.findProd());
		map.put("Client", clientProcessMapDao.findClient());
		map.put("Process", processDao.findByDelFlagAndCheckStatus(0, 1));
		return ApiResponseResult.success().data(map);
	}
	@Override
	public ApiResponseResult getProdList(String keyword, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("checkStatus", SearchFilter.Operator.EQ, 1));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("itemNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("itemName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("itemType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("itemModel", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("itemUnit", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Mtrial> spec = Specification.where(BaseService.and(filters, Mtrial.class));
		Specification<Mtrial> spec1 = spec.and(BaseService.or(filters1, Mtrial.class));
		Page<Mtrial> page = mtrialDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	@Override
	public ApiResponseResult add(String proc, String itemId, String itemNo) throws Exception {
		// TODO Auto-generated method stub
		String[] procs = proc.split("\\,");
		String[] itemIds = itemId.split("\\,");
		String[] itemNos = itemNo.split("\\,");
		List<ProdProcDetail> lp = new ArrayList<ProdProcDetail>();
		for(int i=0;i<itemIds.length;i++){
			String it = itemIds[i];
			if(!StringUtils.isEmpty(it)){
				//20201102-fyx-先删除后在新增
				prodProcDetailDao.delteProdProcDetailByItemIdAnd(Long.parseLong(it));
				//--end
				int j=1;
				for(String pro:procs){
					if(!StringUtils.isEmpty(pro)){
						String[] pros =  pro.split("\\@");
						Process process = processDao.findById(Long.parseLong(pros[0]));
						ProdProcDetail pd = new ProdProcDetail();
						pd.setItemId(Long.valueOf(it));
						pd.setItemNo(itemNos[i]);
						pd.setProcId(Long.valueOf(pros[0]));
						pd.setCreateBy(UserUtil.getSessionUser().getId());
						pd.setCreateDate(new Date());
						//pd.setProcOrder(process.getProcOrder());
						pd.setProcOrder((j)*10);
						pd.setJobAttr(Integer.valueOf(pros[1]));
						lp.add(pd);
						j++;
					}
				}
				
			}
			
		}
		prodProcDetailDao.saveAll(lp);
		return ApiResponseResult.success("新增成功!");
	}
	@Override
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("itemNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("mtrial.itemName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("mtrial.itemNameS", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("process.procName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("process.procNo", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<ProdProcDetail> spec = Specification.where(BaseService.and(filters, ProdProcDetail.class));
				Specification<ProdProcDetail> spec1 = spec.and(BaseService.or(filters1, ProdProcDetail.class));
				Page<ProdProcDetail> page = prodProcDetailDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	@Override
	public ApiResponseResult delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		 if(id == null){
	            return ApiResponseResult.failure("ID不能为空！");
	        }
		 ProdProcDetail o  = prodProcDetailDao.findById((long) id);
	        if(o == null){
	            return ApiResponseResult.failure("该记录不存在！");
	        }
	        o.setDelTime(new Date());
	        o.setDelFlag(1);
	        o.setDelBy(UserUtil.getSessionUser().getId());
	        prodProcDetailDao.save(o);
	        return ApiResponseResult.success("删除成功！");
	}
	@Override
	public ApiResponseResult doJobAttr(Long id, Integer jobAttr) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(jobAttr == null){
            return ApiResponseResult.failure("请正确设置过程属性！");
        }
        ProdProcDetail o = prodProcDetailDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序记录不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setJobAttr(jobAttr);
        prodProcDetailDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
	}
	@Override
	public ApiResponseResult doProcOrder(Long id, Integer procOrder) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(procOrder == null){
            return ApiResponseResult.failure("请填写正确的数字！");
        }
        ProdProcDetail o = prodProcDetailDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序记录不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setProcOrder(procOrder);
        prodProcDetailDao.save(o);
        return ApiResponseResult.success("修改成功！").data(o);
	}
	
	

}
