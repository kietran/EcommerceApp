package com.example.EcommerceApp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    com.google.android.material.textfield.TextInputLayout layout_edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edEmail = findViewById(R.id.edEmail);
        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_edt=findViewById(R.id.layout_su_ed_em);

                if(!Patterns.EMAIL_ADDRESS.matcher(charSequence.toString()).matches())
                     layout_edt.setError("Input isn't email format");
                else {
                    layout_edt.setHelperText("Email is valid");
                    layout_edt.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edPassword = findViewById(R.id.edPassword);
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_edt=findViewById(R.id.layout_su_ed_pass);
                if (charSequence.length() >= 6) {
                    layout_edt.setHelperText("Password is valid");
                    layout_edt.setError("");
                } else {
                    layout_edt.setError("Password must 6 characters long");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edUsername = findViewById(R.id.edUsername);
        edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout_edt=findViewById(R.id.layout_su_ed_un);
                if (charSequence.length() >= 2) {
                    layout_edt.setHelperText("Username is valid");
                    layout_edt.setError("");
                } else {
                    layout_edt.setError("Username must 2 characters long");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnSignUp = findViewById(R.id.btSignUp);
        userRepository = new UserRepository(SignUp_Activity.this);

        if (userRepository.checkUserIsLoggedIn()){
            startActivity(new Intent(SignUp_Activity.this, UserActivity.class));
            finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                findViewById(R.id.prbSU).setVisibility(View.VISIBLE);
                btnSignUp.setVisibility(View.INVISIBLE);
                boolean check = true;
                if (TextUtils.isEmpty(edUsername.getText().toString())){
                    layout_edt=findViewById(R.id.layout_su_ed_un);
                    layout_edt.setError("Username is empty!");
                    check=false;
                }

                if (TextUtils.isEmpty(edEmail.getText().toString())){
                    layout_edt=findViewById(R.id.layout_su_ed_em);
                    layout_edt.setError("Email is empty!");
                    check=false;
                }

                if (TextUtils.isEmpty(edPassword.getText().toString())){
                    layout_edt=findViewById(R.id.layout_su_ed_pass);
                    layout_edt.setError("Password is empty!");
                    check=false;
                }

                if(check)
                    userRepository.createUser(SignUp_Activity.this, edEmail, edUsername, edPassword);
                else
                {
                    findViewById(R.id.prbSU).setVisibility(View.INVISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}