package com.example.EcommerceApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.EcommerceApp.domain.user.SearchRepository;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity  {
    ImageView btBack;
    androidx.appcompat.widget.SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


////////// SEARCH VIEW
        SearchView searchView1 = new SearchView();
        SearchFilter searchFilter = new SearchFilter();
        SearchResult searchResult = new SearchResult();



        searchView = findViewById(R.id.sv_item);
        searchView1.setSearchView(searchView);
        searchView1.setSearchFilter(searchFilter);
        searchFilter.setSearchView(searchView);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchRepository searchRepository = new SearchRepository(SearchActivity.this);
                searchRepository.add(query);
                searchResult.setListForAdapter(searchFilter.searchAdapter.getmListProducts());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_search, searchResult)
                        .addToBackStack(null)
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!Objects.equals(newText, "")) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_search, searchFilter)
                            .addToBackStack(null)
                            .commit();
                    searchFilter.searchAdapter.getFilter().filter(newText);
                    searchResult.setListForAdapter(searchFilter.searchAdapter.getmListProducts());
                }
                else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_search, searchView1)
                            .addToBackStack(null)
                            .commit();
                }
                return false;
            }
        });


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_search, searchView1)
                .addToBackStack(null)
                .commit();
        ///////// BUTTON BACK
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchActivity.this, UserActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}