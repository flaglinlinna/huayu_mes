package com.system.defect.service.internal;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.defect.dao.SysDefectDao;
import com.system.defect.dao.SysDefectFileDao;
import com.system.defect.entity.SysDefect;
import com.system.defect.entity.SysDefectFile;
import com.system.defect.service.SysDefectService;
import com.system.file.dao.FsFileDao;
import com.system.file.entity.FsFile;
import com.system.user.entity.SysUser;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.service.DefectiveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 缺陷记录
 */
@Service(value = "SysDefectService")
@Transactional(propagation = Propagation.REQUIRED)
public class SysDefectImpl implements SysDefectService {

    @Autowired
    private SysDefectDao sysDefectDao;
    @Autowired
    private SysDefectFileDao sysDefectFileDao;
    @Autowired
    private FsFileDao fsFileDao;

    @Override
    @Transactional
    public ApiResponseResult add(SysDefect sysDefect) throws Exception {
        if(sysDefect == null){
            return ApiResponseResult.failure("缺陷记录不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        //1.新增信息
        sysDefect.setOfferName(currUser!=null ? currUser.getUserName() : null);
        sysDefect.setOfferDate(new Date());
        sysDefect.setCreateDate(new Date());
        sysDefect.setCreateBy(currUser!=null ? currUser.getId() : null);
        sysDefectDao.save(sysDefect);

        //2.添加文件关联信息
        if(sysDefect.getFileIds() != null){
            List<SysDefectFile> fileList = new ArrayList<>();
            for(String fileId : sysDefect.getFileIds()){
                if(StringUtils.isNotEmpty(fileId)){
                    FsFile fsFile = fsFileDao.findById((long) Long.parseLong(fileId));//获取文件名称
                    SysDefectFile sysDefectFile = new SysDefectFile();
                    sysDefectFile.setCreateDate(new Date());
                    sysDefectFile.setCreateBy(currUser != null ? currUser.getId() : null);
                    sysDefectFile.setDefectId(sysDefect.getId());
                    sysDefectFile.setFileId(Long.parseLong(fileId));
                    sysDefectFile.setFileName(fsFile != null ? fsFile.getBsName() : null);
                    fileList.add(sysDefectFile);
                }
            }
            if(fileList.size() > 0){
                sysDefectFileDao.saveAll(fileList);
            }
        }

        return ApiResponseResult.success("新增成功！").data(sysDefect);
    }

    @Override
    @Transactional
    public ApiResponseResult edit(SysDefect sysDefect) throws Exception {
        if(sysDefect == null || sysDefect.getId() == null){
            return ApiResponseResult.failure("缺陷记录ID不能为空！");
        }
        SysDefect o = sysDefectDao.findById((long) sysDefect.getId());
        if(o == null){
            return ApiResponseResult.failure("缺陷记录不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setLastupdateDate(new Date());
        o.setLastupdateBy(currUser!=null ? currUser.getId() : null);
        o.setModuleName(sysDefect.getModuleName());
        o.setPriority(sysDefect.getPriority());
        o.setStatus(sysDefect.getStatus());
        o.setDescript(sysDefect.getDescript());

        o.setHandlerName(currUser!=null ? currUser.getUserName() : null);
        o.setHandlerDate(new Date());

        o.setRemark(sysDefect.getRemark());
        sysDefectDao.save(o);

        return ApiResponseResult.success("编辑成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
        if(id == null){
            return ApiResponseResult.failure("缺陷记录ID不能为空！");
        }
        SysDefect o = sysDefectDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("缺陷记录不存在！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        o.setDelTime(new Date());
        o.setDelBy(currUser!=null ? currUser.getId() : null);
        o.setDelFlag(1);
        sysDefectDao.save(o);

        return ApiResponseResult.success("删除成功！").data(o);
    }

    @Override
    @Transactional
    public ApiResponseResult getList(String keyword, Integer priority, String status, PageRequest pageRequest) throws Exception {
        //查询条件1
        List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        if(priority != null){
            filters.add(new SearchFilter("priority", SearchFilter.Operator.EQ, priority));
        }
        if(StringUtils.isNotEmpty(status)){
            filters.add(new SearchFilter("status", SearchFilter.Operator.EQ, status));
        }
        //查询条件2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("moduleName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("descript", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("offerName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("handlerName", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("remark", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("status", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<SysDefect> spec = Specification.where(BaseService.and(filters, SysDefect.class));
        Specification<SysDefect> spec1 =  spec.and(BaseService.or(filters1, SysDefect.class));
        Page<SysDefect> page = sysDefectDao.findAll(spec1, pageRequest);

        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }

    /**
     * 获取附件管理列表
     * @param defectId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public ApiResponseResult getFile(Long defectId) throws Exception{
        if(defectId == null){
            return ApiResponseResult.failure("缺陷记录ID不能为空！");
        }

        List<FsFile> fsFileList = new ArrayList<>();
        List<SysDefectFile> sysDefectFileList = sysDefectFileDao.findByDelFlagAndDefectId(0, defectId);
        for(SysDefectFile sysDefectFile : sysDefectFileList){
            if(sysDefectFile != null && sysDefectFile.getFileId() != null){
                FsFile fsFile = fsFileDao.findById((long) sysDefectFile.getFileId());
                if(fsFile != null){
                    fsFileList.add(fsFile);
                }
            }
        }

        return ApiResponseResult.success().data(fsFileList);
    }

    /**
     * 附件管理上传文件
     * @param defectId
     * @param fileId
     * @param fileName
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult addFile(Long defectId, Long fileId, String fileName) throws Exception{
        if(defectId == null){
            return ApiResponseResult.failure("缺陷记录ID不能为空！");
        }
        if(fileId == null){
            return ApiResponseResult.failure("文件ID不能为空！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        SysDefectFile sysDefectFile = new SysDefectFile();
        sysDefectFile.setCreateDate(new Date());
        sysDefectFile.setCreateBy(currUser != null ? currUser.getId() : null);
        sysDefectFile.setDefectId(defectId);
        sysDefectFile.setFileId(fileId);
        if(StringUtils.isNotEmpty(fileName)){
            sysDefectFile.setFileName(fileName);
        }else{
            FsFile fsFile = fsFileDao.findById((long) fileId);
            sysDefectFile.setFileName(fsFile != null ? fsFile.getBsName() : null);
        }
        sysDefectFileDao.save(sysDefectFile);

        return ApiResponseResult.success("附件上传成功！");
    }

    /**
     * 附件管理删除文件
     * @param defectId
     * @param fileId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public ApiResponseResult delFile(Long defectId, Long fileId) throws Exception{
        if(defectId == null){
            return ApiResponseResult.failure("缺陷记录ID不能为空！");
        }
        if(fileId == null){
            return ApiResponseResult.failure("文件ID不能为空！");
        }
        //获取文件关联表
        List<SysDefectFile> sysDefectFileList = sysDefectFileDao.findByDelFlagAndDefectIdAndFileId(0, defectId, fileId);
        if(sysDefectFileList.size() <= 0){
            return ApiResponseResult.failure("文件不存在，无法删除！");
        }
        //获取文件表信息
        FsFile fsFile = fsFileDao.findById((long) fileId);
        if(fsFile == null){
            return ApiResponseResult.failure("文件不存在，无法删除！");
        }
        SysUser currUser = UserUtil.getSessionUser();

        //1.删除文件关联表
        for(SysDefectFile o : sysDefectFileList){
            if(o != null){
                o.setDelTime(new Date());
                o.setDelBy(currUser != null ? currUser.getId() : null);
                o.setDelFlag(1);
            }
        }
        sysDefectFileDao.saveAll(sysDefectFileList);

        //2.删除文件表信息
        fsFile.setDelTime(new Date());
        fsFile.setDelBy(currUser != null ? currUser.getId() : null);
        fsFile.setDelFlag(1);
        fsFileDao.save(fsFile);

        return ApiResponseResult.success("删除成功！");
    }
}
