package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderSuccess#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderSuccess extends BottomSheetDialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters


    public OrderSuccess() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrderSuccess.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderSuccess newInstance() {
        OrderSuccess fragment = new OrderSuccess();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    private OnDismissListener onDismissListener;

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
//        if (getArguments() != null) {
//            orderId = getArguments().getString(ARG_PARAM1);
//        }
    }
    Button btTrack;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_order_success, container, false);
        btTrack = view.findViewById(R.id.btTrack);
        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), UserActivity.class);
                intent.putExtra("order", true);
                startActivity(intent);
            }
        });
        return view;
    }
    Context context;
    public void setContext(Context context) {
        this.context = context;
    }
}