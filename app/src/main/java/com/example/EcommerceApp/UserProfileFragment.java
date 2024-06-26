package com.example.EcommerceApp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.EcommerceApp.domain.user.ShopRepository;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.model.User;
import com.example.EcommerceApp.utils.AndroidUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment {
    Button editProfile;
    Button changePassword;
    Button legalAndPolicies;
    Button logout;
    Button sellOn23,orderManagement;
    TextView username;
    TextView email;
    ImageView profilePic;
    SwitchCompat nightMode;
    User currentUserModel;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        editProfile = view.findViewById(R.id.editProfileButton);
        changePassword = view.findViewById(R.id.changePasswordButton);
        legalAndPolicies = view.findViewById(R.id.legalAndPoliciesButton);
        logout = view.findViewById(R.id.logoutButton);
        nightMode = view.findViewById(R.id.notificationSwitch);
        username = view.findViewById(R.id.usernameTextView);
        email = view.findViewById(R.id.emailTextView);
        profilePic = view.findViewById(R.id.profileImageView);
        sellOn23 = view.findViewById(R.id.sellOn23Button);
        orderManagement = view.findViewById(R.id.orderManagement);

        getUserData();
        getShop();
        editProfile.setOnClickListener(view1->{
            startActivity(new Intent(getContext(), EditProfileActivity.class));
        });

        legalAndPolicies.setOnClickListener(view1->{
            startActivity(new Intent(getContext(), LegalAndPoliciesActivity.class));
        });

        changePassword.setOnClickListener(view1->{
            startActivity(new Intent(getContext(), ChangePasswordActivity.class));
        });

        sellOn23.setOnClickListener(view1->{
            startActivity(new Intent(getContext(), SellOn23Activity.class));
        });

        logout.setOnClickListener(view1->{
            onClickLogOut();
        });
        getUserSignInMethod();
        return view;
    }
    public void getUserSignInMethod() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            List<? extends UserInfo> providerData = user.getProviderData();

            for (UserInfo userInfo : providerData) {
                String providerId = userInfo.getProviderId();
                switch (providerId) {
                    case "password":
                        System.out.println("User signed in using Email/Password");
                        break;
                    case "google.com":
                        changePassword.setVisibility(View.GONE);
                        System.out.println("User signed in using Google");
                        break;
                    default:
                        System.out.println("User signed in using another provider: " + providerId);
                        break;
                }
            }
        } else {
            System.out.println("No user is signed in");
        }
    }

    void onClickLogOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(requireActivity(), MainActivity.class));
        requireActivity().finish();
    }

    void getUserData(){
        FirebaseUtil.getCurrentUserPicStorageRef("profile_pic").getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setProfilePic(getContext(),uri,profilePic);
                    }
                });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(User.class);
            if(currentUserModel.getUsername() != null)
                username.setText(currentUserModel.getUsername());
            else
                username.setText(FirebaseUtil.currentUserId());
            email.setText(currentUserModel.getEmail());
        });
    }
    void getShop(){
        ShopRepository shopRepository = new ShopRepository(getContext());
        shopRepository.getShopByUserId().addOnCompleteListener(new OnCompleteListener<Shop>() {
            @Override
            public void onComplete(@NonNull Task<Shop> task) {
                Shop shop= task.getResult();
                if(task.isSuccessful()){
                    if(shop!=null){
                        sellOn23.setVisibility(View.GONE);
                        orderManagement.setVisibility(View.VISIBLE);
                        orderManagement.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(getActivity(), OrderManagement.class);
                                i.putExtra("shopName", shop.getShopName());
                                startActivity(i);
                            }
                        });
                    }
                    else{
                        sellOn23.setVisibility(View.VISIBLE);
                        orderManagement.setVisibility(View.GONE);
                    }
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getShop();
    }
}