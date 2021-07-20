package com.system.wechat.service.internal;



import com.alibaba.fastjson.JSONObject;
import com.system.wechat.dao.WechatSettingDao;
import com.system.wechat.entity.WechatSetting;
import com.system.wechat.service.WechatSettingService;
import com.utils.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.base.data.ApiResponseResult;
import com.system.todo.dao.TodoInfoDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class WechatSettingImpl implements WechatSettingService {
    @Autowired
    private WechatSettingDao wechatSettingDao;

    @Override
    public ApiResponseResult getToken() throws Exception {
        List<WechatSetting> list = wechatSettingDao.findAll();
        if(list.size()>0){
            WechatSetting setting = list.get(0);
            if(setting.getEffectTime().before(new Date())){
               String requestDate = HttpRequestUtil.sendGet(setting.getTokenUrl(),"corpid="+setting.getCorpId()+"&corpsecret="+setting.getCorpSecret());
                JSONObject json = JSONObject.parseObject(requestDate);
                if(json.get("access_token")!=null){
                    setting.setAccessToken(json.get("access_token").toString());
                    Calendar newTime = Calendar.getInstance();
                    newTime.setTime(new Date());
                    newTime.add(Calendar.SECOND,Integer.parseInt(json.get("expires_in").toString()));
                    setting.setEffectTime(newTime.getTime());
                    wechatSettingDao.save(setting);
                }
                System.out.println(json);
            }
            return ApiResponseResult.success().data(setting);
        }
        return ApiResponseResult.failure();
    }

    @Override
    public ApiResponseResult getDepartment() throws Exception {
        ApiResponseResult data = this.getToken();
        if(data.isResult()){
          WechatSetting setting =(WechatSetting)data.getData();
            String requestDate = HttpRequestUtil.sendGet("https://qyapi.weixin.qq.com/cgi-bin/department/list","access_token="+setting.getAccessToken());
            JSONObject json = JSONObject.parseObject(requestDate);
            if(json.get("errmsg")!=null){
                return ApiResponseResult.success().data(json);
            }
        }
        return ApiResponseResult.failure();
    }

    @Override
    public ApiResponseResult getUserByDept(String departId) throws Exception {
        ApiResponseResult data = this.getToken();
        if(data.isResult()){
            WechatSetting setting =(WechatSetting)data.getData();
            String requestDate = HttpRequestUtil.sendGet("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist","access_token="+setting.getAccessToken()+"&department_id="+departId+"&fetch_child=1");
            JSONObject json = JSONObject.parseObject(requestDate);
            if(json.get("errmsg")!=null){
                return ApiResponseResult.success().data(json);
            }
        }
        return ApiResponseResult.failure();
    }
}
