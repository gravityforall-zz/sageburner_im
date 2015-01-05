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
    private String key;

    public CryptoMessage(String inMessage, String inKey) {
        this.message = inMessage;
        this.key = inKey;
    }

    public String getMessage() {
        return message;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(message);
        sb.append(Constants.Crypto.CRYPTO_MESSAGE_SEPARATOR);
        sb.append(key);

        return sb.toString();
    }
}
