//package com.system.check.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.app.base.control.WebController;
//import com.app.base.data.ApiResponseResult;
//import com.system.check.entity.CheckInfo;
//import com.system.check.service.CheckService;
//import com.system.user.entity.SysUser;
//import com.utils.UserUtil;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//
///**
// * 审批
// * @author fyx
// *
// */
//@Api(description = "审批")
////@RestController
//@Controller
//@RequestMapping(value="/check")
//public class CheckController extends WebController {
//	@Autowired
//	private CheckService checkService;
//
//	@RequestMapping(value = "/toCheck")
//    public String toUserList(){
//        return "/web/views/iframe/check";
//    }
//
//    @RequestMapping(value = "/toCheckUnit")
//    public String toCheckUnit(){
//        return "/web/views/iframe/checkUnit";
//    }
//
//	@ApiOperation(value="获取审批初始化信息", notes="获取审批初始化信息")
//    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
//    @ResponseBody
//	public ApiResponseResult getInfo(Long id,String wname) {
//        String method = "/check/getInfo";String methodName ="获取审批初始化信息";
//		try {
//            ApiResponseResult result = checkService.getInfo(id,wname);
//            logger.debug("获取审批初始化信息=getInfo:");
////            getSysLogService().success(method, methodName, null);
//            return result;
//		} catch (Exception e) {
//            e.printStackTrace();
//            logger.error("获取审批初始化信息失败！", e);
////            getSysLogService().error(method, methodName, e.toString());
//			return ApiResponseResult.failure("获取审批初始化信息失败！");
//		}
//	}
//
//	@ApiOperation(value="提交审批", notes="提交审批")
//	@ApiImplicitParams({
//	})
//	@RequestMapping(value = "/doCheck", method = RequestMethod.POST)
//	@ResponseBody
//	public ApiResponseResult doCheck(CheckInfo checkInfo) {
//        String method = "/check/doCheck";String methodName ="提交审批";
//		try {
//			//判断是否是第一次发起审批
//			if(checkService.checkFirst(checkInfo.getBsRecordId(),checkInfo.getBsCheckCode())){
//				//1.首次发起审批
//				SysUser user = UserUtil.getSessionUser();
////				if(user.getBsType() !=2){
////					return ApiResponseResult.failure("只有防御单位才可以发起审批!");
////				}
//				//新增流程信息
//				checkService.addCheckFirst(checkInfo);
//			}else{
//				//2.判断此用户是否有审核权限
//				if(checkService.checkSecond(checkInfo.getBsRecordId(),checkInfo.getBsCheckCode())){
//                    //2.1 审批
//					checkService.doCheck(checkInfo);
//                }else{
//				    //2.2 无审批权限，返回提示信息
//                    return ApiResponseResult.failure("当前用户在该步骤无审批权限");
//                }
//			}
//            logger.debug("提交审批=doCheck:");
////            getSysLogService().success(method, methodName, null);
//			return ApiResponseResult.success("提交审批成功");
//		} catch (Exception e) {
//			e.printStackTrace();
//            logger.error("获取审批初始化信息失败！", e);
////            getSysLogService().error(method, methodName, e.toString());
//			return ApiResponseResult.failure("提交审批失败！");
//		}
//	}
//
//	@ApiOperation(value="获取审批信息", notes="获取审批信息")
//	@RequestMapping(value = "/getCheckList", method = RequestMethod.GET)
//	public ApiResponseResult getCheckList(Long id) {
//        String method = "/check/getCheckList";String methodName ="获取审批信息";
//		try {
//            ApiResponseResult result = checkService.getCheckByRecordId(id);
//            logger.debug("获取审批信息=getCheckList:");
////            getSysLogService().success(method, methodName, null);
//            return result;
//		} catch (Exception e) {
//            e.printStackTrace();
//            logger.error("获取审批信息失败！", e);
////            getSysLogService().error(method, methodName, e.toString());
//			return ApiResponseResult.failure("获取审批信息失败！");
//		}
//	}
//
//	@ApiOperation(value="获取待审批信息", notes="获取待审批信息")
//	@RequestMapping(value = "/getUnCheckList", method = RequestMethod.GET)
//	public ApiResponseResult getUnCheckList() {
//		try {
//            return  checkService.getUnCheckList();
//		} catch (Exception e) {
//            e.printStackTrace();
//            logger.error("获取待审批信息！", e);
//			return ApiResponseResult.failure("获取待审批信息！");
//		}
//	}
//
//}
