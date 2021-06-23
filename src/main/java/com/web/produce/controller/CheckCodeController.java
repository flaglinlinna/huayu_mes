package com.web.produce.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.utils.CodeQueue;
import com.utils.UserUtil;
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
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;

@Api(description = "小码校验模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "produce/check_code")
public class CheckCodeController extends WebController {

	 private String module = "小码校验";
	 @Autowired
	 private CheckCodeService checkCodeService;

//	 CodeQueue codeQueue;
//	  CodeQueue codeQueue = CodeQueue.getCodeQueue();
	 @ApiOperation(value = "小码校验页", notes = "小码校验页", hidden = true)
	    @RequestMapping(value = "/toCheckCode")
	    public ModelAndView toCheckCode(String type){
		 ModelAndView mav = new ModelAndView();
		 mav.addObject("type", type);
		 mav.setViewName("/web/produce/check_code/check_code");
		 return mav;
	    }

	@ApiOperation(value = "小码校验页", notes = "小码校验页", hidden = true)
	@RequestMapping(value = "/toInfraredCode")
	public ModelAndView toInfraredCode(String type){
		ModelAndView mav = new ModelAndView();
		mav.addObject("type", type);
		mav.setViewName("/web/produce/check_code/infrared_code");
		return mav;
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
				String checkRep = params.get("checkRep") == null?"":params.get("checkRep").toString();
				String prcType = params.get("prcType") == null?"":params.get("prcType").toString();
//				Integer checkRep = params.get("checkRep") == null?null:Integer.parseInt(params.get("checkRep").toString());
				String type = "";
				if(params.get("type") != null){
					//产出1 投入2
					type = params.get("type").toString().equals("1")?"产出":"投入";
				}
				String company = UserUtil.getSessionUser().getCompany()==null?"":UserUtil.getSessionUser().getCompany();
				String factory = UserUtil.getSessionUser().getFactory()==null?"":UserUtil.getSessionUser().getFactory();

	            ApiResponseResult result = checkCodeService.subCode(taskNo,itemCode,linerName,barcode1,barcode2,checkRep,type,prcType);
				if(result.isResult()){
					JSONObject jsonObject = (JSONObject) result.getData();
//					Map<String,Object> map = JSONObject.parseObject(result.getData()));
					CodeQueue.getCodeQueue().produce( company+ ","+ factory+","+ UserUtil.getSessionUser().getId() + ","+taskNo+","+itemCode+","+linerName+","+type+","+jsonObject.get("time").toString()+","+prcType);
				}
				logger.debug("小码校验=subCode:");

	            //暂不写入日志
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
//	            return null;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("小码校验失败！", e);
//	             getSysLogService().error(module,method, methodName, params+";"+e.toString());
	             return ApiResponseResult.failure("小码校验失败！");
	        }
	    }

		 @PostConstruct
		 public void runQueue(){
			 new Thread(){
				 @Override
				 public void run() {
					 try {
						 while (true) {
							 String info =  CodeQueue.getCodeQueue().consume();
							 String[] infoArray = info.split(",");
//							 System.out.println(infoArray[2]);
							 try {
								 ApiResponseResult result = checkCodeService.updateCode(infoArray[0],infoArray[1],infoArray[2],infoArray[3],infoArray[4],infoArray[5],infoArray[6],infoArray[7],"");
								 System.out.println(result.getData());
							 }catch (Exception e){
							 	e.printStackTrace();
							 }
							 System.out.println("队列剩余任务："+CodeQueue.getCodeQueue().size()+"个");
						 }
					 } catch (InterruptedException ex) {
					 }
				 }
			 }.start();
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
