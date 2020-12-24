package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.ProductMaterTempDao;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "ProductMaterTempService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductMaterTemplmpl implements ProductMaterTempService {
	
	@Autowired
    private ProductMaterTempDao productMaterTempDao;

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
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            List<ProductMaterTemp> hardwareMaterList = new ArrayList<>();
            //五金材料导入顺序: 零件名称、材料名称、规格、用量、单位、基数、供应商、备注
            //组装材料导入顺序: 零件名称、材料名称、规格、用量、单位、基数、供应商、备注
            //注塑材料导入顺序: 零件名称、材料名称、规格、制品量、单位、基数、水口量、穴数、备注
            //表面处理导入顺序: 零件名称、加工类型、配色工艺、材料名称、规格、用料、单位、基数、备注
            for (int row = 2; row <= maxRow; row++) {
                String mid = tranCell(sheet.getRow(row).getCell(0));
                String bsComponent = tranCell(sheet.getRow(row).getCell(1));
                String bsMaterName = tranCell(sheet.getRow(row).getCell(2));
                String bsModel = tranCell(sheet.getRow(row).getCell(3));
                String bsQty = tranCell(sheet.getRow(row).getCell(4));
                String bsUnit = tranCell(sheet.getRow(row).getCell(5));
                String bsRadix = tranCell(sheet.getRow(row).getCell(6));
                String bsSupplier = tranCell(sheet.getRow(row).getCell(7));
                String fmemo = tranCell(sheet.getRow(row).getCell(8));
                String fmemo1 = tranCell(sheet.getRow(row).getCell(9));
                ProductMaterTemp temp = new ProductMaterTemp();

                if(StringUtils.isNotEmpty(mid)){
                    temp.setMid(Long.parseLong(mid));
                }

                //设置类型
                temp.setBsType(bsType);
                temp.setPkQuote(quoteId);
                temp.setBsComponent(bsComponent);
                if(("molding").equals(bsType)){
//                    hardwareMater.setBsProQty(new BigDecimal(bsQty));
                    temp.setBsRadix(bsRadix);
                    temp.setBsWaterGap(bsSupplier);
                    temp.setBsCave(fmemo1);
                } else if(("surface").equals(bsType)){
                    temp.setBsMachiningType(bsMaterName);
                    temp.setBsColor(bsModel);
                    temp.setBsMaterName(bsQty);
                    temp.setBsQty(bsRadix);
                    temp.setBsModel(bsUnit);
                    temp.setBsUnit(bsSupplier);
                    List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(bsSupplier,0);
                    if(unitList!=null&& unitList.size()>0){
                        temp.setPkUnit(unitList.get(0).getId());
                    }
                    temp.setBsRadix(fmemo1);
                }else {
                    temp.setBsMaterName(bsMaterName);
                    temp.setBsModel(bsModel);
                    temp.setBsQty(bsQty);
                    temp.setBsUnit(bsUnit);
                    List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(bsUnit,0);
                    if(unitList!=null&& unitList.size()>0){
                        temp.setPkUnit(unitList.get(0).getId());
                    }
                    temp.setBsRadix(bsRadix);
                    temp.setBsSupplier(bsSupplier);
                    temp.setFmemo(fmemo);
                }
                temp.setCreateBy(userId);
                temp.setCreateDate(doExcleDate);
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

    //采购填报价格导入
    @Override
    public ApiResponseResult importByTemp(String quoteId)throws Exception {
        Long userId = UserUtil.getSessionUser().getId();
        return null;
    }
}
