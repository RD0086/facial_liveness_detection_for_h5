package demo.example.demo.base.gateway;

import com.google.gson.reflect.TypeToken;
import demo.example.demo.base.gateway.entity.GatewayRequest;
import demo.example.demo.base.gateway.entity.GatewayResponse;
import demo.example.demo.util.CommUtil;
import demo.example.demo.util.GsonUtil;
import demo.example.demo.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 一砂请求网关
 */
@Slf4j
public class ESGateway {
    /**
     * 网关配置的算法
     */
    private AlgorithmE algo;
    /**
     * 网关密钥
     */
    private String key;
    /**
     * 用户的APPCODE
     */
    private String appCode;

    /**
     * 实例化
     * @param appCode 用户appCode,参考 : https://esandinfo.yuque.com/yv6e1k/aa4qsg/cdwove
     * @param algo 网关配置的算法
     * @param key 网关密钥
     */
    public ESGateway(String appCode, AlgorithmE algo, String key) {
        this.appCode = appCode;
        this.algo = algo;
        this.key = key;
    }

    /**
     * @param act           业务action（参考具体业务协议文档）
     * @param bizContent    业务数据，（参考具体业务协议文档）(json字符串)
     * @return
     */
    public GatewayResponse sendToGateWay (String act, String bizContent) {
        GatewayResponse response = new GatewayResponse();
        try {
            do {
                // 对传入参数进行判断
                if (act == null || bizContent == null) {
                    response.initWithCode("-1", "无效传入参数");
                    break;
                }

                // 构造请求报文
                GatewayRequest request = new GatewayRequest();
                request.setAct(act);
                request.setAppCode(appCode);
                request.setBizNo(CommUtil.getTimeUUID());
                request.setType(algo.getCode());
                request.setTimestamp(CommUtil.getCurrentTimeStr());
                request.setBizContent(bizContent);
                // 对数据进行签名
                boolean signResult = request.genSign(key);
                if (!signResult) {
                    response.initWithCode("-1", "数据签名失败");
                    break;
                }

                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                Map<String, String> query = new HashMap<String, String>();

                // 发送请求到网关
                HttpResponse httpResponse = HttpUtils.doPost("https://edis.esandcloud.com", "/gateways", "POST", header, query, request.toJsonStr());
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    log.error("服务器处理异常, 状态码 : {}", statusCode);
                }

                HttpEntity entity = httpResponse.getEntity();
                String rspBody = EntityUtils.toString(entity);
                Map<String, String> rspHeader = new HashMap<String, String>();
                for (Header item : httpResponse.getAllHeaders()) {
                    rspHeader.put(item.getName(), item.getValue());
                }

                log.debug("服务器返回 body 数据: {} ",  rspBody);
                log.debug("服务器返回 header 数据: {} ", GsonUtil.getAllJson().toJson(rspHeader));
                // 对返回的数据进行解析
                if (rspBody != null) {
                    response = GatewayResponse.fromJsonStr(rspBody);
                }

                // 对返回的报文进行验签
                if (response!=null && response.getCode().equals("0000")) {
                    boolean signVerifyResult = response.verifySign(key);
                    log.debug("验签结果: {}", signVerifyResult);
                }
            } while (false);
        } catch (Exception e) {
            log.error("发生异常: ", e);
            response.initWithCode("-1", "发生异常 : " + e.getMessage());
        }

        return response;
    }
}
