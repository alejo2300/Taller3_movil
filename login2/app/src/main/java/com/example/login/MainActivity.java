package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText etUserName, etPassword;
    Button buttonLgn, butttonSignIn;

    //open firebase database
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUserName = findViewById(R.id.username);
        etPassword = findViewById(R.id.userpassword);
        buttonLgn = findViewById(R.id.login);
        butttonSignIn = findViewById(R.id.signin);

        //Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        Intent intentService = new Intent(MainActivity.this, AvailablesListenerService.class);
        startService(intentService);

        if(user != null){ //check if user is logged in
            Intent intent = new Intent(MainActivity.this, mapAndMenu.class);
            startActivity(intent);
        }else {

            butttonSignIn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            });

            buttonLgn.setOnClickListener(v -> {
                String username = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (validateUser(username) && validatePassword(password)) {
                    mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "User logged", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, mapAndMenu.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Data fields validation failed", Toast.LENGTH_LONG).show();
                }
                //Toast.makeText(this, "Username: " + username + " Password: " + password, Toast.LENGTH_LONG).show();
            });
        }
    }

    private void updateUI(FirebaseUser userreg) {
        if(userreg != null){
            Intent intent = new Intent(MainActivity.this, mapAndMenu.class);
            startActivity(intent);
        }else {
            etUserName.setText("");
            etPassword.setText("");
        }
    }

    private boolean validatePassword(String password) {
        if(password.isEmpty()) {
            etPassword.setError("Password is required");
            Toast.makeText(this, "Password is required", Toast.LENGTH_LONG).show();
            return false;
        }else{
            if(password.length() >= 6){
                return true;
            }return false;
        }
    }

    private boolean validateUser(String username) {
        if(username.isEmpty()) {
            etUserName.setError("Username is required");
            Toast.makeText(this, "Username is required", Toast.LENGTH_LONG).show();
            return false;
        }else{
            if(username.contains("@") && username.contains(".")){
                return true;
            }return false;
        }
    }
}