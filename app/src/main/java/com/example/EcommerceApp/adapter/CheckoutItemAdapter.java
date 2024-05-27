package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.ShoppingCartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckoutItemAdapter extends RecyclerView.Adapter<CheckoutItemAdapter.CheckoutItemViewHolder> {
    public final Map<String, List<ShoppingCartItem>> groupedCartItems;
    private final List<String> shopNames;
    private final List<ShoppingCartItem> selectList;
    private final Context context;



    public CheckoutItemAdapter(List<ShoppingCartItem> selectList, Context context) {
        this.selectList = selectList;
        this.context = context;
        this.groupedCartItems = new HashMap<>();
        this.shopNames= new ArrayList<>();
        groupByShop();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void groupByShop(){
        Log.println(Log.INFO,"checkout item",String.valueOf(selectList.size()));
        for (ShoppingCartItem cartItem : selectList) {
            if (cartItem != null) {
                Map<String, Object> product_item = (Map<String, Object>) cartItem.getProduct_item();
                Map<String, Object> product = (Map<String, Object>) product_item.get("product");
                Map<String, Object> shop = (Map<String, Object>) product.get("shop");;
                String shopName =(String)shop.get("name");
                if (!groupedCartItems.containsKey(shopName)) {
                    groupedCartItems.put(shopName, new ArrayList<>());
                }
                Objects.requireNonNull(groupedCartItems.get(shopName)).add(cartItem);
            }
        }
        shopNames.addAll(groupedCartItems.keySet());
        notifyDataSetChanged();
        Log.println(Log.INFO,"size",String.valueOf(getItemCount()));
    }


    @NonNull
    @Override
    public CheckoutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_cart_item,parent,false);

        return new CheckoutItemAdapter.CheckoutItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutItemViewHolder holder, int position) {
        String item = shopNames.get(position);
        if(item==null)
            return;
        holder.shopName.setText(item);
        holder.selectAll.setVisibility(View.INVISIBLE);
        holder.iconShop.setVisibility(View.VISIBLE);

        ///// RECYCLER VIEW
        CheckoutProductAdapter checkoutProductAdapter = new CheckoutProductAdapter((List<ShoppingCartItem>)groupedCartItems.get(item));
        holder.rcv_cart_items.setAdapter(checkoutProductAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        holder.rcv_cart_items.addItemDecoration(itemDecoration);
        holder.rcv_cart_items.setLayoutManager(linearLayoutManager);
    }



    @Override
    public int getItemCount() {
        if(groupedCartItems!=null)
            return groupedCartItems.size();
        else return 0;
    }

    public static class CheckoutItemViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        androidx.recyclerview.widget.RecyclerView rcv_cart_items;
        CheckBox selectAll;
        ImageView iconShop;
        public CheckoutItemViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopName);
            rcv_cart_items = itemView.findViewById(R.id.rcv_cart_items);
            selectAll = itemView.findViewById(R.id.selectAll);
            iconShop=itemView.findViewById(R.id.iconShop);

        }
    }
}
