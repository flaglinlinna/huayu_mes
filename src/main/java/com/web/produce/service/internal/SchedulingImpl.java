package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.entity.SysUser;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.DepartmentDao;
import com.web.basic.dao.MtrialDao;
import com.web.basic.dao.WoProcDao;
import com.web.basic.entity.Department;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.WoProc;
import com.web.produce.dao.SchedulingDao;
import com.web.produce.dao.SchedulingTempDao;
import com.web.produce.entity.Scheduling;
import com.web.produce.entity.SchedulingTemp;
import com.web.produce.service.SchedulingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 排产信息
 */
@Service(value = "SchedulingService")
@Transactional(propagation = Propagation.REQUIRED)
public class SchedulingImpl implements SchedulingService {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SchedulingDao schedulingDao;
    @Autowired
    private SchedulingTempDao schedulingTempDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private MtrialDao mtrialDao;
    @Autowired
    private WoProcDao woProcDao;

    @Override
    @Transactional
    public ApiResponseResult add(Scheduling scheduling) throws Exception {
        if(scheduling == null){
            return ApiResponseResult.failure("排产信息不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        scheduling.setCreatedTime(new Date());
        scheduling.setPkCreatedBy(currUser!=null ? currUser.getId() : null);
        scheduling.setBsUniqueOrderNo(this.getUniqueOrderNo());
        schedulingDao.save(scheduling);

        return ApiResponseResult.success("新增成功！").data(scheduling);
    }

    @Override
    @Transactional
    public ApiResponseResult edit(Scheduling scheduling) throws Exception {
        if(scheduling == null && scheduling.getId() == null){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        Scheduling o = schedulingDao.findById((long) scheduling.getId());
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setModifiedTime(new Date());
        o.setPkModifiedBy(currUser!=null ? currUser.getId() : null);
        o.setPkDepartment(scheduling.getPkDepartment());//部门
        o.setBsDepartCode(scheduling.getBsDepartCode());
        o.setBsProduceTime(scheduling.getBsProduceTime());
        o.setBsShift(scheduling.getBsShift());
        o.setBsCustomer(scheduling.getBsCustomer());
        o.setBsLine(scheduling.getBsLine());
        o.setBsOrderNo(scheduling.getBsOrderNo());
        o.setPkMtrial(scheduling.getPkMtrial());//物料
        o.setBsMtrialCode(scheduling.getBsMtrialCode());
        o.setBsMtrialDesc(scheduling.getBsMtrialDesc());
        o.setPkWoProc(scheduling.getPkWoProc());//加工工艺
        o.setBsProcCode(scheduling.getBsProcCode());
        o.setBsRestNum(scheduling.getBsRestNum());
        o.setBsPlanNum(scheduling.getBsPlanNum());
        o.setBsPeopleNum(scheduling.getBsPeopleNum());
        o.setBsCapacityNum(scheduling.getBsCapacityNum());
        o.setBsPlanHours(scheduling.getBsPlanHours());
        o.setBsActualNum(scheduling.getBsActualNum());
//        o.setBsActualHours(scheduling.getBsActualHours());
//        o.setBsPlanPrice(scheduling.getBsPlanPrice());
//        o.setBsActualPrice(scheduling.getBsActualPrice());
//        o.setBsRemark(scheduling.getBsRemark());
        schedulingDao.save(o);

        return ApiResponseResult.success("编辑成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
        if(id == null){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        Scheduling o = schedulingDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setModifiedTime(new Date());
        o.setPkModifiedBy(currUser!=null ? currUser.getId() : null);
        o.setIsDel(1);
        schedulingDao.save(o);

        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        //查询条件2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("bsDepartCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsShift", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsCustomer", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsLine", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsOrderType", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsOrderNo", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsMtrialCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsProcCode", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<Scheduling> spec = Specification.where(BaseService.and(filters, Scheduling.class));
        Specification<Scheduling> spec1 =  spec.and(BaseService.or(filters1, Scheduling.class));
        Page<Scheduling> page = schedulingDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 根据ID获取排产信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getSchedulData(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        Scheduling o = schedulingDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }

        return ApiResponseResult.success().data(o);
    }

    /**
     * 导出模板
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getExcel(HttpServletResponse response) throws Exception{
        //1.创建文件
        OutputStream outputStream = response.getOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();   //创建一个工作簿
        Sheet sheet = workbook.createSheet("排产数据导入模板");
        List<XSSFCellStyle> cellStyleList = getStyle(workbook);
        List<String> headerList = new ArrayList<String>(); //初始化
        List<List<String>> bodyList = new ArrayList<>();//初始化

        //2创建表头信息
        headerList.add("部门");//1
        headerList.add("日期");//2
        headerList.add("班次");//3
        headerList.add("客户");//4
        headerList.add("线别");//5
        headerList.add("工单号");//6
        headerList.add("物料编码");//7
        headerList.add("物料描述");//8
        headerList.add("加工工艺");//9
        headerList.add("工单残");//10
        headerList.add("计划生产数量");//11
        headerList.add("用人量");//12
        headerList.add("产能");//13
        headerList.add("预计工时(H/人)");//14
        headerList.add("实际生产数量");//15
        headerList.add("实际工时(H/人)");//16
        headerList.add("计划金额");//17
        headerList.add("实际生产金额 ");//18
        headerList.add("备注");//19

        //创建行（表头）
        Row createRow1 = sheet.createRow(0);
        for(int i = 0; i < headerList.size(); i++){
            createRow1.createCell(i);
        }
        //设置列宽
        for(int i = 0; i < headerList.size(); i++){
            if(headerList.get(i).equals("物料描述")){
                sheet.setColumnWidth(i, 50*256);
            }else if(headerList.get(i).equals("工单号") || headerList.get(i).equals("物料编码")){
                sheet.setColumnWidth(i, 20*256);
            }else if(headerList.get(i).equals("日期") || headerList.get(i).equals("加工工艺") || headerList.get(i).equals("计划生产数量")
                    || headerList.get(i).equals("预计工时(H/人)") || headerList.get(i).equals("实际生产数量") || headerList.get(i).equals("实际工时(H/人)")
                    || headerList.get(i).equals("备注")){
                sheet.setColumnWidth(i, 10*256);
            }else if(headerList.get(i).equals("线别")) {
                sheet.setColumnWidth(i, 7*256);
            }else {
                sheet.setColumnWidth(i, 5*256);
            }
        }
        //添加样式和数据
        for(int i = 0; i < headerList.size(); i++){
            Cell cell = sheet.getRow(0).getCell(i);
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(headerList.get(i));
            cell.setCellStyle(cellStyleList.get(0));
        }

        //3.创建表内容信息
        int bodyNum = bodyList.size() <= 0 ? 1 : bodyList.size();
        for(int i = 0; i < bodyNum; i++){
            Row createRow2 = sheet.createRow(i + 1);
            for(int j = 0; j < headerList.size(); j++){
                createRow2.createCell(j);
            }
            //设置行高
            //sheet.getRow(i + 1).setHeightInPoints((float) 15.8);
            //添加样式和数据
            for(int k = 0; k < headerList.size(); k++){
                Cell cell = sheet.getRow(i + 1).getCell(k);
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(bodyList.size() <= 0 ? "" : bodyList.get(i).get(k));
                cell.setCellStyle(cellStyleList.get(1));
            }
        }

        response.reset();
        response.setContentType("multipart/form-data");
        String fileName = URLEncoder.encode("排产数据导入模板", "UTF-8")+ ".xlsx";
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

        return ApiResponseResult.success("导出成功！");
    }

    //Excel样式
    public List<XSSFCellStyle> getStyle(XSSFWorkbook workbook) {
        List<XSSFCellStyle> cellStyleList = new ArrayList<XSSFCellStyle>();

        //添加字体
        //0.
        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);  //字体加粗

        //1.
        XSSFFont font1 = workbook.createFont();
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 10);

        //添加样式
        //0.实线边框
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);  //上边框
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);  //右边框
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);  //下边框
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);  //左边框
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);  //水平居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  //垂直居中
        cellStyle.setWrapText(true);  //自动换行
        //cellStyle.setFillForegroundColor(new XSSFColor(new Color(184, 204, 228)));//背景颜色
        //cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cellStyleList.add(cellStyle);

        //1.实线边框
        XSSFCellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setFont(font1);
        cellStyle1.setBorderTop(CellStyle.BORDER_THIN);  //上边框
        cellStyle1.setBorderRight(CellStyle.BORDER_THIN);  //右边框
        cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);  //下边框
        cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);  //左边框
        cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);  //水平居中
        cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  //垂直居中
        cellStyle1.setWrapText(true);  //自动换行
        cellStyleList.add(cellStyle1);

        return cellStyleList;
    }

    /**
     * 导入
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult doExcel(MultipartFile file) throws Exception{
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
            schedulingTempDao.updateIsDelByPkSysUser(1, currUser.getId());
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
                String departCode = "";//部门编码
                Long departId = null;
                Date produceTime = null;//日期
                String shift = "";//班次
                String customer = "";//客户
                String line = "";//线别
                String orderNo = "";//工单号
                String mtrialCode = "";//物料编码
                Long mtrialId = null;
                String mtrialDesc = "";//物料描述
                String procCode = "";//加工工艺编码
                Long procId = null;
                String restNum = null;//工单残
                String planNum = null;//计划生产数量
                String peopleNum = null;//用人量
                String capacityNum = null;//产能
                BigDecimal planHours = null;//预计工时
                String actualNum = null;//实际生产数量
                BigDecimal actualHours = null;//实际工时
                BigDecimal planPrice = null;//计划金额
                BigDecimal actualPrice = null;//实际金额
                String remark = "";//备注

                //部门
                try{
                    departCode = this.readExcelCell(sheet, le, 1);//部门编码
//                    if(StringUtils.isEmpty(departCode)){
//                        //errorStr += "部门为空；";
//                    }else{
//                        //获取个编码关联的ID
//                        List<Department> dList = departmentDao.findByBsName(departCode);
//                        departId = dList.size()>0&&dList.get(i)!=null ? dList.get(i).getId() : null;
//                        if(departId == null){
//                            errorStr += "部门填写错误；";
//                        }
//                    }
                }catch (Exception e){
                    //errorStr += "部门为空或格式错误；";
                }

                //日期
                try{
                    produceTime = this.readExcelDateCell(sheet, le, 2);//日期
                    if(produceTime == null){
                        //errorStr += "日期为空或格式不正确；";
                    }
                }catch (Exception e){
                    //errorStr += "日期为空或格式不正确；";
                }

                //班次
                try{
                    shift = this.readExcelCell(sheet, le, 3);
                }catch (Exception e){
                }

                //客户
                try{
                    customer = this.readExcelCell(sheet, le, 4);
                }catch (Exception e){
                }

                //线别
                try{
                    line = this.readExcelCell(sheet, le, 5);
                }catch (Exception e){
                }

                //工单号
                try{
                    orderNo = this.readExcelCell(sheet, le, 6);
                }catch (Exception e){
                }

                //物料编码
                try{
                    mtrialCode = this.readExcelCell(sheet, le, 7);//物料编码
//                    if(StringUtils.isEmpty(mtrialCode)){
//                        //errorStr += "物料编码为空；";
//                    }else{
//                        //获取个编码关联的ID
//                        List<Mtrial> mList = mtrialDao.findByIsDelAndBsCode(0, mtrialCode);
//                        mtrialId = mList.size()>0&&mList.get(i)!=null ? mList.get(i).getId() : null;
//                        if(departId == null){
//                            errorStr += "物料编码填写错误；";
//                        }
//                    }
                }catch (Exception e){
                    //errorStr += "物料编码为空或格式错误；";
                }

                //物料描述
                try{
                    mtrialDesc = this.readExcelCell(sheet, le, 8);
                }catch (Exception e){
                }

                //加工工艺
                try{
                    procCode = this.readExcelCell(sheet, le, 9);//加工工艺
//                    if(StringUtils.isEmpty(procCode)){
//                    }else{
//                        //获取个编码关联的ID
//                        List<WoProc> pList = woProcDao.findByBsName(procCode);
//                        procId = pList.size()>0&&pList.get(i)!=null ? pList.get(i).getId() : null;
//                        if(procId == null){
//                            errorStr += "加工工艺填写错误；";
//                        }
//                    }
                }catch (Exception e){
                    //errorStr += "加工工艺为空或格式错误；";
                }

                //工单残
                try{
                    restNum = this.readExcelNumberCell(sheet, le, 10);
                }catch (Exception e){
                }

                //计划生产数量
                try{
                    planNum = this.readExcelNumberCell(sheet, le, 11);
                }catch (Exception e){
                }

                //用人量
                try{
                    peopleNum = this.readExcelNumberCell(sheet, le, 12);
                }catch (Exception e){
                }

                //产能
                try{
                    capacityNum = this.readExcelNumberCell(sheet, le, 13);
                }catch (Exception e){
                }

                //预计工时
                try{
                    String planHoursStr = this.readExcelNumberCell(sheet, le, 14);
                    if(StringUtils.isNotEmpty(planHoursStr)){
                        planHours = new BigDecimal(planHoursStr);
                    }
                }catch (Exception e){
                }

                //实际生产数量
                try{
                    actualNum = this.readExcelNumberCell(sheet, le, 15);
                }catch (Exception e){
                }

                //实际工时
                try{
                    String actualHoursStr = this.readExcelNumberCell(sheet, le, 16);
                    if(StringUtils.isNotEmpty(actualHoursStr)){
                        actualHours = new BigDecimal(actualHoursStr);
                    }
                }catch (Exception e){
                }

                //计划金额
                try{
                    String planPriceStr = this.readExcelNumberCell(sheet, le, 17);
                    if(StringUtils.isNotEmpty(planPriceStr)){
                        planPrice = new BigDecimal(planPriceStr);
                    }
                }catch (Exception e){
                }

                //实际金额
                try{
                    String actualPriceStr = this.readExcelNumberCell(sheet, le, 18);
                    if(StringUtils.isNotEmpty(actualPriceStr)){
                        actualPrice = new BigDecimal(actualPriceStr);
                    }
                }catch (Exception e){
                }

                //备注
                try{
                    remark = this.readExcelCell(sheet, le, 19);
                }catch (Exception e){
                }

                //4.保存临时数据
                SchedulingTemp temp = new SchedulingTemp();
                temp.setCreatedTime(new Date());
                temp.setPkDepartment(departId);//部门
                temp.setBsDepartCode(departCode);
                temp.setBsProduceTime(produceTime);
                temp.setBsShift(shift);
                temp.setBsCustomer(customer);
                temp.setBsLine(line);
                temp.setBsOrderNo(orderNo);
                temp.setPkMtrial(mtrialId);
                temp.setBsMtrialCode(mtrialCode);
                temp.setBsMtrialDesc(mtrialDesc);
                temp.setPkWoProc(procId);
                temp.setBsProcCode(procCode);
                temp.setBsRestNum(restNum);
                temp.setBsPlanNum(planNum);
                temp.setBsPeopleNum(peopleNum);
                temp.setBsCapacityNum(capacityNum);
                temp.setBsPlanHours(planHours);
                temp.setBsActualNum(actualNum);
                temp.setBsActualHours(actualHours);
                temp.setBsPlanPrice(planPrice);
                temp.setBsActualPrice(actualPrice);
                temp.setBsRemark(remark);
                temp.setBsError(errorStr);
                if(StringUtils.isNotEmpty(errorStr)){
                    temp.setBsCheckStatus(1);
                }else{
                    temp.setBsCheckStatus(0);
                }
                temp.setPkSysUser(currUser.getId());
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

    //读取单元格日期
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
    public Date readExcelDateCell(Sheet sheet, int rnum, int cnum){
        try{
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
            }
            //2.判断是否为字符串
            if(cell.getCellType() == Cell.CELL_TYPE_STRING){
                String value = cell.getStringCellValue();
                if(StringUtils.isNotEmpty(value)){
                    if(value.contains("-")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        date = sdf.parse(value);
                    }else if(value.contains("/")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        date = sdf.parse(value);
                    }else if(value.contains(".")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                        date = sdf.parse(value);
                    }else if(value.contains("日")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                        date = sdf.parse(value);
                    }else if(value.contains("号")){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd号");
                        date = sdf.parse(value);
                    }else{
                        date = null;
                    }
                }
            }

            return date;
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
     * 获取临时表数据
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getTempList(PageRequest pageRequest) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        List<SearchFilter> filters = new ArrayList<SearchFilter>();
        filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("pkSysUser", SearchFilter.Operator.EQ, currUser.getId()));

        Specification<SchedulingTemp> spec = Specification.where(BaseService.and(filters, SchedulingTemp.class));
        Page<SchedulingTemp> page = schedulingTempDao.findAll(spec, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 根据当前登录用户删除临时表所有数据
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult deleteTempAll() throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        int num = schedulingTempDao.countByIsDelAndPkSysUser(0, currUser.getId());
        if(num > 0){
            //如果当前用户存在临时数据则删除
            schedulingTempDao.updateIsDelByPkSysUser(1, currUser.getId());
        }

        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 确认临时表数据
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult confirmTemp() throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Scheduling> list = new ArrayList<>();

        //1.获取当前用户关联的临时表
        List<SchedulingTemp> tempList =schedulingTempDao.findByIsDelAndPkSysUser(0, currUser.getId());
        for(SchedulingTemp temp : tempList){
            if(temp != null){
                //新增
                Scheduling scheduling = new Scheduling();
                scheduling.setCreatedTime(new Date());
                scheduling.setPkDepartment(temp.getPkDepartment());//部门
                scheduling.setBsDepartCode(temp.getBsDepartCode());
                scheduling.setBsProduceTime(temp.getBsProduceTime());
                scheduling.setBsShift(temp.getBsShift());
                scheduling.setBsCustomer(temp.getBsCustomer());
                scheduling.setBsLine(temp.getBsLine());
                scheduling.setBsUniqueOrderNo(this.getUniqueOrderNo());
                scheduling.setBsOrderNo(temp.getBsOrderNo());
                scheduling.setPkMtrial(temp.getPkMtrial());//物料
                scheduling.setBsMtrialCode(temp.getBsMtrialCode());
                scheduling.setBsMtrialDesc(temp.getBsMtrialDesc());
                scheduling.setPkWoProc(temp.getPkWoProc());//加工工艺
                scheduling.setBsProcCode(temp.getBsProcCode());
                scheduling.setBsRestNum(temp.getBsRestNum());
                scheduling.setBsPlanNum(temp.getBsPlanNum());
                scheduling.setBsPeopleNum(temp.getBsPeopleNum());
                scheduling.setBsCapacityNum(temp.getBsCapacityNum());
                scheduling.setBsPlanHours(temp.getBsPlanHours());
                scheduling.setBsActualNum(temp.getBsActualNum());
                scheduling.setBsActualHours(temp.getBsActualHours());
                scheduling.setBsPlanPrice(temp.getBsPlanPrice());
                scheduling.setBsActualPrice(temp.getBsActualPrice());
                scheduling.setBsRemark(temp.getBsRemark());
                list.add(scheduling);
            }
        }

        if(list.size() > 0){
            schedulingDao.saveAll(list);
        }

        return ApiResponseResult.success("保存成功！").data(list);
    }

    //生成随机制令单号
    private String getUniqueOrderNo(){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr = sdf.format(new Date());

            Random random = new Random();
            int nextInt = random.nextInt(900000);
            nextInt = nextInt + 100000;
            String randonStr = nextInt + "";

            return dateStr + randonStr;
        }catch (Exception e){
            return null;
        }
    }
}