package com.web.basic.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
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
import com.web.basic.dao.LineDao;
import com.web.basic.entity.Line;
import com.web.basic.service.LineService;

import provider.BaseOprService;
import provider.Parameter;
import provider.SQLParameter;


@Service(value = "lineService")
@Transactional(propagation = Propagation.REQUIRED)
public class Linelmpl extends BaseSql  implements LineService {
	@Autowired
    private LineDao lineDao;
	
	
	 /**
     * 新增线体
     */
    @Override
    @Transactional
    public ApiResponseResult add(Line line) throws Exception{
        if(line == null){
            return ApiResponseResult.failure("线体不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineNo())){
            return ApiResponseResult.failure("线体编号不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineName())){
            return ApiResponseResult.failure("线体名称不能为空！");
        }
        int count = lineDao.countByDelFlagAndLineNo(0, line.getLineNo());
        if(count > 0){
            return ApiResponseResult.failure("该线体已存在，请填写其他线体编号！");
        }
        line.setCreateDate(new Date());
        line.setCreateBy(UserUtil.getSessionUser().getId());
        lineDao.save(line);

        return ApiResponseResult.success("线体添加成功！").data(line);
    }
    /**
     * 修改线体
     */
    @Override
    @Transactional
    public ApiResponseResult edit(Line line) throws Exception {
        if(line == null){
            return ApiResponseResult.failure("线体不能为空！");
        }
        if(line.getId() == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineNo())){
            return ApiResponseResult.failure("线体编号不能为空！");
        }
        if(StringUtils.isEmpty(line.getLineName())){
            return ApiResponseResult.failure("线体名称不能为空！");
        }
        Line o = lineDao.findById((long) line.getId());
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        //判断线体编号是否有变化，有则修改；没有则不修改
        if(o.getLineNo().equals(line.getLineNo())){
        }else{
            int count = lineDao.countByDelFlagAndLineNo(0, line.getLineNo());
            if(count > 0){
                return ApiResponseResult.failure("线体编号已存在，请填写其他线体编号！");
            }
            o.setLineNo(line.getLineNo().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setLineName(line.getLineName());
        o.setLinerCode(line.getLinerCode());
        o.setLinerName(line.getLinerName());
        lineDao.save(o);
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
    public ApiResponseResult getLine(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        Line o = lineDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除线体
     */
    @Override
    @Transactional
    public ApiResponseResult delete(String ids) throws Exception{
        if(StringUtils.isEmpty(ids)){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        String[] id_s = ids.split(",");
        List<Line> ll = new ArrayList<Line>();
        for(String id:id_s){
        	Line o  = lineDao.findById(Long.parseLong(id));
            if(o != null){
            	o.setDelTime(new Date());
                o.setDelFlag(1);
                o.setDelBy(UserUtil.getSessionUser().getId());
                ll.add(o);
            }
        }
        lineDao.saveAll(ll);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        Line o = lineDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("线体不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setCheckStatus(checkStatus);
        lineDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
    }

    /**
     * 查询列表
     */
    @Override
    @Transactional
	public ApiResponseResult getList(String keyword,String lineNo,String linerName,String lastupdateDate,
    		String checkStatus,String createDate,String linerCode,String lineName, PageRequest pageRequest) throws Exception {
    			String hql = "select a.* from "+Line.TABLE_NAME+" a" + " where 1=1 and del_Flag=0  ";
    			
    			//SQLParameter<Parameter> params = SQLParameter.newInstance(Parameter.class);
    			//params.add(Parameter.build("bsIsDel", 0));// 删除标识
    			
    			if (StringUtils.isNotEmpty(keyword)) {
    				//hql += "and ( CONCAT(a.line_No,a.line_Name,a.liner_Code,a.liner_Name) like '%:keyword%') ";
    				//params.add(Parameter.build("keyword", keyword));
    				hql += "  and INSTR((a.line_No || a.line_Name || a.liner_Code || a.liner_Name ),  '"
    						+ keyword + "') > 0 ";
    			}
    			//lineNo--in查询类型
    			/*if (StringUtils.isNotEmpty(lineNo)) {
    				String[] lineNos = lineNo.split(",");
    				String lines = "";
    				for(String line:lineNos){
    					if(StringUtils.isNotEmpty(line)){
    						lines += "'"+line+"',";
    					}
    				}
    				if(StringUtils.isNotEmpty(lines)){
    					lines = lines.substring(0, lines.length() - 1);
    				}
    				hql += " and a.lineNo in ("+lines+")";
    			}*/
    			//linerName,linerCode,lineName--模糊搜索类型
    			if (StringUtils.isNotEmpty(lineNo)) {
    				hql += " and a.line_No like '%"+lineNo+"%'";
    			}
    			if (StringUtils.isNotEmpty(linerName)) {
    				hql += " and a.liner_Name like '%"+linerName+"%'";
    			}
    			if (StringUtils.isNotEmpty(linerCode)) {
    				hql += " and a.liner_Code like '%"+linerCode+"%'";
    			}
    			if (StringUtils.isNotEmpty(lineName)) {
    				hql += " and a.line_Name like '%"+lineName+"%'";
    			}
    			//createDate,lastupdateDate--日期类型
    			if(StringUtils.isNotEmpty(createDate)){
    				String[] dates = createDate.split(" - ");
    				hql += " and to_char(a.create_Date,'yyyy-MM-dd') >= '"+dates[0]+"'";
    				hql += " and to_char(a.create_Date,'yyyy-MM-dd') <= '"+dates[1]+"'";
    			}
    			if(StringUtils.isNotEmpty(lastupdateDate)){
    				String[] dates = lastupdateDate.split(" - ");
    				hql += " and to_char(a.lastupdate_Date,'yyyy-MM-dd') >= '"+dates[0]+"'";
    				hql += " and to_char(a.lastupdate_Date,'yyyy-MM-dd') <= '"+dates[1]+"'";
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
    			
    			//List<Map<String, Object>> list = super.findBySql(sql, param);
    			List<Line> list = createSQLQuery(sql, param, Line.class);
    			long count = createSQLQuery(hql, param, null).size();
    			
    			
    			return ApiResponseResult.success().data(DataGrid.create(list, (int) count,
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize())); 
	}
    
    
    @Transactional
	public ApiResponseResult getList_bak(String keyword,String lineNo,String linerName,String lastupdateDate,
    		String checkStatus,String createDate,String linerCode,String lineName, PageRequest pageRequest) throws Exception {
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
					filters1.add(new SearchFilter("lineNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("lineName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("linerCode", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("linerName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<Line> spec = Specification.where(BaseService.and(filters, Line.class));
				Specification<Line> spec1 = spec.and(BaseService.or(filters1, Line.class));
				Page<Line> page = lineDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


}
