package com.example.EcommerceApp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.AddressActivity;
import com.example.EcommerceApp.AddressSetting;
import com.example.EcommerceApp.R;
import com.example.EcommerceApp.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    List<Address> addresses;
    Context context;

    public boolean isGet_addressId() {
        return get_addressId;
    }

    public void setGet_addressId(boolean get_addressId) {
        this.get_addressId = get_addressId;
    }

    boolean get_addressId;

    public AddressAdapter(List<Address> addresses, Context context) {
        this.addresses = addresses;
        this.context = context;
    }


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false);
        return new AddressAdapter.AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addresses.get(position);
        if(address==null)
            return;
        holder.name.setText(address.getReceiver_name());
        holder.phone.setText(address.getPhone());
        String text_address = address.getAddress_line()+", "+address.getWard()+", "+address.getDistrict()+", "+address.getProvince();
        holder.text_address.setText(text_address);
        if(address.getIsDefault())
            holder.text_default.setVisibility(View.VISIBLE);
        else
            holder.text_default.setVisibility(View.INVISIBLE);

        if(get_addressId)
        {
            holder.containerAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    address.getId();
                    if(context instanceof AddressActivity){
                        AddressActivity addressActivity = (AddressActivity) context;
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("address_id", address.getId());
                        addressActivity.setResult(Activity.RESULT_OK, returnIntent);
                        addressActivity.finish();
                    }
                    Log.i("selected address id",address.getId() );
                }
            });
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddressSetting.class);
                i.putExtra("address_id", address.getId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(addresses!=null)
            return addresses.size();
        return 0;
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder{
        TextView edit, name, phone, text_address,text_default;
        LinearLayout containerAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            edit = itemView.findViewById(R.id.edit);
            name = itemView.findViewById(R.id.rcv_name);
            phone = itemView.findViewById(R.id.phone);
            text_address = itemView.findViewById(R.id.address);
            text_default = itemView.findViewById(R.id.textDefault);
            containerAddress = itemView.findViewById(R.id.container_address);
        }
    }
}
