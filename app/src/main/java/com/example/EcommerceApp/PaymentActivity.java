package com.example.EcommerceApp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.EcommerceApp.adapter.CartItemAdapter;
import com.example.EcommerceApp.adapter.CartProductAdapter;
import com.example.EcommerceApp.adapter.CheckoutItemAdapter;
import com.example.EcommerceApp.domain.user.AddressRepository;
import com.example.EcommerceApp.domain.user.OrderItemRepository;
import com.example.EcommerceApp.domain.user.OrderRepository;
import com.example.EcommerceApp.domain.user.ProductItemRepository;
import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.example.EcommerceApp.model.Address;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    ImageView btBack, detail;
    TextView dfAddress,dfPhone,dfName,total;
    Button addAddress, checkout;
    LinearLayout layout_yes, layout_no;
    androidx.recyclerview.widget.RecyclerView rcv_checkout_item;
    CheckoutItemAdapter checkoutItemAdapter;
    Address selectedAddress;
    int REQUEST_CODE =12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        selectedAddress = null;
        dfAddress=findViewById(R.id.dfAddress);
        dfPhone =findViewById(R.id.dfPhone);
        dfName = findViewById(R.id.dfName);
        btBack=findViewById(R.id.btBack);
        btBack.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        detail = findViewById(R.id.detail);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(PaymentActivity.this, AddressActivity.class);
                i.putExtra("get_addressId",true);
                startActivityForResult(i,REQUEST_CODE);
            }
        });
        // checkout
        checkout=findViewById(R.id.btCheckout2);
        ///
        total = findViewById(R.id.total);
        total.setText(String.valueOf(CartProductAdapter.totalFee));
        ///
        rcv_checkout_item = findViewById(R.id.rcv_checkout_item);
        checkoutItemAdapter = new CheckoutItemAdapter(CartItemAdapter.selectList,this);
        rcv_checkout_item.setAdapter(checkoutItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_checkout_item.setLayoutManager(linearLayoutManager);
        ///
        addAddress=findViewById(R.id.addAddress);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(PaymentActivity.this, AddressActivity.class);
                i.putExtra("get_addressId",true);
                startActivityForResult(i,REQUEST_CODE);
            }
        });

        layout_yes=findViewById(R.id.layout_yes);
        layout_no=findViewById(R.id.layout_no);
        setDefaultAddress();



        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedAddress==null)
                    remindUser();
                else
                {
                    handleCheckout();
                }
            }
        });
    }

    private void handleCheckout() {
        if(checkoutItemAdapter.groupedCartItems==null)
            return;
        for (Map.Entry<String, List<ShoppingCartItem>> entry : checkoutItemAdapter.groupedCartItems.entrySet()) {
            String shopNames = entry.getKey();
            List<ShoppingCartItem> shoppingCartItems = entry.getValue();
            // add order
            createOrder(shopNames, shoppingCartItems);
            CartItemAdapter.selectList=new ArrayList<>();
        }
        OrderSuccess orderSuccess = OrderSuccess.newInstance();
        orderSuccess.setOnDismissListener(new OrderSuccess.OnDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
        ;
        orderSuccess.show(getSupportFragmentManager(),"order tracking");
    }



    private void createOrder(String shopNames,List<ShoppingCartItem> shoppingCartItems) {
        Log.i("create order", "start");
        OrderRepository orderRepository = new OrderRepository();
        orderRepository.create(shopNames,"ON PROGRESS",selectedAddress, Timestamp.now())  .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String documentId) {
                        for (ShoppingCartItem item : shoppingCartItems) {
                            System.out.println("Item: " + item);
                            //add order item
                            createOrderItem(documentId,item);
                            //delete cart item
                            deleteCartItem(item.getId());
                            //update qty
                            updateQtyInStock(item);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.err.println("Error adding order: " + e.getMessage());
                    }
                });;
    }

    private void updateQtyInStock(ShoppingCartItem item) {
        //update qty product item

        String productItemId = (String)item.getProduct_item().get("id");


        ProductItemRepository productItemRepository = new ProductItemRepository(this);
        productItemRepository.getQtyInStock(productItemId).addOnCompleteListener(new OnCompleteListener<Long>() {
            @Override
            public void onComplete(@NonNull Task<Long> task) {
                Long qty_in_stock= task.getResult();
                updateQtyProductItem(productItemId, (int) (qty_in_stock-item.getQty()));
                //update qty product item in cart item
                updateQtyProductItemInCartItem(item.getId(),(int) (qty_in_stock-item.getQty()));
            }
        });

    }

    private void updateQtyProductItemInCartItem(String productItemId, int qty) {
        Log.i("update qty product item in cart item", "start");
        ShoppingCartItemRepository shoppingCartItemRepository = new ShoppingCartItemRepository();
        shoppingCartItemRepository.updateQtyInStockForProductItem(productItemId,qty);
    }

    private void updateQtyProductItem(String productItemId, int qty) {
        Log.i("update qty in product item", "start");
        ProductItemRepository productItemRepository = new ProductItemRepository(this);
        productItemRepository.updateQtyInStock(productItemId,qty);
    }

    private void deleteCartItem(String cartItemId) {
        Log.i("delete cart item", "start");
        ShoppingCartItemRepository shoppingCartItemRepository = new ShoppingCartItemRepository();
        shoppingCartItemRepository.delete(cartItemId);
    }

    private void createOrderItem(String order_id, ShoppingCartItem cartItem) {
        Log.i("create order item", "start");
        OrderItemRepository orderItemRepository = new OrderItemRepository();
        orderItemRepository.create(order_id,cartItem);
    }

    private void remindUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Select Address");
        builder.setMessage("Please select an address before proceeding to checkout.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setDefaultAddress() {
        AddressRepository addressRepository =new AddressRepository();
        addressRepository.getDefaultAddress(FirebaseUtil.currentUserId()).addOnCompleteListener(new OnCompleteListener<Address>() {
            @Override
            public void onComplete(@NonNull Task<Address> task) {
                Address defaultAddress = task.getResult();
                if(defaultAddress==null)
                {
                    layout_yes.setVisibility(View.INVISIBLE);
                    layout_no.setVisibility(View.VISIBLE);
                }
                else {
                    layout_yes.setVisibility(View.VISIBLE);
                    layout_no.setVisibility(View.INVISIBLE);
                    selectedAddress=defaultAddress;
                    setTextAddress(selectedAddress);
                }
            }
        });
    }
    private void setTextAddress(Address address)
    {
        layout_yes.setVisibility(View.VISIBLE);
        layout_no.setVisibility(View.INVISIBLE);
        dfName.setText(address.getReceiver_name());
        dfPhone.setText(address.getPhone());
        String text_address = address.getAddress_line()+", "+address.getWard()+", "+address.getDistrict()+", "+address.getProvince();
        dfAddress.setText(text_address);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String address_id = data.getStringExtra("address_id");
                Log.i("payment receiver address id",address_id);
                refreshAddress(address_id);
            }
            else
            {
                setDefaultAddress();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void refreshAddress(String addressId) {
        AddressRepository addressRepository = new AddressRepository();
        addressRepository.getAddressById(addressId).addOnCompleteListener(new OnCompleteListener<Address>() {
            @Override
            public void onComplete(@NonNull Task<Address> task) {
                selectedAddress = task.getResult();
                setTextAddress(selectedAddress);
            }
        });
    }
}