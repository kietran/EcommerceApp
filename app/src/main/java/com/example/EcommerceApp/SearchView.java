package com.example.EcommerceApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.EcommerceApp.adapter.SearchAdapter;
import com.example.EcommerceApp.model.Search;
import com.example.EcommerceApp.domain.user.SearchRepository;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SearchAdapter lastSearchAdapter;
    SearchAdapter popularSearchAdapter;
    androidx.recyclerview.widget.RecyclerView rc_last_search;
    androidx.recyclerview.widget.RecyclerView rc_popular_search;
    SearchRepository searchRepository;


    public void setSearchView(androidx.appcompat.widget.SearchView searchView) {
        this.searchView = searchView;
    }
    public void setSearchFilter(SearchFilter searchFilter) {
        this.searchFilter=searchFilter;
    }
    SearchFilter searchFilter;

    androidx.appcompat.widget.SearchView searchView;


    public SearchView() {
        searchRepository = new SearchRepository(getContext());
        lastSearchAdapter = new SearchAdapter(new ArrayList<>());
        popularSearchAdapter = new SearchAdapter(new ArrayList<>());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchView.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchView newInstance(String param1, String param2) {
        SearchView fragment = new SearchView();
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
        View view = inflater.inflate(R.layout.fragment_search_view, container, false);
        ////// RECYCLERVIEW HISTORY SEARCH
        rc_last_search = view.findViewById(R.id.rc_last_search);
        searchRepository.getRecentSearches(6).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Search> searches = task.getResult();
                if(searches.size()>0)
                {
                    LinearLayout linearLayout = view.findViewById(R.id.layout_last_search);
                    linearLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    LinearLayout linearLayout = view.findViewById(R.id.layout_popular_search);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, 0);
                    linearLayout.setLayoutParams(params);

                }
                lastSearchAdapter.updateData(searches,searchView,searchFilter);
                rc_last_search.setAdapter(lastSearchAdapter);
                ProgressBar progressBar =view.findViewById(R.id.prbLastSearch);
                progressBar.setVisibility(View.INVISIBLE);
                Log.println(Log.ASSERT, "Load search", String.valueOf(lastSearchAdapter.getItemCount()));
            }
            else {
                Exception e = task.getException();
                if (e != null) {
                    Log.println(Log.ASSERT, "Load search", Objects.requireNonNull(e.getMessage()));

                    Toast.makeText(getContext(), "Error loading products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        FlexboxLayoutManager layoutManagerHistory = new FlexboxLayoutManager(getContext());
        rc_last_search.setLayoutManager(layoutManagerHistory);

        ////// RECYCLERVIEW SUGGUESTIONS SEARCH
        rc_popular_search = view.findViewById(R.id.rc_popular_search);
        searchRepository.getTopMostSearchedContents(6).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<Search> searches = task.getResult();
                popularSearchAdapter.updateData(searches,searchView,searchFilter);
                rc_popular_search.setAdapter(popularSearchAdapter);
                ProgressBar progressBar =view.findViewById(R.id.prbPopularSearch);
                progressBar.setVisibility(View.INVISIBLE);
                Log.println(Log.ASSERT, "Load search", String.valueOf(popularSearchAdapter.getItemCount()));
            }
            else {
                Exception e = task.getException();
                if (e != null) {
                    Log.println(Log.ASSERT, "Load search", Objects.requireNonNull(e.getMessage()));

                    Toast.makeText(getContext(), "Error loading products: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        FlexboxLayoutManager layoutManagerSuggestion = new FlexboxLayoutManager(getContext());

        rc_popular_search.setLayoutManager(layoutManagerSuggestion);

        return view;
    }


}