package com.example.chat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PerformanceHintManager;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.example.chat.R;
import com.example.chat.adapter.ChatAdapter;
import com.example.chat.databinding.ActivityChatsBinding;
import com.example.chat.models.ChatMessage;
import com.example.chat.models.User;
import com.example.chat.utilities.Constants;
import com.example.chat.utilities.PreferenceManger;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessage;
    private ChatAdapter chatAdapter;
    private PreferenceManger preferenceManger;
    private FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
    }

    private void init(){
        preferenceManger = new PreferenceManger(getApplicationContext());
        chatMessage = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessage,
                getBitmapFromEncodedString(receiverUser.image),
                preferenceManger.getString(Constants.KEY_USER_ID)
        );
        binding.chatRecyclerview.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID , preferenceManger.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID , receiverUser.id);
        message.put(Constants.KEY_MESSAGE , binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP , new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        binding.inputMessage.setText(null);
    }

    private Bitmap getBitmapFromEncodedString(String  encodedImage){
        byte[] bytes = Base64.decode(encodedImage , Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void loadReceiverDetails(){
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }
}