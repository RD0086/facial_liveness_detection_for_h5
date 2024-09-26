package demo.example.demo.DTO;
import demo.example.demo.util.GsonUtil;
import lombok.Data;
@Data
public class LivingdetectVerifyResponse {
    /**
     * 错误码
     */
    private String code;
    /**
     * 结果描述
     */
    private String msg;
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 活体认证截取的最清晰的一张用户照片的下载url地址，有效期为 24 个小时。
     */
    private String bestImg;
    /**
     * 如果是需要多个照片返回时，不会返回bestImg，转而返回这个字段，一个数组
     */
    private String bestLmgArray;
    /**
     * 如果是需要多个照片返回时，前端不想要照片上传到oss时，会返回这个字段
     */
    private String beatImgBase64Array;
    /**
     * 活体检测状态码，LDT_SUCCESS： 活体检测成功，LDT_FAILED：活体检测失败
     */
    private String resultCode;

    public String toJsonStr(){return GsonUtil.getAllJson().toJson(this);}

    public static LivingdetectVerifyResponse fromJsonStr(String JsonStr){
        return GsonUtil.getAllJson().fromJson(JsonStr, LivingdetectVerifyResponse.class);
    }
}
