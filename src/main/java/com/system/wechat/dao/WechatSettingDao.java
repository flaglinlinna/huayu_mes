package com.system.wechat.dao;

import java.util.List;

import com.system.wechat.entity.WechatSetting;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface WechatSettingDao extends CrudRepository<WechatSetting, Long>, JpaSpecificationExecutor<WechatSetting>{
	public WechatSetting findById(long id);
    public List<WechatSetting> findAll();
}
