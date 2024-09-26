package demo.example.demo.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import demo.example.demo.DTO.LivingdetectVerifyRequest;
import demo.example.demo.base.gateway.AlgorithmE;
import demo.example.demo.base.gateway.ESAliyunGateway;
import demo.example.demo.base.gateway.ESGateway;
import demo.example.demo.base.gateway.entity.GatewayResponse;
import demo.example.demo.util.CommUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 人脸活体检测DEMO
 */
@Slf4j
@Controller
public class FLDTController {

    /**
     * 人脸活体检测入口
     * @param model
     * @return
     */
    @GetMapping("/fldt/index")
    public String indexTo(Model model) {
        // 参考文档构造 URL : https://esandinfo.yuque.com/yv6e1k/aa4qsg/mbexd4
        String bizId = CommUtil.getTimeUUID(); // 业务ID
        String livingType = "1"; // 活体动作类型
        String returnUrl = "/fldt/returnURL"; // 回调URL
        String livingPageStyle = ""; // 自定义样式
        boolean needVideo = false; // 是否录制并返回视频
        int takeImageNumber = 1;// 采集照片数量
        int strategy = 1; // 活体检测认证策略

        model.addAttribute("bizId", bizId);
        model.addAttribute("livingType", livingType);
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("livingPageStyle", livingPageStyle);
        model.addAttribute("needVideo", needVideo);
        model.addAttribute("takeImageNumber", takeImageNumber);
        model.addAttribute("strategy", strategy);

        return "fldt/index";
    }

    @GetMapping("/fldt/returnURL")
    public String resultPage(String code, String msg, String token, String verifyMsg,Model model) {
        model.addAttribute("verifyMsg",verifyMsg);
        // TODO 替换成你自己的appcode, 获取APPCODE  可参考 https://esandinfo.yuque.com/yv6e1k/ulp2ub/fs2mm48opwox3xc4?singleDoc#
        String ALIYUN_appCode = "TODO";//阿里云网关APPCODE

        // 从一砂云接入, 可参考文档： https://esandinfo.yuque.com/yv6e1k/aa4qsg/ghtqp7
        String ES_appCode = "TODO";//一砂云APPCODE
        String key = "TODO";// 一砂云网关密钥
        AlgorithmE algo = AlgorithmE.MD5;

        LivingdetectVerifyRequest livingdetectVerifyRequest = new LivingdetectVerifyRequest();
        livingdetectVerifyRequest.setToken(token);
        livingdetectVerifyRequest.setVerifyMsg(verifyMsg);

        if(ES_appCode == "" || ES_appCode == "TODO"){
            ESAliyunGateway gateway = new ESAliyunGateway(ALIYUN_appCode);
            String rspMsg = gateway.sendToGateWay(LivingdetectVerifyRequest.ALIYUN_URL,livingdetectVerifyRequest.toJsonStr());
            log.info("服务器返回内容为: {}", rspMsg);
            model.addAttribute("rspMsg",rspMsg);
           // return rspMsg;
        }
        else {
            ESGateway esGateway = new ESGateway(ES_appCode, algo, key);
            GatewayResponse gatewayResponse = esGateway.sendToGateWay(LivingdetectVerifyRequest.act, livingdetectVerifyRequest.toJsonStr());
            log.info("服务器端返回：{}",gatewayResponse.toJsonStr());
           // return gatewayResponse.toJsonStr();
            model.addAttribute("rspMsg",gatewayResponse.toJsonStr());
        }
        // TODO 查询结果，并展示
        return "/fldt/returnURL";

    }

}
