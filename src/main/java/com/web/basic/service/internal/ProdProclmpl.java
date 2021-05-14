package com.web.basic.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
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
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	
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
		/*List<SearchFilter> filters = new ArrayList<>();
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
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));*/
		List<Object> list = getProdListPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId()+"",
				keyword, pageRequest.getPageSize(),pageRequest.getPageNumber()+1);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(DataGrid.create((List) list.get(3), (int) list.get(2),
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
				//--end
				int j=1;
				boolean isExists = false;
				for(String pro:procs){
					if(!StringUtils.isEmpty(pro)){
						String[] pros =  pro.split("\\@");
						Process process = processDao.findById(Long.parseLong(pros[0]));
						if(process.getProcNo().equals("A001")){
							if(("1").equals(pros[1])){
								isExists = true;
							}
						}
						ProdProcDetail pd = new ProdProcDetail();
						pd.setItemId(Long.valueOf(it));
						pd.setItemNo(itemNos[i]);
						pd.setProcNo(process.getProcNo());
						pd.setProcName(process.getProcName());
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
				if(!isExists){
					return ApiResponseResult.failure("必须存在包装报工及其过程属性!");
				}
				//20201102-fyx-先删除后在新增
				prodProcDetailDao.delteProdProcDetailByItemIdAnd(Long.parseLong(it));
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
        //20201114-fyx-判断顺序是否存在
        List<ProdProcDetail> lpd = prodProcDetailDao.findByDelFlagAndItemIdAndProcOrder(0, o.getItemId(), procOrder);
        if(lpd.size()>0){
        	 return ApiResponseResult.failure("工序序号重复,请重新填写！");
        }
        //---end
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setProcOrder(procOrder);
        prodProcDetailDao.save(o);
        return ApiResponseResult.success("修改成功！").data(o);
	}
	
	private List getProdListPrc(String facoty,String company,String user_id ,String  keyword,
			int page,int sizes) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_item_no_chs(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, "成品");
				cs.setString(5, keyword);
				cs.setInt(6, page);
				cs.setInt(7, sizes);
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.INTEGER);// 
				cs.registerOutParameter(11, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(8));
				result.add(cs.getString(9));
				if (cs.getString(8).toString().equals("0")) {
					result.add(cs.getInt(10));
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(11);
					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
				}
				System.out.println(l);
				return result;
			}
		});
		return resultList;
	}
	/**
	 * 游标处理
	 * */
	public List<Map<String, Object>> fitMap(ResultSet rs) throws Exception {
		List<Map<String, Object>> list = new ArrayList<>();
		if (null != rs) {
			Map<String, Object> map;
			int colNum = rs.getMetaData().getColumnCount();
			List<String> columnNames = new ArrayList<String>();
			for (int i = 1; i <= colNum; i++) {
				columnNames.add(rs.getMetaData().getColumnName(i));
			}
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (String columnName : columnNames) {
					map.put(columnName, rs.getString(columnName));
				}
				list.add(map);
			}
		}
		return list;
	}

}
