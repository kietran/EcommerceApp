package com.example.EcommerceApp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.utils.AddressUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     AddressSelectDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class AddressSelect extends BottomSheetDialogFragment {

    private static final String ARG_ITEMS = "items";
    private List<String> items;
    private ItemAdapter adapter;

    public TextView getAddress() {
        return address;
    }

    public void setAddress(TextView address) {
        this.address = address;
    }

    TextView address;

    @SuppressLint("SetTextI18n")
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(ward)
            address.setText(AddressUtil.ward+", "+AddressUtil.district+", "+AddressUtil.province);
    }

    public static AddressSelect newInstance(ArrayList<String> items) {
        final AddressSelect fragment = new AddressSelect();
        final Bundle args = new Bundle();
        args.putStringArrayList(ARG_ITEMS, items);
        fragment.setArguments(args);
        province =false;
        district=false;
        ward=false;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_address_select_dialog_list_dialog, container, false);
    }
    static boolean province, district,ward;
    TextView province_text,ward_text,district_text;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        items = getArguments().getStringArrayList(ARG_ITEMS);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        province_text = view.findViewById(R.id.province);
        ward_text = view.findViewById(R.id.ward);
        district_text = view.findViewById(R.id.district);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(items);


        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateItems(List<String> newItems) {
        if(items!=null)
            items.clear();
        else
            items=new ArrayList<>();
        items.addAll(newItems);
        adapter.notifyDataSetChanged();
    }



    private class ViewHolder extends RecyclerView.ViewHolder {
        final TextView text;

        ViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.text);
            text.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    text.setTextColor(R.color.main_purple);
                    if(!province)
                    {
                        province=true;
                        AddressUtil.province= (String) text.getText();
                        province_text.setText(AddressUtil.province);
                        district_text.setVisibility(View.VISIBLE);
                        AddressUtil.initialDistrict((String) text.getText());
                    }
                    else if(!district){
                        district=true;
                        AddressUtil.district= (String) text.getText();
                        district_text.setText(AddressUtil.district);
                        ward_text.setVisibility(View.VISIBLE);
                        AddressUtil.initialWard((String)text.getText());
                    }
                    else {
                        AddressUtil.ward= (String) text.getText();
                        ward = true;
                        ward_text.setText(text.getText());
                        dismiss();
                    }
                }
            });
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {
        private final List<String> mItems;

        ItemAdapter(List<String> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_address_select_dialog_list_dialog_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.text.setText(mItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
