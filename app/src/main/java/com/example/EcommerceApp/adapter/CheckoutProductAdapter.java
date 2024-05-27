package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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
        loadAttribute(item.getProduct_item(),holder);
        String name = (String) product.get("name");
        Object priceObject = product.get("price");
        double price;

        if (priceObject instanceof Long) {
            price = ((Long) priceObject).doubleValue();
        } else if (priceObject instanceof Double) {
            price = (Double) priceObject;
        }
        else
            price=0;
        int qty = item.getQty();
        String product_image =(String) product.get("product_image");
        holder.product_name.setText(name);
        String priceStr ="$"+String.valueOf(price);
        holder.product_price.setText(priceStr);
        String qtyText="qty:"+String.valueOf(qty);
        holder.qty.setText(qtyText);
        Picasso.get().load(product_image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.product_image);
    }
    @SuppressLint("SetTextI18n")
    private void loadAttribute(Map<String, Object> productItem, CheckoutProductAdapter.CheckoutProductViewHolder holder) {
        String size = (String) productItem.get("size");
        String colorCode = (String) productItem.get("color");
        if(size==null || colorCode==null)
            return;

        boolean hasSize=true;
        boolean hasColor=true;
        if(size.isEmpty())
            hasSize=false;
        if(colorCode.isEmpty())
            hasColor=false;

        if(hasSize&&hasColor)
        {
            holder.twoAttribute.setVisibility(View.VISIBLE);
            holder.oneAttribute.setVisibility(View.INVISIBLE);
            holder.size.setText(size);
            int color = Color.parseColor(colorCode);
            Drawable colorDrawable = new ColorDrawable(color);
            holder.color.setImageDrawable(colorDrawable);
        }
        else if(!hasSize&&!hasColor)
        {
            holder.twoAttribute.setVisibility(View.INVISIBLE);
            holder.oneAttribute.setVisibility(View.INVISIBLE);
        }
        else if(hasColor){
            holder.twoAttribute.setVisibility(View.INVISIBLE);
            holder.oneAttribute.setVisibility(View.VISIBLE);
            holder.valueSize.setVisibility(View.INVISIBLE);
            holder.valueColor.setVisibility(View.VISIBLE);
            int color = Color.parseColor(colorCode);
            Drawable colorDrawable = new ColorDrawable(color);
            holder.valueColor.setImageDrawable(colorDrawable);
            holder.key.setText("Color: ");
        }
        else {
            holder.twoAttribute.setVisibility(View.INVISIBLE);
            holder.oneAttribute.setVisibility(View.VISIBLE);
            holder.valueSize.setVisibility(View.VISIBLE);
            holder.valueColor.setVisibility(View.INVISIBLE);
            holder.valueSize.setText(size);
            holder.key.setText("Size: ");

        }
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
        GridLayout twoAttribute,oneAttribute;
        TextView size, valueSize;
        TextView key;
        de.hdodenhof.circleimageview.CircleImageView color, valueColor;

        public CheckoutProductViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_image = itemView.findViewById(R.id.product_image);
            qty = itemView.findViewById(R.id.qty);
            twoAttribute =itemView.findViewById(R.id.twoAttribute);
            oneAttribute =itemView.findViewById(R.id.oneAttribute);
            size = itemView.findViewById(R.id.size);
            valueSize =itemView.findViewById(R.id.valueSize);
            key = itemView.findViewById(R.id.key);
            color =itemView.findViewById(R.id.color);
            valueColor = itemView.findViewById(R.id.valueColor);
        }
    }

}