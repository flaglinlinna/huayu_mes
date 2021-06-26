package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.BjModelTypeDao;
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.entity.BjModelType;
import com.web.basePrice.entity.Proc;
import com.web.quote.dao.ProductProcessDao;
import com.web.quote.dao.ProductProcessTempDao;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.ProductProcess;
import com.web.quote.entity.ProductProcessTemp;
import com.web.quote.service.ProductProcessTempService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

@Service(value = "ProductProcessTempService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductProcessTemplmpl implements ProductProcessTempService {
	
	@Autowired
    private ProductProcessTempDao productProcessTempDao;
    @Autowired
    private ProductProcessDao productProcessDao;
    @Autowired
    private BjModelTypeDao bjModelTypeDao;
    @Autowired
    private ProcDao procDao;
    @Autowired
    QuoteProcessDao quoteProcessDao;
	

    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ProductProcessTemp productProcess) throws Exception {
        String errInfo ="";
        NumberFormat nf = NumberFormat.getInstance();
        if(productProcess == null){
            return ApiResponseResult.failure("制造部材料信息不能为空！");
        }
        if(productProcess.getId() == null){
            return ApiResponseResult.failure("制造部材料信息ID不能为空！");
        }

        ProductProcessTemp o = productProcessTempDao.findById((long) productProcess.getId());
        if(o == null){
            return ApiResponseResult.failure("该制造部材料信息不存在！");
        }

        if(StringUtils.isNotEmpty(productProcess.getBsOrder())){
            String bsOrder = nf.format(new BigDecimal(productProcess.getBsOrder()));

            if(!bsOrder.matches("^\\d+$")){
                errInfo = errInfo + "工序顺序需为正整数;";
            }
            o.setBsOrder(productProcess.getBsOrder());
        }else {
            errInfo = errInfo + "工序顺序不能为空;";
        }
        if("molding".equals(productProcess.getBsType())||"hardware".equals(productProcess.getBsType())) {
            if (StringUtils.isNotEmpty(productProcess.getBsCycle())) {
                if (!productProcess.getBsCycle().matches("^\\d+\\.\\d+$")
                        && !productProcess.getBsCycle().matches("^^\\d+$")) {
                    errInfo = errInfo + "成型周期需输入数字;";
                }
            } else {
                errInfo = errInfo + "成型周期不能为空;";
            }
        }
        if(StringUtils.isNotEmpty(productProcess.getBsRadix())) {
            if (!productProcess.getBsRadix().matches("^\\d+\\.\\d+$")
                    && !productProcess.getBsRadix().matches("^^\\d+$")){
                errInfo = errInfo + "基数需输入数字;";
            }
        }else {
            errInfo = errInfo + "基数需输入数字;";
        }
        if(StringUtils.isNotEmpty(productProcess.getBsUserNum())) {
            if (!productProcess.getBsUserNum().matches("^\\d+\\.\\d+$")
                    && !productProcess.getBsUserNum().matches("^^\\d+$")) {
                errInfo = errInfo + "人数需输入数字;";
            }
        }else {
            errInfo = errInfo + "人数不能为空;";
        }
        if(StringUtils.isNotEmpty(productProcess.getBsYield())) {
            if (!productProcess.getBsYield().matches("^\\d+\\.\\d+$")
                    && !productProcess.getBsYield().matches("^^\\d+$"))  {
                errInfo = errInfo + "工序良率需输入数字;";
            }
        }else {
            errInfo = errInfo + "工序良率不能为空;";
        }

        o.setBsName(productProcess.getBsName());
        o.setPkProc(productProcess.getPkProc());
        o.setBsModelType(productProcess.getBsModelType());
        o.setBsCave(productProcess.getBsCave());
        o.setBsCapacity(productProcess.getBsCapacity());
        o.setFmemo(productProcess.getFmemo());
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());


