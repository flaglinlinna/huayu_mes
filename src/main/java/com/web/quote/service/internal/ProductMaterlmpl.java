package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.entity.ProductMater;
import com.web.quote.service.ProductMaterService;
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

@Service(value = "ProductMaterService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductMaterlmpl implements ProductMaterService {
	
	@Autowired
    private ProductMaterDao productMaterDao;
	
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(ProductMater productMater)throws Exception{
    	if(productMater == null){
            return ApiResponseResult.failure("制造部材料信息不能为空！");
        }
        productMater.setCreateDate(new Date());
        productMater.setCreateBy(UserUtil.getSessionUser().getId());
		productMaterDao.save(productMater);
        return ApiResponseResult.success("制造部材料信息添加成功！").data(productMater);
	}

    /**
     * 修改不良类别
     */
    @Override
    @Transactional
    public ApiResponseResult edit(ProductMater hardwareMater) throws Exception {
        if(hardwareMater == null){
            return ApiResponseResult.failure("制造部材料信息不能为空！");
        }
        if(hardwareMater.getId() == null){
            return ApiResponseResult.failure("制造部材料信息ID不能为空！");
        }

        ProductMater o = productMaterDao.findById((long) hardwareMater.getId());
        if(o == null){
            return ApiResponseResult.failure("该制造部材料信息不存在！");
        }
        o.setBsComponent(hardwareMater.getBsComponent());
        o.setBsMaterName(hardwareMater.getBsMaterName());
        o.setBsModel(hardwareMater.getBsModel());
        o.setBsQty(hardwareMater.getBsQty());
        o.setBsRadix(hardwareMater.getBsRadix());
        o.setBsUnit(hardwareMater.getBsUnit());
        o.setBsSupplier(hardwareMater.getBsSupplier());
        o.setBsCave(hardwareMater.getBsCave());
        o.setBsMachiningType(hardwareMater.getBsMachiningType());
        o.setBsWaterGap(hardwareMater.getBsWaterGap());
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        productMaterDao.save(o);
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
        ProductMater o  = productMaterDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("异常类别不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        productMaterDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    //防止读取Excel为null转String 报空指针异常
    public String tranCell(Object object)
    {
        if(object==null||object==""||("").equals(object)){
            return null;
        }else return object.toString();
    }


    //导入模板
    public ApiResponseResult doExcel(MultipartFile[] file) throws Exception{
        try {
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            List<ProductMater> hardwareMaterList = new ArrayList<>();
            for (int row = 1; row <= maxRow; row++) {
                String bsComponent = tranCell(sheet.getRow(row).getCell(0));
                String bsMaterName = tranCell(sheet.getRow(row).getCell(1));
                String bsModel = tranCell(sheet.getRow(row).getCell(2));
                String bsQty = tranCell(sheet.getRow(row).getCell(3));
                String bsUnit = tranCell(sheet.getRow(row).getCell(4));
                String bsRadix = tranCell(sheet.getRow(row).getCell(5));
                String bsSupplier = tranCell(sheet.getRow(row).getCell(6));
                String fmemo = tranCell(sheet.getRow(row).getCell(7));
                ProductMater hardwareMater = new ProductMater();
                hardwareMater.setBsComponent(bsComponent);
                hardwareMater.setBsMaterName(bsMaterName);
                hardwareMater.setBsModel(bsModel);
                hardwareMater.setBsQty(new BigDecimal(bsQty));
                hardwareMater.setBsUnit(bsUnit);
                hardwareMater.setBsRadix(bsRadix);
                hardwareMater.setBsSupplier(bsSupplier);
                hardwareMater.setFmemo(fmemo);
                hardwareMaterList.add(hardwareMater);
            }
            productMaterDao.saveAll(hardwareMaterList);
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
    public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
//            filters1.add(new SearchFilter("errCode", SearchFilter.Operator.LIKE, keyword));
//            filters1.add(new SearchFilter("errName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<ProductMater> spec = Specification.where(BaseService.and(filters, ProductMater.class));
        Specification<ProductMater> spec1 = spec.and(BaseService.or(filters1, ProductMater.class));
        Page<ProductMater> page = productMaterDao.findAll(spec1, pageRequest);

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
        Specification<ProductMater> spec = Specification.where(BaseService.and(filters, ProductMater.class));
        Specification<ProductMater> spec1 = spec.and(BaseService.or(filters1, ProductMater.class));
        Page<ProductMater> page = productMaterDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}
