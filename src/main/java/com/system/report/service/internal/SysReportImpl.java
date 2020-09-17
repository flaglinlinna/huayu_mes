package com.system.report.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.report.service.SysReportService;

@Service(value = "SysReportService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysReportImpl  implements SysReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public ApiResponseResult getlist(String string, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method 
		
		//cols
		ArrayList list = new ArrayList();
		for(int i=0;i<3;i++){
			Map map = new HashMap();
			map.put("BS_FILED", "TEST"+i);
			map.put("BS_TITLE", "测试"+i);
			map.put("BS_WIDTH", "");
			map.put("BS_TBODY_COLOR", "");//tbody,当前单元格内容的字的颜色
			map.put("BS_TBODY_BG_COLOR", i>1?"red":"");//tbody,当前单元格内容的背景色
			map.put("BS_TITLE_COLOR", i>1?"blue":"");//当前标题的字的颜色
			map.put("BS_TITLE_BG_COLOR", i>1?"red":"");//当前标题的背景色
			map.put("BS_SORT", true);
			//map.put("BS_LEVEL", 1);//一级目录
			list.add(map);
			
		}
		
		//
		ArrayList list1 = new ArrayList();
		for(int i=0;i<8;i++){
			Map map = new HashMap();
			map.put("TEST0", "test0");
			map.put("TEST1", "test0"+i);
			map.put("TEST2", "TEST2"+i);
			list1.add(map);
		}
		
		Map map_config = new HashMap();
		map_config.put("page", false);
		map_config.put("rowspan_name",  new String[]{"TEST0", "TEST1"});
		map_config.put("rowspan_index",  new int[]{1, 2});
		
		
		Map map = new HashMap();
		
		map.put("Cols", list);
		map.put("List", list1);
		map.put("Config", map_config);
		return ApiResponseResult.success().data(map);
	}

}
