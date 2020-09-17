package com.web.produce.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.produce.dao.SchedulingDao;
import com.web.produce.entity.Scheduling;
import com.web.produce.service.SchedulingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 排产信息
 */
@Service(value = "SchedulingService")
@Transactional(propagation = Propagation.REQUIRED)
public class SchedulingImpl implements SchedulingService {

    @Autowired
    private SchedulingDao schedulingDao;

    @Override
    @Transactional
    public ApiResponseResult add(Scheduling scheduling) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public ApiResponseResult edit(Scheduling scheduling) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
        return null;
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
            filters1.add(new SearchFilter("bsCustomer", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsLine", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsOrderType", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsOrderNo", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<Scheduling> spec = Specification.where(BaseService.and(filters, Scheduling.class));
        Specification<Scheduling> spec1 =  spec.and(BaseService.or(filters1, Scheduling.class));
        Page<Scheduling> page = schedulingDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

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
}
