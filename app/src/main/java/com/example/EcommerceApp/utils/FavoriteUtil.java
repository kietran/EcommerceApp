package com.example.EcommerceApp.utils;

import com.example.EcommerceApp.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoriteUtil {

    private static ProductAdapter productAdapterForFavorite;
    private static ProductAdapter productAdapter;

    private static List<String> favoriteProductID;
    public static void setAdapter(ProductAdapter _productAdapter, ProductAdapter _productAdapterForFavorite){
        productAdapter = _productAdapter;
        productAdapterForFavorite = _productAdapterForFavorite;
    }

    public static void setFavoriteProductID(List<String> _favoriteProductID){
        favoriteProductID = _favoriteProductID;
    }
    public static void addFavorite(String productId, String userId){
        FirebaseUtil.addFavorite(productId, userId);
        favoriteProductID.add(productId);
        productAdapter.notifyDataSetChanged();
        productAdapterForFavorite.notifyDataSetChanged();
    }

    public static void removeFavorite(String productId, String userId){
        FirebaseUtil.removeFavorite(productId, userId);
        favoriteProductID.remove(productId);
        productAdapter.notifyDataSetChanged();
        productAdapterForFavorite.notifyDataSetChanged();
    }
}
