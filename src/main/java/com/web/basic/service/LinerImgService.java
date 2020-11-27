package com.web.basic.service;


import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.LinerImg;
import org.springframework.data.domain.PageRequest;

public interface LinerImgService {

    public ApiResponseResult getDeptInfo(String keyword) throws Exception;

	public ApiResponseResult getEmpCode(String keyword,PageRequest pageRequest) throws Exception;

	//获取生产线
	public ApiResponseResult getLine() throws Exception;

    public ApiResponseResult add(LinerImg linerImg) throws Exception;

	public ApiResponseResult edit(LinerImg linerImg) throws Exception;

	public ApiResponseResult getList(String keyword,String sdate,String edate,PageRequest pageRequest) throws Exception;

	// 根据ID获取
	public ApiResponseResult getLinerImg(Long id) throws Exception;

	public ApiResponseResult delete(String ids) throws Exception;

	public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception;// 状态改变


}
