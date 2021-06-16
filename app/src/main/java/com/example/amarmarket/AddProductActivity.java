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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddProductActivity extends AppCompatActivity {

    private ImageView addProductImage;
    private EditText addProuctId,addProductType,addProductPrice;
    private Button addProuductButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private Bitmap imageStore;
    private String currentUserId,productKey;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        init();

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
        currentUserId = mAuth.getCurrentUser().getUid().toString();



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
        final String productid,productType,productPrice;
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

                        Upload upload = new Upload(productid,productType,productPrice,downloadUrl.toString());
                        databaseReference.child("Product").child(productKey).setValue(upload);
                        Toast.makeText(getApplicationContext(), "Successfully stored", Toast.LENGTH_SHORT).show();



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Error :"+exception, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
