package com.example.chatapptest;

import android.os.Build;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

public class MessageModel
{
    private String messageId, senderId, message;
    private String timestamp;


    public MessageModel(String messageId, String senderId, String message) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.message = message;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.timestamp = Timestamp.from(Instant.now()).toString();
        }
    }
    public MessageModel()
    {}


    public String getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp(){ return timestamp;}


    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
