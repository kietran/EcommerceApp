package com.example.EcommerceApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.EcommerceApp.adapter.OrderAdapter;
import com.example.EcommerceApp.domain.user.OrderRepository;
import com.example.EcommerceApp.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderOrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrderOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderOrderFragment newInstance(String param1, String param2) {
        OrderOrderFragment fragment = new OrderOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    androidx.recyclerview.widget.RecyclerView rcMyOrder;
    ProgressBar prgbOrder;
    TextView noOrder;
    OrderAdapter orderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_my_order_order, container, false);
        setWidgets(view);
        setOrderView();
        // Register broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("order_completed");
        intentFilter.addAction("order_canceled");
        requireActivity().registerReceiver(orderCompletedReceiver, intentFilter);
        return view;
    }

    private void setOrderView() {
        OrderRepository orderRepository=new OrderRepository();
        prgbOrder.setVisibility(View.VISIBLE);
        orderRepository.getOrdersWithStatusNotComplete().addOnCompleteListener(new OnCompleteListener<List<Order>>() {
            @Override
            public void onComplete(@NonNull Task<List<Order>> task) {
                if (task.isSuccessful()) {
                    List<Order> orders = task.getResult();
                    Log.i("size orders", String.valueOf(orders.size()));
                    prgbOrder.setVisibility(View.INVISIBLE);
                    if(!orders.isEmpty()) {
                        noOrder.setVisibility(View.INVISIBLE);
                        rcMyOrder.setVisibility(View.VISIBLE);
                        orderAdapter = new OrderAdapter(orders);
                        orderAdapter.setContext(getContext());
                        rcMyOrder.setAdapter(orderAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        rcMyOrder.setLayoutManager(linearLayoutManager);
                    }
                    else {
                        noOrder.setVisibility(View.VISIBLE);
                        rcMyOrder.setVisibility(View.INVISIBLE);
                    }

                } else {
                    System.err.println("Error getting orders: " + task.getException());
                }
            }
        });
    }

    private void setWidgets(View view) {
        rcMyOrder=view.findViewById(R.id.rcMyOrder);
        prgbOrder=view.findViewById(R.id.prgbOrder);
        noOrder=view.findViewById(R.id.noOrder);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // ... other onDestroy code

        // Unregister broadcast receiver to avoid memory leaks
        if (orderCompletedReceiver != null)
            requireActivity().unregisterReceiver(orderCompletedReceiver);
    }
    private final BroadcastReceiver orderCompletedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("order_completed")) {
                setOrderView();
                Toast.makeText(getContext(),"Mark order done successfully!", Toast.LENGTH_LONG).show();
            }
            else if(intent.getAction().equals("order_canceled")){
                setOrderView();
                Toast.makeText(getContext(), "Order cancel successfully", Toast.LENGTH_SHORT).show();
            }
        }
    };
}