package com.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


/**
 * @Author：haung
 * @Date：2019-09-21 11:21
 * @Description：Excel导出工具类，依赖于ClassUtil工具类
 */
public final class ExcelExport {

    /**
     * 将传入的数据导出excel表并下载
     * @param response 返回的HttpServletResponse
     * @param importlist 要导出的对象的集合
     * @param attributeNames 含有每个对象属性在excel表中对应的标题字符串的数组（请按对象中属性排序调整字符串在数组中的位置）
     * @throws Exception 
     */
    public static void export(HttpServletResponse response, List<Map<String, Object>> importlist, String[] attributeNames, String[] mapNames,String file_name) throws Exception {
        //获取数据集
        List<Map<String, Object>> datalist = importlist;

        //声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        //生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        //设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 18);

        //获取字段名数组
        String[] tableAttributeName = attributeNames;
        /*//获取对象属性
        Field[] fields = ClassUtil.getClassAttribute(importlist.get(0));
        //获取对象get方法
        List<Method> methodList = ClassUtil.getMethodGet(importlist.get(0));*/

        //循环字段名数组，创建标题行-16
        Row row = sheet.createRow(0);
        for (int j = 0; j< tableAttributeName.length; j++){
            //创建列
            Cell cell = row.createCell(j);
            //设置单元类型为String
            //cell.setCellType(CellType.);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(transCellType(tableAttributeName[j]));
        }
        //创建普通行
        for (int i = 0;i<datalist.size();i++){
            //因为第一行已经用于创建标题行，故从第二行开始创建
            row = sheet.createRow(i+1);
            //如果是第一行就让其为标题行
            Object targetObj = datalist.get(i);
            for (int j = 0;j<mapNames.length;j++){
                //创建列
                Cell cell = row.createCell(j);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                //
                //try {
                    Object value = datalist.get(i).get(mapNames[j]);//methodList.get(j).invoke(targetObj, new Object[]{});
                    cell.setCellValue(transCellType(value));
                /*} catch (IllegalAccessException e) {
                    e.printStackTrace();
                }*/
            }
        }
        response.setContentType("application/octet-stream");
        //默认Excel名称
       // response.setHeader("Content-Disposition", "attachment;fileName="+file_name);
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(file_name, "UTF-8"));

        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //一定要在XSSFWorkbook用输入流当成构造函数参数创建新对象后，再使用输入流
    public static void export(HttpServletResponse response, List<Map<String, Object>> importlist,XSSFWorkbook workbook, String[] mapNames,String filePath, String file_name) throws Exception {
        //获取数据集
        List<Map<String, Object>> datalist = importlist;
        //获取第一页
        Resource resource = new ClassPathResource(filePath);
        InputStream in = resource.getInputStream();
        workbook = new XSSFWorkbook(in);
        XSSFSheet sheet = workbook.getSheetAt(0);
        //循环字段名数组，创建标题行-16
        Row row = sheet.getRow(0);

        //创建普通行
        for (int i = 0;i<datalist.size();i++){
            //因为前两行已经用于创建标题行，故从第二行开始创建
            row = sheet.createRow(i+2);
            //如果是第一行就让其为标题行
            Object targetObj = datalist.get(i);
            for (int j = 0;j<mapNames.length;j++){
                //创建列
                Cell cell = row.createCell(j);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                //
                //try {
                Object value = datalist.get(i).get(mapNames[j]);//methodList.get(j).invoke(targetObj, new Object[]{});
                cell.setCellValue(transCellType(value));
                /*} catch (IllegalAccessException e) {
                    e.printStackTrace();
                }*/
            }
        }

//        XSSFSheet lastSheet = workbook.getSheetAt(0);
//        int lastRowNum = lastSheet.getLastRowNum();
//        Row lastRow = lastSheet.getRow(lastRowNum);
//        lastRow.getCell(0).setCellValue("");
//
        response.setContentType("application/octet-stream");
        //默认Excel名称
        // response.setHeader("Content-Disposition", "attachment;fileName="+file_name);
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(file_name, "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        try {
            response.flushBuffer();
            workbook.write(outputStream);
//            response.getOutputStream().close();
//            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void exportByRow(HttpServletResponse response, List<Map<String, Object>> importlist,XSSFWorkbook workbook, String[] mapNames,String filePath, String file_name,Integer titleRow) throws Exception {
        //获取数据集
        List<Map<String, Object>> datalist = importlist;
        //获取第一页
        Resource resource = new ClassPathResource(filePath);
        InputStream in = resource.getInputStream();
        workbook = new XSSFWorkbook(in);
        XSSFSheet sheet = workbook.getSheetAt(0);
        //循环字段名数组，创建标题行-16
        Row row = sheet.getRow(0);

        //创建普通行
        for (int i = 0;i<datalist.size();i++){
            //因为前两行已经用于创建标题行，故从第二行开始创建
            row = sheet.createRow(i+titleRow);
            //如果是第一行就让其为标题行
            Object targetObj = datalist.get(i);
            for (int j = 0;j<mapNames.length;j++){
                //创建列
                Cell cell = row.createCell(j);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                //
                //try {
                Object value = datalist.get(i).get(mapNames[j]);//methodList.get(j).invoke(targetObj, new Object[]{});
                cell.setCellValue(transCellType(value));
                /*} catch (IllegalAccessException e) {
                    e.printStackTrace();
                }*/
            }
        }

//        XSSFSheet lastSheet = workbook.getSheetAt(0);
//        int lastRowNum = lastSheet.getLastRowNum();
//        Row lastRow = lastSheet.getRow(lastRowNum);
//        lastRow.getCell(0).setCellValue("");
//
        response.setContentType("application/octet-stream");
        //默认Excel名称
        // response.setHeader("Content-Disposition", "attachment;fileName="+file_name);
        response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(file_name, "UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        try {
            response.flushBuffer();
            workbook.write(outputStream);
//            response.getOutputStream().close();
//            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String transCellType(Object value){
        String str = null;
        if (value instanceof Date){
            Date date = (Date) value;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = sdf.format(date);
        }else{
            str = String.valueOf(value);
            if (str == "null"){
                str = "";
            }
        }

        return str;
    }

}