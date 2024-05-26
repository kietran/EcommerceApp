package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.Product;

import java.util.ArrayList;
import java.util.List;
///// filter while searching

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder> implements Filterable {


    private List<Product> mListProducts;
    private List<Product> mListOldProducts;
    private androidx.appcompat.widget.SearchView searchView;


    public UserSearchAdapter(List<Product> products) {
        this.mListProducts = products;
        this.mListOldProducts= products;
    }

    public UserSearchAdapter(List<Product> products, String userId) {
        this.mListProducts = products;
        this.mListOldProducts= products;
    }

    @NonNull
    @Override
    public UserSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_search_item,parent,false);

        return new UserSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchViewHolder holder, int position) {
        Product product = mListProducts.get(position);
        if(product==null)
            return;

        holder.filterText.setText(product.getName());
        holder.layout_filter_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setQuery(holder.filterText.getText(),true);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListProducts!=null)
            return mListProducts.size();
        else return 0;
    }
    public void updateData(List<Product> products,androidx.appcompat.widget.SearchView searchView)
    {
        this.mListProducts = products;
        this.mListOldProducts = products;
        this.searchView =searchView;
    }
    public List<Product> getmListProducts() {
        return mListProducts;
    }

    public static class UserSearchViewHolder extends RecyclerView.ViewHolder{
        private final TextView filterText;
        private LinearLayout layout_filter_search;
        public UserSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            filterText = itemView.findViewById(R.id.filter_text);
            layout_filter_search = itemView.findViewById(R.id.layout_filter_search);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if(strSearch.isEmpty())
                    mListProducts = mListOldProducts;
                else {
                    List<Product> list = new ArrayList<>();
                    for(Product product:mListOldProducts)
                        if(product.getName().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(product);
                        }
                    mListProducts = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListProducts;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mListProducts = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
