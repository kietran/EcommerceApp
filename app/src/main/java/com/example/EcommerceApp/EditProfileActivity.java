package com.example.EcommerceApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EcommerceApp.model.UserModel;
import com.example.EcommerceApp.utils.AndroidUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class EditProfileActivity extends AppCompatActivity {
    ImageView profilePic;
    ImageView backButton;
    EditText usernameInput;
    EditText addressInput;
    EditText phoneInput;
    EditText emailInput;
    Button updateProfileButton;
    ProgressBar progressBar;

    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePic = findViewById(R.id.profieImageView);
        usernameInput = findViewById(R.id.userNameEditText);
        addressInput = findViewById(R.id.addressEditText);
        phoneInput = findViewById(R.id.phoneNumberEditText);
        emailInput = findViewById(R.id.emailEditText);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        backButton = findViewById(R.id.backImageView);
        progressBar = findViewById(R.id.progressBar);

        getUserData();

        backButton.setOnClickListener((v -> {
            onBackPressed();
        }));

        updateProfileButton.setOnClickListener((v -> {
            updateBtnClick();
        }));

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(this ,selectedImageUri,profilePic);
                        }
                    }
                }
        );

        profilePic.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
    }

    void getUserData(){
        setInProgress(true);

        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setProfilePic(this,uri,profilePic);
                    }
                });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUserModel = task.getResult().toObject(UserModel.class);
            if(currentUserModel.getUsername() != null)
                usernameInput.setText(currentUserModel.getUsername());
            else
                usernameInput.setText(FirebaseUtil.currentUserId());
            phoneInput.setText(currentUserModel.getPhone());
            addressInput.setText(currentUserModel.getAddress());
            emailInput.setText(currentUserModel.getEmail());
        });
    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            updateProfileButton.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            updateProfileButton.setVisibility(View.VISIBLE);
        }
    }

    void updateBtnClick(){
        String newUsername = usernameInput.getText().toString();
        String newAddress = addressInput.getText().toString();
        String newPhoneNumber = phoneInput.getText().toString();


        if(newUsername.isEmpty() || newUsername.length()<3){
            usernameInput.setError("Username length should be at least 3 chars");
            return;
        }

        if(newPhoneNumber.isEmpty()){
            phoneInput.setError("Phone number should not be empty");
            return;
        }else if (!newPhoneNumber.matches("(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b")){
            phoneInput.setError("Invalid phone number");
            return;
        }

        if(newAddress.isEmpty()){
            addressInput.setError("Address should not be empty");
            return;
        }

        currentUserModel.setUsername(newUsername);
        currentUserModel.setPhone(newPhoneNumber);
        currentUserModel.setAddress(newAddress);
        setInProgress(true);


        if(selectedImageUri!=null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        updateToFirestore();
                    });
        }else
            updateToFirestore();
    }

    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(this,"Updated successfully");
                    }else{
                        AndroidUtil.showToast(this,"Updated failed");
                    }
                });
    }
}