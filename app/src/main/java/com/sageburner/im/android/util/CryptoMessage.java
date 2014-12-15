package com.sageburner.im.android.util;

import com.sageburner.im.android.core.Constants;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.security.Key;

/**
 * Created by Ryan on 12/14/2014.
 */
public class CryptoMessage {

    private String message;
    private Key key;

    public CryptoMessage(String message, Key key) {
        this.message = message;
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(message);
        sb.append(Constants.Crypto.CRYPTO_MESSAGE_SEPARATOR);

        byte[] encryptedValue = Base64.encode(key.getEncoded());
        sb.append(new String(encryptedValue));

        return sb.toString();
    }
}
