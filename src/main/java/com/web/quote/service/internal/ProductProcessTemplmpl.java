package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.entity.Proc;
import com.web.quote.dao.ProductProcessTempDao;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.ProductProcessTemp;
import com.web.quote.service.ProductProcessTempService;
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
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service(value = "ProductProcessTempService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductProcessTemplmpl implements ProductProcessTempService {
	
	@Autowired
    private ProductProcessTempDao productProcessTempDao;
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
            if(!productProcess.getBsOrder().matches("^\\d+$")){
                errInfo = errInfo + "工序顺序需为正整数;";
            }
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


        o.setBsOrder(productProcess.getBsOrder());
        o.setBsCycle(productProcess.getBsCycle());
        o.setBsYield(productProcess.getBsYield());
        o.setBsUserNum(productProcess.getBsUserNum());
        o.setBsRadix(productProcess.getBsRadix());
        if(errInfo ==""){
            o.setErrorInfo("");
            o.setCheckStatus(0);
        }else {
            o.setErrorInfo(errInfo);
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
        }else return object.toString();
    }


//    导入模板
    public ApiResponseResult doExcel(MultipartFile[] file,String bsType,Long quoteId) throws Exception{
        try {
            Date doExcleDate = new Date();
            Long userId = UserUtil.getSessionUser().getId();
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            List<ProductProcessTemp> tempList = new ArrayList<>();
            //五金工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、成型周期(S)、工序良率、备注
            //注塑工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、穴数、成型周期(S)、加工人数、工序良率、备注
            //组装工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注
            //表面工艺导入顺序: 零件名称、工序顺序、工序名称、机台类型、基数、人数、产能、工序良率、备注
            for (int row = 2; row <= maxRow; row++) {
                String errInfo = "";
                String id = tranCell(sheet.getRow(row).getCell(0));
                String bsName = tranCell(sheet.getRow(row).getCell(1));
                String bsOrder = tranCell(sheet.getRow(row).getCell(2));
                String procName = tranCell(tranCell(sheet.getRow(row).getCell(3)));
                String bsModelType = tranCell(tranCell(sheet.getRow(row).getCell(4)));

                String bsRadix = tranCell(sheet.getRow(row).getCell(5));
                String row6 = tranCell(sheet.getRow(row).getCell(6));
                String row7 = tranCell(sheet.getRow(row).getCell(7));
                String row8 = tranCell(sheet.getRow(row).getCell(8));
                String row9 = tranCell(sheet.getRow(row).getCell(9));
                String row10 = tranCell(sheet.getRow(row).getCell(10));
                ProductProcessTemp process = new ProductProcessTemp();
                //设置类型
                process.setBsType(bsType);
                process.setPkQuote(quoteId);
                process.setBsName(bsName);
                if(StringUtils.isNotEmpty(id)){
                    process.setMid(Long.parseLong(id));
//                    process.setLastupdateBy(userId);
//                    process.setLastupdateDate(doExcleDate);
                }
                process.setCreateBy(userId);
                process.setCreateDate(doExcleDate);
                process.setBsOrder(bsOrder);
                if(!bsOrder.matches("^\\d+$")){
                    errInfo = errInfo + "工序顺序需位正整数;";
                }
//                process.setBsRadix((int)Double.parseDouble(bsRadix));
                List<Proc> procList = procDao.findByDelFlagAndProcName(0,procName);
                if(procList.size()>0&&procList!=null){
                    process.setPkProc(procList.get(0).getId());
                }
                process.setBsModelType(bsModelType);
                if(("molding").equals(bsType)){
                    //注塑
                    process.setBsCave(row6);
                    process.setBsCycle(row7);
                    process.setBsUserNum(row8);
                    process.setBsYield(row9);
//                    if(!row6.matches("^\\d+$")){
//                        errInfo = errInfo + "穴数数字类型;";
//                    }
                    if(!row7.matches("^\\d+$")){
                        errInfo = errInfo + "成型周期必须是数字类型;";
                    }if(!row8.matches("^\\d+\\.\\d+$")&&!row8.matches("^\\d+$")){
                        errInfo = errInfo + "人数必须是数字类型;";
                    }if(!row9.matches("^\\d+\\.\\d+$")&&!row9.matches("^\\d+$")){
                        errInfo = errInfo + "工序良率必须是数字类型;";
                    }
                    process.setFmemo(row10);
                }else if(("hardware").equals(bsType)){
                    //五金
                    process.setBsUserNum(row6);
                    process.setBsCycle(row7);
                    process.setBsYield(row8);
                    if(!row6.matches("^\\d+$")&&!row6.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "人数必须是数字类型;";
                    }
                    if(!row7.matches("^\\d+$")&&!row7.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "成型周期必须是数字类型;";
                    }if(!row8.matches("^\\d+$")&&!row8.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "工序良率必须是数字类型;";
                    }
                    process.setFmemo(row9);
                }else {
                    //组装和表面
                    process.setBsUserNum(row6);
                    process.setBsCapacity(row7);
                    process.setBsYield(row8);
                    if(!row6.matches("^\\d+$")&&!row6.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "人数必须是数字类型;";
                    }
                    if(!row7.matches("^\\d+$")&&!row7.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "产能必须是数字类型;";
                    }if(!row8.matches("^\\d+$")&&!row8.matches("^\\d+\\.\\d+$")){
                        errInfo = errInfo + "工序良率必须数字类型;";
                    }
                    process.setFmemo(row9);
                }
                process.setErrorInfo(errInfo);
                if(errInfo ==""){
                    process.setCheckStatus(0);
                }else {
                    process.setCheckStatus(1);
                }
                tempList.add(process);
            }
            productProcessTempDao.saveAll(tempList);
            return ApiResponseResult.success("导入成功");
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
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
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
