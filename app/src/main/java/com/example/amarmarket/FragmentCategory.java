package com.example.amarmarket;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment {
    private HorizontalInfiniteCycleViewPager viewPager2,womenFashionViewPager,kidFashioViewpager,giftItemViewpager;
    private View itemView;
    private Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        itemView =  inflater.inflate(R.layout.fragment_category,container,false);

        viewPager2 = itemView.findViewById(R.id.category_viewpager);
        manFashionSlider();
        womenFashionViewPager = itemView.findViewById(R.id.woman_viewpager);
        womanFashionSlider();

        kidFashioViewpager = itemView.findViewById(R.id.kid_viewpager);
        kidFashionSlider();

        giftItemViewpager = itemView.findViewById(R.id.gift_viewpager);
        giftSlider();

        return itemView;
    }

    private void manFashionSlider(){
        List<CategorySliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new CategorySliderItem(R.drawable.men_tshirt,"T_Shirt"));
        sliderItems.add(new CategorySliderItem(R.drawable.man_jeans,"Jeans"));
        sliderItems.add(new CategorySliderItem(R.drawable.man_panjabi,"Panjabi"));
        sliderItems.add(new CategorySliderItem(R.drawable.man_hoody,"Hoody"));
        sliderItems.add(new CategorySliderItem(R.drawable.men_shirt,"Shirt"));
        sliderItems.add(new CategorySliderItem(R.drawable.man_shoe,"Shoe"));
        MyAdapter adapter = new MyAdapter(getContext(),sliderItems);
        viewPager2.setAdapter(adapter);
    }
    private void womanFashionSlider() {
        List<CategorySliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new CategorySliderItem(R.drawable.woman_saree,"Saree"));
        sliderItems.add(new CategorySliderItem(R.drawable.woman_salower,"Salower"));
        sliderItems.add(new CategorySliderItem(R.drawable.woman_borkha,"Borkha"));
        sliderItems.add(new CategorySliderItem(R.drawable.woman_hijab,"Hijab"));
        sliderItems.add(new CategorySliderItem(R.drawable.woman_tshirt,"Woman T Shirt"));
        MyAdapter adapter = new MyAdapter(getContext(),sliderItems);
        womenFashionViewPager.setAdapter(adapter);
    }
    private void kidFashionSlider() {
        List<CategorySliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new CategorySliderItem(R.drawable.kid_woman_dress,"Dress"));
        sliderItems.add(new CategorySliderItem(R.drawable.kid_tshirt,"Kid T Shirt"));
        sliderItems.add(new CategorySliderItem(R.drawable.kid_pant,"Kid Pant"));
        sliderItems.add(new CategorySliderItem(R.drawable.kid_panjabi,"Kid Panjabi"));
        MyAdapter adapter = new MyAdapter(getContext(),sliderItems);
        kidFashioViewpager.setAdapter(adapter);
    }
    private void giftSlider() {
        List<CategorySliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new CategorySliderItem(R.drawable.gift_wedding,"Wedding Gift"));
        sliderItems.add(new CategorySliderItem(R.drawable.gift_birthday,"Birthday Gift"));
        MyAdapter adapter = new MyAdapter(getContext(),sliderItems);
        giftItemViewpager.setAdapter(adapter);

    }

}
