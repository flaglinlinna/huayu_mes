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
import com.web.report.service.WorkTimeMesService;
import com.web.report.service.LineEfficService;

/**
 * 工时统计表(MES 按组长、物料)
 */
@Service(value = "WorkTimeMesService")
@Transactional(propagation = Propagation.REQUIRED)
public class WorkTimeMeslmpl extends ReportPrcUtils implements WorkTimeMesService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	/**
	 * 获取组长列表数据
	 * 2020-11-24
	 * */
	@Override
	public ApiResponseResult getLinerList(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getDeptInfoPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "","0","组长");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	/**
	 * 获取物料列表数据
	 * 2020-11-24
	 * */
	@Override
	public ApiResponseResult getItemList(String keyword,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getItemListPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId() + "",
				"成品", keyword,pageRequest.getPageSize(),pageRequest.getPageNumber()+1);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}
	
	/**
	 * 获取主表数据
	 * 2020-11-24
	 * */
	@Override
	public ApiResponseResult getList(String sdate,String edate,String liner_id,
			String itemCode,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getListPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId()+"",
				sdate, edate,liner_id,itemCode,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	private List getListPrc(String facoty,String company,String user_id ,String  beginTime,
			String endTime,String liner_id,String itemCode,PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_att_calc(?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, beginTime);
				cs.setString(5, endTime);
				cs.setString(6, liner_id);
				cs.setString(7, itemCode);
				cs.setInt(8, pageRequest.getPageSize());
				cs.setInt(9, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(10, java.sql.Types.INTEGER);// 输出参数 返回标识
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
				result.add(cs.getInt(11));
				result.add(cs.getString(12));
				if (cs.getString(11).toString().equals("0")) {
					result.add(cs.getInt(10));
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
	/**
	 * 获取从表数据
	 * 2020-11-24
	 * */
	@Override
	public ApiResponseResult getListDetail(String list_id,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getListDetailPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId()+"",
				list_id,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}
	
	private List getListDetailPrc(String facoty,String company,String user_id,
			String list_id,PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_att_det_chs(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, list_id);
				cs.setInt(5, pageRequest.getPageSize());
				cs.setInt(6, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识
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
					result.add(cs.getInt(7));
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
}
