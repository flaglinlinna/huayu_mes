package com.web.produce.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.UserUtil;
import com.web.produce.dao.AbnormalProductDao;
import com.web.produce.entity.AbnormalHours;
import com.web.produce.entity.AbnormalProduct;
import com.web.produce.service.AbnormalProductService;

/**
 * 生产异常信息
 *
 */
@Service(value = "AbnormalProductService")
@Transactional(propagation = Propagation.REQUIRED)
public class Abnormalproductlmpl extends PrcUtils implements AbnormalProductService {
	@Autowired
	AbnormalProductDao abnormalProductDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取指令单
	 */
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", 7, UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	/**
	 * 获取异常原因
	 */
	public ApiResponseResult getErrorInfo() throws Exception {
		List<Object> list = getErrorInfoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "");
		if (!list.get(0).toString().equals("0")) {
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}


	/**
	 * 获取制令单信息
	 */
	public ApiResponseResult getTaskNoInfo(String taskNo) throws Exception {
		List<Object> list = getTaskNoInfoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "", taskNo);
		if (!list.get(0).toString().equals("0")) {
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	// 获取制令单信息
	public List getTaskNoInfoPrc(String company, String facoty, String user_id, String taskNo) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_task_no_chs(?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, taskNo);
				cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(7, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(5));
				result.add(cs.getString(6));
				if (cs.getString(5).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(7);
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

	private List<Map<String, Object>> fitMap(ResultSet rs) throws Exception {
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

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		
		List<Object> list = getListPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getId() + "","",keyword, pageRequest.getPageNumber()+1,
				pageRequest.getPageSize());//getNumber() 获取当前页码,getSize() 每页指定有多少元素
		if (!list.get(0).toString().equals("0")) {
			return ApiResponseResult.failure(list.get(1).toString());
		}
		List<Map<String, Object>> l = (List<Map<String, Object>>) list.get(3);
		return ApiResponseResult.success().data(DataGrid.create(l, (int) list.get(2),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	public List getListPrc(String company, String facoty, String user_id,String task_no, String keyword,
			int pageNumber,int pageSize) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_get_prod_order_err(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, task_no);
				cs.setString(5, keyword);//--条件
				cs.setInt(6, pageSize);// --每页记录数
				cs.setInt(7, pageNumber);//--页码
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 --总记录数
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(11, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(9));
				result.add(cs.getString(10));
				if (cs.getString(9).toString().equals("0")) {
					result.add(cs.getInt(8));
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
	 * 新增异常工时记录
	 */
	@Override
	@Transactional
	public ApiResponseResult add(AbnormalProduct abnormalProduct) throws Exception {
		if (abnormalProduct == null) {
			return ApiResponseResult.failure("生产异常不能为空！");
		}
		abnormalProduct.setCreateDate(new Date());
		abnormalProduct.setCreateBy(UserUtil.getSessionUser().getId());
		abnormalProduct.setDelFlag(0);
		abnormalProductDao.save(abnormalProduct);

		return ApiResponseResult.success("生产异常记录添加成功！").data(abnormalProduct);
	}

	/**
	 * 根据ID获取
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getAbnormalHours(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("异常工时记录ID不能为空！");
		}
		AbnormalProduct  o = abnormalProductDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该异常工时记录不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除生产异常工时
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(String id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("生产异常工时ID不能为空！");
		}
		AbnormalProduct o = abnormalProductDao.findById(Long.parseLong(id));
		if (o == null) {
			return ApiResponseResult.failure("生产异常工时不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		abnormalProductDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	@Override
	@Transactional
	public ApiResponseResult edit(AbnormalProduct abnormalProduct) throws Exception {
		AbnormalProduct o = abnormalProductDao.findById((long) abnormalProduct.getId());
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setTaskNo(abnormalProduct.getTaskNo());
		o.setFtime(abnormalProduct.getFtime());
		o.setDescription(abnormalProduct.getDescription());
		o.setForReason(abnormalProduct.getForReason());
		abnormalProductDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}
}