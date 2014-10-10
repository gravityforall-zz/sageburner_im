package com.sageburner.im.android.core;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ryan on 10/9/2014.
 */
public class ConversationViewHolder {
    ImageView avatar;
    TextView message;

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }
}
