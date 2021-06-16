package com.example.amarmarket;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfferProducrSlider {
    class SliderHolder extends RecyclerView.ViewHolder{
        private ImageView offerProductImage;
        private TextView offerProductType,getOfferProductPrice;
        public SliderHolder(@NonNull View itemView) {
            super(itemView);

            offerProductImage = itemView.findViewById(R.id.offer_product_image);
            offerProductType = itemView.findViewById(R.id.offer_product_type);
            getOfferProductPrice = itemView.findViewById(R.id.offer_product_price);
        }
    }
}
