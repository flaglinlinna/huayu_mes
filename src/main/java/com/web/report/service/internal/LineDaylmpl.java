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
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.report.service.LineDayService;

/**
 * 各线每日生产报表
 */
@Service(value = "LineDayService")
@Transactional(propagation = Propagation.REQUIRED)
public class LineDaylmpl extends ReportPrcUtils implements LineDayService {
	
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
	public ApiResponseResult getList(String beginTime,
			String endTime,String deptId,String itemNo, String devc_id,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		SysUser user = UserUtil.getSessionUser();
		String facoty="",company="", user_id="1";
		if(user != null){
			facoty=user.getFactory();
			company=user.getCompany();
			user_id=user.getId()+"";
		}
		List<Object> list = getListPrc(facoty,company,user_id,
				beginTime, endTime,deptId,itemNo,devc_id,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	/**
	 * 检验批次报表
	 * 2020-11-07
	 * */
	public List getListPrc(String facoty,String company,String user_id,String beginTime,
			String endTime,String deptId,String itemNo, String devc_id,PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_RPT_DAILY_PROD(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, devc_id);
				cs.setString(4, user_id);
				cs.setString(5, beginTime);
				cs.setString(6, endTime);
				cs.setString(7, deptId);
				cs.setString(8, itemNo);
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(11,  -10);// 输出参数 返回标识
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
	
}
