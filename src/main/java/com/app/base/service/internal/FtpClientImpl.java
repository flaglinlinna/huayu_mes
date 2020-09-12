package com.app.base.service.internal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.app.base.data.ApiResponseResult;
import com.app.base.service.FtpClientService;

@Service
public class FtpClientImpl implements FtpClientService {
    private static final Logger logger = LoggerFactory.getLogger(FtpClientImpl.class);

    @Autowired
    private Environment env;

    private FTPClient login() {
        FTPClient ftp = new FTPClient();
        try {
            FtpConfig config = getConfig();
            if (config == null) {
                return null;
            }

            ftp.connect(config.url, config.port);

            ftp.login(config.username, config.password);
            ftp.enterLocalPassiveMode();
            ftp.setFileType(2);
            ftp.setControlEncoding("gbk");
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.info("FTP连接失败");
                ftp.disconnect();
                return null;
            }
            logger.info("FTP连接成功");
            return ftp;
        } catch (IOException e) {
            logger.debug("连接FTP 服务器失败.", e);
            closeCon(ftp);
        }
        return null;
    }

    private void closeCon(FTPClient ftp) {
        if ((ftp != null) &&
                (ftp.isConnected())) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException e) {
                logger.error("关闭FTP 链接失败", e);
            }
        }
    }

    private FtpConfig getConfig() {
        try {
            FtpConfig ftpConfig = new FtpConfig();
            ftpConfig.url = this.env.getProperty("fs.ftp.url");
            ftpConfig.port = Integer.parseInt(this.env.getProperty("fs.ftp.port"));
            ftpConfig.username = this.env.getProperty("fs.ftp.username");
            ftpConfig.password = this.env.getProperty("fs.ftp.password");
            return ftpConfig;
        } catch (Exception e) {
            logger.error("获取FTP 配置文件失败", e);
        }
        return null;
    }

    public ApiResponseResult uploadFile(String path, String filename, InputStream input) {
        String url = this.env.getProperty("fs.ftp.url");
        int port = Integer.parseInt(this.env.getProperty("fs.ftp.port"));
        String username = this.env.getProperty("fs.ftp.username");
        String password = this.env.getProperty("fs.ftp.password");
        if (logger.isDebugEnabled()) {
            logger.debug("FTP 上传 url:" + url + " user name:" + username + " password:" + password);
        }
        return uploadFile(url, port, username, password, path, filename, input);
    }

    public ApiResponseResult uploadFile(String url, int port, String username, String password, String path, String filename, InputStream input) {
        String rootPath = this.env.getProperty("fs.ftp.rootPath", "");
        if (!StringUtils.startsWith(path, "/")) {
            path = File.separator + path;
        }

        String savePath = rootPath + path;
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(url, port);
            ApiResponseResult localApiResponseResult1;
            if (!ftp.login(username, password)) {
                return ApiResponseResult.failure("ftp登录认证失败");
            }

            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return ApiResponseResult.failure("上传失败");
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(2);
            ftp.setControlEncoding("gbk");
            if (!changeWorkingDirectory(ftp, new String(savePath.getBytes("gbk"), "iso-8859-1"))) {
                return ApiResponseResult.failure("上传目录不存在或创建目录失败");
            }
            boolean success = ftp.storeFile(new String(filename.getBytes("gbk"), "iso-8859-1"), input);

            input.close();
            ftp.logout();
            if (!success) {
                return ApiResponseResult.failure("上传失败");
            }
            return ApiResponseResult.success("上传成功").data(path);
        } catch (IOException e) {
            boolean success;
            logger.error("ftp上传文件异常:" + savePath, e);
            return ApiResponseResult.failure("上传异常");
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException localIOException7) {
                }
            }
        }
    }

    public ApiResponseResult download(String path, String fileName) {
        String url = this.env.getProperty("fs.ftp.url");
        int port = Integer.parseInt(this.env.getProperty("fs.ftp.port"));
        String username = this.env.getProperty("fs.ftp.username");
        String password = this.env.getProperty("fs.ftp.password");
        if (logger.isInfoEnabled()) {
            logger.info("FTP 下载 url:" + url + " user name:" + username + " password:" + password);
        }
        return download(url, port, username, password, path, fileName);
    }

    public ApiResponseResult download(String url, int port, String username, String password, String path, String fileName) {
        logger.info("下载地址: " + url + " port: " + port + " username: " + username + " password: " + password);
        String rootPath = this.env.getProperty("fs.ftp.rootPath", "");
        if (!StringUtils.startsWith(path, "/")) {
            path = File.separator + path;
        }
        path = rootPath + path;
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(url, port);
            ApiResponseResult localApiResponseResult1;
            if (!ftp.login(username, password)) {
                return ApiResponseResult.failure("ftp登录认证失败");
            }
            logger.info("ftp 登录成功");

            ftp.setFileType(2);
            ftp.setControlEncoding("gbk");
            int reply = ftp.getReplyCode();
            logger.info("ftp.getReplyCode() success");
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.out.println("!FTPReply.isPositiveCompletion(reply)");
                ftp.disconnect();
                return ApiResponseResult.failure("下载失败");
            }
            ftp.enterLocalPassiveMode();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            boolean flag = ftp.retrieveFile(new String((path + "/" + fileName).getBytes("gbk"), "iso-8859-1"), (OutputStream) bos);

            logger.info("ftp.retrieveFile success");
            ApiResponseResult localApiResponseResult2;
            if (!flag) {
                return ApiResponseResult.failure("文件不存在或已被删除");
            }
            ftp.logout();
            return ApiResponseResult.success("下载成功").data(((ByteArrayOutputStream) bos).toByteArray());
        } catch (IOException e) {
            Object bos;
            logger.error("ftp下载文件异常", e);
            return ApiResponseResult.failure("下载失败");
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException localIOException6) {
                }
            }
        }
    }

    protected boolean changeWorkingDirectory(FTPClient ftp, String path) throws IOException {
        boolean changed = ftp.changeWorkingDirectory(path);
        if (changed) {
            return true;
        }
        String[] dirs = StringUtils.split(path, "/");
        if (null == dirs) {
            return true;
        }
        String pathname = "";
        for (String dir : dirs) {
            pathname = pathname + File.separator + dir;
            if (ftp.makeDirectory(pathname)) {
                logger.debug("create ftp directory with {}", pathname);
            }
        }

        return ftp.changeWorkingDirectory(path);
    }

    public ApiResponseResult downloadFile(String path, String fileName) {
        return download(path, fileName);
    }

    public ApiResponseResult downloadFile(String url, int port, String username, String password, String path, String fileName) {
        return download(url, port, username, password, path, fileName);
    }

    public ApiResponseResult remove(String path, String fileName) {
        String ftpPath = getPath(path);
        String pathName = ftpPath + File.separator + fileName;
        logger.debug("删除FTP 文件:" + pathName);
        FTPClient ftp = login();
        if (ftp == null)
            return ApiResponseResult.failure().message("链接FTP服务器失败");
        boolean flag = false;
        if (ftp != null) {
            try {
                if (!changeWorkingDirectory(ftp, new String(ftpPath.getBytes("gbk"), "iso-8859-1"))) {
                    logger.error("切换到目录" + ftpPath + "失败,请确认目录是否存在.");
                    return ApiResponseResult.failure().message("操作失败,请确认目录是否存在.");
                }

                flag = ftp.deleteFile(fileName);
            } catch (IOException e) {
                logger.error("文件删除出现异常", e);
            } finally {
                closeCon(ftp);
            }
        }
        return flag ? ApiResponseResult.success() : ApiResponseResult.failure().message("文件删除失败,请检查FTP是否连接正常.");
    }

    public ApiResponseResult rename(String path, String fileName, String newFileName) {
        String ftpPath = getPath(path);
        logger.debug("rename ftp 文件:" + ftpPath + " fileName:" + fileName + " new fileName:" + newFileName);
        FTPClient ftpClient = login();
        boolean flag = false;
        if (ftpClient != null) {
            try {
                if (!changeWorkingDirectory(ftpClient, new String(ftpPath
                        .getBytes("gbk"), "iso-8859-1"))) {
                    logger.error("切换到目录" + ftpPath + "失败,请确认目录是否存在.");
                    return ApiResponseResult.failure().message("操作失败,请确认目录是否存在.");
                }

                flag = ftpClient.rename(fileName, newFileName);
            } catch (IOException e) {
                logger.error("文件重命名出现异常", e);
                closeCon(ftpClient);
            }
        }
        return flag ? ApiResponseResult.success() : ApiResponseResult.failure().message("文件重命名失败,请检查FTP是否连接正常.");
    }

    private String getPath(String path) {
        String rootPath = this.env.getProperty("fs.ftp.rootPath", "");
        if (!StringUtils.startsWith(path, "/")) {
            path = File.separator + path;
        }
        path = rootPath + path;
        return path;
    }

    private class FtpConfig {
        public int port;
        public String url;
        public String username;
        public String password;

        private FtpConfig() {
        }
    }
}
