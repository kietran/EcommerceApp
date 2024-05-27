package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.EcommerceApp.adapter.CartItemAdapter;
import com.example.EcommerceApp.domain.user.ShoppingCartItemRepository;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCart#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCart extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton btDelete;
    androidx.recyclerview.widget.RecyclerView rcv_cart_item;
    ShoppingCartItemRepository shoppingCartItemRepository;
    CartItemAdapter cartItemAdapter;
    Map<String, List<ShoppingCartItem>> groupedCartItems;
    private BottomSheetDialog bottomSheetCheckOut;
    androidx.cardview.widget.CardView layout_checkout;
    ImageView detailCheckout;
    public MyCart() {
        shoppingCartItemRepository=new ShoppingCartItemRepository();
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favorite.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCart newInstance(String param1, String param2) {
        MyCart fragment = new MyCart();
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




    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cartItemAdapter = new CartItemAdapter(new HashMap<String,List<ShoppingCartItem>>(),requireContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mycart, container, false);
        /// DELETE
        btDelete = view.findViewById(R.id.btDelete);
        cartItemAdapter.setBtnDelete(btDelete);
        /// BOTTOM SHEET UP
        bottomSheetCheckOut = new BottomSheetDialog(requireActivity());
        View bottomSheetView = LayoutInflater.from(requireActivity()).inflate(R.layout.bottom_sheet_checkout,null);
        Button btCheckout = bottomSheetView.findViewById(R.id.btCheckout);
        btCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPayment();
            }
        });
        bottomSheetCheckOut.setContentView(bottomSheetView);
        cartItemAdapter.setSheetCheckout(bottomSheetCheckOut);

        //// LAYOUT CHECKOUT
        layout_checkout = view.findViewById(R.id.layout_checkout);
        Button checkout = layout_checkout.findViewById(R.id.btCheckOut1);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPayment();
            }
        });
        cartItemAdapter.setLayoutCheckout(layout_checkout);
        detailCheckout = layout_checkout.findViewById(R.id.detailCheckout);
        detailCheckout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.println(Log.INFO,"click","click");
                if(!bottomSheetCheckOut.isShowing()) {
                    bottomSheetCheckOut.show();
                    detailCheckout.setImageResource(R.drawable.icons8_down_10);
                }
            }
        });
        bottomSheetCheckOut.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                bottomSheetCheckOut.dismiss();
            }
        });
        bottomSheetCheckOut.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                detailCheckout.setImageResource(R.drawable.icons8_up_10);
            }
        });
        /////
        rcv_cart_item = view.findViewById(R.id.rcv_cart_item);
        rcv_cart_item.setAdapter(cartItemAdapter);
        shoppingCartItemRepository.getCartItemGroupByShop().addOnCompleteListener(task->{
            if(task.isSuccessful())
            {
                groupedCartItems = task.getResult();
                if(!groupedCartItems.isEmpty()) {
                    view.findViewById(R.id.prbCart).setVisibility(View.INVISIBLE);
                    rcv_cart_item.setVisibility(View.VISIBLE);
                    cartItemAdapter.updateData(groupedCartItems);
                    Log.println(Log.ASSERT, "load shopping cart item", String.valueOf(cartItemAdapter.getItemCount()));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    rcv_cart_item.setLayoutManager(linearLayoutManager);
                }
                else {
                    view.findViewById(R.id.prbCart).setVisibility(View.INVISIBLE);
                    TextView noProduct =view.findViewById(R.id.noProduct);
                    noProduct.setVisibility(View.VISIBLE);
                }
            }
            else
                Log.println(Log.INFO,"load cart item", "not success");
        }).addOnFailureListener(e -> {
            Log.println(Log.INFO,"load cart item", Objects.requireNonNull(e.getMessage()));

        });





        return view;
    }

    private void notifyUnCheck() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Checkout Error");
        alertDialog.setMessage("Unselect unavailable products to continue checkout");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.show();
    }
    public void navigateToPayment(){
        if(!CartItemAdapter.selectList.isEmpty()) {
            Log.println(Log.INFO,"all available","1");
            Intent i = new Intent(getActivity(), PaymentActivity.class);
            startActivityForResult(i, 4);
        }
        else
            notifyUnCheck();
    }

    public void reloadFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.detach(this);
            fragmentTransaction.attach(this);
            fragmentTransaction.commit();
        }
    }


}