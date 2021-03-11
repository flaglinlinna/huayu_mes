package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.produce.dao.CardDataDao;
import com.web.produce.dao.SchedulingDetDao;
import com.web.produce.dao.SchedulingMainDao;
import com.web.produce.entity.SchedulingDet;
import com.web.produce.entity.SchedulingMain;
import com.web.produce.service.SchedulingMainService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.*;
import java.text.NumberFormat;
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
    @Autowired
    private SchedulingDetDao schedulingDetDao;
    @Autowired
    private CardDataDao cardDataDao;

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
        if(schedulingMain.getFenable() == 1){
            schedulingMain.setFenableBy(currUser!=null ? currUser.getUserName() : null);
            schedulingMain.setFenableDate(new Date());
        }
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
        o.setProdDate(schedulingMain.getProdDate());
        o.setDeptName(schedulingMain.getDeptName());
        o.setDeptId(schedulingMain.getDeptId());
        o.setClassName(schedulingMain.getClassName());
        o.setFenable(schedulingMain.getFenable());
        if(schedulingMain.getFenable() == 1){
            o.setFenableBy(currUser!=null ? currUser.getUserName() : null);
            o.setFenableDate(new Date());
        }
        return ApiResponseResult.success("编辑成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult get(Long id) throws Exception {
        SchedulingMain o = schedulingMainDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
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
        Integer num = schedulingDetDao.countByMidAndDelFlag(id,0);
        if(num>0){
            return ApiResponseResult.failure("子表存在记录，不能删除！");
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
        map.put("Class", cardDataDao.queryClass());//班次信息
        return ApiResponseResult.success("").data(map);
//        return null;
//        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    @Override
    @Transactional
    public ApiResponseResult getOrgSelect() throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getDeptSelect(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                "","组长", "prc_mes_cof_org_chs");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("rows", list.get(3));

        return ApiResponseResult.success("").data(map);
  }

    @Override
    @Transactional
    public ApiResponseResult getItemSelect(String keyword,PageRequest pageRequest) throws Exception {
        List<Object> list = getSchedulingItemPrc(UserUtil.getSessionUser().getCompany()+"",
                UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","成品",keyword,pageRequest);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
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
                cs.registerOutParameter(5, Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(6, Types.VARCHAR);// 输出参数 返回标识
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

    /**
     * 根据ID获取排产信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getSchedulingMain(String id) throws Exception{
        if(StringUtils.isEmpty(id)){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        //获取排产信息
        SchedulingMain o = schedulingMainDao.findById((long) Long.parseLong(id));
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }

        ApiResponseResult result = this.getDeptSelect();

        Map<String, Object> map = new HashMap<>();
        map.put("schedulingMain", o);
        map.put("deptList", result!=null ? result.getData() : null);

        return ApiResponseResult.success().data(map);
    }

    @Override
    @Transactional
    public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        List<Object> list = this.getSchedulingMainPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"", UserUtil.getSessionUser().getId()+"",
                "", "", keyword, pageRequest.getPageNumber()+1, pageRequest.getPageSize(), "prc_mes_get_prod_order_imp_M");

        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    //获取物料列表
    public List getSchedulingItemPrc(String company,String facoty,String user_id, String type,String keyword,PageRequest pageRequst) throws Exception {
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  prc_mes_cof_item_no_chs(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, type);
                cs.setString(5, keyword);
                cs.setInt(6, pageRequst.getPageSize());//每页指定有多少元素
                cs.setInt(7, pageRequst.getPageNumber()+1);//获取当前页码
                cs.registerOutParameter(8, Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(9, Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(10, Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(11, -10);// 输出参数 追溯数据
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(8));
                result.add(cs.getString(9));
                result.add(cs.getInt(10));
                if (cs.getString(8).toString().equals("0")) {
                    // 游标处理
                    ResultSet rs = (ResultSet) cs.getObject(11);
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



    //获取排产导入列表
    public List getSchedulingMainPrc(String factoty, String company, String user_id, String startTime, String endTime, String keyword,
                                     int page, int rows, String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, factoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, startTime);
                cs.setString(5, endTime);
                cs.setString(6, keyword);
                cs.setInt(7, rows);
                cs.setInt(8, page);
                cs.registerOutParameter(9, Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(10, Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(11, Types.INTEGER);// 输出参数 总记录数
                cs.registerOutParameter(12, -10);// 输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(9));
                result.add(cs.getString(10));
                if (cs.getString(9).toString().equals("0")) {
                    result.add(cs.getString(11));
                    // 游标处理
                    ResultSet rs = (ResultSet) cs.getObject(12);

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

    /**
     * 修改生效状态
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer status) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        if(status == null){
            return ApiResponseResult.failure("生效状态不能为空！");
        }
        SchedulingMain o = schedulingMainDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser!=null ? currUser.getId() : null);
        o.setFenable(status);
        if(status == 1){
            o.setFenableBy(currUser!=null ? currUser.getUserName() : null);
            o.setFenableDate(new Date());
        }
        schedulingMainDao.save(o);

        return ApiResponseResult.success("操作成功！");
    }

    /**
     * 获取导入指令单从表数据
     * @param keyword
     * @param startTime
     * @param endTime
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getDetList(String keyword, Long mid, String startTime, String endTime, PageRequest pageRequest) throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        Map map = new HashMap();
        if(mid == 0){
            map.put("total", 0);
            map.put("rows", "");
            return ApiResponseResult.success("").data(map);
        }
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        List<Object> list = this.getSchedulingDetPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"", UserUtil.getSessionUser().getId()+"",
                mid+"", startTime, endTime, keyword, pageRequest.getPageNumber()+1, pageRequest.getPageSize(), "prc_mes_get_prod_order_imp_det");

        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    //获取导入制令单列表
    public List getSchedulingDetPrc(String factoty, String company, String user_id, String mid, String startTime, String endTime, String keyword,
                                     int page, int rows, String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, factoty);
                cs.setString(2, company);
                cs.setString(3, mid);
                cs.setString(4, startTime);
                cs.setString(5, endTime);
                cs.setString(6, keyword);
                cs.setInt(7, rows);
                cs.setInt(8, page);
                cs.registerOutParameter(9, Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(10, Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(11, Types.INTEGER);// 输出参数 总记录数
                cs.registerOutParameter(12, -10);// 输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(9));
                result.add(cs.getString(10));
                if (cs.getString(9).toString().equals("0")) {
                    result.add(cs.getString(11));
                    // 游标处理
                    ResultSet rs = (ResultSet) cs.getObject(12);

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

    /**
     * 导入
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doExcel(MultipartFile file, Long mid) throws Exception{
        try{
            if (file == null) {
                return ApiResponseResult.failure("导入文件不存在！");
            }
            if(mid == null){
                return ApiResponseResult.failure("排产主表ID不能为空！请先保存主表信息！");
            }
            SysUser currUser = UserUtil.getSessionUser();
            if(currUser == null){
                return ApiResponseResult.failure("当前用户已失效，请重新登录！");
            }
            //1.获取Excel文件
            Workbook wb = null;
            Sheet sheet = null;
            String fileName = file.getOriginalFilename();
            //判断excel版本
            if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
                //xlsx版本
                wb = new XSSFWorkbook(file.getInputStream());
            } else {
                //xls版本
                //wb = new HSSFWorkbook(new FileInputStream((File) file));
                wb = new HSSFWorkbook(file.getInputStream());
            }
            //1.1获取第一个sheet
            if(wb != null){
                sheet = wb.getSheetAt(0);
            }else{
                return ApiResponseResult.failure("导入失败！请导入正确的文件！");
            }
            //1.2判断第一个sheet是否有内容
            if(sheet == null || sheet.getLastRowNum() <= 0){
                return ApiResponseResult.failure("导入失败！请导入正确的文件！");
            }

            //删除当前用户关联的临时表原数据
//            schedulingTempDao.updateDelFlagByCreateBy(1, currUser.getId());
            //初始化导入表
            List<SchedulingDet> detList = new ArrayList<>();

            for(int i = 0; i < sheet.getLastRowNum() + 1; i++){
                //初始化错误信息字符串
                String errorStr = "";

                int le = 2 + i;
                //2.获取当前行，如果为空，则循环结束
                Row row = sheet.getRow(le -1);
                if(row == null){
                    break;
                }
                //判断第一列数据是否为空，如果为空，则循环结束
                String firstCol = "";//
                try{
                    firstCol = this.readExcelCell(sheet, le, 1);//
                }catch (Exception e){
                }
                if(StringUtils.isEmpty(firstCol)){
                    break;
                }

                //3.判断各数值是否为空和是否填写错误
                //顺序:组合、分段顺序、备注、客户、组长、工单号、物料编码、物料描述、计划生产数量
                Integer groupNo = null;//组合
                String section = ""; //分段顺序
                String fmemo = "";//备注
                String custName = "";//客户名称
                String linerName = "";//线长名称
                String prodNo = "";//工单号
                String itemNo = "";//物料编码
                String itemName = "";//物料描述
                Integer qtyPlan = null;//计划数量


                //组合
                try{
                    String groupNostr = this.readExcelNumberCell(sheet, le, 1);
                    groupNo = Integer.parseInt(groupNostr);
                }catch (Exception e){
                }

                //工段
                try{
                    section = this.readExcelCell(sheet, le, 2);
                }catch (Exception e){
                }

                //备注
                try{
                    fmemo = this.readExcelCell(sheet, le, 3);
                }catch (Exception e){
                }

                //客户
                try{
                    custName = this.readExcelCell(sheet, le, 4);
                }catch (Exception e){
                }

                //线别->组长
                try{
                    linerName = this.readExcelCell(sheet, le, 5);
                }catch (Exception e){
                }

                //工单号
                try{
                    prodNo = this.readExcelCell(sheet, le, 6);
                }catch (Exception e){
                }



                //物料编码
                try{
                    itemNo = this.readExcelCell(sheet, le, 7);
                }catch (Exception e){
                }

                //物料描述
                try{
                    itemName = this.readExcelCell(sheet, le, 8);
                }catch (Exception e){
                }

                //计划生产数量
                try{
                    String qtyPlanStr = this.readExcelNumberCell(sheet, le, 9);
                    qtyPlan = Double.valueOf(qtyPlanStr).intValue();
                }catch (Exception e){
                }

                //4.保存临时数据
                SchedulingDet det = new SchedulingDet();
                det.setCreateDate(new Date());
                det.setCreateBy(currUser != null ? currUser.getId() : null);
                det.setMid(mid);
                det.setWsSection(section);
                det.setGroupNo(groupNo);
                det.setCustName(custName);
                det.setFmemo(fmemo);
                det.setProdNo(prodNo);
                det.setItemNo(itemNo);
                det.setItemName(itemName);
                det.setQtyPlan(qtyPlan);
                det.setLinerName(linerName);
                det.setErrorInfo(errorStr);
                if(StringUtils.isNotEmpty(errorStr)){
                    det.setCheckStatus(1);
                }else{
                    det.setCheckStatus(0);
                }
                detList.add(det);
            }

            if(detList.size() > 0){
                schedulingDetDao.saveAll(detList);
                return ApiResponseResult.success("导入成功！").data(detList);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("SchedulingMainImpl类的doExcel()导入Excel错误", e);
        }

        return ApiResponseResult.failure("导入失败！请查看导入文件是否有数据或者信息是否填写或格式是否正确！");
    }

    /**
     * 读取单元格内容
     * @param sheet
     * @param rnum 真实行索引
     * @param cnum 真实列索引
     * @return
     */
    public String readExcelCell(Sheet sheet, int rnum, int cnum) {
        Row row = sheet.getRow(rnum-1);
        return this.getCell(row, cnum-1);
    }
    private String getCell(Row row, int num){
        String str = "";
        row.getCell(num).setCellType(Cell.CELL_TYPE_STRING);
        String partNoTemp = row.getCell(num).getStringCellValue();
        str = StringUtils.isNotEmpty(partNoTemp) ? partNoTemp.trim() : "";
        return str;
    }
    //读取单元格数字
    public String readExcelNumberCell(Sheet sheet, int rnum, int cnum){
        try{
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);

            Cell cell = sheet.getRow(rnum-1).getCell(cnum-1);
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                //String value = numberFormat.format(cell.getNumericCellValue());
                String value = String.valueOf(cell.getNumericCellValue());
                return value;
            }
            if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                String value = cell.getStringCellValue();
                return value;
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 调用校验数据存储过程
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doCheckProc(String ids) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null && currUser.getId() == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        Long userId = currUser.getId();
        if(userId != null){
            List<String> resultList = this.doCheckProc(userId.toString(), ids, "PRC_CHECK_TASK_INFO_DET");

            if(resultList.size() > 0){
                String flag = resultList.get(0);
                if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                    return ApiResponseResult.success("校验完成，请留意校验结果！");
                }else{
                    return ApiResponseResult.failure(resultList.get(1));
                }
            }
        }

        return ApiResponseResult.failure("校验失败！");
    }
    //导入校验存储过程
    public List doCheckProc(String user_id, String ids, String prc_name) throws Exception{
        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call "+prc_name+"(?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, user_id);
                cs.setString(2, ids);
                cs.registerOutParameter(3,Types.INTEGER);// 注册输出参数 返回标志
                cs.registerOutParameter(4, Types.VARCHAR);// 注册输出参数 返回信息
                cs.registerOutParameter(5,-10);// 注册输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(3));
                result.add(cs.getString(4));
                result.add(cs.getString(5));
                return result;
            }
        });

        return resultList;
    }

    /**
     * 删除导入数据
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult deleteDet(String ids) throws Exception{
        if(StringUtils.isEmpty(ids)){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null && currUser.getId() == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        Long userId = currUser.getId();
        if(userId != null){
            List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String storedProc = "{call PRC_MES_DEL_PROD_ORDER_IMP_DET(?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, userId.toString());
                    cs.setString(2, ids);
                    cs.registerOutParameter(3,Types.INTEGER);// 注册输出参数 返回标志
                    cs.registerOutParameter(4, Types.VARCHAR);// 注册输出参数 返回信息
                    return cs;
                }
            }, new CallableStatementCallback() {
                public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    List<String> result = new ArrayList<String>();
                    cs.execute();
                    result.add(cs.getString(3));
                    result.add(cs.getString(4));
                    return result;
                }
            });

            if(resultList.size() > 0){
                String flag = resultList.get(0);
                if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                    return ApiResponseResult.success("删除成功！");
                }else{
                    return ApiResponseResult.failure(resultList.get(1));
                }
            }
        }

        return ApiResponseResult.failure("删除失败！");
    }

    /**
     * 调用生效存储过程
     * @param ids
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doEffect(String ids) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null && currUser.getId() == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        Long userId = currUser.getId();
        if(userId != null) {
            //1.校验
            List<String> resultList = this.doCheckProc(userId.toString(), ids, "PRC_CHECK_TASK_INFO_DET");
            if(resultList.size() > 0){
                String flag = resultList.get(0);
                if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                    //2.写入正式表
                    List<String> resultList2 = this.doSaveSchedulingProc(currUser.getCompany(), currUser.getFactory(), ids,currUser.getId().toString(), "PRC_IMP_TASK_INFO_DET");
                    if(resultList2.size() > 0){
                        String flag2 = resultList2.get(0);
                        if(StringUtils.isNotEmpty(flag2) && StringUtils.equals(flag2, "0")){
                            return ApiResponseResult.success("生效成功！");
                        }else{
                            return ApiResponseResult.failure(resultList2.get(1));
                        }
                    }
                }else{
                    return ApiResponseResult.failure(resultList.get(1));
                }
            }
        }
        return ApiResponseResult.failure("生效失败！");
    }
    //导入-临时表写到正式表存储过程
    public List doSaveSchedulingProc(String company, String factory,String ids, String user_id, String prc_name){
        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call "+prc_name+"(?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, company);
                cs.setString(2, factory);
                cs.setString(3, ids);
                cs.setString(4, user_id);
                cs.registerOutParameter(5, Types.INTEGER);// 注册输出参数 返回标志
                cs.registerOutParameter(6, Types.VARCHAR);// 注册输出参数 返回信息
                cs.registerOutParameter(7, -10);// 注册输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(5));
                result.add(cs.getString(6));
                result.add(cs.getString(7));
                return result;
            }
        });

        return resultList;
    }

    /**
     * 导入编辑
     * @param schedulingDet
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult editDet(SchedulingDet schedulingDet) throws Exception{
        if(schedulingDet == null || schedulingDet.getId() == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        SchedulingDet o = schedulingDetDao.findById((long) schedulingDet.getId());
        if(o == null){
            return ApiResponseResult.failure("导入信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser != null ? currUser.getId() : null);
        o.setGroupNo(schedulingDet.getGroupNo());
        o.setCustName(schedulingDet.getCustName());
        o.setLinerName(schedulingDet.getLinerName());
        o.setProdNo(schedulingDet.getProdNo());
        o.setItemNo(schedulingDet.getItemNo());
        o.setItemName(schedulingDet.getItemName());
        o.setQtyPlan(schedulingDet.getQtyPlan());
        schedulingDetDao.save(o);

        return ApiResponseResult.success("编辑成功！").data(o);
    }

    /**
     * 根据ID获取导入数据
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getDet(String id) throws Exception{
        if(StringUtils.isEmpty(id)){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        //获取排产信息
        SchedulingDet o = schedulingDetDao.findById((long) Long.parseLong(id));
        if(o == null){
            return ApiResponseResult.failure("导入信息不存在！");
        }

        return ApiResponseResult.success().data(o);
    }
}
