package com.example.chat.utilities;

import java.util.HashMap;

public class Constants {

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "EMAIL";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PREFERENCE = "chatPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_USER = "user";
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    public static final String KEY_COLLECTION_CHAT = "chat";
    public static final String KEY_SENDER_ID = "senderId";
    public static final String KEY_RECEIVER_ID = "receiverId";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timesTamp";
    public static final String KEY_COLLECTION_CONVERSATIONS = "conversations";
    public static final String KEY_SENDER_NAME = "senderName";
    public static final String KEY_RECEIVER_NAME = "receiverName";
    public static final String KEY_RECEIVER_IMAGE = "receiverImage";
    public static final String KEY_SENDER_IMAGE = "senderImage";
    public static final String KEY_LAST_MESSAGE = "lastMessage";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS = "registration_Ids";

    public static HashMap<String  , String> remoteMsgHeaders = null;
    public static HashMap<String  , String> getRemoteMsgHeaders(){
        if (remoteMsgHeaders == null){
            remoteMsgHeaders = new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "Key = AAAA9BOcZ6M:APA91bEAUgQ5cMdaZjEPJRZuXjh1hQ4zrT3m5-j9yLgu3Yk86idTsELpARnUBOm8pI07-pUUy_rpuMWn0r81-sdcG_JQthwVKt-w9Xjqy70Iy3r288k8aJcohnxKfzVRkBMSGjlVCGAh"
            );
            remoteMsgHeaders.put(
              REMOTE_MSG_CONTENT_TYPE,
                    "application/josn"
            );
        }
        return remoteMsgHeaders;
    }
}
