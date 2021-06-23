package com.web.produce.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
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
	public ApiResponseResult getLiner(String keyword, PageRequest pageRequest) throws Exception {
		List<Object> list1 = getLinerPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"");
		if (!list1.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list1.get(1).toString());
		}
		return ApiResponseResult.success("").data(list1.get(2));
	}

	@Override
	public ApiResponseResult getItemCode(String keyword, PageRequest pageRequest) throws Exception {
		List<Object> list = getReworkItemPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","成品",keyword,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	@Override
	public ApiResponseResult subCode(String taskNo,String itemCode,String linerName,String barcode1, String barcode2,String checkRep,String type,String prcType) throws Exception {
		// TODO Auto-generated method stub

		String prcName = "";
		if(prcType!=null){
			if(("check").equals(prcType)){
				prcName ="prc_mes_cof_bar_s_save";
			}else if(("infrared").equals(prcType)){
				prcName ="prc_mes_cof_resp_save";
			}
		}
		List<Object> list = subCodePrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "",
				taskNo,itemCode,linerName,barcode1,barcode2,checkRep,type,prcName);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(3).toString());
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("time",list.get(1).toString());
		jsonObject.put("count",list.get(2).toString());
		return ApiResponseResult.success().data(jsonObject);
	}


	@Override
	public ApiResponseResult updateCode(String company,String factory,String userId,String taskNo,String itemCode,String linerName,String type,String time,String prcType) throws Exception {
		// TODO Auto-generated method stub
		try {
			String prcName = "prc_mes_cof_bar_s_update";
//			if (prcType != null) {
//				if (("check").equals(prcType)) {
//					prcName = "prc_mes_cof_bar_s_save";
//				} else if (("infrared").equals(prcType)) {
//					prcName = "prc_mes_cof_resp_save";
//				}
//			}

			List<Object> list = updateCodePrc(company,factory,userId,
					taskNo,itemCode,linerName,type,time,prcName);
			if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
				return ApiResponseResult.failure(list.get(1).toString());
			}
			return ApiResponseResult.success();
		}catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure().message("");
		}
	}

	// 提交条码
	public List subCodePrc(String company, String facoty, String user_id,String taskNo , String itemCode,
			String linerName, String barcode1, String barcode2,String checkRep,String type,String prcName) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call   "+prcName+" (?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, taskNo);
				cs.setString(5, itemCode);
				cs.setString(6, linerName);
				cs.setString(7, type);
				cs.setString(8, barcode1);
				cs.setString(9, barcode2);
				cs.setString(10, checkRep);
				cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 时间信息
				cs.registerOutParameter(12, java.sql.Types.INTEGER);// 输出参数 已扫描数
				cs.registerOutParameter(13, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(14, java.sql.Types.VARCHAR);// 输出参数 返回信息
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				cs.execute();
				result.add(cs.getInt(13));
				result.add(cs.getString(11));
				result.add(cs.getString(12));
				result.add(cs.getString(14));
				return result;
			}
		});
		return resultList;
	}

	// 提交条码
	public List updateCodePrc(String company, String facoty, String user_id,String taskNo , String itemCode,
						   String linerName, String type,String time,String prcName) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call   "+prcName+" (?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, taskNo);
				cs.setString(5, itemCode);
				cs.setString(6, linerName);
				cs.setString(7, type);
				cs.setString(8, time);
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				cs.execute();
				result.add(cs.getInt(9));
				result.add(cs.getString(10));
//				result.add(cs.getInt(11));
				return result;
			}
		});
		return resultList;
	}

	@Override
	public ApiResponseResult getHistoryList(String keyword,Integer errFlag,  String hStartTime, String hEndTime, PageRequest pageRequest)
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getBarHistoryPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				errFlag,hStartTime,hEndTime,keyword,
				pageRequest.getPageNumber()+1,pageRequest.getPageSize(),"prc_mes_cof_bar_s_chs");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}
}
