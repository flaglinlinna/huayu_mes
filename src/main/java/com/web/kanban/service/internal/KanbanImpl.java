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
		return ApiResponseResult.success().data(map);
	}

	@Override
	public ApiResponseResult getCjbgDepList() throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(kanbanDao.getDepList());
	}

}
