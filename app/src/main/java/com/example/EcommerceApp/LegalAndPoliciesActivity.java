package com.example.EcommerceApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

public class LegalAndPoliciesActivity extends AppCompatActivity {
    Lorem lorem;
    TextView terms;
    TextView changes;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_and_policies);

        terms = findViewById(R.id.termsTV);
        changes = findViewById(R.id.changesTV);
        backButton = findViewById(R.id.backImageView);

        lorem = LoremIpsum.getInstance();
        generateText();
        backButton.setOnClickListener(view1->{
            finish();
        });
    }

    void generateText(){
        terms.setText(lorem.getParagraphs(4,6));
        changes.setText(lorem.getParagraphs(6,8));
    }
}