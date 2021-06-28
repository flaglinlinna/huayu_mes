package com.system.file.service.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.app.base.data.DataGrid;
import com.system.file.dao.CommonFileDao;
import com.system.file.entity.CommonFile;
import com.system.user.dao.SysUserDao;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.util.StringUtils;
import com.app.base.data.ApiResponseResult;
import com.app.base.service.FtpClientService;
import com.system.file.dao.FsFileDao;
import com.system.file.entity.FsFile;
import com.system.file.service.FileService;

@Configuration
public class FileImpl  implements FileService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FsFileDao fsFileDao;
    @Autowired
    private FtpClientService ftpClientService;
    @Autowired
    private Environment env;
    @Autowired
    private CommonFileDao commonFileDao;
    @Autowired
    private SysUserDao sysUserDao;

    /**
     * 上传文件
     * @param fsFile
     * @param file
     * @return
     * @throws Exception
     */
    public ApiResponseResult upload(FsFile fsFile, MultipartFile file) throws Exception {
        if(null==file || file.isEmpty()) {
            return ApiResponseResult.failure("上传文件不能为空");
        }
        String qmsPath = env.getProperty("fs.qms.path");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());

        String path = qmsPath + "/" + ymd;

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateFileName = df.format(new Date()) + "_" + new Random().nextInt(1000);

        try {
            fsFile.setBsFileSize(file.getSize());
            if(null==fsFile.getBsContentType()) {
                fsFile.setBsContentType(file.getContentType());
            }
            if(null==file.getOriginalFilename()) {
                fsFile.setBsFileType("Unknown");
                return ApiResponseResult.failure("无法识别该文件类型！");
            }

            String originalFiletype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
            fsFile.setBsFileType(originalFiletype);

//            String originalFilename = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
            fsFile.setBsName(file.getOriginalFilename());
            fsFile.setBsFileName(dateFileName + originalFiletype);
            fsFile.setBsFilePath("/"+ymd);
            ApiResponseResult result = ftpClientService.uploadFile(path, dateFileName+fsFile.getBsFileType(), new ByteArrayInputStream(file.getBytes()));
            if(result.isResult()) {
                fsFile.setCreateDate(new Date());
                fsFileDao.save(fsFile);
                return ApiResponseResult.success("文件上传成功！").data(fsFile);
            }
        } catch (IOException e) {
            logger.error("upload file exception", e);
        }
        return ApiResponseResult.failure("上传文件发生异常");
    }


    @Override
    public ApiResponseResult uploadByBs(FsFile fsFile, MultipartFile file,Long bsId,String bsType) throws Exception {
        if(null==file || file.isEmpty()) {
            return ApiResponseResult.failure("上传文件不能为空");
        }
        String qmsPath = env.getProperty("fs.qms.path");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String ymd = sdf.format(new Date());

        String path = qmsPath + "/" + ymd;

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateFileName = df.format(new Date()) + "_" + new Random().nextInt(1000);

        try {
            fsFile.setBsFileSize(file.getSize());
            if(null==fsFile.getBsContentType()) {
                fsFile.setBsContentType(file.getContentType());
            }
            if(null==file.getOriginalFilename()) {
                fsFile.setBsFileType("Unknown");
                return ApiResponseResult.failure("无法识别该文件类型！");
            }

            String originalFiletype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
            fsFile.setBsFileType(originalFiletype);

//            String originalFilename = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
            fsFile.setBsName(file.getOriginalFilename());
            fsFile.setBsFileName(dateFileName + originalFiletype);
            fsFile.setBsFilePath("/"+ymd);
            ApiResponseResult result = ftpClientService.uploadFile(path, dateFileName+fsFile.getBsFileType(), new ByteArrayInputStream(file.getBytes()));
            if(result.isResult()) {
                fsFile.setCreateDate(new Date());
                fsFileDao.save(fsFile);

                CommonFile commonFile = new CommonFile();
                commonFile.setmId(bsId);
                commonFile.setFileId(fsFile.getId());
                commonFile.setFileName(fsFile.getBsName());
                commonFile.setCreateDate(new Date());
                commonFile.setCreateBy(UserUtil.getSessionUser().getId());
                commonFile.setBsType(bsType);
                commonFileDao.save(commonFile);

                return ApiResponseResult.success("文件上传成功！").data(fsFile);
            }
        } catch (IOException e) {
            logger.error("upload file exception", e);
        }
        return ApiResponseResult.failure("上传文件发生异常");
    }




    /**
     * 下载文件
     * @param fsFileId
     * @param response
     * @return
     * @throws Exception
     */
    public ApiResponseResult get(Long fsFileId, HttpServletResponse response) throws Exception {
        Optional<FsFile> fsFiles = fsFileDao.findById(fsFileId);
        if(null==fsFiles) {
            return ApiResponseResult.failure("文件不存在或已被删除");
        }
        FsFile fsFile = fsFiles.get();
        String path = env.getProperty("fs.qms.path")+fsFile.getBsFilePath();
        ApiResponseResult result = ftpClientService.download(path, fsFile.getBsFileName());
        try {
//            String fileName = new String(fsFile.getBsName().getBytes("UTF-8"), "ISO-8859-1")+ fsFile.getBsFileType();
            String fileName = URLEncoder.encode(fsFile.getBsName(), "UTF-8");
//            response.setContentType("application/octet-stream");
            response.setContentType(fsFile.getBsContentType());
            // 设置response的Header
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fsFile.getBsName()+fsFile.getBsFileType()).getBytes("UTF-8"), "ISO-8859-1"));
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName );
            response.addHeader("Content-Length", "" + fsFile.getBsFileSize());
            OutputStream os = response.getOutputStream();
            byte[] bytes = (byte[]) result.getData();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("download file exception", e);
        }
        return null;
    }

    /**
     * 图片在线预览（非图片下载）
     * @param fsFileId
     * @param response
     * @return
     * @throws Exception
     */
    public ApiResponseResult onlineView(Long fsFileId, HttpServletResponse response) throws Exception {
    	Optional<FsFile> fsFiles = fsFileDao.findById(fsFileId);
        if(null==fsFiles) {
            return ApiResponseResult.failure("文件不存在或已被删除");
        }
        FsFile fsFile = fsFiles.get();
        String path = env.getProperty("fs.qms.path")+fsFile.getBsFilePath();
        ApiResponseResult result = ftpClientService.download(path, fsFile.getBsFileName());
        try {
            String fileName = URLEncoder.encode(fsFile.getBsName(), "UTF-8");  //文件名称
            String extName = fsFile.getBsFileType();  //文件后缀名
            response.setContentType(fsFile.getBsContentType());
            response.addHeader("Content-Disposition", "inline;filename=" + fileName );
            response.addHeader("Content-Length", "" + fsFile.getBsFileSize());
//            if(".png".equals(extName)){
//                response.setContentType("image/png");
//            }
            OutputStream os = response.getOutputStream();
            byte[] bytes = (byte[]) result.getData();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("download file exception", e);
        }
        return null;
    }

    /**
     * 删除文件
     * @param fsFileId
     * @return
     * @throws Exception
     */
    public ApiResponseResult delete(Long fsFileId) throws Exception{
        if(fsFileId == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        Optional<FsFile> fsFiles = fsFileDao.findById(fsFileId);
        if(null==fsFiles) {
            return ApiResponseResult.failure("文件不存在或已被删除");
        }
        FsFile fsFile = fsFiles.get();

        fsFile.setDelFlag(1);
        fsFileDao.save(fsFile);
        return ApiResponseResult.success("文件删除成功！");
    }

    /**
     * 删除业务文件
     * @param ids
     * @return
     * @throws Exception
     */
    public ApiResponseResult deleteBsFile(String ids) throws Exception{
        if(ids == null){
            return ApiResponseResult.failure("记录ID不能为空！");
        }
        String[] idsArry = ids.split(",");
        List<CommonFile> commonFileList = new ArrayList<>();
        for(String id:idsArry){
            CommonFile file = commonFileDao.findById(Long.parseLong(id));
            if(null==file) {
                return ApiResponseResult.failure("文件不存在或已被删除");
            }
            file.setDelFlag(1);
            file.setDelTime(new Date());
            file.setDelBy(UserUtil.getSessionUser().getId());
            commonFileList.add(file);
        }
        commonFileDao.saveAll(commonFileList);
        return ApiResponseResult.success("文件删除成功！");
    }
    
    public ApiResponseResult uploadByNameAndUrl(String file_name,String url,FsFile fsFile, MultipartFile file) throws Exception {
        if(null==file || file.isEmpty()) {
            return ApiResponseResult.failure("上传文件不能为空");
        }
        String qmsPath = "/"+url;//env.getProperty("fs.qms.path");

        String path = qmsPath + "/";

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateFileName = file_name;//df.format(new Date()) + "_" + new Random().nextInt(1000);

        try {
            fsFile.setBsFileSize(file.getSize());
            if(null==fsFile.getBsContentType()) {
                fsFile.setBsContentType(file.getContentType());
            }
            if(null==file.getOriginalFilename()) {
                fsFile.setBsFileType("Unknown");
                return ApiResponseResult.failure("无法识别该文件类型！");
            }

            String originalFiletype = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
            fsFile.setBsFileType(originalFiletype);

//            String originalFilename = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
            fsFile.setBsName(file.getOriginalFilename());
            fsFile.setBsFileName(dateFileName + originalFiletype);
            fsFile.setBsFilePath(path);
            ApiResponseResult result = ftpClientService.uploadFile(path, dateFileName+fsFile.getBsFileType(), new ByteArrayInputStream(file.getBytes()));
            if(result.isResult()) {
                fsFile.setCreateDate(new Date());
                fsFileDao.save(fsFile);
                return ApiResponseResult.success("文件上传成功！").data(fsFile);
            }
        } catch (IOException e) {
            logger.error("upload file exception", e);
        }
        return ApiResponseResult.failure("上传文件发生异常");
    }

	@Override
	public ApiResponseResult viewByUrl(String url,String file_Name, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
        if(StringUtils.isEmpty(url)) {
            return ApiResponseResult.failure("文件不存在或已被删除");
        }

        ApiResponseResult result = ftpClientService.download(url, file_Name);
        try {
            String fileName = URLEncoder.encode(file_Name, "UTF-8");  //文件名称
            response.setContentType("image/png");
            response.addHeader("Content-Disposition", "inline;filename=" + fileName );
            //response.addHeader("Content-Length", "" + fsFile.getBsFileSize());
//            if(".png".equals(extName)){
//                response.setContentType("image/png");
//            }
            OutputStream os = response.getOutputStream();
            byte[] bytes = (byte[]) result.getData();
            os.write(bytes);
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("download file exception", e);
        }
        return null;
	}

    /**
     * 删除文件
     * @param mid 业务id，bsType 业务类型
     * @return
     * @throws Exception
     */
    public ApiResponseResult getListByBs(Long mid, String bsType, PageRequest pageRequest) throws Exception{

        Page<CommonFile> page = fsFileDao.getFileListByBs(mid,bsType,pageRequest);
        List<HashMap<String,Object>> mapList = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(CommonFile commonFile:page.getContent()){
          HashMap map =  JSONObject.parseObject(JSONObject.toJSONString(commonFile),HashMap.class);
          if(map.get("createBy")!=null){
              SysUser user = sysUserDao.findById(Long.parseLong(map.get("createBy").toString()));
              if(user!=null){
                  map.put("createName",user.getUserName());
                  map.put("createDate",df.format(commonFile.getCreateDate()));
              }
          }
          mapList.add(map);
        }
        return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
    }
}
