package com.sageburner.im.android.util;

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
}
