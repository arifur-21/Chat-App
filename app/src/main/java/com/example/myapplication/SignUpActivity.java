package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.example.myapplication.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String id;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();




        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Create Account");
        progressDialog.setMessage("we're creating your account");

        binding.alreadyAccountIdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        binding.signUpId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* if (TextUtils.isEmpty(binding.userNameId.getText().toString()))
                {
                    binding.userNameId.setError("enter your name");
                }
                else if (TextUtils.isEmpty(binding.emailId.getText().toString()))
                {
                    binding.emailId.setError("enter your name");
                }
                else if (TextUtils.isEmpty(binding.passwordId.getText().toString()))
                {
                    binding.passwordId.setError("enter your name");
                }*/

                progressDialog.show();

                auth.createUserWithEmailAndPassword(binding.emailId.getText().toString(), binding.passwordId.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful())
                                {
                                    Users users = new Users();
                                  final String user =  binding.userNameId.getText().toString();
                                     final String email = binding.emailId.getText().toString().trim();
                                        final String password = binding.passwordId.getText().toString();
                                   /* if (email.isEmpty())
                                    {
                                        binding.emailId.setError("Enter your name");
                                        return;
                                    }
                                    else if (user.isEmpty())
                                    {
                                        binding.userNameId.setError("Enter your name");
                                        return;
                                    }
                                    else if (password.isEmpty())
                                    {
                                        binding.passwordId.setError("Enter your name");
                                        return;
                                    }*/

                                    String id = task.getResult().getUser().getUid();
                                    database.getReference().child("Users").child(id).setValue(users);
                                    Toast.makeText(SignUpActivity.this, "Sign Up Successfull", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(SignUpActivity.this, "Sign Up Failed"+task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
            }
        });

    }
}