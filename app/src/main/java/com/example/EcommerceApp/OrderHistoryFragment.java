package com.example.EcommerceApp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFragment newInstance(String param1, String param2) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
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
    androidx.recyclerview.widget.RecyclerView rcHistory;
    ProgressBar prgbHistory;
    TextView noOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_my_order_history, container, false);
        setWidgets(view);
        setOrderView();
        return view;
    }
    private void setOrderView() {
        OrderRepository orderRepository=new OrderRepository();
        orderRepository.getOrdersWithStatusComplete().addOnCompleteListener(new OnCompleteListener<List<Order>>() {
            @Override
            public void onComplete(@NonNull Task<List<Order>> task) {
                if (task.isSuccessful()) {
                    List<Order> orders = task.getResult();
                    Log.i("size orders", String.valueOf(orders.size()));
                    prgbHistory.setVisibility(View.INVISIBLE);
                    if(!orders.isEmpty()) {
                        noOrder.setVisibility(View.INVISIBLE);
                        rcHistory.setVisibility(View.VISIBLE);
                        OrderAdapter orderAdapter = new OrderAdapter(orders);
                        orderAdapter.setContext(getContext());
                        rcHistory.setAdapter(orderAdapter);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        rcHistory.setLayoutManager(linearLayoutManager);
                    }
                    else {
                        noOrder.setVisibility(View.VISIBLE);
                    }

                } else {
                    System.err.println("Error getting orders: " + task.getException());
                }
            }
        });
    }

    private void setWidgets(View view) {
        rcHistory=view.findViewById(R.id.rcHistory);
        prgbHistory=view.findViewById(R.id.prgbHistory);
        noOrder=view.findViewById(R.id.noOrder);
    }
}