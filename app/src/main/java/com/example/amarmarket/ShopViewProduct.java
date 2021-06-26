package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShopViewProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ImageView productImageView;
    private TextView productName,productType,productPrice,discountPrice,availability;
    private Spinner sizeSpinner;
    private EditText quantityEditText,currentPriceEditText;
    private Button okButton,doneButton,updateButton,markAsDiscountButton,markdiscountDone;
    private LinearLayout sizeQuantitylayout;
    private TextView firstSize,firstQuantity,secondSize,secondQuantity,thirdSize,thirdQuantity,fourthsize,fourthQuantity
            ,fiftSize,fifthQuantity,sixthsize,sixthQuantiy,seventhSize,seventhQuantity,eightSize,eightSizeQuantity,nineSize,nineSizeQuantity;


    private String id,size;
    private View view1,view2;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUser,category,subCategory;

    private int sixYard,one,two,three,four,five,six,seven,eight,nine,s,m,l,xl,xxl,oneyr,twoyr,threeyr,fouryr,fiveyr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_view_product);
        id = getIntent().getExtras().get("PRODUCT_ID").toString();

        init();
        loadData(id);
        sizeSpinner.setOnItemSelectedListener(this);
        availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizeQuantitylayout.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                loadAvailableSize(id);
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct(id);
                Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT).show();
            }
        });

        markAsDiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPriceEditText.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);

            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAsDiscount(id);
                markAsDiscountButton.setVisibility(View.GONE);
                markdiscountDone.setVisibility(View.VISIBLE);
            }
        });

        markdiscountDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"It is already an Offered product",Toast.LENGTH_SHORT).show();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateProduct(String id) {
        int total,sum;
        String a = quantityEditText.getText().toString();
        if(TextUtils.isEmpty(a)){
            total = 0;
        }
        total = Integer.parseInt(a);
        //Toast.makeText(getApplicationContext(),getSize(),Toast.LENGTH_SHORT).show();

        if(getSize().equals("S")){
            sum = getS() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("M")){
            sum = getM() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("L")){
            sum = getL() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }else if(getSize().equals("XL")){
            sum = getXl() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("XXL")){
            sum = getXxl() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("28")){
            sum = getOne() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("30")){
            sum = getTwo() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("32")){
            sum = getThree() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("34")){
            sum = getFour() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("36")){
            sum = getFive() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("38")){
            sum = getSix() + total;
            Toast.makeText(getApplicationContext(),Integer.toString(sum),Toast.LENGTH_SHORT).show();
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("40")){
            sum = getSeven() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("42")){
            sum = getEight() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("44")){
            sum = getNine() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("1 yr")){
            sum = getOneyr() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("2 yr")){
            sum = getTwoyr() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("3 yr")){
            sum = getThreeyr() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("4 yr")){
            sum = getFouryr() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("5 yr")){
            sum = getFiveyr() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }
        else if(getSize().equals("6 yard")){
            sum = getSixYard() + total;
            databaseReference.child("Product").child(id).child("Sizes").child(getSize()).setValue(Integer.toString(sum));
        }

    }


    private void init(){
        productImageView = findViewById(R.id.shop_product_details_image);
        productName = findViewById(R.id.shop_product_details_product_id);
        productType = findViewById(R.id.shop_product_details_type);
        productPrice = findViewById(R.id.shop_product_details_price);
        discountPrice = findViewById(R.id.shop_product_details_discount_price);
        sizeSpinner = findViewById(R.id.shop_product_details_spinner_size);
        quantityEditText = findViewById(R.id.shop_product_details_quantity_editText);
        currentPriceEditText = findViewById(R.id.discount_price_edit_Text);
        okButton = findViewById(R.id.shop_product_details_ok_Button);
        doneButton = findViewById(R.id.discount_price_done_btn);
        updateButton = findViewById(R.id.shop_product_update_btn);
        markAsDiscountButton = findViewById(R.id.shop_product_mark_discount_Btn);
        availability = findViewById(R.id.shop_product_details_availability);
        sizeQuantitylayout = findViewById(R.id.shop_size_quantity_layout);
        firstSize = findViewById(R.id.first_size);
        firstQuantity = findViewById(R.id.first_size_quantity);
        markdiscountDone = findViewById(R.id.shop_product_mark_discount_done_Btn);

        secondSize = findViewById(R.id.second_size);
        secondQuantity = findViewById(R.id.second_size_quantity);

        thirdSize = findViewById(R.id.third_size);
        thirdQuantity = findViewById(R.id.third_size_quantity);

        fourthsize = findViewById(R.id.fourth_size);
        fourthQuantity = findViewById(R.id.fourth_size_quantity);

        fiftSize = findViewById(R.id.fifth_size);
        fifthQuantity = findViewById(R.id.fifth_size_quantity);

        sixthsize = findViewById(R.id.sixth_size);
        sixthQuantiy = findViewById(R.id.sixth_size_quantity);

        seventhSize = findViewById(R.id.seventh_size);
        seventhQuantity = findViewById(R.id.seventh_size_quantity);

        eightSize = findViewById(R.id.eight_size);
        eightSizeQuantity = findViewById(R.id.eight_size_quantity);
        nineSize = findViewById(R.id.nine_size);
        nineSizeQuantity = findViewById(R.id.nine_size_quantity);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        view1 = findViewById(R.id.lineViewShop1);
        view2 = findViewById(R.id.lineViewShop2);


    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    private void loadData(String id){
        databaseReference.child("Product").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cat = snapshot.child("category").getValue().toString();
                String subCat = snapshot.child("subCategory").getValue().toString();
                setCategory(cat);
                setSubCategory(subCat);
                if(cat.equals("Kid's Fashion")){

                    ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.kids_fashion, android.R.layout.simple_spinner_dropdown_item);
                    sizeSpinner.setAdapter(adapterSubCat);

                }

                else if(subCat.equals("T-shirt") || subCat.equals("Hoody") || subCat.equals("Hijab")){
                    ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T_shirt, android.R.layout.simple_spinner_dropdown_item);
                    sizeSpinner.setAdapter(adapterSubCat);
                }

                else if(subCat.equals("Saree")){
                    ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Saree, android.R.layout.simple_spinner_dropdown_item);
                    sizeSpinner.setAdapter(adapterSubCat);
                }
                else{
                    ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Men_pant, android.R.layout.simple_spinner_dropdown_item);
                    sizeSpinner.setAdapter(adapterSubCat);
                }


                String image = snapshot.child("productImageUrl").getValue().toString();
                String name = snapshot.child("productID").getValue().toString();
                String price = snapshot.child("productPrice").getValue().toString();
                String type = snapshot.child("productType").getValue().toString();

                productName.setText("Product ID : " +name);
                productType.setText("Product Type : "+type);
                productPrice.setText("Price : "+price+"tk");
                Picasso.get().load(image).into(productImageView);

                if(snapshot.hasChild("discountPrice")){
                    markAsDiscountButton.setVisibility(View.GONE);
                    markdiscountDone.setVisibility(View.VISIBLE);
                    String disPrice = snapshot.child("discountPrice").getValue().toString();
                    discountPrice.setText("Current Price : "+disPrice + "tk");
                    discountPrice.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadAvailableSize(String id) {

        databaseReference.child("Product").child(id).child("Sizes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getCategory().equals("Kid's Fashion")){


                    if(snapshot.hasChild("1 yr")){

                        String q = snapshot.child("1 yr").getValue().toString();
                        Visible(1,"1 yr",q);
                        setOneyr(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("2 yr")){
                        String q = snapshot.child("2 yr").getValue().toString();
                        Visible(2,"2 yr",q);
                        setTwoyr(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("3 yr")){
                        String q = snapshot.child("3 yr").getValue().toString();
                        Visible(3,"3 yr",q);
                        setThreeyr(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("4 yr")){
                        String q = snapshot.child("4 yr").getValue().toString();
                        Visible(4,"4 yr",q);
                        setFouryr(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("5 yr")){
                        String q = snapshot.child("5 yr").getValue().toString();
                        Visible(5,"5 yr",q);
                        setFiveyr(Integer.parseInt(q));
                    }
                }

                else if(getSubCategory().equals("T-shirt") || getSubCategory().equals("Hoody") || getSubCategory().equals("Hijab")){

                    if(snapshot.hasChild("S")){

                        String q = snapshot.child("S").getValue().toString();
                        Visible(1,"S",q);
                        setS(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("M")){
                        String q = snapshot.child("M").getValue().toString();
                        Visible(2,"M",q);
                        setM(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("L")){
                        String q = snapshot.child("L").getValue().toString();
                        Visible(3,"L",q);
                        setL(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("XL")){
                        String q = snapshot.child("XL").getValue().toString();
                        Visible(4,"XL",q);
                        setXl(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("XXL")){
                        String q = snapshot.child("XXL").getValue().toString();
                        Visible(5,"XXL",q);
                        setXxl(Integer.parseInt(q));
                    }

                }

                else if(getSubCategory().equals("Saree")){
                    if(snapshot.hasChild("6 yard")){
                        String q = snapshot.child("6 yard").getValue().toString();
                        Visible(1,"6 yard",q);
                        setSixYard(Integer.parseInt(q));
                    }
                }

                else{

                    if(snapshot.hasChild("28")){

                        String q = snapshot.child("28").getValue().toString();
                        Visible(1,"28",q);
                        setOne(Integer.parseInt(q));

                    }
                    if(snapshot.hasChild("30")){
                        String q = snapshot.child("30").getValue().toString();
                        Visible(2,"30",q);
                        setTwo(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("32")){
                        String q = snapshot.child("32").getValue().toString();
                        Visible(3,"32",q);
                        setThree(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("34")){
                        String q = snapshot.child("34").getValue().toString();
                        Visible(4,"34",q);
                        setFour(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("36")){
                        String q = snapshot.child("36").getValue().toString();
                        Visible(5,"36",q);
                        setFive(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("38")){
                        String q = snapshot.child("38").getValue().toString();
                        Visible(6,"38",q);
                        setSix(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("40")){
                        String q = snapshot.child("40").getValue().toString();
                        Visible(7,"40",q);
                        setSeven(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("42")){
                        String q = snapshot.child("42").getValue().toString();
                        Visible(8,"42",q);
                        setEight(Integer.parseInt(q));
                    }
                    if(snapshot.hasChild("44")){
                        String q = snapshot.child("44").getValue().toString();
                        Visible(9,"44",q);
                        setNine(Integer.parseInt(q));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Visible(int a, String size, String quantity){
        if(a == 1){
            firstSize.setVisibility(View.VISIBLE);
            firstQuantity.setVisibility(View.VISIBLE);
            firstSize.setText(size);
            firstQuantity.setText(quantity);
        }

        if(a == 2){
            secondSize.setVisibility(View.VISIBLE);
            secondQuantity.setVisibility(View.VISIBLE);
            secondSize.setText(size);
            secondQuantity.setText(quantity);
        }
        if(a == 3){
            thirdSize.setVisibility(View.VISIBLE);
            thirdQuantity.setVisibility(View.VISIBLE);
            thirdSize.setText(size);
            thirdQuantity.setText(quantity);
        }
        if(a == 4){
            fourthsize.setVisibility(View.VISIBLE);
            fourthQuantity.setVisibility(View.VISIBLE);
            fourthsize.setText(size);
            fourthQuantity.setText(quantity);
        }
        if(a == 6){
            sixthsize.setVisibility(View.VISIBLE);
            sixthQuantiy.setVisibility(View.VISIBLE);
            sixthsize.setText(size);
            sixthQuantiy.setText(quantity);
        }
        if(a == 5){
            fiftSize.setVisibility(View.VISIBLE);
            fifthQuantity.setVisibility(View.VISIBLE);
            fiftSize.setText(size);
            fifthQuantity.setText(quantity);
        }
        if(a == 7){
            seventhSize.setVisibility(View.VISIBLE);
            seventhQuantity.setVisibility(View.VISIBLE);
            seventhSize.setText(size);
            seventhQuantity.setText(quantity);
        }
        if(a == 8){
            eightSize.setVisibility(View.VISIBLE);
            eightSizeQuantity.setVisibility(View.VISIBLE);
            eightSize.setText(size);
            eightSizeQuantity.setText(quantity);
        }
        if(a == 9){
            nineSize.setVisibility(View.VISIBLE);
            nineSizeQuantity.setVisibility(View.VISIBLE);
            nineSize.setText(size);
            nineSizeQuantity.setText(quantity);
        }

    }


    private void markAsDiscount(String id){

        String discPrice = currentPriceEditText.getText().toString();
        if(TextUtils.isEmpty(discPrice)){
            currentPriceEditText.setError("Enter Discounted Price");
            currentPriceEditText.requestFocus();
            return;
        }

        databaseReference.child("Product").child(id).child("discountPrice").setValue(discPrice);
        databaseReference.child("Offer_Product").child(id).child("isHere").setValue("YES");
        Toast.makeText(this, "Product is Marked as Discount", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String sz = parent.getItemAtPosition(position).toString();
        setSize(sz);
        Toast.makeText(getApplicationContext(),sz,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getTwo() {
        return two;
    }

    public void setTwo(int two) {
        this.two = two;
    }

    public int getThree() {
        return three;
    }

    public void setThree(int three) {
        this.three = three;
    }

    public int getFour() {
        return four;
    }

    public void setFour(int four) {
        this.four = four;
    }

    public int getFive() {
        return five;
    }

    public void setFive(int five) {
        this.five = five;
    }

    public int getSix() {
        return six;
    }

    public void setSix(int six) {
        this.six = six;
    }

    public int getSeven() {
        return seven;
    }

    public void setSeven(int seven) {
        this.seven = seven;
    }

    public int getEight() {
        return eight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getSixYard() {
        return sixYard;
    }

    public void setSixYard(int sixYard) {
        this.sixYard = sixYard;
    }

    public void setEight(int eight) {
        this.eight = eight;
    }

    public int getNine() {
        return nine;
    }

    public void setNine(int nine) {
        this.nine = nine;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getXl() {
        return xl;
    }

    public void setXl(int xl) {
        this.xl = xl;
    }

    public int getXxl() {
        return xxl;
    }

    public void setXxl(int xxl) {
        this.xxl = xxl;
    }

    public int getOneyr() {
        return oneyr;
    }

    public void setOneyr(int oneyr) {
        this.oneyr = oneyr;
    }

    public int getTwoyr() {
        return twoyr;
    }

    public void setTwoyr(int twoyr) {
        this.twoyr = twoyr;
    }

    public int getThreeyr() {
        return threeyr;
    }

    public void setThreeyr(int threeyr) {
        this.threeyr = threeyr;
    }

    public int getFouryr() {
        return fouryr;
    }

    public void setFouryr(int fouryr) {
        this.fouryr = fouryr;
    }

    public int getFiveyr() {
        return fiveyr;
    }

    public void setFiveyr(int fiveyr) {
        this.fiveyr = fiveyr;
    }
}
