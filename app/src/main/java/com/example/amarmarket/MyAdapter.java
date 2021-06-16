package com.example.amarmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class MyAdapter extends PagerAdapter {
    Context context;
    List<CategorySliderItem> sliderItems;

    public MyAdapter(Context context, List<CategorySliderItem> sliderItems) {
        this.context = context;
        this.sliderItems = sliderItems;
    }

    @Override
    public int getCount() {
        return sliderItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item,container,false);
        ImageView cardViewImage = view.findViewById(R.id.cardViewImage);
        final TextView cardViewname = view.findViewById(R.id.cardViewTitle);

        cardViewImage.setImageResource(sliderItems.get(position).getImage());
        cardViewname.setText(sliderItems.get(position).getTitle());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,sliderItems.get(position).getTitle().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view);
        return view;
    }
}
