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

public class PrcUtils {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	// 获取指令单
	public List getTaskNoPrc(String company,String facoty,int type,String user_id, String keyword) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_task_no_chs (?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, user_id);
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
				System.out.println(l);
				return result;
			}

		});
		return resultList;
	}
	
	// 输入条码执行过程带出数量
	public List getInfoBarcodePrc(String company,String facoty,String barcode,String prc_name) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call " + prc_name + "(?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, barcode);
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
				System.out.println(l);
				return result;
			}

		});
		return resultList;
	}
	
	// 确定投入
	public List addPutPrc(String company,String facoty,String barcode,String task_no,String item_no,String qty,String user_code,String prc_name) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, task_no);
				cs.setString(4, barcode);
				cs.setInt(5, Integer.parseInt(qty));
				cs.setString(6, item_no);
				cs.setString(7, user_code);
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
				result.add(cs.getString(10));
				if (cs.getString(8).toString().equals("0")) {
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
	
	// 删除生产投料
	public List deletePrc(String company,String facoty,String id,Long user_id,String prc_name) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, id);
				cs.setString(4, user_id+"");
				cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识
				//cs.registerOutParameter(7, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(5));
				result.add(cs.getString(6));
				result.add(cs.getInt(7));
				System.out.println(l);
				return result;
			}

		});
		return resultList;
	}
	
	// 确定投入
	public List afterNeiPrc(String company,String facoty,String barcode,String task_no) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call PRC_BOX_CHECK_BARCODE (?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, task_no);
				cs.setString(4, barcode);
				cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
				//cs.registerOutParameter(7, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(5));
				result.add(cs.getString(6));
				/*if (cs.getString(5).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(7);
					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
				}*/
				return result;
			}
		});
		return resultList;
	}
	
	//-生产报工
	public List afterWaiPrc(String company,String facoty,String task_no,String nbarcode,String wbarcode,String ptype,String hx,String user_id) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call PRC_BOX_IMP_BARCODE (?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, task_no);
				cs.setString(4, nbarcode);
				cs.setString(5, wbarcode);
				cs.setString(6, ptype);
				cs.setString(7, hx);
				cs.setString(8, user_id);
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(13, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(9));
				result.add(cs.getString(10));
				result.add(cs.getString(11));
				result.add(cs.getString(12));
				if (cs.getString(9).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(13);
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
	
	
	//根据指令单获取扫描数据
		public List getDetailByTaskPrc(String company,String facoty,int type,String user_id, String taskNo) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  prc_mes_task_info_get (?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, company);
					cs.setString(2, facoty);
					cs.setString(3, user_id);
					cs.setInt(4, type);
					cs.setString(5, taskNo);
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
					System.out.println(l);
					return result;
				}

			});
			return resultList;
		}
		
		//上线确认-获取产线
		public List getLinePrc(String company,String facoty,String user_id, String keyword) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  prc_mes_cof_line_chs (?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, company);
					cs.setString(2, facoty);
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
		
		//上线确认-获取产线未分配人员
		public List getUserByLinePrc(String company,String facoty,String user_id, String line_id) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  prc_mes_cof_affirm_emp_chs (?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, company);
					cs.setString(2, facoty);
					cs.setString(3, user_id);
					cs.setString(4, line_id);
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
		
		//上线确认-保存分配
		public List getEmpSavePrc(String company,String facoty,String user_id, String task_no,String line_id,
				String hour_type,String class_id,String wdate,String emp_ids) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  PRC_MES_COF_EMP_SAVE (?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, company);
					cs.setString(2, facoty);
					cs.setString(3, user_id);
					cs.setString(4, task_no);
					cs.setString(5, line_id);
					cs.setString(6, hour_type);
					cs.setString(7, class_id);
					cs.setString(8, wdate);
					cs.setString(9, emp_ids);
					cs.registerOutParameter(10, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(12, -10);// 输出参数 追溯数据
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					List<Object> result = new ArrayList<>();
					List<Map<String, Object>> l = new ArrayList();
					cs.execute();
					result.add(cs.getInt(10));
					result.add(cs.getString(11));
					if (cs.getString(10).toString().equals("0")) {
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(12);

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
	
	
	
				
}
