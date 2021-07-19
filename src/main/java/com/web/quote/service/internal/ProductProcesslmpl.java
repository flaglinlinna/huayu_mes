package com.web.quote.service.internal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.system.file.entity.FsFile;
import com.web.basePrice.dao.BaseFeeDao;
import com.web.basePrice.entity.BaseFee;
import com.web.basePrice.entity.BaseFeeFile;
import com.web.basePrice.entity.MjProcFee;
import com.web.quote.dao.*;
import com.web.quote.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.ExcelExport;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.entity.Proc;
import com.web.quote.service.ProductProcessService;
import com.web.quote.service.QuoteProductService;

@Service(value = "ProductProcessService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductProcesslmpl implements ProductProcessService {

    @Autowired
    private ProductProcessDao productProcessDao;

    @Autowired
    private ProductMaterDao productMaterDao;

    @Autowired
    private ProductProcessTempDao productProcessTempDao;

    @Autowired
    private ProcDao procDao;
    @Autowired
    QuoteProcessDao quoteProcessDao;
    @Autowired
    private QuoteItemDao quoteItemDao;
    @Autowired
    private QuoteProductService quoteProductService;
    @Autowired
    private QuoteDao quoteDao;
    @Autowired
    private BaseFeeDao baseFeeDao;
    @Autowired
    private QuoteBomDao quoteBomDao;

    /**
     * 新增报价单
     */
    @Override
    @Transactional
    public ApiResponseResult add(ProductProcess productProcess) throws Exception {
        if (productProcess == null) {
            return ApiResponseResult.failure("制造部材料信息不能为空！");
        }
//        List<QuoteProcess>  quoteProcessList = quoteProcessDao.findByDelFlagAndPkQuoteAndPkProcAndBsName(0, productProcess.getPkQuote(), productProcess.getPkProc(), productProcess.getBsName());

        List<BaseFee> baseFeeList = baseFeeDao.findByDelFlagAndProcId(0, productProcess.getPkProc());
        if (baseFeeList.size() == 0) {
            return ApiResponseResult.failure("该工序未维护人工制费,请维护后再选择！");
        } else {
            productProcess.setBsFeeMh(new BigDecimal(baseFeeList.get(0).getFeeMh()));
            productProcess.setBsFeeLh(new BigDecimal(baseFeeList.get(0).getFeeLh()));
        }

        productProcess.setCreateDate(new Date());
        productProcess.setCreateBy(UserUtil.getSessionUser().getId());


        productProcessDao.save(productProcess);
        return ApiResponseResult.success("制造部材料信息添加成功！").data(productProcess);
    }

    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ProductProcess productProcess) throws Exception {
        if (productProcess == null) {
            return ApiResponseResult.failure("制造部材料信息不能为空！");
        }
        if (productProcess.getId() == null) {
            return ApiResponseResult.failure("制造部材料信息ID不能为空！");
        }

        ProductProcess o = productProcessDao.findById((long) productProcess.getId());
        if (o == null) {
            return ApiResponseResult.failure("该制造部材料信息不存在！");
        }
        o.setBsName(productProcess.getBsName());
        o.setBsOrder(productProcess.getBsOrder());


        //查找工序的制费信息
        o.setPkProc(productProcess.getPkProc());
//        List<QuoteProcess>  quoteProcessList = quoteProcessDao.findByDelFlagAndPkQuoteAndPkProcAndBsName(0, o.getPkQuote(), productProcess.getPkProc(), productProcess.getBsName());

        List<BaseFee> baseFeeList = baseFeeDao.findByDelFlagAndProcId(0, productProcess.getPkProc());
        if (baseFeeList.size() == 0) {
            return ApiResponseResult.failure("该工序未维护人工制费,请维护后再选择！");
        } else {
            productProcess.setBsFeeMh(new BigDecimal(baseFeeList.get(0).getFeeMh()));
            productProcess.setBsFeeLh(new BigDecimal(baseFeeList.get(0).getFeeLh()));
        }

        o.setBsModelType(productProcess.getBsModelType());
        o.setBsCycle(productProcess.getBsCycle());
        o.setBsCapacity(productProcess.getBsCapacity());
        o.setBsYield(productProcess.getBsYield());
        o.setBsCave(productProcess.getBsCave());
        o.setBsUserNum(productProcess.getBsUserNum());
        o.setBsRadix(productProcess.getBsRadix());
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setFmemo(productProcess.getFmemo());
        o.setBsLoss(productProcess.getBsLoss());
        o.setBsFeeWxAll(productProcess.getBsFeeWxAll());
        o.setFileId(productProcess.getFileId());
        o.setFileName(productProcess.getFileName());
        productProcessDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult editFileId(Long id,Long fileId,String fileName) throws Exception {
        ProductProcess o = productProcessDao.findById((long) id);
        if (o == null) {
            return ApiResponseResult.failure("该组件信息不存在！");
        }
        o.setFileId(fileId);
        o.setFileName(fileName);
        productProcessDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }

    /**
     * 删除异常类别
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
        if (id == null) {
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        ProductProcess o = productProcessDao.findById((long) id);
        if (o == null) {
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        productProcessDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 删除异常类别
     */
    @Override
    @Transactional
    public ApiResponseResult deleteIds(String ids) throws Exception {
        String[] idsArry = ids.split(",");
        List<ProductProcess> ppList = new ArrayList<>();
        for(String id : idsArry){
            ProductProcess o = productProcessDao.findById( Long.parseLong(id));
            if (o == null) {
                return ApiResponseResult.failure("制造工艺不存在！");
            }
            o.setDelTime(new Date());
            o.setDelFlag(1);
            o.setDelBy(UserUtil.getSessionUser().getId());
            ppList.add(o);
        }

        productProcessDao.saveAll(ppList);
        return ApiResponseResult.success("删除成功！");
    }

    /**
     * 修改机种类型
     */
    @Override
    @Transactional
    public ApiResponseResult updateModelType(Long id, String bsModelType) throws Exception {
        if (id == null) {
            return ApiResponseResult.failure("制造工艺ID不能为空！");
        }
        ProductProcess o = productProcessDao.findById((long) id);
        if (o == null) {
            return ApiResponseResult.failure("制造工艺不存在！");
        }
        o.setBsModelType(bsModelType);
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setLastupdateDate(new Date());
        productProcessDao.save(o);
        return ApiResponseResult.success("更新机台类型成功！");
    }

    //防止读取Excel为null转String 报空指针异常
    public String tranCell(Object object) {
        if (object == null || object == "" || ("").equals(object)) {
            return null;
        } else return object.toString().trim();
    }

    public void exportExcel(HttpServletResponse response, String bsType, Long quoteId) throws Exception {
        String excelPath = "static/excelFile/";
        String fileName = "";
        String[] map_arr = null;
        //五金工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、成型周期(S)、工序良率、备注
        //注塑工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、穴数、成型周期(S)、加工人数、工序良率、备注
        //组装工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注
        //表面工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注

        //2021-04-29 模板变更  （0626 增加组件名称）
        // 五金  零件名称1、工序顺序2、工序名称3、机台类型4、人数5、成型周期(S)6、工序良率7
        //注塑 零件名称1、工序顺序2、工序名称3、机台类型4、人数5、成型周期(S)6、工序良率7、穴数8
        //组装 零件名称1、工序顺序2、工序名称3、机台类型4、人数5、工序良率6、产能7
        //表面 零件名称1、工序顺序2、工序名称3、机台类型4、人数5、工序良率6、产能7
        //外协:零件名称1、工序顺序2、工序名称3、机台类型4、损耗率5、外协价格6 、备注7

        if (("hardware").equals(bsType)) {
            fileName = "五金工艺模板.xlsx";
            map_arr = new String[]{"id", "bsElement","bsName", "bsOrder", "procName", "bsModelType", "bsUserNum", "bsCycle", "bsYield", "fmemo"};
        } else if (("molding").equals(bsType)) {
            fileName = "注塑工艺模板.xlsx";
            map_arr = new String[]{"id", "bsElement","bsName", "bsOrder", "procName", "bsModelType", "bsUserNum", "bsCycle", "bsYield", "bsCave", "fmemo"};
        } else if (("surface").equals(bsType)) {
            fileName = "表面处理工艺模板.xlsx";
            map_arr = new String[]{"id","bsElement", "bsName", "bsOrder", "procName", "bsModelType", "bsUserNum", "bsYield", "bsCapacity", "fmemo"};
        } else if (("packag").equals(bsType)) {
            fileName = "组装工艺模板.xlsx";
            map_arr = new String[]{"id", "bsElement","bsName", "bsOrder", "procName", "bsModelType", "bsUserNum", "bsYield", "bsCapacity", "fmemo"};
        } else if (("out").equals(bsType)) {
            fileName = "外协填报价格模板.xlsx";
            map_arr = new String[]{"id","bsElement", "bsName", "bsOrder", "procName", "bsModelType", "bsLoss", "bsFeeWxAll", "fmemo"};
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        Resource resource = new ClassPathResource(excelPath + fileName);
        InputStream in = resource.getInputStream();

//        XSSFWorkbook workbook = new XSSFWorkbook(in);
//        in.close();
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("bsType", SearchFilter.Operator.EQ, bsType));
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, quoteId));
        Specification<ProductProcess> spec = Specification.where(BaseService.and(filters, ProductProcess.class));
        List<ProductProcess> productProcessesList = productProcessDao.findByDelFlagAndPkQuoteAndBsTypeOrderByBsOrder(0,quoteId,bsType);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ProductProcess bs : productProcessesList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", bs.getId());
            map.put("bsElement", bs.getBsElement());
            map.put("bsName", bs.getBsName());
            map.put("bsOrder", bs.getBsOrder());
            if (bs.getProc() != null) {
                map.put("procName", bs.getProc().getProcName());
            } else {
                map.put("procName", "");
            }
