package com.example.amarmarket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMyCart extends Fragment{


    private View view;
    private RecyclerView recyclerView;
    private EditText addressText,contactText;
    private TextView confirm;
    private FirebaseAuth mAuth;
    private String currentUser;
    private DatabaseReference databaseReference,productRef,cartRef;
    private LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_my_cart,container,false);
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        addressText = view.findViewById(R.id.framentCart_address);
        contactText = view.findViewById(R.id.framentCart_contact);
        confirm = view.findViewById(R.id.framentCart_Confirm);
        linearLayout = view.findViewById(R.id.address_contact_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        cartRef = FirebaseDatabase.getInstance().getReference();
        cartRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("MyCart");


        databaseReference.child("Users").child(currentUser).child("Identity")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String contact = snapshot.child("contact").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        addressText.setText(address);
                        contactText.setText(contact);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                loadData();
            }
        });

        return view;
    }

    private void loadData(){

        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Upload>()
                        .setQuery(cartRef,Upload.class)
                        .build();
        FirebaseRecyclerAdapter<Upload,CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Upload, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i, @NonNull Upload upload) {
                        final String product_ID = getRef(i).getKey();
                        productRef.child(product_ID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final String id = snapshot.child("productID").getValue().toString();
                                String type = snapshot.child("productType").getValue().toString();
                                String price = snapshot.child("productPrice").getValue().toString();
                                String image = snapshot.child("productImageUrl").getValue().toString();

                                cartViewHolder.cartProductId.setText("Product ID ; "+id);
                                cartViewHolder.cartProductType.setText("Category : "+type);
                                cartViewHolder.cartProductPrice.setText("Price : "+price+" tk");
                                Picasso.get().load(image).into(cartViewHolder.productLogo);

                                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence item[] = new CharSequence[]{
                                                "View",
                                                "Decline"
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle(id + " View or Decline");
                                        builder.setItems(item, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(which == 0){
                                                    Intent intent = new Intent(getActivity(),ProductDetails.class);
                                                    intent.putExtra("visit_product",product_ID);
                                                    startActivity(intent);
                                                }
                                                if(which == 1){
                                                    cartRef.child(product_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                           if(task.isSuccessful()){
                                                               Toast.makeText(getContext(),"Product Removed from your cart",Toast.LENGTH_SHORT).show();
                                                           }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                        builder.show();

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
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_sample,parent,false);
                        CartViewHolder viewHolder = new CartViewHolder(view);
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public  static  class CartViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView productLogo;
        private View view;
        private TextView cartProductId,cartProductType,cartProductPrice,cartProductView,cartProductDecline;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productLogo = itemView.findViewById(R.id.cart_sample_image);
            cartProductId = itemView.findViewById(R.id.cart_product_id);
            cartProductType = itemView.findViewById(R.id.cart_type_id);
            cartProductPrice = itemView.findViewById(R.id.cart_price_id);
            cartProductView = itemView.findViewById(R.id.cart_view_id);
            cartProductDecline = itemView.findViewById(R.id.cart_decline_id);
            view = itemView.findViewById(R.id.cart_bottom_line);
        }
    }
}
