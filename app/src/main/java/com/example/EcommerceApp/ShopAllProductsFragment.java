package com.example.EcommerceApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.domain.user.SearchRepository;
import com.example.EcommerceApp.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopAllProductsFragment extends Fragment {
    ProductAdapter productAdapter;
    androidx.recyclerview.widget.RecyclerView containerSearch;
    ProductRepository productRepository;
    androidx.appcompat.widget.SearchView searchView;

    public ShopAllProductsFragment() {
        productRepository = new ProductRepository(getContext());
        productAdapter = new ProductAdapter(ShopPageActivity.context, new ArrayList<>());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_all_products, container, false);
        String shopId = getArguments().getString("shopId");


        containerSearch = view.findViewById(R.id.container_search);
        SearchView searchView1 = new SearchView();
        SearchFilter searchFilter = new SearchFilter(shopId);
        SearchResult searchResult = new SearchResult();
        searchResult.setContext1(ShopPageActivity.context);
        /////// RECYCLE VIEW
        productRepository.getAllProductsAsListByShopId(shopId).addOnCompleteListener(task -> {
        List<Product> products = task.getResult();
        productAdapter.updateData(products);
        GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(), 2);
        containerSearch.setLayoutManager(layoutManagerProduct);
        containerSearch.setAdapter(productAdapter);
        });



        //// SEARCH VIEW
        searchView = view.findViewById(R.id.sv_item);
        searchView1.setSearchView(searchView);
        searchView1.setSearchFilter(searchFilter);
        searchFilter.setSearchView(searchView);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                containerSearch.setVisibility(View.GONE);

                searchResult.setListForAdapter(searchFilter.searchAdapter.getmListProducts());
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.container_layout, searchResult)
                        .addToBackStack(null)
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!Objects.equals(newText, "")) {
                    searchFilter.searchAdapter.getFilter().filter(newText);
                    searchResult.setListForAdapter(searchFilter.searchAdapter.getmListProducts());
                }
                else {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, new Fragment())
                            .addToBackStack(null)
                            .commit();
                    containerSearch.setVisibility(View.VISIBLE);
                }
                return false;
            }


        });
//        getChildFragmentManager().beginTransaction()
//                .replace(R.id.container_layout, searchView1)
//                .addToBackStack(null)
//                .commit();

        return view;
    }
}