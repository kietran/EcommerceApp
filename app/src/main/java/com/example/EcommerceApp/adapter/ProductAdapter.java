package com.example.EcommerceApp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> mListProducts;
    private Shop shopModel;

    public ProductAdapter(List<Product> mListProducts) {
        this.mListProducts = mListProducts;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mListProducts.get(position);
        if(product==null)
            return;

        setShopName(holder, product.getShop_id());
        holder.product_name.setText(product.getName());
        holder.product_price.setText(String.valueOf(product.getPrice()));
        Picasso.get().load(product.getProduct_image())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        if(mListProducts!=null)
            return mListProducts.size();
        else return 0;
    }

    public void updateData(List<Product> productList) {
        this.mListProducts = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        private final TextView product_name;
        private final TextView product_price;
        private TextView shop_name;
        private final ImageView product_image;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            shop_name = itemView.findViewById(R.id.shop_name);
            product_image = itemView.findViewById(R.id.product_image);
        }
    }

    public void setShopName(@NonNull ProductViewHolder holder, String shopId){
        FirebaseUtil.getShopReference(shopId).get().addOnCompleteListener(task -> {
            shopModel = task.getResult().toObject(Shop.class);
            holder.shop_name.setText(shopModel.getShopName());
        });

    }
}
