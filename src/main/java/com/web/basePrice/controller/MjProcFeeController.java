package com.web.basePrice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.MjProcFee;
import com.web.basePrice.service.MjProcFeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "模具成本维护模块")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "basePrice/procFee")
public class MjProcFeeController extends WebController {

	private String module = "模具成本维护";

	@Autowired
	private MjProcFeeService mjProcFeeService;

	@ApiOperation(value = "模具成本维护表结构", notes = "模具成本维护表结构" + MjProcFee.TABLE_NAME)
	@RequestMapping(value = "/getMjProcFee", method = RequestMethod.GET)
	@ResponseBody
	public MjProcFee getMjProcFee() {
		return new MjProcFee();
	}

	@ApiOperation(value = "模具成本维护页", notes = "模具成本维护页", hidden = true)
	@RequestMapping(value = "/toMjProcFee")
    public String toMjProcFee(){
        return "/web/basePrice/proc_fee";
    }
	
	@ApiOperation(value = "获取模具成本维护列表", notes = "获取模具成本维护列表",hidden = true)
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword,String pkQuote) {
        String method = "basePrice/procFee/getList";String methodName ="获取模具成本维护列表";
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            ApiResponseResult result = mjProcFeeService.getList(keyword,super.getPageRequest(sort));
            logger.debug("获取模具成本维护列表=getList:");
            //getSysLogService().success(module,method, methodName, keyword);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取模具成本维护列表失败！", e);
            getSysLogService().error(module,method, methodName, "关键字:"+keyword+";"+e.toString());
            return ApiResponseResult.failure("获取模具成本维护列表失败！");
        }
    }

    @ApiOperation(value = "获取客户品质标准文件列表", notes = "获取客户品质标准文件列表",hidden = true)
    @RequestMapping(value = "/getFileList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getFileList(Long customId) {
        String method = "basePrice/customQs/getFileList";String methodName ="获取客户品质标准文件列表";
        try {
            System.out.println(customId);
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = mjProcFeeService.getFileList(customId);
            logger.debug("获取客户品质标准列表=getList:");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取客户品质标准列表失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("获取客户品质标准列表失败！");
        }
    }

    @ApiOperation(value = "删除客户品质标准附件", notes = "删除客户品质标准信息",hidden = true)
    @RequestMapping(value = "/delFile", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delFile(Long recordId, Long fileId){
        String method = "basePrice/customQs/delFile";String methodName ="删除客户品质标准附件";
        try{
            ApiResponseResult result = mjProcFeeService.delFile(recordId,fileId);
            logger.debug("删除客户品质标准附件=delete:");
            getSysLogService().success(module,method, methodName, null);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除客户品质标准附件失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure("删除客户品质标准附件失败！");
        }
    }


    @ApiOperation(value = "新增报价-模具成本信息", notes = "新增报价-模具成本信息",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody MjProcFee mjProcFee) {   	
        String method = "basePrice/procFee/add";String methodName ="新增报价-模具成本信息";
        try{
            ApiResponseResult result = mjProcFeeService.add(mjProcFee);
            logger.debug("新增报价-模具成本信息=add:");
            getSysLogService().success(module,method, methodName,mjProcFee.toString());
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("新增报价-模具成本信息失败！", e);
            getSysLogService().error(module,method, methodName,mjProcFee.toString());
            return ApiResponseResult.failure("新增报价-模具成本信息失败！");
        }
    }
	
	@ApiOperation(value = "编辑模具成本信息", notes = "编辑模具成本信息", hidden = true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponseResult edit(@RequestBody MjProcFee mjProcFee) {
		String method = "basePrice/procFee/edit";
		String methodName = "编辑模具成本信息";
		try {
			ApiResponseResult result = mjProcFeeService.edit(mjProcFee);
			logger.debug("编辑模具成本信息=edit:");
			getSysLogService().success(module, method, methodName, mjProcFee.toString());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("编辑模具成本信息编辑失败！", e);
			getSysLogService().error(module, method, methodName, 
					mjProcFee.toString() + "," + e.toString());
			return ApiResponseResult.failure("编辑模具成本信息编辑失败！");
		}
	}
	
	@ApiOperation(value = "删除", notes = "删除",hidden = true)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
        String method = "basePrice/procFee/delete";String methodName ="删除";
        try{
        	Long id = Long.parseLong(params.get("id").toString()) ;
            ApiResponseResult result = mjProcFeeService.delete(id);
            logger.debug("删除=delete:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch(Exception e){
            e.printStackTrace();
            logger.error("删除失败！", e);
            getSysLogService().error(module,method, methodName, params+";"+e.toString());
            return ApiResponseResult.failure("删除失败！");
        }
    }
}
