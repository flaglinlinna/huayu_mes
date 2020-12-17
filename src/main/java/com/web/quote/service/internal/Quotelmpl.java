package com.web.quote.service.internal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.dao.ItemTypeWgDao;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.entity.QuoteBom;
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
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProfitProdDao;
import com.web.basePrice.entity.ProfitProd;
import com.web.basic.entity.Client;
import com.web.basic.entity.Defective;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.entity.QuoteItemBase;
import com.web.quote.service.QuoteService;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "QuoteService")
@Transactional(propagation = Propagation.REQUIRED)
public class Quotelmpl implements QuoteService {
	
	@Autowired
    private QuoteDao quoteDao;
	@Autowired
    private QuoteItemDao quoteItemDao;
	@Autowired
	private ProfitProdDao profitProdDao;
	@Autowired
    private QuoteItemBaseDao quoteItemBaseDao;
	@Autowired
    private TodoInfoService todoInfoService;

	@Autowired
	private QuoteBomDao quoteBomDao;
	@Autowired
	private ItemTypeWgDao itemTypeWgDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private BjWorkCenterDao bjWorkCenterDao;
	
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
    	//1:生成报价编号
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        quote.setBsCode("EQ-" + dateStr);  //编号格式：EQ-年月日时分秒
        
    	quote.setCreateDate(new Date());
    	quote.setCreateBy(UserUtil.getSessionUser().getId());
    	quote = quoteDao.save(quote);
    	//2:建立子表-编码-项目名-代办人-开始/结束时间-进度状态【需有基础信息】
    	List<QuoteItem> lqi = new ArrayList<QuoteItem>();
    	//获取配置的待办项目
    	List<QuoteItemBase> lqb = quoteItemBaseDao.findByDelFlag(0);
    	for(QuoteItemBase qb:lqb){
    		QuoteItem qi = new QuoteItem();
    		qi.setPkQuote(quote.getId());
    		qi.setBsCode(qb.getBsCode());
    		qi.setBsName(qb.getBsName());
    		qi.setToDoBy(qb.getToDoBy());
    		qi.setBsPerson(qb.getBsPerson());
    		qi.setCreateDate(new Date());
    		qi.setCreateBy(UserUtil.getSessionUser().getId());
    		lqi.add(qi);
    	}
    	quoteItemDao.saveAll(lqi);
    	//3:发送待办
    	for(QuoteItemBase qb:lqb){
    		TodoInfo todoInfo = new TodoInfo();
    		todoInfo.setBsType(1);//待办事项都是1
    		todoInfo.setBsUserId(qb.getToDoBy());
    		todoInfo.setBsTitle("新增报价-"+qb.getBsName()+"的资料待录入");
    		todoInfo.setBsContent(quote.getBsCode()+"-"+quote.getBsCustName()+ "的报价单");
    		todoInfo.setBsRouter("/quote/toQuoteAdd?id="+quote.getId());
    		todoInfo.setBsReferId(quote.getId()); //关联ID

    		todoInfoService.add(todoInfo);
    	}
    	
