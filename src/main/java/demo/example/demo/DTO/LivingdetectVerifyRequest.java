package demo.example.demo.DTO;
import demo.example.demo.util.GsonUtil;
import lombok.Data;
@Data
public class LivingdetectVerifyRequest {
    /**
     * 请求action
     */
    public static String act = "livingdetection/livingdetect/verify";

    public static String ALIYUN_URL = "https://efaceid.market.alicloudapi.com/verify";

    /**
     * token（初始化时候返回）
     */
    private String token;
    /**
     * 认证数据（活体检测完成后从sdk返回）
     */
    private String verifyMsg;

    public String  toJsonStr(){return GsonUtil.getAllJson().toJson(this);}
}
