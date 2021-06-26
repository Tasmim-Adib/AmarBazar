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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CategorizedProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView,subRecyclerView;
    private DatabaseReference databaseReference,shopLogoRef,subCategoryRef;
    private Query query;
    private AutoCompleteTextView subCategoryTextView;
    private ImageView searchImage;
    private Spinner priceFilterSpinner;
    private String priceRange,item;
    private ArrayAdapter<String> subCategoryAdapter;

    public String[] manFashion = {"Pant","Punjabi","T-shirt","Shirt","Hoody"};
    public String[] womanFashion = {"Saree","Shalwar Kameez","Borkha","Hijab","T-shirt","Kurta"};
    public String[] kidFashion = {"Pant","Panjabi","T-shirt","Frock","Shoe"};
    public String[] gift = {"Birthday","Wedding"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized_product);
        item = getIntent().getExtras().get("Fashion").toString();
        recyclerView = findViewById(R.id.categorized_product_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        shopLogoRef = FirebaseDatabase.getInstance().getReference().child("Users");
        subRecyclerView = findViewById(R.id.sub_categorized_product_recycler);
        subRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        subCategoryTextView = findViewById(R.id.sub_category_textView);
        searchImage = findViewById(R.id.search_image_btn);
        priceFilterSpinner = findViewById(R.id.price_fileter_spinner);
        query = FirebaseDatabase.getInstance().getReference("Product").orderByChild("category").equalTo(item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.priceFilter,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceFilterSpinner.setAdapter(adapter);
        priceFilterSpinner.setOnItemSelectedListener(this);
        loadItem();

        if(item.equals("Men's Fashion")){
            subCategoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,manFashion);
        }
        if(item.equals("Women's Fashion")){
            subCategoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,womanFashion);

        }
        if (item.equals("Kid's Fashion")){
            subCategoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,kidFashion);

        }

        if (item.equals("Gift_item")){
            subCategoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,gift);

        }


        subCategoryTextView.setThreshold(1);
        subCategoryTextView.setAdapter(subCategoryAdapter);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sub_cat = subCategoryTextView.getText().toString();
                subCategoryRef = FirebaseDatabase.getInstance().getReference("Category").child(item).child(sub_cat);
                recyclerView.setVisibility(View.GONE);
                subRecyclerView.setVisibility(View.VISIBLE);
                loadSubItem();
            }
        });
    }

    private void loadSubItem() {


        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                        .setQuery(subCategoryRef,Upload.class)
                        .build();

        FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FragmentHome.ProductViewHolder productViewHolder, int i, @NonNull Upload upload) {

                final String productId = getRef(i).getKey();
                databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String productid = snapshot.child("productID").getValue().toString();
                        String productType = snapshot.child("productType").getValue().toString();
                        String productPrice = snapshot.child("productPrice").getValue().toString();
                        String productImage = snapshot.child("productImageUrl").getValue().toString();
                        String likes = snapshot.child("reviews").getValue().toString();
                        String orderCount = snapshot.child("ordersCount").getValue().toString();
                        String shopId = snapshot.child("shopId").getValue().toString();


                        productViewHolder.productType.setText("Product Type : " +productType);
                        productViewHolder.productPrice.setText("Price : "+productPrice);
                        productViewHolder.productId.setText("Product ID : "+productid);
                        productViewHolder.thumbsUp.setText(likes);
                        productViewHolder.order.setText(orderCount);
                        Picasso.get().load(productImage).into(productViewHolder.productImage);
                        shopLogoRef.child(shopId).child("Identity").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String shopI = snapshot.child("image").getValue().toString();
                                String shopN = snapshot.child("name").getValue().toString();
                                String Address = snapshot.child("address").getValue().toString();
                                String Contact = snapshot.child("contact").getValue().toString();

                                productViewHolder.shopAdd.setText(Address);
                                productViewHolder.shopName.setText(shopN);
                                productViewHolder.shopContact.setText("Contact : " + Contact);
                                Picasso.get().load(shopI).into(productViewHolder.shopImage);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(),ProductDetails.class);
                                intent.putExtra("visit_product",productId);
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

        subRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void loadItem() {


        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                        .setQuery(query,Upload.class)
                        .build();

        FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder> adapter
                = new FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final FragmentHome.ProductViewHolder productViewHolder, int i, @NonNull Upload upload) {

                final String productId = getRef(i).getKey();
                databaseReference.child(productId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String productid = snapshot.child("productID").getValue().toString();
                        String productType = snapshot.child("productType").getValue().toString();
                        String productPrice = snapshot.child("productPrice").getValue().toString();
                        String productImage = snapshot.child("productImageUrl").getValue().toString();
                        String likes = snapshot.child("reviews").getValue().toString();
                        String orderCount = snapshot.child("ordersCount").getValue().toString();
                        String shopId = snapshot.child("shopId").getValue().toString();


                        productViewHolder.productType.setText("Product Type : " +productType);
                        productViewHolder.productPrice.setText("Price : "+productPrice);
                        productViewHolder.productId.setText("Product ID : "+productid);
                        productViewHolder.thumbsUp.setText(likes);
                        productViewHolder.order.setText(orderCount);
                        Picasso.get().load(productImage).into(productViewHolder.productImage);
                        shopLogoRef.child(shopId).child("Identity").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String shopI = snapshot.child("image").getValue().toString();
                                String shopN = snapshot.child("name").getValue().toString();
                                String Address = snapshot.child("address").getValue().toString();
                                String Contact = snapshot.child("contact").getValue().toString();

                                productViewHolder.shopAdd.setText(Address);
                                productViewHolder.shopName.setText(shopN);
                                productViewHolder.shopContact.setText("Contact : " + Contact);
                                Picasso.get().load(shopI).into(productViewHolder.shopImage);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(),ProductDetails.class);
                                intent.putExtra("visit_product",productId);
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

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text =parent.getItemAtPosition(position).toString();
        setPriceRange(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
