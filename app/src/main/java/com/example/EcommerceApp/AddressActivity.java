package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.EcommerceApp.adapter.AddressAdapter;
import com.example.EcommerceApp.domain.user.AddressRepository;
import com.example.EcommerceApp.model.Address;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    ImageView btClose;
    androidx.cardview.widget.CardView layout_add;
    AddressAdapter addressAdapter;
    androidx.recyclerview.widget.RecyclerView rcv_address;
    AddressRepository addressRepository;
    List<Address> listAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        boolean get_addressId = getIntent().getBooleanExtra("get_addressId", false);




        btClose=findViewById(R.id.btClose);
        btClose.setOnClickListener(view -> {
            finish();
        });

        layout_add=findViewById(R.id.layout_add);
        layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddressActivity.this, AddressSetting.class);
                startActivity(i);
            }
        });

        rcv_address= findViewById(R.id.rcv_address);
        listAddress= new ArrayList<>();
        addressAdapter = new AddressAdapter(listAddress, this);
        addressAdapter.setGet_addressId(get_addressId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_address.setLayoutManager(linearLayoutManager);
        rcv_address.setAdapter(addressAdapter);


        setRcv_address();

    }
    @Override
    protected void onResume() {
        super.onResume();
        setRcv_address();
    }
    private void setRcv_address() {
        addressRepository = new AddressRepository();
        addressRepository.getAddressByUserId(FirebaseUtil.currentUserId()).addOnCompleteListener(new OnCompleteListener<List<Address>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<List<Address>> task) {
                if (task.isSuccessful()) {
                    listAddress.clear();
                    listAddress.addAll(task.getResult());
                    addressAdapter.notifyDataSetChanged();
                } else {
                    Log.e("AddressActivity", "Error getting addresses: ", task.getException());
                }
            }
        });
    }

}