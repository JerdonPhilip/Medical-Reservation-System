package com.example.covidfacres;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    TextView textView, forgotPass;
    EditText eMail,passWord;
    MaterialButton loginBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = findViewById(R.id.e_Mail);
        passWord = findViewById(R.id.passWord);
        mAuth = FirebaseAuth.getInstance();
        loginBtn = findViewById(R.id.loginBtn);
        forgotPass = findViewById(R.id.forgotPass);
        textView =(TextView)findViewById(R.id.signUp);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent= new Intent(login.this, signUp.class);
                startActivity(intent);
            }
        });



        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                EditText resetMail = new EditText(v.getContext());

                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter your Email to receive reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                String email = eMail.getText().toString().trim();
                String password = passWord.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    eMail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    passWord.setError("Password is Required");
                    return;

                }
                if (password.length() < 8 ){
                    passWord.setError("Password must be 8 characters long");
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(login.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });





            }
        });

    }

}