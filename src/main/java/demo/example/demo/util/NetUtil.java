package demo.example.demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtil {
    public static boolean isUrlAvailable(String urlString){
        String patternString = "^(?:http(s)?:\\/\\/)?[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\\\]@!\\$&'\\*\\+,;=.]+$";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(patternString);

        // 现在创建 matcher 对象
        Matcher m = r.matcher(urlString);
        if (m.find( )) {
            return true;
        } else {
            return false;
        }
    }
}
