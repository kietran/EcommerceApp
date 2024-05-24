package com.example.EcommerceApp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.DetailOneAttributeActivity;
import com.example.EcommerceApp.DetailNoAttributeActivity;
import com.example.EcommerceApp.DetailTwoAttributeActivity;
import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.utils.FavoriteUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> mListProducts;
    private Shop shopModel;
    private Context mContext;
    private List<String> favoriteProductID = new ArrayList<>();
    private boolean isHaveColor = false, isHaveSize = false;
    @SuppressLint("NotififyDataSetChanged")
    public void setFavoriteProductID(List<String> favoriteProductID) {
        this.favoriteProductID = favoriteProductID;
        notifyDataSetChanged();
    }


    public ProductAdapter(Context context, List<Product> mListProducts) {
        this.mListProducts = mListProducts;
        this.mContext = context;
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

        int drawableId = R.drawable.red_favorite;
        int drawableId2 = R.drawable.fav_icon;
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        Drawable drawable2 = mContext.getResources().getDrawable(drawableId2);
        holder.btnFav.setImageDrawable(drawable);
        setShopName(holder, product.getShop_id());
        holder.product_name.setText(product.getName());
        holder.product_price.setText(String.valueOf(product.getPrice()));

        if(favoriteProductID.size() != 0 && favoriteProductID.contains(product.getId())) {
            holder.btnFav.setImageDrawable(drawable);
        }
        else{
            holder.btnFav.setImageDrawable(drawable2);
        }
        Picasso.get().load(product.getProduct_image())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.product_image);
        holder.imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoToDetail(product);
            }
        });

        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productId = product.getId();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (favoriteProductID.contains(productId)) {
//                    FirebaseUtil.removeFavorite(productId, userId);
//                    favoriteProductID.remove(productId);
                    FavoriteUtil.removeFavorite(productId, userId);
                    holder.btnFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.fav_icon));
                    Log.i("fav ID", ""+favoriteProductID);
                } else {
//                    FirebaseUtil.addFavorite(productId, userId);
//                    favoriteProductID.add(productId);
                    FavoriteUtil.addFavorite(productId, userId);
                    Log.i("fav ID", ""+favoriteProductID);
                    holder.btnFav.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_favorite));

                }
            }
        });
    }

    private void onClickGoToDetail(Product product) {
        Intent intent;
        switch (product.getCategory_id()) {
            case "bag":
                isHaveColor = true;
                break;
            case "sneaker"  :
                isHaveSize = true;
                break;
            case "electronic":
                break;
            case "book":
                break;
            case "jewelry":
                break;
            case "cloth":
                isHaveColor = true;
                isHaveSize = true;
                break;
            default:
                return;
        }
        intent = new Intent(mContext, DetailTwoAttributeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_product", product);
        bundle.putString("id", product.getId());
        bundle.putBoolean("haveColor", isHaveColor);
        bundle.putBoolean("haveSize", isHaveSize);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
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
        private final ImageView imageProduct;

        private final ImageView btnFav;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            shop_name = itemView.findViewById(R.id.shop_name);
            product_image = itemView.findViewById(R.id.product_image);
            btnFav = itemView.findViewById(R.id.btn_favorite);
        }
    }

    public void setShopName(@NonNull ProductViewHolder holder, String shopId){
        FirebaseUtil.getShopReference(shopId).get().addOnCompleteListener(task -> {
            shopModel = task.getResult().toObject(Shop.class);
            holder.shop_name.setText(shopModel.getShopName());
        });
    }
}
