package com.example.amarmarket;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class BuyerRegister extends AppCompatActivity {

    private CircleImageView buyerImage;
    private EditText buyerName,buyerAddress,buyerContact;
    private Button buyerRegisterButton,okButton;
    private TextView messageText;
    private Uri imageUri;
    private Bitmap imageStore;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String currentUserId,accountType;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_register);
        accountType = getIntent().getExtras().get("ACCOUNT_TYPE").toString();


        init();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterMenu();
            }
        });

        buyerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        buyerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfileImage();
            }
        });
    }



    private void init(){
        buyerImage = (CircleImageView)findViewById(R.id.buyer_register_circleimage);
        buyerName = (EditText)findViewById(R.id.buyer_register_name);
        buyerAddress = (EditText)findViewById(R.id.buyer_register_address);
        buyerContact = (EditText)findViewById(R.id.buyer_register_contact);
        buyerRegisterButton = (Button)findViewById(R.id.buyer_register_Button);
        okButton = (Button)findViewById(R.id.buyer_register_ok);
        messageText = (TextView)findViewById(R.id.messageTextView);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile_Images");
        loadingBar = new ProgressDialog(this);
    }
    private void showRegisterMenu() {
        messageText.setVisibility(View.GONE);
        okButton.setVisibility(View.GONE);
        buyerImage.setVisibility(View.VISIBLE);
        buyerName.setVisibility(View.VISIBLE);
        buyerAddress.setVisibility(View.VISIBLE);
        buyerContact.setVisibility(View.VISIBLE);
        buyerRegisterButton.setVisibility(View.VISIBLE);
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
                            buyerImage.setImageBitmap(imageStore);
                        }
                    }
                    catch (Exception e){

                    }
                }
            }
    );

    private void chooseProfileImage(){
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


    private void registerUser() {

        final String name, address, contact;
        name = buyerName.getText().toString();
        address = buyerAddress.getText().toString();
        contact = buyerContact.getText().toString();

        if(TextUtils.isEmpty(name)){
            buyerName.setError("Your Name");
            buyerName.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(address)){
            buyerAddress.setError("Valid Address");
            buyerAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(contact) && contact.length() < 11){
            buyerContact.requestFocus();
            buyerContact.setError("Valid Contact");
            return;
        }

        else{
            loadingBar.setTitle("Registration");
            loadingBar.setMessage("Please Wait your registration is processing");

            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            StorageReference filePath = storageReference.child(currentUserId + "."+getFileExtension(imageUri));
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful());
                    Uri downloadUrl = uriTask.getResult();

                    HashMap hashMap = new HashMap();
                    hashMap.put("name",name);
                    hashMap.put("address",address);
                    hashMap.put("contact",contact);
                    hashMap.put("image",downloadUrl.toString());
                    hashMap.put("type",accountType);

                    databaseReference.child("Users").child(currentUserId).child("Identity").setValue(hashMap);
                    Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    sendUserTofirstActivity(accountType);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Error :"+exception, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void sendUserTofirstActivity(String accountType) {
        if (accountType.equals("Customer")){
            Intent firstActivityIntent = new Intent(getApplicationContext(),Curstomer_drawyer.class);
            startActivity(firstActivityIntent);
            finish();
        }
        else{
            Intent firstActivityIntent = new Intent(getApplicationContext(),Curstomer_drawyer.class);
            startActivity(firstActivityIntent);
            finish();
        }
    }


}
