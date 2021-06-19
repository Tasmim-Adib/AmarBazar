package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Curstomer_drawyer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private ImageView shoppingCart;
    private CircleImageView myProfileImage;

    private TextView myProfileName;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String currentUser;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curstomer_drawyer);
        toolbar = findViewById(R.id.customer_drawyer_toolbar);
        setSupportActionBar(toolbar);

        shoppingCart = (ImageView)findViewById(R.id.top_shopping_cart_view);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser().getUid();



        drawerLayout = findViewById(R.id.customer_drawyer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        view = navigationView.getHeaderView(0);
        myProfileImage = (CircleImageView)view.findViewById(R.id.nav_profile_image);
        myProfileName = (TextView)view.findViewById(R.id.nav_name);

        databaseReference.child("Users").child(currentUser).child("Identity")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();

                        myProfileName.setText(name);
                        Picasso.get().load(image).into(myProfileImage);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("Error :  " +error);
                    }
                });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.nav_open_drawyer,R.string.nav_close_drawyer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout,
                    new FragmentHome()).commit();
            navigationView.setCheckedItem(R.id.nav_home);



        }


    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout,
                        new FragmentHome()).commit();
                break;
            case R.id.nav_category:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout,
                        new FragmentCategory()).commit();
                break;
            case R.id.nav_myCart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout,
                        new FragmentMyCart()).commit();
                break;
            case R.id.nav_myOrders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout,
                        new FragmentMyOrder()).commit();
                break;
            case R.id.nav_update_profile:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_layout,
                       new FragmentUpdateProfile()).commit();
                break;
            case R.id.nav_logout:
               // mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
