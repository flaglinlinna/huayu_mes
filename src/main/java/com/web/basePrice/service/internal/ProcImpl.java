package com.web.basePrice.service.internal;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.system.user.dao.SysUserDao;

import com.utils.ExcelExport;
import com.web.basePrice.dao.BaseFeeDao;
import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.entity.*;
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
import com.web.basePrice.dao.ProcDao;
import com.web.basePrice.service.ProcService;
import com.web.basic.entity.Mtrial;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @date Dec 4, 2020 3:23:25 PM
 */
@Service(value = "ProcService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProcImpl extends BasePriceUtils implements ProcService {

	@Autowired
	private BaseFeeDao baseFeeDao;

	@Autowired
	private ProcDao procDao;

	@Autowired
	private BjWorkCenterDao bjWorkCenterDao;

	@Autowired
	private SysUserDao sysUserDao;

	/**
	 * 新增工序基础信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(Proc proc) throws Exception {
		if (proc == null) {
			return ApiResponseResult.failure("工序信息不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcNo())) {
			return ApiResponseResult.failure("工序信息编码不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcName())) {
			return ApiResponseResult.failure("工序信息名称不能为空！");
		}
		int count = procDao.countByDelFlagAndProcNo(0, proc.getProcNo());
		if (count > 0) {
			return ApiResponseResult.failure("该工序信息编码已存在，请填写其他工序信息编码！");
		}
		proc.setCreateDate(new Date());
		proc.setCreateBy(UserUtil.getSessionUser().getId());
		procDao.save(proc);
		return ApiResponseResult.success("工序信息添加成功！").data(proc);
	}

	/**
	 * 修改工序信息
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(Proc proc) throws Exception {
		if (proc == null) {
			return ApiResponseResult.failure("工序信息不能为空！");
		}
		if (proc.getId() == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcNo())) {
			return ApiResponseResult.failure("工序信息编码不能为空！");
		}
		if (StringUtils.isEmpty(proc.getProcName())) {
			return ApiResponseResult.failure("工序信息名称不能为空！");
		}
		Proc o = procDao.findById((long) proc.getId());
		if (o == null) {
			return ApiResponseResult.failure("该工序信息不存在！");
		}
		// 判断工序信息编码是否有变化，有则修改；没有则不修改
		if (o.getProcNo().equals(proc.getProcNo())) {
		} else {
			int count = procDao.countByDelFlagAndProcNo(0, proc.getProcNo());
			if (count > 0) {
				return ApiResponseResult.failure("工序信息编码已存在，请填写其他工序信息编码！");
			}
			o.setProcNo(proc.getProcNo().trim());
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setProcName(proc.getProcName());
		o.setWorkcenterId(proc.getWorkcenterId());
		o.setCheckStatus(proc.getCheckStatus());
		procDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 根据ID获取工序信息详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getProc(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		Proc o = procDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该工序信息不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除工序信息
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		Proc o = procDao.findById((long) id);
		Integer num = baseFeeDao.findByDelFlagAndProcId(0,id).size();
		if(num>0){
			return ApiResponseResult.failure("该工序信息存在"+num +"条人工制费信息,请先删除后操作");
		}
		if (o == null) {
			return ApiResponseResult.failure("工序信息不存在！");
		}
		o.setDelFlag(1);
		procDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("工序信息ID不能为空！");
		}
		if (checkStatus == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		Proc o = procDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("工序信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setCheckStatus(checkStatus);
		procDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询工序基础信息维护列表
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
			filters1.add(new SearchFilter("procNo", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Proc> spec = Specification.where(BaseService.and(filters, Proc.class));
		Specification<Proc> spec1 = spec.and(BaseService.or(filters1, Proc.class));
		Page<Proc> page = procDao.findAll(spec1, pageRequest);
		List<Proc> procList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (Proc proc : procList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", proc.getId());
			map.put("procNo", proc.getProcNo());
			map.put("procName", proc.getProcName());
			if(proc.getBjWorkCenter()!=null){
				map.put("workCenterId", proc.getBjWorkCenter().getId());
				map.put("workCenter", proc.getBjWorkCenter().getWorkcenterName());
			}
			map.put("checkStatus", proc.getCheckStatus());
			map.put("createBy", sysUserDao.findById((long) proc.getCreateBy()).getUserName());
			map.put("createDate", df.format(proc.getCreateDate()));
			if (proc.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) proc.getLastupdateBy()).getUserName());
				map.put("lastupdateDate", df.format(proc.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
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
			List<Proc> procArrayList = new ArrayList<>();
			for (int row = 2; row <= maxRow; row++) {
//				String errInfo = "";
				String id = tranCell(sheet.getRow(row).getCell(0)); //id
				String procNo = tranCell(sheet.getRow(row).getCell(1));
				String procName = tranCell(sheet.getRow(row).getCell(2));
				String centerName = tranCell(sheet.getRow(row).getCell(3));
				Proc proc = new Proc();
				if(StringUtils.isNotEmpty(id)){
					proc = procDao.findById(Long.parseLong(id));
					if(proc!=null){
						if(proc.getProcNo()!=procNo){
							List<Proc> procList = procDao.findByDelFlagAndProcNo(0,procNo);
							if(procList.size()>0){
								failures++;
								continue;
							}else {
								proc.setProcNo(procNo);
							}
						}
					}
				}
				else {
					List<Proc> procList = procDao.findByDelFlagAndProcNo(0,procNo);
					if(procList.size()>0){
						failures++;
						continue;
					}else {
						proc.setProcNo(procNo);
					}
				}
				proc.setProcName(procName);
				List<BjWorkCenter> bjWorkCenterList = bjWorkCenterDao.findByWorkcenterNameAndDelFlag(centerName,0);
				if(bjWorkCenterList.size()>0){
					proc.setWorkcenterId(bjWorkCenterList.get(0).getId());
				}else {
					failures++;
					continue;
				}
				proc.setCreateBy(userId);
				proc.setCreateDate(doExcleDate);
				procArrayList.add(proc);
			}
			procDao.saveAll(procArrayList);
			return ApiResponseResult.success("导入成功!,共导入:"+procArrayList.size()+";不通过:"+failures);
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
		Specification<Proc> spec = Specification.where(BaseService.and(filters, Proc.class));
		Specification<Proc> spec1 = spec.and(BaseService.or(filters1, Proc.class));
		List<Proc> procList = procDao.findAll(spec1);

		String excelPath = "static/excelFile/";
		String fileName = "工序基础信息维护模板.xlsx";
		String[] map_arr = new String[]{"id","procNo","procName","workCenter"};
		XSSFWorkbook workbook = new XSSFWorkbook();
//		List<Proc> procList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (Proc proc : procList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", proc.getId());
			map.put("procNo", proc.getProcNo());
			map.put("procName", proc.getProcName());
			if(proc.getBjWorkCenter()!=null){
//				map.put("workCenterId", proc.getBjWorkCenter().getId());
				map.put("workCenter", proc.getBjWorkCenter().getWorkcenterName());
			}
			mapList.add(map);
		}
		ExcelExport.export(response,mapList,workbook,map_arr,excelPath+fileName,fileName);

	}

}