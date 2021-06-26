package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OrderedProductDetails extends AppCompatActivity {

    private LinearLayout sLayout,mLayout,lLayout,xlLayout,xxlLayout,xxxlLayout;
    private ImageView image;
    private TextView id,type,price,firstSize,firstquantity,secondsize,secondquantity,thirdSize,thirdQuantity,
            fourthsizs,fourthQuatity,fifthSize,fifthQuantity,sixSize,sixQuantity,contact,address,sevenSize,sevenQuantity
            ,eightSize,eightQuantity,nineSize,nineQuantity;
    private Button confirm,cancel;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,productRef,cartRef;
    private String currentUser,orderId,shopId,product;
    private int countOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_product_details);

        init();
        orderId = getIntent().getExtras().get("visit_order").toString();
        loadValue();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Users").child(getShopId()).child("Orders").child(orderId).child("isHere").setValue("YES");
                databaseReference.child("Users").child(currentUser).child("Orders").child(orderId).child("isHere").setValue("YES");
                Toast.makeText(getApplicationContext(),"Order is Placed Successfully",Toast.LENGTH_SHORT).show();
                int a = getCountOrder();
                a = a + 1;
                databaseReference.child("Product").child(getProductId()).child("ordersCount").setValue(Integer.toString(a));

                removeFromCart(orderId);


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromCart(orderId);
            }
        });



    }

    private void removeFromCart(String orderId) {
        cartRef.child(orderId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });

        cartRef.child(getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });

    }

    public String getProductId() {
        return product;
    }

    public void setProductId(String productId) {
        this.product = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getCountOrder() {
        return countOrder;
    }

    public void setCountOrder(int countOrder) {
        this.countOrder = countOrder;
    }

    private void init(){

        firstSize = findViewById(R.id.ordered_product_s_size);
        firstquantity = findViewById(R.id.ordered_product_s_size_quantity);
        secondsize = findViewById(R.id.ordered_product_m_size);
        secondquantity = findViewById(R.id.ordered_product_m_size_quantity);
        thirdSize = findViewById(R.id.ordered_product_l_size);
        thirdQuantity = findViewById(R.id.ordered_product_l_size_quantity);
        fourthsizs = findViewById(R.id.ordered_product_xl_size);
        fourthQuatity = findViewById(R.id.ordered_product_xl_size_quantity);
        fifthQuantity = findViewById(R.id.ordered_product_xxl_size_quantity);
        fifthSize = findViewById(R.id.ordered_product_xxl_size);
        sixSize = findViewById(R.id.ordered_product_xxxl_size);
        sixQuantity = findViewById(R.id.ordered_product_xxxl_size_quantity);
        sevenSize = findViewById(R.id.ordered_product_seven_size);
        sevenQuantity = findViewById(R.id.ordered_product_seven_size_quantity);
        eightSize = findViewById(R.id.ordered_product_eight_size);
        eightQuantity = findViewById(R.id.ordered_product_eight_size_quantity);
        nineSize = findViewById(R.id.ordered_product_nine_size);
        nineQuantity = findViewById(R.id.ordered_product_nine_size_quantity);

        image = findViewById(R.id.ordered_product_image);
        id = findViewById(R.id.ordered_product_Id);
        type = findViewById(R.id.ordered_product_type);
        price = findViewById(R.id.ordered_product_price);

        confirm = findViewById(R.id.ordered_product_confirm_btn);
        cancel = findViewById(R.id.ordered_product_cancel_btn);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        productRef = FirebaseDatabase.getInstance().getReference();
        contact = findViewById(R.id.ordered_product_contact);
        address = findViewById(R.id.ordered_product_Address);
        cartRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("MyCart");


    }

    private void loadValue(){

        databaseReference.child("Orders").child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cont = snapshot.child("contact").getValue().toString();
                String add = snapshot.child("address").getValue().toString();

                contact.setText("Contact No : "+cont);
                address.setText("Address : "+add);
                String productId = snapshot.child("productId").getValue().toString();
                setProductId(productId);
                productRef.child("Product").child(productId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String pId = snapshot.child("productID").getValue().toString();
                        String pImage = snapshot.child("productImageUrl").getValue().toString();
                        String pPrice = snapshot.child("productPrice").getValue().toString();
                        String pType = snapshot.child("productType").getValue().toString();
                        String pOrdersCount = snapshot.child("ordersCount").getValue().toString();
                        String s = snapshot.child("shopId").getValue().toString();
                        setShopId(s);
                        setCountOrder(Integer.parseInt(pOrdersCount)); //total order

                        id.setText("Product ID : " + pId);
                        type.setText("Product Type : "+pType);
                        price.setText("Price : "+pPrice);
                        Picasso.get().load(pImage).into(image);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                databaseReference.child("Orders").child(orderId).child("Sizes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.hasChild("S")){
                            String q = snapshot.child("S").getValue().toString();
                            Visible(1,"S",q);
                        }
                        if(snapshot.hasChild("M")){
                            String q = snapshot.child("M").getValue().toString();
                            Visible(2,"M",q);
                        }
                        if(snapshot.hasChild("L")){
                            String q = snapshot.child("L").getValue().toString();
                            Visible(3,"L",q);
                        }
                        if(snapshot.hasChild("XL")){
                            String q = snapshot.child("XL").getValue().toString();
                            Visible(4,"XL",q);
                        }
                        if(snapshot.hasChild("XXL")){
                            String q = snapshot.child("XXL").getValue().toString();
                            Visible(5,"XXL",q);
                        }
                        if(snapshot.hasChild("28")){
                            String q = snapshot.child("28").getValue().toString();
                            Visible(1,"28",q);
                        }

                        if(snapshot.hasChild("30")){
                            String q = snapshot.child("30").getValue().toString();
                            Visible(2,"30",q);
                        }

                        if(snapshot.hasChild("32")){
                            String q = snapshot.child("32").getValue().toString();
                            Visible(3,"32",q);
                        }

                        if(snapshot.hasChild("34")){
                            String q = snapshot.child("34").getValue().toString();
                            Visible(4,"34",q);
                        }
                        if(snapshot.hasChild("36")){
                            String q = snapshot.child("36").getValue().toString();
                            Visible(5,"36",q);
                        }

                        if(snapshot.hasChild("38")){
                            String q = snapshot.child("38").getValue().toString();
                            Visible(6,"38",q);
                        }

                        if(snapshot.hasChild("40")){
                            String q = snapshot.child("40").getValue().toString();
                            Visible(7,"40",q);
                        }

                        if(snapshot.hasChild("42")){
                            String q = snapshot.child("42").getValue().toString();
                            Visible(8,"42",q);
                        }

                        if(snapshot.hasChild("44")){
                            String q = snapshot.child("44").getValue().toString();
                            Visible(9,"44",q);
                        }
                        if(snapshot.hasChild("1 yr")){
                            String q = snapshot.child("1 yr").getValue().toString();
                            Visible(1,"1 yr",q);
                        }

                        if(snapshot.hasChild("2 yr")){
                            String q = snapshot.child("2 yr").getValue().toString();
                            Visible(2,"2 yr",q);
                        }
                        if(snapshot.hasChild("3 yr")){
                            String q = snapshot.child("3 yr").getValue().toString();
                            Visible(3,"3 yr",q);
                        }
                        if(snapshot.hasChild("4 yr")){
                            String q = snapshot.child("4 yr").getValue().toString();
                            Visible(4,"4 yr",q);
                        }
                        if(snapshot.hasChild("5 yr")){
                            String q = snapshot.child("5 yr").getValue().toString();
                            Visible(5,"5 yr",q);
                        }


                        if(snapshot.hasChild("6 yard")){
                            String q = snapshot.child("6 yard").getValue().toString();
                            Visible(1,"6 yard",q);
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

    }

    private void Visible(int a, String size, String quantity){
        if(a == 1){
            firstSize.setVisibility(View.VISIBLE);
            firstquantity.setVisibility(View.VISIBLE);
            firstSize.setText(size);
            firstquantity.setText(quantity);
        }

        if(a == 2){
            secondsize.setVisibility(View.VISIBLE);
            secondquantity.setVisibility(View.VISIBLE);
            secondsize.setText(size);
            secondquantity.setText(quantity);
        }
        if(a == 3){
            thirdSize.setVisibility(View.VISIBLE);
            thirdQuantity.setVisibility(View.VISIBLE);
            thirdSize.setText(size);
            thirdQuantity.setText(quantity);
        }
        if(a == 4){
            fourthsizs.setVisibility(View.VISIBLE);
            fourthQuatity.setVisibility(View.VISIBLE);
            fourthsizs.setText(size);
            fourthQuatity.setText(quantity);
        }
        if(a == 6){
            sixSize.setVisibility(View.VISIBLE);
            sixQuantity.setVisibility(View.VISIBLE);
            sixSize.setText(size);
            sixQuantity.setText(quantity);
        }
        if(a == 5){
            fifthSize.setVisibility(View.VISIBLE);
            fifthQuantity.setVisibility(View.VISIBLE);
            fifthSize.setText(size);
            fifthQuantity.setText(quantity);
        }
        if(a == 7){
            sevenSize.setVisibility(View.VISIBLE);
            sevenQuantity.setVisibility(View.VISIBLE);
            sevenSize.setText(size);
            sevenQuantity.setText(quantity);
        }
        if(a == 8){
            eightSize.setVisibility(View.VISIBLE);
            eightQuantity.setVisibility(View.VISIBLE);
            eightSize.setText(size);
            eightQuantity.setText(quantity);
        }
        if(a == 9){
            nineSize.setVisibility(View.VISIBLE);
            nineQuantity.setVisibility(View.VISIBLE);
            nineSize.setText(size);
            nineQuantity.setText(quantity);
        }

    }
}
