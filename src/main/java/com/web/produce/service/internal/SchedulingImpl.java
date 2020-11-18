package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.organization.dao.OrganizationDao;
import com.system.organization.entity.SysOrganization;
import com.system.user.entity.SysUser;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.*;
import com.web.basic.entity.*;
import com.web.basic.entity.Process;
import com.web.produce.dao.SchedulingDao;
import com.web.produce.dao.SchedulingItemDao;
import com.web.produce.dao.SchedulingProcessDao;
import com.web.produce.dao.SchedulingTempDao;
import com.web.produce.entity.Scheduling;
import com.web.produce.entity.SchedulingItem;
import com.web.produce.entity.SchedulingProcess;
import com.web.produce.entity.SchedulingTemp;
import com.web.produce.service.SchedulingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
//    @Autowired
//    private DepartmentDao departmentDao;
    @Autowired
    private MtrialDao mtrialDao;
    @Autowired
    private ProcessDao processDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SchedulingProcessDao schedulingProcessDao;
    @Autowired
    private SchedulingItemDao schedulingItemDao;
    @Autowired
    private ClientDao clientDao;
//    @Autowired
//    private LineDao lineDao;
    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
    private EmployeeDao employeeDao;

    @Override
    @Transactional
    public ApiResponseResult add(Scheduling scheduling) throws Exception {
        if(scheduling == null){
            return ApiResponseResult.failure("排产信息不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        scheduling.setCreateDate(new Date());
        scheduling.setCreateBy(currUser!=null ? currUser.getId() : null);
        schedulingDao.save(scheduling);

        return ApiResponseResult.success("新增成功！").data(scheduling);
    }

    @Override
    @Transactional
    public ApiResponseResult edit(Scheduling scheduling) throws Exception {
        if(scheduling == null && scheduling.getId() == null){
            return ApiResponseResult.failure("排产信息ID不能为空！");
        }
        if(StringUtils.isEmpty(scheduling.getLinerName())){
            return ApiResponseResult.failure("组长不能为空！");
        }
        Scheduling o = schedulingDao.findById((long) scheduling.getId());
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        //获取物料信息
        Mtrial mtrial = null;
        if(StringUtils.isNotEmpty(scheduling.getItemId())){
            mtrial = mtrialDao.findById((long) Long.parseLong(scheduling.getItemId()));
        }
        //获取客户信息
        Client client = null;
        if(StringUtils.isNotEmpty(scheduling.getCustId())){
            client = clientDao.findById((long) Long.parseLong(scheduling.getCustId()));
        }
        //获取部门信息
        SysOrganization org = null;
        if(StringUtils.isNotEmpty(scheduling.getDeptId())){
            org = organizationDao.findById((long) Long.parseLong(scheduling.getDeptId()));
        }

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser!=null ? currUser.getId() : null);
        o.setProdNo(scheduling.getProdNo());
        o.setGroupNo(scheduling.getGroupNo());
        o.setCustId(scheduling.getCustId());
        o.setCustName(client!=null ? client.getCustName() : null);
        o.setCustNameS(client!=null ? client.getCustNameS() : null);
        o.setCustNo(client!=null ? client.getCustNo() : null);
        o.setItemId(scheduling.getItemId());
        o.setItemNo(mtrial!=null ? mtrial.getItemNo() : null);
        o.setItemName(mtrial!=null ? mtrial.getItemName() : null);
        o.setQtyPlan(scheduling.getQtyPlan());
        o.setProdDate(scheduling.getProdDate());
        o.setDeptId(scheduling.getDeptId());
        o.setDeptName(org!=null ? org.getOrgName() : null);
        o.setLinerName(scheduling.getLinerName());
        o.setLineNo(scheduling.getLineNo());
        o.setClassNo(scheduling.getClassNo());
        o.setProduceState(scheduling.getProduceState());
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

        o.setDelTime(new Date());
        o.setDelBy(currUser!=null ? currUser.getId() : null);
        o.setDelFlag(1);
        schedulingDao.save(o);

        return ApiResponseResult.success("删除成功！");
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
            filters1.add(new SearchFilter("prodNo", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("custName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("linerName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("deptName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("classNo", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("taskNo", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("itemNo", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<Scheduling> spec = Specification.where(BaseService.and(filters, Scheduling.class));
        Specification<Scheduling> spec1 =  spec.and(BaseService.or(filters1, Scheduling.class));
        Page<Scheduling> page = schedulingDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }


    @Override
    @Transactional
    public ApiResponseResult getListByProcedure(String keyword, PageRequest pageRequest) throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getEmpListPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                UserUtil.getSessionUser().getId(),"","",keyword, pageRequest.getPageNumber()+1, pageRequest.getPageSize(), "prc_mes_get_PROD_ORDER");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
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
        //获取排产信息
        Scheduling o = schedulingDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("排产信息不存在！");
        }
        //获取物料信息
//        List<Mtrial> itemList = mtrialDao.findByDelFlagAndCheckStatus(0, 1);
        //获取客户信息
        List<Client> clientList = clientDao.findByDelFlag(0);
        //获取线体信息
        //List<Line> lineList = lineDao.findByDelFlagAndCheckStatus(0, 1);
        List<Object> lineList = getLineistPrc(UserUtil.getSessionUser().getFactory() + "",
        		UserUtil.getSessionUser().getCompany() + "");
        //获取部门信息
        List<SysOrganization> orgList = organizationDao.findByDelFlag(0);
        //获取人员信息
//        List<Employee> employeeList = employeeDao.findByDelFlagAndEmpStatus(0, 1);

        Map<String, Object> map = new HashMap<>();
        map.put("scheduling", o);
//        map.put("itemList", itemList);
        map.put("clientList", clientList);
        map.put("lineList", lineList);
        map.put("orgList", orgList);
//        map.put("employeeList", employeeList);

        return ApiResponseResult.success().data(map);
    }
    /**
     * 获取组长列表
     */
    public List getLineistPrc(String factory,String company)
			throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_cof_org_chs(?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);
				cs.setString(2, factory);
				cs.setString(3, "");
				cs.setString(4, "组长");
				cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(7, -10);// 输出参数 追溯数据
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
					ResultSet rs = (ResultSet) cs.getObject(7);
					try {
						l = fitMap(rs);// 游标处理
					} catch (Exception e) {
						e.printStackTrace();
					}
					result.add(l);
				}
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
        Sheet sheet = workbook.createSheet("排产导入模板");
        List<XSSFCellStyle> cellStyleList = getStyle(workbook);
        List<String> headerList = new ArrayList<String>(); //初始化
        List<List<String>> bodyList = new ArrayList<>();//初始化

        //2创建表头信息
        headerList.add("组合*");//1
        headerList.add("客户*");//2
        headerList.add("组长*");//3
        headerList.add("日期*");//4
        //headerList.add("组装部");//5
        headerList.add("班次*");//6->5
        headerList.add("工单号*");//7->6
        headerList.add("物料编码*");//8->7
        headerList.add("物料描述*");//9->8
        headerList.add("计划生产数量*");//10->9

        //创建行（表头）
        Row createRow1 = sheet.createRow(0);
        for(int i = 0; i < headerList.size(); i++){
            createRow1.createCell(i);
        }
        //设置列宽
        for(int i = 0; i < headerList.size(); i++){
            if(headerList.get(i).equals("物料描述*")){
                sheet.setColumnWidth(i, 80*256);
            }else if(headerList.get(i).equals("物料编码*") || headerList.get(i).equals("计划生产数量*")){
                sheet.setColumnWidth(i, 25*256);
            }else if(headerList.get(i).equals("工单号*")){
                sheet.setColumnWidth(i, 20*256);
            }else if(headerList.get(i).equals("日期*")){
                sheet.setColumnWidth(i, 10*256);
            }else if(headerList.get(i).equals("组长*")) {
                sheet.setColumnWidth(i, 9*256);
            }else {
                sheet.setColumnWidth(i, 7*256);
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
        String fileName = URLEncoder.encode("排产导入模板", "UTF-8")+ ".xlsx";
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
    public ApiResponseResult doCheckProc() throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null && currUser.getId() == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        Long userId = currUser.getId();
        if(userId != null){
            List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String storedProc = "{call PRC_CHECK_TASK_INFO(?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, userId.toString());
                    cs.registerOutParameter(2,Types.INTEGER);// 注册输出参数 返回标志
                    cs.registerOutParameter(3,java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                    cs.registerOutParameter(4,-10);// 注册输出参数 返回数据集合
                    return cs;
                }
            }, new CallableStatementCallback() {
                public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    List<String> result = new ArrayList<String>();
                    cs.execute();
                    result.add(cs.getString(2));
                    result.add(cs.getString(3));
                    result.add(cs.getString(4));
                    return result;
                }
            });

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
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("createBy", SearchFilter.Operator.EQ, currUser.getId()));

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

        int num = schedulingTempDao.countByDelFlagAndCreateBy(0, currUser.getId());
        if(num > 0){
            //如果当前用户存在临时数据则删除
            schedulingTempDao.updateDelFlagByCreateBy(1, currUser.getId());
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
        Long userId = currUser.getId();
        String company = currUser.getCompany();
        String factory = currUser.getFactory();

        //检查是否存在
        int num = schedulingTempDao.countByDelFlagAndCreateByAndCheckStatus(0, userId, 1);
        if(num > 0){
            return ApiResponseResult.failure("保存失败！存在检验不通过的排产，请检查！");
        }

        //将临时数据保存到正式表中
        List<String> resultList = doSaveProc(currUser);
        if(resultList.size() > 0){
            String flag = resultList.get(0);
            if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                return ApiResponseResult.success("保存成功！");
            }else{
                return ApiResponseResult.failure(resultList.get(1));
            }
        }

        return ApiResponseResult.failure("保存失败！");
    }

    //调用保存数据存储过程
    public List<String> doSaveProc(SysUser currUser) {
        Long userId = currUser.getId();
        String company = currUser.getCompany();
        String factory = currUser.getFactory();
        if (userId != null) {
            List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String storedProc = "{call PRC_IMP_TASK_INFO(?,?,?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, company);
                    cs.setString(2, factory);
                    cs.setString(3, userId.toString());
                    cs.registerOutParameter(4, Types.INTEGER);// 注册输出参数 返回标志
                    cs.registerOutParameter(5, java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                    cs.registerOutParameter(6, -10);// 注册输出参数 返回数据集合
                    return cs;
                }
            }, new CallableStatementCallback() {
                public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    List<String> result = new ArrayList<String>();
                    cs.execute();
                    result.add(cs.getString(4));
                    result.add(cs.getString(5));
                    result.add(cs.getString(6));
                    return result;
                }
            });

            return resultList;
        } else {
            return new ArrayList<>();
        }
    }

    //提取工序存储过程
    @Override
    @Transactional
    public ApiResponseResult getProcessProc() throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        Long userId = currUser.getId();
        String company = currUser.getCompany();
        String factory = currUser.getFactory();

        if(userId != null){
            List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String storedProc = "{call PRC_GET_TASK_PRO(?,?,?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, company);
                    cs.setString(2, factory);
                    cs.setString(3, userId.toString());
                    cs.registerOutParameter(4,Types.INTEGER);// 注册输出参数 返回标志
                    cs.registerOutParameter(5,java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                    cs.registerOutParameter(6,-10);// 注册输出参数 返回数据集合
                    return cs;
                }
            }, new CallableStatementCallback() {
                public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    List<String> result = new ArrayList<String>();
                    cs.execute();
                    result.add(cs.getString(4));
                    result.add(cs.getString(5));
                    result.add(cs.getString(6));
                    return result;
                }
            });

            if(resultList.size() > 0){
                String flag = resultList.get(0);
                if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                    return ApiResponseResult.success("提取工序成功！");
                }else{
                    return ApiResponseResult.failure(resultList.get(1));
                }
            }
        }

        return ApiResponseResult.failure("提取工序失败！");
    }

    /**
     * 获取生产制令单从表-工艺
     * @param keyword
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getProcessList(String keyword, Long mid, PageRequest pageRequest) throws Exception{
        //查询条件1
        Sort sort = new Sort(Sort.Direction.ASC, "procOrder");
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("mid", SearchFilter.Operator.EQ, mid));
        Specification<SchedulingProcess> spec = Specification.where(BaseService.and(filters, SchedulingProcess.class));
        List<SchedulingProcess> list = schedulingProcessDao.findAll(spec, sort);
        List<Process> list2 = processDao.findByDelFlagAndCheckStatus(0, 1);

        //封装数据
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(Process process : list2){
            Map<String, Object> map = new HashMap<>();
            boolean flag = false;//从表工艺是否存在

            if(list.size() > 0){
                for(SchedulingProcess item : list){
                    if(process != null && item != null && StringUtils.equals(process.getProcNo(), item.getProcNo())){
                        //已选择
                        map.put("id", item.getId());
                        map.put("mid", mid);
                        map.put("procOrder", item.getProcOrder() != null ? item.getProcOrder() : "0");
                        map.put("procNo", item.getProcNo());
                        map.put("procName", process.getProcName());
                        map.put("procId", process.getId());
                        map.put("jobAttr", item.getJobAttr());
                        map.put("empId", item.getEmpId());
                        map.put("empName", item.getEmployee()!=null ? item.getEmployee().getEmpName() : "");
                        map.put("isCheck", 1);
                        mapList.add(map);
                        flag = true;
                        break;
                    }
                }
            }
            if(!flag){
                //未选择
                map.put("id", null);
                map.put("mid", mid);
                map.put("procOrder", process.getProcOrder() != null ? process.getProcOrder().toString() : "0");
                map.put("procNo", process.getProcNo());
                map.put("procName", process.getProcName());
                map.put("procId", process.getId());
                map.put("jobAttr", 0);
                map.put("empId", null);
                map.put("empName", "");
                map.put("isCheck", 0);
                mapList.add(map);
            }
        }

        try{
            if(mapList.size() > 0){
                Collections.sort(mapList, new Comparator<Map<String, Object>>(){
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        Integer order1 = Integer.parseInt(o1.get("procOrder").toString());
                        Integer order2 = Integer.parseInt(o2.get("procOrder").toString());
                        if(order1.compareTo(order2) > 0){
                            return 1;
                        }
                        if(order1.compareTo(order2) == 0){
                            return 0;
                        }
                        return -1;
                    }
                });
            }
        }catch (Exception e){
        }

        return ApiResponseResult.success().data(DataGrid.create(mapList, mapList.size(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }


    /**
     * 获取生产制令单从表-工艺 存储过程调用
     * @param keyword
     * @param mid 指令单号
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getProcessListByProc(String keyword, String mid) throws Exception {
        SysUser currUser = UserUtil.getSessionUser();
        if (currUser == null) {
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getProdOrderPrc(UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getCompany() + "",
                UserUtil.getSessionUser().getId() + "", mid, "", "prc_mes_prod_order_proc");
            if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    //编辑工艺信息
    @Override
    @Transactional
    public ApiResponseResult editProcess(SchedulingProcess schedulingProcess) throws Exception{
        if(schedulingProcess == null || schedulingProcess.getId() == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        SchedulingProcess o = schedulingProcessDao.findById((long) schedulingProcess.getId());
        if(o == null){
            return ApiResponseResult.failure("工艺信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser!=null ? currUser.getId() : null);
        o.setProcOrder(schedulingProcess.getProcOrder());
        o.setJobAttr(schedulingProcess.getJobAttr()==null ? 0 : schedulingProcess.getJobAttr());
        o.setEmpId(schedulingProcess.getEmpId());
        if(schedulingProcess.getEmpId() != null){
            Employee employee = employeeDao.findById((long) schedulingProcess.getEmpId());
            if(employee != null){
                o.setEmpCode(employee.getEmpCode());
            }
        }
        schedulingProcessDao.save(o);

        return ApiResponseResult.success("编辑成功！");
    }

    /**
     * 保存工艺
     * @param mid
     * @param processIds
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult saveProcessProc(Long mid, String processIds) throws Exception{
        if(mid == null){
            return ApiResponseResult.failure("排产ID不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null && currUser.getId() == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }

        //调用保存存储过程
        Long userId = currUser.getId();
        String company = currUser.getCompany();
        String factory = currUser.getFactory();
        if(userId != null){
            List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String storedProc = "{call PRC_MES_TASK_PROC_UPD(?,?,?,?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, company);
                    cs.setString(2, factory);
                    cs.setLong(3, mid);
                    cs.setString(4, processIds);
                    cs.setString(5, userId.toString());
                    cs.registerOutParameter(6,Types.INTEGER);// 注册输出参数 返回标志
                    cs.registerOutParameter(7,java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                    return cs;
                }
            }, new CallableStatementCallback() {
                public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    List<String> result = new ArrayList<String>();
                    cs.execute();
                    result.add(cs.getString(6));
                    result.add(cs.getString(7));
                    return result;
                }
            });

            if(resultList.size() > 0){
                String flag = resultList.get(0);
                if(StringUtils.isNotEmpty(flag) && StringUtils.equals(flag, "0")){
                    return ApiResponseResult.success("保存成功！");
                }else{
                    return ApiResponseResult.failure(resultList.get(1));
                }
            }
        }

        return ApiResponseResult.success("保存失败！");
    }


    /**
     * 保存工艺
     * @param mid
     * @param fname
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult saveProc(Long mid, String fname) throws Exception {
        if (mid == null) {
            return ApiResponseResult.failure("排产ID不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();
        if (currUser == null && currUser.getId() == null) {
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        //调用保存存储过程
        Long userId = currUser.getId();
        String company = currUser.getCompany();
        String factory = currUser.getFactory();
        if (userId != null) {
            List<Object> resultList = (List<Object>) jdbcTemplate.execute(new CallableStatementCreator() {
                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String storedProc = "{call prc_mes_prod_proc_CRT(?,?,?,?,?,?,?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, company);
                    cs.setString(2, factory);
                    cs.setLong(3, mid);
                    cs.setString(4, fname);
                    cs.setString(5, userId.toString());
                    cs.registerOutParameter(6, Types.INTEGER);// 注册输出参数 返回标志
                    cs.registerOutParameter(7, java.sql.Types.VARCHAR);// 注册输出参数 返回信息
                    cs.registerOutParameter(8, -10);
                    return cs;
                }
            }, new CallableStatementCallback() {
                public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    List<Object> result = new ArrayList<>();
                    List<Map<String, Object>> l = new ArrayList();
                    cs.execute();
                    result.add(cs.getInt(6));
                    result.add(cs.getString(7));
                    System.out.println(l);
                    return result;
                }
            });
            if (resultList.size() > 0) {
                String num = resultList.get(0).toString();
                if (("0").equals(num)) {
                    return ApiResponseResult.success("保存成功！");
                } else {
                    return ApiResponseResult.failure(resultList.get(1).toString());
                }
            }
            return ApiResponseResult.success("保存成功！");
        }
        return ApiResponseResult.success("保存成功！");
    }

    /**
     * 获取生产制令单从表-组件
     * @param keyword
     * @param mid
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getItemList(String keyword, Long mid, PageRequest pageRequest) throws Exception{
        //查询条件1
        Sort sort = new Sort(Sort.Direction.ASC, "procOrder");
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("mid", SearchFilter.Operator.EQ, mid));

        //查询条件2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("taskNo", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("itemNo", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("itemUnit", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("empCode", SearchFilter.Operator.LIKE, keyword));
        }

        Specification<SchedulingItem> spec = Specification.where(BaseService.and(filters, SchedulingItem.class));
        Specification<SchedulingItem> spec1 =  spec.and(BaseService.or(filters1, SchedulingItem.class));
        Page<SchedulingItem> page = schedulingItemDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }


    /**
     * 获取上线人员清单
     * @param mid
     * @param mid
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getEmpList(Long mid, String keyword,PageRequest pageRequest) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getEmpListPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                 mid,"","",keyword==null?"":keyword, pageRequest.getPageNumber()+1, pageRequest.getPageSize(), "prc_mes_get_PROD_ORDER_EMP");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }


    /**
     * 获取生产投料
     * @param mid
     * @param mid
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getProdOrderList(Long mid, PageRequest pageRequest) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getEmpListPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                mid,"","","", pageRequest.getPageNumber()+1, pageRequest.getPageSize(), "prc_mes_get_PROD_ORDER_setup ");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("total", list.get(2));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }


    //获取上线人员清单 存储过程调用
    public List getEmpListPrc(String facoty, String company, Long mid, String startTime, String endTime, String keyword,
                              int page, int rows, String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setLong(3, mid);
                cs.setString(4, startTime);
                cs.setString(5, endTime);
                cs.setString(6, keyword);
                cs.setInt(7, rows);
                cs.setInt(8, page);
                cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(11, java.sql.Types.INTEGER);// 输出参数 总记录数
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

    //编辑组件信息
    @Override
    @Transactional
    public ApiResponseResult editItem(SchedulingItem schedulingItem) throws Exception{
        if(schedulingItem == null || schedulingItem.getId() == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        SchedulingItem o = schedulingItemDao.findById((long) schedulingItem.getId());
        if(o == null){
            return ApiResponseResult.failure("组件信息不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser!=null ? currUser.getId() : null);
        o.setEmpId(schedulingItem.getEmpId());
        if(schedulingItem.getEmpId() != null){
            Employee employee = employeeDao.findById((long) schedulingItem.getEmpId());
            if(employee != null){
                o.setEmpCode(employee.getEmpCode());
            }
        }
        schedulingItemDao.save(o);

        return ApiResponseResult.success("编辑成功！");
    }

    /**
     * 获取制令单产出送检从表
     * @param mid
     * @param mid
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getProdOrderOutList(String mid, PageRequest pageRequest) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getProdOrderPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                UserUtil.getSessionUser().getId()+"",mid,"", "prc_mes_prod_order_out");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    /**
     * 获取品质检验列表
     * @param mid
     * @param mid
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getProdOrderQcList(String mid, PageRequest pageRequest) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getProdOrderPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                UserUtil.getSessionUser().getId()+"",mid,"", "prc_mes_prod_order_qc");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }


    /**
     * 获取异常检验列表
     * @param mid
     * @param mid
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getProdOrderErrList(String mid, PageRequest pageRequest) throws Exception{
        SysUser currUser = UserUtil.getSessionUser();
        if(currUser == null){
            return ApiResponseResult.failure("当前用户已失效，请重新登录！");
        }
        List<Object> list = getProdOrderErrPrc(UserUtil.getSessionUser().getFactory()+"", UserUtil.getSessionUser().getCompany()+"",
                currUser.getId(),mid, "",pageRequest.getPageNumber()+1, pageRequest.getPageSize(),"prc_mes_get_prod_order_err");
        if (!list.get(1).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        Map map = new HashMap();
        map.put("count", list.get(0));
        map.put("rows", list.get(3));
        return ApiResponseResult.success("").data(map);
    }

    //获取上线人员清单 存储过程调用
    public List getProdOrderErrPrc(String facoty, String company, Long userId,String mid, String keyword,
                              int page, int rows, String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setLong(3, userId);
                cs.setString(4, mid);
                cs.setString(5,keyword);
                cs.setInt(6, rows);
                cs.setInt(7, page);
                cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 总记录数
                cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回信息
                cs.registerOutParameter(11, -10);// 输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(8));
                result.add(cs.getInt(9));
                if (cs.getString(9).toString().equals("0")) {
                    result.add(cs.getString(10));
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



    //获取上线人员清单 存储过程调用
    public List getProdOrderPrc(String facoty, String company,String user_id, String mid, String keyword,String prc_name) throws Exception{
        List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call  "+prc_name+" (?,?,?,?,?,?,?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, facoty);
                cs.setString(2, company);
                cs.setString(3, user_id);
                cs.setString(4, mid);
                cs.setString(5, keyword);
                cs.registerOutParameter(6, java.sql.Types.INTEGER);// 输出参数 返回标识
                cs.registerOutParameter(7, java.sql.Types.VARCHAR);// 输出参数 返回标识
                cs.registerOutParameter(8, -10);// 输出参数 返回数据集合
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<Object> result = new ArrayList<>();
                List<Map<String, Object>> l = new ArrayList();
                cs.execute();
                result.add(cs.getInt(6));
                result.add(cs.getString(7));
                if (cs.getString(6).toString().equals("0")) {
                    result.add(cs.getString(7));
                    // 游标处理
                    ResultSet rs = (ResultSet) cs.getObject(8);
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
}
