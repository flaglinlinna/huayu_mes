package com.system.wechat.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 企业wechat信息设置
 */
@Entity
@Table(name = WechatSetting.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class WechatSetting extends BaseEntity {
    private static final long serialVersionUID = 7151771262953316256L;
    public static final String TABLE_NAME = "t_wechat_setting";

    /**
     * token
     */
    @ApiModelProperty(name="access_token",value="token")
    @Column(length = 500)
    protected String accessToken;

    /**
     * 企业ID
     */
    @ApiModelProperty(name="企业ID",value="corpid")
    @Column(length = 100)
    protected String corpId;

    /**
     * 应用密钥
     */
    @ApiModelProperty(name="企业ID",value="corpSecret")
    @Column(length = 100)
    protected String corpSecret;

    /**
     * 应用ID
     */
    @ApiModelProperty(name="应用ID",value="agentId")
    @Column(length = 100)
    protected String agentId;

    /**
     * 更新tokenUrl
     */
    @ApiModelProperty(name="更新tokenUrl",value="tokenUrl")
    @Column(length = 100)
    protected String tokenUrl;


    /**
     * 有效时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @ApiModelProperty(name="effectTime",value="有效时间")
    protected Date effectTime;



    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Date getEffectTime() {
        return effectTime;
    }

    public void setEffectTime(Date effectTime) {
        this.effectTime = effectTime;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
}
