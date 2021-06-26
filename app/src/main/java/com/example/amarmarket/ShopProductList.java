package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShopProductList extends AppCompatActivity {
    private String cat,subCat,currentUser,shopName;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,productRef;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_product_list);
        cat = getIntent().getExtras().get("Category").toString();
        subCat = getIntent().getExtras().get("Sub Category").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("My_Product").child(subCat);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_product_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        productRef = FirebaseDatabase.getInstance().getReference();
        loadData();

    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    private void loadData(){

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                        .setQuery(databaseReference,Upload.class)
                        .build();
        FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FragmentHome.ProductViewHolder productViewHolder, int i, @NonNull Upload upload) {
                        final String productID = getRef(i).getKey();
                        productRef.child("Product").child(productID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String id = snapshot.child("productID").getValue().toString();
                                String type = snapshot.child("productType").getValue().toString();
                                String price = snapshot.child("productPrice").getValue().toString();
                                String reviews = snapshot.child("reviews").getValue().toString();
                                String orders = snapshot.child("ordersCount").getValue().toString();
                                String shopId = snapshot.child("shopId").getValue().toString();
                                String image = snapshot.child("productImageUrl").getValue().toString();

                                productViewHolder.productId.setText("Product ID : "+id);
                                productViewHolder.productType.setText("Product Type : "+type);
                                productViewHolder.productPrice.setText("Price : "+price);
                                productViewHolder.thumbsUp.setText(reviews);
                                productViewHolder.order.setText(orders);
                                Picasso.get().load(image).into(productViewHolder.productImage);

                                productRef.child("Users").child(shopId).child("Identity").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String shopImage = snapshot.child("image").getValue().toString();
                                        String shopname = snapshot.child("name").getValue().toString();
                                        Picasso.get().load(shopImage).into(productViewHolder.shopImage);
                                        productViewHolder.shopName.setText(shopname);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getApplicationContext(),ShopViewProduct.class);
                                        intent.putExtra("PRODUCT_ID",productID);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public FragmentHome.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
                        FragmentHome.ProductViewHolder viewHolder = new FragmentHome.ProductViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
