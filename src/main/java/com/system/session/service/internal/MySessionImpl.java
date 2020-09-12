package com.system.session.service.internal;

import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.session.service.MySessionService;


@Service(value = "mySessionService")
@Transactional(propagation = Propagation.REQUIRED)
public class MySessionImpl implements MySessionService {


	@Override
	public ApiResponseResult getlist(String keyword, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return ApiResponseResult.success();
	}
    
    
}
