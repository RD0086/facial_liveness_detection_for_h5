package demo.example.demo.base.gateway.entity;

import demo.example.demo.base.gateway.AlgorithmE;
import demo.example.demo.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
@Data
@Slf4j
public class GatewayResponse {
    /**
     * 协议版本
     */
    private String ver;
    /**
     * 响应状态码
     */
    private String code;
    /**
     * 结果描述
     */
    private String msg;
    /**
     *
     */
    private String gwRequestID;
    /**
     * 业务action
     */
    private String act;
    /**
     * 响应时间戳
     */
    private String timestamp;
    /**
     * 业务响应数据
     */
    private String bizContent;
    /**
     * 签名数据
     */
    private String sign;
    /**
     * 签名类型
     */
    private String signType;
    /**
     * 验签结果
     */
    private boolean signVerifyResult = false;

    public void initWithCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
        if ("-1".equals(code)) {
            log.error("本地执行发生异常 : ", msg);
        }
    }

    public boolean verifySign(String key) {
        boolean result = false;
        if (AlgorithmE.MD5.getCode().equals(signType)) { // MD5的签名
            StringBuilder builder = new StringBuilder();
            builder.append(this.getAct());
            builder.append("&");
            builder.append(this.getTimestamp());
            builder.append("&");
            builder.append(this.getCode());
            if (StringUtils.isNotEmpty(this.getBizContent())) {
                builder.append("&");
                builder.append(this.getBizContent());
            }
            builder.append("&");
            builder.append(key);
            byte[] signedData = builder.toString().getBytes();
            log.debug("验签数据: {}", new String(signedData));

            byte[] signBuff = MD5Util.getMD5Digest(signedData);
            String signHexLowerCase = HexUtil.encodeHexStr(signBuff).toLowerCase();
            if (signHexLowerCase.equals(sign.toLowerCase())) {
                result = true;
            }
        } else {
            if (AlgorithmE.ECC256.getCode().equals(signType)){//ECC的签名
                key = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE1Tkqczk9t4xyoJQOOR4YP5uzNCbXHYekYZPo2/5LWG05vtr8sPabMg8LiUpE9QOtSxSq7oT5NclGOeSIlJmqFw==";
                StringBuilder signDataBuilder = new StringBuilder();
                signDataBuilder.append(this.getAct());
                signDataBuilder.append("&");
                signDataBuilder.append(this.getTimestamp());
                signDataBuilder.append("&");
                signDataBuilder.append(this.getCode());
                if (this.getBizContent() !=null ) {
                    signDataBuilder.append("&");
                    signDataBuilder.append(this.getBizContent());
                }
                String signedData = signDataBuilder.toString();
                log.debug("验签数据: {}", signedData);
                byte[] signedDataHash = SHA256Util.digest(signedData.getBytes());
                byte[] signatureBuff = B64Util.decode(sign);
                result = ECCUtil.verify(signatureBuff,signedDataHash,key);

            } else if (AlgorithmE.RSA2048.getCode().equals(signType)) {
                key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3e5p5BIO9WBsTw5yCYm01vaMrmSn3311q7CmkS6DCyXf4P0I5CXX6LtdkXA94ON/SwBWug0wNTvJaiA7MsGWyfD4uPJyFH9OiyG1zJf/dbCoU2LNiurB/j3B+0u1GvDm8xF38sfzV8YkTxzvXmLbs7/VRlbVVoGNZyLECbe+mSsLWNm/PSmC9yMpGt69tTcR/fg/NXRkWodMqPlIUdM47Yh8mUDPH97+gkr08lfwBAG8jl0YUT700IfeV+va3lR9GQNej5vxIuyplliBDQAdJDlTJ7r0BUzM7R8DEjNnOGNxpYdAWOrAJVTUzIKH6JZ3ktM0E8aaTeCW4k0d2VgHxQIDAQAB";
                StringBuilder signDataBuilder = new StringBuilder();
                signDataBuilder.append(this.getAct());
                signDataBuilder.append("&");
                signDataBuilder.append(this.getTimestamp());
                signDataBuilder.append("&");
                signDataBuilder.append(this.getCode());
                if (this.getBizContent() !=null ) {
                    signDataBuilder.append("&");
                    signDataBuilder.append(this.getBizContent());
                }
                String signedData = signDataBuilder.toString();
                log.debug("验签数据: {}", signedData);
                result = RSAUtil.verify(signedData.getBytes(),key,sign);

            } else {
                log.error("不支持此签名算法 : {}", signType);
            }
        }

        this.setSignVerifyResult(result);
        return result;
    }

    public String toJsonStr() {
        return GsonUtil.getAllJson().toJson(this);
    }

    public static GatewayResponse fromJsonStr(String jsonStr) {
        return GsonUtil.getAllJson().fromJson(jsonStr, GatewayResponse.class);
    }
}
