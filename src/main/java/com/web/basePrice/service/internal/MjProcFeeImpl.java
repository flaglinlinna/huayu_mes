package com.web.basePrice.service.internal;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.system.file.dao.CommonFileDao;
import com.system.file.dao.FsFileDao;
import com.system.file.entity.CommonFile;
import com.system.file.entity.FsFile;
import com.system.user.dao.SysUserDao;
import com.system.user.entity.SysUser;
//import com.web.basePrice.entity.CustomQsFile;
import com.utils.ExcelExport;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.Proc;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.MjProcFeeDao;
import com.web.basePrice.dao.MjProcFeeDao;
import com.web.basePrice.entity.MjProcFee;
import com.web.basePrice.service.MjProcFeeService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @date Dec 21, 2020 4:27:53 PM
 */
@Service(value = "MjProcFeeService")
@Transactional(propagation = Propagation.REQUIRED)
public class MjProcFeeImpl implements MjProcFeeService {
    @Autowired
    private MjProcFeeDao mjProcFeeDao;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private FsFileDao fsFileDao;

    @Autowired
    private CommonFileDao commonFileDao;
   
    
    @Override
    @Transactional
    public ApiResponseResult add(MjProcFee mjProcFee) throws Exception{
    if(mjProcFee == null){
    	return ApiResponseResult.failure("模具成本信息不可为空");
    }
    
   //生成模具编号
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String dateStr = sdf.format(new Date());
    mjProcFee.setProductCode("MJ-" + dateStr);  //编号格式：MJ-年月日时分秒
    
    mjProcFee.setCreateDate(new Date());
    mjProcFee.setCreateBy(UserUtil.getSessionUser().getId());
    mjProcFeeDao.save(mjProcFee);

        String[] fileIds =  mjProcFee.getFileId().split(",");
        List<CommonFile> fileList = new ArrayList<>();
        for(String fileId :fileIds){
            if(StringUtils.isNotEmpty(fileId)){
                FsFile fsFile = fsFileDao.findById(Long.parseLong(fileId));
                if(fsFile.getDelFlag()==0) {
                    CommonFile qsFile = new CommonFile();
                    qsFile.setmId(mjProcFee.getId());
                    qsFile.setFileId(fsFile.getId());
                    qsFile.setFileName(fsFile.getBsName());
                    qsFile.setCreateDate(new Date());
                    qsFile.setCreateBy(UserUtil.getSessionUser().getId());
                    qsFile.setBsType("ProcFee");
                    fileList.add(qsFile);
                }
            }
        }
        commonFileDao.saveAll(fileList);
    return ApiResponseResult.success("添加模具成本信息成功").data(mjProcFee);
    }

    /**
    * 修改
    */
    @Override
    @Transactional
    public ApiResponseResult edit(MjProcFee mjProcFee) throws Exception {
    if(mjProcFee == null){
    	return ApiResponseResult.failure("模具成本信息不可为空");
      }
    if(mjProcFee.getId() == null){
    	return ApiResponseResult.failure("模具成本信息ID不可为空");
    }
    MjProcFee o= mjProcFeeDao.findById((long)mjProcFee.getId());
    o.setLastupdateDate(new Date());
    o.setLastupdateBy(UserUtil.getSessionUser().getId());
    
    o.setFimg(mjProcFee.getFimg());//图示
	o.setProductName(mjProcFee.getProductName());//产品名称
	o.setStructureMj(mjProcFee.getStructureMj());//模具结构
	o.setMjPrice(mjProcFee.getMjPrice());// mo ju jia ge
	o.setNumHole(mjProcFee.getNumHole());//穴数
	o.setFeeProc(mjProcFee.getFeeProc());//工序费用（元/小时）
	o.setFeeType1(mjProcFee.getFeeType1());//
	o.setFeeType2(mjProcFee.getFeeType2());//
	o.setFeeType3(mjProcFee.getFeeType3());//
	o.setFeeType4(mjProcFee.getFeeType4());//
	o.setStQuote(mjProcFee.getStQuote());//参考报价
	o.setFeeAll(mjProcFee.getFeeAll());//评估总费用（含税）
    
    mjProcFeeDao.save(o);
        String[] fileIds =  mjProcFee.getFileId().split(",");
        List<CommonFile> fileList = new ArrayList<>();
        for(String fileId :fileIds){
            if(StringUtils.isNotEmpty(fileId)){
                FsFile fsFile = fsFileDao.findById(Long.parseLong(fileId));
                if(fsFile.getDelFlag()==0) {
                    CommonFile qsFile = new CommonFile();
                    qsFile.setmId(mjProcFee.getId());
                    qsFile.setFileId(fsFile.getId());
                    qsFile.setFileName(fsFile.getBsName());
                    qsFile.setCreateDate(new Date());
                    qsFile.setCreateBy(UserUtil.getSessionUser().getId());
                    qsFile.setBsType("ProcFee");
                    fileList.add(qsFile);
                }
            }
        }
        commonFileDao.saveAll(fileList);
    return ApiResponseResult.success("编辑成功！");
}

