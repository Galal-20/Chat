package com.example.chat.Activity;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chat.databinding.ActivitySiginUpBinding;
import com.example.chat.utilities.Constants;
import com.example.chat.utilities.PreferenceManger;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class Sigin_Up_Activity extends AppCompatActivity {

    private ActivitySiginUpBinding binding;
    private String encodeImage;
    private PreferenceManger preferenceManger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySiginUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManger = new PreferenceManger(getApplicationContext());
        setListeners();
    }

    private void setListeners(){
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.ButtonSignUp.setOnClickListener(v ->{
            if (isValidSignUpDetails()){
                SignUp();
            }
        });
        binding.layoutImage.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void SignUp(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String , Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME , binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL , binding.inputMail.getText().toString());
        user.put(Constants.KEY_PASSWORD , binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE , encodeImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManger.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManger.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManger.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    preferenceManger.putString(Constants.KEY_IMAGE , encodeImage);
                    Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                })
                .addOnFailureListener(exception ->{
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    //Image
    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight , false);
        ByteArrayOutputStream byteArrayoutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG , 50, byteArrayoutputStream);
        byte[] bytes = byteArrayoutputStream.toByteArray();
        return Base64.encodeToString(bytes , Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.TextAddImageProfile.setVisibility(View.GONE);
                            encodeImage = encodeImage(bitmap);
                        }catch (FileNotFoundException exception){
                            exception.printStackTrace();
                        }
                    }
                }
            }
    );

    private Boolean isValidSignUpDetails(){
        if (encodeImage == null){
            showToast("Select profile image");
            return false;
        }else  if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Enter your name");
            return false;
        }else if (binding.inputMail.getText().toString().trim().isEmpty()){
            showToast("Enter your email");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputMail.getText().toString()).matches()){
            showToast(" Enter valid Email");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()){
            showToast("Enter your password");
            return false;
        }else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Confirm password");
            return false;
        }else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
            showToast("Make sure your password and confirm password are identical");
            return false;
        }else {
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            binding.ButtonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.ButtonSignUp.setVisibility(View.VISIBLE);
        }
    }
}