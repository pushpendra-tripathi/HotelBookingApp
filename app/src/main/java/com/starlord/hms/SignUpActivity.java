package com.starlord.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    EditText email, password, userName;
    Button signUp, login;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.edtCustomerEmailSignUp);
        password = findViewById(R.id.edtCustomerPasswordSignUp);
        userName = findViewById(R.id.edtUserName);
        signUp = findViewById(R.id.btnCustomerSignUp);
        login = findViewById(R.id.btnCustomerSignIn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailValue = email.getText().toString();
                final String userNameValue = userName.getText().toString();
                final String passwordValue = password.getText().toString();
                if (TextUtils.isEmpty(emailValue)){
                    email.setError("Required field");
                    return;}
                if (TextUtils.isEmpty(passwordValue)){
                    password.setError("Required field");
                    return;}
                if (TextUtils.isEmpty(userNameValue)){
                    userName.setError("Required field");
                    return;}

                signUp.setEnabled(false);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = new AppDatabase(SignUpActivity.this);
                        int valid = db.searchUser(emailValue,passwordValue);
                        if (valid == 0){
                            int customerId = db.numberOfColumns("Customer");
                            db.insertCustomer(emailValue,customerId,userNameValue,passwordValue,1);
                            startActivity(new Intent(getApplicationContext(),NavigationActivity.class));
                            Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            signUp.setEnabled(true);
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            signUp.setEnabled(true);
                            Toast.makeText(SignUpActivity.this, "User with this email id, already exists", Toast.LENGTH_LONG).show();
                        }
                    }
                },3000);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}
