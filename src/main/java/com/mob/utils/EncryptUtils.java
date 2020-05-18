package com.mob.utils;

import com.mob.library.util.sdk.cipher.http.MobCipherUtils;
import com.mob.library.util.sdk.cipher.http.dto.MobCipherDto;
import org.testng.Assert;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Properties;

public class EncryptUtils {

    private static BigInteger RSA_MODULES;
    private static BigInteger RSA_PRIVATE;
    private static BigInteger RSA_PUBLIC;
    private static int KEY_SIZE;

    private static final String aeskey = "19779ABVCDECSa4a";

    public static String generalEncode(String content,String fileName) throws Exception {
        InputStream is = Object.class.getResourceAsStream("/"+fileName);
        Properties pro = new Properties();
        pro.load(is);

        RSA_MODULES = new BigInteger(pro.getProperty("rsa.modules"),16);
        RSA_PRIVATE = new BigInteger(pro.getProperty("rsa.privateKey"),16);
        RSA_PUBLIC = new BigInteger(pro.getProperty("rsa.publicKey"),16);
        KEY_SIZE = Integer.parseInt(pro.getProperty("key.size"));

        String result = MobCipherUtils.encryptRequest(content, aeskey, RSA_PUBLIC, RSA_MODULES);
//        System.out.println(result);
//
//        MobCipherDto mobCipherDtoCustomKey = MobCipherUtils.decryptRequest(result, RSA_PRIVATE, RSA_MODULES);
//        System.out.println(mobCipherDtoCustomKey.getPlainText());
//        Assert.assertEquals(mobCipherDtoCustomKey.getPlainText(), content);
        return result;
    }

    public static String generalDecode(String content){
        byte[] aesKey = aeskey.getBytes();
        String result = MobCipherUtils.decryptResponse(content, aesKey);
        return result;
    }

}
