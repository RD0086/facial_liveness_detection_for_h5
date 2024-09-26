package demo.example.demo.base.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支持得算法枚举
 */
@Getter
@AllArgsConstructor
public enum AlgorithmE {
    MD5("3"),
    ECC256("1"),
    RSA2048("2");

    // 1：ECC,2:RSA,3:MD5
    private String code;
}
