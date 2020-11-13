package com.web.produce.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.produce.dao.CardDataDao;
import com.web.produce.service.VerifyService;

/**
 * 上线确认模块
 *
 */
@Service(value = "VerifyService")
@Transactional(propagation = Propagation.REQUIRED)
public class Veritylmpl extends PrcUtils implements VerifyService {
	
	@Autowired
	CardDataDao cardDataDao;
	
	
	@Override
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		SysUser sy = UserUtil.getSessionUser();
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",7,UserUtil.getSessionUser().getId()+"",keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getUserByLine(String lineId,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getUserByLinePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				lineId,pageRequest.getPageNumber()+1,pageRequest.getPageSize());
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
		//return ApiResponseResult.success("").data(list.get(2));
	}

	@Override
	public ApiResponseResult getInfoAdd() throws Exception {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("Class", cardDataDao.queryClass());//班次信息
		List<Object> list = getLinePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		map.put("Line", list.get(2));//线体信息
		return ApiResponseResult.success().data(map);
	}

	@Override
	public ApiResponseResult save(String task_no, String line_id, String hour_type, String class_id, String wdate,
			String emp_ids) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getEmpSavePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				task_no,line_id,hour_type,class_id,wdate,emp_ids);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success("保存成功!").data(list.get(2));
	}

	@Override
	public ApiResponseResult getInfoCreateReturn() throws Exception {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		SysUser sy = UserUtil.getSessionUser();

		map.put("Class", cardDataDao.queryClass());//班次信息
		
		List<Object> list1 = getLinerPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"");
		if (!list1.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list1.get(1).toString());
		}
		map.put("User", list1.get(2));
		
		List<Object> list2 = getTaskNoPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",6,UserUtil.getSessionUser().getId()+"","");
		if (!list2.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list1.get(1).toString());
		}
		map.put("Task", list2.get(2));

		List<Object> list3 = getDeptPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
				"2","", "prc_mes_cof_org_chs");

		map.put("Dept", list3.get(3));

		return ApiResponseResult.success().data(map);
	}
	
	/*
	 * 获取返工料号-【创建在线返工制令单】
	 * */
	@Override
	public ApiResponseResult getReworkItem(String keyword,PageRequest pageRequest) throws Exception {
		List<Object> list = getReworkItemPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","成品",keyword,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map_liao = new HashMap();
		map_liao.put("total", list.get(2));
		map_liao.put("rows", list.get(3));		
		return ApiResponseResult.success("").data(map_liao);
		
	}
	
	@Override
	public ApiResponseResult add(String task_no, String item_no, String liner_name, String qty, String pdate,String deptId,String classId)
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getCreateReturnPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				task_no,item_no,  liner_name,  Integer.parseInt(qty),  pdate,deptId,classId);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success("保存成功!");
	}

	@Override
	public ApiResponseResult getHistoryList(String keyword, String hStartTime, String hEndTime, PageRequest pageRequest)
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getHistoryPrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"",
				hStartTime,hEndTime,keyword,
				pageRequest.getPageNumber()+1,pageRequest.getPageSize(),"PRC_LINE_AFFIRM_EMP");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	
}
