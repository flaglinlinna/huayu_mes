package com.web.produce.service.internal;

import java.io.IOException;

//import java.io.OutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.utils.ExcelExport;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.*;
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
import com.web.attendance.ZkemSDKUtils;
import com.web.basic.dao.LineDao;

import com.web.basic.entity.Line;
import com.web.produce.dao.DevClockDao;
import com.web.produce.entity.DevClock;
import com.web.produce.service.DevClockService;

import javax.servlet.http.HttpServletResponse;

/**
 * 打卡机设备维护
 *
 */
@Service(value = "DevClockService")
@Transactional(propagation = Propagation.REQUIRED)
public class DevClocklmpl implements DevClockService {

	@Autowired
	DevClockDao devClockDao;

	@Autowired
	LineDao lineDao;

	/**
	 * 新增卡机
	 */
	@Override
	@Transactional
	public ApiResponseResult add(DevClock devClock) throws Exception {
		if (devClock == null) {
			return ApiResponseResult.failure("卡机不能为空！");
		}
		if (StringUtils.isEmpty(devClock.getDevCode())) {
			return ApiResponseResult.failure("卡机编码不能为空！");
		}
		if (StringUtils.isEmpty(devClock.getDevName())) {
			return ApiResponseResult.failure("卡机名称不能为空！");
		}
		int count = devClockDao.countByDelFlagAndDevCode(0, devClock.getDevCode());
		if (count > 0) {
			return ApiResponseResult.failure("该卡机编码已存在，请填写其他卡机编码！");
		}
		int nameCount = devClockDao.countByDelFlagAndDevName(0, devClock.getDevName());
		if (nameCount > 0) {
			return ApiResponseResult.failure("该卡机名称已存在，请填写其他卡机名称！");
		}
		int ipCount = devClockDao.countByDelFlagAndDevIp(0, devClock.getDevIp());
		if (ipCount > 0) {
			return ApiResponseResult.failure("该IP地址已存在，请填写其他IP！");
		}
		devClock.setCreateDate(new Date());
		devClock.setCreateBy(UserUtil.getSessionUser().getId());
		devClockDao.save(devClock);

		return ApiResponseResult.success("卡机添加成功！").data(devClock);
	}

