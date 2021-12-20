package com.web.quote.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.Quote;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteService {

	public ApiResponseResult add(Quote quote)throws Exception;

	public ApiResponseResult copy(Quote quote)throws Exception;
	
	public ApiResponseResult getProdType()throws Exception;//获取产品类型

	public ApiResponseResult getMonBadeList(PageRequest pageRequest) throws Exception; //获取报价中标率列表
	
	public ApiResponseResult getList(String quoteId,String keyword,String status,String bsCode,String bsType,String bsStatus,
									 String bsFinishTime,String bsRemarks,String bsProd,String bsSimilarProd,
									 String bsPosition,String bsCustRequire,String bsLevel,String bsRequire,
									 String bsDevType,String bsCustName,String userName,PageRequest pageRequest)throws Exception;//获取报价单列表

	public ApiResponseResult edit(Quote quote)throws Exception;
	
	public ApiResponseResult getSingle(Long id)throws Exception;//获取单张报价单部分信息
	
	public ApiResponseResult getSingleAll(Long id)throws Exception;//获取单张报价单全部信息
	
	public ApiResponseResult getItemPage(Long id,String bsStyle)throws Exception;//获取待办项数据
	
	public ApiResponseResult getItemStatus(Long quoteId,String bsCode)throws Exception;//获取项目的当前状态
	
	public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;//变更报价单状态

	public ApiResponseResult doBsBade(Long id, Integer bsBade) throws Exception;//变更报价单中标状态

	public ApiResponseResult findUserName(Long usr_id)throws Exception;//获取用户信息
	
	public ApiResponseResult doItemFinish(String code,Long quoteId)throws Exception;//20201219-fyx-确认完成后要调用
	
	public ApiResponseResult doCheckProfit(String bsDevType,String bsProdType)throws Exception;//20201223-校验是否维护了利润

	public ApiResponseResult getOutStatus(Long id) throws Exception;//外协填写审批状态

	public ApiResponseResult getStatus(Long id) throws Exception;//制造部填写审批状态

	public ApiResponseResult getStatus2(Long id) throws Exception;//制造部填写审批状态

	public ApiResponseResult getStatus3(Long id) throws Exception;//业务填写审批状态

	public ApiResponseResult getFreightStatus(Long id) throws Exception; //获取包装运输费填写审批状态


	//去除审批逻辑，改为直接下发到制造部。 根据已存在的工艺和材料进行更新，不存在的进行新增
	public ApiResponseResult doPush(Long id) throws Exception;
}
