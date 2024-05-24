package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.domain.user.FavoriteRepository;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.model.Favorite;
import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.utils.FavoriteUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout swipeRefreshLayout;

    ProductAdapter productAdapter, productAdapterforFavorite;

    androidx.recyclerview.widget.RecyclerView viewArrifals;
    androidx.recyclerview.widget.RecyclerView viewFavorites;
    LinearLayout hide_layout_favorite;
    ProductRepository productRepository;

    FavoriteRepository favoriteRepository;

    public HomeHomeFragment() {
        productRepository = new ProductRepository(getContext());
        favoriteRepository = new FavoriteRepository(getContext());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home_home.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeHomeFragment newInstance(String param1, String param2) {
        HomeHomeFragment fragment = new HomeHomeFragment();
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
    @SuppressLint("SuspiciousIndentation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_home, container, false);
        ImageSlider imageSlider = view.findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        TextView seeAllTextView = view.findViewById(R.id.btnSeeAll1);
        TextView btnSeeAll2 = view.findViewById(R.id.btnSeeAll2);
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/ac/f8/2e/acf82efc1950addf7e06c6854d966b67.jpg",  ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/56/df/e5/56dfe56ccbd928b020a1480931166c52.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://i.pinimg.com/564x/41/28/e5/4128e5984d2e40ef30e6b8ae0eef8095.jpg", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        viewArrifals = view.findViewById(R.id.viewArrivals);
        viewFavorites = view.findViewById(R.id.viewFavorites);
        hide_layout_favorite = view.findViewById(R.id.hide_layout_fav);
        productAdapter = new ProductAdapter(getContext(), new ArrayList<>());
        productRepository.get2ProductsAsList().addOnCompleteListener(task -> {
            List<Product> products = task.getResult();
            if (products.size() != 0)
                view.findViewById(R.id.pbNewArrive).setVisibility(View.INVISIBLE);
                viewArrifals.setVisibility(View.VISIBLE);
                productAdapter.updateData(products);
                GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(), 2);
                viewArrifals.setLayoutManager(layoutManagerProduct);
                viewArrifals.setAdapter(productAdapter);
        });
        seeAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllArrivalActivity.class);
                startActivity(intent);
            }
        });
        favoriteRepository.getAllFavoriteAsListByUserID(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(task -> {
            List<String> favorites = task.getResult();
            List<Product> products = new ArrayList<>();
            ProductAdapter productAdapterforFavorite = new ProductAdapter(getContext(), products);
            FavoriteUtil.setAdapter(productAdapter, productAdapterforFavorite);
            FavoriteUtil.setFavoriteProductID(favorites);
            productAdapterforFavorite.setFavoriteProductID(favorites);
            productAdapter.setFavoriteProductID(favorites);
                for (String favorite : favorites) {
                    String productId = favorite;

                    productRepository.getProductByProductId(productId).addOnCompleteListener(task1 -> {
                            Product product = task1.getResult();
                            product.setFavorite(true);
                            products.add(product);
                            productAdapterforFavorite.notifyDataSetChanged();
                    });
                }
                Log.i("DASDsadasdasd", "" + favorites.size());
            if (favorites.size() == 0){
                hide_layout_favorite.setVisibility(View.GONE);
                viewFavorites.setVisibility(View.GONE);

            }
            else {
                hide_layout_favorite.setVisibility(View.VISIBLE);
                viewFavorites.setVisibility(View.VISIBLE);
                productAdapterforFavorite.updateData(products);
                GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(), 2);
                viewFavorites.setLayoutManager(layoutManagerProduct);
                viewFavorites.setAdapter(productAdapterforFavorite);
            }
        });
        btnSeeAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllFavoriteActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadThisFragment();
            }
        });
        return view;
    }
    private void reloadThisFragment() {
        favoriteRepository.getAllFavoriteAsListByUserID(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(task -> {
            List<String> favorites = task.getResult();
            List<Product> products = new ArrayList<>();
            ProductAdapter productAdapterforFavorite = new ProductAdapter(getContext(), products);
            FavoriteUtil.setAdapter(productAdapter, productAdapterforFavorite);
            FavoriteUtil.setFavoriteProductID(favorites);
            productAdapterforFavorite.setFavoriteProductID(favorites);
            productAdapter.setFavoriteProductID(favorites);
            for (String favorite : favorites) {
                String productId = favorite;

                productRepository.getProductByProductId(productId).addOnCompleteListener(task1 -> {
                    Product product = task1.getResult();
                    product.setFavorite(true);
                    products.add(product);
                    productAdapterforFavorite.notifyDataSetChanged();
                });
            }
            if (favorites.size() == 0){
                hide_layout_favorite.setVisibility(View.GONE);
                viewFavorites.setVisibility(View.GONE);
            }
            else {
                hide_layout_favorite.setVisibility(View.VISIBLE);
                viewFavorites.setVisibility(View.VISIBLE);
                productAdapterforFavorite.updateData(products);
                GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(), 2);
                viewFavorites.setLayoutManager(layoutManagerProduct);
                viewFavorites.setAdapter(productAdapterforFavorite);
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }
}