package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.domain.user.ProductItemRepository;
import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.example.EcommerceApp.model.OrderItem;
import com.example.EcommerceApp.model.ShoppingCart;
import com.example.EcommerceApp.utils.CartNumberUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{
    List<OrderItem> orderItemList;

    public OrderItemAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    Context context ;

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    Boolean isComplete;


    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product,parent,false);
        return new OrderItemAdapter.OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem= orderItemList.get(position);
        if (orderItem==null)
            return;
        /// RATE
        if(isComplete) {
            holder.complete.setVisibility(View.VISIBLE);
            holder.reBuy.setVisibility(View.VISIBLE);
            holder.reBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> cartMap = orderItem.getCartItem();
                    String cartId= (String) cartMap.get("id");
                    Map<String,Object> productItem= (Map<String, Object>) cartMap.get("product_item");
                    String productItemId = (String) productItem.get("id");
                    ProductItemRepository productItemRepository = new ProductItemRepository(context);
                    productItemRepository.getQtyInStock(productItemId).addOnCompleteListener(new OnCompleteListener<Long>() {
                        @Override
                        public void onComplete(@NonNull Task<Long> task) {
                            if(task.isSuccessful()){
                                Long newQty = task.getResult();
                                productItem.put("qty_in_stock",newQty);
                                ShoppingCart shoppingCart = new ShoppingCart(FirebaseAuth.getInstance().getUid(),cartId);
                                addToCartAgain(productItem,shoppingCart,1);
                            }
                            else
                                Log.i("add to cart again", "not success");
                        }
                    });

                }
            });
            /// remind update rate is visible for se104
            holder.rate.setVisibility(View.INVISIBLE);
        }
        else{
            holder.complete.setVisibility(View.GONE);
        }

        Map<String,Object> cartItem=orderItem.getCartItem();
        Object qtyObject = cartItem.get("qty");
        double qty;
        if(qtyObject instanceof  Long){
            qty = ((Long) qtyObject).doubleValue();
        } else if (qtyObject instanceof Double) {
            qty = (Double) qtyObject;
        }
        else
            qty=0;
        Map<String,Object> product_item = (Map<String, Object>) cartItem.get("product_item");
        Map<String,Object> product = (Map<String, Object>) product_item.get("product");
        String product_image =(String) product.get("product_image");
        loadAttribute(product_item,holder);
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

        holder.qty.setText(String.valueOf((int)qty));
        holder.product_name.setText(name);
        String priceStr ="$"+String.valueOf(price);
        holder.product_price.setText(priceStr);
        String qtyText="qty:"+String.valueOf(qty);
        holder.qty.setText(qtyText);
        Picasso.get().load(product_image)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.product_image);

    }

    private void addToCartAgain(Map<String,Object> productItem, ShoppingCart shoppingCart, int qty) {

        Object qtyInStockObject = productItem.get("qty_in_stock");
        double qty_in_stock;

        if (qtyInStockObject instanceof Long) {
            qty_in_stock = ((Long) qtyInStockObject).doubleValue();
        } else if (qtyInStockObject instanceof Double) {
            qty_in_stock = (Double) qtyInStockObject;
        }
        else
            qty_in_stock=0;

        if(qty_in_stock>0)
        {
            confirmAddToCart(productItem,shoppingCart,qty);
        }
        else {
            notifyUnavailable();
        }
    }

    private void confirmAddToCart(Map<String,Object> productItem, ShoppingCart shoppingCart, int qty) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm add to cart again");
        builder.setMessage("The product will be added to the cart with a quantity of 1.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createCart(productItem,shoppingCart,qty);
                Toast.makeText(context, "Product added to cart, check your cart to continue the checkout process.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void createCart(Map<String,Object> productItem, @NonNull ShoppingCart shoppingCart, int qty) {
        Map<String,Object> cartMap = new HashMap<>();
        cartMap.put("id",shoppingCart.getId());
        cartMap.put("user_id",shoppingCart.getUserID());
        ShoppingCartItemRepository shoppingCartItemRepository = new ShoppingCartItemRepository();
        Log.i("product item id", (String) productItem.get("id"));
        shoppingCartItemRepository.getCartItem((String) productItem.get("id"),shoppingCart.getId()).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    String cartItemId = task.getResult();
                    if (cartItemId == null)
                        shoppingCartItemRepository.addToCartAgain(productItem,cartMap,qty);
                    else
                        ShoppingCartItemRepository.updateQty2(cartItemId, 1);
                    CartNumberUtil.getBadge_mycart();
                }
                else
                    Log.i("createCart","not success");
            }
        });
    }

    private void notifyUnavailable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Product Unavailable");
        builder.setMessage("This product is currently unavailable.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void loadAttribute(Map<String, Object> productItem, OrderItemAdapter.OrderItemViewHolder holder) {
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
        if(orderItemList!=null)
            return  orderItemList.size();
        return 0;
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView product_name;
        private final TextView product_price;
        private final TextView qty;
        private final ImageView product_image;
        GridLayout twoAttribute,oneAttribute;
        TextView size, valueSize;
        TextView key;
        de.hdodenhof.circleimageview.CircleImageView color, valueColor;
        Button rate, reBuy;
        LinearLayout complete;


        public OrderItemViewHolder(@NonNull View itemView) {
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
            rate = itemView.findViewById(R.id.rate);
            reBuy = itemView.findViewById(R.id.reBuy);
            complete = itemView.findViewById(R.id.complete);
        }
    }
}
