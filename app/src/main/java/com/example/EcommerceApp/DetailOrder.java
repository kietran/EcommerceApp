package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.EcommerceApp.domain.user.ProductItemRepository;
import com.example.EcommerceApp.domain.user.ShopRepository;
import com.example.EcommerceApp.model.Order;
import com.example.EcommerceApp.model.OrderItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DetailOrder extends AppCompatActivity {
    ImageView btBack;
    Button markDone,cancel;
    androidx.recyclerview.widget.RecyclerView rcv_product_item;
    TextView shopName, orderCode, statusWaiting, statusConfirm, statusDelivery, statusComplete, rcvName, phone, address, subTotal, total;
    Order order;
    boolean isComplete;
    List<OrderItem> orderItemList;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_order);
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
            isComplete = bundle.getBoolean("isComplete");
        }
        setWidgets();

        if(isComplete) {
            markDone.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }
        else {
            if(Objects.equals(order.getStatus(), "ON PROGRESS"))
            {
                // waiting case
                markDone.setVisibility(View.GONE);
                cancel.setVisibility(View.VISIBLE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleCancel(order.getId());
                    }
                });
            }
            else if(Objects.equals(order.getStatus(), "confirm"))
            {
                markDone.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
            else if(Objects.equals(order.getStatus(), "delivery"))
            {
                cancel.setVisibility(View.GONE);
                markDone.setVisibility(View.VISIBLE);
                markDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        confirmMarkAsDone();
                    }
                });
            }
        }
        setHeader();
        setTracking();
        setProductView();
        setAddress();
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void handleCancel(String id) {
        CancelOrder cancelOrder = CancelOrder.newInstance(id);
        cancelOrder.show(getSupportFragmentManager(),"cancel order");
    }

    private ProgressDialog progressDialog;

    private void confirmMarkAsDone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Mark As Done")
                .setMessage("Are you sure you want to mark this order as done?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog = new ProgressDialog(DetailOrder.this);
                        progressDialog.setMessage("Processing...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        updateOrderStatus();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updateOrderStatus() {
        OrderRepository orderRepository = new OrderRepository();
        orderRepository.updateOrderStatus(order.getId(), "complete").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendOrderCompletedBroadcast();
                } else {
                }
            }
        });
    }
    private void sendOrderCompletedBroadcast() {
        Intent intent = new Intent("order_completed");
        getApplicationContext().sendBroadcast(intent);
        progressDialog.cancel();
        finish();
    }

    void performCancel(String orderId, String reason) {
        OrderItemRepository orderItemRepository = new OrderItemRepository();
        orderItemRepository.getOrderItemsByOrderId(orderId).addOnCompleteListener(new OnCompleteListener<List<OrderItem>>() {
            @Override
            public void onComplete(@NonNull Task<List<OrderItem>> task) {
                if(task.isSuccessful()) {
                    List<OrderItem> orderItems = task.getResult();
                    if(orderItems != null && !orderItems.isEmpty()) {
                        for(OrderItem orderItem : orderItems) {
                            Map<String, Object> cartMap = orderItem.getCartItem();
                            Object qtyObject = cartMap.get("qty");
                            double qty;
                            if (qtyObject instanceof Long) {
                                qty = ((Long) qtyObject).doubleValue();
                            } else if (qtyObject instanceof Double) {
                                qty = (Double) qtyObject;
                            } else {
                                qty = 0;
                            }
                            Map<String, Object> product_item = (Map<String, Object>) cartMap.get("product_item");
                            String productItemId = (String) product_item.get("id");
                            ProductItemRepository productItemRepository = new ProductItemRepository(DetailOrder.this);
                            productItemRepository.updatePlusQtyInStock(productItemId, (int) qty);
                        }
                        OrderRepository orderRepository = new OrderRepository();
                        orderRepository.cancelOrder(orderId, reason).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent("order_canceled");
                                getApplicationContext().sendBroadcast(intent);
                                finish();
                            }
                        });
                    }
                } else {
                    Log.i("get order item", "fail");
                }
            }
        });
    }


    private void setProductView() {
        OrderItemRepository orderItemRepository = new OrderItemRepository();
        orderItemRepository.getOrderItemsByOrderId(order.getId()).addOnCompleteListener(new OnCompleteListener<List<OrderItem>>() {
            @Override
            public void onComplete(@NonNull Task<List<OrderItem>> task) {
                if (task.isSuccessful()) {
                    orderItemList=task.getResult();
                    setBillView();
                    OrderItemAdapter orderItemAdapter = new OrderItemAdapter(DetailOrder.this, orderItemList);
                    orderItemAdapter.setComplete(isComplete);
                    rcv_product_item.setAdapter(orderItemAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailOrder.this);
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


        double totalFee = prices +30;
        total.setText("$"+String.valueOf(totalFee));
        subTotal.setText("$"+String.valueOf(prices));
    }

    private void setAddress() {
        Map<String,Object> addressMap=new HashMap<>(order.getAddress());
        String text_address = addressMap.get("address_line")+", "+addressMap.get("ward")+", "+addressMap.get("district")+", "+addressMap.get("province");
        address.setText(text_address);
        phone.setText((String)addressMap.get("phone"));
        rcvName.setText((String)addressMap.get("receiver_name"));
    }

    private void setTracking() {
        if(order.getOrderAt()!=null)
        {
            statusWaiting.setText(generateTimestamp(order.getOrderAt(),"Order at"));
            statusWaiting.setVisibility(View.VISIBLE);
        }
        else
            statusWaiting.setVisibility(View.GONE);

        if(order.getConfirmAt()!=null)
        {
            statusConfirm.setText(generateTimestamp(order.getConfirmAt(),"Confirmed at"));
            statusConfirm.setVisibility(View.VISIBLE);
        }
        else
            statusConfirm.setVisibility(View.GONE);

        if(order.getDeliveryAt()!=null)
        {
            statusDelivery.setText(generateTimestamp(order.getDeliveryAt(),"Delivery at"));
            statusDelivery.setVisibility(View.VISIBLE);
        }
        else
            statusDelivery.setVisibility(View.GONE);

        if(order.getCompleteAt()!=null)
        {
            statusComplete.setText(generateTimestamp(order.getCompleteAt(),"Completed at"));
            statusComplete.setVisibility(View.VISIBLE);
        }
        else
            statusComplete.setVisibility(View.GONE);
    }
    private String generateTimestamp(Timestamp timestamp, String status) {
        Date date = timestamp.toDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return status+" "+dateFormat.format(date);
    }
    @SuppressLint("SetTextI18n")
    private void setHeader() {
        shopName.setText(order.getShop());
        orderCode.setText("Order code: "+order.getId());
        ShopRepository shopRepository=new ShopRepository(this);
        shopRepository.getIdByName(order.getShop()).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()) {
                    String shopId=task.getResult();
                    if(shopId!=null) {
                        shopName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(DetailOrder.this, ShopPageActivity.class);
                                i.putExtra("shopID", shopId);
                                startActivity(i);
                            }
                        });
                    }
                }
            }
        });
    }

    private void setWidgets() {
        btBack=findViewById(R.id.btBack);
        markDone = findViewById(R.id.markDone);
        rcv_product_item = findViewById(R.id.rcv_product_item);
        shopName = findViewById(R.id.shopName);
        orderCode = findViewById(R.id.orderCode);
        statusWaiting = findViewById(R.id.statusWaiting);
        statusConfirm = findViewById(R.id.statusConfirm);
        statusDelivery = findViewById(R.id.statusDelivery);
        statusComplete = findViewById(R.id.statusComplete);
        rcvName = findViewById(R.id.rcvName);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        subTotal = findViewById(R.id.subTotal);
        total = findViewById(R.id.total);
        cancel=findViewById(R.id.cancel);
    }
}