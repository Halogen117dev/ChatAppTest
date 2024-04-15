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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    EditText userEmail, userPassword;
    TextView signInButton, registerButton;
    String email, password;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        userEmail = findViewById(R.id.emailText);
        userPassword = findViewById(R.id.passwordText);
        signInButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.signup);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString().trim();
                password = userPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email))
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
                SignIn();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void SignIn()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra("name", username);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthInvalidUserException)
                {
                    Toast.makeText(SignInActivity.this, "User does not exist!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SignInActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
    }
}