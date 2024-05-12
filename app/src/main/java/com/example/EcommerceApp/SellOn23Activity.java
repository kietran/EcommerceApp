package com.example.EcommerceApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EcommerceApp.domain.user.ShopRepository;
import com.example.EcommerceApp.domain.user.UserRepository;
import com.example.EcommerceApp.utils.AndroidUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SellOn23Activity extends AppCompatActivity {
    EditText shopName;
    EditText phoneNumber;
    EditText shopAddress;
    TextView logoTV;
    TextView bannerTV;
    ImageView back;
    ImageView logo;
    ImageView banner;
    CheckBox agreeTermsAndConditions;
    Button submit;
    ActivityResultLauncher<Intent> logoPickLauncher;
    Uri logoSelectedImageUri;
    ActivityResultLauncher<Intent> bannerPickLauncher;
    Uri bannerSelectedImageUri;
    ShopRepository shopRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_on23);

        shopRepository = new ShopRepository(SellOn23Activity.this);
        shopName = findViewById(R.id.shopNameEditText);
        phoneNumber = findViewById(R.id.phoneNumberEditText);
        shopAddress = findViewById(R.id.shopAddressEditText);
        logo = findViewById(R.id.logoImageView);
        banner = findViewById(R.id.bannerImageView);
        agreeTermsAndConditions = findViewById(R.id.agreeCheckBox);
        submit = findViewById(R.id.submitButton);
        logoTV = findViewById(R.id.logoTextView);
        bannerTV = findViewById(R.id.bannerTextView);
        back = findViewById(R.id.backImageView);


        back.setOnClickListener(view1->{
            finish();
        });

        String text1 = "<font color='#000000'>Image Size </font><font color='#FF0000'>(1:1)</font>";
        logoTV.setText(Html.fromHtml(text1));

        String text2 = "<font color='#000000'>Image Size </font><font color='#FF0000'>(3:1)</font>";
        bannerTV.setText(Html.fromHtml(text2));

        String text3 = "I agree to all the <b>Terms & Conditions</b>";
        agreeTermsAndConditions.setText(Html.fromHtml(text3));

        submit.setOnClickListener((v -> {
            submitBtnClick();
        }));

        agreeTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agreeTermsAndConditions.isChecked()){
                    submit.setBackgroundResource(R.drawable.btn_background_2);
                    submit.setEnabled(true);
                }
                else{
                    submit.setBackgroundResource(R.drawable.btn_background_3);
                    submit.setEnabled(false);
                }
            }
        });

        logoPickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            logoSelectedImageUri = data.getData();
                            AndroidUtil.setShopProfilePic(this ,logoSelectedImageUri,logo);
                        }
                    }
                }
        );

        logo.setOnClickListener((v)->{
            ImagePicker.with(this).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            logoPickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        bannerPickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            bannerSelectedImageUri = data.getData();
                            AndroidUtil.setShopProfilePic(this ,bannerSelectedImageUri,banner);
                        }
                    }
                }
        );

        banner.setOnClickListener((v)->{
            ImagePicker.with(this).maxResultSize(1536,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            bannerPickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
    }

    void submitBtnClick(){
        boolean check = true;
        check = checkValidInformation();

        if(check){
            shopRepository.saveShop(shopName.getText().toString(),phoneNumber.getText().toString()
                    ,shopAddress.getText().toString(),logoSelectedImageUri.toString(),bannerSelectedImageUri.toString(), new ShopRepository.OnShopSaveListener(){
                        @Override
                        public void onShopSaved(String shopId) {
                            FirebaseUtil.getCurrentShopPicStorageRef("logo_shop_pic", shopId).putFile(logoSelectedImageUri)
                                    .addOnCompleteListener(task -> {
                                        updateToFirestore();
                                    });

                            FirebaseUtil.getCurrentShopPicStorageRef("banner_shop_pic", shopId).putFile(bannerSelectedImageUri)
                                    .addOnCompleteListener(task -> {
                                        updateToFirestore();
                                    });
                        }
                    });
            finish();
        }
    }

    boolean checkValidInformation(){
        boolean check = true;
        if (TextUtils.isEmpty(shopName.getText().toString())){
            shopName.setError("Shop name is empty!");
            check = false;
        }

        if (TextUtils.isEmpty(phoneNumber.getText().toString())){
            phoneNumber.setError("Phone number is empty!");
            check = false;
        }

        if (TextUtils.isEmpty(shopAddress.getText().toString())){
            shopAddress.setError("Shop address is empty!");
            check = false;
        }

        if (logoSelectedImageUri == null){
            AndroidUtil.showToast(this, "Logo Image is required!");
            check = false;
        }

        if (bannerSelectedImageUri == null){
            AndroidUtil.showToast(this, "Banner Image is required!");
            check = false;
        }

        return check;
    }

    void updateToFirestore(){

    }
}