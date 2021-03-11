package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.ExcelExport;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;

import com.web.basePrice.dao.BjModelTypeDao;
import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.entity.BjModelType;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.PriceComm;
import com.web.basePrice.service.BjModelTypeService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@Service(value = "BjModelTypeService")
@Transactional(propagation = Propagation.REQUIRED)
public class BjModelTypeImpl extends BasePriceUtils implements BjModelTypeService {
	@Autowired
	private BjModelTypeDao bjModelTypeDao;

	@Autowired
	private BjWorkCenterDao bjWorkCenterDao;

	@Autowired
	private SysUserDao sysUserDao;
		
	/**
	 * 新增人工制费信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(BjModelType bjModelType) throws Exception {
		if (bjModelType == null) {
			return ApiResponseResult.failure("机台类型不能为空！");
		}
		if (StringUtils.isEmpty(bjModelType.getPkWorkcenter().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}
		if(bjModelTypeDao.findByDelFlagAndModelCode(0,bjModelType.getModelCode()).size()>0){
			return ApiResponseResult.failure("该机台编码已存在,请重新输入！");
		}
		if(bjModelTypeDao.findByDelFlagAndModelName(0,bjModelType.getModelName()).size()>0){
			return ApiResponseResult.failure("该机台名称已存在,请重新输入！");
		}
		bjModelType.setCreateDate(new Date());
		bjModelType.setCreateBy(UserUtil.getSessionUser().getId());
		bjModelTypeDao.save(bjModelType);
		return ApiResponseResult.success("人工制费信息添加成功！").data(bjModelType);
	}

	/**
	 * 修改人工制费
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(BjModelType bjModelType) throws Exception {
		if (bjModelType == null) {
			return ApiResponseResult.failure("机台类型信息不能为空！");
		}
		if (StringUtils.isEmpty(bjModelType.getPkWorkcenter().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}

		BjModelType o = bjModelTypeDao.findById((long) bjModelType.getId());
		if (o == null) {
			return ApiResponseResult.failure("该机台类型不存在！");
		}
		if (!o.getModelCode().equals(bjModelType.getModelCode())){
			if(bjModelTypeDao.findByDelFlagAndModelCode(0,bjModelType.getModelCode()).size()>0){
				return ApiResponseResult.failure("该机台编码已存在,请重新输入！");
			}
		}
		if (!o.getModelName().equals(bjModelType.getModelName())){
			if(bjModelTypeDao.findByDelFlagAndModelName(0,bjModelType.getModelName()).size()>0){
				return ApiResponseResult.failure("该机台名称已存在,请重新输入！");
			}
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setPkWorkcenter(bjModelType.getPkWorkcenter());
	    o.setModelCode(bjModelType.getModelCode());
	    o.setModelName(bjModelType.getModelName());
		bjModelTypeDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除人工制费
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("人工制费ID不能为空！");
		}
		BjModelType o = bjModelTypeDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("人工制费不存在！");
		}
		o.setDelFlag(1);
		bjModelTypeDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}



	/**
	 * 查询人工制费信息维护列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword,String bsType,String workCenterId ,PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		if(StringUtils.isNotEmpty(bsType)){
			filters.add(new SearchFilter("workCenter.bsCode", SearchFilter.Operator.EQ, bsType));
		}
		if(StringUtils.isNotEmpty(workCenterId)){
			filters.add(new SearchFilter("pkWorkcenter", SearchFilter.Operator.EQ, workCenterId));
		}
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("modelCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("modelName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("workCenter.workcenterCode", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<BjModelType> spec = Specification.where(BaseService.and(filters, BjModelType.class));
		Specification<BjModelType> spec1 = spec.and(BaseService.or(filters1, BjModelType.class));
		Page<BjModelType> page = bjModelTypeDao.findAll(spec1, pageRequest);
		List<BjModelType> bjModelTypeList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BjModelType bjModelType : bjModelTypeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", bjModelType.getId());
			map.put("workCenterId", bjModelType.getPkWorkcenter());
			if(bjModelType.getWorkCenter()!=null) {
				map.put("workCenterCode", bjModelType.getWorkCenter().getWorkcenterCode());
				map.put("workCenterName", bjModelType.getWorkCenter().getWorkcenterName());
			}
			map.put("modelCode", bjModelType.getModelCode());
			map.put("modelName", bjModelType.getModelName());
			map.put("createBy", sysUserDao.findById((long) bjModelType.getCreateBy()).getUserName());
			map.put("createDate", df.format(bjModelType.getCreateDate()));
			if (bjModelType.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) bjModelType.getLastupdateBy()).getUserName());
				map.put("lastupdateDate", df.format(bjModelType.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	@Override
	public ApiResponseResult getProcList(String type, String condition,PageRequest pageRequest)throws Exception{
				List<Object> list = getBJProcPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,condition,pageRequest);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Total", list.get(2));
				map.put("List", list.get(3));
				return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getType(String keyword ,PageRequest pageRequest)throws Exception{
		List<Object> list = getSystemSubParamPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getId() + "",keyword,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Total", list.get(2));
		map.put("List", list.get(3));
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getWorkCenterList(String type, String condition,PageRequest pageRequest)throws Exception{
				List<Object> list = getBJWorkCenterPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,condition,pageRequest);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Total", list.get(2));
				map.put("List", list.get(3));
				return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult doCheckInfo(String type,String input1,String input2,
			String input3,String input4)throws Exception{
				List<Object> list = chkCenterAndProcPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,input1,input2,input3,input4);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				return ApiResponseResult.success();
	}

	//防止读取Excel为null转String 报空指针异常
	public String tranCell(Object object)
	{
		if(object==null||object==""||("").equals(object)){
			return null;
		}else return object.toString();
	}

	@Override
	public ApiResponseResult doExcel(MultipartFile[] file) throws Exception{
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
			List<BjModelType> bjModelTypeList = new ArrayList<>();
			for (int row = 1; row <= maxRow; row++) {
//				String errInfo = "";
				String workCenterName = tranCell(sheet.getRow(row).getCell(0));
				String modelCode = tranCell(sheet.getRow(row).getCell(1));
				String modelName = tranCell(sheet.getRow(row).getCell(2));
				BjModelType bjModelType = new BjModelType();
				if(StringUtils.isNotEmpty(modelCode)){
					List<BjModelType> bjModelTypeList1 = bjModelTypeDao.findByDelFlagAndModelCode(0,modelCode);
					if(bjModelTypeList1.size()>0){
						bjModelType = bjModelTypeList1.get(0);
						bjModelType.setLastupdateBy(userId);
						bjModelType.setLastupdateDate(doExcleDate);
					}else {
						bjModelType.setCreateBy(userId);
						bjModelType.setCreateDate(doExcleDate);
					}
				}
				if(StringUtils.isNotEmpty(workCenterName)){
					List<BjWorkCenter> bjWorkCenterList = bjWorkCenterDao.findByWorkcenterNameAndDelFlag(workCenterName,0);
					if(bjWorkCenterList.size()>0){
						bjModelType.setPkWorkcenter(bjWorkCenterList.get(0).getId());
					}else {
						failures++;
						continue;
					}
				}
				bjModelType.setModelCode(modelCode);
				bjModelType.setModelName(modelName);
				bjModelTypeList.add(bjModelType);
			}
			bjModelTypeDao.saveAll(bjModelTypeList);
			return ApiResponseResult.success("导入成功!,共导入:"+bjModelTypeList.size()+";不通过:"+failures);
//			return ApiResponseResult.success("导入成功! 导入总数:" +all+" :校验通过数:"+successes+" ;不通过数: "+failures);
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
		Specification<BjModelType> spec = Specification.where(BaseService.and(filters, BjModelType.class));
		Specification<BjModelType> spec1 = spec.and(BaseService.or(filters1, BjModelType.class));
		List<BjModelType> procList = bjModelTypeDao.findAll(spec1);

		String excelPath = "static/excelFile/";
		String fileName = "机台类型维护模板.xlsx";
		String[] map_arr = new String[]{"wcName","modelCode","modelName"};
		XSSFWorkbook workbook = new XSSFWorkbook();
//		List<Proc> procList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BjModelType bjModelType : procList) {
			Map<String, Object> map = new HashMap<>();
			if(bjModelType.getWorkCenter()!=null) {
				map.put("wcName", bjModelType.getWorkCenter().getWorkcenterName());
			}
			map.put("modelCode",bjModelType.getModelCode());
			map.put("modelName",bjModelType.getModelName());
			mapList.add(map);
		}
		ExcelExport.exportByRow(response,mapList,workbook,map_arr,excelPath+fileName,fileName,1);

	}
}