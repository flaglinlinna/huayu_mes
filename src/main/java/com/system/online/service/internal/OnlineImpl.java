package com.system.online.service.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.online.entity.OnlineUser;
import com.system.online.service.OnlineService;
import com.system.user.entity.SysUser;


@Service(value = "onlineService")
@Transactional(propagation = Propagation.REQUIRED)
public class OnlineImpl implements OnlineService {

    @Autowired
    private EnterpriseCacheSessionDAO enterCacheSessionDAO;

    /**
     * 获取在线用户列表
     * @param keyword
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getlist(String keyword, PageRequest pageRequest) throws Exception{
    	 //获取当前在线session
    	  Collection<Session> sessions = enterCacheSessionDAO.getActiveSessions();
    	  
    	  System.out.println(sessions.size());
    	  List<OnlineUser> list = new ArrayList<>();
    	  Iterator<Session> it = sessions.iterator() ;

    	  while(it.hasNext()) {
    		  try{
    			  Session session = it.next() ;
        		  if (session == null) continue;
        		  SysUser user = new SysUser();
        		  OnlineUser o = new OnlineUser();
        		  //principalCollection 身份
        		  SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
        		  if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
        			  continue;
        		  } else {
        			  principalCollection = (SimplePrincipalCollection) session
        					  .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        			  user = (SysUser) principalCollection.getPrimaryPrincipal();
        			  boolean flag = true;//根据查询条件判断是否需要添加到列表
        			  if(StringUtils.isBlank(keyword)){
        				  flag = true;
        			  }else{
        				  int result1 = user.getUserName().indexOf(keyword);
        				  int result2 = user.getMobile().indexOf(keyword);
//                          int result1 = user.getFcode().indexOf(keyword);
//                          int result2 = user.getFname().indexOf(keyword);
        				  if((result1 == -1) &&(result2 == -1)){
        					  flag = false;
        				  }else{
        					  flag = true;
        				  }
        			  }
        			  if(flag){
        				  o.setBsCode(user.getUserCode());
            			  o.setBsName(user.getUserName());
            			  o.setId(user.getId());
            			  o.setMobile(user.getMobile());
//                          o.setBsCode(user.getFcode());
//                          o.setBsName(user.getFname());
//                          o.setId(Long.parseLong(user.getFid()));
//                          o.setMobile(user.getFcode());
            			  
            			  o.setSessionId(session.getId().toString());
            			  o.setHost(session.getHost());
            			  o.setLastAccessTime(session.getLastAccessTime());
            			  o.setStartTimestamp(session.getStartTimestamp());
            			  list.add(o);
        			  }
        			  
        		  }
        		  
        		  
    		  }catch(Exception e){
    			  continue;
    		  }
    		  
    	  }
    	  DataGrid s = DataGrid.create(list, (int)list.size(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize());
          return ApiResponseResult.success().data(DataGrid.create(list, (int)list.size(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

	@Override
	public ApiResponseResult delete(String sessionId) throws Exception {
		// TODO Auto-generated method stub
		//通过readSession读取session，然后调用delete删除
		 Session session = enterCacheSessionDAO.readSession(sessionId);
		 enterCacheSessionDAO.delete(session);
		return ApiResponseResult.success();
	}
    
}
