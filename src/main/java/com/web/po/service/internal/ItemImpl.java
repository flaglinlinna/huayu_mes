package com.web.po.service.internal;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.app.base.data.ApiResponseResult;
import com.web.po.ApiUtil;
import com.web.po.service.ItemService;

@Service(value = "ItemService")
@Transactional(propagation = Propagation.REQUIRED)
public class ItemImpl  implements ItemService {


	@Override
	public ApiResponseResult findForecastList(String type,int page,int rows) throws Exception {
		// TODO Auto-generated method stub
		if("0".equals(String.valueOf(page)) || "null".equals(String.valueOf(page)) || page <= 0){
			page = 1;
		}
		if("0".equals(String.valueOf(rows)) || "null".equals(String.valueOf(rows)) || rows <= 0){
			rows = 20;
		}
		String param = "{}";
		if(type.equals("findForecastList")){
			param = "{\"suppItemCode\": null, \"itemCode\": null,\"organizationId\": \"157\",\"startTime\": \"2020-09-10\",\"endTime\": \"2021-01-03\",\"purchaseMode\": null,\"bizModel\": null,\"buyerName\": null,\"queryLastFC\": false,\"level\": null,\"queryType\": \"hw\"}";
		}
		                                                                          //http://apigw-beta.huawei.com/api/service/esupplier/findForecastList/1.0.0
		return ApiResponseResult.success("").data(JSON.parseObject(ApiUtil.getList("http://apigw-beta.huawei.com/api/service/esupplier/findForecastList/1.0.0/"+rows+"/"+page+"",param), HashMap.class));
	}

}
