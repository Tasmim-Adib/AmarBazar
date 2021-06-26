package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    HomePageAdapter adapter, adapter2, adapter3;

    ArrayList<Upload> featuredLocations;
    ArrayList<Upload> featuredLocations2;
    ArrayList<Upload> featuredLocations3;

    DatabaseReference databaseReference,idRef;

    private TextView profileName;
    private CircleImageView profileImage;
    FirebaseAuth auth;
    String shopId;
    private View view;

    HomePageAdapter.OnNoteListenerHome onNoteListenerHome, onNoteListenerHome2, onNoteListenerHome3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);

        toolbar = findViewById(R.id.toolbar_home);
        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.nav_view_home);

        recyclerView = findViewById(R.id.featured_recycler_home);
        recyclerView2 = findViewById(R.id.featured_recycler2_home);
        recyclerView3 = findViewById(R.id.featured_recycler3_home);
        idRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Product");
        auth = FirebaseAuth.getInstance();
        shopId = auth.getCurrentUser().getUid();


        featuredRecycler();
        featuredRecycler2();
        featuredRecycler3();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view = navigationView.getHeaderView(0);
        profileImage = view.findViewById(R.id.nav_profile_image);
        profileName = view.findViewById(R.id.nav_name);
        idRef.child("Users").child(shopId).child("Identity")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();

                        profileName.setText(name);
                        Picasso.get().load(image).into(profileImage);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_shop_home);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void featuredRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Upload upload = dataSnapshot.getValue(Upload.class);
                    if (upload.getCategory().equals("Women's Fashion") && upload.getShopId().equals(shopId)) {
                        featuredLocations.add(upload);
                    }
                }
                setOnClickListener();
                adapter = new HomePageAdapter(featuredLocations, ShopHome.this, onNoteListenerHome);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void featuredRecycler2() {
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations2 = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Upload upload = dataSnapshot.getValue(Upload.class);
                    if (upload.getCategory().equals("Men's Fashion") && upload.getShopId().equals(shopId)) {
                        featuredLocations2.add(upload);
                    }
                }
                setOnClickListener();
                adapter2 = new HomePageAdapter(featuredLocations2, ShopHome.this, onNoteListenerHome2);
                recyclerView2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void featuredRecycler3() {
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredLocations3 = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Upload upload = dataSnapshot.getValue(Upload.class);
                    if (upload.getCategory().equals("Kid's Fashion") && upload.getShopId().equals(shopId)) {
                        featuredLocations3.add(upload);
                    }
                }
                setOnClickListener();
                adapter3 = new HomePageAdapter(featuredLocations3, ShopHome.this, onNoteListenerHome3);
                recyclerView3.setAdapter(adapter3);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setOnClickListener() {
        onNoteListenerHome = new HomePageAdapter.OnNoteListenerHome() {
            @Override
            public void onNoteClick(int position) {
                Log.i("ProductID", featuredLocations.get(position).getFirebaseId());
                Intent intent = new Intent(ShopHome.this, ShopViewProduct.class);
                intent.putExtra("PRODUCT_ID", featuredLocations.get(position).getFirebaseId());
                startActivity(intent);
            }
        };
        onNoteListenerHome2 = new HomePageAdapter.OnNoteListenerHome() {
            @Override
            public void onNoteClick(int position) {
                Log.i("ProductID", featuredLocations2.get(position).getFirebaseId());
                Intent intent = new Intent (ShopHome.this, ShopViewProduct.class);
                intent.putExtra("PRODUCT_ID",featuredLocations2.get(position).getFirebaseId());
                startActivity(intent);
            }
        };

        onNoteListenerHome3 = new HomePageAdapter.OnNoteListenerHome() {
            @Override
            public void onNoteClick(int position) {
                Log.i("ProductID", featuredLocations3.get(position).getFirebaseId());
                Intent intent = new Intent (ShopHome.this, ShopViewProduct.class);
                intent.putExtra("PRODUCT_ID",featuredLocations3.get(position).getFirebaseId());
                startActivity(intent);
            }
        };
    }


        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
            switch (item.getItemId()) {
                case R.id.nav_shop_home:
                    break;
                case R.id.nav_shop_categories:
                    Intent intent = new Intent(ShopHome.this, ShopCategory.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.nav_shop_orders:
                    Intent orderIntent = new Intent(ShopHome.this,ShopOrder.class);
                    startActivity(orderIntent);
                    finish();
                    break;
                case R.id.nav_shop_add_product:
                    Intent intentAdd = new Intent(ShopHome.this, AddProductActivity.class);
                    startActivity(intentAdd);
                    break;
                case R.id.nav_shop_logout:
                    // auth.signOut();
                    startActivity(new Intent(ShopHome.this, MainActivity.class));
                    finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
    }

