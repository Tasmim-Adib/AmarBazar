package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetails extends AppCompatActivity {

    private TextView productId,productType,price,discountPrice;
    private ImageView productImage;
    private String findProductId;
    private Button addToCartButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        findProductId = getIntent().getExtras().get("visit_product").toString();
        init();
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),findProductId,Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void init(){
        productId = findViewById(R.id.product_details_product_id);
        productType = findViewById(R.id.product_details_type);
        price = findViewById(R.id.product_details_price);
        discountPrice = findViewById(R.id.product_details_discount_price);
        addToCartButton = findViewById(R.id.product_details_add_to_cart_Button);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        productImage = findViewById(R.id.product_details_image);
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
}
