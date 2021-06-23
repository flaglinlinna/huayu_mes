package com.web.quote.service.internal;

import com.alibaba.fastjson.JSON;
import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.ExcelExport;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.ProductMaterDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.*;
import com.web.quote.service.ProductMaterService;
import com.web.quote.service.QuoteProductService;

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

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service(value = "ProductMaterService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductMaterlmpl implements ProductMaterService {
	
	@Autowired
    private ProductMaterDao productMaterDao;

    @Autowired
    private UnitDao unitDao;

    @Autowired
    private QuoteDao quoteDao;

    @Autowired
    QuoteProcessDao quoteProcessDao;
    
    @Autowired
    private QuoteItemDao quoteItemDao;
    @Autowired
    private QuoteProductService quoteProductService;
	
	/**
     * 新增
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
     * 修改
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
        productMaterDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }

    /**
     * 删除
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

    @Override
    public ApiResponseResult getBomSelect(String pkQuote) throws Exception {
        return ApiResponseResult.success().data(quoteProcessDao.getBomName(pkQuote));
    }

    public void exportExcel(HttpServletResponse response, String bsType, Long quoteId) throws Exception{
        String excelPath = "static/excelFile/";
        String fileName = "";
        String[] map_arr = null;
        //五金材料导入顺序: 零件名称、材料名称、规格、用量、单位、基数、供应商、备注
        //组装材料导入顺序: 零件名称、材料名称、规格、用量、单位、基数、供应商、备注
        //注塑材料导入顺序: 零件名称、材料名称、规格、制品量、单位、基数、水口数、穴数、备注
        //表面处理导入顺序: 零件名称、加工类型、配色工艺、材料名称、规格、用料、单位、基数、备注

        //2021-04-29 五金和注塑 零件名称1、材料名称2、规格3、材料用量4、制品重5、材料用量单位6、水口数7、穴数8
        //2021-04-29 表面和组装 零件名称1、材料名称2、规格3、材料用量4、材料用量单位5
        //20210226-hjj-导出去除基数
        if(("hardware").equals(bsType)){
            fileName = "五金材料模板.xlsx";
//            map_arr = new String[]{"id","bsComponent","bsMaterName","bsModel","bsQty","bsUnit","bsRadix","bsSupplier","fmemo"};
            map_arr = new String[]{"id","bsComponent","bsMaterName","bsModel","bsQty","bsProQty","bsUnit","bsWaterGap","bsCave"};
        }else if(("molding").equals(bsType)){
            fileName = "注塑材料模板.xlsx";
            map_arr = new String[]{"id","bsComponent","bsMaterName","bsModel","bsQty","bsProQty","bsUnit","bsWaterGap","bsCave"};
        }else if(("surface").equals(bsType)){
            fileName = "表面处理材料模板.xlsx";
            map_arr = new String[]{"id","bsComponent","bsMaterName","bsModel","bsQty","bsUnit"};
        }else if(("packag").equals(bsType)){
            fileName = "组装材料模板.xlsx";
            map_arr = new String[]{"id","bsComponent","bsMaterName","bsModel","bsQty","bsUnit"};
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
//        Resource resource = new ClassPathResource(excelPath+fileName);
//        InputStream in = resource.getInputStream();

//        XSSFWorkbook workbook = new XSSFWorkbook(in);
//        in.close();
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("bsType", SearchFilter.Operator.EQ, bsType));
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, quoteId));
        Specification<ProductMater> spec = Specification.where(BaseService.and(filters, ProductMater.class));
        List<ProductMater> productMaterList  = productMaterDao.findAll(spec);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ProductMater bs : productMaterList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", bs.getId());
//            if(("hardware").equals(bsType)){
//                map.put("bsType", "五金");
//            }else if(("molding").equals(bsType)){
//                map.put("bsType", "注塑");
//            }else if(("surface").equals(bsType)){
//                map.put("bsType", "表面处理");
//            }else if(("packag").equals(bsType)){
//                map.put("bsType", "组装");
//            }
            map.put("bsComponent", bs.getBsComponent());
            map.put("bsMaterName", bs.getBsMaterName());
            map.put("bsModel", bs.getBsModel());
//            map.put("bsRadix", bs.getBsRadix());
            map.put("bsQty", bs.getBsQty());
            map.put("bsProQty", bs.getBsProQty());
            if(bs.getUnit()!=null){
                map.put("bsUnit", bs.getUnit().getUnitCode());
            }
//            map.put("fmemo", bs.getFmemo());
//            map.put("bsSupplier", bs.getBsSupplier());
            map.put("bsWaterGap", bs.getBsWaterGap());
            map.put("bsCave", bs.getBsCave());
//            map.put("bsMachiningType", bs.getBsMachiningType());
//            map.put("bsColor", bs.getBsColor());
            list.add(map);
        }
        ExcelExport.export(response,list,workbook,map_arr,excelPath+fileName,fileName);
    }

    /**
     * 确认完成
     */
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long quoteId,String bsType,String bsCode,List<ProductMater> productMaterList2) throws Exception{
    	
    	//判断状态是否已执行过确认提交-lst-20210112
    	int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,quoteId,bsCode, 2);
    	if(i>0){
    		return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
    	}
    	productMaterDao.saveAll(productMaterList2);
        List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuoteAndBsType(0,quoteId,bsType);
        for(ProductMater o : productMaterList) {
//            if("hardware".equals(bsType)) {
//                if (o.getBsQty() == null ) {
//                    return ApiResponseResult.failure("用量存在空值,请检查后再确认！");
//                }
//            }else
            if(("PCS").equals(o.getPurchaseUnit())){
                if ( o.getBsQty() == null ) {
                    return ApiResponseResult.failure("用量不能为空,请检查后再确认！");
                }
            }else if("molding".equals(bsType)||"hardware".equals(bsType)) {
                if ( o.getBsProQty() == null || o.getBsCave() == null ||("0").equals(o.getBsCave()) || o.getBsWaterGap() == null) {
                    return ApiResponseResult.failure("制品重(g)、穴数、水口数不能为空,请检查后再确认！");
                }
            }else if("surface".equals(bsType)) {
//                o.getBsColor() == null || o.getBsMachiningType() == null || 配色工艺、加工类型、
                if ( o.getBsQty() == null ) {
                    return ApiResponseResult.failure("用量不能为空,请检查后再确认！");
                }
            }else if("packag".equals(bsType)) {
                if (o.getBsQty() == null ) {
                    return ApiResponseResult.failure("用量存在空值,请检查后再确认！");
                }
            }
            o.setBsStatus(1);
            o.setLastupdateDate(new Date());
            o.setLastupdateBy(UserUtil.getSessionUser().getId());
        }
        productMaterDao.saveAll(productMaterList);
        //项目状态设置-状态 2：已完成
      	quoteItemDao.switchStatus(2, quoteId, bsCode);
       //增加处理人-20210112-lst-param(用户名,用户id,报价单ID,项目编码)
        quoteItemDao.setPerson(UserUtil.getSessionUser().getUserName(),UserUtil.getSessionUser().getId(),quoteId, bsCode);
        //设置结束时间
        quoteItemDao.setEndTime(new Date(), quoteId, bsCode);
        
      //20210121-fyx-统一修改状态,返回是否全部完成 0否1是
        Object data = quoteProductService.doItemFinish(bsCode, quoteId,3).getData();
