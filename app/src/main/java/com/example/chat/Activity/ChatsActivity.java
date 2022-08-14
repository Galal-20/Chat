package com.example.chat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chat.R;
import com.example.chat.databinding.ActivityChatsBinding;
import com.example.chat.models.User;
import com.example.chat.utilities.Constants;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private User receiverUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
    }

    private void loadReceiverDetails(){
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
    }
}