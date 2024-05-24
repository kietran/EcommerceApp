package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartItemAdapter extends  RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>{
    private Map<String, List<ShoppingCartItem>> groupedCartItems;
    private List<String> shopNames;
    private Context context;
    private int countSelect;
    private boolean falseBySubSelect;
    private List<ShoppingCartItem> selectList;
    private androidx.cardview.widget.CardView layout_checkout;
    public CartItemAdapter(Map<String, List<ShoppingCartItem>> groupedCartItems, Context context) {
        this.groupedCartItems = groupedCartItems;
        this.context = context;
        countSelect=0;
        falseBySubSelect=false;
        selectList=new ArrayList<>();
        updateData(groupedCartItems);
    }
    public Context getContext()
    {
        return  context;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(Map<String, List<ShoppingCartItem>> list){
        groupedCartItems=list;
        shopNames = new ArrayList<>();
        shopNames.addAll(groupedCartItems.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_cart_item,parent,false);

        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        String item = shopNames.get(position);
        if(item==null)
            return;
        holder.shopName.setText(item);

        ///// RECYCLER VIEW
        CartProductAdapter cartProductAdapter = new CartProductAdapter((List<ShoppingCartItem>)groupedCartItems.get(item));
        cartProductAdapter.setParentAdapter(this);
        cartProductAdapter.setLayoutCheckout(layout_checkout);
        cartProductAdapter.setSheetCheckOut(bottomSheetCheckOut);
        holder.rcv_cart_items.setAdapter(cartProductAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        holder.rcv_cart_items.setLayoutManager(linearLayoutManager);
        cartProductAdapter.setBtDelete(btDelete);
        cartProductAdapter.setParentAdapter(this);


        //// SUB SELECT
        holder.rcv_cart_items.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                CheckBox select = view.findViewById(R.id.select);
                select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                        int n= cartProductAdapter.getItemCount();

                        int countCheck=0;
                        for(int i=0;i<n;i++) {
                            View childView = holder.rcv_cart_items.getChildAt(i);
                            CheckBox select = childView.findViewById(R.id.select);
                            /// check for check select all by sub checkbox
                            if(select.isChecked()) {
                                countCheck++;
                            }
                            ShoppingCartItem cartItem = cartProductAdapter.getData().get(i);

                            /// update select list
                            if(select.equals(buttonView)) {
                                if (b)
                                    selectList.add(cartItem);
                                 else
                                    selectList.remove(cartItem);
                            }
                        }


                        /// update select count for check select all
                        if (b) {
                            countSelect++;
                        } else {
                            countSelect--;
                            falseBySubSelect=true;
                            holder.selectAll.setChecked(false);
                        }


                        if(countCheck==n)
                            holder.selectAll.setChecked(true);

                        updateUI();
                        cartProductAdapter.setSelectListForCalculate(selectList);
                        Log.println(Log.INFO,"select list", String.valueOf(selectList.size()));
                    }
                });
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {

            }
        });
        /// SELECT ALL
        holder.selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int n= cartProductAdapter.getItemCount();

                if(b){
                    for(int i=0;i<n;i++) {
                        View childView = holder.rcv_cart_items.getChildAt(i);
                        CheckBox select = childView.findViewById(R.id.select);
                        if(!select.isChecked()) {
                            select.setChecked(true);
                        }
                    }
                }
                else {
                    if(falseBySubSelect)
                    {
                        falseBySubSelect=false;
                        return;
                    }
                    else {
                        for (int i = 0; i < n; i++) {
                            View childView = holder.rcv_cart_items.getChildAt(i);
                            CheckBox select = childView.findViewById(R.id.select);
                            if (select.isChecked()) {
                                select.setChecked(false);
                                falseBySubSelect=false;
                            }
                        }
                    }
                }
                updateUI();
            }
        });
    }
    public void updateUI(){
        if(countSelect!=0){
            btDelete.setVisibility(View.VISIBLE);
            layout_checkout.setVisibility(View.VISIBLE);
        }
        else{
            btDelete.setVisibility(View.INVISIBLE);
            layout_checkout.setVisibility(View.INVISIBLE);
        }
    }
    public void confirmDelete(String id, boolean mul){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Confirm");
        if(mul)
            alertDialog.setMessage("Are you sure to delete these item?");
        else {
            alertDialog.setMessage("Are you sure to delete this item?");
        }

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //delete
                if(mul) {
                    ShoppingCartItemRepository.delete(selectList);
                    for(int j=0;j<selectList.size();j++) {
                        String shopIdToDelete = getShopOfItem(selectList.get(j).getId());
                        if (shopIdToDelete != null && groupedCartItems.containsKey(shopIdToDelete)) {
                            List<ShoppingCartItem> shopItems = groupedCartItems.get(shopIdToDelete);
                            for (ShoppingCartItem item : shopItems) {
                                if (item.getId().equals(id)) {
                                    shopItems.remove(item);
                                    break;
                                }
                            }
                            if (shopItems.isEmpty()) {
                                groupedCartItems.remove(shopIdToDelete);
                            }
                        }
                    }
                }
                else {
                    ShoppingCartItemRepository.delete(id);

                    String shopIdToDelete = getShopOfItem(id);
                    if (shopIdToDelete != null && groupedCartItems.containsKey(shopIdToDelete)) {
                        List<ShoppingCartItem> shopItems = groupedCartItems.get(shopIdToDelete);
                        for (ShoppingCartItem item : shopItems) {
                            if (item.getId().equals(id)) {
                                shopItems.remove(item);
                                break;
                            }
                        }
                        if (shopItems.isEmpty()) {
                            groupedCartItems.remove(shopIdToDelete);
                        }
                    }
                }
                updateData(groupedCartItems);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
    String getShopOfItem(String itemId) {
        for (Map.Entry<String, List<ShoppingCartItem>> entry : groupedCartItems.entrySet()) {
            List<ShoppingCartItem> items = entry.getValue();
            for (ShoppingCartItem item : items) {
                if (item.getId().equals(itemId)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
    @Override
    public int getItemCount() {
        if(groupedCartItems!=null)
            return groupedCartItems.size();
        else return 0;
    }
    ImageButton btDelete;
    public void setBtnDelete(ImageButton btDelete) {
        this.btDelete = btDelete;
    }



    public void setLayoutCheckout(androidx.cardview.widget.CardView layoutCheckout) {
        this.layout_checkout = layoutCheckout;
    }
    BottomSheetDialog bottomSheetCheckOut;
    public void setSheetCheckout(BottomSheetDialog bottomSheetCheckOut) {
        this.bottomSheetCheckOut =bottomSheetCheckOut;
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView shopName;
        androidx.recyclerview.widget.RecyclerView rcv_cart_items;
        CheckBox selectAll;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopName);
            rcv_cart_items = itemView.findViewById(R.id.rcv_cart_items);
            selectAll = itemView.findViewById(R.id.selectAll);
        }
    }
}