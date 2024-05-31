package demo.example.demo.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
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
        String livingType = "26"; // 活体动作类型
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
        // TODO 查询结果，并展示
        return "result";
    }
}
