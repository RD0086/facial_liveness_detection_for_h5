package demo.example.demo.base.gateway;

import com.google.gson.reflect.TypeToken;
import demo.example.demo.base.gateway.entity.GatewayResponse;
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
 * 阿里云请求网关
 */
@Slf4j
public class ESAliyunGateway {
    private String appCode = null;

    /**
     * 实例化
     * @param appCode 获取 appCode,参考 : https://esandinfo.yuque.com/yv6e1k/ulp2ub/fs2mm48opwox3xc4?singleDoc#
     */
    public ESAliyunGateway(String appCode) {
        this.appCode = appCode;
    }

    /**
     * @param url           阿里云的URL地址
     * @param bizContent    业务数据
     * @return
     */
    public String sendToGateWay (String url, String bizContent) {
        GatewayResponse response = new GatewayResponse();
        String rspBody = null;
        try {
            do {
                // 对传入参数进行判断
                if (url == null || bizContent == null) {
                    response.initWithCode("-1", "无效传入参数");
                    break;
                }

                Map<String, String> header = new HashMap<String, String>();
                header.put("Authorization", "APPCODE " + appCode);
                header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                header.put("X-Ca-Nonce", UUID.randomUUID().toString());
                header.put("X-Ca-Nonce", UUID.randomUUID().toString());
                Map<String, String> query = new HashMap<String, String>();
                Map<String,String> body = GsonUtil.getAllJson().fromJson(bizContent, new TypeToken<HashMap<String,String>>(){}.getType());

                // 发送请求到网关
                HttpResponse httpResponse = HttpUtils.doPost(url, "", "POST", header, query, body);
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    log.error("服务器处理异常, 状态码 : {}", statusCode);
                }

                HttpEntity entity = httpResponse.getEntity();
                rspBody = EntityUtils.toString(entity);
                Map<String, String> rspHeader = new HashMap<String, String>();
                for (Header item : httpResponse.getAllHeaders()) {
                    rspHeader.put(item.getName(), item.getValue());
                }

                log.debug("服务器返回 body 数据: {} ",  rspBody);
                log.debug("服务器返回 header 数据: {} ", GsonUtil.getAllJson().toJson(rspHeader));
            } while (false);
        } catch (Exception e) {
            log.error("发生异常: ", e);
            response.initWithCode("-1", "发生异常 : " + e.getMessage());
        }

        return rspBody;
    }
}
