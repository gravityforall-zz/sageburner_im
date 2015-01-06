package com.sageburner.im.android.util;

import android.util.Log;
import com.sageburner.im.android.BootstrapApplication;
import com.sageburner.im.android.core.Constants;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.jpbc.IBE;
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

    private static final String CRYPTO_ALGORITHM = Constants.Crypto.CRYPTO_ALGORITHM;
    private static final String CRYPTO_ALGORITHM_MODE = Constants.Crypto.CRYPTO_ALGORITHM_MODE;

    private static String encrypt(String inKeyString, String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_MODE, new BouncyCastleProvider());
        Key key = new SecretKeySpec(Base64.decode(inKeyString.getBytes()), Constants.Crypto.CRYPTO_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        byte[] encryptedValue = Base64.encode(encrypted);
        return new String(encryptedValue);
    }

    private static String decrypt(String inKeyString, String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM_MODE, new BouncyCastleProvider());
        Key key = new SecretKeySpec(Base64.decode(inKeyString.getBytes()), Constants.Crypto.CRYPTO_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.decode(ciphertext.getBytes());
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

    private static Key generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPTO_ALGORITHM);
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    public static CryptoMessage createCryptoMessage(String inMessage, String inUsername) throws Exception {
        Key key = generateKey();
        String keyString = createKeyString(key);
        String encryptedMessage = encrypt(keyString, inMessage);

        IBE ibe = BootstrapApplication.getInstance().getIBE();
        String encryptedKey = ibe.getEncFromID(keyString, inUsername);
        String encryptedKeyString = new String(Base64.encode(encryptedKey.getBytes()));

        return new CryptoMessage(encryptedMessage, encryptedKeyString);
    }

    public static CryptoMessage createCryptoMessage(String inMessage) throws Exception {
        String encryptedMessage = parseMessage(inMessage);
        String encryptedKey = parseKeyString(inMessage);

        BootstrapApplication bootstrapApplication = BootstrapApplication.getInstance();

        IBE ibe = bootstrapApplication.getIBE();
        User user = bootstrapApplication.getLocalUser();
        String decryptedKey = ibe.getDecFromID(encryptedKey, user.getUsername());
        String decryptedKeyString = new String(Base64.encode(decryptedKey.getBytes()));

        return new CryptoMessage(encryptedMessage, decryptedKeyString);
    }

    public static String parseMessage(String cryptoMessage) {
        int separatorPos = cryptoMessage.lastIndexOf(";");
        String messageString = cryptoMessage.substring(0, separatorPos);
        return messageString;
    }

    public static byte[] parseKey(String cryptoMessage) {
        int separatorPos = cryptoMessage.lastIndexOf(";");
        String keyString = cryptoMessage.substring(separatorPos + 1);
        //Log.d("CryptoUtils::parseKey: ", " keyString: " + keyString);
        return Base64.decode(keyString.getBytes());
    }

    public static String parseKeyString(String cryptoMessage) {
        int separatorPos = cryptoMessage.lastIndexOf(";");
        String keyString = cryptoMessage.substring(separatorPos + 1);
        //Log.d("CryptoUtils::parseKey: ", " keyString: " + keyString);
        return keyString;
    }

    public static String readCryptoMessage(String cryptoMessageString) throws Exception {
        String message = parseMessage(cryptoMessageString);
        String keyString = parseKeyString(cryptoMessageString);
        return decrypt(keyString, message);
    }

    public static String createKeyString(Key inKey) {
        byte[] encodedKey = Base64.encode(inKey.getEncoded());
        return new String(encodedKey);
    }

    public static void main(String[] args) throws Exception {

        System.out.println("=================================================");
        System.out.println("Testing basic encryption/decryption functionality");
        User testUser = new User();
        testUser.setUsername("testUser@sageburner.com");
        String data = "Yabba Dabba Do!";
        CryptoMessage cryptoMessage = createCryptoMessage(data, testUser.getUsername());
        System.out.println("Plain Text : " + data);
        System.out.println("Encrypted Text : " + cryptoMessage.getMessage());
        System.out.println("Decrypted Text : " + decrypt(cryptoMessage.getKey(), cryptoMessage.getMessage()));
        System.out.println("=================================================");

        //=================================================================================

        System.out.println("=================================================");
        System.out.println("Testing encryption/decryption with 'marshalling'");
        //Test creating a message string and reading it back
        data = "This text has been encrypted, stringified, and decrypted!";
        cryptoMessage = createCryptoMessage(data, testUser.getUsername());
        System.out.println("Plain Text : " + data);
        System.out.println("Encrypted Text : " + cryptoMessage.getMessage());

        System.out.println("Key : " + cryptoMessage.getKey());
        String cryptoMessageString = cryptoMessage.toString();
        String keyString = parseKeyString(cryptoMessageString);
        System.out.println("cryptoMessageString : " + cryptoMessageString);
        System.out.println("keyString : " + keyString);

        //=================================================================================

        System.out.println("Decrypted Text : " + decrypt(keyString, cryptoMessage.getMessage()));
        System.out.println("=================================================");

        System.out.println("=================================================");
        System.out.println("Testing readCryptoMessage(CryptoMessage cryptoMessage)");
        //Test creating a message string and reading it back
        data = "Time to test readCryptoMessage(CryptoMessage cryptoMessage)!";
        cryptoMessage = createCryptoMessage(data, testUser.getUsername());
        cryptoMessageString = cryptoMessage.toString();
        System.out.println("Plain Text : " + data);
        System.out.println("Encrypted Text : " + cryptoMessage.getMessage());
        System.out.println("Decrypted Text : " + readCryptoMessage(cryptoMessageString));
        System.out.println("=================================================");

    }
}
