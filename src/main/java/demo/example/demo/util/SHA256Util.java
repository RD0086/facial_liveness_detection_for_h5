package demo.example.demo.util;

import java.security.MessageDigest;

public class SHA256Util {

    /**
     * 获取sha256摘要,添加了前缀 3031300d060960864801650304020105000420
     * @param data 原始数据
     * @return
     */
    public static byte[] digestExt(byte[] data) {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data);
            String hashHex = HexUtil.encodeHexStr(data);
            hashHex = "3031300d060960864801650304020105000420" + hashHex;
            digest = HexUtil.decodeHex(hashHex.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return digest;
        }
    }

    /**
     * 获取sha256摘要
     * @param data 原始数据
     * @return
     */
    public static byte[] digest(byte[] data) {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            digest = md.digest(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return digest;
        }
    }
}
