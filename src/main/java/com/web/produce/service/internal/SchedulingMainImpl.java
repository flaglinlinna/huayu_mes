package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.entity.SysUser;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.produce.dao.SchedulingMainDao;
import com.web.produce.entity.SchedulingMain;
import com.web.produce.service.SchedulingMainService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.*;

/**
 * 排产信息 主
 */
@Service(value = "SchedulingMainService")
@Transactional(propagation = Propagation.REQUIRED)
public class SchedulingMainImpl implements SchedulingMainService {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SchedulingMainDao schedulingMainDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    @Transactional
    public ApiResponseResult add(SchedulingMain schedulingMain) throws Exception {
        if(schedulingMain == null){
            return ApiResponseResult.failure("排产信息不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();
        schedulingMain.setIdNo(schedulingMainDao.getBillCode(3));
        schedulingMain.setCreateDate(new Date());
        schedulingMain.setCreateBy(currUser!=null ? currUser.getId() : null);
        schedulingMainDao.save(schedulingMain);

        return ApiResponseResult.success("新增成功！").data(schedulingMain);
    }

    @Override
    @Transactional
    public ApiResponseResult edit(SchedulingMain schedulingMain) throws Exception {
        if(schedulingMain == null && schedulingMain.getId() == null){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        SchedulingMain o = schedulingMainDao.findById((long) schedulingMain.getId());
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser!=null ? currUser.getId() : null);
        return ApiResponseResult.success("编辑成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
        if(id == null){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        SchedulingMain o = schedulingMainDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setDelTime(new Date());
        o.setDelBy(currUser!=null ? currUser.getId() : null);
        o.setDelFlag(1);
        schedulingMainDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult getDeptSelect() throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getDeptSelect(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                "","排产导入", "prc_mes_cof_org_chs");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
//        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
//        return null;
//        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    //获取上线人员清单 存储过程调用
    public List getDeptSelect(String facoty, String company, String mid, String keyword,
                               String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, mid);
                cs.setString(4, keyword);
                cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
//                cs.registerOutParameter(7, java.sql.Types.INTEGER);// 输出参数 总记录数
                cs.registerOutParameter(7, -10);// 输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(5));
                result.add(cs.getString(6));
                if (cs.getString(5).toString().equals("0")) {
                    result.add(cs.getString(7));
                    // 游标处理
                    ResultSet rs = (ResultSet) cs.getObject(7);

                    try {
                        l = fitMap(rs);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    result.add(l);
                }
                System.out.println(l);
                return result;
            }

        });
        return resultList;
    }

    private List<Map<String, Object>> fitMap(ResultSet rs) throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        if (null != rs) {
            Map<String, Object> map;
            int colNum = rs.getMetaData().getColumnCount();
            List<String> columnNames = new ArrayList<String>();
            for (int i = 1; i <= colNum; i++) {
                columnNames.add(rs.getMetaData().getColumnName(i));
            }
            while (rs.next()) {
                map = new HashMap<String, Object>();
                for (String columnName : columnNames) {
                    map.put(columnName, rs.getString(columnName));
                }
                list.add(map);
            }
        }
        return list;
    }


    @Override
    @Transactional
    public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        //查询条件2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("prodName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<SchedulingMain> spec = Specification.where(BaseService.and(filters, SchedulingMain.class));
        Specification<SchedulingMain> spec1 =  spec.and(BaseService.or(filters1, SchedulingMain.class));
        Page<SchedulingMain> page = schedulingMainDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }


}
