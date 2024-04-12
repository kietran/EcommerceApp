package com.example.EcommerceApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_onboarding);

        ImageSlider imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.onboarding,"Various Collection Of The Latest Products",  ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.onboarding1, "Complete Collection Of Colors And Sizes", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.onboarding2,"Find The Most Suitable Outfit For You", ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.onboarding3, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);



    }

    public void signUp(View view){
        startActivity(new Intent(MainActivity.this, com.example.EcommerceApp.SignUp_Activity.class));
    }

    public void signIn(View view){
        Toast.makeText(MainActivity.this,"aaaaaa", Toast.LENGTH_SHORT);

        startActivity(new Intent(MainActivity.this,com.example.EcommerceApp.SignIn_Activity.class));
    }
//
//    private void skipAuth(){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user!=null)
//            startActivity(new Intent(MainActivity.this,UserActivity.class));
//        else return;
//    }
}