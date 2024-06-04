package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.EcommerceApp.adapter.OrderItemAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ShopDetailOrder extends AppCompatActivity {
    ImageView btBack;
    Button update;
    androidx.recyclerview.widget.RecyclerView rcv_product_item;
    TextView  orderCode, rcvName, phone, address, subTotal, reason, cancelAt, orderAt;
    Order order;
    Spinner spinner;
    LinearLayout updateStatus,layout_cancel;
    List<OrderItem> orderItemList;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_detail_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Gson gson = new Gson();
            String orderJson = bundle.getString("orderJson");
            order = gson.fromJson(orderJson, Order.class);
        }
        setWidgets();
        setAddress();
        setProductView();
        orderAt.setText(generateTimestamp(order.getOrderAt(),"Order at"));
        orderCode.setText("Order code: "+order.getId());
        if(Objects.equals(order.getStatus(), "cancel"))
        {
            layout_cancel.setVisibility(View.VISIBLE);
            updateStatus.setVisibility(View.GONE);
            cancelAt.setText(generateTimestamp(order.getCancelAt(),"Cancel at"));
            reason.setText("Reason: "+order.getReasonCancel());
        }
        else if(!Objects.equals(order.getStatus(), "complete")){
            layout_cancel.setVisibility(View.GONE);
            updateStatus.setVisibility(View.VISIBLE);
            setUpdateView();
        }
        else {
            layout_cancel.setVisibility(View.GONE);
            updateStatus.setVisibility(View.GONE);
        }
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void setUpdateView() {
        setSpinner(order.getStatus());
        update.setText("UPDATED");
        update.setEnabled(false);
    }
    private void setSpinner(String status) {

        List<String> list = new ArrayList<>();
        switch (order.getStatus()) {
            case "ON PROGRESS":
                list.add("WAITING"); // status is ON PROGRESS

                list.add("CONFIRM"); // status is confirm

                list.add("DELIVERY"); // status is delivery

                break;
            case "confirm":
                list.add("CONFIRM");
                list.add("DELIVERY");
                break;
            case "delivery":
                list.add("DELIVERY");
                break;
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);

        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = list.get(position);
                if (position != 0) {
                    update.setText("UPDATE");
                    update.setEnabled(true);
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OrderRepository orderRepository = new OrderRepository();
                            if(Objects.equals(selectedStatus, "DELIVERY")&& Objects.equals(order.getStatus(), "confirm")){
                                orderRepository.updateOrderStatus(order.getId(),"delivery").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ShopDetailOrder.this,"Update order status successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }
                            else if(Objects.equals(selectedStatus, "DELIVERY")&& Objects.equals(order.getStatus(), "ON PROGRESS")){
                                orderRepository.updateOrderStatus(order.getId(),"confirm").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        orderRepository.updateOrderStatus(order.getId(),"delivery").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(ShopDetailOrder.this,"Update order status successfully", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                            else if(Objects.equals(selectedStatus, "CONFIRM")){
                                orderRepository.updateOrderStatus(order.getId(),"confirm").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ShopDetailOrder.this,"Update order status successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    update.setText("UPDATE");
                    update.setEnabled(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    private String generateTimestamp(Timestamp timestamp, String status) {
        Date date = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return status+" "+dateFormat.format(date);
    }
    private void setProductView() {
        OrderItemRepository orderItemRepository = new OrderItemRepository();
        orderItemRepository.getOrderItemsByOrderId(order.getId()).addOnCompleteListener(new OnCompleteListener<List<OrderItem>>() {
            @Override
            public void onComplete(@NonNull Task<List<OrderItem>> task) {
                if (task.isSuccessful()) {
                    orderItemList=task.getResult();
                    setBillView();
                    OrderItemAdapter orderItemAdapter = new OrderItemAdapter(orderItemList);
                    orderItemAdapter.setContext(ShopDetailOrder.this);
                    orderItemAdapter.setComplete(false);
                    rcv_product_item.setAdapter(orderItemAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShopDetailOrder.this);
                    rcv_product_item.setLayoutManager(linearLayoutManager);
                } else {
                    System.err.println("Error getting order items: " + task.getException());
                }

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setBillView() {
        if(orderItemList==null)
            return;
        double prices = 0;
        for(OrderItem orderItem:orderItemList){
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

            assert product != null;
            Object priceObject = product.get("price");
            double price;

            if (priceObject instanceof Long) {
                price = ((Long) priceObject).doubleValue();
            } else if (priceObject instanceof Double) {
                price = (Double) priceObject;
            }
            else
                price=0;

            prices +=price*qty;
        }
        subTotal.setText("$"+String.valueOf(prices));
    }
    private void setAddress() {
        Map<String,Object> addressMap=new HashMap<>(order.getAddress());
        String text_address = addressMap.get("address_line")+", "+addressMap.get("ward")+", "+addressMap.get("district")+", "+addressMap.get("province");
        address.setText(text_address);
        phone.setText((String)addressMap.get("phone"));
        rcvName.setText((String)addressMap.get("receiver_name"));
    }
    private void setWidgets() {
        btBack=findViewById(R.id.btBack);
        update = findViewById(R.id.update);
        rcv_product_item = findViewById(R.id.rcv_product_item);
        orderCode = findViewById(R.id.orderCode);
        rcvName = findViewById(R.id.rcvName);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        subTotal = findViewById(R.id.subTotal);
        spinner=findViewById(R.id.spinner);
        updateStatus =findViewById(R.id.updateStatus);
        layout_cancel =findViewById(R.id.layout_cancel);
        orderAt = findViewById(R.id.orderAt);
        cancelAt = findViewById(R.id.cancelAt);
        reason = findViewById(R.id.reason);
    }
}