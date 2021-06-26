package com.example.amarmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_offer extends Fragment {
    private View view;
    private DatabaseReference databaseReference,shopLogoRef,offerRef;
    private RecyclerView recyclerView;
    private CircleImageView myProfileImage;

    private TextView myProfileName;
    private FirebaseAuth mAuth;
    private String currentUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_offer,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Offer_Product");
        offerRef = FirebaseDatabase.getInstance().getReference().child("Product");
        shopLogoRef = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = (RecyclerView)view.findViewById(R.id.offer_product_recycler_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                        .setQuery(databaseReference,Upload.class)
                        .build();


        FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Upload, FragmentHome.ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final FragmentHome.ProductViewHolder productViewHolder, int i, @NonNull Upload upload) {
                        final String productId = getRef(i).getKey();
                        offerRef.child(productId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final String productid = snapshot.child("productID").getValue().toString();
                                String productType = snapshot.child("productType").getValue().toString();
                                String productPrice = snapshot.child("productPrice").getValue().toString();
                                String productImage = snapshot.child("productImageUrl").getValue().toString();
                                String likes = snapshot.child("reviews").getValue().toString();
                                String orderCount = snapshot.child("ordersCount").getValue().toString();
                                String shopId = snapshot.child("shopId").getValue().toString();
                                if(snapshot.hasChild("discountPrice")){
                                    String dis = snapshot.child("discountPrice").getValue().toString();
                                    productViewHolder.prductDiscountPrice.setText("Current Price : "+dis + "tk");
                                    productViewHolder.prductDiscountPrice.setVisibility(View.VISIBLE);
                                }


                                productViewHolder.productType.setText("Product Type : " +productType);
                                productViewHolder.productPrice.setText("Price : "+productPrice+"tk");
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
                                        Intent intent = new Intent(getActivity(),ProductDetails.class);
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
}