	/**
	 * 修改卡机
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(DevClock devClock) throws Exception {
		if (devClock == null) {
			return ApiResponseResult.failure("卡机不能为空！");
		}
		if (devClock.getId() == null) {
			return ApiResponseResult.failure("卡机ID不能为空！");
		}
		if (StringUtils.isEmpty(devClock.getDevCode())) {
			return ApiResponseResult.failure("卡机编码不能为空！");
		}
		if (StringUtils.isEmpty(devClock.getDevName())) {
			return ApiResponseResult.failure("卡机名称不能为空！");
		}
		DevClock o = devClockDao.findById((long) devClock.getId());
		if (o == null) {
			return ApiResponseResult.failure("该卡机不存在！");
		}
		// 判断卡机编码是否有变化，有则修改；没有则不修改
		if (o.getDevCode().equals(devClock.getDevCode())) {
		} else {
			int count = devClockDao.countByDelFlagAndDevCode(0, devClock.getDevCode());
			if (count > 0) {
				return ApiResponseResult.failure("卡机编码已存在，请填写其他卡机编码！");
			}
			o.setDevCode(devClock.getDevCode().trim());
		}
		// 判断卡机名称是否有变化，有则修改；没有则不修改
		if (o.getDevName().equals(devClock.getDevName())) {
		} else {
			int nameCount = devClockDao.countByDelFlagAndDevName(0, devClock.getDevName());
			if (nameCount > 0) {
				return ApiResponseResult.failure("该卡机名称已存在，请填写其他卡机名称！");
			}
			o.setDevName(devClock.getDevName());
		}
		// 判断卡机Ip是否有变化，有则修改；没有则不修改
		if (o.getDevIp().equals(devClock.getDevIp())) {
		} else {
			int ipCount = devClockDao.countByDelFlagAndDevIp(0, devClock.getDevIp());
			if (ipCount > 0) {
				return ApiResponseResult.failure("该IP地址已存在，请填写其他IP！");
			}
			o.setDevIp(devClock.getDevIp());
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setDevSeries(devClock.getDevSeries());
		o.setDevType(devClock.getDevType());
		o.setLineId(devClock.getLineId());
		o.setIsOnline(devClock.getIsOnline());
		o.setEnabled(devClock.getEnabled());
		devClockDao.save(o);
		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 根据ID获取
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public ApiResponseResult getDevClock(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("卡机ID不能为空！");
		}
		DevClock o = devClockDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("该卡机不存在！");
		}
		return ApiResponseResult.success().data(o);
	}

	/**
	 * 删除卡机
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("卡机ID不能为空！");
		}
		DevClock o = devClockDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("卡机不存在！");
		}
		o.setDelTime(new Date());
		o.setDelFlag(1);
		o.setDelBy(UserUtil.getSessionUser().getId());
		devClockDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 查询列表
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
			filters1.add(new SearchFilter("devCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devIp", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devSeries", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("line.lineName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<DevClock> spec = Specification.where(BaseService.and(filters, DevClock.class));
		Specification<DevClock> spec1 = spec.and(BaseService.or(filters1, DevClock.class));
		Page<DevClock> page = devClockDao.findAll(spec1, pageRequest);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (DevClock bs : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("lineId", bs.getLine().getLineName());// 获取关联表的数据
			map.put("id", bs.getId());
			map.put("devCode", bs.getDevCode());
			map.put("devName", bs.getDevName());
			map.put("devIp", bs.getDevIp());
			map.put("devSeries", bs.getDevSeries());
			map.put("devType", bs.getDevType());
			map.put("enabled", bs.getEnabled());
			map.put("lastupdateDate", bs.getLastupdateDate());
			map.put("createDate", bs.getCreateDate());
            map.put("isOnline", bs.getIsOnline());
			list.add(map);
		}

		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}


	/**
	 * 导出
	 */
	@Override
	@Transactional
	public void exportList(String keyword, HttpServletResponse response) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("devCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devIp", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devSeries", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("devType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("line.lineName", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<DevClock> spec = Specification.where(BaseService.and(filters, DevClock.class));
		Specification<DevClock> spec1 = spec.and(BaseService.or(filters1, DevClock.class));
		List<DevClock> devClockList  = devClockDao.findAll(spec1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (DevClock bs : devClockList) {
			Map<String, Object> map = new HashMap<>();
//			map.put("lineId", bs.getLine().getLineName());// 获取关联表的数据
//			map.put("id", bs.getId());
			map.put("devCode", bs.getDevCode());
			map.put("devName", bs.getDevName());
			map.put("devIp", bs.getDevIp());
			map.put("devSeries", bs.getDevSeries());
			map.put("lineName", bs.getLine().getLineName());
			map.put("devType", bs.getDevType());
			map.put("enabled", bs.getEnabled()==1?"有效":"无效");
			map.put("lastupdateDate", sdf.format(bs.getLastupdateDate()));
			map.put("createDate", sdf.format(bs.getCreateDate()));
			list.add(map);
		}
		//创建一个数组用于设置表头
		String[] arr = new String[]{"卡机编号","卡机名称","卡机IP","卡机序列","线别","卡机类型","是否有效","更新时间","添加时间"};
		String[] map_arr = new String[]{"devCode","devName","devIp","devSeries","lineName","devType","enabled","lastupdateDate","createDate"};
		//调用Excel导出工具类
		ExcelExport.export(response,list,arr,map_arr,"卡机信息.xls");
	}


	//Excel样式
	public List<XSSFCellStyle> getStyle(XSSFWorkbook workbook) {
		List<XSSFCellStyle> cellStyleList = new ArrayList<XSSFCellStyle>();

		//添加字体
		//0.
		XSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);  //字体加粗

		//1.
		XSSFFont font1 = workbook.createFont();
		font1.setFontName("宋体");
		font1.setFontHeightInPoints((short) 10);

		//添加样式
		//0.实线边框
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);  //上边框
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);  //右边框
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);  //下边框
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);  //左边框
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);  //水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  //垂直居中
		cellStyle.setWrapText(true);  //自动换行
		//cellStyle.setFillForegroundColor(new XSSFColor(new Color(184, 204, 228)));//背景颜色
		//cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellStyleList.add(cellStyle);

		//1.实线边框
		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFont(font1);
		cellStyle1.setBorderTop(CellStyle.BORDER_THIN);  //上边框
		cellStyle1.setBorderRight(CellStyle.BORDER_THIN);  //右边框
		cellStyle1.setBorderBottom(CellStyle.BORDER_THIN);  //下边框
		cellStyle1.setBorderLeft(CellStyle.BORDER_THIN);  //左边框
		cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);  //水平居中
		cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);  //垂直居中
		cellStyle1.setWrapText(true);  //自动换行
		cellStyleList.add(cellStyle1);

		return cellStyleList;
	}

	/**
	 * 获取线体数据
	 */
	@Override
	@Transactional
	public ApiResponseResult getLineList() throws Exception {
		List<Line> list = lineDao.findByDelFlag(0);
		return ApiResponseResult.success().data(list);
	}

	/**
	 * 是否有效
	 */
	@Override
	@Transactional
	public ApiResponseResult doEnabled(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("卡机ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置有效或无效！");
		}
		DevClock o = devClockDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("卡机不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		devClockDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	@Override
	public ApiResponseResult test(DevClock devClock) throws Exception {
		// TODO Auto-generated method stub
		boolean connFlag = ZkemSDKUtils.connect(devClock.getDevIp(), 4370);
		String str = "";
		if (connFlag) {
			str = "连接成功!";
		} else {
			str = "连接失败!请检查一下网络以及IP地址!";
		}
		return ApiResponseResult.success(str);
	}
}
