package com.web.produce.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.produce.dao.OnlineStaffDao;
import com.web.produce.entity.CardData;
import com.web.produce.entity.DevClock;
import com.web.produce.entity.OnlineStaff;
import com.web.produce.service.OnlineStaffService;

/**
 * 上线人员
 *
 */
@Service(value = "OnlineStaffService")
@Transactional(propagation = Propagation.REQUIRED)
public class OnlineStafflmpl implements OnlineStaffService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	OnlineStaffDao onlineStaffDao;

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("taskNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("line.lineName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("hourType", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<OnlineStaff> spec = Specification.where(BaseService.and(filters, OnlineStaff.class));
		Specification<OnlineStaff> spec1 = spec.and(BaseService.or(filters1, OnlineStaff.class));
		Page<OnlineStaff> page = onlineStaffDao.findAll(spec1, pageRequest);
		
		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(OnlineStaff bs:page.getContent()){ 
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("taskNo", bs.getTaskNo());
			map.put("hourType", bs.getHourType());
			map.put("lineName", bs.getLine().getLineName());
			map.put("lastupdateDate", bs.getLastupdateDate());
			map.put("createDate", bs.getCreateDate());
			list.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	/**
	 * 根据ID获取
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getMain(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("记录ID不能为空！");
		}
		OnlineStaff o = onlineStaffDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该上线记录不存在！");
		}
		return ApiResponseResult.success().data(o);
	}
	
	@Override
	public ApiResponseResult getMainInfo(Long main_id) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getMainInfoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "",
				main_id);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	public List getMainInfoPrc(String company, String facoty, String user_id,Long  main_id)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_COF_AFFIRM_DET_CHS(?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, user_id);
				cs.setLong(4, main_id);
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
