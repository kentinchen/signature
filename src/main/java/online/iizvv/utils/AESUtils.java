package online.iizvv.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import online.iizvv.core.config.Config;

import java.math.BigDecimal;

/**
 * @author ：iizvv
 * @date ：Created in 2019-09-03 09:35
 * @description：TODO
 * @version: 1.0
 */
public class AESUtils {

    /**
      * create by: iizvv
      * description: 加密
      * create time: 2019-09-03 09:37

      * @return str
      */
    public static String encryptHex(String hex) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, Config.aesKey.getBytes());
        //加密为16进制表示
        String encryptHex = aes.encryptHex(hex);
        System.out.println(encryptHex);
        return encryptHex;
    }

    /**
      * create by: iizvv
      * description: 解密
      * create time: 2019-09-03 09:38

      * @return str
      */
    public static long decryptStr(String encryptHex) {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, Config.aesKey.getBytes());
        //解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);
        if (isNumeric(decryptStr)) {
            return Integer.valueOf(decryptStr);
        }
        return 0;
    }

    /**
      * create by: iizvv
      * description: 判断是否为数字
      * create time: 2019-09-03 10:07

      * @return boolean
      */
    static boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }


}
