package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.EcommerceApp.adapter.ShopOrderAdapter;
import com.example.EcommerceApp.domain.user.OrderRepository;
import com.example.EcommerceApp.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrderManagement extends AppCompatActivity {
    ImageView btBack;
    androidx.recyclerview.widget.RecyclerView rcvOrder;
    TextView noOrders;
    List<Order> orderList;
    ShopOrderAdapter shopOrderAdapter;
    String shopName;
    ProgressBar progressBar;
    Spinner spinner;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        shopName = intent.getStringExtra("shopName");
        rcvOrder = findViewById(R.id.rcvOrder);
        noOrders = findViewById(R.id.noOrders);
        progressBar=findViewById(R.id.progressbar25);
        spinner= findViewById(R.id.spinner);
        btBack=findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orderList= new ArrayList<>();
        shopOrderAdapter = new ShopOrderAdapter(orderList);
        shopOrderAdapter.setContext(OrderManagement.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderManagement.this);
        rcvOrder.setLayoutManager(linearLayoutManager);
        rcvOrder.setAdapter(shopOrderAdapter);

        setOrderView();
    }

    private void setSpinner() {

        List<String> list = new ArrayList<>();
        list.add("ALL");
        list.add("WAITING");
        list.add("CONFIRMED");
        list.add("DELIVERY");
        list.add("COMPLETE");
        list.add("CANCEL");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);

        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedStatus = list.get(position);
                switch (selectedStatus) {
                    case "ALL":
                        shopOrderAdapter.getFilter().filter("ALL");
                        break;
                    case "WAITING":
                        shopOrderAdapter.getFilter().filter("ON PROGRESS");
                        break;
                    case "CONFIRMED":
                        shopOrderAdapter.getFilter().filter("confirm");
                        break;
                    case "DELIVERY":
                        shopOrderAdapter.getFilter().filter("delivery");
                        break;
                    case "COMPLETE":
                        shopOrderAdapter.getFilter().filter("complete");
                        break;
                    case "CANCEL":
                        shopOrderAdapter.getFilter().filter("cancel");
                        break;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                shopOrderAdapter.getFilter().filter("ALL");
            }
        });
    }

    private void setOrderView() {
        if (shopName == null) return;

        OrderRepository orderRepository = new OrderRepository();
        orderRepository.getOrdersByShop(shopName).addOnCompleteListener(new OnCompleteListener<List<Order>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<List<Order>> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    List<Order> orders = task.getResult();
                    if (orders != null && !orders.isEmpty()) {
                        Log.i("size",String.valueOf(orders.size()));
                        orders.sort(new Comparator<Order>() {
                            @Override
                            public int compare(Order o1, Order o2) {
                                return o2.getOrderAt().compareTo(o1.getOrderAt());
                            }
                        });
                        orderList.clear(); // Clear current list
                        orderList.addAll(orders); // Add new data
                        shopOrderAdapter.setOldOrderList(orders);
                        shopOrderAdapter.notifyDataSetChanged(); // Notify adapter of data change
                        noOrders.setVisibility(View.GONE);
                        rcvOrder.setVisibility(View.VISIBLE);
                        setSpinner();
                    } else {
                        noOrders.setVisibility(View.VISIBLE);
                        rcvOrder.setVisibility(View.GONE);
                    }
                } else {
                    // Handle failure
                    noOrders.setVisibility(View.VISIBLE);
                    rcvOrder.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setOrderView();
    }
}