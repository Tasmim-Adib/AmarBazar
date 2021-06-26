package com.example.amarmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements PopUP.SingleChoiceListener{
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private EditText emailEditText,passwordEditText;
    private TextView topTextView,bottomTextView;
    private Button loginButton,signupButton;
    private ProgressDialog loadingBar;
    private String signupEmail,signupPassword,accountType,currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

       if(currentUser != null){
            sendUsertoFirstActivity();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        bottomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.GONE);
                signupButton.setVisibility(View.VISIBLE);
                topTextView.setText(getString(R.string.needAccount));
                bottomTextView.setVisibility(View.GONE);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });


    }

    private void init(){
        topTextView = (TextView)findViewById(R.id.already_have_account);
        bottomTextView = (TextView)findViewById(R.id.need_account_texView);
        loginButton = (Button)findViewById(R.id.login_button);
        signupButton = (Button)findViewById(R.id.Signup_button);
        emailEditText = (EditText)findViewById(R.id.email_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
       //
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("Images");


    }

    private void userLogin() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("please enter an valid email");
            emailEditText.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password) && password.length() <=8){
            passwordEditText.setError("Password (at least 8 character)");
            passwordEditText.requestFocus();
        }

        else{
            loadingBar.setTitle("Log In");
            loadingBar.setMessage("Please Wait..");

            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUser = mAuth.getCurrentUser().getUid();
                                sendUsertoFirstActivity();
                                Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else {
                                String message = task.getException().toString();
                                Toast.makeText(MainActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }


                    });
        }
    }

    private void userSignUp() {
        signupEmail = emailEditText.getText().toString();
        signupPassword = passwordEditText.getText().toString();


        if(TextUtils.isEmpty(signupEmail)){
            emailEditText.requestFocus();
            emailEditText.setError("please enter an email");
            return;
        }
        if(TextUtils.isEmpty(signupPassword) && signupPassword.length() <=8){
            passwordEditText.requestFocus();
            passwordEditText.setError("Password must be 8 characters");
            return;
        }



        else{

            DialogFragment popupDailog = new PopUP();
            popupDailog.setCancelable(false);
            popupDailog.show(getSupportFragmentManager(),"Account Type");
        }

    }
    public void setAccountType(String a){
        accountType = a;
    }

    public String getAccountType(){
        return accountType;
    }


    @Override
    public void onPositiveButtonClick(final String[] list, final int position) {
        loadingBar.setTitle("Sign Up");
        loadingBar.setMessage("Please wait, while your account is creating...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        mAuth.createUserWithEmailAndPassword(signupEmail, signupPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            setAccountType(list[position]);
                            loadingBar.dismiss();
                            currentUser = mAuth.getCurrentUser().getUid();
                            sendUsertoRegisterActivity();
                        }
                        else {
                            String message = task.getException().toString();
                            Toast.makeText(MainActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }
                });

    }

    private void sendUsertoRegisterActivity() {

        Intent registerActivityIntent = new Intent(getApplicationContext(),BuyerRegister.class);
        registerActivityIntent.putExtra("ACCOUNT_TYPE",getAccountType());
        startActivity(registerActivityIntent);
        finish();
    }

    @Override
    public void onNegativeButtonClick() {

    }

    private void sendUsertoFirstActivity() {
        databaseReference.child("Users").child(currentUser).child("Identity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String a = snapshot.child("type").getValue().toString();
                if(a.equals("Customer")){
                    Intent firstActivityIntent = new Intent(getApplicationContext(),Curstomer_drawyer.class);
                    startActivity(firstActivityIntent);
                    finish();
                }

                else{
                    Intent firstActivityIntent = new Intent(getApplicationContext(),ShopHome.class);
                    startActivity(firstActivityIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
