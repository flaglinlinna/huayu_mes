package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.dao.ProductMaterTempDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.entity.ProductMaterTemp;
import com.web.quote.service.ProductMaterTempService;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "ProductMaterTempService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductMaterTemplmpl implements ProductMaterTempService {
	
	@Autowired
    private ProductMaterTempDao productMaterTempDao;

    @Autowired
    private ProductMaterDao productMaterDao;

    @Autowired
    private UnitDao unitDao;
	


    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ProductMaterTemp hardwareMater) throws Exception {
        if(hardwareMater == null){
            return ApiResponseResult.failure("制造部材料信息不能为空！");
        }
        if(hardwareMater.getId() == null){
            return ApiResponseResult.failure("制造部材料信息ID不能为空！");
        }

        ProductMaterTemp o = productMaterTempDao.findById((long) hardwareMater.getId());
        if(o == null){
            return ApiResponseResult.failure("该制造部材料信息不存在！");
        }
        o.setBsComponent(hardwareMater.getBsComponent());
        o.setBsMaterName(hardwareMater.getBsMaterName());
        o.setBsModel(hardwareMater.getBsModel());
        o.setBsQty(hardwareMater.getBsQty());
        o.setBsProQty(hardwareMater.getBsProQty());
        o.setBsRadix(hardwareMater.getBsRadix());
        o.setBsUnit(hardwareMater.getBsUnit());
        o.setPkUnit(hardwareMater.getPkUnit());
        o.setBsSupplier(hardwareMater.getBsSupplier());
        o.setBsCave(hardwareMater.getBsCave());
        o.setBsMachiningType(hardwareMater.getBsMachiningType());
        o.setBsWaterGap(hardwareMater.getBsWaterGap());
        o.setBsColor(hardwareMater.getBsColor());
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setFmemo(hardwareMater.getFmemo());
        o.setBsGear(hardwareMater.getBsGear());
        o.setBsAssess(hardwareMater.getBsAssess());
        productMaterTempDao.save(o);
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
        ProductMaterTemp o  = productMaterTempDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        productMaterTempDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    //防止读取Excel为null转String 报空指针异常
    public String tranCell(Object object)
    {
        if(object==null||object==""||("").equals(object)){
            return null;
        }else return object.toString();
    }


    //制造部材料 导入临时表
    public ApiResponseResult doExcel(MultipartFile[] file,String bsType,Long quoteId) throws Exception{
        try {
            Date doExcleDate = new Date();
            Long userId = UserUtil.getSessionUser().getId();

            //删除临时表数据
            productMaterTempDao.deleteByPkQuoteAndBsTypeAndCreateBy(quoteId,bsType,userId);

            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            NumberFormat nf = NumberFormat.getInstance();
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            List<ProductMaterTemp> hardwareMaterList = new ArrayList<>();
            //五金材料导入顺序: 零件名称1、材料名称2、规格3、用量4、单位5、基数6、供应商7、备注8
            //组装材料导入顺序: 零件名称1、材料名称2、规格3、用量4、单位5、基数6、供应商7、备注8
            //注塑材料导入顺序: 零件名称1、材料名称2、规格3、制品量4、单位5、基数6、水口量7、穴数8、备注9
            //表面处理导入顺序: 零件名称1、加工类型2、配色工艺3、材料名称4、规格5、用料6、单位7、基数8、备注9
            for (int row = 2; row <= maxRow; row++) {
                String errInfo = "";
                String mid = tranCell(sheet.getRow(row).getCell(0));
                String row1 = tranCell(sheet.getRow(row).getCell(1));
                String row2 = tranCell(sheet.getRow(row).getCell(2));
                String row3 = tranCell(sheet.getRow(row).getCell(3));
                String row4 = tranCell(sheet.getRow(row).getCell(4));
                String row5 = tranCell(sheet.getRow(row).getCell(5));
                String row6 = tranCell(sheet.getRow(row).getCell(6));
                String row7 = tranCell(sheet.getRow(row).getCell(7));
                String row8 = tranCell(sheet.getRow(row).getCell(8));
                String row9 = tranCell(sheet.getRow(row).getCell(9));
                String row10 = tranCell(sheet.getRow(row).getCell(10));
                ProductMaterTemp temp = new ProductMaterTemp();

                if(StringUtils.isNotEmpty(mid)){
                    temp.setMid(Long.parseLong(mid));
                }
                //设置类型
                temp.setBsType(bsType);
                temp.setPkQuote(quoteId);
                temp.setBsComponent(row1);
                if(!StringUtils.isNotEmpty(row1)){
                    errInfo += errInfo + "零件名称不能为空";
                }
                if(("molding").equals(bsType)){
                    temp.setBsMaterName(row2);
                    if(!StringUtils.isNotEmpty(row2)){
                        errInfo = errInfo + "材料名称不能为空";
                    }
                    temp.setBsModel(row3);
                    if(!StringUtils.isNotEmpty(row3)){
                        errInfo = errInfo + "材料规格不能为空";
                    }

                    temp.setBsRadix(row6);
                    temp.setBsWaterGap(row7);
                    temp.setBsCave(row8);
                    temp.setBsSupplier(row9);
                    temp.setFmemo(row10);

                    if(StringUtils.isNotEmpty(row4)) {
                        temp.setBsProQty(row4);
                        if (!row4.matches("^\\d+\\.\\d+$") && !row4.matches("^\\d+$")) {
                            errInfo = errInfo + "制品量必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "制品量不能为空;";
                    }
                    if(StringUtils.isNotEmpty(row5)){
                        temp.setBsUnit(row5);
                        List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(row5,0);
                        if(unitList!=null&& unitList.size()>0){
                            temp.setPkUnit(unitList.get(0).getId());
                        }else {
                            errInfo = errInfo +"没有维护:"+ row5 + " 单位;";
                        }
                    }else {
                        errInfo = errInfo + "单位不能为空;";
                    }

                    if(StringUtils.isNotEmpty(row6)) {
                        if (!row6.matches("^\\d+\\.\\d+$") && !row6.matches("^\\d+$")) {
                            errInfo = errInfo + "基数必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "基数不能为空;";
                    }

                    if(StringUtils.isNotEmpty(row7)) {
                        if (!row7.matches("^\\d+\\.\\d+$") && !row7.matches("^\\d+$")) {
                            errInfo = errInfo + "水口数必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "水口数不能为空;";
                    }

                    if(StringUtils.isNotEmpty(row8)) {
                        if (!row8.matches("^\\d+\\.\\d+$") && !row8.matches("^\\d+$")) {
                            errInfo = errInfo + "穴数必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "穴数不能为空;";
                    }

                } else if(("surface").equals(bsType)){
                    temp.setBsMachiningType(row2);
                    if(!StringUtils.isNotEmpty(row2)) {
                        errInfo = errInfo + "加工类型不能为空;";
                    }
                    if(!StringUtils.isNotEmpty(row3)) {
                        errInfo = errInfo + "配色工艺不能为空;";
                    }
                    temp.setBsColor(row3);
                    temp.setBsMaterName(row4);
                    temp.setBsModel(row5);
                    if(!StringUtils.isNotEmpty(row4)){
                        errInfo = errInfo + "材料名称不能为空";
                    }
                    if(!StringUtils.isNotEmpty(row5)){
                        errInfo = errInfo + "材料规格不能为空";
                    }
                    temp.setBsQty(row6);
                    if(StringUtils.isNotEmpty(row6)) {
                        if (!row6.matches("^\\d+\\.\\d+$") && !row6.matches("^\\d+$")) {
                            errInfo = errInfo + "用量必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "用量不能为空;";
                    }

                    if(StringUtils.isNotEmpty(row7)){
                        temp.setBsUnit(row7);
                        List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(row7,0);
                        if(unitList!=null&& unitList.size()>0){
                            temp.setPkUnit(unitList.get(0).getId());
                        }else {
                            errInfo = errInfo +"没有维护:"+ row7 + " 单位;";
                        }
                    }else {
                        errInfo = errInfo + "单位不能为空;";
                    }

                    temp.setBsRadix(row8);
                    if(StringUtils.isNotEmpty(row8)) {
                        if (!row8.matches("^\\d+\\.\\d+$") && !row8.matches("^\\d+$")) {
                            errInfo = errInfo + "基数必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "基数不能为空;";
                    }
                    temp.setFmemo(row9);
                }else {
                    temp.setBsComponent(row1);
                    temp.setBsMaterName(row2);
                    temp.setBsModel(row3);
                    if(!StringUtils.isNotEmpty(row1)){
                        errInfo = errInfo + "零件名称不能为空";
                    }
                    if(!StringUtils.isNotEmpty(row2)){
                        errInfo = errInfo + "材料名称不能为空";
                    }
                    if(!StringUtils.isNotEmpty(row3)){
                        errInfo = errInfo + "材料规格不能为空";
                    }
                    temp.setBsQty(row4);
                    if(StringUtils.isNotEmpty(row5)){
                        temp.setBsUnit(row5);
                        List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(row5,0);
                        if(unitList!=null&& unitList.size()>0){
                            temp.setPkUnit(unitList.get(0).getId());
                        }else {
                            errInfo = errInfo +"没有维护:"+ row5 + " 单位;";
                        }
                    }else {
                        errInfo = errInfo + "单位不能为空;";
                    }
                    temp.setBsRadix(row6);

                    if(StringUtils.isNotEmpty(row4)) {
                        if (!row4.matches("^\\d+\\.\\d+$") && !row4.matches("^\\d+$")) {
                            errInfo = errInfo + "用量必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "用量不能为空;";
                    }

                    if(StringUtils.isNotEmpty(row6)) {
                        if (!row6.matches("^\\d+\\.\\d+$") && !row6.matches("^\\d+$")) {
                            errInfo = errInfo + "基数必须是数字类型;";
                        }
                    }else {
                        errInfo = errInfo + "基数不能为空;";
                    }
                    temp.setBsSupplier(row7);
                    temp.setFmemo(row8);
                }
                temp.setCreateBy(userId);
                temp.setCreateDate(doExcleDate);
                if("".equals(errInfo)){
                    temp.setCheckStatus(0);
                }else {
                    temp.setCheckStatus(1);
                    temp.setErrorInfo(errInfo);
                }
                hardwareMaterList.add(temp);
            }
            productMaterTempDao.saveAll(hardwareMaterList);
            return ApiResponseResult.success("导入成功");
        }
        catch (Exception e){
            e.printStackTrace();
            return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
        }
    }


    /**
     *
     * @param file
     * @param quoteId
     * @return 采购报价导入临时表
     * @throws Exception
     */
    public ApiResponseResult doExcel(MultipartFile[] file,Long quoteId) throws Exception{
        try {
            Long userId = UserUtil.getSessionUser().getId();
            //删除临时表数据
            productMaterTempDao.deleteByPkQuoteAndCreateByAndBsPurchase(quoteId,userId,0);
            Date doExcleDate = new Date();
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            List<ProductMaterTemp> hardwareMaterList = new ArrayList<>();

            for (int row = 2; row <= maxRow; row++) {
                String errInfo = "";
                String id = tranCell(sheet.getRow(row).getCell(0));
                String bsType = tranCell(sheet.getRow(row).getCell(1));
                String bsComponent = tranCell(sheet.getRow(row).getCell(2));
                String bsMaterName = tranCell(sheet.getRow(row).getCell(3));
                String bsModel = tranCell(sheet.getRow(row).getCell(4));
                String bsQty = tranCell(sheet.getRow(row).getCell(5));
                String BsUnit = tranCell(sheet.getRow(row).getCell(6));
                String bsRadix = tranCell(sheet.getRow(row).getCell(7));
                String bsGeneral = tranCell(sheet.getRow(row).getCell(8));
                String bsGear = tranCell(sheet.getRow(row).getCell(9));
                String bsRefer = tranCell(sheet.getRow(row).getCell(10));
                String bsAssess = tranCell(sheet.getRow(row).getCell(11));
                String fmemo = tranCell(sheet.getRow(row).getCell(12));
                String bsSupplier = tranCell(sheet.getRow(row).getCell(13));
                String bsExplain = tranCell(sheet.getRow(row).getCell(14));
                ProductMaterTemp temp = new ProductMaterTemp();
                temp.setBsPurchase(0);
                temp.setCreateBy(userId);
                temp.setCreateDate(doExcleDate);
                if(StringUtils.isNotEmpty(id)){
                    temp.setMid(Long.parseLong(id));
                }
                temp.setPkQuote(quoteId);
                temp.setBsGear(bsGear);
                temp.setBsAssess(bsAssess);
//                productMater.setBsAssess(new BigDecimal(bsAssess));
                temp.setBsType(bsType);
                temp.setBsComponent(bsComponent);
                temp.setBsMaterName(bsMaterName);
                temp.setBsModel(bsModel);
                temp.setBsQty(bsQty);
                temp.setBsUnit(BsUnit);
                List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(bsSupplier,0);
                if(unitList!=null&& unitList.size()>0){
                    temp.setPkUnit(unitList.get(0).getId());
                }
                if(StringUtils.isNotEmpty(bsRadix)) {
                    if (!bsRadix.matches("^\\d+\\.\\d+$")
                            && !bsRadix.matches("^^\\d+$")){
                        errInfo = errInfo + "基数需输入数字;";
                    }
                }else {
                    errInfo = errInfo + "基数不能为空;";
                }

                if(StringUtils.isNotEmpty(bsAssess)) {
                    if (!bsAssess.matches("^\\d+\\.\\d+$")
                            && !bsAssess.matches("^^\\d+$")){
                        errInfo = errInfo + "评估价格需输入数字;";
                    }
                }else {
                    errInfo = errInfo + "评估价格不能为空;";
                }

                if(StringUtils.isNotEmpty(bsRefer)) {
                    if (!bsRefer.matches("^\\d+\\.\\d+$")
                            && !bsRefer.matches("^^\\d+$")){
                        errInfo = errInfo + "参考价格需输入数字;";
                    }
                }


                temp.setBsRadix(bsRadix);
                temp.setBsGeneral(bsGeneral);
                temp.setBsGear(bsGear);
                temp.setBsRefer(bsRefer);
                temp.setBsAssess(bsAssess);
                temp.setFmemo(fmemo);
                temp.setBsSupplier(bsSupplier);
                temp.setBsExplain(bsExplain);
                if(errInfo ==""){
                    temp.setCheckStatus(0);
                }else {
                    temp.setCheckStatus(1);
                    temp.setErrorInfo(errInfo);
                }
                hardwareMaterList.add(temp);
            }
            productMaterTempDao.saveAll(hardwareMaterList);
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
    public ApiResponseResult getList(String bsPurchase,String bsType, String quoteId,PageRequest pageRequest) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        Long userId = UserUtil.getSessionUser().getId();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if (StringUtils.isNotEmpty(bsPurchase)) {
            filters.add(new SearchFilter("bsPurchase", SearchFilter.Operator.EQ, bsPurchase));
        }
        if (StringUtils.isNotEmpty(bsType)) {
            filters.add(new SearchFilter("bsType", SearchFilter.Operator.EQ, bsType));
        }
        filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, quoteId));
        filters.add(new SearchFilter("createBy", SearchFilter.Operator.EQ, userId));

        Specification<ProductMaterTemp> spec = Specification.where(BaseService.and(filters, ProductMaterTemp.class));
        Page<ProductMaterTemp> page = productMaterTempDao.findAll(spec, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    //采购填报价格 临时表导入正式表
    @Override
    public ApiResponseResult importByPurchase(Long quoteId)throws Exception {
        Long userId = UserUtil.getSessionUser().getId();

        //查出需要导入的临时表数据
        List<ProductMaterTemp> tempList =productMaterTempDao.
                findByDelFlagAndPkQuoteAndCreateByAndBsPurchaseAndCheckStatus(0,quoteId,userId,0,0);

        List<ProductMater> productMaterList = new ArrayList<>();
        for(ProductMaterTemp temp :tempList){
            ProductMater purchase = new ProductMater();
            if(temp.getMid()!=null){
                purchase = productMaterDao.findById( (long)temp.getMid());
            }
            if("五金".equals(temp.getBsType())){
                purchase.setBsType("hardware");
            }else if("组装".equals(temp.getBsType())){
                purchase.setBsType("packag");
            }else if("注塑".equals(temp.getBsType())){
                purchase.setBsType("molding");
            }else if("表面处理".equals(temp.getBsType())){
                purchase.setBsType("surface");
            }
            purchase.setBsComponent(temp.getBsComponent());
            purchase.setBsMaterName(temp.getBsMaterName());
            purchase.setBsModel(temp.getBsModel());
            if(temp.getBsQty()!=null) {
                purchase.setBsQty(new BigDecimal(temp.getBsQty()));
            }
            purchase.setBsUnit(temp.getBsUnit());
            purchase.setPkUnit(temp.getPkUnit());
            purchase.setBsRadix(temp.getBsRadix());
            purchase.setBsGeneral(temp.getBsPurchase());
            purchase.setBsGear(temp.getBsGear());
            if(temp.getBsRefer()!=null) {
                purchase.setBsRefer(new BigDecimal(temp.getBsRefer()));
            }
            purchase.setBsAssess(new BigDecimal(temp.getBsAssess()));
            purchase.setPkQuote(temp.getPkQuote());
            purchase.setBsAssess(new BigDecimal(temp.getBsAssess()));
            purchase.setFmemo(temp.getFmemo());
            purchase.setBsSupplier(temp.getBsSupplier());
            purchase.setBsExplain(temp.getBsExplain());//lst-202107
            productMaterList.add(purchase);
        }
        productMaterDao.saveAll(productMaterList);
        productMaterTempDao.deleteByPkQuoteAndCreateByAndBsPurchase(quoteId,userId,0);
        return ApiResponseResult.success();
    }

    //制造部材料 临时表导入正式表
    @Override
    public ApiResponseResult importByMater(Long quoteId,String bsType)throws Exception {
        Long userId = UserUtil.getSessionUser().getId();

        //查出需要导入的临时表数据
        List<ProductMaterTemp> tempList =productMaterTempDao.
                findByDelFlagAndPkQuoteAndCreateByAndBsTypeAndCheckStatus(0,quoteId,userId,bsType,0);

        List<ProductMater> productMaterList = new ArrayList<>();
        for(ProductMaterTemp temp :tempList){
            ProductMater mater = new ProductMater();
            if(temp.getMid()!=null){
                mater = productMaterDao.findById( (long)temp.getMid());
            }
            mater.setBsType(bsType);
//            if("五金".equals(temp.getBsType())){
//                mater.setBsType("hardware");
//            }else if("组装".equals(temp.getBsType())){
//                mater.setBsType("packag");
//            }else if("注塑".equals(temp.getBsType())){
//                mater.setBsType("molding");
//            }else if("表面处理".equals(temp.getBsType())){
//                mater.setBsType("surface");
//            }
            mater.setBsColor(temp.getBsColor());
            mater.setBsMachiningType(temp.getBsMachiningType());
            if(StringUtils.isNotEmpty(temp.getBsProQty())){
                mater.setBsProQty(new BigDecimal(temp.getBsProQty()));
            }
            if(StringUtils.isNotEmpty(temp.getBsWaterGap())){
                mater.setBsWaterGap(temp.getBsWaterGap());
            }
            if(StringUtils.isNotEmpty(temp.getBsCave())){
                mater.setBsCave(temp.getBsCave());
            }
            mater.setBsComponent(temp.getBsComponent());
            mater.setBsMaterName(temp.getBsMaterName());
            mater.setBsModel(temp.getBsModel());
            if(temp.getBsQty()!=null) {
                mater.setBsQty(new BigDecimal(temp.getBsQty()));
            }
            mater.setBsUnit(temp.getBsUnit());
            mater.setPkUnit(temp.getPkUnit());
            mater.setBsRadix(temp.getBsRadix());
//            mater.setBsGeneral(temp.getBsPurchase());
//            mater.setBsGear(temp.getBsGear());

            mater.setPkQuote(temp.getPkQuote());
            mater.setFmemo(temp.getFmemo());
            mater.setBsSupplier(temp.getBsSupplier());
            productMaterList.add(mater);
        }
        productMaterDao.saveAll(productMaterList);
        productMaterTempDao.deleteByPkQuoteAndBsTypeAndCreateBy(quoteId,bsType,userId);
        return ApiResponseResult.success();
    }
}
