package com.example.amarmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentHome extends Fragment {
    private View view;
    private DatabaseReference databaseReference,shopLogoRef;
    private RecyclerView recyclerView;
    private CircleImageView myProfileImage;

    private TextView myProfileName;
    private FirebaseAuth mAuth;
    private String currentUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        shopLogoRef = FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = (RecyclerView)view.findViewById(R.id.product_recycler_view_id);
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
        FirebaseRecyclerAdapter<Upload,ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Upload, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ProductViewHolder productViewHolder, final int i, @NonNull Upload upload) {
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
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
                        ProductViewHolder viewHolder = new ProductViewHolder(view);
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        public ImageView productImage;
        public CircleImageView shopImage;
        private View view;


        public TextView shopContact,productId,productType,productPrice,shopAdd,prductDiscountPrice,thumbsUp,order,shopName;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            prductDiscountPrice = itemView.findViewById(R.id.recycler_view_product_discount_price);
            productPrice = itemView.findViewById(R.id.recycler_view_product_price);
            productType = itemView.findViewById(R.id.recycler_view_product_type);
            productImage = itemView.findViewById(R.id.recycler_view_product_image);
            thumbsUp = itemView.findViewById(R.id.thumbs_up_id);
            order = itemView.findViewById(R.id.thumbs_order_id);
            shopImage = itemView.findViewById(R.id.shop_logo_image);
            shopName = itemView.findViewById(R.id.shop_logo_name);
            productId = itemView.findViewById(R.id.recycler_view_product_id);
            view = itemView.findViewById(R.id.viewid);
            shopAdd = itemView.findViewById(R.id.shop_address);
            shopContact = itemView.findViewById(R.id.shop_contact);
        }


    }
    
}
