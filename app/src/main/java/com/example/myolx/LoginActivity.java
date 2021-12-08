package com.example.myolx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
     private TextInputLayout Lemail,Lpassword;
     private   Button lLogin,lRegister;
     private TextView Forget;
   //  private String email,pass;
     private FirebaseAuth firebaseAuth;
    // private FirebaseAuth.AuthStateListener stateListener;
  //   private FirebaseUser currentUser;
   //  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  //  private CollectionReference collectionReference = db.collection("Users");
   // private DatabaseReference databaseReference;
  //  private StorageReference storageReference;
   FirebaseDatabase  database;
   SharedPreferences preferences;
   CheckBox rememberMe;
   String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iniviews();
        rememberMe = findViewById(R.id.login_rememberMe);
        rememberMe();
        this.setTitle("Log in");

    }

    private void rememberMe(){
        preferences = getSharedPreferences("mypref",MODE_PRIVATE);
        String log_checkbox = preferences.getString("remember", "");


        if(log_checkbox.equals("true"))
        {
            //Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }



    private void iniviews() {
        firebaseAuth = FirebaseAuth.getInstance();
     //   databaseReference =FirebaseDatabase.getInstance().getReference();
           Lemail=findViewById(R.id.etLemail);
        Lpassword = findViewById(R.id.etlpassword);
        lLogin = findViewById(R.id.btnlogin);
        lRegister = findViewById(R.id.btnregister);
        Forget = findViewById(R.id.forgetpass);
        lLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getApplicationContext(),MainActivity.class));
                email = Lemail.getEditText().getText().toString().trim();
                password = Lpassword.getEditText().getText().toString().trim();
                HashMap<String,Object>Post = new HashMap<>();
                Post.put("Email",email);
                Post.put("Password",password);

                if(TextUtils.isEmpty(email)){
                    Lemail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Lpassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    Lpassword.setError("Password Must be >= 6 Characters");
                    return;
                }


                if (rememberMe.isChecked()){
                    SharedPreferences.Editor editorforRemember = preferences.edit();
                    editorforRemember.putString("remember", "true");
                    editorforRemember.apply();
                    editorforRemember.commit();
                    signINwithFirebase();
                }else{
                    SharedPreferences.Editor editorforRemember = preferences.edit();
                    editorforRemember.putString("remember", "false");
                    editorforRemember.apply();
                    editorforRemember.commit();
                    signINwithFirebase();
                }


            }
        });


        lRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
        Forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(LoginActivity.this,ForgetPassword.class);
              startActivity(intent);
            }
        });
//        stateListener = new FirebaseAuth.AuthStateListener(){
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                currentUser = firebaseAuth.getCurrentUser();
//                if (currentUser!=null){
//
//                }else {
//
//                }
//            }
//        };


    }

    private void signINwithFirebase(){
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            //  Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                            //   progressBar.setVisibility(View.GONE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "login error"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    protected void onStart() {
//        super.onStart();
//        currentUser =firebaseAuth.getCurrentUser();
//        //CREATE STATELISTENER IN INITVIEW.
//        firebaseAuth.addAuthStateListener(stateListener);
//    }
}