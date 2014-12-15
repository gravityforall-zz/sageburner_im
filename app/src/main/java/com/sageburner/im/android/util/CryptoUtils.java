package com.sageburner.im.android.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;

/**
 * Created by Ryan on 11/30/2014.
 */
public class CryptoUtils {

    private static final String CRYPTO_ALGORITHM = "AES";
    private static final String CRYPTO_ALGORITHM_MODE = "AES/ECB/PKCS7Padding";

    private static String encrypt(Key inKey, String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_MODE, new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, inKey);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        byte[] encryptedValue = Base64.encode(encrypted);
        return new String(encryptedValue);
    }

    private static String decrypt(Key inKey, String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_MODE, new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, inKey);
        byte[] decodedBytes = Base64.decode(ciphertext.getBytes());
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

    private static Key generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPTO_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    public static CryptoMessage createCryptoMessage(String inMessage) throws Exception {
        Key key = generateKey();
        String encryptedMessage = encrypt(key, inMessage);
        return new CryptoMessage(encryptedMessage, key);
    }

    public static byte[] parseKey(String cryptoMessage) {
        int separatorPos = cryptoMessage.lastIndexOf(";");
        String keyString = cryptoMessage.substring(separatorPos + 1);
        return Base64.decode(keyString.getBytes());
    }

    public static void main(String[] args) throws Exception {

        System.out.println("=================================================");
        System.out.println("Testing basic encryption/decryption functionality");
        String data = "Yabba Dabba Do!";
        CryptoMessage cryptoMessage = createCryptoMessage(data);
        System.out.println("Plain Text : " + data);
        System.out.println("Encrypted Text : " + cryptoMessage.getMessage());
        System.out.println("Decrypted Text : " + decrypt(cryptoMessage.getKey(), cryptoMessage.getMessage()));
        System.out.println("=================================================");

        System.out.println("=================================================");
        System.out.println("Testing encryption/decryption with 'marshalling'");
        //Test creating a message string and reading it back
        data = "This text has been encrypted, stringified, and decrypted!";
        cryptoMessage = createCryptoMessage(data);
        System.out.println("Plain Text : " + data);
        System.out.println("Encrypted Text : " + cryptoMessage.getMessage());

        System.out.println("Key getEncoded() : " + cryptoMessage.getKey().getEncoded());
        String cryptoMessageString = cryptoMessage.toString();
        String keyString = new String(parseKey(cryptoMessageString));
        byte[] keyBytes = parseKey(cryptoMessageString);
        System.out.println("cryptoMessageString : " + cryptoMessageString);
        System.out.println("keyString : " + keyString);
        Key key = new SecretKeySpec(keyBytes, CRYPTO_ALGORITHM);

        System.out.println("Decrypted Text : " + decrypt(key, cryptoMessage.getMessage()));
        System.out.println("=================================================");

    }
}
