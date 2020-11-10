package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.entity.SysUser;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.produce.dao.SchedulingTempDao;
import com.web.produce.entity.SchedulingTemp;
import com.web.produce.service.SchedulingTempService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * 排产导入临时信息
 */
@Service(value = "SchedulingTempService")
@Transactional(propagation = Propagation.REQUIRED)
public class SchedulingTempImpl extends PrcUtils implements SchedulingTempService {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SchedulingTempDao schedulingTempDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public ApiResponseResult edit(SchedulingTemp temp) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public ApiResponseResult delete(String ids) throws Exception {
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
                    String storedProc = "{call PRC_MES_DEL_PROD_ORDER_IMP(?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, userId.toString());
                    cs.setString(2, ids);
                    cs.registerOutParameter(3,Types.INTEGER);// 注册输出参数 返回标志
                    cs.registerOutParameter(4,java.sql.Types.VARCHAR);// 注册输出参数 返回信息
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

    @Override
    @Transactional
    public ApiResponseResult getList(String keyword, String startTime, String endTime, PageRequest pageRequest) throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        List<Object> list = super.getSchedulingTempPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"", UserUtil.getSessionUser().getId()+"",
                startTime, endTime, keyword, pageRequest.getPageNumber()+1, pageRequest.getPageSize(), "prc_mes_get_PROD_ORDER_IMP");

        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    /**
     * 根据ID获取排产导入临时信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getTempData(Long id) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public ApiResponseResult getExcel(HttpServletResponse response) throws Exception {
        return null;
    }

    /**
     * 导入
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doExcel(MultipartFile file) throws Exception {
        try{
            if (file == null) {
                return ApiResponseResult.failure("导入文件不存在！");
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
            schedulingTempDao.updateDelFlagByCreateBy(1, currUser.getId());
            //初始化临时表
            List<SchedulingTemp> tempList = new ArrayList<>();

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
                Integer groupNo = null;//组合
                String custName = "";//客户名称
                String prodNo = "";//工单号
                String itemNo = "";//物料编码
                String itemName = "";//物料描述
                Integer qtyPlan = null;//计划数量
                String prodDate = null;//生产日期
                //String deptName = "";//部门名称
                String linerName = "";//线长名称
                String classNo = "";//班次

                //组合
                try{
                    String groupNostr = this.readExcelNumberCell(sheet, le, 1);
                    groupNo = Integer.parseInt(groupNostr);
                }catch (Exception e){
                }

                //客户
                try{
                    custName = this.readExcelCell(sheet, le, 2);
                }catch (Exception e){
                }

                //线别->组长
                try{
                    linerName = this.readExcelCell(sheet, le, 3);
                }catch (Exception e){
                }

                //日期
                try{
                    prodDate = this.readExcelDateCellToString(sheet, le, 4);
                }catch (Exception e){
                }

                //部门
                //try{
                //    deptName = this.readExcelCell(sheet, le, 5);
                // }catch (Exception e){
                // }

                //班次
                try{
                    classNo = this.readExcelCell(sheet, le, 5);
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
                SchedulingTemp temp = new SchedulingTemp();
                temp.setCreateDate(new Date());
                temp.setCreateBy(currUser != null ? currUser.getId() : null);
                temp.setGroupNo(groupNo);
                temp.setCustName(custName);
                temp.setProdNo(prodNo);
                temp.setItemNo(itemNo);
                temp.setItemName(itemName);
                temp.setQtyPlan(qtyPlan);
                temp.setProdDate(prodDate);
                // temp.setDeptName(deptName);
                temp.setLinerName(linerName);
                temp.setClassNo(classNo);
                temp.setErrorInfo(errorStr);
                if(StringUtils.isNotEmpty(errorStr)){
                    temp.setCheckStatus(1);
                }else{
                    temp.setCheckStatus(0);
                }
                tempList.add(temp);
            }

            if(tempList.size() > 0){
                schedulingTempDao.saveAll(tempList);
                return ApiResponseResult.success("导入成功！").data(tempList);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("SchedulingImpl类的doExcel()导入Excel错误", e);
        }

        return ApiResponseResult.failure("导入失败！请查看导入文件是否有数据或者产品编码是否填写或格式是否正确！");
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
    //读取单元格日期，返回String格式
    //说明：（1）HSSFDateUtil.isCellDateFormatted(cell)用来判断当前单元格格式是否是日期格式，
    // 它内部的实现单元格读取成数字（excel日期格式默认是用数字保存的），
    // 所以调用这个api判断时需先要判断当前单元格格式不是字符串，否则字符串会导致读取失败
    //（2）在自定义格式中，所有日期格式都可以通过getDataFormat()值来判断，如下
    // yyyy-MM-dd----- 14
    // yyyy年m月d日--- 31
    // yyyy年m月------- 57
    // m月d日  ---------- 58
    // HH:mm----------- 20
    // h时mm分  ------- 32
    public String readExcelDateCellToString(Sheet sheet, int rnum, int cnum){
        try{
            String dateStr = null;
            Date date = null;
            Cell cell = sheet.getRow(rnum-1).getCell(cnum-1);
            //1.判断是否为数字（日期在POI中会转换成数字处理）
            if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    //1.1如果是日期格式，直接获取日期
                    date = sheet.getRow(rnum-1).getCell(cnum-1).getDateCellValue();
                }else{
                    //1.2如果是其他格式（比如自定义），调用getDataFormat()方法判断是否为日期
                    short format = cell.getCellStyle().getDataFormat();
                    if (format == 14 || format == 31 || format == 57 || format == 58
                            || (176<=format && format<=178) || (182<=format && format<=196)
                            || (210<=format && format<=213) || (208==format ) ) { // 日期
                        date = sheet.getRow(rnum-1).getCell(cnum-1).getDateCellValue();
                    } else if (format == 20 || format == 32 || format==183 || (200<=format && format<=209) ) { // 时间
                        date = null;
                    } else { // 不是日期时间格式
                        date = null;
                    }
                }
                if(date != null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    dateStr = sdf.format(date);
                }
            }
            //2.判断是否为字符串
            if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                dateStr = cell.getStringCellValue();
            }

            return dateStr;
        }catch (Exception e){
            return null;
        }
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
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doCheckProc(String ids) throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null && currUser.getId() == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        Long userId = currUser.getId();
        if(userId != null){
            List<String> resultList = super.doCheckProc(userId.toString(), ids, "PRC_CHECK_TASK_INFO_NEW");

            if(resultList.size() > 0){
                String flag = resultList.get(0);
                if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                    return ApiResponseResult.success("校验成功！");
                }else{
                    return ApiResponseResult.failure(resultList.get(1));
                }
            }
        }

        return ApiResponseResult.failure("校验失败！");
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
        if(userId != null){
            //1.校验
            List<String> resultList = super.doCheckProc(userId.toString(), ids, "PRC_CHECK_TASK_INFO_NEW");
            if(resultList.size() > 0){
                String flag = resultList.get(0);
                if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                    //2.写入正式表
                    List<String> resultList2 = super.doSaveSchedulingProc(currUser.getCompany(), currUser.getFactory(), ids, "PRC_IMP_TASK_INFO");
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
}
