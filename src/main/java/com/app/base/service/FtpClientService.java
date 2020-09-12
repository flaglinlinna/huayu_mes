package com.app.base.service;

import java.io.InputStream;

import com.app.base.data.ApiResponseResult;

public abstract interface FtpClientService {
    public static final String FTP_ENCODE = "gbk";
    public static final String FTP_SERVER_ENCODE = "iso-8859-1";

    public abstract ApiResponseResult uploadFile(String paramString1, String paramString2, InputStream paramInputStream);

    public abstract ApiResponseResult uploadFile(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5, InputStream paramInputStream);

    public abstract ApiResponseResult downloadFile(String paramString1, String paramString2);

    public abstract ApiResponseResult downloadFile(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5);

    public abstract ApiResponseResult download(String paramString1, String paramString2);

    public abstract ApiResponseResult download(String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5);

    public abstract ApiResponseResult remove(String paramString1, String paramString2);

    public abstract ApiResponseResult rename(String paramString1, String paramString2, String paramString3);
}

