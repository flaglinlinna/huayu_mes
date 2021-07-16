package com.web.basePrice.service.internal;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.system.user.dao.SysUserDao;

import com.utils.ExcelExport;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.Proc;
import com.web.produce.service.internal.PrcUtils;
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
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.PriceCommDao;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.PriceComm;
import com.web.basePrice.entity.Unit;
import com.web.basePrice.service.PriceCommService;
import com.web.basic.entity.Mtrial;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @date Dec 7, 2020 5:11:13 PM
 */
@Service(value = "PriceCommService")
@Transactional(propagation = Propagation.REQUIRED)
public class PriceCommImpl extends PrcUtils implements PriceCommService {
	@Autowired
	private PriceCommDao priceCommDao;

	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private UnitDao unitDao;

	/**
	 * 新增物料通用价格信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(PriceComm priceComm) throws Exception {
		if (priceComm == null) {
			return ApiResponseResult.failure("物料通用价格信息不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getItemName())) {
			return ApiResponseResult.failure("物料名称不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getPriceUn())) {
			return ApiResponseResult.failure("单价不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getUnitId())) {
			return ApiResponseResult.failure("单位不能为空！");
		}
		if(priceCommDao.countByDelFlagAndItemNo(0,priceComm.getItemNo())>0){
			return ApiResponseResult.failure("该物料编码已经存在！");
		}
		//20210120-fyx-物料名称去掉头尾空格
		priceComm.setItemName(priceComm.getItemName().trim());
		//--end
		priceComm.setCreateDate(new Date());
		priceComm.setCreateBy(UserUtil.getSessionUser().getId());
		priceCommDao.save(priceComm);
		return ApiResponseResult.success("物料通用价格添加成功！").data(priceComm);
	}

	/**
	 * 修改物料通用价格
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(PriceComm priceComm) throws Exception {
		if (priceComm == null) {
			return ApiResponseResult.failure("物料通用价格不能为空！");
		}
		if (priceComm.getId() == null) {
			return ApiResponseResult.failure("物料通用价格ID不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getItemName())) {
			return ApiResponseResult.failure("物料名称不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getPriceUn())) {
			return ApiResponseResult.failure("单价不能为空！");
		}
		if (StringUtils.isEmpty(priceComm.getUnitId())) {
			return ApiResponseResult.failure("单位不能为空！");
		}
		PriceComm o = priceCommDao.findById((long) priceComm.getId());
		if (o == null) {
			return ApiResponseResult.failure("该物料通用价格不存在！");
		}
		if(!priceComm.getItemNo().equals(o.getItemNo())){
			if(priceCommDao.countByDelFlagAndItemNo(0,priceComm.getItemNo())>0){
				return ApiResponseResult.failure("该物料编码已经存在！");
			}
		}
		o.setItemNo(priceComm.getItemNo());
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		//o.setEnabled(priceComm.getEnabled());
		o.setItemName(priceComm.getItemName().trim());
	    o.setRangePrice(priceComm.getRangePrice());
	    o.setPriceUn(priceComm.getPriceUn());
	    o.setUnitId(priceComm.getUnitId());
	    o.setAlternativeSuppliers(priceComm.getAlternativeSuppliers());
		priceCommDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除物料通用价格
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("物料通用价格ID不能为空！");
		}
		PriceComm o = priceCommDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("物料通用价格不存在！");
		}
		o.setDelFlag(1);
		priceCommDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("物料通用价格ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		PriceComm o = priceCommDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("此物料通用价格信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		priceCommDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询物料通用价格信息维护列表
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
			filters1.add(new SearchFilter("itemNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("itemName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("rangePrice", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("alternativeSuppliers", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<PriceComm> spec = Specification.where(BaseService.and(filters, PriceComm.class));
		Specification<PriceComm> spec1 = spec.and(BaseService.or(filters1, PriceComm.class));
		Page<PriceComm> page = priceCommDao.findAll(spec1, pageRequest);
		List<PriceComm> priceCommList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (PriceComm priceComm : priceCommList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", priceComm.getId());
			map.put("itemName", priceComm.getItemName());
			map.put("itemNo",priceComm.getItemNo());
			map.put("rangePrice", priceComm.getRangePrice());
			map.put("priceUn", priceComm.getPriceUn());
			if(priceComm.getUnit()!=null){
				map.put("unit", priceComm.getUnit().getUnitName());
				map.put("unitId", priceComm.getUnit().getId());
			}
			
			map.put("alternativeSuppliers", priceComm.getAlternativeSuppliers());
			map.put("enabled", priceComm.getEnabled());
			map.put("createBy", sysUserDao.findById((long) priceComm.getCreateBy()).getUserName());
			map.put("createDate", df.format(priceComm.getCreateDate()));
			if (priceComm.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) priceComm.getLastupdateBy()).getUserName());
				map.put("lastupdateDate", df.format(priceComm.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	/**
	 * 获取单位数据【下拉框】
	 * */
	 public ApiResponseResult getUnitList()throws Exception{
		 List<Unit> list=unitDao.findByDelFlag(0);
		 return ApiResponseResult.success().data(list);
		 
	 }

