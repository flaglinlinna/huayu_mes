package com.web.basePrice.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;

import com.web.basePrice.dao.BjModelTypeDao;
import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.entity.BjModelType;
import com.web.basePrice.entity.BjWorkCenter;
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
			return ApiResponseResult.failure("人工制费信息不能为空！");
		}
		if (StringUtils.isEmpty(bjModelType.getPkWorkcenter().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
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
			return ApiResponseResult.failure("人工制费信息不能为空！");
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
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("modelCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("modelName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<BjModelType> spec = Specification.where(BaseService.and(filters, BjModelType.class));
		Specification<BjModelType> spec1 = spec.and(BaseService.or(filters1, BjModelType.class));
		Page<BjModelType> page = bjModelTypeDao.findAll(spec1, pageRequest);
		List<BjModelType> baseFeeList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BjModelType baseFee : baseFeeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", baseFee.getId());
			map.put("workCenterId", baseFee.getPkWorkcenter());
			map.put("workCenterCode", baseFee.getWorkCenter().getWorkcenterCode());
            map.put("workCenterName", baseFee.getWorkCenter().getWorkcenterName());
			map.put("modelCode", baseFee.getModelCode());
			map.put("modelName", baseFee.getModelName());
			map.put("createBy", sysUserDao.findById((long) baseFee.getCreateBy()).getUserName());
			map.put("createDate", df.format(baseFee.getCreateDate()));
			if (baseFee.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) baseFee.getLastupdateBy()).getUserName());
				map.put("lastupdateDate", df.format(baseFee.getLastupdateDate()));
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
//			Integer failures = 0;
			List<BjModelType> bjModelTypeList = new ArrayList<>();
			for (int row = 1; row <= maxRow; row++) {
//				String errInfo = "";
				String workCenterCode = tranCell(sheet.getRow(row).getCell(0));
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
				if(StringUtils.isNotEmpty(workCenterCode)){
					List<BjWorkCenter> bjWorkCenterList = bjWorkCenterDao.findByDelFlagAndWorkcenterCode(0,workCenterCode);
					if(bjWorkCenterList.size()>0){
						bjModelType.setPkWorkcenter(bjWorkCenterList.get(0).getId());
					}
				}
				bjModelType.setModelCode(modelCode);
				bjModelType.setModelName(modelName);
				bjModelTypeList.add(bjModelType);
			}
			bjModelTypeDao.saveAll(bjModelTypeList);
			return ApiResponseResult.success("导入成功!");
//			return ApiResponseResult.success("导入成功! 导入总数:" +all+" :校验通过数:"+successes+" ;不通过数: "+failures);
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}
}