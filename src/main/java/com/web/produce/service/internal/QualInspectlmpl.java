package com.web.produce.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.utils.UserUtil;
import com.web.produce.service.QualInspectService;

/**
 * 品质检验
 *
 */
@Service(value = "QualInspectService")
@Transactional(propagation = Propagation.REQUIRED)
public class QualInspectlmpl extends PrcUtils implements QualInspectService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ApiResponseResult getProcList(String company,String factory,String userId,String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getProcListPrc(company,factory,userId,2, keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	public List getProcListPrc(String company, String factory,String userId, int type,
			String keyword)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_proc_chs(?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, factory);
				cs.setString(2, company);
				cs.setString(3, userId);
				cs.setInt(4, type);
				cs.setString(5, keyword);
				cs.registerOutParameter(6, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(7, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(8, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(6));
				result.add(cs.getString(7));
				if (cs.getString(6).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(8);

					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
				}
				return result;
			}
		});
		return resultList;
	}
	
	@Override
	public ApiResponseResult scanBarcode(String company,String factory,String user_id,
			String proc, String barcode) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = scanBarcodePrc(company,factory,user_id,proc, barcode);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	public List scanBarcodePrc(String company, String factory, String user_id,String proc,String barcode)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_qc_insp_bar_chk(?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, factory);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, proc);
				cs.setString(5, barcode);
				cs.setString(6, "");
				cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(8, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(9, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(7));
				result.add(cs.getString(8));
				if (cs.getString(7).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(9);

					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
				}
				return result;
			}
		});
		return resultList;
	}
	
	@Override
	public ApiResponseResult getDepatrList(String factory,String company,String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getDepatrListPrc(factory,company,keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	public List getDepatrListPrc( String factory,String company,String keyword)throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_org_chs(?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, factory);
				cs.setString(2, company);
				cs.setInt(3, 2);
				cs.setString(4, keyword);
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
				return result;
			}
		});
		return resultList;
	}
	
	@Override
	public ApiResponseResult getBadList(String company,String factory,String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getBadListPrc(company,factory,keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	public List getBadListPrc(String company, String factory,String keyword)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_defect_det_chs(?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, factory);
				cs.setString(2, company);
				cs.setString(3, keyword);
				cs.registerOutParameter(4, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(5, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(6, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(4));
				result.add(cs.getString(5));
				if (cs.getString(4).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(6);

					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
				}
				return result;
			}
		});
		return resultList;
	}
	
	@Override
	public ApiResponseResult saveData(String factory,String company,String user_id, String proc,String barcodeList,int checkTotal,int badTotal,
			String chkResult,String departCode,String badList) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = saveDataPrc(company,factory,user_id,proc, barcodeList, 
				checkTotal,badTotal,chkResult,departCode,badList);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success();
	}
	
	public List saveDataPrc(String company, String factory,String user_id,String proc,String barcodeList,
			int checkTotal,int badTotal,String chkResult,String departCode,String badList)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_qc_insp_save(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, factory);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, proc);
				cs.setString(5, barcodeList);
				cs.setInt(6, checkTotal);
				cs.setInt(7, badTotal);
				cs.setString(8, chkResult);
				cs.setString(9, departCode);
				cs.setString(10, badList);
				cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				cs.execute();
				result.add(cs.getInt(11));
				result.add(cs.getString(12));
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
	
	@Override
	public ApiResponseResult getHistoryList(String keyword, String hStartTime, String hEndTime, PageRequest pageRequest)
			throws Exception {
		// TODO Auto-generated method stub 因为是免登录的 所以无法取当前登录人信息
		List<Object> list = getHistoryPrc("","","",
				hStartTime,hEndTime,keyword,
				pageRequest.getPageNumber()+1,pageRequest.getPageSize(),"prc_mes_qc_insp_chs");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}
}
