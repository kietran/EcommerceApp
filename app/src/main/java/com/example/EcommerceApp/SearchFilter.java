    package com.example.EcommerceApp;

    import android.os.Bundle;

    import androidx.appcompat.widget.SearchView;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.DividerItemDecoration;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import com.example.EcommerceApp.adapter.UserSearchAdapter;
    import com.example.EcommerceApp.model.Product;
    import com.example.EcommerceApp.domain.user.ProductRepository;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Objects;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link SearchFilter#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class SearchFilter extends Fragment {

        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;
        public UserSearchAdapter searchAdapter;
        androidx.recyclerview.widget.RecyclerView rc_filter;
        ProductRepository productRepository;
        androidx.appcompat.widget.SearchView searchView;


        public SearchFilter() {
            productRepository = new ProductRepository(getContext());
            searchAdapter = new UserSearchAdapter(new ArrayList<>());
            productRepository.getAllProductsAsList().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    List<Product> productList = task.getResult();
                    searchAdapter.updateData(productList,searchView);
                    Log.println(Log.ASSERT, "Load product", String.valueOf(searchAdapter.getItemCount()));

                }
                else {
                    Exception e = task.getException();
                    if (e != null) {
                        Log.println(Log.ASSERT, "Load product", Objects.requireNonNull(e.getMessage()));
                    }
                }
            });
        }

        public SearchFilter(String shopId) {
            productRepository = new ProductRepository(getContext());
            searchAdapter = new UserSearchAdapter(new ArrayList<>());
            productRepository.getAllProductsAsListByShopId(shopId).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    List<Product> productList = task.getResult();
                    searchAdapter.updateData(productList,searchView);
                    Log.println(Log.ASSERT, "Load product", String.valueOf(searchAdapter.getItemCount()));

                }
                else {
                    Exception e = task.getException();
                    if (e != null) {
                        Log.println(Log.ASSERT, "Load product", Objects.requireNonNull(e.getMessage()));

                        Toast.makeText(getContext(), "Error loading products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        public void setSearchView(SearchView searchView) {
            this.searchView=searchView;
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFilter.
         */
        // TODO: Rename and change types and number of parameters
        public static SearchFilter newInstance(String param1, String param2) {
            SearchFilter fragment = new SearchFilter();
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


            ////////// RECYCLERVIEW FILTER TEXT
            View view = inflater.inflate(R.layout.fragment_search_filter, container, false);
            rc_filter = view.findViewById(R.id.rc_filter);
            LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
            rc_filter.setLayoutManager(linearLayout);
            rc_filter.setAdapter(searchAdapter);

            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
            rc_filter.addItemDecoration(itemDecoration);
            return view;
        }


    }
