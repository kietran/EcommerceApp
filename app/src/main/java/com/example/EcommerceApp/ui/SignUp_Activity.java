package com.example.EcommerceApp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.EcommerceApp.domain.user.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp_Activity extends AppCompatActivity {

    private EditText edEmail, edPassword, edUsername;
    private Button btnSignUp;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edUsername = findViewById(R.id.edUsername);
        btnSignUp = findViewById(R.id.btSignUp);
        userRepository = new UserRepository(SignUp_Activity.this);

        if (userRepository.checkUserIsLoggedIn()){
            startActivity(new Intent(SignUp_Activity.this, UserActivity.class));
            finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                userRepository.createUser(SignUp_Activity.this, edEmail, edUsername, edPassword);
            }
        });
    }
}