package demo.example.demo.DTO;

import demo.example.demo.util.GsonUtil;
import lombok.Data;

/**
 * 获取人脸比对结果，响应对象
 */
@Data
public class FaceContrastVerifyResponse {
    /**
     * 错误码
     */
    private String code;
    /**
     * 活体检测结果
     */
    private String IdtResult;
    /**
     * 人脸比对结果
     */
    private String faceVerifyResult;
    /**
     * 结果描述
     */
    private String msg;
    /**
     * 人脸比对是否通过
     */
    private String passed;
    /**
     * 人脸比对分数（通常认为大于75分为同一个人）
     */
    private String confident;
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 认证token
     */
    private String token;
    /**
     * 活体检测的截图
     */
    private String bestLmg;
    /**
     * 活体检测类型
     */
    private String livingType;
    /**
     *
     * 人脸遮挡检测结果：
     *     Y：无遮挡
     *     N：遮挡
     */
    private String occlusionResult;
    /**
     * 遮挡检测分数
     */
    private String occlusionConfidence;
    /**
     *名族，ocr模式才有值
     */
    private String nationality;

    /**
     * 住址, ocr 模式才有值
     */
    private String address;

    /**
     *
     * 身份证签发机构, ocr 模式才有值
     */
    private String issuedBy;

    /**
     *  有效期(如2010.11.13-2020.11.13), ocr 模式才有值
     */
    private String validDate;

    /**
     * 身份证正面照片(国徽面)下载URL, 24 小时有效
     */
    private String idFront;

    /**
     * 身份证反面(人像面)照片下载URL, 24 小时有效
     */
    private String idBack;
    public String toJsonStr(){return GsonUtil.getAllJson().toJson(this);}

    public static FaceContrastVerifyResponse fromJsonStr(String JsonStr){
        return GsonUtil.getAllJson().fromJson(JsonStr, FaceContrastVerifyResponse.class);
    }


}

