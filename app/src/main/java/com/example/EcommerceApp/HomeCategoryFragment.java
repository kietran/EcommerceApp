package com.example.EcommerceApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EcommerceApp.adapter.CategoryAdapter;
import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.domain.user.CategoryRepository;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.model.Category;
import com.example.EcommerceApp.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeCategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CategoryAdapter categoryAdapter;
    androidx.recyclerview.widget.RecyclerView recyclerView;
    CategoryRepository categoryRepository;
    public HomeCategoryFragment() {
        categoryRepository = new CategoryRepository(getContext());
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home_category.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeCategoryFragment newInstance(String param1, String param2) {
        HomeCategoryFragment fragment = new HomeCategoryFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        categoryAdapter = new CategoryAdapter(getContext(), new ArrayList<>());
        categoryRepository.getAllCategoriesAsList().addOnCompleteListener(task -> {
            List<Category> categories = task.getResult();
            categoryAdapter.updateData(categories);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(categoryAdapter);
        });
        return view;
    }
}