//        o.setBsOrder(productProcess.getBsOrder());
        o.setBsCycle(productProcess.getBsCycle());
        o.setBsYield(productProcess.getBsYield());
        o.setBsUserNum(productProcess.getBsUserNum());
        o.setBsRadix(productProcess.getBsRadix());
        if(errInfo ==""){
            o.setErrorInfo("");
            o.setCheckStatus(0);
        }else {
            o.setErrorInfo(errInfo);
            o.setCheckStatus(1);
        }
        productProcessTempDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }

    /**
     * 删除异常类别
     */
    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("异常类别ID不能为空！");
        }
        ProductProcessTemp o  = productProcessTempDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        productProcessTempDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    //防止读取Excel为null转String 报空指针异常
    public String tranCell(Object object)
    {
        if(object==null||object==""||("").equals(object)){
            return "";
        }else return object.toString().trim();
    }


//    导入模板
    public ApiResponseResult doExcel(MultipartFile[] file,String bsType,Long quoteId) throws Exception{
        try {
            Long userId = UserUtil.getSessionUser().getId();

            //先删除临时表中存在的数据
            List<Long> idList = productProcessDao.getIdByTypeAndPkQuote(bsType,quoteId);
            productProcessTempDao.deleteByPkQuoteAndBsTypeAndCreateBy(quoteId,bsType,userId);
            Date doExcleDate = new Date();
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            if(idList.size()!=maxRow-1){
                return ApiResponseResult.failure("导入失败！导入的行数与原工艺信息行量不符！");
            }
            List<ProductProcessTemp> tempList = new ArrayList<>();
            NumberFormat nf = NumberFormat.getInstance();
            //五金工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、成型周期(S)、工序良率、备注
            //注塑工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、穴数、成型周期(S)、加工人数、工序良率、备注
            //组装工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注
            //表面工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注
            //外协报价导入顺序: 零件名称、工序顺序、工序名称、机台类型、损耗率、外协价格 、备注

            //2021-04-29 模板变更 五金  组件名称1 、零件名称2、工序顺序3、工序名称4、机台类型5、人数6、成型周期(S)7、工序良率8
                                //注塑 组件名称1 、零件名称1、工序顺序2、工序名称3、机台类型4、人数5、成型周期(S)6、工序良率7、穴数8
                                //组装 组件名称1 、零件名称1、工序顺序2、工序名称3、机台类型4、人数5、工序良率6、产能7
                                //表面 组件名称1 、零件名称1、工序顺序2、工序名称3、机台类型4、人数5、工序良率6、产能7
                                //外协:组件名称1 、零件名称1、工序顺序2、工序名称3、机台类型4、损耗率5、外协价格6 、备注7
            Integer successes = 0;
            Integer failures = 0;
            Set<Long> improtIdList = new HashSet<Long>();
            for (int row = 2; row <= maxRow; row++) {
                String errInfo = "";
                XSSFCell midCell = sheet.getRow(row).getCell(0);
                midCell.setCellType(midCell.CELL_TYPE_STRING);
                String id = tranCell(midCell);
                String bsElement = tranCell(sheet.getRow(row).getCell(1));
                String bsName = tranCell(sheet.getRow(row).getCell(2));
                String bsOrder = tranCell(sheet.getRow(row).getCell(3));
                String procName = tranCell(tranCell(sheet.getRow(row).getCell(4)));
                String bsModelType = tranCell(tranCell(sheet.getRow(row).getCell(5)));

                String row5 = tranCell(sheet.getRow(row).getCell(6));
                XSSFCell xssfCell6 = sheet.getRow(row).getCell(7);
                xssfCell6.setCellType(midCell.CELL_TYPE_STRING);
                String row6 = tranCell(xssfCell6);
                String row7 = tranCell(sheet.getRow(row).getCell(8));
                String row8 = tranCell(sheet.getRow(row).getCell(9));
//                String row9 = tranCell(sheet.getRow(row).getCell(9));
//                String row10 = tranCell(sheet.getRow(row).getCell(10));
                ProductProcessTemp process = new ProductProcessTemp();

                ProductProcess processMain = new ProductProcess();
                processMain = productProcessDao.findById(Long.parseLong(id));
                boolean isPcs = false;
                if(("PCS").equals(processMain.getPurchaseUnit())){
                    isPcs = true;
                }

                //设置类型
                process.setBsType(bsType);
                process.setPkQuote(quoteId);

                if(StringUtils.isNotEmpty(bsElement)){
                    process.setBsElement(bsElement);
                }else {
                    errInfo += "组件名称不能为空;";
                }

                if(StringUtils.isNotEmpty(bsName)){
                    process.setBsName(bsName);
                }else {
                    errInfo += "零件名称不能为空;";
                }
//                process.setBsRadix(bsRadix);


//                if(StringUtils.isNotEmpty(bsRadix)) {
//                    if (!bsRadix.matches("^\\d+\\.\\d+$")
//                            && !bsRadix.matches("^^\\d+$")){
//                        errInfo = errInfo + "基数需输入数字;";
//                    }else if(("0").equals(bsRadix)){
//                        errInfo = errInfo + "基数不能为0;";
//                     }else {
//                        process.setBsRadix(nf.format(new BigDecimal(bsRadix)));
//                    }
//                }else {
//                    errInfo = errInfo + "基数不能为空;";
//                }

                if(StringUtils.isNotEmpty(id)){
                    process.setMid(Long.parseLong(id));
                    improtIdList.add(Long.parseLong(id)) ;
//                    process.setLastupdateBy(userId);
//                    process.setLastupdateDate(doExcleDate);
                }
                process.setCreateBy(userId);
                process.setCreateDate(doExcleDate);
                if(StringUtils.isNotEmpty(bsOrder)) {
                    if (!bsOrder.matches("^\\d+$")&&!bsOrder.matches("^\\d+\\.\\d+$")) {
                        errInfo = errInfo + "工序顺序需为正整数;";
                    }else {
                        process.setBsOrder(nf.format(new BigDecimal(bsOrder)));
                    }
                }
//                process.setBsRadix((int)Double.parseDouble(bsRadix));
                if(StringUtils.isNotEmpty(procName)) {
                    List<Proc> procList = procDao.findByDelFlagAndProcName(0, procName);
                    if (procList.size() > 0 && procList != null) {
                        process.setPkProc(procList.get(0).getId());
                    }else {
                        errInfo += "没有维护名称为 "+procName +" 的工序;";
                    }
                }else {
                    errInfo += "工序名称不能为空;";
                }
//                process.setBsModelType(bsModelType);
                if(StringUtils.isNotEmpty(bsModelType)) {
                    List<BjModelType> bjModelTypesList = bjModelTypeDao.findByDelFlagAndModelName(0, bsModelType);
                    if (bjModelTypesList.size() == 0) {
                        errInfo = errInfo + "没有维护" + bsModelType + " 机台类型;";
                    } else {
                        process.setBsModelType(bjModelTypesList.get(0).getModelCode());
                    }
                }


                if(("molding").equals(bsType)){
                    //注塑
                    if(StringUtils.isNotEmpty(row8)){
                        if(!row8.matches("^\\d+\\.\\d+$")&&!row8.matches("^\\d+$")){
                            errInfo = errInfo + "穴数必须是数字类型;";
                        }else {
                            process.setBsCave(nf.format(new BigDecimal(row8)));
                        }
                    }else if(isPcs){

                    }
                    else {
                        errInfo = errInfo + "穴数不能为空;";
                    }


                    process.setBsYield(row7);
//                    if(!row6.matches("^\\d+$")){
//                        errInfo = errInfo + "穴数数字类型;";
//                    }
                    if(!isPcs) {
                        if (!row6.matches("^\\d+\\.\\d+$") && !row6.matches("^\\d+$")) {
                            errInfo = errInfo + "成型周期必须是数字类型;";
                        } else {
                            process.setBsCycle(nf.format(new BigDecimal(row6)));
                        }
                        if (!row5.matches("^\\d+\\.\\d+$") && !row5.matches("^\\d+$")) {
                            errInfo = errInfo + "人数必须是数字类型;";
                        } else {
                            process.setBsUserNum(nf.format(new BigDecimal(row5)));
                        }
                    }
                    if(!row7.matches("^\\d+\\.\\d+$")&&!row7.matches("^\\d+$")){
                        errInfo = errInfo + "工序良率必须是数字类型;";
                    }
//                    process.setFmemo(row10);
                }
                else if(("hardware").equals(bsType)){
                    //五金
//                    process.setBsUserNum(row6);
                    process.setBsCycle(row6);
                    process.setBsYield(row7);
                    if(!isPcs) {
                        if (!row5.matches("^\\d+$") && !row5.matches("^\\d+\\.\\d+$")) {
                            errInfo = errInfo + "人数必须是数字类型;";
                        } else {
                            process.setBsUserNum(nf.format(new BigDecimal(row5)));
                        }
                        if (!row6.matches("^\\d+$") && !row6.matches("^\\d+\\.\\d+$")) {
                            errInfo = errInfo + "成型周期必须是数字类型;";
                        }
                    }
                    if(!row7.matches("^\\d+$")&&!row7.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "工序良率必须是数字类型;";
                    }
//                    process.setFmemo(row9);
                }
                else if(("out").equals(bsType)){
                    if(StringUtils.isNotEmpty(row5)) {
                        if (!row5.matches("^\\d+\\.\\d+$")
                                && !row5.matches("^^\\d+$")){
                            errInfo = errInfo + "损耗率需输入数字;";
                        }else if(("0").equals(row5)){
                            errInfo = errInfo + "损耗率不能为0;";
                        }else {
                            process.setBsLoss(nf.format(new BigDecimal(row5)));
                        }
                    }else {
                        errInfo = errInfo + "损耗率不能为空;";
                    }

                    if(StringUtils.isNotEmpty(row6)) {
                        if (!row6.matches("^\\d+\\.\\d+$")
                                && !row6.matches("^^\\d+$")){
                            errInfo = errInfo + "外协价格需输入数字;";
                        }else {
                            process.setBsFeeWxAll(nf.format(new BigDecimal(row6)));
                        }
                    }else {
                        errInfo = errInfo + "外协价格不能为空;";
                }
//                    process.setBsLoss(bsLoss);
                }
                else {
                    //组装和表面
//                    process.setBsUserNum(row6);
                    process.setBsCapacity(row7);
                    process.setBsYield(row6);
                    if(!isPcs||("packag").equals(process.getBsType())) {
                        if (!row5.matches("^\\d+$") && !row5.matches("^\\d+\\.\\d+$")) {
                            errInfo = errInfo + "人数必须是数字类型;";
                        } else {
                            process.setBsUserNum(nf.format(new BigDecimal(row5)));
                        }
                        if (!row7.matches("^\\d+$") && !row7.matches("^\\d+\\.\\d+$")) {
                            errInfo = errInfo + "产能必须是数字类型;";
                        }
                    }
                    if(!row6.matches("^\\d+$")&&!row6.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "工序良率必须数字类型;";
                    }
//                    process.setFmemo(row9);
                }
                process.setErrorInfo(errInfo);
                if(errInfo ==""){
                    process.setCheckStatus(0);
                    successes ++;
                }else {
                    process.setCheckStatus(1);
                    failures ++;
                }
                tempList.add(process);
            }

            Set idSet = new HashSet(idList);
            if(!idSet.removeAll(improtIdList)){
                return ApiResponseResult.success("导入的id与实际工艺信息id不符,请在本界面导出修改后导入");
            }

            productProcessTempDao.saveAll(tempList);
            Integer all = maxRow -1;
            return ApiResponseResult.success("导入成功! 导入总数:" +all+" :校验通过数:"+successes+" ;不通过数: "+failures);
        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
        }
    }

    /**
     * 查询列表
     */
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,String bsType, String quoteId,PageRequest pageRequest) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        Long userId = UserUtil.getSessionUser().getId();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("createBy", SearchFilter.Operator.EQ, userId));
        if (StringUtils.isNotEmpty(bsType)) {
            filters.add(new SearchFilter("bsType", SearchFilter.Operator.EQ, bsType));
        }
        if (!"null".equals(quoteId)&&quoteId!=null) {
            filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, quoteId));
        }else {
            List<ProductProcessTemp> productMaterList = new ArrayList<>();
            return ApiResponseResult.success().data(DataGrid.create(productMaterList, 0,
                    1, 10));
        }
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
//            filters1.add(new SearchFilter("bsMaterName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<ProductProcessTemp> spec = Specification.where(BaseService.and(filters, ProductProcessTemp.class));
        Specification<ProductProcessTemp> spec1 = spec.and(BaseService.or(filters1, ProductProcessTemp.class));
        Page<ProductProcessTemp> page = productProcessTempDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }




}
