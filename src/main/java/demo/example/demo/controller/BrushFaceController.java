package demo.example.demo.controller;

import demo.example.demo.DTO.FaceContrastVerifyRequest;
import demo.example.demo.DTO.UserAddRequest;
import demo.example.demo.base.gateway.AlgorithmE;
import demo.example.demo.base.gateway.ESAliyunGateway;
import demo.example.demo.base.gateway.ESGateway;
import demo.example.demo.base.gateway.entity.GatewayResponse;
import demo.example.demo.util.CommUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 刷脸认证DEMO
 */
@Slf4j
@Controller
public class BrushFaceController {
    /**
     * 活体检测入口
     * @param model
     * @return
     */
    @GetMapping("/fldt/BrushFace_index")
    public String indexTo(Model model) {
        // 参考文档构造 URL : https://esandinfo.yuque.com/yv6e1k/aa4qsg/omb64l
        String bizId = CommUtil.getTimeUUID(); // 业务ID
        /**
         * 业务类型
         * 1.活体检测  (默认) 2.全平台实名认证 3.H5实名认证(兼容版) 4.全平台刷脸认证 5.人脸搜索 6.非大陆公民实名认证 7.非大陆实人认证兼容版
         */
        String bizType = "1";//刷脸认证
        String livingType = "1"; // 活体动作类型
        String returnUrl = "/fldt/BrushFace_returnURL"; // 回调URL
        String livingPageStyle = ""; // 自定义样式

        model.addAttribute("bizId", bizId);
        model.addAttribute("bizType", bizType);
        model.addAttribute("livingType", livingType);
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("livingPageStyle", livingPageStyle);

        return "/fldt/BrushFace_index";
    }

    @GetMapping  ("/fldt/BrushFace_returnURL")
    public String getResult(String code,String msg, String token,  String verifyMsg, Model model) {
        // TODO 替换成你自己的appcode, 获取APPCODE  可参考 https://esandinfo.yuque.com/yv6e1k/ulp2ub/fs2mm48opwox3xc4?singleDoc#
        String ALIYUN_appCode = "TODO";//阿里云网关APPCODE

        // 从一砂云接入, 可参考文档： https://esandinfo.yuque.com/yv6e1k/aa4qsg/ghtqp7
        String ES_appCode = "";//一砂云APPCODE
        String key = "";// 一砂云网关密钥
        AlgorithmE algo = AlgorithmE.MD5;

        FaceContrastVerifyRequest faceContrastVerifyRequest = new FaceContrastVerifyRequest();
        faceContrastVerifyRequest.setUuid("123"); //用户唯一ID
        faceContrastVerifyRequest.setToken(token);
        faceContrastVerifyRequest.setVerifyMsg(verifyMsg);

        if(ES_appCode == "" || ES_appCode == "TODO"){
            ESAliyunGateway gateway = new ESAliyunGateway(ALIYUN_appCode);
           String rspMsg = gateway.sendToGateWay(FaceContrastVerifyRequest.ALIYUN_URL,faceContrastVerifyRequest.toJsonStr());
            model.addAttribute("rspMsg",rspMsg);
            log.info("服务器返回内容为: {}", rspMsg);
        }
        else {
            ESGateway esGateway = new ESGateway(ES_appCode, algo, key);
            GatewayResponse gatewayResponse = esGateway.sendToGateWay(FaceContrastVerifyRequest.act, faceContrastVerifyRequest.toJsonStr());
            log.info("服务器端返回：{}",gatewayResponse.toJsonStr());
            model.addAttribute("rspMsg",gatewayResponse.toJsonStr());
        }
        return "/fldt/BrushFace_returnURL";

    }

    /**
     用户注册
     */
    @PostMapping("/fldt/UserAdd")
    public String UserAdd(@RequestParam String image64, Model model){
        // TODO 替换成你自己的appcode, 获取APPCODE  可参考 https://esandinfo.yuque.com/yv6e1k/ulp2ub/fs2mm48opwox3xc4?singleDoc#
        String ALIYUN_appCode = "TODO";//阿里云网关APPCODE

        // 从一砂云接入, 可参考文档： https://esandinfo.yuque.com/yv6e1k/aa4qsg/ghtqp7
        String ES_appCode = "";//一砂云APPCODE
        String key = "";// 一砂云网关密钥
        AlgorithmE algo = AlgorithmE.MD5;

        UserAddRequest userAddRequest = new UserAddRequest();
        userAddRequest.setUuid("123"); //用户唯一ID
        userAddRequest.setImage(image64);

        if(ES_appCode == "" || ES_appCode == "TODO"){
            ESAliyunGateway gateway = new ESAliyunGateway(ALIYUN_appCode);
            String rspMsg = gateway.sendToGateWay(UserAddRequest.ALIYUN_URL,userAddRequest.toJsonStr());
            log.info("服务器返回内容为: {}", rspMsg);
            model.addAttribute("rspMsg",rspMsg);
        }
        else {
            ESGateway esGateway = new ESGateway(ES_appCode, algo, key);
            GatewayResponse gatewayResponse = esGateway.sendToGateWay(UserAddRequest.act, userAddRequest.toJsonStr());
            log.info("服务器端返回：{}",gatewayResponse.toJsonStr());
            model.addAttribute("rspMsg",gatewayResponse.toJsonStr());

        }
        // TODO 查询结果，并展示
        return "/fldt/UserAdd";

    }
}
