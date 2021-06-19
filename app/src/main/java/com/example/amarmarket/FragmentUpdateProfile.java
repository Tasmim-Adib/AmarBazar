package com.example.amarmarket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUpdateProfile extends Fragment {

    private EditText updateProfileName,updateProfileContact,updateProfileAddress;
    private Button updateProfileDoneButton;
    private Uri imageUri;
    private Bitmap imageStore;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String currentUser,img,tp;
    private ProgressDialog loadingBar;

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_update_profile,container,false);
        updateProfileName = view.findViewById(R.id.update_profile_name);
        updateProfileAddress = view.findViewById(R.id.update_profile_address);
        updateProfileContact = view.findViewById(R.id.update_profile_contact);
        updateProfileDoneButton = view.findViewById(R.id.update_profile_Button);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        loadingBar = new ProgressDialog(getContext());

        databaseReference.child("Users").child(currentUser).child("Identity")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String name = snapshot.child("name").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();
                        String contact = snapshot.child("contact").getValue().toString();
                        String image = snapshot.child("image").getValue().toString();
                        String type = snapshot.child("type").getValue().toString();

                        updateProfileName.setText(name);
                        updateProfileAddress.setText(address);
                        updateProfileContact.setText(contact);
                        setTp(type);
                        setImg(image);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        updateProfileDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        return view;
    }



    private void updateProfile(){

        loadingBar.setTitle("Update Profile");
        loadingBar.setMessage("Your profile is Updating");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();

        String name,address,contact;
        name = updateProfileName.getText().toString();
        address = updateProfileAddress.getText().toString();
        contact = updateProfileContact.getText().toString();

        if(TextUtils.isEmpty(name)){
            updateProfileName.setError("Your Name");
            updateProfileName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(address)){
            updateProfileAddress.setError("Valid Address");
            updateProfileAddress.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(contact)){
            updateProfileContact.setError("Valid Contact No");
            updateProfileContact.requestFocus();
            return;
        }

        else{

            HashMap hashMap = new HashMap();
            hashMap.put("name",name);
            hashMap.put("address",address);
            hashMap.put("contact",contact);
            hashMap.put("image",getImg());
            hashMap.put("type",getTp());
            databaseReference.child("Users").child(currentUser).child("Identity").updateChildren(hashMap);
            loadingBar.dismiss();
            Toast.makeText(getContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
        }

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }
}
