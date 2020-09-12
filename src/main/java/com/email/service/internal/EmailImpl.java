package com.email.service.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import com.email.EmailUtils;
import com.email.EmailUtils_2;
import com.email.dao.SysEmailInfoDao;
import com.email.entity.SysEmailInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.base.data.ApiResponseResult;
import com.app.base.service.FtpClientService;
import com.email.service.EmailService;
import com.system.file.dao.FsFileDao;
import com.system.file.entity.FsFile;

@Configuration
public class EmailImpl  implements EmailService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private FsFileDao fsFileDao;
    @Autowired
    private FtpClientService ftpClientService;
    @Autowired
    private Environment env;
    @Autowired
    private SysEmailInfoDao sysEmailInfoDao;
    @Autowired
    private EmailUtils_2 emailUtils_2;

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
                fsFile.setCreatedTime(new Date());
                fsFileDao.save(fsFile);
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

        fsFile.setIsDel(1);
        fsFileDao.save(fsFile);
        return ApiResponseResult.success("文件删除成功！");
    }

    //发送邮件测试
    @Transactional
    public ApiResponseResult test() throws Exception{
        String enabled = env.getProperty("scm.mail.enabled");
        if(StringUtils.isNotEmpty(enabled) && enabled.equals("true")){
            String subject = "";
            String emailTo = "";
            String[] toUser = {emailTo};
            String content = "";

            EmailUtils.sendEmail(subject, toUser, null, content, null);
            Boolean flag = emailUtils_2.sendEmail(subject, toUser, null, "时间的沙漏总是忙碌和紧张_2", null);
            if(flag){
                SysEmailInfo sysEmailInfo = new SysEmailInfo();
                sysEmailInfo.setCreatedTime(new Date());
                String emailFrom = env.getProperty("mail.auth.name");
                sysEmailInfo.setBsEmailFrom(emailFrom);
                sysEmailInfo.setBsEmailTo(emailTo);
                sysEmailInfo.setBsEmailCc(null);
                sysEmailInfo.setBsSubject(subject);
                sysEmailInfo.setBsContent(content);
                //sysEmailInfoDao.save(sysEmailInfo);
                return ApiResponseResult.success("邮件发送成功！").data(sysEmailInfo);
            }else{
                return ApiResponseResult.success("邮件发送失败！");
            }
        }else{
            return ApiResponseResult.success("邮件发送失败！");
        }
    }
}
