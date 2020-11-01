package com.web.kanban.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.web.kanban.KanbanDao;
import com.web.kanban.service.KanbanService;

@Service(value = "KanbanService")
@Transactional(propagation = Propagation.REQUIRED)
public class KanbanImpl extends PrcKanbanUtils  implements KanbanService {

	@Autowired
	KanbanDao kanbanDao;

	@Override
	public ApiResponseResult getCjbgList(String class_no, String dep_id, String sdata, String edata,String dev_ip) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getCjbgListPrc("","","",class_no,  dep_id,  sdata,  edata, dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List", list.get(2));
		map.put("Sdata", list.get(3));
		map.put("Edata", list.get(4));
		map.put("LineNum", list.get(5));//开线数
		return ApiResponseResult.success().data(map);
	}

	@Override
	public ApiResponseResult getCjbgDepList() throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(kanbanDao.getDepList());
	}

	@Override
	public ApiResponseResult getScdzList(String class_no, String dep_id, String sdata, String edata, String dev_ip)
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getScdzListPrc("","","",class_no,  dep_id,  sdata,  edata, dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List", list.get(2));
		map.put("Sdata", list.get(3));
		map.put("Edata", list.get(4));
		map.put("LINE_NUM_PLN", list.get(5));//
		map.put("LINE_NUM_NOW", list.get(6));
		map.put("EMP_NUM_PLN", list.get(7));
		map.put("EMP_NUM_NOW", list.get(8));
		map.put("PRD_NUM_PLN", list.get(9));
		map.put("PRD_NUM_DONE", list.get(10));
		map.put("PRD_RATE_DONE", list.get(11));
		map.put("PO_NUM_EMP_OFF", list.get(12));
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getZcblList()
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getZcblListPrc();
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List", list.get(2));
		map.put("DeptName", list.get(3));
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		
		return ApiResponseResult.success().data(map);
	}

}
