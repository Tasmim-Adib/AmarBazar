package com.example.amarmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment {
    private View itemView;
    private GridLayout categoryGridLayout;
    private CardView manFashion,womanFashion,kidFashion,giftItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView =  inflater.inflate(R.layout.fragment_category,container,false);
        categoryGridLayout = itemView.findViewById(R.id.category_grid_layout);

        manFashion = itemView.findViewById(R.id.man_fashion_cardView);
        womanFashion = itemView.findViewById(R.id.woman_fashion_cardView);
        kidFashion = itemView.findViewById(R.id.kid_fashion_cardView);
        giftItem = itemView.findViewById(R.id.gift_item_cardView);
        manFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CategorizedProduct.class);
                intent.putExtra("Fashion","Man_Fashion");
                startActivity(intent);
            }
        });
        womanFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CategorizedProduct.class);
                intent.putExtra("Fashion","Woman_Fashion");
                startActivity(intent);
            }
        });
        kidFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CategorizedProduct.class);
                intent.putExtra("Fashion","Kid_Fashion");
                startActivity(intent);
            }
        });
        giftItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CategorizedProduct.class);
                intent.putExtra("Fashion","Gift_item");
                startActivity(intent);
            }
        });

        return itemView;
    }



}
