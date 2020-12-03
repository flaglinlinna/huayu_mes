package com.web.produce.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import com.web.produce.dao.PatchCardDao;
import com.web.produce.entity.CardData;
import com.web.produce.entity.DevClock;
import com.web.produce.entity.PatchCard;
import com.web.produce.entity.PatchCard;
import com.web.produce.service.PatchCardService;

/**
 * 补卡处理
 *
 */
@Service(value = "PatchCardService")
@Transactional(propagation = Propagation.REQUIRED)
public class PatchCardlmpl extends PrcUtils implements PatchCardService {
	@Autowired
	PatchCardDao patchCardDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取指令单
	 */
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", 10, UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	/**
	 * 获取员工信息
	 */
	public ApiResponseResult getEmpInfo(String keyword,PageRequest pageRequest) throws Exception {
		List<Object> list = getEmpPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "",
				keyword,pageRequest.getPageNumber()+1,pageRequest.getPageSize());
		if (!list.get(0).toString().equals("0")) {
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map<String,Object> map = new HashMap<>();
		map.put("total",list.get(2));
		map.put("rows",list.get(3));
		return ApiResponseResult.success().data(map);
	}

	// 获取指令单
//	public List getEmpInfoPrc(String company, String facoty, String user_id, String keyword) throws Exception {
//		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
//			@Override
//			public CallableStatement createCallableStatement(Connection con) throws SQLException {
//				String storedProc = "{call  prc_mes_cof_emp_chs(?,?,?,?,?,?,?)}";// 调用的sql
//				CallableStatement cs = con.prepareCall(storedProc);
//				cs.setString(1, facoty);
//				cs.setString(2, company);
//				cs.setString(3, user_id);
//				cs.setString(4, keyword);
//				cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
//				cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
//				cs.registerOutParameter(7, -10);// 输出参数 追溯数据
//				return cs;
//			}
//		}, new CallableStatementCallback() {
//			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
//				List<Object> result = new ArrayList<>();
//				List<Map<String, Object>> l = new ArrayList();
//				cs.execute();
//				result.add(cs.getInt(5));
//				result.add(cs.getString(6));
//				if (cs.getString(5).toString().equals("0")) {
//					// 游标处理
//					ResultSet rs = (ResultSet) cs.getObject(7);
//
//					try {
//						l = fitMap(rs);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					result.add(l);
//				}
//				System.out.println(l);
//				return result;
//			}
//
//		});
//		return resultList;
//	}

	public List getEmpPrc(String company,String facoty,String user_id, String keyword,Integer page,Integer limit) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_emp_chs_PG (?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, facoty);
				cs.setString(3, user_id);
				cs.setString(4, "");
				cs.setString(5, keyword);
				cs.setInt(6, limit);
				cs.setInt(7, page);
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 总记录数
				cs.registerOutParameter(9, java.sql.Types.INTEGER); //返回标识
				cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(11, -10);// 输出参数 追溯数据
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
					result.add(cs.getString(8));
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

	/**
	 * 查询列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("employee.empCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("employee.empName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("line.lineName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("hourType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("taskNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("cardType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("signDate", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<PatchCard> spec = Specification.where(BaseService.and(filters, PatchCard.class));
		Specification<PatchCard> spec1 = spec.and(BaseService.or(filters1, PatchCard.class));
		Page<PatchCard> page = patchCardDao.findAll(spec1, pageRequest);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (PatchCard bs : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bs.getId());
			map.put("empCode", bs.getEmployee().getEmpCode());// 获取关联表的数据-工号
			map.put("empName", bs.getEmployee().getEmpName());// 获取关联表的数据-姓名
			map.put("cardType", bs.getCardType());
			map.put("taskNo", bs.getTaskNo());
			map.put("className", bs.getClassType().getClassName());
			map.put("hourType", bs.getHourType());
			map.put("signTime", bs.getSignTime());
			map.put("signDate", bs.getSignDate());
			map.put("lineName", bs.getLine().getLineName());
			map.put("createDate", bs.getCreateDate());
			map.put("lastupdateDate", bs.getLastupdateDate());
			list.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	/**
	 * 新增补卡记录
	 */
	@Override
	@Transactional
	public ApiResponseResult add(PatchCard patchCard) throws Exception {
		if (patchCard == null) {
			return ApiResponseResult.failure("补卡记录不能为空！");
		}

		int cc = patchCardDao.countByDelFlagAndEmpIdAndSignTimeAndSignDate(0, patchCard.getEmpId(),
				patchCard.getSignTime(), patchCard.getSignDate());
		if (cc > 0) {
			return ApiResponseResult.failure("该数据已存在!不允许重复添加!");
		}
		patchCard.setCreateDate(new Date());
		patchCard.setCreateBy(UserUtil.getSessionUser().getId());
		patchCard.setDelFlag(0);
		patchCardDao.save(patchCard);

		return ApiResponseResult.success("补卡记录添加成功！").data(patchCard);
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
	public ApiResponseResult getPatchCard(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("补卡记录ID不能为空！");
		}
		PatchCard o = patchCardDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该补卡记录不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除补卡记录
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("补卡记录ID不能为空！");
		}
		PatchCard o = patchCardDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("补卡记录不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		patchCardDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}
	
	@Override
	@Transactional
	public ApiResponseResult edit(PatchCard patchCard) throws Exception {
		PatchCard o = patchCardDao.findById((long) patchCard.getId());
//		int cc = patchCardDao.countByDelFlagAndEmpIdAndSignTimeAndSignDate(0, patchCard.getEmpId(),
//				patchCard.getSignTime(), patchCard.getSignDate());
//		if (cc > 0) {
//			return ApiResponseResult.failure("该数据已存在!不允许重复添加!");
//		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEmpId(patchCard.getEmpId());
		o.setLineId(patchCard.getLineId());
		o.setClassId(patchCard.getClassId());
		o.setWorkDate(patchCard.getWorkDate());
		o.setCardType(patchCard.getCardType());
		o.setClassId(patchCard.getClassId());
		o.setHourType(patchCard.getHourType());	
		o.setSignDate(patchCard.getSignDate());
		o.setSignTime(patchCard.getSignTime());
		o.setTaskNo(patchCard.getTaskNo());
		patchCardDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}
}
