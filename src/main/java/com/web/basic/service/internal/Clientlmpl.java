package com.web.basic.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.ClientDao;
import com.web.basic.entity.Client;
import com.web.basic.service.ClientService;


@Service(value = "clientService")
@Transactional(propagation = Propagation.REQUIRED)
public class Clientlmpl implements ClientService {
	@Autowired
    private ClientDao clientDao;
	
	 /**
     * 新增客户
     */
    @Override
    @Transactional
    public ApiResponseResult add(Client client) throws Exception{
        if(client == null){
            return ApiResponseResult.failure("客户不能为空！");
        }
        if(StringUtils.isEmpty(client.getCustNo())){
            return ApiResponseResult.failure("客户编码不能为空！");
        }
        if(StringUtils.isEmpty(client.getCustName())){
            return ApiResponseResult.failure("客户名称不能为空！");
        }
        int count = clientDao.countByDelFlagAndCustNo(0, client.getCustNo());
        if(count > 0){
            return ApiResponseResult.failure("该客户已存在，请填写其他客户编码！");
        }
        client.setCreateDate(new Date());
        clientDao.save(client);

        return ApiResponseResult.success("客户添加成功！").data(client);
    }
    /**
     * 修改客户
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Client client) throws Exception {
        if(client == null){
            return ApiResponseResult.failure("客户不能为空！");
        }
        if(client.getId() == null){
            return ApiResponseResult.failure("客户ID不能为空！");
        }
        if(StringUtils.isEmpty(client.getCustNo())){
            return ApiResponseResult.failure("客户编码不能为空！");
        }
        if(StringUtils.isEmpty(client.getCustName())){
            return ApiResponseResult.failure("客户名称不能为空！");
        }
        Client o = clientDao.findById((long) client.getId());
        if(o == null){
            return ApiResponseResult.failure("该客户不存在！");
        }
        //判断客户编码是否有变化，有则修改；没有则不修改
        if(o.getCustNo().equals(client.getCustNo())){
        }else{
            int count = clientDao.countByDelFlagAndCustNo(0, client.getCustNo());
            if(count > 0){
                return ApiResponseResult.failure("客户编码已存在，请填写其他客户编码！");
            }
            o.setCustNo(client.getCustNo().trim());
        }
        o.setLastupdateDate(new Date());
        o.setCustName(client.getCustName());
        o.setCustNameS(client.getCustNameS());
        clientDao.save(o);
        return ApiResponseResult.success("编辑成功！");
	}
    
    /**
     * 根据ID获取
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getClient(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("客户ID不能为空！");
        }
        Client o = clientDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该客户不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除客户
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("客户ID不能为空！");
        }
        Client o  = clientDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("客户不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setDelFlag(1);
        clientDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }
     
    /**
     * 查询列表
     */
	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("custNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("custName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("custNameS", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Client> spec = Specification.where(BaseService.and(filters, Client.class));
				Specification<Client> spec1 = spec.and(BaseService.or(filters1, Client.class));
				Page<Client> page = clientDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	
}
