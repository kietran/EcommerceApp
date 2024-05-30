package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.DetailTwoAttributeActivity;
import com.example.EcommerceApp.R;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.example.EcommerceApp.utils.CartNumberUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        loadAttribute(item.getProduct_item(),holder);
        long qty_in_stock=(long)item.getProduct_item().get("qty_in_stock");
        String name = (String) product.get("name");
        String product_id= (String)product.get("id");
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


        if(qty_in_stock<1||qty_in_stock<qty)
        {
            holder.nota.setVisibility(View.VISIBLE);
            holder.avai.setVisibility(View.INVISIBLE);
            item.setAvailable(false);
            if(holder.select.isChecked())
                holder.select.setChecked(false);
            CartItemAdapter.selectList.remove(item);
            calculateFee();
        }
        else {
            holder.nota.setVisibility(View.INVISIBLE);
            holder.avai.setVisibility(View.VISIBLE);
            item.setAvailable(true);
            calculateFee();
        }

        holder.select.setChecked(CartItemAdapter.selectList.contains((item)));

        holder.product_name.setText(name);
        holder.product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToDetail(product_id,item.getProduct_item());
            }
        });
        String priceStr ="$"+String.valueOf(price);
        holder.product_price.setText(priceStr);
        holder.qty.setText(String.valueOf(qty));
        Picasso.get().load(product_image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.product_image);

        long qtyPrice= (long) (qty*price);
        String strQtyPrice ="$"+String.valueOf(qtyPrice);
        holder.cal_price.setText(strQtyPrice);

        holder.sub1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                if(item.getQty()==1)
                    parentAdapter.confirmDelete(item.getId(),false);
                else {
                    item.setQty(item.getQty() - 1);
                    //parentAdapter.notifyDataSetChanged();
                    CartNumberUtil.setQty_in_cart(CartNumberUtil.getQty_in_cart() - 1);
                    CartNumberUtil.getCartNumber();
                    notifyDataSetChanged();
                    ShoppingCartItemRepository shoppingCartItemRepository = new ShoppingCartItemRepository();
                    shoppingCartItemRepository.updateQty(item.getId(), item.getQty());
                }
            }
        });
        holder.add1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                item.setQty(item.getQty()+1);
                //parentAdapter.notifyDataSetChanged();
                CartNumberUtil.setQty_in_cart(CartNumberUtil.getQty_in_cart() + 1);
                CartNumberUtil.getCartNumber();
                notifyDataSetChanged();
                ShoppingCartItemRepository shoppingCartItemRepository = new ShoppingCartItemRepository();
                shoppingCartItemRepository.updateQty(item.getId(),item.getQty());
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentAdapter.confirmDelete("id",true);
            }
        });

    }

    private void navigateToDetail(String product_id, Map<String, Object> productItem) {
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

        ProductRepository productRepository = new ProductRepository(context);
        boolean finalHasColor = hasColor;
        boolean finalHasSize = hasSize;
        productRepository.getProductByProductId(product_id).addOnCompleteListener(new OnCompleteListener<Product>() {
            @Override
            public void onComplete(@NonNull Task<Product> task) {
                Product product = task.getResult();
                Intent intent = new Intent(context, DetailTwoAttributeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("object_product", product);
                bundle.putString("id", product_id);
                bundle.putBoolean("haveColor", finalHasColor);
                bundle.putBoolean("haveSize", finalHasSize);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadAttribute(Map<String, Object> productItem, CartProductViewHolder holder) {
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
    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    public void calculateFee() {
        List<ShoppingCartItem> selectList=new ArrayList<>(CartItemAdapter.selectList);
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
                sum += qty * price;
            }
            else
                check=true;
        }
        if(check) {
            parentAdapter.setAllAvailable(false);
            Toast.makeText(parentAdapter.getContext(), "Total price does not include unavailable products", Toast.LENGTH_SHORT).show();
        }
        else
            parentAdapter.setAllAvailable(true);

        subtotal.setText("$"+String.valueOf(sum));
        shipping.setText("$"+String.valueOf(30L *setShop.size()));
        total.setText("$"+String.valueOf(+sum+ 30L *setShop.size()));
        total = bottomSheetCheckOut.findViewById(R.id.total);
        total.setText("$"+String.valueOf(sum+ 30L *setShop.size()));
        totalFee=sum+ 30L *setShop.size();
    }
    public static long totalFee;
    androidx.cardview.widget.CardView layoutCheckout;
    public void setLayoutCheckout(androidx.cardview.widget.CardView layoutCheckout) {
        this.layoutCheckout=layoutCheckout;
    }
    BottomSheetDialog bottomSheetCheckOut;
    public void setSheetCheckOut(BottomSheetDialog bottomSheetCheckOut) {
        this.bottomSheetCheckOut=bottomSheetCheckOut;
    }
    Context context;
    public void setContext(Context context) {
        this.context=context;
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
        GridLayout twoAttribute,oneAttribute;
        TextView size, valueSize;
        TextView key,cal_price;
        de.hdodenhof.circleimageview.CircleImageView color, valueColor;


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
            twoAttribute =itemView.findViewById(R.id.twoAttribute);
            oneAttribute =itemView.findViewById(R.id.oneAttribute);
            size = itemView.findViewById(R.id.size);
            valueSize =itemView.findViewById(R.id.valueSize);
            key = itemView.findViewById(R.id.key);
            color =itemView.findViewById(R.id.color);
            valueColor = itemView.findViewById(R.id.valueColor);
            cal_price =itemView.findViewById(R.id.cal_price);
        }
    }
}
