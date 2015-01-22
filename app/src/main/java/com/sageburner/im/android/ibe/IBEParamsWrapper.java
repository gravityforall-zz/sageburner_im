package com.sageburner.im.android.ibe;

/**
 * Created by Ryan on 1/17/2015.
 */
public class IBEParamsWrapper {

    private int key;
    private IBEParams ibeParams;

    public IBEParamsWrapper(int key, IBEParams ibeParams) {
        this.key = key;
        this.ibeParams = ibeParams;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public IBEParams getIbeParams() {
        return ibeParams;
    }

    public void setIbeParams(IBEParams ibeParams) {
        this.ibeParams = ibeParams;
    }
}
