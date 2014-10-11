package com.sageburner.im.android.core;

/**
 * Created by Ryan on 10/10/2014.
 */
public class ConversationMessageItem {
    boolean isIncoming;
    User toUser;
    User fromUser;
    String messageText;
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

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
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
}