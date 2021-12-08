package com.example.myolx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
   private Button Fbutton;
   private EditText ftext;
   private FirebaseAuth auth;
   private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password2);
      auth = FirebaseAuth.getInstance();
       Fbutton = findViewById(R.id.btnfpass);
       ftext = findViewById(R.id.femail);
       Fbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ValidateDate();
           }
       });

    }

    private void ValidateDate() {
    email = ftext.getText().toString();
    if (email.isEmpty()){
        ftext.setError("Email Required");
    }else{
        forgetpassword();
    }
    }

    private void forgetpassword() {
    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                Toast.makeText(ForgetPassword.this, "Check Your Mail", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetPassword.this,LoginActivity.class));
                finish();
            }else{
                Toast.makeText(ForgetPassword.this, "error"+task.getException(), Toast.LENGTH_SHORT).show();
            }
        }
    });

    }
}