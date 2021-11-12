package com.cactus.ui;

import android.content.Intent;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button signupButton;

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signup);

        signupButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(view -> {
            String givenUsername = username.getText().toString();
            String givenPassword = password.getText().toString();

            if (userInteractFacade.login(givenUsername, givenPassword)){
                Intent intent = new Intent(MainActivity.this, DisplayingListsActivity.class);
                startActivity(intent);

            }else{
                Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_LONG).show();
            }
        });

    }
}