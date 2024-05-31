package demo.example.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Util {

    /**
     * 获取MD5摘要
     * @param content 计算摘要的数据
     * @return 返回MD5摘要数据
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getMD5Digest (byte[] content) {
        byte[] md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("md5").digest(content);
        } catch (NoSuchAlgorithmException e) {
            log.error("发生异常 : ", e);
        }

        return md5Digest;
    }
}
