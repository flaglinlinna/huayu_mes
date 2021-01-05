package com.system.check.service;

import com.app.base.data.ApiResponseResult;
import com.system.check.entity.CheckInfo;

/**
 * 审批
 * @author fyx
 *
 */
public interface CheckService {

	public boolean checkFirst(Long id, String checkCode) throws Exception;

	public boolean addCheckFirst(CheckInfo checkInfoSrm) throws Exception;

	public ApiResponseResult getInfo(Long id, String checkCode) throws Exception;

	public boolean doCheck(CheckInfo checkInfoSrm) throws Exception;

	public boolean checkSecond(Long id, String checkCode) throws Exception;

	public ApiResponseResult getCheckByRecordId(Long id) throws Exception;

	public ApiResponseResult getUnCheckList() throws Exception;
	
	public ApiResponseResult doCheckQuote(CheckInfo checkInfoSrm) throws Exception;

}
