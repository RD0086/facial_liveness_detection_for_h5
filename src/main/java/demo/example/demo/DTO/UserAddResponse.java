package demo.example.demo.DTO;

import demo.example.demo.util.GsonUtil;
import lombok.Data;

@Data
public class UserAddResponse {
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
     * 执行add或者update
     */
    private String action;

    public String toJsonStr(){return GsonUtil.getAllJson().toJson(this);}

    public static UserAddResponse fromJsonStr(String JsonStr){
        return GsonUtil.getAllJson().fromJson(JsonStr, UserAddResponse.class);
    }
}

