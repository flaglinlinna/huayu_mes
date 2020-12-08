package com.web.basePrice.service.internal;

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

public class BasePriceUtils {
	@Autowired
    private JdbcTemplate jdbcTemplate;

	/**
	 * 获取报价-工序信息[未测试]
	 * */
	public List getBJProcPrc(String facoty,String company,String user_id, String type,
			String condition, PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_BJ_GET_BASE_PROC (?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, type);
				cs.setString(5, condition);
				cs.setInt(6, pageRequest.getPageSize());
				cs.setInt(7, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.INTEGER);// 输出参数 返回标识
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
					result.add(cs.getString(10));
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
	 * 获取系统参数子表[未测试]
	 * */
	public List getSystemSubParamPrc(String company,String facoty,String user_id,
			String condition, PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_SYS_GET_parameter_SUB (?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, condition);
				cs.setInt(5, pageRequest.getPageSize());
				cs.setInt(6, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(8, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, -10);// 输出参数 追溯数据
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
					result.add(cs.getString(9));
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(10);
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
	
		
	//-------------------------------------	
		
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
