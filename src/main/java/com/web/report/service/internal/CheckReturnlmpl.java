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
import com.app.base.data.DataGrid;
import com.utils.UserUtil;
import com.web.report.service.CheckReturnService;

/**
 * 检验批数及退货报表
 */
@Service(value = "CheckReturnService")
@Transactional(propagation = Propagation.REQUIRED)
public class CheckReturnlmpl extends ReportPrcUtils implements CheckReturnService {
	
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
	public ApiResponseResult getList(String month,String deptId,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getCheckReturnReportPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId() + "",
				deptId, month,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("dept", list.get(2));
		map.put("Total", list.get(3));
		map.put("List", list.get(4));
		return ApiResponseResult.success().data(map);
	}
	
	/**
	 * 检验批数及退货报表
	 * 2020-12-07
	 * */
	public List getCheckReturnReportPrc(String facoty,String company,String user_id,String deptId,
			String month,PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_rpt_chk_return(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, month);
				cs.setString(5, deptId);
				cs.setInt(6, pageRequest.getPageSize());
				cs.setInt(7,  pageRequest.getPageNumber()+1);
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识-信息
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识-部门名称
				cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 返回标识-总记录数
				cs.registerOutParameter(12, -10);// 输出参数 追溯数据-游标
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
	
}
