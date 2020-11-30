package com.web.basic.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 线体基础信息表
 *
 */
@Entity(name = "LinerImg")
@Table(name = LinerImg.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class LinerImg extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "MES_BASE_LINER_IMG";

    /**
     * 组长组织架构ID
     */
    @ApiModelProperty(name = "orgIdLiner", value = "组长组织架构ID")
    @Column(length = 50)
    protected String orgIdLiner;

    /**
     * 组长照片
     */
    @ApiModelProperty(name = "imgLiner", value = "组长照片")
    @Column(length = 50)
    protected String imgLiner;

    /**
     * QC员工ID
     */
    @ApiModelProperty(name = "empIdQc", value = "QC员工ID")
    @Column(length = 50)
    protected String empIdQc;

    /**
     * QC照片
     */
    @ApiModelProperty(name = "imgQc", value = "QC照片")
    @Column(length = 50)
    protected String imgQc;

    /**
     * 工程员ID
     */
    @ApiModelProperty(name = "empIdPe", value = "工程员ID")
    @Column(length = 50)
    protected String empIdPe;

    /**
     * 工程员照片
     */
    @ApiModelProperty(name = "imgPe", value = "工程员照片")
    @Column(length = 50)
    protected String imgPe;

    /**
     * 生产线ID
     */
    @ApiModelProperty(name = "lineId", value = "生产线ID")
    @Column(length = 50)
    protected String lineId;



    /**
     * 组长员工ID
     */
    @ApiModelProperty(name = "empIdLiner", value = "组长员工ID")
    @Column(length = 50)
    protected String empIdLiner;

    /**
     * 是否有效 0无效1有效
     */
    @ApiModelProperty(name = "enabled", value = "是否有效")
    @Column(length = 1)
    protected Integer enabled=1;

    public String getOrgIdLiner() {
        return orgIdLiner;
    }

    public void setOrgIdLiner(String orgIdLiner) {
        this.orgIdLiner = orgIdLiner;
    }

    public String getImgLiner() {
        return imgLiner;
    }

    public void setImgLiner(String imgLiner) {
        this.imgLiner = imgLiner;
    }

    public String getEmpIdQc() {
        return empIdQc;
    }

    public void setEmpIdQc(String empIdQc) {
        this.empIdQc = empIdQc;
    }

    public String getImgQc() {
        return imgQc;
    }

    public void setImgQc(String imgQc) {
        this.imgQc = imgQc;
    }

    public String getEmpIdPe() {
        return empIdPe;
    }

    public void setEmpIdPe(String empIdPe) {
        this.empIdPe = empIdPe;
    }

    public String getImgPe() {
        return imgPe;
    }

    public void setImgPe(String imgPe) {
        this.imgPe = imgPe;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getEmpIdLiner() {
        return empIdLiner;
    }

    public void setEmpIdLiner(String empIdLiner) {
        this.empIdLiner = empIdLiner;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("组长ID:").append(this.orgIdLiner);
//        sb.append(",imgLiner:").append(this.imgLiner);
        sb.append(",QC员工ID:").append(this.empIdQc);
//        sb.append(",imgQc:").append(this.imgQc);
        sb.append(",工程员ID:").append(this.empIdPe);
//        sb.append(",imgPe:").append(this.imgPe);
        sb.append(",生产线ID:").append(this.lineId);
        sb.append(",组长员工ID:").append(this.empIdLiner);
        sb.append(",状态:").append(this.enabled==0?"无效":"有效");
        sb.append(";");
        return sb.toString();
    }
}
