package com.example.EcommerceApp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.databinding.ColorItemBinding;
import com.example.EcommerceApp.model.Product;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SizesAdapter extends RecyclerView.Adapter<SizesAdapter.SizesViewHolder> {

    private String selectedSize;
    private List<String> mListSizes;
    private Context mContext;
    public SizesAdapter(Context context, List<String> ListSizes){
        this.mContext = context;
        this.mListSizes = ListSizes;
        if (mListSizes.size() > 0)
            this.selectedSize = mListSizes.get(0);
    }
    public String getSelectedSize(){
        return selectedSize;
    }
    public SizesAdapter.SizesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.size_item,parent,false);

        return new SizesAdapter.SizesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizesViewHolder holder, int position) {

        String txtSize = mListSizes.get(position);
        setAllPickFalse(holder);
        if (txtSize == selectedSize){
            holder.imageShadow.setVisibility(View.VISIBLE);
        }
        holder.size.setText(txtSize);
        holder.size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (holder.imageShadow.getVisibility() == View.INVISIBLE){
//                    holder.imageShadow.setVisibility(View.VISIBLE);
//                }
//                else{
//                    holder.imageShadow.setVisibility(View.INVISIBLE);
//                }
                selectedSize = txtSize;
                notifyDataSetChanged();
            }
        });
    }

    private void setAllPickFalse(SizesViewHolder holder) {
        for (String color : mListSizes){
            holder.imageShadow.setVisibility(View.INVISIBLE);
        }
    }

    public int getItemCount() {
        if(mListSizes!=null)
            return mListSizes.size();
        else return 0;
    }
    public void updateData(List<String> mListSizes) {
        this.mListSizes = mListSizes;
        this.selectedSize = mListSizes.get(0);
    }
    public static class SizesViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView imageShadow;
        private final TextView size;
        public SizesViewHolder(@NonNull View itemView){
            super(itemView);
            imageShadow = itemView.findViewById(R.id.imageShadow);
            size = itemView.findViewById(R.id.size);
        }
    }


}
