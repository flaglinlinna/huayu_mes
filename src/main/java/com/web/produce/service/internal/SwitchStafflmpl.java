package com.web.produce.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.produce.dao.ClassTypeDao;
import com.web.produce.entity.ClassType;
import com.web.produce.service.SwitchStaffService;

/**
 * 在线人员调整模块
 *2020-11-27
 */
@Service(value = "SwitchStaffService")
@Transactional(propagation = Propagation.REQUIRED)
public class SwitchStafflmpl extends PrcUtils implements SwitchStaffService {

	ClassTypeDao classTypeDao;
	
	//获取制令单
	@Override
	public ApiResponseResult getTaskNo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getTaskNoPrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", 15, UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	// 获取待调整人员
	@Override
	public ApiResponseResult getTaskNoEmp(String taskNo, String workDate, PageRequest pageRequest) throws Exception{
		// TODO Auto-generated method stub
				List<Object> list = getEmpByTaskNoPrc(UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getId() + "",
						 taskNo,  workDate,  pageRequest);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				Map map = new HashMap();
				map.put("total", list.get(2));
				map.put("rows", list.get(3));
				return ApiResponseResult.success("").data(map);
	}
	//获取产线信息
	@Override
	public ApiResponseResult getLine(String keyword) throws Exception{
		List<Object> list = getLinePrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	//获取班次信息
	@Override
	public ApiResponseResult getClassType() throws Exception{
		List<ClassType> list = classTypeDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}
	//执行人员调整
	@Override
	public ApiResponseResult doSwitch(String lastTaskNo, String lastLineId, String lastHourType, 
			String lastClassId,String lastWorkDate, String lastDateEnd, String lastTimeEnd,
			String newTaskNo, String newLineId,String newHourType, String newClassId,
			String newWorkDate, String newTimeBegin, String empList,PageRequest pageRequest) throws Exception{
		
		List<Object> list = doTaskNoSwitchPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId() + "",
				lastTaskNo,lastLineId,lastHourType,lastClassId,lastWorkDate,lastDateEnd,
				lastTimeEnd,newTaskNo,newLineId,newHourType,newClassId,newWorkDate,
				newTimeBegin,empList,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}
 
}
