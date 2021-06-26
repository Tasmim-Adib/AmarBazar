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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopCategory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView profileName;
    private CircleImageView profileImage;
    private RecyclerView recyclerView,recyclerView2,recyclerView3;
    private RecyclerView.Adapter adapter;
    ArrayList<FeaturedHelperClass> featuredLocations;
    ArrayList<FeaturedHelperClass> featuredLocations2;
    ArrayList<FeaturedHelperClass> featuredLocations3;
    private View view;

    FeaturedAdapter.OnNoteListener onNoteListener, onNoteListener2, onNoteListener3;
    private DatabaseReference idRef;
    private FirebaseAuth auth;
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_category);
        toolbar = findViewById(R.id.toolbar_category);
        drawerLayout = findViewById(R.id.drawer_layout_cat);
        navigationView = findViewById(R.id.nav_view_category);

        recyclerView = findViewById(R.id.featured_recycler_category);
        recyclerView2 = findViewById(R.id.featured_recycler2_category);
        recyclerView3 = findViewById(R.id.featured_recycler3_category);
        idRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser().getUid();
        featuredRecycler();
        featuredRecycler2();
        featuredRecycler3();


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Category");
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view = navigationView.getHeaderView(0);
        profileImage = view.findViewById(R.id.nav_profile_image);
        profileName = view.findViewById(R.id.nav_name);
        idRef.child("Users").child(currentUser).child("Identity")
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

        navigationView.setCheckedItem(R.id.nav_shop_categories);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void featuredRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.woman_salower, "Shalwar Kameez"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.woman_kurta, "Kurta"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.woman_saree, "Saree"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.woman_hijab, "Hijab"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.woman_borkha, "Borkha"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.woman_tshirt, "T_shirt"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.women_collection, "Sub-Category5"));
        setOnClickListener();
        adapter = new FeaturedAdapter(featuredLocations, this, onNoteListener);
        recyclerView.setAdapter(adapter);

    }

    private void featuredRecycler2() {
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations2 = new ArrayList<>();

        featuredLocations2.add(new FeaturedHelperClass(R.drawable.men_tshirt, "T-shirt"));
        featuredLocations2.add(new FeaturedHelperClass(R.drawable.man_panjabi, "Punjabi"));
        featuredLocations2.add(new FeaturedHelperClass(R.drawable.man_jeans, "Pant"));
        featuredLocations2.add(new FeaturedHelperClass(R.drawable.man_hoody, "Hoody"));
        featuredLocations2.add(new FeaturedHelperClass(R.drawable.men_shirt, "Shirt"));
        featuredLocations2.add(new FeaturedHelperClass(R.drawable.man_collection, "Sub-Category6"));
        setOnClickListener();
        adapter = new FeaturedAdapter(featuredLocations2, this, onNoteListener2);
        recyclerView2.setAdapter(adapter);

    }
    private void featuredRecycler3() {
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        featuredLocations3 = new ArrayList<>();

        featuredLocations3.add(new FeaturedHelperClass(R.drawable.kid_tshirt, "T-shirt"));
        featuredLocations3.add(new FeaturedHelperClass(R.drawable.kid_woman_dress, "Frock"));
        featuredLocations3.add(new FeaturedHelperClass(R.drawable.kid_panjabi, "Panjabi"));
        featuredLocations3.add(new FeaturedHelperClass(R.drawable.kid_pant, "Pant"));
        featuredLocations3.add(new FeaturedHelperClass(R.drawable.kids_collection, "Sub-Category5"));
        setOnClickListener();
        adapter = new FeaturedAdapter(featuredLocations3, this, onNoteListener3);
        recyclerView3.setAdapter(adapter);
    }
    private void setOnClickListener() {
        onNoteListener = new FeaturedAdapter.OnNoteListener() {
            @Override
            public void onNoteClick(int position) {
                Log.i("Position: ", featuredLocations.get(position).getName());
                Intent intent = new Intent(getApplicationContext(), ShopProductList.class);
                intent.putExtra("Category", "Women's Fashion");
                intent.putExtra("Sub Category", featuredLocations.get(position).getName());
                startActivity(intent);

            }
        };

        onNoteListener2 = new FeaturedAdapter.OnNoteListener() {
            @Override
            public void onNoteClick(int position) {
                Log.i("Position: ", featuredLocations2.get(position).getName());
                Intent intent = new Intent(getApplicationContext(), ShopProductList.class);
                intent.putExtra("Category", "Men's Fashion");
                intent.putExtra("Sub Category", featuredLocations2.get(position).getName());
                startActivity(intent);

            }
        };
        onNoteListener3 = new FeaturedAdapter.OnNoteListener() {
            @Override
            public void onNoteClick(int position) {
                Log.i("Position: ", featuredLocations3.get(position).getName());
                Intent intent = new Intent(getApplicationContext(), ShopProductList.class);
                intent.putExtra("Category", "Kid's Fashion");
                intent.putExtra("Sub Category", featuredLocations3.get(position).getName());
                startActivity(intent);
            }
        };

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_shop_home:
                Intent intent = new Intent(ShopCategory.this,ShopHome.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_shop_orders:
                Intent orderIntent = new Intent(ShopCategory.this,ShopOrder.class);
                startActivity(orderIntent);
                finish();
                break;
            case R.id.nav_shop_categories:
                break;
            case R.id.nav_shop_add_product:
                Intent intentAdd = new Intent(ShopCategory.this, AddProductActivity.class);
                startActivity(intentAdd);
                break;
            case R.id.nav_shop_logout:
               // auth.signOut();
                startActivity(new Intent(ShopCategory.this, MainActivity.class));
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
