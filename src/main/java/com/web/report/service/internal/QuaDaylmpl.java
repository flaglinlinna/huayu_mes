package com.web.report.service.internal;

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
import com.web.report.service.QuaDayService;

/**
 * 品质检验日报表
 */
@Service(value = "QuaDayService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuaDaylmpl extends ReportPrcUtils implements QuaDayService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public ApiResponseResult getDeptInfo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getDeptInfoPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "", "2", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	@Override
	public ApiResponseResult getItemList(String keyword, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getItemListPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId() + "",
				"成品", keyword,pageRequest.getPageSize(),pageRequest.getPageNumber()+1);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("Total", list.get(2));
		map.put("List", list.get(3));
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getReport(String beginTime,
			String endTime,String deptId,String itemNo, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getReportPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId() + "",
				beginTime, endTime,deptId,itemNo,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("Depart", list.get(2));
		map.put("Total", list.get(3));
		map.put("List", list.get(4));
		return ApiResponseResult.success().data(map);
	}
	
	/**
	 * 品质检验日报表
	 * 2020-11-21
	 * */
	public List getReportPrc(String facoty,String company,String user_id,String beginTime,
			String endTime,String deptId,String itemNo, PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_rpt_chk_day(?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, beginTime);
				cs.setString(5, endTime);
				cs.setString(6, deptId);
				cs.setInt(7, pageRequest.getPageSize());
				cs.setInt(8, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(12, java.sql.Types.INTEGER);// 输出参数 返回标识
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
				if (cs.getString(9).toString().equals("0")) {
					result.add(cs.getString(11));//部门名称
					result.add(cs.getString(12));//总记录数
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
				System.out.println(l);
				return result;
			}
		});
		return resultList;
	}

	@Override
	public ApiResponseResult getDetailReport(String user_id, String dep_id, String proc_no, String fdate,
			PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getDetailReportPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",user_id,
				dep_id, proc_no,fdate,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("Depart", list.get(2));
		map.put("Total", list.get(3));
		map.put("List", list.get(4));
		return ApiResponseResult.success().data(map);
	}
	/**
	 * 品质检验日报表-下钻明细
	 * 2020-11-21
	 * */
	public List getDetailReportPrc(String facoty,String company,String user_id,String dep_id, 
			String proc_no, String fdate, PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_rpt_chk_day_det(?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, dep_id);
				cs.setString(5, fdate);
				cs.setString(6, proc_no);
				cs.setInt(7, pageRequest.getPageSize());
				cs.setInt(8, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(12, java.sql.Types.INTEGER);// 输出参数 返回标识
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
				if (cs.getString(9).toString().equals("0")) {
					result.add(cs.getString(11));//部门名称
					result.add(cs.getString(12));//总记录数
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
				System.out.println(l);
				return result;
			}
		});
		return resultList;
	}
	
}
