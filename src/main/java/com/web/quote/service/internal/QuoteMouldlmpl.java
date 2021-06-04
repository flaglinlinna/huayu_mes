package com.web.quote.service.internal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.system.user.dao.SysUserDao;
import com.utils.ExcelExport;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteBom;
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

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.service.TodoInfoService;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.MjProcFeeDao;
import com.web.basePrice.entity.MjProcFee;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.dao.QuoteMouldDao;
import com.web.quote.entity.QuoteMould;
import com.web.quote.service.QuoteMouldService;
import com.web.quote.service.QuoteService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 模具清单维护
 *
 */
@Service(value = "QuoteMouldService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteMouldlmpl implements QuoteMouldService{

	@Autowired
	QuoteMouldDao quoteMouldDao;

	@Autowired
	QuoteBomDao quoteBomDao;
	
	@Autowired
	MjProcFeeDao mjProcFeeDao;


	
	@Autowired
	QuoteItemDao quoteItemDao;

	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	QuoteService quoteService;
	@Autowired
	TodoInfoService todoInfoService;
	@Autowired
	private QuoteDao quoteDao;
	
	/**
	 * 获取Bom清单的组件下拉列表
	 * **/
	public ApiResponseResult getBomList(String quoteId)throws Exception{
		//修改获取零件下拉列表
		List<Map<String, Object>> list=quoteMouldDao.getComponentName(quoteId);
		return ApiResponseResult.success().data(list);
	}
	
	/**
	 * 获取模具成本清单
	 * **/
	@Override
	public ApiResponseResult getMouldList() throws Exception {
		// TODO Auto-generated method stub
		List<MjProcFee> list = mjProcFeeDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}
	
	/**
	 * 模具清单维护表
	 **/
	@Override
	public ApiResponseResult getList(String keyword,String pkQuote, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mjProcFee.productName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mjProcFee.productCode", SearchFilter.Operator.LIKE, keyword));
		}
		if (!"null".equals(pkQuote)&&pkQuote!=null) {
			filters.add(new SearchFilter("pkQuote", SearchFilter.Operator.EQ, pkQuote));
		}else {
			List<QuoteMould> quoteMouldList = new ArrayList<>();
			return ApiResponseResult.success().data(DataGrid.create(quoteMouldList, (int) quoteMouldList.size(),
					pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		}
		Specification<QuoteMould> spec = Specification.where(BaseService.and(filters, QuoteMould.class));
		Specification<QuoteMould> spec1 = spec.and(BaseService.or(filters1, QuoteMould.class));
		Page<QuoteMould> page = quoteMouldDao.findAll(spec1, pageRequest);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<QuoteMould> quoteMouldList = page.getContent();
		List<JSONObject> list = new ArrayList<>();
		for(QuoteMould quoteMould :quoteMouldList){
			JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(quoteMould));
			jsonObject.put("createName",sysUserDao.findById((long)quoteMould.getCreateBy()).getUserName());
			jsonObject.put("createDate",df.format(quoteMould.getCreateDate()));
			if(quoteMould.getLastupdateBy()!=null){
				jsonObject.put("lastupdateName",sysUserDao.findById((long)quoteMould.getCreateBy()).getUserName());
				jsonObject.put("lastupdateDate",df.format(quoteMould.getLastupdateDate()));
			}
			list.add(jsonObject);
		}

		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));

	}
	
	/**
	 * 增加模具清单记录
	 * mould:被选中的模具信息id串
	 * itemId:被选择的组件信息
	 * quoteId:报价单主表ID
	 **/
	@Override
	public ApiResponseResult add(String mould, String itemId,String quoteId) throws Exception {
		// TODO Auto-generated method stub
		String[] mouldIds = mould.split("\\,");
		List<QuoteMould> lp = new ArrayList<QuoteMould>();
		// 20201218-先删除后在新增
		quoteMouldDao.delteQuoteMouldByBsNameAndPkQuote(itemId,Long.valueOf(quoteId));//使用组件名字做标识
		int j = 1;
		for (String m : mouldIds) {
			if (!StringUtils.isEmpty(m)) {
				// Proc procItem = procDao.findById(Long.parseLong(pro));
				QuoteMould pd = new QuoteMould();
				//pd.setPkQuoteBom(Long.valueOf(itemId));//bomID
				pd.setBsName(itemId);//bom组件名称
				pd.setPkProcFee(Long.valueOf(m));//模具信息-模具成本id
				pd.setCreateBy(UserUtil.getSessionUser().getId());
				pd.setCreateDate(new Date());
				pd.setPkQuote(Long.valueOf(quoteId));//报价单主表
				lp.add(pd);
				j++;
			}
		}
		quoteMouldDao.saveAll(lp);
		return ApiResponseResult.success("新增成功!");
	}
	
	/**
	 * 编辑实际报价
	 * **/
	@Override
	public ApiResponseResult doActQuote(Long id, BigDecimal bsActQuote) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
            return ApiResponseResult.failure("模具成本信息ID不能为空！");
        }
        if(bsActQuote == null){
            return ApiResponseResult.failure("请填写正确的数字！");
        }
        QuoteMould o = quoteMouldDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("模具成本信息记录不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsActQuote(bsActQuote);
        quoteMouldDao.save(o);
        return ApiResponseResult.success("修改成功！").data(o);
	}
	
	/**
	 * 删除
	 * **/
	@Override
	public ApiResponseResult delete(Long id) throws Exception {
		// TODO Auto-generated method stub
		 if(id == null){
	            return ApiResponseResult.failure("ID不能为空！");
	        }
		 QuoteMould o  = quoteMouldDao.findById((long) id);
	        if(o == null){
	            return ApiResponseResult.failure("该记录不存在！");
	        }
	        o.setDelTime(new Date());
	        o.setDelFlag(1);
	        o.setDelBy(UserUtil.getSessionUser().getId());
	        quoteMouldDao.save(o);
	        return ApiResponseResult.success("删除成功！");
	}
	
	/**
	 * 提交模具维护清单
	 * **/
	 public ApiResponseResult doStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交-lst-20210112
		 int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
		 if(i>0){
			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
		 }
		 int count =quoteMouldDao.countByDelFlagAndPkQuoteAndBsActQuote(0,Long.parseLong(quoteId),null);
		 if(count>0){
			 return ApiResponseResult.failure("提交失败：实际报价不可为空，请检查数据");
		 }
		 quoteMouldDao.saveQuoteMouldByQuoteId(1,Long.parseLong(quoteId));
		 //项目状态设置-状态 2：已完成
		 quoteItemDao.switchStatus(2, Long.parseLong(quoteId), code);
		 Object data = quoteService.doItemFinish(code, Long.parseLong(quoteId)).getData();
		 
		 //20210112-fyx-关闭待办
		 todoInfoService.closeByIdAndModel(Long.parseLong(quoteId), "模具清单");
		 return ApiResponseResult.success("提交成功！").data(data);
	 }
	 
	 /**
	  * 不需要报价状态设置
	  * **/
	 public ApiResponseResult doNoNeed(String quoteId,String bsCode)throws Exception{
		 if(quoteId==null||quoteId==""){
			 return ApiResponseResult.failure("报价单不可为空");
		 }
		 if(bsCode==null||bsCode==""){
			 return ApiResponseResult.failure("报价单项目编码不可为空");
		 }
		 //状态 3：不需要填写
		 quoteItemDao.switchStatus(3, Long.parseLong(quoteId), bsCode);
		 //写入完成时间-20210112-lst
		 quoteService.doItemFinish(bsCode, Long.parseLong(quoteId));
		 //20210113-lst-关闭待办
		 todoInfoService.closeByIdAndModel(Long.parseLong(quoteId), "模具清单");
		 return ApiResponseResult.success("操作成功！");
	 }


	/**
	 * 取消完成 模具维护清单
	 * **/
	public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception{
		//判断状态是否已执行过确认提交-lst-20210112
//		int i=quoteItemDao.countByDelFlagAndPkQuoteAndBsCodeAndBsStatus(0,Long.parseLong(quoteId),code, 2);
//		if(i>0){
//			return ApiResponseResult.failure("此项目已完成，请不要重复确认提交。");
//		}
//		int count =quoteMouldDao.countByDelFlagAndPkQuoteAndBsActQuote(0,Long.parseLong(quoteId),null);
//		if(count>0){
//			return ApiResponseResult.failure("提交失败：实际报价不可为空，请检查数据");
//		}
		Quote quote = quoteDao.findById(Long.parseLong(quoteId));
		if(quote.getBsStatus()==1||quote.getBsStatus()==4){
			return ApiResponseResult.failure("报价单已提交审批，不能取消完成。");
		}
		quoteMouldDao.saveQuoteMouldByQuoteId(0,Long.parseLong(quoteId));
		//项目状态设置-状态 2：已完成,1 未完成
		quoteItemDao.switchStatus(1, Long.parseLong(quoteId), code);
		quoteService.doItemFinish(code, Long.parseLong(quoteId));

		todoInfoService.openByIdAndModel(Long.parseLong(quoteId), "模具清单");
		return ApiResponseResult.success("提交成功！");
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
			List<QuoteMould> quoteMouldList = new ArrayList<>();
			//前两行为标题
			for (int row = 2; row <= maxRow; row++) {
				QuoteMould o = new QuoteMould();
				String bsName = tranCell(sheet.getRow(row).getCell(0));
				String productCode = tranCell(sheet.getRow(row).getCell(1));
				String bsActQuote = tranCell(sheet.getRow(row).getCell(2));
				if(bsName!=null) {
					if(quoteBomDao.findByDelFlagAndPkQuoteAndBsComponent(0, pkQuote, bsName).size()==0){
						return ApiResponseResult.failure("外购件中没有 "+bsName +"的零件");
					}
					o.setBsName(bsName);
				}else {
					return ApiResponseResult.failure("零件名称不能为空");
				}

				if(productCode!=null) {
					List<MjProcFee> mjProcFeeList =mjProcFeeDao.findByDelFlagAndProductCode(0, productCode);
					if(mjProcFeeList.size()==0){
						return ApiResponseResult.failure("模具成本信息中没有 "+productCode +"的模具编码信息");
					}
					o.setPkProcFee(mjProcFeeList.get(0).getId());
//					o.getBsMoFee()
				}else {
					return ApiResponseResult.failure("模具编码不能为空");
				}

				o.setBsActQuote(new BigDecimal(bsActQuote));
				o.setPkQuote(pkQuote);
				o.setCreateBy(userId);
				o.setCreateDate(doExcleDate);
//				String productName = tranCell(sheet.getRow(row).getCell(3));
//				String bsMoFee = tranCell(sheet.getRow(row).getCell(4));
//				String stQuote = tranCell(sheet.getRow(row).getCell(5));
//				String createBy = tranCell(sheet.getRow(row).getCell(6));
//				String createDate = tranCell(sheet.getRow(row).getCell(7));
				quoteMouldList.add(o);

			}
			quoteMouldDao.deletBypkQuote(pkQuote);
			quoteMouldDao.saveAll(quoteMouldList);
			return ApiResponseResult.success("导入成功");
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}


	@Override
	public void exportExcel(HttpServletResponse response, Long pkQuote) throws Exception {
//		long startTime=System.currentTimeMillis();   //获取开始时间
		List<QuoteMould> quoteMouldList = quoteMouldDao.findByDelFlagAndPkQuote(0,pkQuote);
//		List<QuoteBom> quoteBomList = quoteBomDao.findByDelFlag(0);
		String excelPath = "static/excelFile/";
		String fileName = "报价模具清单.xlsx";
		String[] map_arr = new String[]{"bsName","productCode","bsActQuote","productName","bsMoFee","stQuote","createBy",
				"createDate","lastupdateBy","lastupdateDate"};
		XSSFWorkbook workbook = new XSSFWorkbook();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(QuoteMould o :quoteMouldList){
			Map<String, Object> map = new HashMap<>();
			map.put("bsName", o.getBsName());
			map.put("productCode",o.getMjProcFee().getProductCode());
			map.put("bsActQuote",o.getBsActQuote());
			map.put("productName", o.getMjProcFee().getProductName());
			map.put("bsMoFee",o.getMjProcFee().getFeeAll());
			map.put("stQuote",o.getMjProcFee().getStQuote());
			map.put("createBy", sysUserDao.findById((long) o.getCreateBy()).getUserName());
			map.put("createDate", df.format(o.getCreateDate()));
			if (o.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) o.getCreateBy()).getUserName());
				map.put("lastupdateDate", df.format(o.getLastupdateDate()));
			}
//			map.put("bsExplain",quoteBom.getBsExplain());//lst-20210107-增加采购说明字段
			list.add(map);
		}
		ExcelExport.export(response,list,workbook,map_arr,excelPath+fileName,fileName);
//		EasyExcelUtils.download(excelPath+fileName,map_arr,map_arr,list);
//		long endTime=System.currentTimeMillis(); //获取结束时间
//		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
	}
}
