package demo.example.demo.DTO;

import demo.example.demo.util.GsonUtil;
import lombok.Data;

/**
 * 获取人脸比对结果，响应对象
 */
@Data
public class FaceContrastVerifyRequest {
    /**
     * 请求action
     */
    public static String act = "livingdetection/faceContrast/verify";
    
    public static String ALIYUN_URL = "https://efaceid.market.alicloudapi.com/verify";
    /**
     * 比对照片数据(base64字符串)
     */
    private String image;
    /**
     * 用户注册ID
     */
    private String uuid;
    /**
     * 比对照片的URL地址 (确保地址公网可访问)
     */
    private String imageUrl;
    /**
     * 认证token(初始化时候返回)
     */
    private String token;
    /**
     * 认证数据（认证完成后从sdk返回）
     */
    private String verifyMsg;

    public String toJsonStr(){return GsonUtil.getAllJson().toJson(this);}
}
