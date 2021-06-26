package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShopOrder extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private DatabaseReference databaseReference,orderRef,productRef,shopRef;
    private FirebaseAuth mAuth;
    private String currentUser;
    private TextView profileName;
    private CircleImageView profileImage;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order);
        recyclerView = findViewById(R.id.shop_order_recycler);
        drawerLayout = findViewById(R.id.drawer_layout_order);
        navigationView = findViewById(R.id.nav_view_order);
        toolbar = findViewById(R.id.toolbar_order);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Orders");
        orderRef = FirebaseDatabase.getInstance().getReference();
        productRef = FirebaseDatabase.getInstance().getReference();
        shopRef = FirebaseDatabase.getInstance().getReference();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Orders");
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        view = navigationView.getHeaderView(0);
        profileImage = view.findViewById(R.id.nav_profile_image);
        profileName = view.findViewById(R.id.nav_name);
        orderRef.child("Users").child(currentUser).child("Identity")
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

        navigationView.setCheckedItem(R.id.nav_shop_orders);
        navigationView.setNavigationItemSelectedListener(this);

        loadData();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_shop_home:
                Intent intent = new Intent(ShopOrder.this,ShopHome.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_shop_categories:
                Intent catIntent = new Intent(ShopOrder.this,ShopCategory.class);
                startActivity(catIntent);
                finish();
                break;
            case R.id.nav_shop_orders:
                break;
            case R.id.nav_shop_add_product:
                Intent intentAdd = new Intent(ShopOrder.this, AddProductActivity.class);
                startActivity(intentAdd);
                finish();
                break;
            case R.id.nav_shop_logout:
                // auth.signOut();
                startActivity(new Intent(ShopOrder.this, MainActivity.class));
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
    private void loadData(){

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MakeOrder>()
                .setQuery(databaseReference,MakeOrder.class)
                .build();

        FirebaseRecyclerAdapter<MakeOrder, FragmentMyCart.CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<MakeOrder, FragmentMyCart.CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FragmentMyCart.CartViewHolder cartViewHolder, final int i, @NonNull MakeOrder makeOrder) {
                        final String orderId = getRef(i).getKey();
                        orderRef.child("Orders").child(orderId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String date = snapshot.child("date").getValue().toString();
                                String product = snapshot.child("productId").getValue().toString();
                                final String sh = snapshot.child("customerId").getValue().toString();
                                if(snapshot.hasChild("state")){
                                    String st = snapshot.child("state").getValue().toString();
                                    cartViewHolder.state.setVisibility(View.VISIBLE);
                                    cartViewHolder.state.setText("Order is "+st);
                                }

                                cartViewHolder.cartProductPrice.setText("Date : "+date);
                                cartViewHolder.cartAcceptView.setVisibility(View.GONE);
                                cartViewHolder.cartProductDecline.setVisibility(View.GONE);
                                cartViewHolder.cartProductView.setVisibility(View.GONE);

                                productRef.child("Product").child(product).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String productName = snapshot.child("productID").getValue().toString();
                                        String image = snapshot.child("productImageUrl").getValue().toString();

                                        cartViewHolder.cartProductId.setText("ProductID : "+productName);
                                        Picasso.get().load(image).into(cartViewHolder.productLogo);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                shopRef.child("Users").child(sh).child("Identity").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String Name = snapshot.child("name").getValue().toString();
                                        cartViewHolder.cartProductType.setText("Customer : "+Name);
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

                        cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent newIntent = new Intent(getApplicationContext(),ShopOrderedProduct.class);
                                newIntent.putExtra("ORDER_ID",orderId);
                                startActivity(newIntent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public FragmentMyCart.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_sample,parent,false);
                        FragmentMyCart.CartViewHolder viewHolder = new FragmentMyCart.CartViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
