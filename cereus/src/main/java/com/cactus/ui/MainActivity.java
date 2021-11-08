package com.cactus.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button signupButton;
    //Private UserInteractFacade userFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signup);
        //userFacade = USERFACADE;

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String givenUsername = username.getText().toString();
                String givenPassword = password.getText().toString();

                Toast.makeText(MainActivity.this, "Logging in " + givenUsername, Toast.LENGTH_LONG).show();

//                if (userFacade.login.equals(true)){
//                    Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
//                    startActivity(intent);
//
//                }else{
//                    Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_LONG).show();
//                }


            }
        });

    }
}