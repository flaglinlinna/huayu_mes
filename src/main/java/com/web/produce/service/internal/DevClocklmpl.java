package com.web.produce.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.web.basic.entity.Client;
import com.web.basic.entity.Defective;
import com.web.basic.entity.DefectiveDetail;
import com.web.basic.entity.Line;
import com.web.basic.entity.Mtrial;
import com.web.produce.dao.DevClockDao;
import com.web.produce.entity.DevClock;
import com.web.produce.service.DevClockService;

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
			return ApiResponseResult.failure("该卡机已存在，请填写其他卡机编码！");
		}
		int ipCount=devClockDao.countByDelFlagAndDevIp(0, devClock.getDevIp());
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
		if(o.getDevIp().equals(devClock.getDevIp())){
			int ipCount=devClockDao.countByDelFlagAndDevIp(0, devClock.getDevIp());
			if (ipCount > 0) {
				return ApiResponseResult.failure("该IP地址已存在，请填写其他IP！");
			}
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setDevName(devClock.getDevName());
		o.setDevIp(devClock.getDevIp());
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

		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(DevClock bs:page.getContent()){ 
			Map<String, Object> map = new HashMap<>();
			map.put("lineId",bs.getLine().getLineName());//获取关联表的数据
			map.put("id", bs.getId());
			map.put("devCode", bs.getDevCode());
			map.put("devName", bs.getDevName());
			map.put("devIp", bs.getDevIp());
			map.put("devSeries", bs.getDevSeries());
			map.put("devType", bs.getDevType());
			map.put("enabled", bs.getEnabled());
			map.put("lastupdateDate",bs.getLastupdateDate());
			map.put("createDate", bs.getCreateDate());
			list.add(map);
		}
		
		return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
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
        if(connFlag){
        	str = "连接成功!";
        }else{
        	str = "连接失败!请检查一下网络以及IP地址!";
        }
		return ApiResponseResult.success(str);
	}
}
