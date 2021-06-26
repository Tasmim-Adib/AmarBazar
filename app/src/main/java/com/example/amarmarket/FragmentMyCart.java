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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentMyCart extends Fragment{


    private View view;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private String currentUser,shopId;
    private DatabaseReference databaseReference,productRef,cartRef,removeRef,orderRef;
    private Query query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_my_cart,container,false);
        recyclerView = view.findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        removeRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        orderRef = FirebaseDatabase.getInstance().getReference();
        cartRef = FirebaseDatabase.getInstance().getReference();
        query = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("MyCart").orderByChild("isHere").equalTo("ORDERED");
        cartRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser).child("MyCart");

        loadData();

        return view;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    private void removeProduct(String orderId,String orderProductId){
        cartRef.child(orderId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),"Product Removed from your cart",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cartRef.child(orderProductId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });
    }

    private void loadData(){

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<MakeOrder>()
                .setQuery(query,MakeOrder.class)
                .build();

        FirebaseRecyclerAdapter<MakeOrder,CartViewHolder> adapter = new
                FirebaseRecyclerAdapter<MakeOrder, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i, @NonNull MakeOrder makeOrder) {
                        final String orderId = getRef(i).getKey();
                        databaseReference.child("Orders").child(orderId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final String orderProductID = snapshot.child("productId").getValue().toString();
                                databaseReference.child("Product").child(orderProductID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String id = snapshot.child("productID").getValue().toString();
                                        String type = snapshot.child("productType").getValue().toString();
                                        String logo = snapshot.child("productImageUrl").getValue().toString();
                                        String price = snapshot.child("productPrice").getValue().toString();
                                        String s = snapshot.child("shopId").getValue().toString();
                                        setShopId(s);

                                        cartViewHolder.cartProductId.setText("Product ID : "+id);
                                        cartViewHolder.cartProductPrice.setText("Price : "+price+"tk");
                                        cartViewHolder.cartProductType.setText("Product Type : "+type);
                                        Picasso.get().load(logo).into(cartViewHolder.productLogo);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence item[] = new CharSequence[]{
                                                "View",
                                                "Confirm",
                                                "Decline"
                                        };

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setTitle("What do you Want?");
                                        builder.setItems(item, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(which == 0){
                                                    Intent intent = new Intent(getActivity(),OrderedProductDetails.class);
                                                    intent.putExtra("visit_order",orderId);
                                                    startActivity(intent);
                                                }
                                                if(which == 1){

                                                    orderRef.child("Users").child(getShopId()).child("Orders").child(orderId).child("isHere").setValue("YES");
                                                    orderRef.child("Users").child(currentUser).child("Orders").child(orderId).child("isHere").setValue("YES");
                                                    removeProduct(orderId,orderProductID);
                                                    Toast.makeText(getContext(),"Order is Placed Successfully",Toast.LENGTH_SHORT).show();

                                                }
                                                if(which == 2){
                                                   removeProduct(orderId,orderProductID);
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
        public CircleImageView productLogo;
        public View view;
        public TextView state,cartProductId,cartProductType,cartProductPrice,cartProductView,cartProductDecline,cartAcceptView;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productLogo = itemView.findViewById(R.id.cart_sample_image);
            cartProductId = itemView.findViewById(R.id.cart_product_id);
            cartProductType = itemView.findViewById(R.id.cart_type_id);
            cartProductPrice = itemView.findViewById(R.id.cart_price_id);
            cartProductView = itemView.findViewById(R.id.cart_view_id);
            cartProductDecline = itemView.findViewById(R.id.cart_decline_id);
            view = itemView.findViewById(R.id.cart_bottom_line);
            cartAcceptView = itemView.findViewById(R.id.cart_accept_id);
            cartProductView = itemView.findViewById(R.id.cart_view_id);
            cartProductDecline = itemView.findViewById(R.id.cart_decline_id);
            state = itemView.findViewById(R.id.state_TextView);
        }
    }
}
