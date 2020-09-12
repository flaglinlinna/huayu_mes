package com.system.router.service.internal;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.router.dao.SysRouterDao;
import com.system.router.entity.SysRouter;
import com.system.router.service.SysRouterService;
import com.system.user.entity.SysUser;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;


@Service(value = "sysRouterService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysRouterImpl implements SysRouterService {

    @Autowired
    private SysRouterDao sysRouterDao;

	@Override
	public ApiResponseResult add(SysRouter sysRouter) throws Exception {
		// TODO Auto-generated method stub
		SysUser currUser = UserUtil.getCurrUser();  //获取当前用户
		if(sysRouter.getId() == null || sysRouter.getId() <= 0){
			sysRouter.setCreatedTime(new Date());
//			sysRouter.setPkCreatedBy((currUser!=null) ? (currUser.getId()) : null);
			sysRouterDao.save(sysRouter);
		}else{
			SysRouter s = sysRouterDao.findById((long)sysRouter.getId());
			s.setRouterCode(sysRouter.getRouterCode());
			s.setRouterIndex(sysRouter.getRouterIndex());
			s.setRouterStatus(sysRouter.getRouterStatus());
			s.setRouterName(sysRouter.getRouterName());
			s.setModifiedTime(new Date());
//			s.setPkModifiedBy((currUser!=null) ? (currUser.getId()) : null);
			sysRouterDao.save(s);
		}
		
		
		return ApiResponseResult.success("操作成功！");
	}

	@Override
	public ApiResponseResult edite(SysRouter sysRouter) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApiResponseResult delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		 if(id == null){
	            return ApiResponseResult.failure("记录ID不能为空！");
	        }
		    SysRouter s = sysRouterDao.findById((long) id);
	        if(s == null){
	            return ApiResponseResult.failure("该资源不存在或已删除！");
	        }
	        SysUser currUser = UserUtil.getCurrUser();  //获取当前用户
	        s.setIsDel(BasicStateEnum.TRUE.intValue());
	        s.setModifiedTime(new Date());
//	        s.setPkModifiedBy((currUser!=null) ? (currUser.getId()) : null);
	        sysRouterDao.save(s);

	        return ApiResponseResult.success("删除成功！");
	}

	@Override
	public ApiResponseResult getlist(String rolecode, String rolename) throws Exception {
		// TODO Auto-generated method stub
		
		//1.精准查询
        List<SearchFilter> filters = new ArrayList<SearchFilter>();
        filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if(StringUtils.isNotEmpty(rolecode)){
			filters.add(new SearchFilter("routerCode", SearchFilter.Operator.LTE, rolecode));
		}
		if(StringUtils.isNotEmpty(rolename)){
			filters.add(new SearchFilter("routerName", SearchFilter.Operator.LTE, rolename));
		}
		//获取排序对象
		Sort sort = new Sort(Sort.Direction.DESC, "id");
		//创建分页对象
		PageRequest pageRequest = new PageRequest(0, 10, sort);
		//分页查询
		//List<SysRouter> list  = sysRoleDao.findAll(example, pageRequest).getContent();
		Specification<SysRouter> spec = Specification.where(BaseService.and(filters, SysRouter.class));
        Page<SysRouter> page = sysRouterDao.findAll(spec, pageRequest);
        //Page<SysRouter> page1 = sysRouterDao.findAll(pageRequest);

        return ApiResponseResult.success().data(page.getContent());
		//return null;
	}

	@Override
	public ApiResponseResult getRolesByUserId(long userId) throws Exception {
		// TODO Auto-generated method stub
		return ApiResponseResult.success().data(sysRouterDao.getRolesByUserId(userId));
	}

	@Override
	public ApiResponseResult getTreeList() throws Exception {
		// TODO Auto-generated method stub
		List<SysRouter> list = sysRouterDao.findByIsDelAndRouterStatusOrderByRouterIndexAsc(0, 0);//所有数据
		List<Map<String, Object>> resultList = this.getTreeList(list);
		System.out.println(resultList);
      //return resultList;
		return ApiResponseResult.success().data(resultList);
	}
	
	private List<Map<String, Object>>  getTreeList(List<SysRouter> list){
		List<Map<String, Object>> newTrees = new ArrayList<Map<String, Object>>();
		if(list.size() > 0){
			for(int i = 0;i<list.size();i++){
				if(list.get(i).getParentId().equals(1L)){
					Map m = new HashMap();
					m.put("id", list.get(i).getId());
					m.put("routerName", list.get(i).getRouterName());
					m.put("routerComment", list.get(i).getRouterComment());
					m.put("routerCode", list.get(i).getRouterCode());
					m.put("routerStatus", list.get(i).getRouterStatus());
					m.put("routerIndex", list.get(i).getRouterIndex());
					m.put("children", this.getChildren(list, list.get(i).getId()));
					newTrees.add(m);
				}
			}
		}
		return newTrees;
	}
	
	/**
	 * 无限递归菜单tree
	 * @param list
	 * @param pid
	 * @return
	 */
	private List<Map<String, Object>> getChildren(List<SysRouter> list,Long pid){
		List<Map<String, Object>> newTrees = new ArrayList<Map<String, Object>>();
		if(list.size() > 0){
			for(int i = 0;i<list.size();i++){
				if(list.get(i).getParentId().equals(pid)){
					Map m = new HashMap();
					m.put("id", list.get(i).getId());
					m.put("routerName", list.get(i).getRouterName());
					m.put("routerComment", list.get(i).getRouterComment());
					m.put("routerCode", list.get(i).getRouterCode());
					m.put("routerStatus", list.get(i).getRouterStatus());
					m.put("routerIndex", list.get(i).getRouterIndex());
					m.put("children", this.getChildren(list, list.get(i).getId()));
					newTrees.add(m);
				}
			}
		}
		return newTrees;
	}

	@Override
	public ApiResponseResult getRouterTree(String rolecode) throws Exception {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = sysRouterDao.getRouterTree(rolecode);//所有数据
		List<Map<String, Object>> resultList = this.getRoleTreeList(list);
		System.out.println(resultList);
      //return resultList;
		return ApiResponseResult.success().data(resultList);
	}
	private List<Map<String, Object>>  getRoleTreeList(List<Map<String, Object>> list){
		List<Map<String, Object>> newTrees = new ArrayList<Map<String, Object>>();
		if(list.size() > 0){
			for(int i = 0;i<list.size();i++){
				//System.out.println(list.get(i).get("router_id"));
				if(list.get(i).get("parent_id").toString().equals("1")){
					Map m = new HashMap();
					m.put("id", list.get(i).get("id").toString());
					m.put("field", list.get(i).get("router_code").toString());
					m.put("title", list.get(i).get("router_name").toString());
					m.put("spread", true);
					m.put("checked",false);//第一级统一设置成false，子节点有的话  会默认勾上的 不需要设置成true否则子节点会全部选中
					m.put("children", this.getRoleChildren(list, list.get(i).get("id").toString()));
					newTrees.add(m);
				}
				
				/*if(list.get(i).getParentId().equals(1L)){
					Map m = new HashMap();
					m.put("id", list.get(i).getId());
					m.put("routerName", list.get(i).getRouterName());
					m.put("routerComment", list.get(i).getRouterComment());
					m.put("routerCode", list.get(i).getRouterCode());
					m.put("routerStatus", list.get(i).getRouterStatus());
					m.put("routerIndex", list.get(i).getRouterIndex());
					m.put("children", this.getChildren(list, list.get(i).getId()));
					newTrees.add(m);
				}*/
			}
		}
		return newTrees;
	}
	private List<Map<String, Object>> getRoleChildren(List<Map<String, Object>> list,String pid){
		List<Map<String, Object>> newTrees = new ArrayList<Map<String, Object>>();
		if(list.size() > 0){
			for(int i = 0;i<list.size();i++){
				if(list.get(i).get("parent_id").toString().equals(pid)){
					Map m = new HashMap();
					m.put("id", list.get(i).get("id").toString());
					m.put("field", list.get(i).get("router_code").toString());
					m.put("title", list.get(i).get("router_name").toString());
					m.put("spread", true);
					m.put("checked", list.get(i).get("router_id").toString().equals("0")?false:true);
					m.put("children", this.getRoleChildren(list, list.get(i).get("id").toString()));
					newTrees.add(m);
				}
			}
		}
		return newTrees;
	}

}
