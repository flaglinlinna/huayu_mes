package com.app.config.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.system.user.entity.SysUser;

import org.springframework.stereotype.Service;


/**
 * @author jinbin
 * @date 2018-07-08 21:04
 */
@Service("TokenService")
public class TokenService {
//    public String getToken(SysUser user) {
//        String token="";
//        token= JWT.create().withAudience(user.getFcode())// 将 user id 保存到 token 里面
//                .sign(Algorithm.HMAC256(user.getFpassword()));// 以 password 作为 token 的密钥
//        return token;
//    }
    public String getToken(SysUser user) {
        String token="";
        token= JWT.create().withAudience(user.getUserCode())// 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }
}