        return ApiResponseResult.success("报价单新增成功！").data(lqi);
	}
    /**
     * 获取产品利润率维护表
     * **/
    
    public ApiResponseResult getProfitProd()throws Exception{
    	List<ProfitProd> list=profitProdDao.findByDelFlag(0);
    	return ApiResponseResult.success().data(list);
    }
    
    /**
     * 获取报价单列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,PageRequest pageRequest)throws Exception{
    	// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsProd", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Quote> spec = Specification.where(BaseService.and(filters, Quote.class));
		Specification<Quote> spec1 = spec.and(BaseService.or(filters1, Quote.class));
		Page<Quote> page = quoteDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
    /**
     * 获取报价单-项目列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getItemPage(Long id)throws Exception{
    	List<QuoteItem> list=quoteItemDao.findByDelFlagAndPkQuote(0,id);
    	return ApiResponseResult.success().data(list);
    }
    
    /**
     * 变更报价单状态
     * **/
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
    	if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置报价单状态！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsStatus(bsStatus);
        quoteDao.save(o);
        return ApiResponseResult.success("设置报价单状态成功！").data(o);
    }
    
    /**
     * 编辑报价单
     * **/
    @Override
    @Transactional
    public ApiResponseResult edit(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
        if(quote.getId() == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) quote.getId());
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsType(quote.getBsType());
        o.setBsFinishTime(quote.getBsFinishTime());
        o.setBsRemarks(quote.getBsRemarks());
        o.setBsSimilarProd(quote.getBsSimilarProd());
        o.setPkProfitProd(quote.getPkProfitProd());
        o.setBsCustName(quote.getBsCustName());
        o.setBsPosition(quote.getBsPosition());
        o.setBsMaterial(quote.getBsMaterial());
        o.setBsChkOutItem(quote.getBsChkOutItem());
        o.setBsChkOut(quote.getBsChkOut());
        o.setBsFunctionItem(quote.getBsFunctionItem());
        o.setBsFunction(quote.getBsFunction());
        o.setBsRequire(quote.getBsRequire());
        o.setBsLevel(quote.getBsLevel());
        o.setBsCustRequire(quote.getBsCustRequire());
        quoteDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }
    
    /**
     * 获取单张报价单
     * **/
    public ApiResponseResult getSingle(Long id)throws Exception{
    	if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        return ApiResponseResult.success().data(o);
    }

	/**
	 * 删除外购件清单列表
	 * **/
	public ApiResponseResult deleteQuoteBom(Long id) throws Exception{
		if(id == null){
			return ApiResponseResult.failure("外购件信息ID不能为空！");
		}
		QuoteBom o  = quoteBomDao.findById((long) id);
		if(o == null){
			return ApiResponseResult.failure("外购件信息不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		quoteBomDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 获取报价单列表
	 * **/
	public ApiResponseResult getQuoteBomList(String keyword,Long pkQuote,PageRequest pageRequest) throws Exception{
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsComponent", SearchFilter.Operator.LIKE, keyword));
		}
		if (pkQuote!=null) {
			filters1.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		}
		Specification<QuoteBom> spec = Specification.where(BaseService.and(filters, QuoteBom.class));
		Specification<QuoteBom> spec1 = spec.and(BaseService.or(filters1, QuoteBom.class));
		Page<QuoteBom> page = quoteBomDao.findAll(spec1, pageRequest);

		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	//防止读取Excel为null转String 报空指针异常
	public String tranCell(Object object)
	{
		if(object==null||object==""||("").equals(object)){
			return null;
		}else return object.toString();
	}

	//导入模板
	@Override
	public ApiResponseResult doQuoteBomExcel(MultipartFile[] file, Long pkQuote) throws Exception{
		try {
//			if(pkQuote ==null){
//				return ApiResponseResult.failure("导入失败！请检查选中的报价单！");
//			}

			Date doExcleDate = new Date();
			Long userId = UserUtil.getSessionUser().getId();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
			List<QuoteBom> quoteBomList = new ArrayList<>();
			for (int row = 1; row <= maxRow; row++) {
				QuoteBom quoteBom = new QuoteBom();
				String bsElement = tranCell(sheet.getRow(row).getCell(0));
				String bsComponent = tranCell(sheet.getRow(row).getCell(1));
				String wc = tranCell(sheet.getRow(row).getCell(2));
				List<BjWorkCenter> bjWorkCenterList = bjWorkCenterDao.findByWorkcenterNameAndDelFlag(wc,0);
				if(bjWorkCenterList!=null&& bjWorkCenterList.size()>0){
					quoteBom.setPkBjWorkCenter(bjWorkCenterList.get(0).getId());
				}
//				String bsItemCode = tranCell(sheet.getRow(row).getCell(3));
				String itp = tranCell(sheet.getRow(row).getCell(3));
				List<ItemTypeWg> itemTypeWgList = itemTypeWgDao.findByItemTypeAndDelFlag(itp,0);
				if(itemTypeWgList!=null&&itemTypeWgList.size()>0){
					quoteBom.setPkItemTypeWg(itemTypeWgList.get(0).getId());
				}
				String bsMaterName = tranCell(sheet.getRow(row).getCell(4));
				String bsModel = tranCell(sheet.getRow(row).getCell(5));
				String fmemo = tranCell(sheet.getRow(row).getCell(6));
				String bsProQty = tranCell(sheet.getRow(row).getCell(7));
				String unit = tranCell(sheet.getRow(row).getCell(8));
				List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(unit,0);
				if(unitList!=null&& unitList.size()>0){
					quoteBom.setPkUnit(unitList.get(0).getId());
				}
				String bsRadix = tranCell(sheet.getRow(row).getCell(9));
				quoteBom.setPkQuote(pkQuote);
				quoteBom.setBsElement(bsElement);
				quoteBom.setBsComponent(bsComponent);
				quoteBom.setBsMaterName(bsMaterName);
//				quoteBom.setBsItemCode(bsItemCode); 暂定不存，系统生成
				quoteBom.setBsModel(bsModel);
				quoteBom.setFmemo(fmemo);
				if(StringUtils.isNotEmpty(bsProQty)) {
					quoteBom.setBsQty(new BigDecimal(bsProQty));
				}
				quoteBom.setBsElement(bsElement);
				quoteBom.setBsElement(bsElement);
				quoteBom.setBsRadix(bsRadix);
				quoteBom.setCreateDate(doExcleDate);
				quoteBom.setCreateBy(userId);
				quoteBomList.add(quoteBom);
			}
			quoteBomDao.saveAll(quoteBomList);
			return ApiResponseResult.success("导入成功");
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}
}
