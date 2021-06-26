package com.example.amarmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {
    ArrayList<FeaturedHelperClass> featuredList;
    OnNoteListener monNoteListener;
    Context context;

    public FeaturedAdapter(ArrayList<FeaturedHelperClass> featuredList, Context context,OnNoteListener monNoteListener) {
        this.featuredList = featuredList;
        this.context = context;
        this.monNoteListener = monNoteListener;
    }

    @NonNull
    @Override
    public FeaturedAdapter.FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view, monNoteListener);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedAdapter.FeaturedViewHolder holder, int position) {
        FeaturedHelperClass featuredHelperClass = featuredList.get(position);
        holder.image.setImageResource(featuredHelperClass.getImage());
        holder.productSubCategory.setText(featuredHelperClass.getName());

    }

    @Override
    public int getItemCount() {
        return featuredList.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView productSubCategory;
        OnNoteListener onNoteListener;

        public FeaturedViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            image = itemView.findViewById(R.id.featured_image);
            productSubCategory = itemView.findViewById(R.id.featured_subCat_name);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
