package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShopOrderedProduct extends AppCompatActivity {
    private ImageView productImage;
    private TextView productId,customerName,productPrice,contactEdit,addressEdit,firstSize,secondSize,thirdSize,
    fourthSize,fifthSize,sixthSize,seventhSize,eightSize,nineSize,quantityFirst,quantitySecond,quantityThird,quantityFourth,
    quantityFifth,quantitySix,quantitySeven,quantityeight,quantityNine;
    private Button accept,delivered;
    private DatabaseReference databaseReference,productRef,customerRef,sizeRef;
    private String currentUser,orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_ordered_product);
        initialize();
        orderID = getIntent().getExtras().get("ORDER_ID").toString();
        loadData();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Orders").child(orderID).child("state").setValue("accepted");
            }
        });
        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Orders").child(orderID).child("state").setValue("delivered");
            }
        });
    }

    private void loadData() {
        databaseReference.child("Orders").child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String customerId = snapshot.child("customerId").getValue().toString();
                final String pId = snapshot.child("productId").getValue().toString();
                String contact = snapshot.child("contact").getValue().toString();
                String address = snapshot.child("address").getValue().toString();

                contactEdit.setText("Contact No : "+contact);
                addressEdit.setText("Address : "+address);
                customerRef.child("Users").child(customerId).child("Identity").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String Name = snapshot.child("name").getValue().toString();
                        customerName.setText("Customer : "+Name);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                productRef.child("Product").child(pId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String image = snapshot.child("productImageUrl").getValue().toString();
                        String id = snapshot.child("productID").getValue().toString();

                        productId.setText("Id : "+id);
                        Picasso.get().load(image).into(productImage);
                        if(snapshot.hasChild("discountPrice")){
                            String price = snapshot.child("discountPrice").getValue().toString();
                            productPrice.setText("Price : "+price);
                        }
                        else{
                            String price = snapshot.child("productPrice").getValue().toString();
                            productPrice.setText("Price : "+price);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Orders").child(orderID).child("Sizes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("S")){
                    String q = snapshot.child("S").getValue().toString();
                    makeVisible(1,"S",q);
                }
                if(snapshot.hasChild("M")){
                    String q = snapshot.child("M").getValue().toString();
                    makeVisible(2,"M",q);
                }
                if(snapshot.hasChild("L")){
                    String q = snapshot.child("L").getValue().toString();
                    makeVisible(3,"L",q);
                }
                if(snapshot.hasChild("XL")){
                    String q = snapshot.child("XL").getValue().toString();
                    makeVisible(4,"XL",q);
                }
                if(snapshot.hasChild("XXL")){
                    String q = snapshot.child("XXL").getValue().toString();
                    makeVisible(5,"XXL",q);
                }
                if(snapshot.hasChild("XXXL")){
                    String q = snapshot.child("XXXL").getValue().toString();
                    makeVisible(6,"XXXL",q);
                }
                if(snapshot.hasChild("28")){
                    String q = snapshot.child("28").getValue().toString();
                    makeVisible(1,"28",q);
                }
                if(snapshot.hasChild("30")){
                    String q = snapshot.child("30").getValue().toString();
                    makeVisible(2,"30",q);
                }
                if(snapshot.hasChild("32")){
                    String q = snapshot.child("32").getValue().toString();
                    makeVisible(3,"32",q);
                }
                if(snapshot.hasChild("34")){
                    String q = snapshot.child("34").getValue().toString();
                    makeVisible(4,"34",q);
                }
                if(snapshot.hasChild("36")){
                    String q = snapshot.child("36").getValue().toString();
                    makeVisible(5,"36",q);
                }
                if(snapshot.hasChild("38")){
                    String q = snapshot.child("38").getValue().toString();
                    makeVisible(6,"38",q);
                }
                if(snapshot.hasChild("40")){
                    String q = snapshot.child("40").getValue().toString();
                    makeVisible(7,"40",q);
                }
                if(snapshot.hasChild("42")){
                    String q = snapshot.child("42").getValue().toString();
                    makeVisible(8,"42",q);
                }
                if(snapshot.hasChild("44")){
                    String q = snapshot.child("44").getValue().toString();
                    makeVisible(9,"44",q);
                }
                if(snapshot.hasChild("1 yr")){
                    String q = snapshot.child("1 yr").getValue().toString();
                    makeVisible(1,"1 yr",q);
                }
                if(snapshot.hasChild("2 yr")){
                    String q = snapshot.child("2 yr").getValue().toString();
                    makeVisible(2,"2 yr",q);
                }
                if(snapshot.hasChild("3 yr")){
                    String q = snapshot.child("3 yr").getValue().toString();
                    makeVisible(3,"3 yr",q);
                }
                if(snapshot.hasChild("4 yr")){
                    String q = snapshot.child("4 yr").getValue().toString();
                    makeVisible(4,"4 yr",q);
                }
                if(snapshot.hasChild("5 yr")){
                    String q = snapshot.child("5 yr").getValue().toString();
                    makeVisible(5,"5 yr",q);
                }
                if(snapshot.hasChild("6 yard")){
                    String q = snapshot.child("6 yard").getValue().toString();
                    makeVisible(1,"6 yard",q);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void makeVisible(int i, String s, String q) {
        if(i == 1){
            firstSize.setText(s);
            quantityFirst.setText(q);
            firstSize.setVisibility(View.VISIBLE);
            quantityFirst.setVisibility(View.VISIBLE);
        }
        if(i == 2){
            secondSize.setText(s);
            quantitySecond.setText(q);
            secondSize.setVisibility(View.VISIBLE);
            quantitySecond.setVisibility(View.VISIBLE);
        }
        if(i == 3){
            thirdSize.setText(s);
            quantityThird.setText(q);
            thirdSize.setVisibility(View.VISIBLE);
            quantityThird.setVisibility(View.VISIBLE);
        }
        if(i == 4){
            fourthSize.setText(s);
            quantityFourth.setText(q);
            fourthSize.setVisibility(View.VISIBLE);
            quantityFourth.setVisibility(View.VISIBLE);
        }
        if(i == 5){
            fifthSize.setText(s);
            quantityFifth.setText(q);
            fifthSize.setVisibility(View.VISIBLE);
            quantityFifth.setVisibility(View.VISIBLE);
        }
        if(i == 6){
            sixthSize.setText(s);
            quantitySix.setText(q);
            sixthSize.setVisibility(View.VISIBLE);
            quantitySix.setVisibility(View.VISIBLE);
        }
        if(i == 7){
            seventhSize.setText(s);
            quantitySeven.setText(q);
            seventhSize.setVisibility(View.VISIBLE);
            quantitySeven.setVisibility(View.VISIBLE);
        }
        if(i == 8){
            eightSize.setText(s);
            quantityeight.setText(q);
            eightSize.setVisibility(View.VISIBLE);
            quantityeight.setVisibility(View.VISIBLE);
        }
        if(i == 9){
            nineSize.setText(s);
            quantityNine.setText(q);
            nineSize.setVisibility(View.VISIBLE);
            quantityNine.setVisibility(View.VISIBLE);
        }
    }

    private void initialize(){
        productImage = findViewById(R.id.shop_ordered_product_image);
        productId = findViewById(R.id.shop_ordered_product_Id);
        productPrice = findViewById(R.id.shop_ordered_product_price);
        customerName = findViewById(R.id.shop_ordered_product_type);
        contactEdit = findViewById(R.id.shop_ordered_product_contact);
        addressEdit = findViewById(R.id.shop_ordered_product_Address);
        firstSize = findViewById(R.id.shop_order_first_sizse);
        secondSize = findViewById(R.id.shop_order_second_size);
        thirdSize = findViewById(R.id.shop_order_third_size);
        fourthSize = findViewById(R.id.shop_order_fourth_sizse);
        fifthSize = findViewById(R.id.shop_order_fifth_sizse);
        sixthSize = findViewById(R.id.shop_order_sixth_size);
        seventhSize = findViewById(R.id.shop_order_seventh_size);
        eightSize = findViewById(R.id.shop_order_eight_size);
        nineSize = findViewById(R.id.shop_order_nine_size);
        quantityFirst = findViewById(R.id.shop_ordered_product_first_size_quantity);
        quantitySecond = findViewById(R.id.shop_ordered_product_second_size_quantity);
        quantityThird = findViewById(R.id.shop_ordered_product_third_size_quantity);
        quantityFourth = findViewById(R.id.shop_ordered_product_fourth_size_quantity);
        quantityFifth = findViewById(R.id.shop_ordered_product_fifth_size_quantity);
        quantitySix = findViewById(R.id.shop_ordered_product_sixth_size_quantity);
        quantitySeven = findViewById(R.id.shop_ordered_product_seventh_size_quantity);
        quantityeight = findViewById(R.id.shop_ordered_product_eight_size_quantity);
        quantityNine = findViewById(R.id.shop_ordered_product_nine_size_quantity);
        accept = findViewById(R.id.shop_ordered_product_accept_btn);
        delivered = findViewById(R.id.shop_ordered_product_deliver_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        productRef = FirebaseDatabase.getInstance().getReference();
        customerRef = FirebaseDatabase.getInstance().getReference();
        sizeRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


    }
}
