package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.net.InetAddress;
import java.net.UnknownHostException;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.service.CheckCodeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "小码校验模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/check_code")
public class CheckCodeController extends WebController {

	 private String module = "小码校验";
	 @Autowired
	 private CheckCodeService checkCodeService;
	 
	 @ApiOperation(value = "小码校验页", notes = "小码校验页", hidden = true)
	    @RequestMapping(value = "/toCheckCode")
	    public String toCheckCode(){
	        return "/web/produce/check_code/check_code";
	    }
	 
	  @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
	    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getTaskNo(String keyword) {
	        String method = "produce/check_code/getTaskNo";String methodName ="获取指令单信息";
	        try {
	            ApiResponseResult result = checkCodeService.getTaskNo(keyword);
	            logger.debug("获取指令单信息=getTaskNo:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("获取指令单信息失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	             return ApiResponseResult.failure("获取指令单信息失败！");
	        }
	    }

	@ApiOperation(value="获取产品编码信息", notes="获取产品编码信息", hidden = true)
	@RequestMapping(value = "/getItemCode", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getItemCode(String keyword) {
		String method = "produce/check_code/getItemCode";String methodName ="获取产品编码信息";
		try {
			ApiResponseResult result = checkCodeService.getItemCode(keyword,super.getPageRequest());
			logger.debug("获取产品编码信息=getTaskNo:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产品编码信息失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取产品编码信息失败！");
		}
	}

	@ApiOperation(value="获取组长信息", notes="获取组长信息", hidden = true)
	@RequestMapping(value = "/getLiner", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getLiner(String keyword) {
		String method = "produce/check_code/getLiner";String methodName ="获取产品编码信息";
		try {
			ApiResponseResult result = checkCodeService.getLiner(keyword,super.getPageRequest());
			logger.debug("获取组长信息=getLiner:");
//	            getSysLogService().success(module,method, methodName, "关键字:"+keyword);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取组长信息失败！", e);
			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
			return ApiResponseResult.failure("获取组长信息失败！");
		}
	}

	  @ApiOperation(value="小码校验", notes="小码校验", hidden = true)
	    @RequestMapping(value = "/subCode", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult subCode(@RequestBody Map<String, Object> params) {
	        String method = "produce/check_code/subCode";String methodName ="小码校验";
	        try {
				System.out.println(super.getRequest().getLocalPort()+"");
				if(!(super.getRequest().getLocalPort()+"").equals("8083")){
					return  ApiResponseResult.failure("小码校验端口仅支持8083！请在192.168.0.21:8083登录使用");
				}
				String taskNo = params.get("taskNo").toString();
	        	String barcode1 = params.get("barcode1") == null?"":params.get("barcode1").toString();
	        	String barcode2 = params.get("barcode2") == null?"":params.get("barcode2").toString();
				String itemCode = params.get("itemCode") == null?"":params.get("itemCode").toString();
				String linerName = params.get("linerName") == null?"":params.get("linerName").toString();
	            ApiResponseResult result = checkCodeService.subCode(taskNo,itemCode,linerName,barcode1,barcode2);
	            logger.debug("小码校验=subCode:");
	            //暂不写入日志
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("小码校验失败！", e);
	             getSysLogService().error(module,method, methodName, params+";"+e.toString());
	             return ApiResponseResult.failure("小码校验失败！");
	        }
	    }
	  
	  @ApiOperation(value = "获取历史列表", notes = "获取历史列表")
	    @RequestMapping(value = "/getHistoryList", method = RequestMethod.GET)
	    @ResponseBody
		public ApiResponseResult getHistoryList(
				@RequestParam(value = "hkeywork", required = false) String hkeywork,
				@RequestParam(value = "errorFlag", required = false) Integer errorFlag,
				@RequestParam(value = "hStartTime", required = false) String hStartTime,
				@RequestParam(value = "hEndTime", required = false) String hEndTime){
		  String method = "/product/check_code/getHistoryList";String methodName ="获取历史列表";
	        try {
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result =checkCodeService.getHistoryList(hkeywork,errorFlag,hStartTime,hEndTime, super.getPageRequest(sort));
	            logger.debug(methodName+"=getList:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error(methodName+"失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure(methodName+"失败！");
	        }
		}
}
