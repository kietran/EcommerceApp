package com.example.EcommerceApp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     CancelOrder.newInstance(order_id).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class CancelOrder extends BottomSheetDialogFragment {

    private static final String ARG_ORDER_ID = "order_id";
    private String orderId;

    public static CancelOrder newInstance(String orderId) {
        CancelOrder fragment = new CancelOrder();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }
    Button bt_cancel,bt_submit;
    EditText input_reason;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_cancel_order, container, false);
        if (getArguments() != null) {
            orderId = getArguments().getString(ARG_ORDER_ID);
        }
        bt_cancel=view.findViewById(R.id.bt_cancel);
        bt_submit=view.findViewById(R.id.bt_submit);
        input_reason=view.findViewById(R.id.input_reason);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = input_reason.getText().toString().trim();
                if (TextUtils.isEmpty(reason)) {
                    input_reason.setError("Reason is required");
                } else {
                    ((DetailOrder) getActivity()).performCancel(orderId, reason);
                    dismiss();
                }
            }
        });
        return view;
    }



 

    

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
