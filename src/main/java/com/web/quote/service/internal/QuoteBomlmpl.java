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
import com.web.quote.entity.QuoteBom;
import com.web.quote.service.QuoteBomService;
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
	private BjWorkCenterDao bjWorkCenterDao;

	@Override
	public ApiResponseResult add(QuoteBom quoteBom) throws Exception {
		if(quoteBom == null){
			return ApiResponseResult.failure("外购件清单信息不能为空！");
		}
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
		o.setBsComponent(quoteBom.getBsComponent());
		o.setBsElement(quoteBom.getBsElement());
		o.setBsModel(quoteBom.getBsModel());
		o.setPkUnit(quoteBom.getPkUnit());
		o.setPkItemTypeWg(quoteBom.getPkItemTypeWg());
		o.setPkBjWorkCenter(quoteBom.getPkBjWorkCenter());
		o.setBsProQty(quoteBom.getBsProQty());
		o.setBsMaterName(quoteBom.getBsMaterName());
		o.setBsModel(quoteBom.getBsModel());
//		o.setBsQty(quoteBom.getBsQty());
		o.setBsRadix(quoteBom.getBsRadix());
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
				quoteBom.setPkQuote(pkQuote);
				quoteBom.setBsElement(bsElement);
				quoteBom.setBsComponent(bsComponent);
				quoteBom.setBsMaterName(bsMaterName);
//				quoteBom.setBsItemCode(bsItemCode); 暂定不存，系统生成
				quoteBom.setBsModel(bsModel);
				quoteBom.setFmemo(fmemo);
				if(StringUtils.isNotEmpty(bsProQty)) {
					quoteBom.setBsProQty(new BigDecimal(bsProQty));
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
