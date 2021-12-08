package com.example.myolx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.PostModel;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout Rname, remail, rpassword;
    private Button rLogin, rRegister;
    private String name, email, password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        initview();
        this.setTitle("Sign Up");
    }

    private void initview() {
        remail = findViewById(R.id.etremail);
        Rname = findViewById(R.id.etname);
        rpassword = findViewById(R.id.etrpassword);
        rLogin = findViewById(R.id.btnrlogin);
        rRegister = findViewById(R.id.btnrregister);

        rLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {

                } else {

                }
            }
        };
        rRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(Rname.getEditText().getText().toString()) &&
                        !TextUtils.isEmpty(remail.getEditText().getText().toString()) &&
                        !TextUtils.isEmpty(rpassword.getEditText().getText().toString())) {
                    name = Rname.getEditText().getText().toString().trim();
                    email = remail.getEditText().getText().toString().trim();
                    password = rpassword.getEditText().getText().toString().trim();
                    createUserWithEmailAndPassword(name, email, password);
                } else {
                    Toast.makeText(RegisterActivity.this, "Fields not be empty", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    private void createUserWithEmailAndPassword(final String name, final String email, final String password) {
        if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(email) &&
                !TextUtils.isEmpty(password)) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                               String currentUserID = firebaseAuth.getCurrentUser().getUid();
                             Map userobj = new HashMap<>();
                                userobj.put("id", currentUserID);
                                userobj.put("name", name);
                                userobj.put("password", password);
                                userobj.put("Email", email);

                                  //ModelRegister mode = new ModelRegister(id,name,email,password);
                                  FirebaseDatabase.getInstance().getReference("Register")
                                          .child(FirebaseAuth
                                                  .getInstance().getCurrentUser().getUid()).updateChildren(userobj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          Toast.makeText(RegisterActivity.this, "Data save to firebase", Toast.LENGTH_SHORT).show();
                                      }
                                  });
                   //             databaseReference.child("Register").child(currentUserID).setValue("");
                                //here is the value in rt database

                                // sendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account create Successfully" +
                                        "", Toast.LENGTH_SHORT).show();
                                //  loadingbar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                //pass intent to another activity to get username and email from database to textview
                                //  intent.putExtra("name",na);
                                //  intent.putExtra("User ID",UserID);
                                //  intent.putExtra("UserEmail",email);

                                startActivity(intent);
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Error" + message, Toast.LENGTH_SHORT).show();
                                //     loadingbar.dismiss();
                            }
                        }
                    });
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
     currentUser =firebaseAuth.getCurrentUser();
     //CREATE STATELISTENER IN INITVIEW.
     firebaseAuth.addAuthStateListener(stateListener);
    }
}