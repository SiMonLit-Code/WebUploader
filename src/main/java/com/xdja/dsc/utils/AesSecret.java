package com.xdja.dsc.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * AESAES加密
 * 解密 AES-128-ECB加密
 *
 * @author niugang
 * @date 2018年11月12日
 */

public class AesSecret {



    public static String CKEY="1234567890123456";


    private static final int KEYLENGTH = 16;

    /**
     * @param sSrc：需要加密的字节数组
     * @param sKey:秘钥
     * @return byte[]
     * @throws BadPaddingException       BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @date 2018年11月12日
     */
    public static byte[] encrypt(byte[] sSrc, String sKey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        if (sKey == null) {
            throw new NullPointerException("Param key is not null");
        }
        // 判断Key是否为16位
        if (sKey.length() != KEYLENGTH) {
            throw new IllegalArgumentException("The key length must is 16 ");
        }
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc);
        // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return new Base64().encode(encrypted);
    }

    /**
     * 解密
     *
     * @param sSrc：需要解密的字节数组
     * @param sKey:秘钥
     * @return byte[]
     * @throws InvalidKeyException       InvalidKeyException
     * @throws BadPaddingException       BadPaddingException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @date 2018年11月12日
     */
    public static byte[] decrypt(byte[] sSrc, String sKey) throws NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        // 判断Key是否正确
        if (sKey == null) {
            throw new NullPointerException("Param key is not null");
        }
        // 判断Key是否为16位
        if (sKey.length() != KEYLENGTH) {
            throw new IllegalArgumentException("The key length must is 16 ");
        }
        byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        // 先用base64解密
        byte[] encrypted1 = new Base64().decode(sSrc);
        return cipher.doFinal(encrypted1);

    }

    public static void main(String[] args) throws Exception {
        /*	*//*
         * 此处使用AES-128-ECB加密模式，key需要为16位。
         *//*
		String cKey = "1234567890123456";
		// 需要加密的字串
		String cSrc = "www.safecenter.com";
		System.out.println(cSrc);
		// 加密
		byte[] enString = AesSecret.encrypt(cSrc.getBytes(), cKey);
		System.out.println("加密后的字串是：" + new String(enString));

		// 解密
		byte[] deString = AesSecret.decrypt(enString, cKey);
		System.out.println("解密后的字串是：" + new String(deString));*/

        File readFile = new File("D:\\config.tar");
        byte[] clientInputBytes = FileUtils.readFileToByteArray(readFile);
        byte[] decrypt = AesSecret.decrypt(clientInputBytes, AesSecret.CKEY);
        File readFile1 = new File("D:\\config_en.tar");
        FileUtils.writeByteArrayToFile(readFile1, decrypt);


    }
}
