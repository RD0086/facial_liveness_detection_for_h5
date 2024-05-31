package demo.example.demo.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA 工具类
 */
public class RSAUtil {
    public static final String KEY_ALGORITHM = "RSA";
    public static final int KEYSIZE = 2048;
    public static final String SIGNATURE_ALGORITHM = "NONEwithRSA";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 生成 RSA2048 密钥对
     * @return 成功返回密钥对象,失败返回null
     */
    public static KeyPair genKeyPair()
    {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEYSIZE);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            return keyPair;
        }
    }

    /**
     * 获取base64编码的公钥数据
     * @param keyPair rsa 密钥对
     * @return
     */
    public static String getPubKeyB64(KeyPair keyPair)
    {
        Security.addProvider(new BouncyCastleProvider());
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        String resPublicKeyBase64 = B64Util.encodeStr(rsaPublicKey.getEncoded());

        return resPublicKeyBase64;
    }

    /**
     * 转换成公钥对象
     * @param pubKeyHex 公钥hex字符串
     * @return RSAPublicKey 实例
     */
    public static RSAPublicKey getPublicKeyHex(String pubKeyHex)
    {
        byte[] publicKeyBytes = HexUtil.decodeHex(pubKeyHex.toCharArray());

        return getPublicKey(publicKeyBytes);
    }

    /**
     * 获取公钥对象
     * @param pubKeyBase64
     * @return
     */
    public static RSAPublicKey getPublicKey(String pubKeyBase64)
    {
        byte[] publicKeyBytes = B64Util.decode(pubKeyBase64);

        return getPublicKey(publicKeyBytes);
    }

    /**
     * 获取公钥对象
     * @param pubKeyBytes
     * @return
     */
    public static RSAPublicKey getPublicKey(byte[] pubKeyBytes)
    {
        RSAPublicKey rsaPublicKey = null;
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            rsaPublicKey = (RSAPublicKey)keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } finally {
            return rsaPublicKey;
        }
    }

    /**
     * 获取私钥base64字符串
     * @param keyPair rsa密钥对对象
     * @return
     */
    public static String getPrivKeyB64(KeyPair keyPair)
    {
        Security.addProvider(new BouncyCastleProvider());
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String resPrivateKeyBase64 = B64Util.encodeStr(rsaPrivateKey.getEncoded());

        return resPrivateKeyBase64;
    }

    public static PrivateKey getPrivKey(String privKeyMsg)
    {
        PrivateKey priKey = null;
        do {
            try {
                byte[] privateKeyBytes = B64Util.decode(privKeyMsg);

                PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(privateKeyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
                priKey = keyFactory.generatePrivate(priPkcs8);
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } while (false);

        return priKey;
    }

    /**
     * 签名
     * @param digestB64 摘要数据的b64字符串
     * @param privKeyB64 私钥的b64字符串
     * @return 签名数据的b64字符串
     */
    public static byte[] sign(String digestB64, String privKeyB64)
    {
        byte[] privateKeyBytes = B64Util.decode(privKeyB64);
        byte[] digestBytes = B64Util.decode(digestB64);

        return sign(digestBytes, privateKeyBytes);
    }

    /**
     * 签名
     * @param digestBytes 摘要数据的b64字符串
     * @param privateKeyBytes 私钥的b64字符串
     * @return 签名数据的b64字符串
     */
    public static byte[] sign(byte[] digestBytes, byte[] privateKeyBytes) {
        byte[] signatureBytes = null;

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM,new BouncyCastleProvider());
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM,new BouncyCastleProvider());
            signature.initSign(privateK);
            signature.update(digestBytes);
            signatureBytes = signature.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } finally {
            return signatureBytes;
        }
    }

    /**
     * 签名
     * @param data 签名前原始数据
     * @param privateKey 私钥（base64格式）
     * @return
     */
    public static byte[] signature(byte[] data, String privateKey) {
        byte[] result = null;
        try {
            byte[] keyBytes = B64Util.decode(privateKey);
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            // 指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            // 取得私钥对象
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(priKey);
            signature.update(data);
            result = signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 验签
     * @param signatureBase64 签名数据的b64编码字符串
     * @param hashBase64 hash 数据的b64编码字符串
     * @param publicKeyBase64 公钥数据的b64字符串
     * @return
     */
    public static boolean verify(String signatureBase64, String hashBase64, String publicKeyBase64) {
        byte[] digestBytes = B64Util.decode(hashBase64);
        byte[] signBytes = B64Util.decode(signatureBase64);
        byte[] pubKeyBytes = B64Util.decode(publicKeyBase64);

        return verify(signBytes, digestBytes, pubKeyBytes);
    }

    /**
     * 验签
     * @param signatureBase64 签名数据的b64编码字符串
     * @param digestBytes 摘要数据
     * @param pubKeyBytes 公钥数据
     * @return
     */
    public static boolean verify(String signatureBase64, byte[] digestBytes, byte[] pubKeyBytes) {
        byte[] signBytes = B64Util.decode(signatureBase64);

        return verify(signBytes, digestBytes, pubKeyBytes);
    }

    /**
     * 验签
     * @param signBytes
     * @param digestBytes 签名的数据
     * @param pubKeyBytes 公钥字符串
     * @return 验签结果
     */
    public static boolean verify(byte[] signBytes, byte[] digestBytes, byte[] pubKeyBytes) {
        boolean verifyResult = false;
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM,new BouncyCastleProvider());
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

            Signature signature;
            signature = Signature.getInstance(SIGNATURE_ALGORITHM,new BouncyCastleProvider());

            signature.initVerify(publicKey);
            signature.update(digestBytes);
            verifyResult = signature.verify(signBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } finally {
            return verifyResult;
        }
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
            byte[] publicKeyBytes = B64Util.decode(publicKey);
            //构造X509EncodedKeySpec对象
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            //指定加密算法
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            //取公钥匙对象
            PublicKey pubKey = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(pubKey);
            signature.update(data);
            //验证签名是否正常
            return signature.verify(B64Util.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
