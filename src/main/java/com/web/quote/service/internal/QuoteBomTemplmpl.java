package com.web.quote.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.dao.ItemTypeWgDao;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.*;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteBom;
import com.web.quote.entity.QuoteBomTemp;
import com.web.quote.entity.QuoteProcess;
import com.web.quote.service.QuoteBomTempService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "QuoteBomTempService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteBomTemplmpl implements QuoteBomTempService {

	@Autowired
	private QuoteBomTempDao quoteBomTempDao;

	@Autowired
	private QuoteBomDao quoteBomDao;

	@Autowired
	private QuoteDao quoteDao;

	@Autowired
	private ItemTypeWgDao itemTypeWgDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private BjWorkCenterDao bjWorkCenterDao;

	@Autowired
	private ProductMaterDao productMaterDao;

	@Autowired
	private QuoteProcessDao quoteProcessDao;

	/**
	 * 获取报价单列表
	 * **/
	public ApiResponseResult getList(String keyword,String pkQuote,PageRequest pageRequest) throws Exception{
		// 查询条件1
		Long userId = UserUtil.getSessionUser().getId();
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		filters.add(new SearchFilter("createBy", SearchFilter.Operator.EQ, userId));
		Specification<QuoteBomTemp> spec = Specification.where(BaseService.and(filters, QuoteBomTemp.class));
		Page<QuoteBomTemp> page = quoteBomTempDao.findAll(spec, pageRequest);
		return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	//防止读取Excel为null转String 报空指针异常
	public String tranCell(XSSFCell xssfCell)
	{
		if(xssfCell==null||("").equals(xssfCell.getRawValue())){
			return null;
		}else {
			if(xssfCell.getCellType()== Cell.CELL_TYPE_FORMULA){
				try {
					return String.valueOf(xssfCell.getNumericCellValue());
				} catch (IllegalStateException e) {
					return String.valueOf(xssfCell.getRichStringCellValue());
				}
			}else {
				return xssfCell.toString().trim();
			}
		}
	}

	//导入模板
	@Override
	public ApiResponseResult doExcel(MultipartFile[] file, Long pkQuote) throws Exception{
		try {
			if(pkQuote ==null){
				return ApiResponseResult.failure("导入失败！请检查选中的报价单！");
			}
			Long userId = UserUtil.getSessionUser().getId();
			quoteBomTempDao.deleteByPkQuoteAndCreateBy(pkQuote,userId);
			Date doExcleDate = new Date();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
			List<QuoteBomTemp> quoteBomList = new ArrayList<>();
			String[] bsGroupsArray = new String[maxRow+1];
			//前一行为标题
			Integer successes = 0;
			Integer failures = 0;
			//列顺序:0主表id，1工作中心，2物料类型，3是否代采，4组件名称，5零件名称，6材料名称，7材料规格，8工艺说明，
			// 9用量，10制品重(g)，11重量单位，12水口重(g)，13穴数，14采购说明

			//列顺序:0主表id，1工作中心，2物料类型，3是否代采，4组件名称，5零件名称，6材料名称，7材料规格，8损耗分组，8工艺说明，
			// 9用量，10用量单位，11采购单位，12采购说明  20200220-hjj
			for (int row = 2; row <= maxRow; row++) {
				String errInfo = "";
				QuoteBomTemp temp = new QuoteBomTemp();
				String mid = tranCell(sheet.getRow(row).getCell(0)); //2021-04-09 去除id ，excel暂时不去除

				String wc = tranCell(sheet.getRow(row).getCell(1));
				if(!StringUtils.isNotEmpty(wc)){
					errInfo += "工作中心不能为空;";
				}else {
					List<BjWorkCenter> bjWorkCenterList = bjWorkCenterDao.findByWorkcenterNameAndDelFlag(wc, 0);
					if (bjWorkCenterList != null && bjWorkCenterList.size() > 0) {
						BjWorkCenter bjWorkCenter = bjWorkCenterList.get(0);
//						if(("out").equals(bjWorkCenter.getBsCode())){
//							errInfo += "外协不允许在清单中,请到“工艺流程”中选择外协。";
//						}
//						else {
							temp.setPkBjWorkCenter(bjWorkCenterList.get(0).getId());
//						}
					} else {
						errInfo += "没有维护:"+ wc +" 工作中心;";
					}
				}

				String itp = tranCell(sheet.getRow(row).getCell(2));
				if(!StringUtils.isNotEmpty(itp)){
					errInfo += "物料类型不能为空;";
				}else {
					List<ItemTypeWg> itemTypeWgList = itemTypeWgDao.findByItemTypeAndDelFlag(itp, 0);
					if (itemTypeWgList != null && itemTypeWgList.size() > 0) {
						temp.setPkItemTypeWg(itemTypeWgList.get(0).getId());
					} else {
						errInfo +=  "没有维护:"+ itp+" 物料类型;";
					}
				}

				String bsAgent = tranCell(sheet.getRow(row).getCell(3)); //是否代采
				if(StringUtils.isNotEmpty(bsAgent)) {
					if (("是").equals(bsAgent)) {
						temp.setBsAgent(1);
					}
				}

				String bsElement = tranCell(sheet.getRow(row).getCell(4));

				if(!StringUtils.isNotEmpty(bsElement)){
					errInfo += "组件名称不能为空;";
				}
				String bsComponent = tranCell(sheet.getRow(row).getCell(5));
				if(!StringUtils.isNotEmpty(bsComponent)){
					errInfo += "零件名称不能为空;";
				}

				String bsMaterName = tranCell(sheet.getRow(row).getCell(6));
//				if(!StringUtils.isNotEmpty(bsMaterName)){
//					errInfo += "材料名称不能为空;";
//				}
				String bsModel = tranCell(sheet.getRow(row).getCell(7));
//				if(!StringUtils.isNotEmpty(bsModel)){
//					errInfo += "材料规格不能为空;";
//				}
				String bsGroups = tranCell(sheet.getRow(row).getCell(8));
				temp.setBsGroups(bsGroups);
				bsGroupsArray[row]= bsGroups;
				String fmemo = tranCell(sheet.getRow(row).getCell(9));
				String bsQty = tranCell(sheet.getRow(row).getCell(10)); //hjj-20210119增加用量字段
//				String bsProQty = tranCell(sheet.getRow(row).getCell(10));
//				if(StringUtils.isNoneEmpty(bsQty)){
//					if(!bsQty.matches("^\\d+\\.\\d+$") && !bsQty.matches("^^\\d+$")){
//						errInfo += "用量需输入数字类型";
//					}
//				}else {
//					errInfo += "用量不能为空";
//				}
//				if(StringUtils.isNotEmpty(bsProQty)) {
//					if(!bsProQty.matches("^\\d+\\.\\d+$") && !bsProQty.matches("^^\\d+$")){
//						errInfo += "制品重(g)需输入数字类型";
//					}
//				}

				String unit = tranCell(sheet.getRow(row).getCell(11));
				if(StringUtils.isNotEmpty(unit)) {
//					List<Unit> unitList = unitDao.findByUnitNameAndDelFlag(unit, 0);
					List<Unit> unitList = unitDao.findByUnitCodeAndDelFlag(unit, 0);
					if (unitList != null && unitList.size() > 0) {
						temp.setPkUnit(unitList.get(0).getId());
					} else {
						errInfo += "没有维护:"+ unit +" 单位;";
					}
				}
//				else {
//					errInfo += "单位不能为空";
//				}
				temp.setBsQty(bsQty);
				String purchaseUnit = tranCell(sheet.getRow(row).getCell(12));
				if(StringUtils.isNotEmpty(purchaseUnit)){
					List<Unit> unitList = unitDao.findByUnitCodeAndDelFlag(purchaseUnit, 0);
					if(unitList.size()==0) {
						errInfo += "没有维护:" + purchaseUnit + " 采购单位;";
					}
				}
				temp.setPurchaseUnit(purchaseUnit);

//				String bsWaterGap = tranCell(sheet.getRow(row).getCell(12));
//				String bsCave = tranCell(sheet.getRow(row).getCell(13));
				String bsExplain = tranCell(sheet.getRow(row).getCell(13));//lst-20210107增加采购说明字段


//				temp.setBsWaterGap(bsWaterGap);
//				if(StringUtils.isNotEmpty(bsWaterGap)) {
//					if(!bsWaterGap.matches("^\\d+\\.\\d+$") && !bsWaterGap.matches("^^\\d+$")){
//						errInfo += "水口重需输入数字类型";
//					}
//				}
//				temp.setBsCave(bsCave);
//				if(StringUtils.isNotEmpty(bsCave)) {
//					if(!bsCave.matches("^\\d+\\.\\d+$") && !bsCave.matches("^^\\d+$")){
//						errInfo += "穴数需输入数字类型";
//					}
//				}
				temp.setErrorInfo(errInfo);
				if(("").equals(errInfo)){
					temp.setCheckStatus(0);
					successes ++;
				}else {
					temp.setCheckStatus(1);
					failures ++;
				}
//				if(StringUtils.isNotEmpty(mid)) {
//					temp.setMid(Long.parseLong(mid));
//					QuoteBom quoteBom = quoteBomDao.findById((Long.parseLong(mid)));
//					if(quoteBom!=null) {
//						if (quoteBom.getPkQuote() != pkQuote) {
////							Quote quote = quoteDao.findById((long) quoteBom.getPkQuote());
////							return ApiResponseResult.failure("导入失败！导入的信息中包含报价单编号为:"+quote.getBsCode()+"的外购件清单信息数据," +
////									"请在当前页面导出后再导入导出的文件");
//							return ApiResponseResult.failure("请在当前页面导出后再导入导出的文件  ->  导入的文件必须是从当前页面导出的。");
//
//						}
//					}
//				}
				temp.setPkQuote(pkQuote);
				temp.setBsElement(bsElement);
				temp.setBsComponent(bsComponent);
				temp.setBsMaterName(bsMaterName);
//				quoteBom.setBsItemCode(bsItemCode); 暂定不存，系统生成
				temp.setBsModel(bsModel);
				temp.setFmemo(fmemo);
//				temp.setBsProQty(bsProQty);

				temp.setBsElement(bsElement);
				temp.setBsExplain(bsExplain);
				temp.setCreateDate(doExcleDate);
				temp.setCreateBy(userId);
				quoteBomList.add(temp);
			}
			for(Integer k = 0;k<= bsGroupsArray.length-1;k++){
				List<Integer> list = new ArrayList<>();
				List<String> bsGroupsString = new ArrayList<>();
				for(Integer j = 0;j<=bsGroupsArray.length-1;j++){
					if(StringUtils.isNotEmpty(bsGroupsArray[k])){
						if(bsGroupsArray[k].equals(bsGroupsArray[j])){
							list.add(j);
							bsGroupsString.add(bsGroupsArray[k]);
						}
					}
				}
//		 	if(list.size()==1){
//				return ApiResponseResult.failure("损耗分组不能只存在一条!");
//			}
				for(int m = 0;m<list.size()-1;m++){
					if(list.get(m+1)-list.get(m)!=1){
						return ApiResponseResult.failure("相同的损耗分组("+bsGroupsString.get(m)+")必须相邻!");
					}
				}
			}
			quoteBomTempDao.saveAll(quoteBomList);
			Integer all = maxRow -1;
			return ApiResponseResult.success("导入成功! 导入总数:" +all+" :校验通过数:"+successes+" ;不通过数: "+failures);
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}

	@Override
	public ApiResponseResult importByTemp(Long pkQuote) throws Exception {
		Long userId = UserUtil.getSessionUser().getId();
		Date doExcelDate = new Date();
		//hjj 2021/04/02 临时表导入正式表时 删除原数据
		//hjj 2021/04/09 删除复制的制造部材料,删除工艺流程
		quoteBomDao.deleteAllByPkQuote(pkQuote);
		productMaterDao.deleteByPkQuote(pkQuote);
		quoteProcessDao.delteQuoteProcessByPkQuote(pkQuote);

		List<QuoteBomTemp> tempList = quoteBomTempDao.findByCheckStatusAndDelFlagAndCreateByAndPkQuoteOrderById(0,0,userId,pkQuote);
		List<QuoteBom> quoteBomList =  new ArrayList<>();
		for(QuoteBomTemp temp:tempList){
			QuoteBom quoteBom = new QuoteBom();
//			if(temp.getMid()!=null){
//				quoteBom = quoteBomDao.findById((long) temp.getMid());
//			}
			quoteBom.setBsComponent(temp.getBsComponent());
			quoteBom.setBsMaterName(temp.getBsMaterName());
			quoteBom.setBsElement(temp.getBsElement());
			quoteBom.setBsModel(temp.getBsModel());
//			if(StringUtils.isNotEmpty(temp.getBsProQty())){
//				quoteBom.setBsProQty(new BigDecimal(temp.getBsProQty()));
//			}
			if(StringUtils.isNotEmpty(temp.getBsQty())){
				quoteBom.setBsQty(new BigDecimal(temp.getBsQty()));
			}
			quoteBom.setPurchaseUnit(temp.getPurchaseUnit());
//			if(StringUtils.isNotEmpty(temp.getBsCave())){
//				quoteBom.setBsCave(temp.getBsCave());
//			}
//			if(StringUtils.isNotEmpty(temp.getBsWaterGap())){
//				quoteBom.setBsWaterGap(temp.getBsWaterGap());
//			}
			quoteBom.setPkUnit(temp.getPkUnit());
			quoteBom.setPkQuote(temp.getPkQuote());
			quoteBom.setPkItemTypeWg(temp.getPkItemTypeWg());
			quoteBom.setPkBjWorkCenter(temp.getPkBjWorkCenter());
			quoteBom.setBsGroups(temp.getBsGroups());
			quoteBom.setFmemo(temp.getFmemo());
			quoteBom.setBsAgent(temp.getBsAgent());
			quoteBom.setBsRadix(temp.getBsRadix());
			quoteBom.setBsExplain(temp.getBsExplain());
			quoteBom.setCreateBy(userId);
			quoteBom.setCreateDate(doExcelDate);
			quoteBomList.add(quoteBom);
		}
		quoteBomDao.saveAll(quoteBomList);
		for(QuoteBom o :quoteBomList){
			o.setPkBomId(o.getId());
		}
//		quoteBomDao.saveAll(quoteBomList);
		quoteBomTempDao.deleteByPkQuoteAndCreateBy(pkQuote,userId);
		return ApiResponseResult.success().message("确认导入成功!共导入:"+quoteBomList.size()+"条");
	}

	@Override
	public ApiResponseResult deleteTemp(String ids) throws Exception {
		if(StringUtils.isNoneEmpty(ids)){
			long[] idsArray = (long[]) ConvertUtils.convert(ids.split(","),long.class);
			quoteBomTempDao.deleteByIdIn(idsArray);
			return ApiResponseResult.success().message("删除成功");
		}else {
			return ApiResponseResult.failure().message("请选中需要删除的数据");
		}

	}
}
