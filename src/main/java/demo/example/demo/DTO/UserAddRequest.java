package demo.example.demo.DTO;

import demo.example.demo.util.GsonUtil;
import lombok.Data;

@Data
public class UserAddRequest {
    /**
     * 请求action
     */
    public static String act = "livingdetection/faceContrast/user/add";

    public static String ALIYUN_URL = "https://efaceid.market.alicloudapi.com/verify";
    /**
     * 用户唯一ID
     */
    private String uuid;
    /**
     * 图片BASE64字符串，与verifyMsg, imageURL三选其一
     */
    private String image;
    /**
     * 图片URL地址，与image, verifyMsg三选其一
     */
    private String imageURL;
    /**
     * 认证TOKEN （选择verify时传入)
     */
    private String token;
    /**
     * 活体检测数据，与image, imageURL三选其一
     */
    private String verifyMsg;

    public String toJsonStr(){return GsonUtil.getAllJson().toJson(this);}
}

