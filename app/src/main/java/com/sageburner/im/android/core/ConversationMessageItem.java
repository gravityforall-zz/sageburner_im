package com.sageburner.im.android.core;

import com.sageburner.im.android.util.CryptoMessage;

/**
 * Created by Ryan on 10/10/2014.
 */
public class ConversationMessageItem {
    boolean isIncoming;
    User toUser;
    User fromUser;
    CryptoMessage message;
    String objectId;
    String avatarUrl;

    public boolean isIncoming() {
        return isIncoming;
    }

    public void setIncoming(boolean isIncoming) {
        this.isIncoming = isIncoming;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public CryptoMessage getMessage() {
        return message;
    }

    public void setMessage(CryptoMessage message) {
        this.message = message;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("ConversationMessageItem:\n");
        sb.append("========================\n");
        sb.append("isIncoming: " + isIncoming + "\n");
        if (toUser != null) {
            sb.append("toUser: " + toUser.getUsername() + "\n");
        } else {
            sb.append("toUser: null\n");
        }
        if (fromUser != null) {
            sb.append("fromUser: " + fromUser.getUsername() + "\n");
        } else {
            sb.append("fromUser: null\n");
        }
        sb.append("message: " + message + "\n");
        sb.append("objectId: " + objectId + "\n");
        sb.append("avatarUrl: " + avatarUrl + "\n");
        sb.append("========================");

        return sb.toString();
    }
}