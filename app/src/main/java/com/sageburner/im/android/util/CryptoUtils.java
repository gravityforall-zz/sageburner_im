package com.sageburner.im.android.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Created by Ryan on 11/30/2014.
 */
public class CryptoUtils {
    private static final String CRYPTO_ALGORITHM = "AES";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/CBC/PKCS5Padding";
    private static final String CRYPTO_ALGORITHM_MODE = "AES/ECB/PKCS7Padding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/CBC/PKCS7Padding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/OFB/NoPadding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/CFB/NoPadding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/ECB/PKCS7Padding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/CBC/PKCS7Padding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/OFB/NoPadding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/CFB/NoPadding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/ECB/PKCS7Padding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/CBC/PKCS7Padding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/OFB/NoPadding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/CFB/NoPadding";
    //private static final String CRYPTO_ALGORITHM_MODE = "AES/ECB/NoPadding";
    //128
    //private static final String keyValue = "000102030405060708090a0b0c0d0e0f";
    //192
    //private static final String keyValue = "000102030405060708090a0b0c0d0e0f1011121314151617";
    //256
    private static final String keyValue = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f";


    public static String encrypt(String inKey, String plaintext) throws Exception {
        Key key = generateKey(inKey);
        Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_MODE, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        byte[] encryptedValue = Base64.encode(encrypted);
        return new String(encryptedValue);
    }

    public static String decrypt(String inKey, String ciphertext) throws Exception {
        Key key = generateKey(inKey);
        Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_MODE, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.decode(ciphertext.getBytes());
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

    private static Key generateKey(String inKey) throws Exception {
        Key key = new SecretKeySpec(Hex.decode(inKey), CRYPTO_ALGORITHM);
        return key;
    }

    public static void main(String[] args) throws Exception {

        //String data = "00112233445566778899aabbccddeeff";
        String data = "Hello There!";
        String dataEnc = encrypt(keyValue, data);
        String dataDec = decrypt(keyValue, dataEnc);

        System.out.println("Plain Text : " + data);
        System.out.println("Encrypted Text : " + dataEnc);
        System.out.println("Decrypted Text : " + dataDec);
    }
}
