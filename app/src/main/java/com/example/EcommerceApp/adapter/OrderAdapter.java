package com.example.EcommerceApp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.DetailOrder;
import com.example.EcommerceApp.R;
import com.example.EcommerceApp.domain.user.OrderItemRepository;
import com.example.EcommerceApp.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {

        this.orderList = orderList;
        this.orderList.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o2.getOrderAt().compareTo(o1.getOrderAt());
            }
        });
    }

    public void setContext(Context context) {
        this.context = context;
    }

    Context context;


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
        return new OrderAdapter.OrderViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order =orderList.get(position);
        if(order==null)
            return;
        holder.shopName.setText(order.getShop());
        holder.orderAt.setText(generateTextOrderAt(order.getOrderAt()));

        if(order.getStatus().equals("complete")){
            setViewStatus(holder, holder.complete);
        }
        else
            setViewStatus(holder, holder.waiting);

        OrderItemRepository orderItemRepository=new OrderItemRepository();
        orderItemRepository.getOrderItemCountByOrderId(order.getId()).addOnCompleteListener(new OnCompleteListener<Integer>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<Integer> task) {
                if (task.isSuccessful()) {
                    int orderItemCount = task.getResult();
                    if(orderItemCount==1)
                        holder.numProducts.setText("1 item");
                    else
                        holder.numProducts.setText(String.valueOf(orderItemCount)+" items");
                } else {
                    System.err.println("Error getting order item count: " + task.getException());
                }
            }
        });

        holder.bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isComplete = order.getStatus().equals("complete");
                Intent intent = new Intent(context, DetailOrder.class);
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String orderJson = gson.toJson(order);
                intent.putExtra("orderJson", orderJson);
                bundle.putBoolean("isComplete", isComplete);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
    void setViewStatus(OrderViewHolder holder, TextView status){
        holder.confirm.setVisibility(View.INVISIBLE);
        holder.complete.setVisibility(View.INVISIBLE);
        holder.waiting.setVisibility(View.INVISIBLE);
        holder.delivery.setVisibility(View.INVISIBLE);

        status.setVisibility(View.VISIBLE);
    }

    private String generateTextOrderAt(Timestamp orderAt) {
        Date date = orderAt.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return "Order at "+dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        if(!orderList.isEmpty())
            return orderList.size();
        return 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView shopName, orderAt, numProducts, complete, waiting, confirm, delivery;
        androidx.cardview.widget.CardView bound;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName=itemView.findViewById(R.id.shopName);
            orderAt=itemView.findViewById(R.id.orderAt);
            numProducts=itemView.findViewById(R.id.numProducts);
            complete=itemView.findViewById(R.id.complete);
            waiting=itemView.findViewById(R.id.waiting);
            confirm=itemView.findViewById(R.id.confirm);
            delivery=itemView.findViewById(R.id.delivery);
            bound=itemView.findViewById(R.id.bound);
        }
    }
}
