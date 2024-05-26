package com.example.EcommerceApp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.CheckoutProductViewHolder>{
    private List<ShoppingCartItem> mListShoppingCartItem;

    public CheckoutProductAdapter(List<ShoppingCartItem> shoppingCartItems) {
        this.mListShoppingCartItem=shoppingCartItems;
    }

    @NonNull
    @Override
    public CheckoutProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_product,parent,false);

        return new CheckoutProductAdapter.CheckoutProductViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutProductViewHolder holder, int position) {
        ShoppingCartItem item = mListShoppingCartItem.get(position);
        if(item==null)
            return;
        Map<String,Object> product = (Map<String,Object>)item.getProduct_item().get("product");
        String name = (String) product.get("name");
        long price =(long) product.get("price");
        int qty = item.getQty();
        String product_image =(String) product.get("product_image");
        holder.product_name.setText(name);
        String priceStr =String.valueOf(price)+"vnd";
        holder.product_price.setText(priceStr);
        String qtyText="qty:"+String.valueOf(qty);
        holder.qty.setText(qtyText);
        Picasso.get().load(product_image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.product_image);
    }

    @Override
    public int getItemCount() {
        if(mListShoppingCartItem!=null)
            return mListShoppingCartItem.size();
        else return 0;
    }

    public static class CheckoutProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView product_name;
        private final TextView product_price;
        private final TextView qty;
        private final ImageView product_image;

        public CheckoutProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_image = itemView.findViewById(R.id.product_image);
            qty = itemView.findViewById(R.id.qty);
        }
    }

}