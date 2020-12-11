package com.web.produce.service.internal;

import java.sql.*;
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

    //获取合箱制令单
    public List getHXTaskNoPrc(String company,String facoty,String user_id, String taskNo,String keyword) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  prc_mes_cof_merge_task_chs (?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, taskNo);
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
    
    //获取返工料号-【创建在线返工制令单】
    public List getReworkItemPrc(String company,String facoty,String user_id, String type,String keyword,PageRequest pageRequst) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  prc_mes_cof_item_no_chs(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, type);
                cs.setString(5, keyword);
                cs.setInt(6, pageRequst.getPageSize());//每页指定有多少元素
                cs.setInt(7, pageRequst.getPageNumber()+1);//获取当前页码
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
                result.add(cs.getInt(10));
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


    public List getEmpChangePrc(String company,String facoty,String user_id, String beginDate,
                               String endDate,String keyword,PageRequest pageRequst) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  PRC_MES_GET_TASK_NO_EMP_CHANGE(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, beginDate);
                cs.setString(5, endDate);
                cs.setString(6, keyword);
                cs.setInt(7, pageRequst.getPageSize());//每页指定有多少元素
                cs.setInt(8, pageRequst.getPageNumber()+1);//获取当前页码
                cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(12, -10);// 输出参数 追溯数据
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(9));
                result.add(cs.getString(10));
                result.add(cs.getInt(11));
                if (cs.getString(9).toString().equals("0")) {
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

    // 创建在线返工制令单-获取组长数据
    public List getLinerPrc(String company,String facoty) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call prc_mes_cof_org_chs(?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setInt(3, 0);
                cs.setString(4, "组长");
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
    
    // 输入条码执行过程带出数量
    public List getInfoBarcodePrc(String company,String facoty,String taskNo,String barcode,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, taskNo);
                cs.setString(4, barcode);
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

    //prc_mes_cof_bar_s_join 传参带出条码样例
    public List getFsamplePrc(String company,String facoty,String userId,
                              String fixValue, String fyear , String fmonth,String fday,String serialNum,
                              String serialLen,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, userId);
                cs.setString(4, fixValue);
                cs.setString(5,fyear);
                cs.setString(6, fmonth);//每页指定有多少元素
                cs.setString(7, fday);//获取当前页码
                cs.setString(8, serialNum);//获取当前页码
                cs.setString(9, serialLen);//获取当前页码
                cs.registerOutParameter(10, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 追溯数据
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(10));
                result.add(cs.getString(11));
                result.add(cs.getString(12));
                return result;
            }

        });
        return resultList;
    }

    //小码校验规则编辑选择客户
    //prc_mes_cof_customer_chs
    public List getCustomerProc(String company,String facoty,String userId,String type,
                                String keyword, PageRequest pageRequst,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, userId);
                cs.setString(4, type);
                cs.setString(5,keyword);
                cs.setInt(6, pageRequst.getPageSize());//每页指定有多少元素
                cs.setInt(7, pageRequst.getPageNumber()+1);//获取当前页码
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
                result.add(cs.getInt(10));
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

    //小码校验规则 年、月、日、流水号下拉
    public List getBarListPrc(String company,String facoty,String userId,String type,String keyword,
                              PageRequest pageRequest,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, userId);
                cs.setString(4, type);
                cs.setString(5, keyword);
                cs.setInt(6, pageRequest.getPageSize());//每页指定有多少元素
                cs.setInt(7, pageRequest.getPageNumber()+1);//获取当前页码
                cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.INTEGER);// 输出参数 返回总数
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
                result.add(cs.getInt(10));
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


    // 创建在线返工制令单 输入条码带出返工料号和名称
    public List getItemByBarcodePrc(String company,String facoty,String userId,Integer type,String barcode,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, userId);
                cs.setInt(4, type);
                cs.setString(5, barcode);
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

    // 确定投入
    public List addPutPrc(String company,String facoty,String barcode,String task_no,String item_no,String qty,String user_code,
                          String feedType,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call " + prc_name + "(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, task_no);
                cs.setString(4, barcode);
                cs.setInt(5, Integer.parseInt(qty));
                cs.setString(6, item_no);
                cs.setString(7, user_code);
                cs.setString(8, feedType);
                cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(12, -10);// 输出参数 追溯数据
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
                if (cs.getString(9).toString().equals("0")) {
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
    public List getUserByLinePrc(String company,String facoty,String user_id, String line_id,int page,int rows) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  prc_mes_cof_affirm_emp_chs (?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, user_id);
                cs.setString(4, line_id);
                cs.setInt(5, rows);
                cs.setInt(6, page);
                cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识 -总记录数
                cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(10, -10);// 输出参数 追溯数据
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
                    result.add(cs.getString(7));
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
    //在线人员调整-获取待调整人员清单
    public List getEmpByTaskNoPrc(String company,String facoty,String user_id,
    		String aff_id,int size, int pageNumber)throws Exception{
    	List resultList=(List)jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_TASK_NO_EMP (?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, aff_id);
                cs.setInt(5, size);
                cs.setInt(6, pageNumber+1);
                cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(10, -10);// 
				return cs;
			}
		},new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs)throws SQLException,DataAccessException{
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(8));
				result.add(cs.getString(9));
				if (cs.getString(8).toString().equals("0")) {
					result.add(cs.getString(7));
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
				return result;
			}
		});
    	return resultList;
    }
    //人员调整-执行操作
    public List doTaskNoSwitchPrc(String facoty,String company,String user_id,
    		String lastTaskNo_id,String lastDatetimeEnd,
			String newTaskNo, String newLineId,String newHourType, String newClassId,
			String newDatetimeBegin, String empList,String switchType,PageRequest pageRequest)throws Exception{
    	List resultList=(List)jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_TASK_NO_CHANGE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, lastTaskNo_id);
                cs.setString(5, lastDatetimeEnd);
                cs.setString(6, newTaskNo);
                cs.setString(7, newLineId);
                cs.setString(8, newHourType);
                cs.setString(9, newClassId);
                cs.setString(10, newDatetimeBegin);
                cs.setString(11, empList);       
                cs.setString(12, switchType); 
                cs.setInt(13, pageRequest.getPageSize());
                cs.setInt(14, pageRequest.getPageNumber()+1);
                cs.registerOutParameter(15, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(16, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(17, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(18, -10);// 
				return cs;
			}
		},new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs)throws SQLException,DataAccessException{
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(16));
				result.add(cs.getString(17));
				if (cs.getString(16).toString().equals("0")) {
					result.add(cs.getString(15));
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(18);
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
    
    
    //创建在线返工制令单
    public List getCreateReturnPrc(String company,String facoty,String user_id, String task_no,String item_no,String liner_name,int qty,String pdate,String deptId,String classId) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  PRC_MES_TASK_OLN_CREATE (?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, user_id);
                cs.setString(4, "");//task_no--废除2020-11-03
                cs.setString(5, item_no);
                cs.setString(6, liner_name);
                cs.setInt(7, qty);
                cs.setString(8, pdate);
                cs.setString(9, deptId);
                cs.setString(10, classId);
                cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识

                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(11));
                result.add(cs.getString(12));
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

    //生产投料、投料检验的历史查询
    public List getInputHistoryPrc(String company,String facoty,String feedType,String user_id,
                              String hStartTime,String hEndTime,String keyword,
                              int page,int rows,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, feedType);
                cs.setString(4, hStartTime);
                cs.setString(5, hEndTime);
                cs.setString(6, keyword);
                cs.setInt(7, rows);
                cs.setInt(8, page);
                cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(11, java.sql.Types.INTEGER);// 总记录数
                cs.registerOutParameter(12, -10);// 输出参数 追溯数据
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
                    result.add(cs.getString(11));
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


    //来料不良历史查询
    public List getMaterialHistoryPrc(String company,String facoty, String hStartTime,String hEndTime,
                                      String keyword, int page,int rows,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, hStartTime);
                cs.setString(4, hEndTime);
                cs.setString(5, keyword);
                cs.setInt(6, rows);
                cs.setInt(7, page);
                cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.INTEGER);// 总记录数
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


    //创建在线返工制令单
    public List getHistoryPrc(String company,String facoty,String user_id,
                              String hStartTime,String hEndTime,String keyword,
                              int page,int rows,String prc_name) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, facoty);
                cs.setString(3, hStartTime);
                cs.setString(4, hEndTime);
                cs.setString(5, keyword);
                cs.setInt(6, rows);
                cs.setInt(7, page);
                cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.INTEGER);// 总记录数
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

    //获取排产导入临时列表
    public List getSchedulingTempPrc(String facoty, String company, String user_id, String startTime, String endTime, String keyword,
                                     int page, int rows, String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, startTime);
                cs.setString(4, endTime);
                cs.setString(5, keyword);
                cs.setInt(6, rows);
                cs.setInt(7, page);
                cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.INTEGER);// 输出参数 总记录数
                cs.registerOutParameter(11, -10);// 输出参数 返回数据集合
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

    //排产导入校验
    public List doCheckProc(String user_id, String ids, String prc_name) throws Exception{
        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call "+prc_name+"(?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, user_id);
                cs.setString(2, ids);
                cs.registerOutParameter(3,Types.INTEGER);// 注册输出参数 返回标志
                cs.registerOutParameter(4,java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                cs.registerOutParameter(5,-10);// 注册输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(3));
                result.add(cs.getString(4));
                result.add(cs.getString(5));
                return result;
            }
        });

        return resultList;
    }

    //排产导入写入临时表  ？ 正式表
    public List doSaveSchedulingProc(String company, String factory,String ids, String user_id, String prc_name){
        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call "+prc_name+"(?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, factory);
                cs.setString(3, ids);
                cs.setString(4, user_id);
                cs.registerOutParameter(5, Types.INTEGER);// 注册输出参数 返回标志
                cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                cs.registerOutParameter(7, -10);// 注册输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(5));
                result.add(cs.getString(6));
                result.add(cs.getString(7));
                return result;
            }
        });

        return resultList;
    }

    //获取部门信息  prc_mes_cof_org_chs
    public List getDeptPrc(String facoty, String company, String mid, String keyword,
                              String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, mid);
                cs.setString(4, keyword);
                cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