	@Override
	public ApiResponseResult getItemList(String keyword,PageRequest pageRequest) throws Exception{
		List<Object> list = getReworkItemPrc(UserUtil.getSessionUser().getCompany()+"",
				UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","原材料",keyword,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	//防止读取Excel为null转String 报空指针异常
	public String tranCell(Object object)
	{
		if(object==null||object==""||("").equals(object)){
			return null;
		}else return object.toString().trim();
	}

	@Override
	public ApiResponseResult doExcel(MultipartFile[] file) throws Exception {
		try {
			Date doExcleDate = new Date();
			Long userId = UserUtil.getSessionUser().getId();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
//			Integer successes = 0;
			Integer failures = 0;
			List<PriceComm> priceCommList = new ArrayList<>();
			for (int row = 2; row <= maxRow; row++) {
//				String errInfo = "";
				String id = tranCell(sheet.getRow(row).getCell(0)); //id
				String itemNo = tranCell(sheet.getRow(row).getCell(1));
				String itemName = tranCell(sheet.getRow(row).getCell(2));
				String rangePrice = tranCell(sheet.getRow(row).getCell(3));
				String priceUn = tranCell(sheet.getRow(row).getCell(4));
				String unitCode = tranCell(sheet.getRow(row).getCell(5));
				String suppliers = tranCell(sheet.getRow(row).getCell(6));
				PriceComm priceComm = new PriceComm();
				if(StringUtils.isNotEmpty(id)){
					priceComm = priceCommDao.findById(Long.parseLong(id));
					if(priceComm!=null){
						if(!(priceComm.getItemNo()+"").equals(itemNo)){
							if(priceCommDao.countByDelFlagAndItemNo(0,itemNo)>0){
//								return ApiResponseResult.failure("该物料编码已经存在！");
								failures++;
								continue;
							}
							List<Map<String,Object>> mapList  = priceCommDao.findItemByItemNo(itemNo);
							if(mapList.size()>0){
								Map map = mapList.get(0);
								priceComm.setItemNo(map.get("ITEM_NO").toString());
								priceComm.setItemName(map.get("ITEM_NAME").toString());
								priceComm.setLastupdateBy(userId);
								priceComm.setLastupdateDate(doExcleDate);
							}else {
								failures++;
								continue;
							}
						}
					}
				}
				else {
					priceComm.setCreateBy(userId);
					priceComm.setCreateDate(doExcleDate);
				}

				List<Unit> unitList = unitDao.findByUnitCodeAndDelFlag(unitCode,0);
				if(unitList.size()>0){
					priceComm.setUnitId(unitList.get(0).getId().toString());
				}else {
					failures++;
					continue;
				}
				priceComm.setPriceUn(priceUn);
				priceComm.setRangePrice(rangePrice);
				priceComm.setAlternativeSuppliers(suppliers);
				priceCommList.add(priceComm);
			}
			priceCommDao.saveAll(priceCommList);
			return ApiResponseResult.success("导入成功!,共导入:"+priceCommList.size()+";不通过:"+failures);
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}

	/**
	 * 查询工序基础信息维护列表
	 */
	@Override
	@Transactional
	public void exportExcel(HttpServletResponse response, String keyword) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("procNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<PriceComm> spec = Specification.where(BaseService.and(filters, PriceComm.class));
		Specification<PriceComm> spec1 = spec.and(BaseService.or(filters1, PriceComm.class));
		List<PriceComm> procList = priceCommDao.findAll(spec1);

		String excelPath = "static/excelFile/";
		String fileName = "物料通用价格维护模板.xlsx";
		String[] map_arr = new String[]{"id","itemNo","itemName","rangePrice","priceUn","unitCode","suppliers"};
		XSSFWorkbook workbook = new XSSFWorkbook();
//		List<Proc> procList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (PriceComm priceComm : procList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", priceComm.getId());
			map.put("itemNo", priceComm.getItemNo());
			map.put("priceUn", priceComm.getPriceUn());
			map.put("itemName", priceComm.getItemName());
			if(priceComm.getUnit()!=null){
//				map.put("workCenterId", proc.getBjWorkCenter().getId());
				map.put("unitCode", priceComm.getUnit().getUnitCode());
			}
			map.put("rangePrice",priceComm.getRangePrice());
			map.put("suppliers",priceComm.getAlternativeSuppliers());
			mapList.add(map);
		}
		ExcelExport.export(response,mapList,workbook,map_arr,excelPath+fileName,fileName);

	}

}