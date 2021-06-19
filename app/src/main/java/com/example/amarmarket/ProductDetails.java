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

public class ProductDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView productId,productType,price,discountPrice;
    private ImageView productImage,beforeFavourite,afterFavourite;
    private String findProductId;
    private Button addToCartButton;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUser,like,state;
    private ProgressDialog loadingBar;
    private Spinner spinnerSize,spinnerPantSize;
    private EditText quantityEditText;
    private ListView listView;
    private LinearLayout linearLayout;
    private TextView s,m,l,xl,xxl,xxxl,availablilty;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        state = "new";
        findProductId = getIntent().getExtras().get("visit_product").toString();
        init();
        loadData();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Sizes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(adapter);
        spinnerSize.setOnItemSelectedListener(this);


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
                                    Toast.makeText(getApplicationContext(),"Product Added to Favourites",Toast.LENGTH_SHORT).show();
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
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("new")){
                    loadingBar.setTitle("Add to Cart");
                    loadingBar.setMessage("Product is Adding to Your Cart");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                    sendProducttoCart();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Product is already added to your cart",Toast.LENGTH_SHORT).show();
                }

            }
        });
        availablilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewSize();
            }
        });


    }

    private void viewSize() {
        linearLayout.setVisibility(View.VISIBLE);
        availablilty.setVisibility(View.GONE);
        databaseReference.child("Product").child(findProductId).child("Sizes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("S")){
                    String q = snapshot.child("S").getValue().toString();
                    s.setText(q);
                }
                if(snapshot.hasChild("M")){
                    String q = snapshot.child("M").getValue().toString();
                    m.setText(q);
                }
                if(snapshot.hasChild("L")){
                    String q = snapshot.child("L").getValue().toString();
                    l.setText(q);
                }
                if(snapshot.hasChild("XL")){
                    String q = snapshot.child("XL").getValue().toString();
                    xl.setText(q);
                }
                if(snapshot.hasChild("XXL")){
                    String q = snapshot.child("XXL").getValue().toString();
                    xxl.setText(q);
                }
                if(snapshot.hasChild("XXXL")){
                    String q = snapshot.child("XXXL").getValue().toString();
                    xxxl.setText(q);
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
        spinnerPantSize = findViewById(R.id.product_details_spinner_pant);
        spinnerSize = findViewById(R.id.product_details_spinner_size);
        quantityEditText = findViewById(R.id.product_details_quantity_texView);
        beforeFavourite  = findViewById(R.id.image_before_favourite);
        afterFavourite = findViewById(R.id.image_favourite);
        linearLayout = findViewById(R.id.main_linear_layout);
        s = findViewById(R.id.s_size_quantity);
        m = findViewById(R.id.m_size_quantity);
        l = findViewById(R.id.l_size_quantity);
        xl = findViewById(R.id.xl_size_quantity);
        xxl = findViewById(R.id.xxl_size_quantity);
        xxxl = findViewById(R.id.xxxl_size_quantity);
        availablilty = findViewById(R.id.product_details_availability);
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

                        setLike(react);

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
        databaseReference.child("Users").child(currentUser).child("MyCart").child(findProductId).child("isHere").setValue("YES");
        loadingBar.dismiss();
        Toast.makeText(getApplicationContext(),"Product added to your cart",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