    /**
    * 删除
    */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
    if(id == null){
    	return ApiResponseResult.failure("模具成本信息id不可为空");
    }
    MjProcFee o  = mjProcFeeDao.findById((long) id);
    if(o == null){
    	return ApiResponseResult.failure("模具成本信息不存在");
    }
    o.setDelFlag(1);
    mjProcFeeDao.save(o);
    return ApiResponseResult.success("删除成功！");
    }

    /**
    * 查询
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
            filters1.add(new SearchFilter("productName", SearchFilter.Operator.LIKE, keyword));
            }
            Specification<MjProcFee> spec = Specification.where(BaseService.and(filters, MjProcFee.class));
            Specification<MjProcFee> spec1 = spec.and(BaseService.or(filters1, MjProcFee.class));
            Page<MjProcFee> page = mjProcFeeDao.findAll(spec1, pageRequest);
            List<MjProcFee> mjProcFeeList = page.getContent();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<Map<String,Object>> mapList = new ArrayList<>();
            for(MjProcFee mjProcFee:mjProcFeeList){
                Map<String,Object> map = new HashMap<>();
                map.put("id",mjProcFee.getId());
                map.put("productCode",mjProcFee.getProductCode());
                map.put("fimg",mjProcFee.getFimg());
                map.put("productName",mjProcFee.getProductName());
                map.put("structureMj",mjProcFee.getStructureMj());
                map.put("numHole",mjProcFee.getNumHole());
                map.put("feeProc",mjProcFee.getFeeProc());
                map.put("mjPrice",mjProcFee.getMjPrice());
                
                map.put("feeType1",mjProcFee.getFeeType1());
                map.put("feeType2",mjProcFee.getFeeType2());
                map.put("feeType3",mjProcFee.getFeeType3());
                map.put("feeType4",mjProcFee.getFeeType4());
                
                map.put("stQuote",mjProcFee.getStQuote());
                map.put("feeAll",mjProcFee.getFeeAll());
                map.put("fmemo",mjProcFee.getFmemo());
                if(mjProcFee.getCreateBy()!=null) {
                    map.put("createBy", sysUserDao.findById((long) mjProcFee.getCreateBy()).getUserName());
                    map.put("createDate", df.format(mjProcFee.getCreateDate()));
                }
                if(mjProcFee.getLastupdateBy()!=null){
                    SysUser updateUser = sysUserDao.findById((long)mjProcFee.getCreateBy());
                    if(updateUser!=null){
                        map.put("lastupdateBy",updateUser.getUserName());
                        map.put("department",updateUser.getDepartment());
                    }
                    map.put("lastupdateDate",df.format(mjProcFee.getLastupdateDate()));
                }
                mapList.add(map);
            }
            return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
            pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
        }

    /**
     *
     * @param customId 主表id
     * @return
     * @throws Exception
     */
    @Override
    public ApiResponseResult getFileList(Long customId) throws Exception {
        List<CommonFile> customQsFiles = commonFileDao.findByDelFlagAndMIdAndBsType(0,customId,"ProcFee");
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(CommonFile qsFile :customQsFiles){
            Map<String, Object> map = new HashMap<>();
            map.put("id",qsFile.getFileId());
            map.put("qsFileId",qsFile.getId());
            map.put("bsName",qsFile.getFileName());
            map.put("bsContentType","stp");
            mapList.add(map);
        }
        return ApiResponseResult.success().data(mapList);
    }

    /**
     * 删除客户品质标准附件
     */
    @Override
    @Transactional
    public ApiResponseResult delFile(Long id,Long fileId) throws Exception {
        if (id == null) {
            return ApiResponseResult.failure("客户品质标准信息ID不能为空！");
        }
        CommonFile o = commonFileDao.findById((long) id);
        if (o == null) {
            return ApiResponseResult.failure("客户品质标准信息不存在！");
        }
        o.setDelFlag(1);
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setLastupdateDate(new Date());
        commonFileDao.save(o);

        FsFile fsFile = fsFileDao.findById((long) fileId);
        fsFile.setDelFlag(1);
        fsFileDao.save(fsFile);

        return ApiResponseResult.success("删除附件成功！");
    }

    //防止读取Excel为null转String 报空指针异常
    public String tranCell(Object object)
    {
        if(object==null||object==""||("").equals(object)){
            return null;
        }else return object.toString().trim();
    }

    @Override
    public ApiResponseResult doExcel(MultipartFile[] file) throws Exception {
        try {
            Date doExcleDate = new Date();
            Long userId = UserUtil.getSessionUser().getId();
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
			Integer successes = 0;
            Integer failures = 0;
            List<MjProcFee> mjProcFeeList = new ArrayList<>();
            for (int row = 2; row <= maxRow; row++) {
//				String errInfo = "";
                //产品*	 穴数*	模具结构	模具报价价格(未税)*	材料成本(未税)*	制造成本(未税)*
                // 外发纹理费用(未税)*	参考报价*	评估总费用(未税)	备注
                String id = tranCell(sheet.getRow(row).getCell(0)); //id
                String productCode = tranCell(sheet.getRow(row).getCell(1));
                String productName = tranCell(sheet.getRow(row).getCell(2));
                String numHole = tranCell(sheet.getRow(row).getCell(3));
                String structureMj = tranCell(sheet.getRow(row).getCell(4));
                String mjPrice = tranCell(sheet.getRow(row).getCell(5));
                String feeType1 = tranCell(sheet.getRow(row).getCell(6));
                String feeType2 = tranCell(sheet.getRow(row).getCell(7));
                String feeType3 = tranCell(sheet.getRow(row).getCell(8));
                String stQuote = tranCell(sheet.getRow(row).getCell(9));
                String feeAll = tranCell(sheet.getRow(row).getCell(10));
                String fmemo = tranCell(sheet.getRow(row).getCell(11));

                MjProcFee mjProcFee = new MjProcFee();
                if(StringUtils.isNotEmpty(id)){
                    mjProcFee = mjProcFeeDao.findById(Long.parseLong(id));
                }else {
                    //生成模具编号
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String dateStr = sdf.format(new Date());
                    mjProcFee.setProductCode("MJ-" + dateStr);  //编号格式：MJ-年月日时分秒
                }

                mjProcFee.setProductName(productName);
                mjProcFee.setNumHole(new BigDecimal(numHole));
                mjProcFee.setStructureMj(structureMj);
                mjProcFee.setMjPrice(new BigDecimal(mjPrice));
                mjProcFee.setFeeType1(new BigDecimal(feeType1));
                mjProcFee.setFeeType2(new BigDecimal(feeType2));
                mjProcFee.setFeeType3(new BigDecimal(feeType3));
                mjProcFee.setStQuote(new BigDecimal(stQuote));
                mjProcFee.setFeeAll(new BigDecimal(feeAll));
                mjProcFee.setFmemo(fmemo);

                mjProcFee.setCreateBy(userId);
                mjProcFee.setCreateDate(doExcleDate);

                mjProcFeeList.add(mjProcFee);
                successes++;
            }
            mjProcFeeDao.saveAll(mjProcFeeList);
            return ApiResponseResult.success("导入成功!,共导入:" + mjProcFeeList.size() + ";不通过:" + failures);
        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
        }
    }


    @Override
    @Transactional
    public void exportExcel(HttpServletResponse response, String keyword) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            filters1.add(new SearchFilter("productName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<MjProcFee> spec = Specification.where(BaseService.and(filters, MjProcFee.class));
        Specification<MjProcFee> spec1 = spec.and(BaseService.or(filters1, MjProcFee.class));
        List<MjProcFee> mjProcFeeList = mjProcFeeDao.findAll(spec1);
        String excelPath = "static/excelFile/";
        String fileName = "模具成本维护模板.xlsx";
        String[] map_arr = new String[]{"id","productCode","productName","numHole","structureMj","mjPrice","feeType1","feeType2","feeType3","stQuote","feeAll","fmemo"};
        XSSFWorkbook workbook = new XSSFWorkbook();
//		List<Proc> procList = page.getContent();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String, Object>> mapList = new ArrayList<>();
        for(MjProcFee o :mjProcFeeList){
            Map<String,Object> map =new HashMap<>();
            map = JSONObject.parseObject(JSONObject.toJSONString(o),Map.class);
            mapList.add(map);
        }
        ExcelExport.export(response,mapList,workbook,map_arr,excelPath+fileName,fileName);

    }

}