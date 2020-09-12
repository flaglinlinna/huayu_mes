package com.web.basic.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.system.user.entity.SysUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author 
 */
@Entity
@Table(name = TodoInfo.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class TodoInfo extends BaseEntity {
    private static final long serialVersionUID = 7151771262953316256L;
    public static final String TABLE_NAME = "t_todo_info";

    /**
     * 用户ID
     */
    @ApiModelProperty(name="bsUserId",value="用户ID")
    @Column
    protected Long bsUserId;

//    @ApiModelProperty(name="todoerBy",hidden=true,value="待办人--user")
//    @ManyToOne
//    @JoinColumn(name = "bsUserId", insertable = false, updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    protected SysUser todoerBy;

    /**
     * 页面路由
     */
    @ApiModelProperty(name="bsRouter",required=true,value="页面路由")
    @NotNull
    @Column(length = 50)
    protected String bsRouter;

    /**
     * 状态
     */
    @ApiModelProperty(name="bsStatus",required=true,value="状态")
    @Column
    protected int bsStatus;

    /**
     * 标题
     */
    @ApiModelProperty(name="bsTitle",required=true,value="标题")
    @Column(length = 200)
    protected String bsTitle;

    /**
     * 内容
     */
    @ApiModelProperty(name="bsContent",value="内容")
    @Column(length = 200)
    protected String bsContent;

    /**
     * 备注
     */
    @ApiModelProperty(name="bsRemark",value="备注")
    @Column(length = 500)
    protected String bsRemark;

    /**
     * 类型
     */
    @ApiModelProperty(name="bsType",required=true,value="类型")
    @NotNull
    @Column
    protected int bsType;

    /**
     * 优先级
     */
    @ApiModelProperty(name="bsPriority",required=true,value="优先级")
    @Column
    protected int bsPriority;

    /**
     * 开始日期
     */
    @ApiModelProperty(name="bsStartTime",value="开始日期")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    protected Date bsStartTime;

    /**
     * 截止日期
     */
    @ApiModelProperty(name="bsEndTime",value="截止日期")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    protected Date bsEndTime;

    /**
     * 关联ID
     */
    @ApiModelProperty(name="bsReferId",required=true,value="关联ID")
    @NotNull
    @Column
    protected Long bsReferId;

    /**
     * 扩展内容
     */
    @ApiModelProperty(name="bsExtend",required=true,value="扩展内容")
    @Column
    protected Long bsExtend;

    /**
     * 创建人
     */
    @ApiModelProperty(name="pkCreatedBy",hidden=true,value="创建人")
    @Column
    protected Long pkCreatedBy;

    /**
     * 修改人
     */
    @ApiModelProperty(name="pkModifiedBy",hidden=true,value="修改人")
    @Column
    protected Long pkModifiedBy;

//    @ApiModelProperty(name="createdBy",hidden=true,value="创建人--user")
//    @ManyToOne
//    @JoinColumn(name = "pkCreatedBy", insertable = false, updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    protected SysUser createdBy;

//    @ApiModelProperty(name="modifiedBy",hidden=true,value="修改人--user")
//    @ManyToOne
//    @JoinColumn(name = "pkModifiedBy", insertable = false, updatable = false)
//    @NotFound(action = NotFoundAction.IGNORE)
//    protected SysUser modifiedBy;

    public Long getBsUserId() {
        return bsUserId;
    }

    public void setBsUserId(Long bsUserId) {
        this.bsUserId = bsUserId;
    }

//    public SysUser getTodoerBy() {
//        return todoerBy;
//    }
//
//    public void setTodoerBy(SysUser todoerBy) {
//        this.todoerBy = todoerBy;
//    }

    public String getBsRouter() {
        return bsRouter;
    }

    public void setBsRouter(String bsRouter) {
        this.bsRouter = bsRouter;
    }

    public int getBsStatus() {
        return bsStatus;
    }

    public void setBsStatus(int bsStatus) {
        this.bsStatus = bsStatus;
    }

    public String getBsTitle() {
        return bsTitle;
    }

    public void setBsTitle(String bsTitle) {
        this.bsTitle = bsTitle;
    }

    public String getBsContent() {
        return bsContent;
    }

    public void setBsContent(String bsContent) {
        this.bsContent = bsContent;
    }

    public String getBsRemark() {
		return bsRemark;
	}

	public void setBsRemark(String bsRemark) {
		this.bsRemark = bsRemark;
	}

	public int getBsType() {
        return bsType;
    }

    public void setBsType(int bsType) {
        this.bsType = bsType;
    }

    public int getBsPriority() {
        return bsPriority;
    }

    public void setBsPriority(int bsPriority) {
        this.bsPriority = bsPriority;
    }

    public Date getBsStartTime() {
        return bsStartTime;
    }

    public void setBsStartTime(Date bsStartTime) {
        this.bsStartTime = bsStartTime;
    }

    public Date getBsEndTime() {
        return bsEndTime;
    }

    public void setBsEndTime(Date bsEndTime) {
        this.bsEndTime = bsEndTime;
    }

    public Long getBsReferId() {
        return bsReferId;
    }

    public void setBsReferId(Long bsReferId) {
        this.bsReferId = bsReferId;
    }

    public Long getBsExtend() {
        return bsExtend;
    }

    public void setBsExtend(Long bsExtend) {
        this.bsExtend = bsExtend;
    }

    public Long getPkCreatedBy() {
        return pkCreatedBy;
    }

    public void setPkCreatedBy(Long pkCreatedBy) {
        this.pkCreatedBy = pkCreatedBy;
    }

    public Long getPkModifiedBy() {
        return pkModifiedBy;
    }

    public void setPkModifiedBy(Long pkModifiedBy) {
        this.pkModifiedBy = pkModifiedBy;
    }
//
//    public SysUser getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(SysUser createdBy) {
//        this.createdBy = createdBy;
//    }

//    public SysUser getModifiedBy() {
//        return modifiedBy;
//    }
//
//    public void setModifiedBy(SysUser modifiedBy) {
//        this.modifiedBy = modifiedBy;
//    }
}
