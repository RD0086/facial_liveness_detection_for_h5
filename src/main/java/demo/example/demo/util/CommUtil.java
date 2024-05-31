package demo.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.ui.Model;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CommUtil {
    public static String toErrorPage(Model model, String token, String title, String content) {
        model.addAttribute("pageTitle", "实人认证");
        model.addAttribute("token", "");
        model.addAttribute("p", title);
        model.addAttribute("h", content);

        log.error("跳转到错误页面: title: {} , content: {}", title, content);
        return "error";
    }

    /**
     * 检查姓名格式是否合法
     * @param name 姓名字符串
     * @return true : 合法, false : 不合法
     */
    public static boolean checkNameFormat(String name) {
        String pattern = "^[\\u4e00-\\u9fa5.·\\u36c3\\u4DAE]{2,}$";
        boolean isMatch = Pattern.matches(pattern, name);

        return isMatch;
    }

    //在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。
    private static Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[1][1,3,4,5,6,7,8,9][0-9]{9}$");
    /**
     * 基于时间戳的uuid
     * @return
     */
    public static String getTimeUUID() {
        return DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS")+"-"+ RandomStringUtils.random(8,"1qw2er3ty4ui5op6as7df8gh9jk0lz1xc2vb3nm4");
    }

    /**
     * 银行卡校验
     * @param str
     * @return
     */
    public static boolean isIdCard(String str) {
        if(null == str || str.trim().isEmpty()) {
            return false;
        }

        str = str.trim().toUpperCase();
        // 判断号码的长度 15位或18位
        if (str.length() > 19) {
            return false;
        }

        return isNumeric(str);
    }

    private static boolean isNumeric(String strnum) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(strnum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 手机号验证
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(final String str) {
        Matcher m = null;
        boolean b = false;
        m = PHONE_NUMBER_PATTERN.matcher(str);
        b = m.matches();
        return b;
    }

    public static byte[] getFileToByte(File file) {
        byte[] by = new byte[(int)file.length()];
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            byte[] bb = new byte[2048];
            // 从此输入流中读入bb.length个字节放进bb数组
            int ch = is.read(bb);
            while(ch != -1) {
                // 将bb数组中的内容写入到输出流
                bytestream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            // 将输出流中的内容复制到by数组
            by = bytestream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return by;
    }

    public static String getTmpDirectoryPath(){
        String tmpPath = System.getProperty("java.io.tmpdir");
        if(tmpPath.endsWith("/")){
            tmpPath = tmpPath.substring(0,tmpPath.length() - 1);
        }

        return tmpPath;
    }

    public static String chineseNumber2MathNumber(String srcStr) {
        return srcStr.replace('一', '1')
                .replace('二', '2')
                .replace('三', '3')
                .replace('四', '4')
                .replace('五', '5')
                .replace('六', '6')
                .replace('七', '7')
                .replace('八', '8')
                .replace('九', '9');

    }

    /**
     * 获取照片BASE64字符串 (去掉前缀 data:image/jpeg;base64,)
     * @return
     */
    public static String getImageBase64(String imgStr) {
        String imgBase64Str = imgStr;
        if (imgStr.contains("base64,")) {
            String[] split = imgStr.split("base64,");
            if (split.length <= 1) {
                imgBase64Str = split[0];
            } else {
                imgBase64Str = split[1];
            }
        }

        return imgBase64Str;
    }

    /**
     * 获取当前时间字符串, 格式为 yyyyMMddHHmmssSSS
     * @return
     */
    public static String getCurrentTimeStr() {
        return DateFormatUtils.format(new Date(),"yyyyMMddHHmmssSSS");
    }

    /**
     * 获取URL中query域的参数
     * @param queryStr
     * @return
     */
    public static Map<String, String> getParameter(String queryStr) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            final String charset = "utf-8";
            String[] keyValues = queryStr.split("&");
            for (int i = 0; i < keyValues.length; i++) {
                String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
                String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

}
