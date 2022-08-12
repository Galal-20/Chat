package com.example.chat.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.databinding.ActivitySiginUpBinding;
import com.example.chat.databinding.ActivitySignInBinding;
import com.example.chat.utilities.Constants;
import com.example.chat.utilities.PreferenceManger;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.Inflater;

public class Sign_in_Activity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManger preferenceManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManger = new PreferenceManger(getApplicationContext());
        if (preferenceManger.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        //apply for test firebase store
       // binding.ButtonSignIn.setOnClickListener(v -> addDataToFirebase());
    }

    private void setListeners(){
        binding.CreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(),Sigin_Up_Activity.class)));
        binding.ButtonSignIn.setOnClickListener(v -> {
            if (isValidSignDetails()){
                signIn();
            }
        });

    }

    //tst for firebase store
    /*private void addDataToFirebase(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String , Object> data = new HashMap<>();
        data.put("First name", "Galal");
        data.put("Last name", "Ahmed");
        data.put("Email", "ga71387@gamil.com");
        data.put("password", "12346");
        database.collection("users")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception ->{
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }*/

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    // SignIn

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL ,binding.inputMail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD , binding.inputPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
                       DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                       preferenceManger.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                       preferenceManger.putString(Constants.KEY_USER_ID , documentSnapshot.getId());
                       preferenceManger.putString(Constants.KEY_NAME , documentSnapshot.getString(Constants.KEY_NAME));
                       preferenceManger.putString(Constants.KEY_IMAGE , documentSnapshot.getString(Constants.KEY_IMAGE));
                       Intent i = new Intent(getApplicationContext() , MainActivity.class);
                       i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(i);
                   }else {
                       loading(false);
                       showToast("Unable to sign in");
                   }
                });

    }

    //progressBar
    private void loading(Boolean isLoading){
        if (isLoading){
            binding.ButtonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.ButtonSignIn.setVisibility(View.VISIBLE);
        }
    }
    private Boolean isValidSignDetails(){
        if (binding.inputMail.getText().toString().trim().isEmpty()){
            showToast("Enter your Email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputMail.getText().toString()).matches()){
            showToast("Enter valid email");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Enter Password");
            return false;
        }else {return true;}
    }
}