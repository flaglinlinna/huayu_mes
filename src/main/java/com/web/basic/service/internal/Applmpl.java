package com.web.basic.service.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FileUtils;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.BaseSql;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.AppDao;
import com.web.basic.entity.AppVersion;
import com.web.basic.service.AppService;


@Service(value = "appService")
@Transactional(propagation = Propagation.REQUIRED)
public class Applmpl extends BaseSql  implements AppService {
	@Autowired
    private AppDao appDao;
	@Autowired  
	private Environment env;
	
	 /**
     * 新增
     */
    @Override
    @Transactional
    public ApiResponseResult add(AppVersion app) throws Exception{
        if(app == null){
            return ApiResponseResult.failure("APP版本不能为空！");
        }
        if(StringUtils.isEmpty(app.getVersionNo())){
            return ApiResponseResult.failure("APP版本号不能为空！");
        }
        if(StringUtils.isEmpty(app.getVersionUrl())){
            return ApiResponseResult.failure("APP地址不能为空！");
        }
        int count = appDao.countByDelFlagAndVersionNo(0, app.getVersionNo());
        if(count > 0){
            return ApiResponseResult.failure("该APP版本号已存在！");
        }
        app.setCreateDate(new Date());
        app.setCreateBy(UserUtil.getSessionUser().getId());
        appDao.save(app);

        return ApiResponseResult.success("线体添加成功！");
    }
    /**
     * 修改线体
     */
    @Override
    @Transactional
    public ApiResponseResult edit(AppVersion app) throws Exception {
    	if(app == null){
            return ApiResponseResult.failure("APP版本不能为空！");
        }
        if(StringUtils.isEmpty(app.getVersionNo())){
            return ApiResponseResult.failure("APP版本号不能为空！");
        }
        if(StringUtils.isEmpty(app.getVersionUrl())){
            return ApiResponseResult.failure("APP地址不能为空！");
        }
        AppVersion o = appDao.findById((long) app.getId());
        if(o == null){
            return ApiResponseResult.failure("该线体不存在！");
        }
        //判断线体编号是否有变化，有则修改；没有则不修改
        if(o.getVersionNo().equals(app.getVersionNo())){
        }else{
            int count = appDao.countByDelFlagAndVersionNo(0, app.getVersionNo());
            if(count > 0){
                return ApiResponseResult.failure("APP版本号已存在，请填写其他APP版本号！");
            }
            o.setVersionNo(app.getVersionNo().trim());
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setVersionUrl(app.getVersionUrl());
        appDao.save(o);
        return ApiResponseResult.success("编辑成功！");
	}

    /**
     * 删除线体
     */
    @Override
    @Transactional
    public ApiResponseResult delete(String ids) throws Exception{
        if(StringUtils.isEmpty(ids)){
            return ApiResponseResult.failure("ID不能为空！");
        }
        String[] id_s = ids.split(",");
        List<AppVersion> ll = new ArrayList<AppVersion>();
        for(String id:id_s){
        	AppVersion o  = appDao.findById(Long.parseLong(id));
            if(o != null){
            	o.setDelTime(new Date());
                o.setDelFlag(1);
                o.setDelBy(UserUtil.getSessionUser().getId());
                ll.add(o);
            }
        }
        appDao.saveAll(ll);
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
					filters1.add(new SearchFilter("versionNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("versionUrl", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<AppVersion> spec = Specification.where(BaseService.and(filters, AppVersion.class));
				Specification<AppVersion> spec1 = spec.and(BaseService.or(filters1, AppVersion.class));
				Page<AppVersion> page = appDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	@Override
	public ApiResponseResult upload(MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		if(file.isEmpty()){
			return ApiResponseResult.failure("文件不能为空!");
		}
		String url = env.getProperty("pda.url");
		String fileName = file.getOriginalFilename();
        String filePath = env.getProperty("pda.url");
        //filePath = "E:\\apache-tomcat-8.5.34\\webapps\\apk\\";
        File dest = new File(filePath + fileName);
       // file.transferTo(dest);
        FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
		return ApiResponseResult.success("上传成功").data(fileName);
		
		/* String jar_parent = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();
                jar_parent+= File.separator+"apk"+File.separator;
         String fileName = file.getOriginalFilename();        
         File dest = new File(jar_parent + fileName);  
         FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
		 return ApiResponseResult.success("上传成功").data(fileName);*/        
	}


}
