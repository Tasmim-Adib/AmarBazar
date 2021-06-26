package com.example.amarmarket;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FragmentMyOrder extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference,orderRef,shopRef,productRef;
    private String currentUser,shopId;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_order,container,false);
        recyclerView = view.findViewById(R.id.order_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("Orders");
        orderRef = FirebaseDatabase.getInstance().getReference();
        productRef = FirebaseDatabase.getInstance().getReference();
        shopRef = FirebaseDatabase.getInstance().getReference();
        loadData();


        return view;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    private void loadData(){
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MakeOrder>()
                .setQuery(databaseReference,MakeOrder.class)
                .build();

        FirebaseRecyclerAdapter<MakeOrder, FragmentMyCart.CartViewHolder>  adapter =
                new FirebaseRecyclerAdapter<MakeOrder, FragmentMyCart.CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FragmentMyCart.CartViewHolder cartViewHolder, final int i, @NonNull MakeOrder makeOrder) {
                        String orderId = getRef(i).getKey();
                        orderRef.child("Orders").child(orderId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String date = snapshot.child("date").getValue().toString();
                                String product = snapshot.child("productId").getValue().toString();
                                if(snapshot.hasChild("state")){
                                    String st = snapshot.child("state").getValue().toString();
                                    cartViewHolder.state.setVisibility(View.VISIBLE);
                                    cartViewHolder.state.setText("Your order is "+st);
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
                                        String sh = snapshot.child("shopId").getValue().toString();
                                        cartViewHolder.cartProductId.setText("ProductID : "+productName);
                                        Picasso.get().load(image).into(cartViewHolder.productLogo);

                                        shopRef.child("Users").child(sh).child("Identity").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String shopName = snapshot.child("name").getValue().toString();
                                                cartViewHolder.cartProductType.setText("Shop Name : "+shopName);
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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
