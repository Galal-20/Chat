package com.example.chat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.databinding.ActivityMainBinding;
import com.example.chat.databinding.ActivitySignInBinding;
import com.example.chat.utilities.Constants;
import com.example.chat.utilities.PreferenceManger;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManger preferenceManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManger = new PreferenceManger(getApplicationContext());
        loadUserDetails();
        getToken();
        setListeners();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setListeners(){
        binding.imageSignIn.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), UserActivity.class)));
    }


    private void loadUserDetails(){
        binding.textName.setText(preferenceManger.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferenceManger.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        DocumentReference documentReference = dataBase.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManger.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN,token)
               // .addOnSuccessListener(unused -> showToast("Token updated"))
                .addOnFailureListener(e -> showToast("unable to update"));

    }

    //SignOut
    private void signOut(){
        showToast("Signing out.....");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManger.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManger.clear();
                    startActivity(new Intent(getApplicationContext(), Sign_in_Activity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }
}