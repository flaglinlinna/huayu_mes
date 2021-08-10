package com.web.report.service.internal;

import com.app.base.data.ApiResponseResult;
import com.utils.ExcelExport;
import com.utils.UserUtil;
import com.web.report.service.RptHourService;
import com.web.report.service.RptUpphService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过程检验日报表
 */
@Service(value = "RptHourService")
@Transactional(propagation = Propagation.REQUIRED)
public class RptHourlmpl extends ReportPrcUtils implements RptHourService {
	
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
	public ApiResponseResult getReport(String beginTime, String endTime, String itemNo,String linerName,String taskNo,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getReportPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId()+"",
				beginTime, endTime,linerName,itemNo+"",taskNo+"",pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
//		map.put("Depart", list.get(2));
		map.put("Total", list.get(2));
		map.put("List", list.get(3));
		return ApiResponseResult.success().data(map);
	}

//	pi_company    varchar2, --公司
//	pi_factory    varchar2, --工厂
//	pi_user       varchar2, --用户ID
//	pi_liner_name varchar2, --组长
//	pi_task_no    varchar2, --制令单
//	pi_item_no    varchar2, --产品编码
//	pi_day_begin  varchar2, --统计开始日期(年月日)
//	pi_day_end    varchar2, --统计结束日期(年月日)
//	pi_page       in number, --每页记录数
//	pi_size       in number, --页码
//	po_flag       out number, --返回标志
//	po_text       out varchar, --返回信息
//	po_renum      out number, --总记录数
//	po_result     out sys_refcursor --返回数据集合
	
	/**
	 * 品质检验日报表
	 * 2020-11-21
	 * */
	public List getReportPrc(String facoty, String company, String userId, String beginTime,
							 String endTime, String linerName, String itemNo, String taskNo, PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_rpt_hour_data(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, userId);
				cs.setString(4, linerName);
				cs.setString(5, taskNo);
				cs.setString(6, itemNo);
				cs.setString(7, beginTime);
				cs.setString(8, endTime);
				cs.setInt(9, pageRequest.getPageSize());
				cs.setInt(10, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(13, java.sql.Types.INTEGER);// 输出参数 返回总数
				cs.registerOutParameter(14, -10);// 输出参数 追溯数据
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
					// 游标处理
					result.add(cs.getInt(13));
					ResultSet rs = (ResultSet) cs.getObject(14);
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
	public void exportExcel(HttpServletResponse response,String beginTime, String endTime, String itemNo,String linerName,String taskNo,PageRequest pageRequest)
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getReportPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId()+"",
				beginTime, endTime,linerName,itemNo+"",taskNo+"",pageRequest);
		if (list.get(0).toString().equals("0")) {
			String excelPath = "static/excelFile/";
			String fileName = "UPH日报表模板.xlsx";
			String[] map_arr = new String[]{"ITEM_NO","LINER_NAME","TASK_NO","FDAY","TIME_BG","TIME_END","TIME_NUM","TIME_BG_ACT","TIME_END_ACT","QTY_T_TAR"
			,"QTY_T_ACT","RATE_T","QTY_C_TAR","QTY_C_ACT","RATE_C","QTY_NG","RATE_OK"};
			XSSFWorkbook workbook = new XSSFWorkbook();
//			Map map = new HashMap();
//			List<Map> mapList=	TypeChangeUtils.objectToList(list.get(3),Map.class);
//			list.get(3);
			List<Map<String, Object>> mapList = (List<Map<String, Object>>)list.get(3);
			ExcelExport.exportByRow(response,mapList,workbook,map_arr,excelPath+fileName,"UPH日报表查询导出.xlsx",2);

		}

	}
	
}
