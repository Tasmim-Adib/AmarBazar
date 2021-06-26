package com.example.amarmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomeViewHolder> {
    ArrayList<Upload> arrayList;
    OnNoteListenerHome mOnNoteListenerHome;
    Context context;

    public HomePageAdapter(ArrayList<Upload> arrayList, Context context, OnNoteListenerHome mOnNoteListenerHome) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnNoteListenerHome = mOnNoteListenerHome;
    }

    @NonNull
    @Override
    public HomePageAdapter.HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_home_card, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view, mOnNoteListenerHome);
        return homeViewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull HomePageAdapter.HomeViewHolder holder, int position) {
        Upload upload = arrayList.get(position);
        holder.name.setText(upload.getProductID());
        holder.price.setText(upload.getProductPrice());
        Picasso.get().load(upload.getProductImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView name;
        TextView price;
        OnNoteListenerHome onNoteListenerHome;

        public HomeViewHolder(@NonNull View itemView, OnNoteListenerHome onNoteListenerHome) {
            super(itemView);

            image = itemView.findViewById(R.id.featured_product_image);
            name = itemView.findViewById(R.id.featured_product_name);
            price = itemView.findViewById(R.id.featured_product_price);
            this.onNoteListenerHome = onNoteListenerHome;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListenerHome.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListenerHome {
        void onNoteClick(int position);
    }
}
