package com.web.basic.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.role.entity.RolePermissionMap;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.ClientProcessMapDao;
import com.web.basic.dao.ClientDao;
import com.web.basic.dao.ProcessDao;
import com.web.basic.entity.ClientProcessMap;
import com.web.basic.entity.Client;
import com.web.basic.entity.Process;
import com.web.basic.service.ClientProcessMapService;

/**
 * 客户通用工艺流程维护
 *
 */
@Service(value = "clientProcessMapService")
@Transactional(propagation = Propagation.REQUIRED)
public class ClientProcessMaplmpl implements ClientProcessMapService{

	@Autowired
	ClientProcessMapDao clientProcessMapDao;
	@Autowired
	ProcessDao processDao;
	@Autowired
	ClientDao clientDao;
	
	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
				}
				Specification<ClientProcessMap> spec = Specification.where(BaseService.and(filters, ClientProcessMap.class));
				Specification<ClientProcessMap> spec1 = spec.and(BaseService.or(filters1, ClientProcessMap.class));
				Page<ClientProcessMap> page = clientProcessMapDao.findAll(spec1, pageRequest);

				List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
				for(ClientProcessMap bs:page.getContent()){
					Map<String, Object> map = new HashMap<>();
					map.put("pClientCode", bs.getClient().getBsCode());//获取关联表的数据-客户表
					map.put("pClientName", bs.getClient().getBsName());//客户名
					
					map.put("pProcCode", bs.getProcess().getBsCode());//工序表
					map.put("pProcName", bs.getProcess().getBsName());//工序名
					
					map.put("id", bs.getId());
					map.put("modifiedTime",bs.getLastupdateDate());
					map.put("createdTime", bs.getCreateDate());
					list.add(map);
				}
				
				return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	/*
	 * 增加工艺流程
	 * **/
	@Override
    @Transactional
    public ApiResponseResult addItem(String procIdList,Long clientId) throws Exception{
        
		if(clientId == null){
            return ApiResponseResult.failure("物料数据不能为空！");
        }
        //转换
        String[] porcIdArray = procIdList.split(";");
        List<Long> procList = new ArrayList<Long>();
        for(int i = 0; i < porcIdArray.length; i++){
            if(StringUtils.isNotEmpty(porcIdArray[i])) {
            	procList.add(Long.parseLong(porcIdArray[i]));
            }
        }
      //1.删除物料原工序信息
        List<ClientProcessMap> listOld = clientProcessMapDao.findByDelFlagAndPkClient(0, clientId);
        if(listOld.size() > 0){
            for(ClientProcessMap item : listOld){
                item.setLastupdateDate(new Date());
                item.setDelFlag(1);
            }
            clientProcessMapDao.saveAll(listOld);
        }
      //2.添加物料新工序信息
        List<ClientProcessMap> listNew = new ArrayList<>();
        if(procList.size() > 0){
            for(Long procId : procList){
            	ClientProcessMap item = new ClientProcessMap();
                item.setCreateDate(new Date());
                item.setPkClient(clientId);
                item.setPkProcess(procId);
                listNew.add(item);
            }
            clientProcessMapDao.saveAll(listNew);
        }

        return ApiResponseResult.success("工艺流程添加成功！");
    }
	
	/**
     * 客户ID获取原来工序记录
     */
	public ApiResponseResult getClientItem(Long id) throws Exception{
		 List<ClientProcessMap> list = clientProcessMapDao.findByDelFlagAndPkClient(0, id);
		return ApiResponseResult.success().data(list);
	}
	
	/**
	 * 删除工序记录
	 * */
	@Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("ID不能为空！");
        }
        ClientProcessMap o  = clientProcessMapDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setDelFlag(1);
        clientProcessMapDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }
	
	/**
     * 获取物料数据，工序数据
     */
	@Override
    @Transactional
	public ApiResponseResult getProcList() throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Process> pList = processDao.findByDelFlagAndBsStatus(0,0);
		List<Client> cList = clientDao.findByDelFlag(0);
		
		map.put("process", pList);
		map.put("client", cList);
		return ApiResponseResult.success().data(map);
	}
}
