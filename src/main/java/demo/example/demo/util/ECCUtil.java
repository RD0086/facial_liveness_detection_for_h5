package demo.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ECCUtil {
    private static final String PROVIDER = "BC";
    private static final byte[] PUB_KEY_TL = new byte[26];
    public static final String DEFAULT_CURVE_NAME = "secp256r1";
    public static final String SIGNATURE_ALGORITM = "NONEwithECDSA";
    public static final String KEY_ALGORITHM = "EC";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        try {
            KeyPair keyPair = genKeyPair();
            PublicKey publicKeyExample = keyPair.getPublic();
            System.arraycopy(publicKeyExample.getEncoded(), 0, PUB_KEY_TL, 0, 26);
        } catch (Exception e) {
            log.error("无法初始化算法", e);
        }
    }

    public static KeyPair genKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static PublicKey decodePublicKey(String keyStr) throws NoSuchProviderException, NoSuchAlgorithmException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyStr));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        try {
            return keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            log.error("无效的ECC公钥", e);
            return null;
        }
    }

    public static String encodePrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public static PrivateKey decodePrivateKey(String keyStr) throws NoSuchProviderException, NoSuchAlgorithmException {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        try {
            return keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            log.error("无效的ECC私钥", e);
            return null;
        }
    }

    public static byte[] encrypt(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);

        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");

        ECPublicKey pubKey = (ECPublicKey) keyFactory.generatePublic(x509KeySpec);

        Cipher cipher = new NullCipher();
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");

        ECPrivateKey priKey = (ECPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);

        Cipher cipher = new NullCipher();
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        return cipher.doFinal(data);
    }

    public static byte[] signature(byte[] content, String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        byte[] signBytes = null;
        PrivateKey privateKey = null;
        try {
            privateKey = decodePrivateKey(privateKeyStr);
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(privateKey);
            signature.update(content);
            signBytes = signature.sign();
        } catch (NoSuchProviderException e) {
            log.error("数据加密失败", e);
        }
        return signBytes;
    }

    private static final int KEY_SIZE = 256;    //密钥长度
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static Map<String,String> getGenerateKey() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC","BC");
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
        KeyPair kp = keyPairGenerator.generateKeyPair();
        org.bouncycastle.jce.interfaces.ECPublicKey publicKey = (org.bouncycastle.jce.interfaces.ECPublicKey) kp.getPublic();
        org.bouncycastle.jce.interfaces.ECPrivateKey privateKey = (org.bouncycastle.jce.interfaces.ECPrivateKey) kp.getPrivate();
        Map<String,String> map = new HashMap<>();
        System.out.println("private: "+Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        System.out.println("public: "+Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        return map;
    }

    public static void main(String[] args) throws Exception{
        Map<String, String> generateKey = getGenerateKey();
    }
    /**
     * 签名验证
     * @param data 签名前原始数据
     * @param publicKey 公钥（base64格式）
     * @param sign 签名后的数据（base64格式）
     * @return
     */
    public static boolean verify(byte[] data, String publicKey, String sign) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

            //构造X509EncodedKeySpec对象
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            //取公钥匙对象
            PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(pubKey);
            signature.update(data);

            //验证签名是否正常
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            log.error("ECC256.verify", e);
            return false;
        }
    }

    /**
     * 对摘要进行签名
     * @param hash 摘要数据
     * @param privateKeyStr 私钥字符串
     * @return 签名数据
     */
    public static byte[] sign(byte[] hash, String privateKeyStr) {
        byte[] signatureBuff = null;
        byte[] privateKeyBuff = B64Util.decode(privateKeyStr);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBuff);

        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITM);
            signature.initSign(privateKey);
            signature.update(hash);
            signatureBuff = signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return signatureBuff;
        }
    }

    /**
     * 验签
     * @param signatureBuff 签名摘要
     * @param hashBuff 数据摘要
     * @param publicKeyBase64 公钥数据
     * @return 验签结果
     */
    public static boolean verify(byte[] signatureBuff, byte[] hashBuff, String publicKeyBase64) {
        boolean verifyResult = false;
        byte[] publicKeyBuff = B64Util.decode(publicKeyBase64);

        Security.addProvider(new BouncyCastleProvider());
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBuff);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITM);
            signature.initVerify(publicKey);
            signature.update(hashBuff);
            verifyResult = signature.verify(signatureBuff);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            return verifyResult;
        }
    }
}
