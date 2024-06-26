package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EcommerceApp.adapter.HomePageAdapter;
import com.example.EcommerceApp.domain.user.ShopRepository;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.model.User;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.example.EcommerceApp.utils.ShopUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeUserFragment extends Fragment {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private User currentUserModel;
    ImageView btnSearch;
    TextView hiName;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeUser.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeUserFragment newInstance(String param1, String param2) {
        HomeUserFragment fragment = new HomeUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_user, container, false);
        viewPager = rootView.findViewById(R.id.view_pager);
        tabLayout = rootView.findViewById(R.id.tab_layout);
        hiName = rootView.findViewById(R.id.hiName);

        getUserData();
        ShopRepository shopRepository= new ShopRepository(getContext());
        shopRepository.getShopByUserId().addOnCompleteListener(new OnCompleteListener<Shop>() {
            @Override
            public void onComplete(@NonNull Task<Shop> task) {
                if(task.isSuccessful()){
                    if(task.isComplete()){
                        Shop shop= task.getResult();
                        if(shop!=null)
                            ShopUtil.setShopId(shop.getShopId());
                        else
                            ShopUtil.setShopId("");
                        // Tạo adapter và thiết lập cho ViewPager2
                        HomePageAdapter adapter = new HomePageAdapter(requireActivity());
                        viewPager.setAdapter(adapter);

                        // Kết nối ViewPager2 với TabLayout
                        new TabLayoutMediator(tabLayout, viewPager,
                                (tab, position) -> {
                                    if (position == 0) {
                                        tab.setText("Home");
                                    } else if (position == 1) {
                                        tab.setText("Category");
                                    }
                                }
                        ).attach();
                    }
                }
            }
        });


        btnSearch = rootView.findViewById(R.id.btSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

    void getUserData(){
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(User.class);
            if(currentUserModel.getUsername() != null)
                hiName.setText(getString(R.string.welcome_message, currentUserModel.getUsername()));
            else
                hiName.setText(getString(R.string.welcome_message, FirebaseUtil.currentUserId()));
        });
    }
}