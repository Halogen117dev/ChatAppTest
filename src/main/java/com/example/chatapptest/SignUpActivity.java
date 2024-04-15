package com.example.chatapptest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText userName, userEmail, userPassword;
    TextView signInButton, signUpButton;
    String email, password, name;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        userName = findViewById(R.id.usernameText);
        userEmail = findViewById(R.id.emailText);
        userPassword = findViewById(R.id.passwordText);
        signInButton = findViewById(R.id.login);
        signUpButton = findViewById(R.id.signup);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = userName.getText().toString().trim();
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();

                if(TextUtils.isEmpty(name))
                {
                    userName.setError("Please enter your username!");
                    userName.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(email))
                {
                    userEmail.setError("Please enter your email!");
                    userEmail.requestFocus();
                    return;
                }
                else if(TextUtils.isEmpty(password))
                {
                    userPassword.setError("Please enter your password!");
                    userPassword.requestFocus();
                    return;
                }
                SignUp();

            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SignUp()
    {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.updateProfile(userProfileChangeRequest);

                UserModel userModel = new UserModel(FirebaseAuth.getInstance().getUid(), name, email, password);
                databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(SignUpActivity.this, "Sign up fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }
}