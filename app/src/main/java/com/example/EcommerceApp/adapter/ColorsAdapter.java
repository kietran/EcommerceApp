package com.example.EcommerceApp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EcommerceApp.R;
import com.example.EcommerceApp.databinding.ColorItemBinding;
import com.example.EcommerceApp.model.Product;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder> {

    private String selectedColor;
    private List<String> mListColors;
    private Context mContext;
    public ColorsAdapter(Context context, List<String> ListColors){
        this.mContext = context;
        this.mListColors = ListColors;
        if (mListColors.size() > 0)
            this.selectedColor = mListColors.get(0);
    }
    public String getSellectedColor(){
        return selectedColor;
    }
    public ColorsAdapter.ColorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item,parent,false);
        return new ColorsAdapter.ColorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorsViewHolder holder, int position) {
        String colorCode = mListColors.get(position);
        if (colorCode == null)
            return;
        setAllPickFalse(holder);
        if (colorCode == selectedColor){
            holder.imageShadow.setVisibility(View.VISIBLE);
            holder.imagePicked.setVisibility(View.VISIBLE);
        }

        int color = Color.parseColor(colorCode);
        Drawable colorDrawable = new ColorDrawable(color);
        holder.imageColor.setImageDrawable(colorDrawable);
        holder.imageColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (holder.imageShadow.getVisibility() == View.INVISIBLE){
//                    holder.imageShadow.setVisibility(View.VISIBLE);
//                    holder.imagePicked.setVisibility(View.VISIBLE);
//                }
//                else{
//                    holder.imageShadow.setVisibility(View.INVISIBLE);
//                    holder.imagePicked.setVisibility(View.INVISIBLE);
//                }
                selectedColor = colorCode;
                notifyDataSetChanged();
            }
        });
    }

    private void setAllPickFalse(ColorsViewHolder holder) {
        for (String color : mListColors){
            holder.imageShadow.setVisibility(View.INVISIBLE);
            holder.imagePicked.setVisibility(View.INVISIBLE);
        }

    }

    public int getItemCount() {
        if(mListColors!=null)
            return mListColors.size();
        else return 0;
    }
    public void updateData(List<String> mListColors) {
        this.mListColors = mListColors;
        this.selectedColor = mListColors.get(0);
    }
    public static class ColorsViewHolder extends RecyclerView.ViewHolder{
        private final CircleImageView imageColor;
        private final CircleImageView imageShadow;
        private final ImageView imagePicked;
        public ColorsViewHolder(@NonNull View itemView){
            super(itemView);
            imageColor = itemView.findViewById(R.id.imageColor);
            imageShadow = itemView.findViewById(R.id.imageShadow);
            imagePicked = itemView.findViewById(R.id.imagePicked);
        }
    }


}