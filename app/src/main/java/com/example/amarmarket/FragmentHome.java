package com.example.amarmarket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FragmentHome extends Fragment {
    private View view;
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_home,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");
        recyclerView = (RecyclerView)view.findViewById(R.id.product_recycler_view_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Product");


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
                                String productid = snapshot.child("productID").getValue().toString();
                                String productType = snapshot.child("productType").getValue().toString();
                                String productPrice = snapshot.child("productPrice").getValue().toString();
                                String productImage = snapshot.child("productImageUrl").getValue().toString();

                                productViewHolder.productType.setText("Product Type : " +productType);
                                productViewHolder.productPrice.setText("Price : "+productPrice);
                                Picasso.get().load(productImage).into(productViewHolder.productImage);

                                productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String visit_product = getRef(i).getKey();
                                        Toast.makeText(getContext(),visit_product,Toast.LENGTH_SHORT).show();
                                        /*Intent intent = new Intent(getContext(),ProductDetails.class);
                                        intent.putExtra("visit_product",visit_product);
                                        startActivity(intent);*/
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

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productId,productType,productPrice,prductDiscountPrice;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            prductDiscountPrice = itemView.findViewById(R.id.recycler_view_product_discount_price);
            productPrice = itemView.findViewById(R.id.recycler_view_product_price);
            productType = itemView.findViewById(R.id.recycler_view_product_type);
            productImage = itemView.findViewById(R.id.recycler_view_product_image);
        }
    }
}
