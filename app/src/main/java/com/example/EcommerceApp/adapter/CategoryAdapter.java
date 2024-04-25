package com.example.EcommerceApp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> mListCategories;

    public CategoryAdapter(List<Category> mListCategories) {
        this.mListCategories = mListCategories;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);

        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = mListCategories.get(position);
        if(category==null)
            return;

        holder.category_name.setText(category.getName());
        holder.category_numProduct.setText(category.getNumProduct());
        Picasso.get().load(category.getCategory_image())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.category_image);
    }

    @Override
    public int getItemCount() {
        if(mListCategories!=null)
            return mListCategories.size();
        else return 0;
    }

    public void updateData(List<Category>categoriesList) {
        this.mListCategories = categoriesList;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        private final TextView category_name;
        private final TextView category_numProduct;
        private final ImageView category_image;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            category_numProduct = itemView.findViewById(R.id.category_numProduct);
            category_image = itemView.findViewById(R.id.category_image);
        }
    }
}
