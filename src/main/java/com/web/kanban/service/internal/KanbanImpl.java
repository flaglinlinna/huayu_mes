package com.web.kanban.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.kanban.KanbanDao;
import com.web.kanban.service.KanbanService;

@Service(value = "KanbanService")
@Transactional(propagation = Propagation.REQUIRED)
public class KanbanImpl extends PrcKanbanUtils  implements KanbanService {

	@Autowired
	KanbanDao kanbanDao;

	@Override
	public ApiResponseResult getCjbgList(String class_no, String dep_id, String sdata, String dev_ip) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getCjbgListPrc("","","",class_no,  dep_id,  sdata,  dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List", list.get(2));
		map.put("Sdata", list.get(3));
		map.put("Edata", list.get(4));
		map.put("LineNum", list.get(5));//开线数
		map.put("Title", list.get(6));//开线数
		return ApiResponseResult.success().data(map);
	}
	@Override
	public ApiResponseResult getCjbgDetailList(String liner, String dev_ip) throws Exception {
		// TODO Auto-generated method stub
		String usr_id = "1";
		SysUser su = UserUtil.getSessionUser();
		if(su != null){
			usr_id = su.getId()+"";
		}
		if(liner.equals("总体")){
			liner = "";
		}
		//List<Object> list = getCjbgDetailListPrc(usr_id,liner, dev_ip);
		List<Object> list = getCjbgDetailListPrc(usr_id,liner, "1");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getCjbgDepList() throws Exception {
		// TODO Auto-generated method stub
		//return ApiResponseResult.success().data(kanbanDao.getDepList());
		List<Object> list = getOrgListPrc("","","","电子看板");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getScdzList(String class_no, String dep_id, String sdata,  String dev_ip)
			throws Exception {
		String user_id = "1";
		SysUser su = UserUtil.getSessionUser();
		if(su != null){
			user_id = su.getId()+"";
		}
		List<Object> list = getScdzListPrc("","",user_id,class_no,  dep_id,  sdata,  dev_ip);
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
		map.put("Title", list.get(13));
		return ApiResponseResult.success().data(map);
	}
	@Override
	public ApiResponseResult getScdzDetailList(String liner, String dep_id, 
			String dev_ip,String fieldword) throws Exception {
		// TODO Auto-generated method stub
		String user_id = "1";
		SysUser su = UserUtil.getSessionUser();
		if(su != null){
			user_id = su.getId()+"";
		}
		List<Object> list = getScdzDetailListPrc( user_id, liner,dep_id, dev_ip,fieldword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	@Override
	public ApiResponseResult getZcblDepList() throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getOrgListPrc("","","2","");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	@Override
	public ApiResponseResult getZcblList(String class_no, String dep_id, String sdata,String dev_ip)
			throws Exception {
		// TODO Auto-generated method stub
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
				
		List<Object> list = getZcblListPrc("","",usr_id,class_no,  dep_id,  sdata, dev_ip);
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
	
	@Override
	public ApiResponseResult getXlpmList( String class_id,String dep_id, String sdata, String dev_ip,String liner) throws Exception{	
		List<Object> list = getXlpmListPrc("","","",class_id,dep_id,sdata,dev_ip,liner);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_table", list.get(2));
		map.put("List_line", list.get(3));
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		map.put("Title", list.get(6));
		
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getDfgList(String class_id,String dep_id, String sdata,
			String usr_id,String dev_ip) throws Exception{	
		List<Object> list = getDfgListPrc("","",usr_id,class_id,dep_id,sdata,dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_table", list.get(2));
		map.put("List_dept", list.get(3));
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		
		return ApiResponseResult.success().data(map);
	}
	
	public ApiResponseResult getLiner ()throws Exception{
		List<Object> list = getOrgListPrc("","","","组长");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}	
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getCxdzList(String class_id, String dep_id, String sdata, String dev_ip, String liner)
			throws Exception {
		// TODO Auto-generated method stub
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
		List<Object> list = getCxdzListtPrc("","",usr_id,class_id,dep_id,sdata,dev_ip,liner);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_table", list.get(2));//工单明细
		map.put("List_line", list.get(3));//产线信息
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		map.put("Title", list.get(6));
		
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getCxscList(String taskNo,String deptId,String liner,
			String dev_ip,String interval)throws Exception{
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
		List<Object> list = getCxscList("","",taskNo,deptId,liner,dev_ip,usr_id,interval);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_result1", list.get(2));
		map.put("List_result2", list.get(3));
		map.put("Title", list.get(4));
		
		return ApiResponseResult.success().data(map);		
	}	
	/**
	 * 获取看板刷新间隔的时间
	 *2020-11-19 
	 * **/
	@Override
	public ApiResponseResult getIntervalTime()throws Exception{
		return ApiResponseResult.success().data(kanbanDao.getIntervalTime());
	}
	
	/**
	 * 获取看板轮播间隔的时间
	 *2020-11-21
	 * **/
	@Override
	public ApiResponseResult getRotationTime()throws Exception{
		return ApiResponseResult.success().data(kanbanDao.getRotationTime());
	}
}
