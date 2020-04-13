package com.starlord.hms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.Inet4Address;
import java.util.prefs.Preferences;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login, createAccount;
    ProgressDialog progressDialog;
    AppDatabase db = new AppDatabase(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (db.searchSession()==1){
            startActivity(new Intent(this,NavigationActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edtCustomerEmailLogin);
        password = findViewById(R.id.edtCustomerPasswordLogin);
        login = findViewById(R.id.btnCustomerLogin);
        createAccount = findViewById(R.id.btnCustomerCreate);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailValue = email.getText().toString();
                final String passwordValue = password.getText().toString();
                if (TextUtils.isEmpty(emailValue)){
                    email.setError("Required field");
                    return;}
                if (TextUtils.isEmpty(passwordValue)){
                    password.setError("Required field");
                    return;}
                login.setEnabled(false);
                progressDialog.show();

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = new AppDatabase(LoginActivity.this);
                        int valid = db.searchUser(emailValue,passwordValue);
                        if (valid == 0){
                            Toast.makeText(getApplicationContext(), "Wrong email id or password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            login.setEnabled(true);
                        }
                        else{
                            db.loginSession(emailValue,passwordValue);
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            login.setEnabled(true);
                            startActivity(new Intent(getApplicationContext(),NavigationActivity.class));
                            finish();
                        }
                    }
                },2000);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
    }
}