//                cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 总记录数
                cs.registerOutParameter(7, -10);// 输出参数 返回数据集合
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
                    result.add(cs.getString(7));
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

//    prc_mes_material_ng_save
        public List saveMaterialPrc(String company, String factory,String userId,String barcode,String itemNo,Integer deptId,
                                 Integer venderId,String prodDate,String lotNo,String defectCode,String defectQty)
                throws Exception {
            List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String storedProc = "{call  prc_mes_material_ng_save(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, factory);
                    cs.setString(2, company);
                    cs.setString(3, userId);
                    cs.setString(4, barcode);
                    cs.setString(5, itemNo);
                    cs.setInt(6, deptId);
                    cs.setInt(7, venderId);
                    cs.setString(8, prodDate);
                    cs.setString(9, lotNo);
                    cs.setString(10, defectCode);
                    cs.setString(11, defectQty);
                    cs.registerOutParameter(12, java.sql.Types.INTEGER);// 输出参数 返回标识
                    cs.registerOutParameter(13, java.sql.Types.VARCHAR);// 输出参数 返回标识
                    cs.registerOutParameter(14, -10);// 输出参数 返回标识
                    return cs;
                }
            }, new CallableStatementCallback() {
                public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    List<Object> result = new ArrayList<>();
                    List<Map<String, Object>> l = new ArrayList();
                    cs.execute();
                    result.add(cs.getInt(12));
                    result.add(cs.getString(13));
                    if (cs.getString(12).toString().equals("0")) {
                        // 游标处理
                        ResultSet rs = (ResultSet) cs.getObject(14);
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

    //获取不良内容下拉列表
    public List getBadSelectPrc(String company, String factory,String keyword)
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

    //供应商选择
    public List getSupplierPrc(String facoty, String company,Integer type,String keyword,PageRequest pageRequest) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  prc_mes_cof_vender_chs(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setInt(3, type);
                cs.setString(4, keyword);
                cs.setInt(5, pageRequest.getPageSize());
                cs.setInt(6,pageRequest.getPageNumber()+1);
                cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(8, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(9, java.sql.Types.INTEGER);// 总记录
                cs.registerOutParameter(10, -10);// 输出参数 返回数据集合
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
                    result.add(cs.getInt(9));
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
    
  //生产异常-获取异常原因
    public List getErrorInfoPrc(String facoty, String company, String uid) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  prc_mes_base_prod_err_chs(?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, uid);
                cs.registerOutParameter(4, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(5, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(6, -10);// 输出参数 返回数据集合
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
}
