package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.Proc;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.ProductProcessDao;
import com.web.quote.entity.ProductProcess;

import com.web.quote.service.ProductProcessService;
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

@Service(value = "ProductProcessService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductProcesslmpl implements ProductProcessService {
	
	@Autowired
    private ProductProcessDao productProcessDao;

    @Autowired
    private ProcDao procDao;
	
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(ProductProcess productProcess)throws Exception{
    	if(productProcess == null){
            return ApiResponseResult.failure("制造部材料信息不能为空！");
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
        if(productProcess == null){
            return ApiResponseResult.failure("制造部材料信息不能为空！");
        }
        if(productProcess.getId() == null){
            return ApiResponseResult.failure("制造部材料信息ID不能为空！");
        }

        ProductProcess o = productProcessDao.findById((long) productProcess.getId());
        if(o == null){
            return ApiResponseResult.failure("该制造部材料信息不存在！");
        }
        o.setBsName(productProcess.getBsName());
        o.setBsOrder(productProcess.getBsOrder());
        o.setPkProc(productProcess.getPkProc());
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
        productProcessDao.save(o);
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
        ProductProcess o  = productProcessDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        productProcessDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    //防止读取Excel为null转String 报空指针异常
    public String tranCell(Object object)
    {
        if(object==null||object==""||("").equals(object)){
            return null;
        }else return object.toString();
    }

    public ApiResponseResult exportExcel(MultipartFile[] file, String bsType, Long quoteId) throws Exception{
        String fileName = "五金工艺模板";
        InputStream in = this.getClass().getResourceAsStream("/static/excelFile/"+fileName);
        return null;
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
            List<ProductProcess> hardwareMaterList = new ArrayList<>();
            //五金工艺导入顺序: 零件名称、工序顺序、工序名称、工序说明、机台类型、基数、人数、成型周期(S)、工序良率、备注
            //注塑工艺导入顺序: 零件名称、工序顺序、工序名称、工序说明、机台类型、基数、穴数、成型周期(S)、加工人数、工序良率、备注
            //组装工艺导入顺序: 零件名称、工序顺序、工序名称、工序说明、机台类型、基数、人数、产能、工序良率、备注
            //表面工艺导入顺序: 零件名称、工序顺序、工序名称、工序说明、机台类型、基数、人数、产能、工序良率、备注
            for (int row = 2; row <= maxRow; row++) {
                String bsName = tranCell(sheet.getRow(row).getCell(0));
                String bsOrder = tranCell(sheet.getRow(row).getCell(1));
                String procName = tranCell(sheet.getRow(row).getCell(2));
                String procFmemo = tranCell(sheet.getRow(row).getCell(3));
                String bsModelType = tranCell(sheet.getRow(row).getCell(4));
                String bsRadix = tranCell(sheet.getRow(row).getCell(5));
                String bsUserNum = tranCell(sheet.getRow(row).getCell(6));
                String fmemo = tranCell(sheet.getRow(row).getCell(7));
                String fmemo1 = tranCell(sheet.getRow(row).getCell(8));
                ProductProcess process = new ProductProcess();

                //设置类型
                process.setBsType(bsType);
                process.setPkQuote(quoteId);
                process.setBsName(bsName);
                process.setBsOrder(Integer.parseInt(bsOrder));
                List<Proc> procList = procDao.findByDelFlagAndProcName(0,procName);
                if(procList.size()>0&&procList!=null){
                    process.setPkProc(procList.get(0).getId());
                }
                process.setBsModelType(bsModelType);
//                if(("molding").equals(bsType)){
//                    process.setBsProQty(new BigDecimal(bsQty));
//                    process.setBsRadix(bsRadix);
//                    process.setBsWaterGap(bsSupplier);
//                    process.setBsCave(bsSupplier);
//                    process.setBsCave(fmemo1);
//                } else if(("surface").equals(bsType)){
//                    process.setBsMachiningType(bsMaterName);
//                    process.setBsColor(bsModel);
//                    process.setBsMaterName(bsQty);
//                    process.setBsQty(new BigDecimal(bsRadix));
//                    process.setBsModel(bsUnit);
//                    process.setBsUnit(bsSupplier);
//                    List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(bsSupplier,0);
//                    if(unitList!=null&& unitList.size()>0){
//                        process.setPkUnit(unitList.get(0).getId());
//                    }
//                    process.setBsRadix(fmemo1);
//                }else {
//                    process.setBsMaterName(bsMaterName);
//                    process.setBsModel(bsModel);
//                    process.setBsQty(new BigDecimal(bsQty));
//                    process.setBsUnit(bsUnit);
//                    List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(bsUnit,0);
//                    if(unitList!=null&& unitList.size()>0){
//                        process.setPkUnit(unitList.get(0).getId());
//                    }
//                    process.setBsRadix(bsRadix);
//                    process.setBsSupplier(bsSupplier);
//                    process.setFmemo(fmemo);
//                }

                process.setCreateBy(userId);
                process.setCreateDate(doExcleDate);
                hardwareMaterList.add(process);
            }
            productProcessDao.saveAll(hardwareMaterList);
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

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 查询报价单下 五金材料列表
     */
    @Override
    @Transactional
    public ApiResponseResult getListByPkQuote(Long pkQuote, PageRequest pageRequest) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        if (pkQuote!=null) {
            filters1.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
        }
        Specification<ProductProcess> spec = Specification.where(BaseService.and(filters, ProductProcess.class));
        Specification<ProductProcess> spec1 = spec.and(BaseService.or(filters1, ProductProcess.class));
        Page<ProductProcess> page = productProcessDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}
