package com.example.EcommerceApp.domain.user;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.HomeUser;
import com.example.EcommerceApp.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private FirebaseAuth auth;
    private Context context;
    private FirebaseFirestore db;

    public UserRepository(Context context) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public boolean checkUserIsLoggedIn(){
        return auth.getCurrentUser() != null;
    }

    public void createUser(Context context, EditText email, EditText username, EditText password) {
        String userName = username.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        // Check if any fields is empty
        if (TextUtils.isEmpty(userName)){
            Toast.makeText(context, "Name is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(context, "Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(context, "Password is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() <= 6){
            Toast.makeText(context, "Password must be longer than 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create user
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "User Created!", Toast.LENGTH_SHORT).show();
                    String userId = auth.getCurrentUser().getUid();
                    saveUser(userId, userName, userEmail, userPassword);
                    navigateToHomepage();

                }else {
                    Toast.makeText(context, "Error " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void navigateToHomepage() {
        Intent intent = new Intent(context, UserActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
        context.startActivity(intent);
    }

    public void saveUser(String userId, String userName,String userEmail, String userPassword ){
        DocumentReference documentReference = db.collection("users").document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("userName", userName);
        user.put("password", userPassword);
        user.put("email", userEmail);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "onSuccess: User " + userId + "is created!");
            }
        });
    }
}
