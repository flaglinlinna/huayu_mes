package com.web.kanban.service.internal;

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

public class PrcKanbanUtils {
	@Autowired
    private JdbcTemplate jdbcTemplate;

	// 获取车间报工看板信息
	public List getCjbgListPrc(String company,String facoty,String user_id, String class_id,
			String dep_id, String sdata, String edata,String dev_ip) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_rpt_cjbg (?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, "");//company
				cs.setString(2, "");//facoty
				cs.setString(3, "");//class_id
				cs.setString(4, "");//dep_id
				cs.setString(5, "");//sdata
				cs.setString(6, "1");//dev_ip
				cs.setString(7, user_id);
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(10, -10);// 输出参数 追溯数据
				cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(13, java.sql.Types.INTEGER);// 输出参数 返回标识
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
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(10);

					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
					
					result.add(cs.getString(11));
					result.add(cs.getString(12));
					result.add(cs.getString(13));
				}
				System.out.println(l);
				return result;
			}

		});
		return resultList;
	}
	
	// 获取生产电子看板信息
		public List getScdzListPrc(String company,String facoty,String user_id, String class_id,
				String dep_id, String sdata, String edata,String dev_ip) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  PRC_MES_RPT_SCDZ (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, "");//company
					cs.setString(2, "");//facoty
					cs.setString(3, "");//class_id
					cs.setString(4, "");//dep_id
					cs.setString(5, "");//sdata
					cs.setString(6, "2");//dev_ip
					cs.setString(7, user_id);//
					cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(10, -10);// 输出参数 追溯数据
					cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(13, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(14, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(15, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(16, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(17, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(18, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(19, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(20, java.sql.Types.INTEGER);// 输出参数 返回标识
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
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(10);

						try {
							l = fitMap(rs);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result.add(l);
						
						result.add(cs.getString(11));
						result.add(cs.getString(12));
						result.add(cs.getString(13));
						result.add(cs.getString(14));
						result.add(cs.getString(15));
						result.add(cs.getString(16));
						result.add(cs.getString(17));
						result.add(cs.getString(18));
						result.add(cs.getString(19));
						result.add(cs.getString(20));
					}
					System.out.println(l);
					return result;
				}

			});
			return resultList;
		}

		// 获取车间报工看板信息
		public List getZcblListPrc() throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  prc_mes_rpt_zcbl (?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, "");//facoty
					cs.setString(2, "");//company
					cs.setString(3, "3");//唯一硬件标识
					cs.setString(4, "5602");//user_id
					cs.setString(5, "1");//班次ID
					cs.setString(6, "2020/10/31");//日期
					cs.setString(7, "5253");//部门ID
					cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(10, -10);// 输出参数 追溯数据
					cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(13, java.sql.Types.VARCHAR);// 输出参数 返回标识
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
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(10);

						try {
							l = fitMap(rs);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result.add(l);
						result.add(cs.getString(11));
						result.add(cs.getString(12));
						result.add(cs.getString(13));
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
	
	
	
				
}
