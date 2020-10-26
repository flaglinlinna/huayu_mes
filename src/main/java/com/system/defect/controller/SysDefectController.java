package com.system.defect.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.system.defect.entity.SysDefect;
import com.system.defect.service.SysDefectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Api(description = "缺陷记录管理模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/sysDefect")
public class SysDefectController extends WebController {

    @Autowired
    private SysDefectService sysDefectService;

    @ApiOperation(value = "新增", notes = "新增")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(SysDefect sysDefect){
        try{
            return sysDefectService.add(sysDefect);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("新增缺陷记录失败！" + e);
            return ApiResponseResult.failure("新增缺陷记录失败！");
        }
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult edit(SysDefect sysDefect){
        try{
            return sysDefectService.edit(sysDefect);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("编辑缺陷记录失败！" + e);
            return ApiResponseResult.failure("编辑缺陷记录失败！");
        }
    }

    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "记录ID", dataType = "Long", paramType = "query", defaultValue = "")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult delete(Long id){
        try{
            return sysDefectService.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除缺陷记录失败！" + e);
            return ApiResponseResult.failure("删除缺陷记录失败！");
        }
    }

    @RequestMapping(value = "/toDefectList")
    public String toDefectList(){
        return "/system/defect/defect";
    }

    @ApiOperation(value = "获取列表", notes = "获取列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "查询关键字", dataType = "String", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "priority", value = "优先级", dataType = "Integer", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "String", paramType = "query", defaultValue = "")
    })
    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getList(String keyword, Integer priority, String status) {
        try {
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            return sysDefectService.getList(keyword, priority, status, super.getPageRequest(sort));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取缺陷记录列表失败！", e);
            return ApiResponseResult.failure("获取缺陷记录列表失败！");
        }
    }
}
