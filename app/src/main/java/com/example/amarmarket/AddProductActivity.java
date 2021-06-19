package com.example.amarmarket;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView addProductImage;
    private EditText addProuctId,addProductType,addProductPrice,productSubCategory,productCategory;
    private Button addProuductButton,okButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private Bitmap imageStore;
    private String currentUserId,productKey,size;
    private FirebaseAuth mAuth;
    private Spinner sizeSpinner;
    private EditText quantityEditText;
    private HashMap hashMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        init();
        hashMap = new HashMap();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Sizes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);
        sizeSpinner.setOnItemSelectedListener(this);

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
        sizeSpinner = findViewById(R.id.add_product_spinner_size);
        okButton = findViewById(R.id.add_product_ok_button);
        quantityEditText = findViewById(R.id.add_product_quantity_texView);
        productSubCategory = findViewById(R.id.add_product_sub_Category);
        productCategory = findViewById(R.id.add_product_Category);


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

        subcategory = productSubCategory.getText().toString();
        category = productCategory.getText().toString();
        StorageReference filePath = storageReference.child(productKey + "."+getFileExtension(imageUri));
        filePath.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUrl = uriTask.getResult();

                        Upload upload = new Upload(productid,productType,productPrice,downloadUrl.toString(),Integer.toString(0),Integer.toString(0),currentUserId,category);
                        databaseReference.child("Product").child(productKey).setValue(upload);
                        databaseReference.child("Category").child(category).child(subcategory).child(productKey).child("Price").setValue(productPrice);
                        databaseReference.child("Product").child(productKey).child("Sizes").setValue(hashMap);
                        Toast.makeText(getApplicationContext(), "Successfully stored", Toast.LENGTH_SHORT).show();
                        addProductType.setText(null);
                        addProuctId.setText(null);
                        addProductPrice.setText(null);
                        quantityEditText.setText(null);
                        hashMap = new HashMap();

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        setSize(text);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
