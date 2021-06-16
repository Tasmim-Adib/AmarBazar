package com.example.amarmarket;

public class CategorySliderItem {
    private int image;
    private String title;

    public CategorySliderItem(int image,String title) {
        this.image = image;
        this.title = title;
    }
    public  int getImage(){
        return image;
    }
    public String getTitle(){
        return title;
    }
}