//            map.put("bsModelType", bs.getBsModelType());
            if (bs.getBjModelType() != null) {
                map.put("bsModelType", bs.getBjModelType().getModelName());
            }
            map.put("bsUserNum", bs.getBsUserNum());
            map.put("bsCycle", bs.getBsCycle());
            map.put("bsYield", bs.getBsYield());
            map.put("fmemo", bs.getFmemo());
            map.put("bsCave", bs.getBsCave());
            map.put("bsCapacity", bs.getBsCapacity());
            map.put("bsLoss", bs.getBsLoss());
            map.put("bsFeeWxAll", bs.getBsFeeWxAll());
            list.add(map);
        }
        ExcelExport.export(response, list, workbook, map_arr, excelPath + fileName, fileName);
    }


    //    导入模板
    public ApiResponseResult doExcel(MultipartFile[] file, String bsType, Long quoteId) throws Exception {
        try {
            Date doExcleDate = new Date();
            Long userId = UserUtil.getSessionUser().getId();
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            List<ProductProcess> hardwareMaterList = new ArrayList<>();
            //五金工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、成型周期(S)、工序良率、备注
            //注塑工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、穴数、成型周期(S)、加工人数、工序良率、备注
            //组装工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注
            //表面工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注
            //外协报价导入顺序:零件名称、工序顺序、工序名称、机台类型、损耗率、外协价格 、备注
            for (int row = 2; row <= maxRow; row++) {
                String id = tranCell(sheet.getRow(row).getCell(0));
                String bsName = tranCell(sheet.getRow(row).getCell(1));
                String bsOrder = tranCell(sheet.getRow(row).getCell(2));
                String procName = tranCell(sheet.getRow(row).getCell(3));
                String bsModelType = tranCell(sheet.getRow(row).getCell(4));

                String bsRadix = tranCell(sheet.getRow(row).getCell(5));
                String row6 = tranCell(sheet.getRow(row).getCell(6));
                String row7 = tranCell(sheet.getRow(row).getCell(7));
                String row8 = tranCell(sheet.getRow(row).getCell(8));
                String row9 = tranCell(sheet.getRow(row).getCell(9));
                String row10 = tranCell(sheet.getRow(row).getCell(10));
                ProductProcess process = new ProductProcess();

                //设置类型
                process.setBsType(bsType);
                process.setPkQuote(quoteId);
                process.setBsName(bsName);
                if (StringUtils.isNotEmpty(id)) {
                    process.setId(Long.parseLong(id));
                    process.setLastupdateBy(userId);
                    process.setLastupdateDate(doExcleDate);
                } else {
                    process.setCreateBy(userId);
                    process.setCreateDate(doExcleDate);
                }
                process.setBsOrder((int) Double.parseDouble(bsOrder));
                process.setBsRadix(new BigDecimal(bsRadix));
                List<Proc> procList = procDao.findByDelFlagAndProcName(0, procName);
                if (procList.size() > 0 && procList != null) {
                    process.setPkProc(procList.get(0).getId());
                }
                process.setBsModelType(bsModelType);
                if (("molding").equals(bsType)) {
                    //注塑
                    process.setBsCave(row6);
                    process.setBsCycle(new BigDecimal(row7));
                    process.setBsUserNum(new BigDecimal(row8));
                    process.setBsYield(new BigDecimal(row9));
                    process.setFmemo(row10);
                } else if (("hardware").equals(bsType)) {
                    //五金
                    process.setBsUserNum(new BigDecimal(row6));
                    process.setBsCycle(new BigDecimal(row7));
                    process.setBsYield(new BigDecimal(row8));
                    process.setFmemo(row9);
                } else {
                    //组装和表面
                    process.setBsUserNum(new BigDecimal(row6));
                    process.setBsCapacity(row7);
                    process.setBsYield(new BigDecimal(row8));
                    process.setFmemo(row9);
                }
                hardwareMaterList.add(process);
            }
            productProcessDao.saveAll(hardwareMaterList);
            return ApiResponseResult.success("导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
        }
    }

    /**
     * 查询列表
     */
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword, String bsType, String quoteId, PageRequest pageRequest) throws Exception {
        // 查询条件1
        if(bsType.equals("out")){
//            UserUtil.getSessionUser().getId();
            try {
                Page<ProductProcess>  page =  productProcessDao.findOutListByUserId(Long.parseLong(quoteId),UserUtil.getSessionUser().getId(),pageRequest);
                return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                        pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));

            }catch (Exception e){
                e.printStackTrace();
            }
          }
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if (StringUtils.isNotEmpty(bsType)) {
            filters.add(new SearchFilter("bsType", SearchFilter.Operator.EQ, bsType));
        }
        if (!"null".equals(quoteId) && quoteId != null) {
            filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, quoteId));
        } else {
            List<ProductProcess> productMaterList = new ArrayList<>();
            return ApiResponseResult.success().data(DataGrid.create(productMaterList, 0,
                    1, 10));
        }
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
//            filters1.add(new SearchFilter("bsMaterName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<ProductProcess> spec = Specification.where(BaseService.and(filters, ProductProcess.class));
        Specification<ProductProcess> spec1 = spec.and(BaseService.or(filters1, ProductProcess.class));
        Page<ProductProcess> page = productProcessDao.findAll(spec1, pageRequest);

        List<Map<String, Object>> mapList = new ArrayList<>();

        List<Map<String, Object>> sumUser = productProcessDao.getSumByBsElement(Long.parseLong(quoteId),bsType);
        List<Map<String, Object>> minCapacity = productProcessDao.getMinCapaCityGroupBy(Long.parseLong(quoteId),bsType);

        for (ProductProcess pm : page.getContent()) {

            Map<String,Object> map =new HashMap<>();
            map = JSONObject.parseObject(JSONObject.toJSONString(pm),Map.class);
            List<Map<String, Object>> lm = productProcessDao.findByWorkcenter(pm.getPkProc(), pm.getProc().getBjWorkCenter().getId());

            //待优化
            if (lm.size() > 0) {
                String str1 = JSON.toJSONString(lm); //此行转换
                map.put("bsTypeList",str1);
            } else {
                map.put("bsTypeList",null);
            }

            for(Map<String,Object> map1 :sumUser){
                if(pm.getBsElement().equals(map1.get("BS_ELEMENT"))){
                    map.put("allUser",map1.get("ALLUSER")==null?0:map1.get("ALLUSER"));
                    map.put("allBsFeeLh",map1.get("BSFEELHALL")==null?0:map1.get("BSFEELHALL"));
                }
            }

            for(Map<String,Object> map2 :minCapacity){
                if(map2.get("BS_ELEMENT").equals(pm.getBsElement())){
                    map.put("minCapacity",map2.get("MINCAPACITY")==null?0:map2.get("MINCAPACITY"));
                }
            }

//            map.put("allUser",sumUser.get(0).get("ALLUSER")==null?0:sumUser.get(0).get("ALLUSER"));
//            if(minCapacity.size()>0){
//                map.put("minCapacity",minCapacity.get(0).get("MINCAPACITY"));
//            }else {
//                map.put("minCapacity",0);
//            }
            mapList.add(map);
        }

        return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 查询报价单下 工艺列表
     */
    @Override
    @Transactional
    public ApiResponseResult getListByPkQuote(Long pkQuote, PageRequest pageRequest) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        if (pkQuote != null) {
            filters1.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
        }
        Specification<ProductProcess> spec = Specification.where(BaseService.and(filters, ProductProcess.class));
        Specification<ProductProcess> spec1 = spec.and(BaseService.or(filters1, ProductProcess.class));
        Page<ProductProcess> page = productProcessDao.findAll(spec1, pageRequest);


        for (ProductProcess pm : page.getContent()) {
//        	List<Map<String, Object>> lm = productProcessDao.findByDelFlagAndWorkcenter( pm.getProc().getBjWorkCenter().getId());
            List<Map<String, Object>> lm = productProcessDao.findByWorkcenter(pm.getProc().getId(), pm.getProc().getBjWorkCenter().getId());
            if (lm.size() > 0) {
                String str1 = JSON.toJSONString(lm); //此行转换
                pm.setBsTypeList(str1);
            }
        }

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 确认完成
     */
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long quoteId, String bsType, String bsCode,List<ProductProcess> productProcessList) throws Exception {
        //判断状态是否已执行过确认提交-lst-20210112
        int i = quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0, quoteId, bsCode, 2);
        if (i > 0) {
            return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
        }

        productProcessDao.saveAll(productProcessList);



        List<ProductProcess> productMaterList = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, quoteId, bsType);
        for (ProductProcess o : productMaterList) {
            if (o.getPkProc() == null) {
                return ApiResponseResult.failure("工序名称不能为空,请检查后再确认！");
            }
            if("PCS".equals(o.getPurchaseUnit())&&!"packag".equals(bsType)){
                if ( o.getBsYield() == null || o.getBsYield() == BigDecimal.ZERO) {
                    return ApiResponseResult.failure("工序良率不能为空或者0,请检查后再确认！");
                }
            }
            else if ("hardware".equals(bsType)) {
                if (o.getBsUserNum() == null || o.getBsCycle() == null || o.getBsYield() == null) {
                    return ApiResponseResult.failure("人数、成型周期和工序良率不能为空或者0,请检查后再确认！");
                }
            } else if ("molding".equals(bsType)) {
                if (o.getBsUserNum() == null || o.getBsCycle() == null || o.getBsYield() == null) {
                    return ApiResponseResult.failure("人数、穴数、成型周期和工序良率不能为空,请检查后再确认！");
                }
            } else if ("surface".equals(bsType)) {
                if(o.getBsUserNum() ==BigDecimal.ZERO&&o.getBsYield() == BigDecimal.ZERO&&("0").equals(o.getBsCapacity())){

                }
                else if (o.getBsUserNum() == null || o.getBsYield() == null || o.getBsCapacity() == null
                        || o.getBsUserNum() == BigDecimal.ZERO || o.getBsYield() == BigDecimal.ZERO || o.getBsCapacity() == "0") {
                    return ApiResponseResult.failure("人数、工序良率、产能不能为空,请检查后再确认！");
                }
            } else if ("packag".equals(bsType)) {
                if(o.getBsUserNum() == BigDecimal.ZERO&&o.getBsYield() == BigDecimal.ZERO&&("0").equals(o.getBsCapacity())){

                }
                else if (o.getBsUserNum() == null || o.getBsYield() == null || o.getBsCapacity() == null) {
                    return ApiResponseResult.failure("人数、工序良率、产能不能为空,请检查后再确认！");
                }
            } else if ("out".equals(bsType)) {
                if (o.getBsFeeWxAll() == null || o.getBsLoss() == null) {
                    return ApiResponseResult.failure("损耗率、外协价格不能为空,请检查后再确认！");
                }
            }
            if(!("out").equals(o.getBsType())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String currentTime = sdf.format(new Date());
                if (StringUtils.isNotEmpty(o.getBsModelType())) {
                    List<BaseFee> baseFeeList = baseFeeDao.findByDelFlagAndProcIdAndModelCodeAndWorkCenterId(0, o.getPkProc(), o.getBsModelType(), o.getProc().getWorkcenterId());
                    if (baseFeeList.size() == 0) {
                        return ApiResponseResult.failure("存在工序:" + o.getProc().getProcName() + "未维护人工制费,请检查后再确认！");
                    } else {
                        if(baseFeeList.get(0).getExpiresTime()!=null) {
                            String expiresTime = sdf.format(baseFeeList.get(0).getExpiresTime());
                            if ((sdf.parse(expiresTime)).compareTo(sdf.parse(currentTime)) >= 0) {
                                //失效日期大于今天
                                o.setBsFeeMh(new BigDecimal(baseFeeList.get(0).getFeeMh()));
                                o.setBsFeeLh(new BigDecimal(baseFeeList.get(0).getFeeLh()));
                            }
                        }else {
                            return ApiResponseResult.failure("存在工序维护的人工制费已失效,请检查后再确认！");
                        }
                    }
                } else {
                    List<BaseFee> baseFeeList = baseFeeDao.findByDelFlagAndProcId(0, o.getPkProc());
                    if (baseFeeList.size() == 0) {
                        return ApiResponseResult.failure("存在工序未维护人工制费,请检查后再确认！");
                    } else {
                        if(baseFeeList.get(0).getExpiresTime()!=null) {
                            String expiresTime = sdf.format(baseFeeList.get(0).getExpiresTime());
                            if ((sdf.parse(expiresTime)).compareTo(sdf.parse(currentTime)) >= 0) {
                                //失效日期大于今天
                                o.setBsFeeMh(BigDecimal.ZERO);
                                o.setBsFeeLh(new BigDecimal(baseFeeList.get(0).getFeeLh()));
                            } else {
                                return ApiResponseResult.failure("存在工序维护的人工制费已失效,请检查后再确认！");
                            }
                        }else {
                            o.setBsFeeMh(BigDecimal.ZERO);
                            o.setBsFeeLh(new BigDecimal(baseFeeList.get(0).getFeeLh()));
                        }
                    }
                }
            }
//            o.setBsStatus(1);
//            o.setLastupdateDate(new Date());
//            o.setLastupdateBy(UserUtil.getSessionUser().getId());
        }

        List <ProductMater> materList = productMaterDao.findByDelFlagAndPkQuoteAndBsTypeAndBsSingleton(0,quoteId,bsType,0);
        for(ProductMater pm :materList){
            if(StringUtils.isEmpty(pm.getBsGroups())){
                List<ProductProcess> processList = productProcessDao.findByBsNameAndBsElementAndPkQuoteAndBsTypeAndDelFlagAndBsMaterNameOrderByBsOrderDesc(
                        pm.getBsComponent(), pm.getBsElement(), pm.getPkQuote(), pm.getBsType(), 0, pm.getBsMaterName());
//                pm.setBsYield(processList.size() > 0 ? processList.get(0).getBsYield() : bsYield);
                if(processList.size()>0){
                    if (processList.get(0).getBsYield().compareTo(BigDecimal.ZERO)!=1){
                        return ApiResponseResult.failure("工艺顺序"+processList.get(0).getBsOrder()+"零件名称:"+pm.getBsComponent()+"对应的材料的工序良率为0，请检查");
                    }
                }
            }else {
                List<ProductProcess> processList1 = productProcessDao.findByDelFlagAndPkQuoteAndBsGroups(0, pm.getPkQuote(), pm.getBsGroups());
//                pm.setBsYield(processList1.size() > 0 ? processList1.get(0).getBsYield() : bsYield);
                if(processList1.size()>0){
                    if (processList1.get(0).getBsYield().compareTo(BigDecimal.ZERO)!=1){
                        return ApiResponseResult.failure("工艺顺序"+processList1.get(0).getBsOrder()+"损耗分组:"+pm.getBsGroups()+"对应的材料的工序良率为0，请检查");
                    }
                }
            }
        }

