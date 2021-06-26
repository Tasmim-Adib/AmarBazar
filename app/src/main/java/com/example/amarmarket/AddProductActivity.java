package com.example.amarmarket;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private ImageView addProductImage;
    private EditText addProuctId,addProductType,addProductPrice;
    private Button addProuductButton,okButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private Bitmap imageStore;
    private String currentUserId,productKey,size,subCategory,category;
    private FirebaseAuth mAuth;
    private Spinner spinnerSize,spinnerCat,spinnerSubCat;
    private EditText quantityEditText;
    private HashMap hashMap;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView profileName;
    private CircleImageView profileImage;
    private View view;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        init();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Product");
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_shop_add_product);
        view = navigationView.getHeaderView(0);
        profileImage = view.findViewById(R.id.nav_profile_image);
        profileName = view.findViewById(R.id.nav_name);
        databaseReference.child("Users").child(currentUserId).child("Identity")
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




        hashMap = new HashMap();

        String[] items = new String[]{"Women's Fashion", "Men's Fashion", "Kid's Fashion"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinnerCat.setAdapter(adapter);
        spinnerCat.setOnItemSelectedListener(this);

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Sizes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);
        sizeSpinner.setOnItemSelectedListener(this);*/

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sizeTotal = quantityEditText.getText().toString();
                hashMap.put(getSize(),sizeTotal);
                Toast.makeText(getApplicationContext(),getSize() + " " + sizeTotal,Toast.LENGTH_SHORT).show();
            }
        });

        addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addProuductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productKey = databaseReference.push().getKey();
                loadingBar.setTitle("Uploading");
                loadingBar.setMessage("Your product is uploding please wait");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();
                saveData();

            }
        });
    }




    private void init(){

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("Product_Images");
        addProductImage = findViewById(R.id.add_product_image);
        addProuctId = findViewById(R.id.add_product_product_id);
        addProductType = findViewById(R.id.add_product_product_type);
        addProductPrice = findViewById(R.id.add_product_product_price);
        addProuductButton = findViewById(R.id.add_product_add_button);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        spinnerSize= findViewById(R.id.add_product_spinner_size);
        okButton = findViewById(R.id.add_product_ok_button);
        quantityEditText = findViewById(R.id.add_product_quantity_texView);
        drawerLayout = findViewById(R.id.add_product_drawyer);
        navigationView = findViewById(R.id.add_product_navigation_view);
        toolbar = findViewById(R.id.add_product_toolbar);
        spinnerCat = findViewById(R.id.spinner_cat);
        spinnerSubCat = findViewById(R.id.spinner_sub_cat);
        loadingBar = new ProgressDialog(this);


    }


    ActivityResultLauncher<Intent> chooseImageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    try {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            imageStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                            addProductImage.setImageBitmap(imageStore);
                        }
                    }
                    catch (Exception e){

                    }
                }
            }
    );

    private void chooseImage(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        chooseImageResultLauncher.launch(galleryIntent);

    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    private void saveData(){
        final String productid,productType,productPrice,subcategory,category;
        productid = addProuctId.getText().toString();
        productType = addProductType.getText().toString();
        productPrice = addProductPrice.getText().toString();


        StorageReference filePath = storageReference.child(productKey + "."+getFileExtension(imageUri));
        filePath.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUrl = uriTask.getResult();

                        Upload upload = new Upload(productKey,productid,productType,productPrice,downloadUrl.toString(),Integer.toString(0),Integer.toString(0),currentUserId,getCategory(),getSubCategory());
                        databaseReference.child("Product").child(productKey).setValue(upload);
                        databaseReference.child("Category").child(getCategory()).child(getSubCategory()).child(productKey).child("Price").setValue(productPrice);
                        databaseReference.child("Product").child(productKey).child("Sizes").setValue(hashMap);
                        databaseReference.child("Users").child(currentUserId).child("My_Product").child(getSubCategory()).child(productKey).child("isHere").setValue("Yes");
                        Toast.makeText(getApplicationContext(), currentUserId, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        addProductType.setText(null);
                        addProuctId.setText(null);
                        addProductPrice.setText(null);
                        quantityEditText.setText(null);
                        hashMap = new HashMap();
                        loadingBar.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Error :"+exception, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (spinnerCat.getSelectedItem().equals("Women's Fashion")) {
            ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(this, R.array.women, android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCat.setAdapter(adapterSubCat);
            String cat = parent.getItemAtPosition(position).toString();
            setCategory(cat);
        }

        else if (spinnerCat.getSelectedItem().equals("Men's Fashion")) {
            ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(this, R.array.men, android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCat.setAdapter(adapterSubCat);
            String cat = parent.getItemAtPosition(position).toString();
            setCategory(cat);
        }
        else if(spinnerCat.getSelectedItem().equals("Kid's Fashion")){
            ArrayAdapter adapterSubCat = ArrayAdapter.createFromResource(this, R.array.kid, android.R.layout.simple_spinner_dropdown_item);
            spinnerSubCat.setAdapter(adapterSubCat);
            String cat = parent.getItemAtPosition(position).toString();
            setCategory(cat);
        }

        spinnerSubCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerCat.getSelectedItem().toString().equals("Men's Fashion") && spinnerSubCat.getSelectedItem().toString().equals("T-shirt")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T_shirt, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);
                }
                else if (spinnerCat.getSelectedItem().toString().equals("Men's Fashion") && spinnerSubCat.getSelectedItem().toString().equals("Punjabi")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Shirt_punjabi, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);

                }
                else if (spinnerCat.getSelectedItem().toString().equals("Men's Fashion") && spinnerSubCat.getSelectedItem().toString().equals("Shirt")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Shirt_punjabi, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);

                }
                else if (spinnerCat.getSelectedItem().toString().equals("Men's Fashion") && spinnerSubCat.getSelectedItem().toString().equals("Pant")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Men_pant, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);

                }
                else if (spinnerCat.getSelectedItem().toString().equals("Men's Fashion") && spinnerSubCat.getSelectedItem().toString().equals("Hoody")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.T_shirt, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);

                }
                else if(spinnerCat.getSelectedItem().toString().equals("Women's Fashion")){
                    if(spinnerSubCat.getSelectedItem().toString().equals("Saree")){
                        ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Saree, R.layout.support_simple_spinner_dropdown_item);
                        spinnerSize.setAdapter(adapterSize);
                    }

                    else{
                        ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.WomenFashion, R.layout.support_simple_spinner_dropdown_item);
                        spinnerSize.setAdapter(adapterSize);
                    }
                }
                /*else if (spinnerCat.getSelectedItem().toString().equals("Women's Fashion") && spinnerSubCat.getSelectedItem().toString().equals("Shalwar Kameez")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.WomenFashion, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);

                }
                else if (spinnerCat.getSelectedItem().toString().equals("Women's Fashion") && spinnerSubCat.getSelectedItem().toString().equals("Kurta")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.WomenFashion, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);

                }*/else if (spinnerCat.getSelectedItem().toString().equals("Kid's Fashion")) {
                    ArrayAdapter adapterSize = ArrayAdapter.createFromResource(getApplicationContext(), R.array.kids_fashion, R.layout.support_simple_spinner_dropdown_item);
                    spinnerSize.setAdapter(adapterSize);

                }
                String cat = parent.getItemAtPosition(position).toString();
                setSubCategory(cat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                setSize(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_shop_home:
                Intent intentHome = new Intent(AddProductActivity.this, ShopHome.class);
                startActivity(intentHome);
                finish();
                break;
            case R.id.nav_shop_categories:
                Intent intentCat = new Intent(AddProductActivity.this, ShopCategory.class);
                startActivity(intentCat);
                finish();
                break;
            case R.id.nav_shop_add_product:
                break;
            case R.id.nav_shop_orders:
                Intent orderIntent = new Intent(AddProductActivity.this,ShopOrder.class);
                startActivity(orderIntent);
                finish();
                break;
            case R.id.nav_shop_logout:
               // mAuth.signOut();
                startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                finish();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
