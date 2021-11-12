package com.cactus.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.cactus.systems.UserInteractFacade;

import javax.inject.Inject;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText password;
    private Button signupButton;

    @Inject
    UserInteractFacade userInteractFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((CereusApplication) getApplicationContext()).appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        username = findViewById(R.id.newUsername);
        password = findViewById(R.id.newPassword);
        signupButton = findViewById(R.id.button);

        signupButton.setOnClickListener(view -> {
            String givenName = name.getText().toString();
            String givenUsername = username.getText().toString();
            String givenPassword = password.getText().toString();

            if (userInteractFacade.createUser(givenName, givenUsername, givenPassword)){
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(SignupActivity.this, "The username already exists", Toast.LENGTH_LONG).show();
            }
        });
    }
}