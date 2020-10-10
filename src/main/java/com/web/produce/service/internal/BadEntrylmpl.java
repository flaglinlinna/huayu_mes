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
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.produce.service.BadEntryService;

/**
 * 在线不良录入
 *
 */
@Service(value = "BadEntryService")
@Transactional(propagation = Propagation.REQUIRED)
public class BadEntrylmpl extends PrcUtils implements BadEntryService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 获取指令单信息
	@Override
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", 6, UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	// 获取不良信息
	@Override
	public ApiResponseResult getBadInfo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getBadInfoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	public List getBadInfoPrc(String company, String facoty, String user_id, String keyword) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_QC_NG_TXT_GET(?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
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
				System.out.println(l);
				return result;
			}
		});
		return resultList;
	}

	@Override
	public ApiResponseResult checkBarCode(String taskNo, String barCode) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = checkBarCodePrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "", taskNo, barCode);
		
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败
			return ApiResponseResult.failure(list.get(1).toString());
		}
		List<Map<String, Object>> l_last = new ArrayList<Map<String, Object>>();
		Map<String, Object> m_new = new HashMap<String, Object>();
		if (list.get(2) == null) {
			m_new.put("qty", "");
		} else {
			m_new.put("qty", list.get(2).toString());
		}
		l_last.add(m_new);
		
		return ApiResponseResult.success().data(l_last);
	}

	public List checkBarCodePrc(String company, String facoty, String user_id, String taskNo, String barCode)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_QC_NG_BARCODE_CHK(?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, taskNo);
				cs.setString(5, barCode);
				cs.registerOutParameter(6, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(7, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				cs.execute();
				result.add(cs.getInt(6));
				result.add(cs.getString(7));
				if (cs.getString(6).toString().equals("0")) {
					result.add(cs.getInt(8));
				}
				return result;
			}
		});
		return resultList;
	}

	// 保存不良
	public ApiResponseResult saveBad(String taskNo, String barCode, int qty, String defCode, String memo)
			throws Exception {
		List<Object> list = saveBadPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "", taskNo, barCode,
				qty, defCode, memo);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	public List saveBadPrc(String company, String facoty, String user_id, String taskNo, String barCode, int qty,
			String defCode, String memo) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_QC_NG_TXT_IMP(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, taskNo);
				cs.setString(4, barCode);
				cs.setInt(5, qty);
				cs.setString(6, defCode);
				cs.setString(7, memo);
				cs.setString(8, user_id);
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
	
	public ApiResponseResult deleteBad(String recordId) throws  Exception{
		List<Object> list = deleteBadPrc(UserUtil.getSessionUser().getId() + "",recordId );
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	public List deleteBadPrc( String user_id, String recordId) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_QC_NG_TXT_DEL(?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, recordId);
				cs.setString(2, user_id);
				cs.registerOutParameter(3, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(4, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(5, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(3));
				result.add(cs.getString(4));
				if (cs.getString(3).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(5);

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
	
	
	
	//游标整理
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
}
