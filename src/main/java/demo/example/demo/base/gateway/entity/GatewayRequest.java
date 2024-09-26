package demo.example.demo.base.gateway.entity;

import com.google.gson.reflect.TypeToken;
import demo.example.demo.base.gateway.AlgorithmE;
import demo.example.demo.util.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.Map;

@Data
@Slf4j
public class GatewayRequest {
    /**
     * 用户appCode,参考 : https://esandinfo.yuque.com/yv6e1k/aa4qsg/cdwove
     */
    private String appCode;
    /**
     * 业务action（参考具体业务协议文档）
     */
    private String act;
    /**
     * 业务ID (随机数，保证其唯一)
     */
    private String bizNo;
    /**
     * 当前时间戳(时区为北京时间，防黑客重放攻击)，格式为：yyyyMMddHHmmssSSS
     */
    private String timestamp;
    /**
     * 签名类型，1：ECC,2:RSA,3:MD5
     */
    private String type;
    /**
     * 业务内容
     */
    private String bizContent;
    /**
     * 数据签名，可参考下面章节 “业务服务器签名验签”
     */
    private String sign;

    public String getAct() {
        String myAction = this.act;
        if (myAction != null) {
            myAction = myAction.trim();
            int indexStart = myAction.indexOf("{");
            int indexEnd = myAction.indexOf("}");

            if (indexStart==0 && indexEnd != -1) {
                myAction = myAction.substring(indexStart+1, indexEnd);
            }
        }

        return myAction;
    }

    public String getActExt(String relpaceStr[]) {
        String myAction = this.act;
        if (myAction != null && relpaceStr != null) {
            myAction = myAction.trim();
            for (String item : relpaceStr) {
                myAction = myAction.replace(item, "");
            }
        }

        return myAction;
    }

    /**
     * 对数据进行签名
     * @param key    签名密钥
     * @return 签名执行结果
     */
    public boolean genSign(String key) {
        boolean result = false;
        String content = null;
        content = bizContent;
        if (type.equals(AlgorithmE.MD5.getCode())) {
            if (bizContent != null && !bizContent.contains("\\\"")) {
                content = bizContent.replaceAll("\"","\\\\\"");
            }
            String signData = this.getBizNo() + "&" + this.getAppCode() + "&" + content + "&" + this.getTimestamp()+ "&"+ key;
            this.sign = HexUtil.encodeHexStr(MD5Util.getMD5Digest(signData.getBytes()));
            log.debug("签名数据 : {} , 签名: {}", signData, sign);
            result = true;
        } else {
            StringBuilder signDataBuilder = new StringBuilder();
            String replaceStr[] = {"{","}"};
            signDataBuilder.append(this.getActExt(replaceStr));
            signDataBuilder.append("&");
            signDataBuilder.append(this.getAppCode());
            signDataBuilder.append("&");
            signDataBuilder.append(this.getTimestamp());
            signDataBuilder.append("&");
            signDataBuilder.append(content);
            String signData = signDataBuilder.toString();
            log.debug("签名数据 : {}", signData);

            if (type.equals(AlgorithmE.ECC256.getCode())) {
                byte[] Hash = SHA256Util.digest(signData.getBytes());
                byte[] signature = ECCUtil.sign(Hash,key);
                this.sign = B64Util.encodeStr(signature);
                log.debug("签名: {}",sign);
                result = true;
            } else if (type.equals(AlgorithmE.RSA2048.getCode())) {
                byte[] signature = RSAUtil.signature(signData.getBytes(),key);
                this.sign = B64Util.encodeStr(signature);
                log.debug("签名: {}",sign);
                result = true;
            } else {
                log.error("不支持的签名类型 : {}", type);
            }
        }

        return result;
    }

    public String toJsonStr() {
        return GsonUtil.getAllJson().toJson(this);
    }

    public Map<String, String> toMap() {
        Type type = new TypeToken<Map<String, String>>() {}.getType();
        Map<String, String> map = GsonUtil.getAllJson().fromJson(toJsonStr(), type);

        return map;
    }
}
