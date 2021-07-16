package com.web.project.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 在建项目档案信息表
 *
 */
@Entity(name = "ProjectDuty")
@Table(name = ProjectDuty.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProjectDuty extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;

    public static final String TABLE_NAME = "PLEE_HR_PROJECT_DUTY";
    /**
     * 项目编码
     */
    @ApiModelProperty(name = "projectCode", value = "项目编码")
    @Column(length = 50)
    protected String projectCode;

    /**
     * 项目名称
     */
    @ApiModelProperty(name = "projectName", value = "项目名称")
    @Column(length = 50)
    protected String projectName;

    /**
     * 项目经理
     */
    @ApiModelProperty(name = "manageName", value = "项目经理")
    @Column(length = 50)
    protected String manageName;


    /**
     * 开始时间(蓝图阶段)
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="startTime1",value="开始时间")
    protected Date startTime1;

    /**
     * 结束时间(蓝图阶段)
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="bsEndTime1",value="结束时间")
    protected Date endTime1;


    /**
     * 开始时间(开发测试阶段)
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="startTime2",value="开始时间")
    protected Date startTime2;

    /**
     * 结束时间(开发测试阶段)
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="endTime2",value="结束时间")
    protected Date endTime2;

    /**
     * 开始时间(上线实施)
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="startTime3",value="开始时间")
    protected Date startTime3;

    /**
     * 结束时间(上线实施)
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="endTime3",value="结束时间")
    protected Date endTime3;


    /**
     * 开始时间(验收结案阶段)
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="startTime4",value="开始时间")
    protected Date startTime4;

    /**
     * 结束时间（验收结案阶段）
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    @ApiModelProperty(name="endTime4",value="结束时间")
    protected Date endTime4;

    /**
     * 项目经理
     */
    @ApiModelProperty(name = "manageName", value = "项目经理")
    @Column(length = 50)
    protected String latestShow;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getManageName() {
        return manageName;
    }

    public void setManageName(String manageName) {
        this.manageName = manageName;
    }

    public Date getStartTime1() {
        return startTime1;
    }

    public void setStartTime1(Date startTime1) {
        this.startTime1 = startTime1;
    }

    public Date getEndTime1() {
        return endTime1;
    }

    public void setEndTime1(Date endTime1) {
        this.endTime1 = endTime1;
    }

    public Date getStartTime2() {
        return startTime2;
    }

    public void setStartTime2(Date startTime2) {
        this.startTime2 = startTime2;
    }

    public Date getEndTime2() {
        return endTime2;
    }

    public void setEndTime2(Date endTime2) {
        this.endTime2 = endTime2;
    }

    public Date getStartTime3() {
        return startTime3;
    }

    public void setStartTime3(Date startTime3) {
        this.startTime3 = startTime3;
    }

    public Date getEndTime3() {
        return endTime3;
    }

    public void setEndTime3(Date endTime3) {
        this.endTime3 = endTime3;
    }

    public Date getStartTime4() {
        return startTime4;
    }

    public void setStartTime4(Date startTime4) {
        this.startTime4 = startTime4;
    }

    public Date getEndTime4() {
        return endTime4;
    }

    public void setEndTime4(Date endTime4) {
        this.endTime4 = endTime4;
    }

    public String getLatestShow() {
        return latestShow;
    }

    public void setLatestShow(String latestShow) {
        this.latestShow = latestShow;
    }


    //    @Override
//    public String toString() {
//        final StringBuffer sb = new StringBuffer();
//        sb.append("异常代码:").append(this.abnormalCode);
//        sb.append("异常类型:").append(this.abnormalType);
//        return sb.toString();
//    }
}
