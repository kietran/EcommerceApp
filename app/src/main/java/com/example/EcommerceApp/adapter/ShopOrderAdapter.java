package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.ShopDetailOrder;
import com.example.EcommerceApp.domain.user.OrderItemRepository;
import com.example.EcommerceApp.domain.user.OrderRepository;
import com.example.EcommerceApp.model.Order;
import com.example.EcommerceApp.model.OrderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ShopOrderAdapter extends RecyclerView.Adapter<ShopOrderAdapter.ShopOrderViewHolder>implements Filterable {
    public ShopOrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
        this.oldOrderList= orderList;
    }

    List<Order> orderList;

    public void setOldOrderList(List<Order> oldOrderList) {
        this.oldOrderList.clear();
        this.oldOrderList.addAll(oldOrderList);
    }

    List<Order> oldOrderList;
    public void setContext(Context context) {
        this.context = context;
    }

    Context context;

    @NonNull
    @Override
    public ShopOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_order,parent,false);
        return new ShopOrderAdapter.ShopOrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShopOrderViewHolder holder, int position) {
        Order order =orderList.get(position);
        Log.i("shop order", String.valueOf(position));
        if(order==null)
            return;

        // STATUS
        if(Objects.equals(order.getStatus(), "ON PROGRESS")){
            holder.status.setText("WAITING");
            holder.confirm.setVisibility(View.VISIBLE);
            holder.confirm.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    handleConfirm(order);
                    holder.confirm.setVisibility(View.GONE);
                    holder.status.setText("CONFIRMED");
                }
            });
        }
        else if(order.getStatus().equals("confirm")){
            holder.status.setText("CONFIRMED");
            holder.confirm.setVisibility(View.GONE);
        }
        else if(order.getStatus().equals("delivery")){
            holder.status.setText("DELIVERY");
            holder.confirm.setVisibility(View.GONE);
        }
        else if(order.getStatus().equals("complete")){
            holder.status.setText("COMPLETE");
            holder.confirm.setVisibility(View.GONE);
        }
        else if(order.getStatus().equals("cancel")){
            holder.status.setText("CANCEL");
            holder.confirm.setVisibility(View.GONE);
        }

        // DATE TIME
        holder.orderAt.setText(generateTextOrderAt(order.getOrderAt()));
        // ADDRESS
        Map<String,Object> addressMap = order.getAddress();
        String text_address = addressMap.get("address_line")+", "+addressMap.get("ward")+", "+addressMap.get("district")+", "+addressMap.get("province");
        holder.address.setText(text_address);
        holder.phone.setText((String)addressMap.get("phone"));
        holder.rcvName.setText((String)addressMap.get("receiver_name"));
        holder.showAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.showAddress.setVisibility(View.INVISIBLE);
                holder.layout_address.setVisibility(View.VISIBLE);
                holder.hideAddress.setVisibility(View.VISIBLE);
                holder.hideAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.hideAddress.setVisibility(View.INVISIBLE);
                        holder.showAddress.setVisibility(View.VISIBLE);
                        holder.layout_address.setVisibility(View.GONE);
                    }
                });
            }
        });
        // TOTAL
        OrderItemRepository orderItemRepository = new OrderItemRepository();
        orderItemRepository.getOrderItemsByOrderId(order.getId()).addOnCompleteListener(new OnCompleteListener<List<OrderItem>>() {
            @Override
            public void onComplete(@NonNull Task<List<OrderItem>> task) {
                List<OrderItem> orderItems = new ArrayList<>();
                if(task.isSuccessful()&&!task.getResult().isEmpty())
                {
                    orderItems=new ArrayList<>(task.getResult());
                    long  total =0;
                    for(OrderItem orderItem:orderItems){
                        Map<String,Object> cartItem=orderItem.getCartItem();
                        Map<String,Object> product_item = (Map<String, Object>) cartItem.get("product_item");
                        Map<String,Object> product = (Map<String, Object>) product_item.get("product");
                        Object priceObject = product.get("price");
                        double price;

                        if (priceObject instanceof Long) {
                            price = ((Long) priceObject).doubleValue();
                        } else if (priceObject instanceof Double) {
                            price = (Double) priceObject;
                        }
                        else
                            price=0;
                        total+=(long)price;
                    }
                    holder.total.setText("$"+String.valueOf(total));
                }
            }
        });
        // CLICK
        holder.bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopDetailOrder.class);
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String orderJson = gson.toJson(order);
                intent.putExtra("orderJson", orderJson);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    private void handleConfirm(Order order) {
        OrderRepository orderRepository = new OrderRepository();
        orderRepository.updateOrderStatus(order.getId(),"confirm").addOnCompleteListener(new OnCompleteListener<Void>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    order.setStatus("confirm");
                    order.setConfirmAt(Timestamp.now());
                    notifyDataSetChanged();
                } else {
                    // Handle update failure
                }
            }
        });;
    }

    private String generateTextOrderAt(Timestamp orderAt) {
        Date date = orderAt.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(date);
    }
    @Override
    public int getItemCount() {

        if(orderList!=null)
            return orderList.size();
        return 0;
    }

    public static class ShopOrderViewHolder extends RecyclerView.ViewHolder{
        TextView status, orderAt, phone, rcvName, address, total;
        ImageView showAddress, hideAddress;
        Button confirm;
        LinearLayout layout_address;
        LinearLayout bound;
        public ShopOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            orderAt = itemView.findViewById(R.id.orderAt);
            phone = itemView.findViewById(R.id.phone);
            rcvName = itemView.findViewById(R.id.rcvName);
            address = itemView.findViewById(R.id.address);
            total = itemView.findViewById(R.id.total);
            showAddress = itemView.findViewById(R.id.showAddress);
            confirm = itemView.findViewById(R.id.confirm);
            layout_address = itemView.findViewById(R.id.layout_address);
            hideAddress = itemView.findViewById(R.id.hideAddress);
            bound = itemView.findViewById(R.id.bound);
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String status = charSequence.toString().trim();
                if (status.equals("ALL"))
                {
                    orderList=oldOrderList;
                }
                else if (!status.isEmpty()) {
                    List<Order> orders = new ArrayList<>();
                    for(Order order:oldOrderList)
                    {
                        if(order.getStatus().equals(status))
                            orders.add(order);
                    }
                    orderList=orders;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=orderList;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orderList=(List<Order>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
