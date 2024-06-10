package com.example.EcommerceApp.domain.user;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private FirebaseAuth auth;
    private Context context;
    private FirebaseFirestore db;
    private Timestamp timestamp;


    public UserRepository(Context context) {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        timestamp = Timestamp.now();
        this.context = context;
    }

    public boolean checkUserIsLoggedIn(){
        return auth.getCurrentUser() != null;
    }

    public void createUser(Context context, EditText email, EditText username, EditText password) {
        String userName = username.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        //Create user
        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "User Created!", Toast.LENGTH_SHORT).show();
                    String userId = auth.getCurrentUser().getUid();
                    saveUser(userId, userName, userEmail, userPassword, timestamp);
                    navigateToHomepage();

                }else {
                    Toast.makeText(context, "Error " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public Task<Void> createUserWithEmail(String email) {
        //
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        Task<QuerySnapshot> queryTask = db.collection("users").whereEqualTo("email", email).get();
        return queryTask.continueWithTask(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    FirebaseUser currentUser = auth.getCurrentUser();
                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        String userName = email.substring(0, email.indexOf('@'));
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", userName);
                        user.put("password", null);
                        user.put("email", email);
                        user.put("createdTimestamp", timestamp);

                        return db.collection("users").document(userId).set(user);
                    } else {
                        return Tasks.forException(new IllegalStateException("User not authenticated"));
                    }
                } else {
                    return Tasks.forResult(null); // User already exists, no new user creation needed
                }
            } else {
                return Tasks.forException(task.getException());
            }
        });
    }

    public void navigateToHomepage() {
        Intent intent = new Intent(context, UserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear back stack
        context.startActivity(intent);
    }

    public void saveUser(String userId, String userName,String userEmail, String userPassword, Timestamp timestamp ){
        DocumentReference documentReference = db.collection("users").document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("username", userName);
        user.put("password", userPassword);
        user.put("email", userEmail);
        user.put("createdTimestamp", timestamp);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("TAG", "onSuccess: User " + userId + "is created!");
            }
        });


    }
}
