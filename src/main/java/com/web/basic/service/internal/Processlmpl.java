package com.web.basic.service.internal;

import java.util.*;

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
import com.utils.BaseSql;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.ProcessDao;
import com.web.basic.entity.Line;
import com.web.basic.entity.Process;
import com.web.basic.service.ProcessService;
import provider.BaseOprService;


@Service(value = "processService")
@Transactional(propagation = Propagation.REQUIRED)
public class Processlmpl extends BaseSql implements ProcessService {
	@Autowired
    private ProcessDao processDao;

	 /**
     * 新增工序
     */
    @Override
    @Transactional
    public ApiResponseResult add(Process process) throws Exception{
        if(process == null){
            return ApiResponseResult.failure("工序不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcNo())){
            return ApiResponseResult.failure("工序编码不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcName())){
            return ApiResponseResult.failure("工序名称不能为空！");
        }
        int count = processDao.countByDelFlagAndProcNo(0, process.getProcNo());
        if(count > 0){
            return ApiResponseResult.failure("该工序已存在，请填写其他工序编码！");
        }
        process.setCreateDate(new Date());
        process.setCreateBy(UserUtil.getSessionUser().getId());
        processDao.save(process);

        return ApiResponseResult.success("工序添加成功！").data(process);
    }
    /**
     * 修改工序
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Process process) throws Exception {
        if(process == null){
            return ApiResponseResult.failure("工序不能为空！");
        }
        if(process.getId() == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcNo())){
            return ApiResponseResult.failure("工序编码不能为空！");
        }
        if(StringUtils.isEmpty(process.getProcName())){
            return ApiResponseResult.failure("工序名称不能为空！");
        }
        Process o = processDao.findById((long) process.getId());
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        //判断工序编码是否有变化，有则修改；没有则不修改
        if(o.getProcNo().equals(process.getProcNo())){
        }else{
            int count = processDao.countByDelFlagAndProcNo(0, process.getProcNo());
            if(count > 0){
                return ApiResponseResult.failure("工序编码已存在，请填写其他工序编码！");
            }
            o.setProcNo(process.getProcNo().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setProcName(process.getProcName());
        o.setProcOrder(process.getProcOrder());
        processDao.save(o);
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
    public ApiResponseResult getProcess(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        Process o = processDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除工序
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        Process o  = processDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        processDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Process o = processDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setCheckStatus(checkStatus);
        processDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
    }

    /**
     * 查询列表
     */
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,String procNo,String procName,String procOrder,
                                     String checkStatus,String createDate,String lastupdateDate, PageRequest pageRequest) throws Exception {
//        String hql = "select new Process(a.procNo, a.procName, a.procOrder, "
//                + "		a.checkStatus,a.createDate, a.lastupdateDate,	 "
//                + "			 a.id  "
//                + "			) from Process a" + " where 1=1 and delFlag=:bsIsDel  ";

        String hql = "select a.* from "+Process.TABLE_NAME+" a" + " where 1=1 and del_Flag=0  ";
//        SQLParameter<Parameter> params = SQLParameter.newInstance(Parameter.class);
//        params.add(Parameter.build("bsIsDel", 0));// 删除标识
        if (StringUtils.isNotEmpty(keyword)) {
            hql += "  and INSTR((a.proc_No || a.proc_Name || a.proc_Order),  '"
                    + keyword + "') > 0 ";
        }
        if (StringUtils.isNotEmpty(procNo)) {
            hql += " and a.proc_No like '%"+procNo+"%'";
        }
        if (StringUtils.isNotEmpty(procName)) {
            hql += " and a.proc_Name like '%"+procName+"%'";
        }
        if (StringUtils.isNotEmpty(procOrder)) {
            hql += " and a.proc_Order like '%"+procOrder+"%'";
        }
        //createDate,lastupdateDate--日期类型
        if(StringUtils.isNotEmpty(createDate)){
            String[] dates = createDate.split(" - ");
            hql += " and to_char(a.createDate,'yyyy-MM-dd') >= '"+dates[0]+"'";
            hql += " and to_char(a.createDate,'yyyy-MM-dd') <= '"+dates[1]+"'";
        }
        if(StringUtils.isNotEmpty(lastupdateDate)){
            String[] dates = lastupdateDate.split(" - ");
            hql += " and to_char(a.lastupdateDate,'yyyy-MM-dd') >= '"+dates[0]+"'";
            hql += " and to_char(a.lastupdateDate,'yyyy-MM-dd') <= '"+dates[1]+"'";
        }
        //checkStatus--需要转移的类型
        if(StringUtils.isNotEmpty(checkStatus)){
            if(checkStatus.equals("禁用")){
                hql += " and a.check_Status =0 ";
            }else{
                hql += " and a.check_Status =1 ";
            }
        }
        int pn = pageRequest.getPageNumber() + 1;
        String sql = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM ( " + hql + " ) A  WHERE ROWNUM <= ("
                + pn + ")*" + pageRequest.getPageSize() + "  )  WHERE RN > (" + pageRequest.getPageNumber() + ")*"
                + pageRequest.getPageSize() + " ";

        Map<String, Object> param = new HashMap<String, Object>();
        List<Process> list = createSQLQuery(sql, param, Process.class);
		long count = createSQLQuery(hql, param, null).size();
		
        return ApiResponseResult.success().data(DataGrid.create(list, (int) count,
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 查询列表
     */
	@Override
    @Transactional
	public ApiResponseResult getList_bak(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("procNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
				
				}
				Specification<Process> spec = Specification.where(BaseService.and(filters, Process.class));
				Specification<Process> spec1 = spec.and(BaseService.or(filters1, Process.class));
				Page<Process> page = processDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
