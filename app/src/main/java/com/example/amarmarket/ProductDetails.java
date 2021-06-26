package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView productId,productType,price,discountPrice,changeLocation;
    private ImageView productImage,beforeFavourite,afterFavourite;
    private String findProductId,text,size,currentDate,shop,subCat,cat;
    private Button addToCartButton,okButton,doneButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUser,like,state,orderKey,accountType,contact,address;
    private ProgressDialog loadingBar;
    private Spinner spinnerSize,spinnerPantSize;
    private EditText quantityEditText,contactEdit,addressEdit;
    private ListView listView;
    private LinearLayout linear;
    private TextView availablilty,tvText;
    private View horizontalLineView1,horizontalLineView2;
    private HashMap hashMap;
    private ArrayAdapter<CharSequence> adapter;
    private HashMap hash;
    private TextView firstSize,firstQuantity,secondSize,secondQuantity,thirdSize,thirdQuantity,fourthsize,fourthQuantity
            ,fiftSize,fifthQuantity,sixthsize,sixthQuantiy,seventhSize,seventhQuantity,eightSize,eightSizeQuantity,nineSize,nineSizeQuantity;

    private int sixYard,one,two,three,four,five,six,seven,eight,nine,s,m,l,xl,xxl,oneyr,twoyr,threeyr,fouryr,fiveyr;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        state = "new";
        findProductId = getIntent().getExtras().get("visit_product").toString();
        init();
        loadData();
        hashMap = new HashMap();
        getContactAddress();

        databaseReference.child("Users").child(currentUser).child("Identity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String t = snapshot.child("type").getValue().toString();
                setAccountType(t);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


            databaseReference.child("Users").child(currentUser).child("Favourites")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(findProductId)){
                                afterFavourite.setVisibility(View.VISIBLE);

                            }
                            else{
                                beforeFavourite.setVisibility(View.VISIBLE);
                                beforeFavourite.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        beforeFavourite.setVisibility(View.GONE);
                                        afterFavourite.setVisibility(View.VISIBLE);

                                        int l = Integer.parseInt(getLike());
                                        l = l + 1;
                                        databaseReference.child("Product").child(findProductId).child("reviews").setValue(Integer.toString(l));
                                        databaseReference.child("Users").child(currentUser).child("Favourites").child(findProductId).child("favourites").setValue("YES");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            databaseReference.child("Users").child(currentUser).child("MyCart").
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(findProductId)){
                                addToCartButton.setText("Added");
                                state = snapshot.child(findProductId).child("isHere").getValue().toString();
                            }

                            else{
                                addToCartButton.setText("Add to Cart");

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            databaseReference.child("Product").child(findProductId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String c = snapshot.child("category").getValue().toString();
                    String s = snapshot.child("subCategory").getValue().toString();
                    setCat(c);
                    setSubCat(s);
                    if(cat.equals("Kid's Fashion")){

                        ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.kids_fashion, android.R.layout.simple_spinner_dropdown_item);
                        spinnerSize.setAdapter(adapterSubCat);

                    }

                    else if(subCat.equals("T-shirt") || subCat.equals("Hoody") || subCat.equals("Hijab")){
                        ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T_shirt, android.R.layout.simple_spinner_dropdown_item);
                        spinnerSize.setAdapter(adapterSubCat);


                    }

                    else if(subCat.equals("Saree")){
                        ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Saree, android.R.layout.simple_spinner_dropdown_item);
                        spinnerSize.setAdapter(adapterSubCat);


                    }
                    else{
                        ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Men_pant, android.R.layout.simple_spinner_dropdown_item);
                        spinnerSize.setAdapter(adapterSubCat);


                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        spinnerSize.setOnItemSelectedListener(this);




        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("new")){
                    Calendar calendar = Calendar.getInstance();
                    currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                    orderKey = databaseReference.push().getKey();
                    loadingBar.setTitle("Add to Cart");
                    loadingBar.setMessage("Product is Adding to Your Cart");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    sendProducttoCart();
                    makeAnOrder();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Product is already added to your cart",Toast.LENGTH_SHORT).show();
                }

            }
        });

        availablilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalLineView1.setVisibility(View.VISIBLE);
                horizontalLineView2.setVisibility(View.VISIBLE);
                viewSize();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = quantityEditText.getText().toString().trim();
                int a = checkAvailability(getSize(), Integer.parseInt(q));
                if(a == 1){
                    hashMap.put(getSize(),q);
                    quantityEditText.setText(null);
                }
                else{
                    quantityEditText.requestFocus();
                    quantityEditText.setError("Size is not Available");
                    return;
                }


            }
        });

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactEdit.setVisibility(View.VISIBLE);
                addressEdit.setVisibility(View.VISIBLE);
                doneButton.setVisibility(View.VISIBLE);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setManually();
            }
        });


    }


    private void setManually(){

        String c = contactEdit.getText().toString();
        String a = addressEdit.getText().toString();
        setContact(c);
        setAddress(a);
    }
    private void getContactAddress() {
        databaseReference.child("Users").child(currentUser).child("Identity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String con = snapshot.child("contact").getValue().toString();
                String add = snapshot.child("address").getValue().toString();

                setContact(con);
                setAddress(add);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    private int checkAvailability(String size, int quantity){
        if(size.equals("S")){
            if(quantity > getS())
                return 0;
            else{
                int a = getS() - quantity ;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("S").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("L")){
            if(quantity > getL())
                return 0;
            else{
                int a = getL() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("L").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("M")){
            if (quantity > getM())
                return 0;
            else{
                int a = getM() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("M").setValue(Integer.toString(a));
                return 1;
            }
        }

        else if (size.equals("XL")){
            if(quantity > getXl())
                return 0;
            else{
                int a = getXl() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("XL").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("XXL")){
            if(quantity > getXxl())
                return 0;
            else{
                int a = getXxl() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("XXL").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("28")){
            if(quantity > getOne())
                return 0;
            else{
                int a = getOne() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("28").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("30")){
            if(quantity > getTwo())
                return 0;
            else{
                int a = getTwo() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("30").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("32")){
            if(quantity > getThree())
                return 0;
            else{
                int a = getThree() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("32").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("34")){
            if(quantity > getFour())
                return 0;
            else{
                int a = getFour() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("34").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("36")){
            if(quantity > getFive())
                return 0;
            else{
                int a = getFive() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("36").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("38")){
            if(quantity > getSix())
                return 0;
            else{
                int a = getSix() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("38").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("40")){
            if(quantity > getSeven())
                return 0;
            else{
                int a = getSeven() -  quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("40").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("42")){
            if(quantity > getEight())
                return 0;
            else{
                int a = getEight() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("42").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("44")){
            if(quantity > getNine())
                return 0;
            else{
                int a = getNine() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("44").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("1 yr")){
            if(quantity > getOneyr())
                return 0;
            else{
                int a = getOneyr() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("1 yr").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("2 yr")){
            if(quantity > getTwoyr())
                return 0;
            else{
                int a = getTwoyr() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("2 yr").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("3 yr")){
            if(quantity > getThreeyr())
                return 0;
            else{
                int a = getThreeyr() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("3 yr").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("4 yr")){
            if(quantity > getFouryr())
                return 0;
            else{
                int a = getFouryr() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("4 yr").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("5 yr")){
            if(quantity > getFiveyr())
                return 0;
            else{
                int a = getFiveyr() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("5 yr").setValue(Integer.toString(a));
                return 1;
            }
        }
        else if(size.equals("6 yard")){
            if(quantity > getSixYard())
                return 0;
            else{
                int a = getSixYard() - quantity;
                databaseReference.child("Product").child(findProductId).child("Sizes").child("6 yard").setValue(Integer.toString(a));
                return 1;
            }
        }
        return 1;
    }



    private void makeAnOrder() {
        MakeOrder makeOrder = new MakeOrder(currentUser,orderKey,currentDate,findProductId,getContact(),getAddress());
        databaseReference.child("Orders").child(orderKey).setValue(makeOrder);
        databaseReference.child("Orders").child(orderKey).child("Sizes").setValue(hashMap);
        hashMap = new HashMap();
    }


    private void viewSize() {
        linear.setVisibility(View.VISIBLE);
        availablilty.setText("Available");
        databaseReference.child("Product").child(findProductId).child("Sizes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("S")){
                    String q = snapshot.child("S").getValue().toString();
                    makeVisible(1,"S",q);
                    setS(Integer.parseInt(q));
                }
                if(snapshot.hasChild("M")){
                    String q = snapshot.child("M").getValue().toString();
                    makeVisible(2,"M",q);
                    setM(Integer.parseInt(q));
                }
                if(snapshot.hasChild("L")){
                    String q = snapshot.child("L").getValue().toString();
                    makeVisible(3,"L",q);
                    setL(Integer.parseInt(q));
                }
                if(snapshot.hasChild("XL")){
                    String q = snapshot.child("XL").getValue().toString();
                    makeVisible(4,"XL",q);
                    setXl(Integer.parseInt(q));
                }
                if(snapshot.hasChild("XXL")){
                    String q = snapshot.child("XXL").getValue().toString();
                    makeVisible(5,"XXL",q);
                    setXxl(Integer.parseInt(q));
                }

                if(snapshot.hasChild("28")){
                    String q = snapshot.child("28").getValue().toString();
                    makeVisible(1,"28",q);
                    setOne(Integer.parseInt(q));
                }
                if(snapshot.hasChild("30")){
                    String q = snapshot.child("30").getValue().toString();
                    makeVisible(2,"30",q);
                    setTwo(Integer.parseInt(q));

                }
                if(snapshot.hasChild("32")){
                    String q = snapshot.child("32").getValue().toString();
                    setThree(Integer.parseInt(q));
                    makeVisible(3,"32",q);
                }
                if(snapshot.hasChild("34")){
                    String q = snapshot.child("34").getValue().toString();
                    makeVisible(4,"34",q);
                    setFour(Integer.parseInt(q));
                }
                if(snapshot.hasChild("36")){
                    String q = snapshot.child("36").getValue().toString();
                    makeVisible(5,"36",q);
                    setFive(Integer.parseInt(q));
                }
                if(snapshot.hasChild("38")){
                    String q = snapshot.child("38").getValue().toString();
                    makeVisible(6,"38",q);
                    setSix(Integer.parseInt(q));
                }
                if(snapshot.hasChild("40")){
                    String q = snapshot.child("40").getValue().toString();
                    makeVisible(7,"40",q);
                    setSeven(Integer.parseInt(q));
                }
                if(snapshot.hasChild("42")){
                    String q = snapshot.child("42").getValue().toString();
                    makeVisible(8,"42",q);
                    setEight(Integer.parseInt(q));
                }
                if(snapshot.hasChild("44")){
                    String q = snapshot.child("44").getValue().toString();
                    makeVisible(9,"44",q);
                    setNine(Integer.parseInt(q));
                }
                if(snapshot.hasChild("1 yr")){
                    String q = snapshot.child("1 yr").getValue().toString();
                    makeVisible(1,"1 yr",q);
                    setOneyr(Integer.parseInt(q));
                }
                if(snapshot.hasChild("2 yr")){
                    String q = snapshot.child("2 yr").getValue().toString();
                    makeVisible(2,"2 yr",q);
                    setTwo(Integer.parseInt(q));
                }
                if(snapshot.hasChild("3 yr")){
                    String q = snapshot.child("3 yr").getValue().toString();
                    makeVisible(3,"3 yr",q);
                    setThreeyr(Integer.parseInt(q));
                }
                if(snapshot.hasChild("4 yr")){
                    String q = snapshot.child("4 yr").getValue().toString();
                    makeVisible(4,"4 yr",q);
                    setFouryr(Integer.parseInt(q));
                }
                if(snapshot.hasChild("5 yr")){
                    String q = snapshot.child("5 yr").getValue().toString();
                    makeVisible(5,"5 yr",q);
                    setFiveyr(Integer.parseInt(q));
                }
                if(snapshot.hasChild("6 yard")){
                    String q = snapshot.child("6 yard").getValue().toString();
                    makeVisible(1,"6 yard",q);
                    setSixYard(Integer.parseInt(q));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    private void init(){
        productId = findViewById(R.id.product_details_product_id);
        productType = findViewById(R.id.product_details_type);
        price = findViewById(R.id.product_details_price);
        discountPrice = findViewById(R.id.product_details_discount_price);
        addToCartButton = findViewById(R.id.product_details_add_to_cart_Button);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        productImage = findViewById(R.id.product_details_image);
        loadingBar = new ProgressDialog(this);
         spinnerSize = findViewById(R.id.product_details_spinner_size);
        quantityEditText = findViewById(R.id.product_details_quantity_texView);
        beforeFavourite  = findViewById(R.id.image_before_favourite);
        afterFavourite = findViewById(R.id.image_favourite);
        linear = findViewById(R.id.size_quantity_quantity_layout);
        availablilty = findViewById(R.id.product_details_availability);
        horizontalLineView1 = findViewById(R.id.view1);
        horizontalLineView2 = findViewById(R.id.view2);
        okButton = findViewById(R.id.product_details_ok_Button);
        tvText = findViewById(R.id.tvText);
        doneButton = findViewById(R.id.done_for_order);
        contactEdit = findViewById(R.id.contact_for_order);
        addressEdit = findViewById(R.id.address_for_order);
        changeLocation = findViewById(R.id.change_location_textView);
        hash = new HashMap();

        firstSize = findViewById(R.id.my_cart_first_size);
        firstQuantity = findViewById(R.id.my_cart_first_size_quantity);
        secondSize = findViewById(R.id.my_cart_second_size);
        secondQuantity = findViewById(R.id.my_cart_second_size_quantity);

        thirdSize = findViewById(R.id.my_cart_third_size);
        thirdQuantity = findViewById(R.id.my_cart_third_size_quantity);

        fourthsize = findViewById(R.id.my_cart_fourth_size);
        fourthQuantity = findViewById(R.id.my_cart_fourth_size_quantity);

        fiftSize = findViewById(R.id.my_cart_fifth_size);
        fifthQuantity = findViewById(R.id.my_cart_fifth_size_quantity);

        sixthsize = findViewById(R.id.my_cart_sixth_size);
        sixthQuantiy = findViewById(R.id.my_cart_sixth_size_quantity);

        seventhSize = findViewById(R.id.my_cart_seventh_size);
        seventhQuantity = findViewById(R.id.my_cart_seventh_size_quantity);

        eightSize = findViewById(R.id.my_cart_eight_size);
        eightSizeQuantity = findViewById(R.id.my_cart_eight_size_quantity);
        nineSize = findViewById(R.id.my_cart_nine_size);
        nineSizeQuantity = findViewById(R.id.my_cart_nine_size_quantity);
    }
    private void loadData(){
        databaseReference.child("Product").child(findProductId).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String image = snapshot.child("productImageUrl").getValue().toString();
                        String Id = snapshot.child("productID").getValue().toString();
                        String thisPrice = snapshot.child("productPrice").getValue().toString();
                        String type = snapshot.child("productType").getValue().toString();
                        String react = snapshot.child("reviews").getValue().toString();
                        String shopID = snapshot.child("shopId").getValue().toString();
                        if(snapshot.hasChild("discountPrice")){
                            String dis = snapshot.child("discountPrice").getValue().toString();
                            discountPrice.setText("Current Price : "+dis + "tk");
                            discountPrice.setVisibility(View.VISIBLE);
                        }



                        setLike(react);
                        setShop(shopID);

                        productId.setText("Product ID : "+Id);
                        price.setText("Price : "+thisPrice);
                        productType.setText("Product Type : "+type);
                        Picasso.get().load(image).into(productImage);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void sendProducttoCart() {
        databaseReference.child("Users").child(currentUser).child("MyCart").child(orderKey).child("isHere").setValue("ORDERED");
        databaseReference.child("Users").child(currentUser).child("MyCart").child(findProductId).child("isHere").setValue("YES");
        loadingBar.dismiss();
        addToCartButton.setText("Added");
        Toast.makeText(getApplicationContext(),"Product added to your cart",Toast.LENGTH_SHORT).show();
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       text = parent.getItemAtPosition(position).toString();
       setSize(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getSubCat() {
        return subCat;
    }

    public void setSubCat(String subCat) {
        this.subCat = subCat;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public int getSixYard() {
        return sixYard;
    }

    public void setSixYard(int sixYard) {
        this.sixYard = sixYard;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    private void makeVisible(int i, String s, String q) {
        if(i == 1){
            firstSize.setText(s);
            firstQuantity.setText(q);
            firstSize.setVisibility(View.VISIBLE);
            firstQuantity.setVisibility(View.VISIBLE);
        }
        if(i == 2){
            secondSize.setText(s);
            secondQuantity.setText(q);
            secondSize.setVisibility(View.VISIBLE);
            secondQuantity.setVisibility(View.VISIBLE);
        }
        if(i == 3){
            thirdSize.setText(s);
            thirdQuantity.setText(q);
            thirdSize.setVisibility(View.VISIBLE);
            thirdQuantity.setVisibility(View.VISIBLE);
        }
        if(i == 4){
            fourthsize.setText(s);
            fourthQuantity.setText(q);
            fourthsize.setVisibility(View.VISIBLE);
            fourthQuantity.setVisibility(View.VISIBLE);
        }
        if(i == 5){
            fiftSize.setText(s);
            fifthQuantity.setText(q);
            fiftSize.setVisibility(View.VISIBLE);
            fifthQuantity.setVisibility(View.VISIBLE);
        }
        if(i == 6){
            sixthsize.setText(s);
            sixthQuantiy.setText(q);
            sixthsize.setVisibility(View.VISIBLE);
            sixthQuantiy.setVisibility(View.VISIBLE);
        }
        if(i == 7){
            seventhSize.setText(s);
            seventhQuantity.setText(q);
            seventhSize.setVisibility(View.VISIBLE);
            seventhQuantity.setVisibility(View.VISIBLE);
        }
        if(i == 8){
            eightSize.setText(s);
            eightSizeQuantity.setText(q);
            eightSize.setVisibility(View.VISIBLE);
            eightSizeQuantity.setVisibility(View.VISIBLE);
        }
        if(i == 9){
            nineSize.setText(s);
            nineSizeQuantity.setText(q);
            nineSize.setVisibility(View.VISIBLE);
            nineSizeQuantity.setVisibility(View.VISIBLE);
        }
    }
}
