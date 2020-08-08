package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
  EditText mEmail,mPassword;
  Button mLoginBtn;
  TextView mCreateBtn,forgotTextLink;
  FirebaseAuth fAuth;
  private Dialog waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail=findViewById(R.id.email);
        mEmail.setHintTextColor(Color.rgb(103,58,183));

        mPassword=findViewById(R.id.password);
        mPassword.setHintTextColor(Color.rgb(103,58,183));

       fAuth=FirebaseAuth.getInstance();
       mLoginBtn=findViewById(R.id.loginBtn);
       mCreateBtn=findViewById(R.id.createText);
       forgotTextLink=findViewById(R.id.forgotPassword);

        waiting = new Dialog(Login.this);
        waiting.setContentView(R.layout.waiting_progressbar);
        waiting.setCancelable(true);
        waiting.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        waiting.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

       mLoginBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email=mEmail.getText().toString().trim();
               String password=mPassword.getText().toString().trim();

               if (TextUtils.isEmpty(email)){
                   mEmail.setError("Email is Required!");
                   return;
               }
               if (TextUtils.isEmpty(password)){
                   mPassword.setError("Password is Req");
               }
               if (password.length() < 6){
                   mPassword.setError("Password must be >= 6 Characters");
                   return;
               }
               waiting.show();

               //authenticate the user

               fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){

                           if(fAuth.getCurrentUser().isEmailVerified()){

                               Toast.makeText(Login.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(getApplicationContext(),MainActivity2.class));

                           }else {


                               Toast.makeText(Login.this, "Please verify your email", Toast.LENGTH_SHORT).show();

                           }

                           /*Toast.makeText(Login.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),MainActivity2.class));*/
                       }else{
                           Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                           waiting.cancel();
                       }
                   }
               });
           }
       });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                overridePendingTransition(android.R.anim.fade_in, R.anim.zoom);
            }
        });
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter your Email to Receive Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! Link Is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });

    }
}