//        productProcessDao.saveAll(productMaterList);
        productProcessDao.doProcessStatusByType(UserUtil.getSessionUser().getId(), new Date(), quoteId, bsType);
        if (!("out").equals(bsType)) {
            //项目状态设置-状态 2：已完成
            quoteItemDao.switchStatus(2, quoteId, bsCode);
            //设置结束时间
            quoteItemDao.setEndTime(new Date(), quoteId, bsCode);
            //增加处理人-20210112-lst-param(用户名,用户id,报价单ID,项目编码)
            quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(), UserUtil.getSessionUser().getId(), quoteId, bsCode);

            //20210121-fyx-统一修改状态
            Object data = quoteProductService.doItemFinish(bsCode, quoteId, 3).getData();
            //20201225-fyx-计算后工序良率
            this.updateHouYield(quoteId, bsType);
            return ApiResponseResult.success("确认完成成功！").data(data);
        } else {
            //20210121-fyx-外协
            List<Quote> lo = quoteDao.findByDelFlagAndId(0, quoteId);
            if (lo.size() > 0) {
                Quote o = lo.get(0);
                o.setBsStatus2Out(3);
                quoteDao.save(o);
            }
        }
        return ApiResponseResult.success("确认完成成功！");
    }

    //取消完成
    @Override
    public ApiResponseResult cancelStatus(Long quoteId, String bsType, String bsCode) throws Exception {
        Quote quote = quoteDao.findById((long) quoteId);
        Integer quoteStatus = 0; //判断当前报价单是否已经发起审核
        if (bsType.equals("hardware")) {
            quoteStatus = quote.getBsStatus2Hardware();
        } else if (bsType.equals("molding")) {
            quoteStatus = quote.getBsStatus2Molding();
        } else if (bsType.equals("surface")) {
            quoteStatus = quote.getBsStatus2Surface();
        } else if (bsType.equals("packag")) {
            quoteStatus = quote.getBsStatus2Packag();
        }else if(bsType.equals("out")){
            quoteStatus = quote.getBsStatus2Out();
        }

        if (quoteStatus == 4 || quoteStatus == 2) {
            return ApiResponseResult.failure("发起审批后不能取消确认");
        } else {
//                List<QuoteItem> quoteItemList = quoteItemDao.findByDelFlagAndPkQuoteAndBsCode(0,quoteId,bsCode);
//                if(quoteItemList.size()>0){
//                    if(quoteItemList.get(0).getBsEndTime()==null){
//                        return ApiResponseResult.failure("自动确认完成的项目不能取消完成");
//                    }
//                }
            //项目状态设置-状态 1：未完成
            quoteItemDao.switchStatus(1, quoteId, bsCode);
            //设置结束时间
            //quoteItemDao.setEndTime(null, quoteId, bsCode);
            //取消报价单对应类别的完成状态
            quoteProductService.doItemFinish(bsCode, quoteId, 1);
            List<ProductProcess> productProcessList = productProcessDao.findByDelFlagAndPkQuoteAndBsType(0, quoteId, bsType);
            for (ProductProcess o : productProcessList) {
                //修改所有工艺为未完成
                o.setBsStatus(0);
                o.setLastupdateDate(new Date());
                o.setLastupdateBy(UserUtil.getSessionUser().getId());
            }
            productProcessDao.saveAll(productProcessList);
            return ApiResponseResult.success("取消完成成功");
        }

    }

    private ApiResponseResult updateHouYield(Long quoteId, String bsType) throws Exception {
        //20201225-fyx-计算后工序良率
        //1.先查询有多少个零件名称 
        //2.根据零件名称,工艺顺序倒叙获取信息
        //3.计算后工序

        List<Map<String, Object>> lml = productProcessDao.getBomName(bsType, quoteId);
        if (lml.size() > 0) {
            for (Map<String, Object> map : lml) {
                System.out.println(map.get("BS_NAME").toString());
                List<ProductProcess> lpp = productProcessDao.findByDelFlagAndPkQuoteAndBsTypeAndBsNameOrderByBsOrderDesc(0, quoteId, bsType, map.get("BS_NAME").toString());
                for (int i = 0; i < lpp.size(); i++) {
                    ProductProcess pp = lpp.get(i);
                    if (i == 0) {//认为是最后一道工序,后工序良率是100
                        pp.setBsHouYield(new BigDecimal(100));
                    } else {//3的后工序良率就是4的后工序良率/100*本工序良率
                        pp.setBsHouYield(lpp.get(i - 1).getBsHouYield().divide(new BigDecimal(100)).multiply(pp.getBsYield()));
                    }
                    pp.setLastupdateDate(new Date());
                    pp.setLastupdateBy(UserUtil.getSessionUser().getId());
                }
                productProcessDao.saveAll(lpp);
            }
        }
        return ApiResponseResult.success();
    }

    @Override
    public ApiResponseResult getBomSelect(String pkQuote) throws Exception {
        return ApiResponseResult.success().data(quoteProcessDao.getBomName(pkQuote));
    }

    @Override
    public ApiResponseResult uploadCheck(Long pkQuote, String bsType) throws Exception {
        Long userId = UserUtil.getSessionUser().getId();

        //临时表数据 (未删除且校验通过)
        List<ProductProcessTemp> productProcessTempList = productProcessTempDao.
                findByDelFlagAndPkQuoteAndBsTypeAndCreateByAndCheckStatus(0, pkQuote, bsType, userId, 0);
        Date doDate = new Date();

        //需要导入的主表数据
        List<ProductProcess> productProcessList = new ArrayList<>();
        for (ProductProcessTemp processTemp : productProcessTempList) {
            ProductProcess process = new ProductProcess();
            boolean isPcs = false;
            if (processTemp.getMid() != null) {
                process = productProcessDao.findById((long) processTemp.getMid());
                process.setLastupdateBy(userId);
                process.setLastupdateDate(doDate);
                if(("PCS").equals(process.getPurchaseUnit())){
                    isPcs = true;
                }
            } else {
                process.setCreateDate(doDate);
                process.setCreateBy(userId);
            }
//            process.setPkProc(processTemp.getPkProc());
//            process.setPkQuote(processTemp.getPkQuote());
//            process.setBsName(processTemp.getBsName());
            process.setBsModelType(processTemp.getBsModelType());
            process.setFmemo(processTemp.getFmemo());
            process.setBsType(processTemp.getBsType());
            if("out".equals(bsType)){
                process.setBsFeeWxAll(new BigDecimal(processTemp.getBsFeeWxAll()));
                process.setBsLoss(new BigDecimal(processTemp.getBsLoss()));
            }
            if(!isPcs||("packag").equals(processTemp.getBsType())) {
                if (StringUtils.isNotEmpty(processTemp.getBsCycle())) {
                    process.setBsCycle(new BigDecimal(processTemp.getBsCycle()));
                }
                if (StringUtils.isNotEmpty(processTemp.getBsUserNum())) {
                    process.setBsUserNum(new BigDecimal(processTemp.getBsUserNum()));
                }
                process.setBsCapacity(processTemp.getBsCapacity());
                process.setBsCave(processTemp.getBsCave());
            }
//            if(StringUtils.isNotEmpty(processTemp.getBsRadix())){
//                process.setBsRadix(new BigDecimal(processTemp.getBsRadix()));
//            }
            if (StringUtils.isNotEmpty(processTemp.getBsYield())) {
                process.setBsYield(new BigDecimal(processTemp.getBsYield()));
            }
//            if(StringUtils.isNotEmpty(processTemp.getBsOrder())){
//                process.setBsOrder(Integer.parseInt(processTemp.getBsOrder()));
//            }

            productProcessList.add(process);

            //标记已导入
//            processTemp.setEnabled(1);
            //导入后删除标记
//            processTemp.setDelFlag(1);
        }
        //删除临时表数据
        productProcessTempDao.deleteByPkQuoteAndBsTypeAndCreateBy(pkQuote, bsType, userId);
        productProcessDao.saveAll(productProcessList);
        return ApiResponseResult.success().data("导入成功! 导入总数:" + productProcessList.size());
    }

    /**
     * 根据工作中心id和工序id查询人工和制费
     *
     * @param w_id
     * @param p_id
     * @return
     */
    private String[] getLhBy(Long w_id, Long p_id) {
        String[] strs = new String[2];
        strs[0] = "";
        strs[1] = "";
        List<BaseFee> lbl = baseFeeDao.findByDelFlagAndWorkcenterIdAndProcId(0, w_id, p_id);
        if (lbl.size() == 0) {
            lbl = baseFeeDao.findByDelFlagAndWorkcenterIdAndProcIdIsNull(0, w_id);
            if (lbl.size() > 0) {
                strs[0] = lbl.get(0).getFeeLh();
                strs[1] = lbl.get(0).getFeeMh();
            }
        } else {
            strs[0] = lbl.get(0).getFeeLh();
            strs[1] = lbl.get(0).getFeeMh();
        }
        return strs;
    }

    @Override
    public ApiResponseResult getProcListByType(String bsType) throws Exception {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(bsType)) {
            return ApiResponseResult.failure("查询类型为空!");
        }
        List<Map<String, Object>> lm = new ArrayList<Map<String, Object>>();
        List<Proc> list = productProcessDao.getListByType(bsType);
        for (Proc proc : list) {
            String[] strs = this.getLhBy(proc.getWorkcenterId(), Long.valueOf(proc.getId()));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ID", proc.getId());
            map.put("PROC_NO", proc.getProcNo());
            map.put("PROC_NAME", proc.getProcName());
            map.put("WORKCENTER_NAME", proc.getBjWorkCenter().getWorkcenterName());
            if (strs[0] == "" || strs[1] == "") {//判断人工制费是否有维护
                map.put("STATUS", "0");
            } else {
                map.put("STATUS", "1");
            }
            lm.add(map);
        }
        return ApiResponseResult.success().data(lm);
    }

    /**
     * 删除附件
     */
    @Override
    @Transactional
    public ApiResponseResult delFile(Long id, Long fileId) throws Exception {
        if (id == null) {
            return ApiResponseResult.failure("ID不能为空！");
        }
        ProductProcess o = productProcessDao.findById((long) id);
        if (o == null) {
            return ApiResponseResult.failure("附件信息不存在！");
        }
        o.setFileId(null);
        productProcessDao.save(o);
        return ApiResponseResult.success("删除附件成功！");
    }

    @Override
    public ApiResponseResult editProcessList(List<ProductProcess> productProcessList) throws Exception {
        // TODO Auto-generated method stub
//		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(0,Long.valueOf(quoteId),name);
        productProcessDao.saveAll(productProcessList);
        return ApiResponseResult.success();
    }

    /**
     * 获取bom列表-下拉选择
     **/
    @Override
    public ApiResponseResult getBomList(String keyword, Long quoteId,String bsType,PageRequest pageRequest) throws Exception {
        Page<Map<String, Object>> page=productProcessDao.getBomNameByPage(quoteId,bsType,pageRequest);
        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    @Override
    public ApiResponseResult getLinkNameList(Long quoteId, String bsElement) throws Exception {
        //    List<Map<String, Object>> componentList = quoteBomDao.getBsComponent(Long.parseLong(pkQuote),o.getBsElement());
        return ApiResponseResult.success().data(quoteBomDao.getBsComponent(quoteId,bsElement));
    }

}
