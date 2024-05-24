package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {

    private List<ShoppingCartItem> mListShoppingCartItem;

    public void setParentAdapter(CartItemAdapter parentAdapter) {
        this.parentAdapter = parentAdapter;
    }

    private CartItemAdapter parentAdapter;



    public CartProductAdapter(List<ShoppingCartItem> mList) {
        this.mListShoppingCartItem = mList;
    }
    public List<ShoppingCartItem> getData(){
        return mListShoppingCartItem;
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);

        return new CartProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        ShoppingCartItem item = mListShoppingCartItem.get(position);
        if(item==null)
            return;
        Map<String,Object> product = (Map<String,Object>)item.getProduct_item().get("product");
        long qty_in_stock=(long)item.getProduct_item().get("qty_in_stock");
        String name = (String) product.get("name");
        double price =(double) product.get("price");
        int qty = item.getQty();
        String product_image =(String) product.get("product_image");
        if(qty_in_stock<1||qty_in_stock<qty)
        {
            holder.nota.setVisibility(View.VISIBLE);
            holder.avai.setVisibility(View.INVISIBLE);
            mListShoppingCartItem.get(position).setAvailable(false);
        }
        else {
            holder.nota.setVisibility(View.INVISIBLE);
            holder.avai.setVisibility(View.VISIBLE);
            mListShoppingCartItem.get(position).setAvailable(true);
        }

        holder.product_name.setText(name);
        String priceStr =String.valueOf(price)+"vnd";
        holder.product_price.setText(priceStr);
        holder.qty.setText(String.valueOf(qty));
        Picasso.get().load(product_image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.product_image);


        holder.sub1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if(item.getQty()==1)
                    parentAdapter.confirmDelete(item.getId(),false);
                else {
                    item.setQty(item.getQty() - 1);
                    parentAdapter.notifyDataSetChanged();
                    ShoppingCartItemRepository.updateQty(item.getId(), item.getQty());
                }
            }
        });
        holder.add1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                item.setQty(item.getQty()+1);
                parentAdapter.notifyDataSetChanged();
                ShoppingCartItemRepository.updateQty(item.getId(),item.getQty());
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentAdapter.confirmDelete("id",true);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListShoppingCartItem!=null)
            return mListShoppingCartItem.size();
        else return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<ShoppingCartItem> mList) {
        Log.println(Log.INFO,"inside updateData", String.valueOf(mList.size()));
        this.mListShoppingCartItem = mList;
        notifyDataSetChanged();
    }
    ImageButton btDelete;

    public void setBtDelete(ImageButton btDelete) {
        this.btDelete=btDelete;
    }
    List<ShoppingCartItem> selectList;
    @SuppressLint("NotifyDataSetChanged")
    public void setSelectListForCalculate(List<ShoppingCartItem> selectList) {
        this.selectList=selectList;
        TextView total = layoutCheckout.findViewById(R.id.total);
        TextView subtotal = bottomSheetCheckOut.findViewById(R.id.subTotal);
        TextView shipping = bottomSheetCheckOut.findViewById(R.id.shipping);
        long sum = 0;
        Set<String> setShop = new HashSet<>();

        boolean check = false;
        for(int i=0;i<selectList.size();i++)
        {
            ShoppingCartItem item=selectList.get(i);
            if(item.isAvailable()) {
                Map<String, Object> product = (Map<String, Object>) item.getProduct_item().get("product");
                Map<String, Object> shop = (Map<String, Object>) product.get("shop");
                String shop_id = (String) shop.get("id");
                setShop.add(shop_id);
                long price = (long) product.get("price");
                int qty = item.getQty();
                sum += qty * price;
            }
            else
                check=true;
        }
        if(check)
            Toast.makeText(parentAdapter.getContext(), "Total price does not include unavailable products", Toast.LENGTH_SHORT).show();
        subtotal.setText(String.valueOf(sum));
        shipping.setText(String.valueOf(30L *setShop.size()));
        total.setText(String.valueOf(sum+ 30L *setShop.size()));
        total = bottomSheetCheckOut.findViewById(R.id.total);
        total.setText(String.valueOf(sum+ 30L *setShop.size()));
    }
    androidx.cardview.widget.CardView layoutCheckout;
    public void setLayoutCheckout(androidx.cardview.widget.CardView layoutCheckout) {
        this.layoutCheckout=layoutCheckout;
    }
    BottomSheetDialog bottomSheetCheckOut;
    public void setSheetCheckOut(BottomSheetDialog bottomSheetCheckOut) {
        this.bottomSheetCheckOut=bottomSheetCheckOut;
    }

    public static class CartProductViewHolder extends RecyclerView.ViewHolder{
        private final TextView product_name;
        private final TextView product_price;
        private final TextView qty;
        private final ImageView product_image;
        private final TextView sub1;

        private final TextView add1;

        private final CheckBox select;
        private final TextView nota;
        private final TextView avai;


        public CartProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_image = itemView.findViewById(R.id.product_image);
            qty = itemView.findViewById(R.id.qty);
            sub1 = itemView.findViewById(R.id.sub1);
            add1 = itemView.findViewById(R.id.add1);
            select = itemView.findViewById(R.id.select);
            nota = itemView.findViewById(R.id.nota);
            avai = itemView.findViewById(R.id.avai);
        }
    }
}
