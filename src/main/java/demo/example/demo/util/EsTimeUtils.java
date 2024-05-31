package demo.example.demo.util;

import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * 一砂日期时间工具类
 */
public class EsTimeUtils {

    /**
     * 获取格式化日期时间戳-精确到毫秒
     * @return yyyyMMddHHmmssSSS
     */
    public static String getFormatTimeStamp(){
        return DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS");
    }
}
