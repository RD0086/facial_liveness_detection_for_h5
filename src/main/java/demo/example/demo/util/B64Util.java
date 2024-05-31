package demo.example.demo.util;

import org.bouncycastle.util.encoders.Base64;

/**
 * BASE64编码工具类
 */
public class B64Util {

    //解码返回byte
    public static byte[] decode(String strData) {
        return Base64.decode(strData);
    }

    /**
     * base64编码
     * @param bufferData
     * @return
     */
    public static byte[] encode(byte[] bufferData) {
        byte[] base64 = Base64.encode(bufferData);
        return base64;
    }

    /**
     * base64编码
     * @param bufferData
     * @return
     */
    public static String encodeStr(byte[] bufferData) {
        byte[] base64 = Base64.encode(bufferData);
        String base64Str = new String(base64);

        return base64Str;
    }

    public static String hex2B64(String hexStr) {
        String b64Str = null;
        byte[] dataBytes = HexUtil.decodeHex(hexStr.toCharArray());
        b64Str = encodeStr(dataBytes);

        return b64Str;
    }

    public static String b642Hex(String b64Str) {
        byte[] bytes = decode(b64Str);
        String hexStr = HexUtil.encodeHexStr(bytes);

        return hexStr;
    }
}
