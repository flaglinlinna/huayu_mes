package com.web.produce.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.web.produce.service.CheckCodeService;

/**
 * 小码校验
 *
 */
@Service(value = "CheckCodeService")
@Transactional(propagation = Propagation.REQUIRED)
public class CheckCodelmpl extends PrcUtils implements CheckCodeService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", 2, UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult subCode(String taskNo,String barcode1, String barcode2) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = subCodePrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "",
				taskNo,  barcode1,barcode2);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success();
	}

	// 提交条码
	public List subCodePrc(String company, String facoty, String user_id,String taskNo , 
			String barcode1, String barcode2)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_bar_s_save(?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, taskNo);
				cs.setString(5, barcode1);
				cs.setString(6, barcode2);
				cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(8, java.sql.Types.VARCHAR);// 输出参数 返回标识
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				cs.execute();
				result.add(cs.getInt(7));
				result.add(cs.getString(8));
				return result;
			}
		});
		return resultList;
	}
}
