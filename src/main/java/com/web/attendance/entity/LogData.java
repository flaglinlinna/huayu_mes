package com.web.attendance.entity;

/**
 * 获取缓存中的考勤数据。配合readGeneralLogData / readLastestLogData使用。
 *      *
 *      * @return 返回的map中，包含以下键值：
 *      * "EnrollNumber"   人员编号
 *      * "Time"           考勤时间串，格式: yyyy-MM-dd HH:mm:ss
 *      * "VerifyMode"     验证方式  1：指纹 2：面部识别
 *      * "InOutMode"      考勤状态 0：上班 1：下班 2：外出 3：外出返回 4：加班签到 5：加班签退
 *      * "Year"          考勤时间：年
 *      * "Month"         考勤时间：月
 *      * "Day"           考勤时间：日
 *      * "Hour"            考勤时间：时
 *      * "Minute"        考勤时间：分
 *      * "Second"        考勤时间：秒
 *
 */
public class LogData {
    private String Year;
    private String Hour;
    private String InOutMode;
    private String Time;
    private String InOutMode1;
    private String Second;
    private String Minute;
    private String EnrollNumber;
    private String Day;
    private String Month;
    private String VerifyMode;

    public LogData(String year, String hour, String inOutMode, String time, String inOutMode1, String second, String minute, String enrollNumber, String day, String month, String verifyMode) {
        Year = year;
        Hour = hour;
        InOutMode = inOutMode;
        Time = time;
        InOutMode1 = inOutMode1;
        Second = second;
        Minute = minute;
        EnrollNumber = enrollNumber;
        Day = day;
        Month = month;
        VerifyMode = verifyMode;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getInOutMode() {
        return InOutMode;
    }

    public void setInOutMode(String inOutMode) {
        InOutMode = inOutMode;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getInOutMode1() {
        return InOutMode1;
    }

    public void setInOutMode1(String inOutMode1) {
        InOutMode1 = inOutMode1;
    }

    public String getSecond() {
        return Second;
    }

    public void setSecond(String second) {
        Second = second;
    }

    public String getMinute() {
        return Minute;
    }

    public void setMinute(String minute) {
        Minute = minute;
    }

    public String getEnrollNumber() {
        return EnrollNumber;
    }

    public void setEnrollNumber(String enrollNumber) {
        EnrollNumber = enrollNumber;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getVerifyMode() {
        return VerifyMode;
    }

    public void setVerifyMode(String verifyMode) {
        VerifyMode = verifyMode;
    }

    public LogData(){};
}
