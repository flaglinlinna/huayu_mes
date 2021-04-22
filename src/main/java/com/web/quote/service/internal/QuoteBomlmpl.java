package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.service.TodoInfoService;
import com.utils.*;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.dao.ItemTypeWgDao;

import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.dao.QuoteProcessDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteBom;
import com.web.quote.entity.QuoteItem;
import com.web.quote.service.QuoteBomService;
import com.web.quote.service.QuoteProcessService;
import com.web.quote.service.QuoteService;

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

@Service(value = "QuoteBomService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteBomlmpl implements QuoteBomService {

	@Autowired
	private QuoteBomDao quoteBomDao;
	@Autowired
	private ItemTypeWgDao itemTypeWgDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private QuoteItemDao quoteItemDao;
	@Autowired
	private BjWorkCenterDao bjWorkCenterDao;
	@Autowired
	private QuoteService quoteService;
	@Autowired
	TodoInfoService todoInfoService;
	@Autowired
	QuoteProcessService quoteProcessService;
	@Autowired
	QuoteProcessDao quoteProcessDao;
	@Autowired
	private QuoteDao quoteDao;
	
	@Override
	public ApiResponseResult add(QuoteBom quoteBom) throws Exception {
		if(quoteBom == null){
			return ApiResponseResult.failure("外购件清单信息不能为空！");
		}
		//20210120-fyx-物料信息去掉头尾空格
		quoteBom.setBsElement(quoteBom.getBsElement().trim());
		quoteBom.setBsComponent(quoteBom.getBsComponent().trim());
		quoteBom.setBsMaterName(quoteBom.getBsMaterName().trim());
//		BjWorkCenter bjWorkCenter = bjWorkCenterDao.findById((long) quoteBom.getPkBjWorkCenter());
//		if(("out").equals(bjWorkCenter.getBsCode())){
//			return ApiResponseResult.failure("外协不允许在清单中,请到“工艺流程”中选择外协。");
//		}
		//--end
		quoteBomDao.save(quoteBom);
		return ApiResponseResult.success("外购件清单信息新增成功！").data(quoteBom);
	}

	@Override
	public ApiResponseResult edit(QuoteBom quoteBom) throws Exception {
		if(quoteBom == null){
			return ApiResponseResult.failure("外购件清单信息不能为空！");
		}
		if(quoteBom.getId() == null){
			return ApiResponseResult.failure("外购件清单信息ID不能为空！");
		}

		QuoteBom o = quoteBomDao.findById((long) quoteBom.getId());
		if(o == null){
			return ApiResponseResult.failure("该外购件清单信息不存在！");
		}
		o.setBsComponent(quoteBom.getBsComponent().trim());
		o.setBsElement(quoteBom.getBsElement().trim());
		o.setBsModel(quoteBom.getBsModel());
		o.setPkUnit(quoteBom.getPkUnit());
		o.setPkItemTypeWg(quoteBom.getPkItemTypeWg());
		o.setPkBjWorkCenter(quoteBom.getPkBjWorkCenter());
		o.setBsGroups(quoteBom.getBsGroups());
//		BjWorkCenter bjWorkCenter = bjWorkCenterDao.findById((long) quoteBom.getPkBjWorkCenter());
//		if(("out").equals(bjWorkCenter.getBsCode())){
//			return ApiResponseResult.failure("外协不允许在清单中,请到“工艺流程”中选择外协。");
//		}
//		o.setBsProQty(quoteBom.getBsProQty());
		o.setBsMaterName(quoteBom.getBsMaterName().trim());
		o.setBsModel(quoteBom.getBsModel());
		o.setPurchaseUnit(quoteBom.getPurchaseUnit());
//		o.setBsQty(quoteBom.getBsQty());
		o.setBsRadix(quoteBom.getBsRadix());
		o.setBsExplain(quoteBom.getBsExplain());
//		o.setBsUnit(quoteBom.getBsUnit());
		o.setBsSupplier(quoteBom.getBsSupplier());
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		quoteBomDao.save(o);
		return ApiResponseResult.success("编辑成功！");

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
	 * 外购件清单列表修改重审状态
	 * **/
	public ApiResponseResult updateRetrial(Long id,String type,Integer value) throws Exception{
		if(id == null){
			return ApiResponseResult.failure("外购件信息ID不能为空！");
		}
		QuoteBom o  = quoteBomDao.findById((long) id);
		if(o == null){
			return ApiResponseResult.failure("外购件信息不存在！");
		}
		if(("productRetrial").equals(type)){
			o.setProductRetrial(value);
		}
		if(("purchaseRetrial").equals(type)){
			o.setPurchaseRetrial(value);
		}
		if(("outRetrial").equals(type)){
			o.setOutRetrial(value);
		}

		quoteBomDao.save(o);
		return ApiResponseResult.success("外购件清单列表修改重审状态成功！");
	}

	/**
	 * 外购件清单列表修改重审状态
	 * **/
	public ApiResponseResult updateBsGroups(Long id,String bsGroups) throws Exception{
		if(id == null){
			return ApiResponseResult.failure("外购件信息ID不能为空！");
		}
		QuoteBom o  = quoteBomDao.findById((long) id);
		if(o == null){
			return ApiResponseResult.failure("外购件信息不存在！");
		}
		o.setBsGroups(bsGroups);
		quoteBomDao.save(o);
		return ApiResponseResult.success("外购件清单列表修改损耗分组成功！");
	}

	@Override
	public void exportExcel(HttpServletResponse response, Long pkQuote) throws Exception {
//		long startTime=System.currentTimeMillis();   //获取开始时间
		List<QuoteBom> quoteBomList = quoteBomDao.findByDelFlagAndPkQuote(0,pkQuote);
//		List<QuoteBom> quoteBomList = quoteBomDao.findByDelFlag(0);
		String excelPath = "static/excelFile/";
		String fileName = "外购件清单模板.xlsx";
		String[] map_arr = new String[]{"id","wcName","itemType","bsAgent","bsElement","bsComponent","bsMaterName","bsModel",
										"bsGroups","fmemo","bsQty","unitName","purchaseUnit","bsExplain"};
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(QuoteBom quoteBom :quoteBomList){
			Map<String, Object> map = new HashMap<>();
//			map.put("id", quoteBom.getId());
			map.put("id", "");
			map.put("bsElement",quoteBom.getBsElement());
			map.put("bsComponent",quoteBom.getBsComponent());
			map.put("bsMaterName",quoteBom.getBsMaterName());
			map.put("bsModel",quoteBom.getBsModel());
			map.put("bsGroups",quoteBom.getBsGroups());
			if(quoteBom.getItp()!=null) {
				map.put("itemType", quoteBom.getItp().getItemType());
			}
			if(quoteBom.getWc()!=null){
				map.put("wcName", quoteBom.getWc().getWorkcenterName());
			}
			if(quoteBom.getUnit()!=null){
				map.put("unitName", quoteBom.getUnit().getUnitCode());
			}
			map.put("bsAgent",quoteBom.getBsAgent()==1?"是":"否");
			map.put("fmemo",quoteBom.getFmemo());
			map.put("bsQty",quoteBom.getBsQty());
			map.put("purchaseUnit",quoteBom.getPurchaseUnit());
//			map.put("bsProQty",quoteBom.getBsProQty());
//			map.put("bsWaterGap",quoteBom.getBsWaterGap());
//			map.put("bsCave",quoteBom.getBsCave());
//			map.put("bsRadix",quoteBom.getBsRadix());
			map.put("bsExplain",quoteBom.getBsExplain());//lst-20210107-增加采购说明字段
			list.add(map);
		}
		ExcelExport.export(response,list,workbook,map_arr,excelPath+fileName,fileName);
//		EasyExcelUtils.download(excelPath+fileName,map_arr,map_arr,list);
//		long endTime=System.currentTimeMillis(); //获取结束时间
//		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
	}

	/**
	 * 获取报价单列表
	 * **/
	public ApiResponseResult getQuoteBomList(String keyword,String pkQuote,PageRequest pageRequest) throws Exception{
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsComponent", SearchFilter.Operator.LIKE, keyword));
		}
		if (!"null".equals(pkQuote)&&pkQuote!=null) {
			filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		}else {
			List<QuoteBom> quoteBomList = new ArrayList<>();
			return ApiResponseResult.success().data(DataGrid.create(quoteBomList, 0,
					1, 10));
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
		}else return object.toString().trim();
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
			//前两行为标题
			for (int row = 2; row <= maxRow; row++) {
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
				String bsExplain = tranCell(sheet.getRow(row).getCell(10));//lst-20210107-新增采购说明字段
				quoteBom.setPkQuote(pkQuote);
				quoteBom.setBsElement(bsElement.trim());
				quoteBom.setBsComponent(bsComponent.trim());
				quoteBom.setBsMaterName(bsMaterName.trim());
//				quoteBom.setBsItemCode(bsItemCode); 暂定不存，系统生成
				quoteBom.setBsModel(bsModel);
				quoteBom.setFmemo(fmemo);
				quoteBom.setBsExplain(bsExplain);//lst-20210107
				if(StringUtils.isNotEmpty(bsProQty)) {
					quoteBom.setBsProQty(new BigDecimal(bsProQty));
				}

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
	/**
	 * 确认完成外购件清单 
	 * **/
	public ApiResponseResult doStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交
		int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
		if(i>0){
			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
		}
		List<QuoteBom> quoteBomList = quoteBomDao.findByDelFlagAndPkQuote(0,Long.parseLong(quoteId));
		if(quoteBomList.size()<=0){
			return ApiResponseResult.failure("外购件清单信息为空，请先填写后确认提交。");
		}
		//设置该报价单下的bom状态
		quoteBomDao.saveQuoteBomByQuoteId(Long.parseLong(quoteId),1);
		//项目状态设置-状态 2：已完成
		quoteItemDao.switchStatus(2, Long.parseLong(quoteId), code);
		//模具清单、工艺流程增加开始时间+状态变更：进行中

		List<QuoteItem> quoteItemList =quoteItemDao.findByDelFlagAndPkQuoteAndBsCode(0,Long.parseLong(quoteId),"A003");
		if(quoteItemList.size()>0){
			QuoteItem o = quoteItemList.get(0);
			if(o.getBsStatus()==0){
				quoteItemDao.switchStatus(1, Long.parseLong(quoteId), "A003");
				quoteItemDao.setBegTime(new Date(), Long.parseLong(quoteId), "A003");
				quoteItemDao.switchStatus(1, Long.parseLong(quoteId), "A004");
				quoteItemDao.setBegTime(new Date(), Long.parseLong(quoteId), "A004");
			}
		}

		Object data = quoteService.doItemFinish(code, Long.parseLong(quoteId)).getData();
		
		//20210112-fyx-关闭待办
		 todoInfoService.closeByIdAndModel(Long.parseLong(quoteId), "外购件清单");

		 //20210420-hjj-下发工艺流程
		quoteProcessService.addProcessByBom(Long.parseLong(quoteId));
		return ApiResponseResult.success("提交成功！").data(data);
	}

	/**
	 * 取消确认完成外购件清单
	 * **/
	public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交
		int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
		if(i==0){
			return ApiResponseResult.failure("此项目未完成，不需要取消完成。");
		}
		Quote quote = quoteDao.findById(Long.parseLong(quoteId));
		if(quote.getBsStatus()==1||quote.getBsStatus()==4){
			return ApiResponseResult.failure("报价单已提交审批，不能取消完成。");
		}

		//删除已添加的工艺流程信息
		quoteProcessDao.delteQuoteProcessByPkQuote(Long.parseLong(quoteId));

		//设置该报价单下的bom状态
		quoteBomDao.saveQuoteBomByQuoteId(Long.parseLong(quoteId),0);
		//项目状态设置-状态 2：已完成,1进行中
		quoteItemDao.switchStatus(1, Long.parseLong(quoteId), code);
//		//模具清单、工艺流程增加开始时间+状态变更：进行中
//		quoteItemDao.switchStatus(1, Long.parseLong(quoteId), "A003");
//		quoteItemDao.setBegTime(new Date(), Long.parseLong(quoteId), "A003");
//		quoteItemDao.switchStatus(1, Long.parseLong(quoteId), "A004");
//		quoteItemDao.setBegTime(new Date(), Long.parseLong(quoteId), "A004");

		quoteService.doItemFinish(code, Long.parseLong(quoteId));

		//20210305-hjj-打开待办
		todoInfoService.openByIdAndModel(Long.parseLong(quoteId), "外购件清单");
		return ApiResponseResult.success("取消完成成功！");
	}
}
