package demo.example.demo.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class LivingDetectionController {


    @RequestMapping("livingDetection/index")
    public String indexTo() {
        return "fldt/sdk_living_detection/living_index";
    }

    @RequestMapping("livingDetection/result")
    public String toGetResult(@RequestParam String token , @RequestParam String verifyMsg, @RequestParam String code, @RequestParam String msg, Model model) {
        model.addAttribute("token",token);
        model.addAttribute("verifyMsg",verifyMsg);
        model.addAttribute("code",code);
        model.addAttribute("msg",msg);
        return "fldt/sdk_living_detection/living_getResult";
    }

    @PostMapping("livingDetection/init")
    @ResponseBody
    public String init(String initMsg) throws Exception {
        log.info("请求参数:{}", initMsg);
        HttpResponse httpResponse = HttpRequest.post("https://eface.market.alicloudapi.com/init")
                .form("initMsg", initMsg)
                .header("Authorization", "APPCODE 你的appcode")
                .header("X-Ca-Nonce", RandomStringUtils.random(8, "1234567890987654321234567890"))
                .header("Content-Type", "application/x-www-form-urlencoded;chart-set:utf-8")
                .execute();

        log.info("status:{}", httpResponse.getStatus());
        log.info("body:{}", httpResponse.body());
        //如果返回status是500则抛出异常
        if(httpResponse.getStatus() == 500){
            throw new Exception("请求错误");
        }
        return httpResponse.body();
    }

    @PostMapping("livingDetection/verify")
    @ResponseBody
    public String verify(String token, String verifyMsg, Boolean captureFace) {
        log.info("请求参数:{}=={}", token, verifyMsg, captureFace);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("verifyMsg", verifyMsg);
        map.put("chopPortrait", captureFace);
        HttpResponse httpResponse = HttpRequest.post("https://eface.market.alicloudapi.com/verify")
                .form(map)
                .header("Authorization", "APPCODE 你的appcode")
                .header("X-Ca-Nonce", RandomStringUtils.random(8, "1234567890987654321234567890"))
                .header("Content-Type", "application/x-www-form-urlencoded;chart-set:utf-8")
                .execute();
        log.info("status:{}", httpResponse.getStatus());
        log.info("body:{}", httpResponse.body());
        return httpResponse.body();
    }


}
