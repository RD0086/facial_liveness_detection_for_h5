package demo.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class TouchController {
    @RequestMapping("/touch")
    public String touch() {
        return new JSONObject(){{
            put("time", DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        }}.toJSONString();
    }
}
