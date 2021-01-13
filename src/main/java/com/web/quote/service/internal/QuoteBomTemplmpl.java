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
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.dao.QuoteBomTempDao;
import com.web.quote.entity.QuoteBom;
import com.web.quote.entity.QuoteBomTemp;
import com.web.quote.service.QuoteBomTempService;
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

@Service(value = "QuoteBomTempService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteBomTemplmpl implements QuoteBomTempService {

	@Autowired
	private QuoteBomTempDao quoteBomTempDao;

	@Autowired
	private QuoteBomDao quoteBomDao;

	@Autowired
	private ItemTypeWgDao itemTypeWgDao;
	@Autowired
	private UnitDao unitDao;
	@Autowired
	private BjWorkCenterDao bjWorkCenterDao;
	

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
	public String tranCell(Object object)
	{
		if(object==null||object==""||("").equals(object)){
			return null;
		}else return object.toString();
	}

	//导入模板
	@Override
	public ApiResponseResult doExcel(MultipartFile[] file, Long pkQuote) throws Exception{
		try {
//			if(pkQuote ==null){
//				return ApiResponseResult.failure("导入失败！请检查选中的报价单！");
//			}
			Long userId = UserUtil.getSessionUser().getId();
			quoteBomTempDao.deleteByPkQuoteAndCreateBy(pkQuote,userId);
			Date doExcleDate = new Date();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
			List<QuoteBomTemp> quoteBomList = new ArrayList<>();
			//前两行为标题
			Integer successes = 0;
			Integer failures = 0;

			for (int row = 2; row <= maxRow; row++) {
				String errInfo = "";
				QuoteBomTemp temp = new QuoteBomTemp();
				String mid = tranCell(sheet.getRow(row).getCell(0));
				String bsElement = tranCell(sheet.getRow(row).getCell(1));
				if(!StringUtils.isNotEmpty(bsElement)){
					errInfo += "组件名称不能为空;";
				}
				String bsComponent = tranCell(sheet.getRow(row).getCell(2));
				if(!StringUtils.isNotEmpty(bsComponent)){
					errInfo += "零件名称不能为空;";
				}
				String wc = tranCell(sheet.getRow(row).getCell(3));
				if(!StringUtils.isNotEmpty(wc)){
					errInfo += "工作中心不能为空;";
				}else {
					List<BjWorkCenter> bjWorkCenterList = bjWorkCenterDao.findByWorkcenterNameAndDelFlag(wc, 0);
					if (bjWorkCenterList != null && bjWorkCenterList.size() > 0) {
						temp.setPkBjWorkCenter(bjWorkCenterList.get(0).getId());
					} else {
						errInfo += "没有维护:"+ wc +" 工作中心;";
					}
				}


//				String bsItemCode = tranCell(sheet.getRow(row).getCell(3));
				String itp = tranCell(sheet.getRow(row).getCell(4));
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
				String bsMaterName = tranCell(sheet.getRow(row).getCell(5));
				if(!StringUtils.isNotEmpty(bsMaterName)){
					errInfo += "材料名称不能为空;";
				}
				String bsModel = tranCell(sheet.getRow(row).getCell(6));
				if(!StringUtils.isNotEmpty(bsModel)){
					errInfo += "材料规格不能为空;";
				}
				String fmemo = tranCell(sheet.getRow(row).getCell(7));
				String bsProQty = tranCell(sheet.getRow(row).getCell(8));
				String unit = tranCell(sheet.getRow(row).getCell(9));
				List<Unit> unitList =unitDao.findByUnitNameAndDelFlag(unit,0);
				if(unitList!=null&& unitList.size()>0){
					temp.setPkUnit(unitList.get(0).getId());
				}
				String bsRadix = tranCell(sheet.getRow(row).getCell(10));
				String bsExplain = tranCell(sheet.getRow(row).getCell(11));//lst-20210107增加采购说明字段
				if(StringUtils.isNotEmpty(bsProQty)) {
					if(!bsProQty.matches("^\\d+\\.\\d+$") && !bsProQty.matches("^^\\d+$")){
						errInfo += "制品重需输入数字类型";
					}
				}
				if(StringUtils.isNotEmpty(bsRadix)) {
					if (!bsRadix.matches("^\\d+\\.\\d+$")
							&& !bsRadix.matches("^^\\d+$")){
						errInfo = errInfo + "基数需输入数字;";
					}
				}

				temp.setErrorInfo(errInfo);
				if(("").equals(errInfo)){
					temp.setCheckStatus(0);
					successes ++;
				}else {
					temp.setCheckStatus(1);
					failures ++;
				}
				if(StringUtils.isNotEmpty(mid)) {
					temp.setMid(Long.parseLong(mid));
				}
				temp.setPkQuote(pkQuote);
				temp.setBsElement(bsElement);
				temp.setBsComponent(bsComponent);
				temp.setBsMaterName(bsMaterName);
//				quoteBom.setBsItemCode(bsItemCode); 暂定不存，系统生成
				temp.setBsModel(bsModel);
				temp.setFmemo(fmemo);
				temp.setBsProQty(bsProQty);

				temp.setBsElement(bsElement);
				temp.setBsRadix(bsRadix);
				temp.setBsExplain(bsExplain);
				temp.setCreateDate(doExcleDate);
				temp.setCreateBy(userId);
				quoteBomList.add(temp);
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
		Date doExcleDate = new Date();
		List<QuoteBomTemp> tempList = quoteBomTempDao.findByCheckStatusAndDelFlagAndCreateByAndPkQuote(0,0,userId,pkQuote);
		List<QuoteBom> quoteBomList =  new ArrayList<>();
		for(QuoteBomTemp temp:tempList){
			QuoteBom quoteBom = new QuoteBom();
			if(temp.getMid()!=null){
				quoteBom = quoteBomDao.findById((long) temp.getMid());
			}
			quoteBom.setBsComponent(temp.getBsComponent());
			quoteBom.setBsMaterName(temp.getBsMaterName());
			quoteBom.setBsElement(temp.getBsElement());
			quoteBom.setBsModel(temp.getBsModel());
			if(StringUtils.isNotEmpty(temp.getBsProQty())){
				quoteBom.setBsProQty(new BigDecimal(temp.getBsProQty()));
			}
			quoteBom.setPkUnit(temp.getPkUnit());
			quoteBom.setPkQuote(temp.getPkQuote());
			quoteBom.setPkItemTypeWg(temp.getPkItemTypeWg());
			quoteBom.setPkBjWorkCenter(temp.getPkBjWorkCenter());
			quoteBom.setFmemo(temp.getFmemo());
			quoteBom.setBsRadix(temp.getBsRadix());
			quoteBom.setBsExplain(temp.getBsExplain());
			quoteBomList.add(quoteBom);
		}
		quoteBomDao.saveAll(quoteBomList);
		quoteBomTempDao.deleteByPkQuoteAndCreateBy(pkQuote,userId);
		return ApiResponseResult.success();
	}
}
