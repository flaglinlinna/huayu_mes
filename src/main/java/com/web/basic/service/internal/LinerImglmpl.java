package com.web.basic.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.BaseSql;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.LinerImgDao;
import com.web.basic.entity.LinerImg;
import com.web.basic.service.LinerImgService;
import com.web.report.service.internal.ReportPrcUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service(value = "linerImgService")
@Transactional(propagation = Propagation.REQUIRED)
public class LinerImglmpl extends ReportPrcUtils implements LinerImgService {
	@Autowired
    private LinerImgDao linerImgDao;

    //组长取数
    @Override
    public ApiResponseResult getDeptInfo(String keyword) throws Exception {
        // TODO Auto-generated method stub
        List<Object> list = getDeptInfoPrc(UserUtil.getSessionUser().getFactory() + "",
                UserUtil.getSessionUser().getCompany() + "", "", "组长铁三角");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
    }

    // QC取数，工程取数
    @Override
    public ApiResponseResult getEmpCode(String keyword,PageRequest pageRequest) throws Exception {
        // TODO Auto-generated method stub
        List<Object> list = getEmpPrc(UserUtil.getSessionUser().getCompany() + "",
                UserUtil.getSessionUser().getFactory() + "", "", keyword,
                pageRequest.getPageNumber()+1, pageRequest.getPageSize());
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        //20201127-fyx-
        Map map = new HashMap();
        map.put("Total", list.get(2));
        map.put("Rows", list.get(3));
        return ApiResponseResult.success().data(map);
    }

    // 获取生产线
    @Override
    public ApiResponseResult getLine() throws Exception {
        List<Object> list = getLinePrc(UserUtil.getSessionUser().getCompany()+"",UserUtil.getSessionUser().getFactory()+"",UserUtil.getSessionUser().getId()+"","");
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(1).toString());
        }
        return ApiResponseResult.success().data(list.get(2));
    }


	 /**
     * 新增线体
     */
    @Override
    @Transactional
    public ApiResponseResult add(LinerImg linerImg) throws Exception{
        if(linerImg == null){
            return ApiResponseResult.failure("组长铁三角不能为空！");
        }
        linerImg.setCreateDate(new Date());
        linerImg.setCreateBy(UserUtil.getSessionUser().getId());
        linerImgDao.save(linerImg);
        return ApiResponseResult.success("线体添加成功！").data(linerImg);
    }
    /**
     * 修改线体
     */
    @Override
    @Transactional
    public ApiResponseResult edit(LinerImg linerImg) throws Exception {
        LinerImg o = linerImgDao.findById((long) linerImg.getId());
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setEmpIdLiner(linerImg.getEmpIdLiner());
        o.setEmpIdPe(linerImg.getEmpIdPe());
        o.setEmpIdQc(linerImg.getEmpIdQc());
        o.setLineId(linerImg.getLineId());
        o.setOrgIdLiner(linerImg.getOrgIdLiner());
        linerImgDao.save(o);
        return ApiResponseResult.success("编辑成功！");
	}

    /**
     * 根据ID获取
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult getLinerImg(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        LinerImg o = linerImgDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    /**
     * 删除线体
     */
    @Override
    @Transactional
    public ApiResponseResult delete(String ids) throws Exception{
        if(StringUtils.isEmpty(ids)){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        String[] id_s = ids.split(",");
        List<LinerImg> ll = new ArrayList<LinerImg>();
        for(String id:id_s){
            LinerImg o  = linerImgDao.findById(Long.parseLong(id));
            if(o != null){
            	o.setDelTime(new Date());
                o.setDelFlag(1);
                o.setDelBy(UserUtil.getSessionUser().getId());
                ll.add(o);
            }
        }
        linerImgDao.saveAll(ll);
        return ApiResponseResult.success("删除成功！");
    }

    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer checkStatus) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("线体ID不能为空！");
        }
        if(checkStatus == null){
            return ApiResponseResult.failure("请正确设置正常或禁用！");
        }
        LinerImg o = linerImgDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("线体不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setEnabled(checkStatus);
        linerImgDao.save(o);
        return ApiResponseResult.success("设置成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,String beginTime,String endTime,PageRequest pageRequest) throws Exception {

        List<Object> list = getLinerImgProc(UserUtil.getSessionUser().getFactory() + "",
                UserUtil.getSessionUser().getCompany() + "",
                beginTime, endTime,keyword,pageRequest.getPageSize(),pageRequest.getPageNumber()+1);
        if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
            return ApiResponseResult.failure(list.get(2).toString());
        }
        Map map = new HashMap();
        map.put("Total",list.get(2));
        map.put("Rows",list.get(3));
        return ApiResponseResult.success().data(map);

    }


}
