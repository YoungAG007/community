package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    //MD5加密 只能加密不能解密
    //在密码的后面加上一个随机字符串 再进行加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){ //如果是空串 空格 null 都是空的都返回null
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
