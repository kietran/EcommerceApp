package com.example.EcommerceApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.domain.user.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResult#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResult extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProductAdapter productAdapter;
    androidx.recyclerview.widget.RecyclerView rc_result_product;
    ProductRepository productRepository;
    public SearchResult() {
        productRepository = new ProductRepository(getContext());
        productAdapter = new ProductAdapter(new ArrayList<>());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResult.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResult newInstance(String param1, String param2) {
        SearchResult fragment = new SearchResult();
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
    public void setListForAdapter(List<Product> products)
    {
        productAdapter.updateData(products);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        ////// RECYCLERVIEW PRODUCT
        rc_result_product = view.findViewById(R.id.rc_result_product);
        rc_result_product.setAdapter(productAdapter);
        GridLayoutManager layoutManagerProduct = new GridLayoutManager(getContext(),2);
        rc_result_product.setLayoutManager(layoutManagerProduct);
        Log.println(Log.ASSERT, "Load product rs", "fragment");

        return view;
    }
}