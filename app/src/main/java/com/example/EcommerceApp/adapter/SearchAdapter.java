package com.example.EcommerceApp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.SearchFilter;
import com.example.EcommerceApp.model.Search;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Search> mListSearchs;

    private androidx.appcompat.widget.SearchView searchView;
    SearchFilter searchFilter;




    public SearchAdapter(List<Search> mListSearchs) {
        this.mListSearchs = mListSearchs;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_item,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Search search = mListSearchs.get(position);
        if(search==null)
            return;
        //searchFilter.searchAdapter.getFilter().filter(search.getContent());
        holder.content_search.setText(search.getContent());
        holder.layout_item_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFilter.searchAdapter.getFilter().filter(search.getContent());
                searchView.setQuery(search.getContent(),true);
            }
        });
    }



    @Override
    public int getItemCount() {
        if(mListSearchs!=null)
            return mListSearchs.size();
        else return 0;
    }

    public void updateData(List<Search> searches,androidx.appcompat.widget.SearchView searchView,SearchFilter searchFilter) {
        this.mListSearchs = searches;
        this.searchView = searchView;
        this.searchFilter=searchFilter;
    }



    public static class SearchViewHolder extends RecyclerView.ViewHolder{
        private final TextView content_search;
        private LinearLayout layout_item_search;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            content_search = itemView.findViewById(R.id.content_search);
            layout_item_search = itemView.findViewById(R.id.layout_item_search);
        }

    }
}
