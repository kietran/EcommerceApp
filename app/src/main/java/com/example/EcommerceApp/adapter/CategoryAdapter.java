package com.example.EcommerceApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.AllArrivalActivity;
import com.example.EcommerceApp.AllCategoryProduct;
import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> mListCategories;
    private Context mContext;
    public CategoryAdapter(Context context, List<Category> mListCategories) {
        this.mContext = context;
        this.mListCategories = mListCategories;}

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final Category category = mListCategories.get(position);
        if(category==null)
            return;

        holder.category_name.setText(category.getName());
        Picasso.get().load(category.getCategory_image())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.category_image);
        holder.imageCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoToAllProduct(category);
            }
        });
    }
    private void onClickGoToAllProduct(Category category){
        Intent intent = new Intent(mContext, AllCategoryProduct.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object category", category);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        if(mListCategories!=null)
            return mListCategories.size();
        else return 0;
    }

    public void updateData(List<Category> categoriesList) {
        this.mListCategories = categoriesList;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        private final TextView category_name;
        private final ImageView category_image;
        private final ImageView imageCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.category_image);
            category_name = itemView.findViewById(R.id.category_name);
            category_image = itemView.findViewById(R.id.category_image);
        }
    }
}