//        quoteDao.findById(quoteId);
        return ApiResponseResult.success("确认完成成功！").data(data);
    }

    //取消完成
    @Override
    public ApiResponseResult cancelStatus(Long quoteId, String bsType, String bsCode) throws Exception {
        Quote quote = quoteDao.findById((long) quoteId);
        Integer quoteStatus = 0; //判断当前报价单是否已经发起审核
        if(bsType.equals("hardware")){
            quoteStatus = quote.getBsStatus2Hardware();
        }else if(bsType.equals("molding")){
            quoteStatus = quote.getBsStatus2Molding();
        }else if(bsType.equals("surface")){
            quoteStatus = quote.getBsStatus2Surface();
        }else if(bsType.equals("packag")){
            quoteStatus = quote.getBsStatus2Packag();
        }
        if(quoteStatus ==4||quoteStatus==2) {
            return ApiResponseResult.failure("发起审批后不能取消确认");
        } else {
//            List<QuoteItem> quoteItemList = quoteItemDao.findByDelFlagAndPkQuoteAndBsCode(0,quoteId,bsCode);
//            if(quoteItemList.size()>0){
//                if(quoteItemList.get(0).getBsEndTime()==null){
//                    return ApiResponseResult.failure("自动确认完成的项目不能取消完成");
//                }
//            }
            //项目状态设置-状态 1：未完成
            quoteItemDao.switchStatus(1, quoteId, bsCode);
            //设置结束时间
//            quoteItemDao.setEndTime(null, quoteId, bsCode);
            //取消报价单对应类别的完成状态
            quoteProductService.doItemFinish(bsCode, quoteId,1);
            List<ProductMater> productMaterList  = productMaterDao.findByDelFlagAndPkQuoteAndBsType(0,quoteId,bsType);
            for(ProductMater o : productMaterList){
                //修改所有材料为未完成
                o.setBsStatus(0);
                o.setLastupdateDate(new Date());
                o.setLastupdateBy(UserUtil.getSessionUser().getId());
            }
            productMaterDao.saveAll(productMaterList);
            return ApiResponseResult.success("取消完成成功");
        }

    }

    //防止读取Excel为null转String 报空指针异常
    public String tranCell(Object object)
    {
        if(object==null||object==""||("").equals(object)){
            return null;
        }else return object.toString().trim();
    }


    //导入模板
    public ApiResponseResult doExcel(MultipartFile[] file,String bsType,Long quoteId) throws Exception{
        try {
            Date doExcleDate = new Date();
            Long userId = UserUtil.getSessionUser().getId();
            InputStream fin = file[0].getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取最后一行的num，即总行数。此处从0开始计数
            int maxRow = sheet.getLastRowNum();
            List<ProductMater> hardwareMaterList = new ArrayList<>();
            //五金材料导入顺序: 零件名称、材料名称、规格、用量、单位、基数、供应商、备注
            //组装材料导入顺序: 零件名称、材料名称、规格、用量、单位、基数、供应商、备注
            //注塑材料导入顺序: 零件名称、材料名称、规格、制品量、单位、基数、水后数、穴数、备注
            //表面处理导入顺序: 零件名称、加工类型、配色工艺、材料名称、规格、用料、单位、基数、备注
            for (int row = 2; row <= maxRow; row++) {
                String bsComponent = tranCell(sheet.getRow(row).getCell(0));
                String bsMaterName = tranCell(sheet.getRow(row).getCell(1));
                String bsModel = tranCell(sheet.getRow(row).getCell(2));
                String bsQty = tranCell(sheet.getRow(row).getCell(3));
                String bsUnit = tranCell(sheet.getRow(row).getCell(4));
                String bsRadix = tranCell(sheet.getRow(row).getCell(5));
                String bsSupplier = tranCell(sheet.getRow(row).getCell(6));
                String fmemo = tranCell(sheet.getRow(row).getCell(7));
                String fmemo1 = tranCell(sheet.getRow(row).getCell(8));
                ProductMater hardwareMater = new ProductMater();

                //设置类型
                hardwareMater.setBsType(bsType);
                hardwareMater.setPkQuote(quoteId);
                hardwareMater.setBsComponent(bsComponent);
                if(("molding").equals(bsType)){
                    hardwareMater.setBsProQty(new BigDecimal(bsQty));
                    hardwareMater.setBsRadix(bsRadix);
                    hardwareMater.setBsWaterGap(bsSupplier);
                    hardwareMater.setBsCave(bsSupplier);
                    hardwareMater.setBsCave(fmemo1);
                } else if(("surface").equals(bsType)){
                    hardwareMater.setBsMachiningType(bsMaterName);
                    hardwareMater.setBsColor(bsModel);
                    hardwareMater.setBsMaterName(bsQty);
                    hardwareMater.setBsQty(new BigDecimal(bsRadix));
                    hardwareMater.setBsModel(bsUnit);
                    hardwareMater.setBsUnit(bsSupplier);
                    List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(bsSupplier,0);
                    if(unitList!=null&& unitList.size()>0){
                        hardwareMater.setPkUnit(unitList.get(0).getId());
                    }
                    hardwareMater.setBsRadix(fmemo1);
                }else {
                    hardwareMater.setBsMaterName(bsMaterName);
                    hardwareMater.setBsModel(bsModel);
                    hardwareMater.setBsQty(new BigDecimal(bsQty));
                    hardwareMater.setBsUnit(bsUnit);
                    List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(bsUnit,0);
                    if(unitList!=null&& unitList.size()>0){
                        hardwareMater.setPkUnit(unitList.get(0).getId());
                    }
                    hardwareMater.setBsRadix(bsRadix);
                    hardwareMater.setBsSupplier(bsSupplier);
                    hardwareMater.setFmemo(fmemo);
                }

                hardwareMater.setCreateBy(userId);
                hardwareMater.setCreateDate(doExcleDate);
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
    public ApiResponseResult getList(String keyword,String bsType, String quoteId,String bsAgent,PageRequest pageRequest) throws Exception {
        // 查询条件1
        List<SearchFilter> filters = new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("bsSingleton", SearchFilter.Operator.EQ, 0));
//        filters.add(new SearchFilter("purchaseUnit", SearchFilter.Operator.NEQ, "PCS"));
        if (StringUtils.isNotEmpty(bsType)) {
            filters.add(new SearchFilter("bsType", SearchFilter.Operator.EQ, bsType));
        }
        if(StringUtils.isNotEmpty(bsAgent)){
            filters.add(new SearchFilter("bsAgent", SearchFilter.Operator.EQ, bsAgent));
        }
        if (!"null".equals(quoteId)&&quoteId!=null) {
            filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, quoteId));
        }else {
            List<ProductMater> productMaterList = new ArrayList<>();
            return ApiResponseResult.success().data(DataGrid.create(productMaterList, 0,
                    1, 10));
        }
        // 查询2
        List<SearchFilter> filters1 = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyword)) {
            filters1.add(new SearchFilter("bsComponent", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsMaterName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<ProductMater> spec = Specification.where(BaseService.and(filters, ProductMater.class));
        Specification<ProductMater> spec1 = spec.and(BaseService.or(filters1, ProductMater.class));
        Page<ProductMater> page = productMaterDao.findAll(spec1, pageRequest);
        List<Unit> unitList = unitDao.findByDelFlag(0);
        for(ProductMater pm:page.getContent()){
            if(unitList.size()>0){
                String str1 = JSON.toJSONString(unitList); //此行转换
                pm.setBsUnitList(str1);
            }
        }
        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 修改单位
     */
    @Override
    @Transactional
    public ApiResponseResult updateUnit(Long id,Long unitId) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("制造材料ID不能为空！");
        }
         ProductMater o = productMaterDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("制造材料不存在！");
        }
        if(unitId!=null) {
            Unit unit = unitDao.findById((long) unitId);
            if (unit != null) {
                o.setBsUnit(unit.getUnitName());
            }
            o.setPkUnit(unitId);
        }else {
            o.setBsUnit(null);
            o.setPkUnit(null);
        }
        o.setPkUnit(unitId);
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setLastupdateDate(new Date());
        productMaterDao.save(o);
        return ApiResponseResult.success("更新单位成功！");
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

	@Override
	public ApiResponseResult doSumFee(Long pkQuote) throws Exception {
		// TODO Auto-generated method stub
		//五金材料,表面处理,组装-材料单价*材料用量/基数
		//注塑-材料单价*(制品重(g)+水口重/穴数)/基数
		return null;
	}

    @Override
    public ApiResponseResult editMaterList(List<ProductMater> productMaterList) throws Exception {
        // TODO Auto-generated method stub
//		List<QuoteProcess> lqp = quoteProcessDao.findByDelFlagAndPkQuoteAndBsNameOrderByBsOrder(0,Long.valueOf(quoteId),name);
        productMaterDao.saveAll(productMaterList);
        return ApiResponseResult.success();
    }
}
