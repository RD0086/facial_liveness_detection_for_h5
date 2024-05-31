package demo.example.demo.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * @author zh
 * @data 2020/9/10 15:34
 * 说明：uuid工具类
 */
public class UUidUtils {

    public static String getTimeUUid32(){
        String time = DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS");
        String randomStr = RandomStringUtils.random(15,"1234567890qwertyuiopasfdghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM");
        return time+randomStr;
    }
}
