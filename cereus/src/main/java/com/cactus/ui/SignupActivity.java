package com.cactus.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SignupActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText password;
    private Button signupButton;
    // Private UserInteractFacade userFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name);
        username = findViewById(R.id.newUsername);
        password = findViewById(R.id.newPassword);
        signupButton = findViewById(R.id.signupButton);
        //userFacade = USERFACADE;

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String givenName = name.getText().toString();
                String givenUsername = username.getText().toString();
                String givenPassword = password.getText().toString();

                Toast.makeText(SignupActivity.this, "Registering in " + givenUsername, Toast.LENGTH_LONG).show();

//                if (userFacade.createUser.equals(true)){
//                    Intent intent = new Intent(SignupActivity.this, OptionsActivity.class);
//                    startActivity(intent);
//
//                }else{
//                    Toast.makeText(SignupActivity.this, "The username already exists", Toast.LENGTH_LONG).show();
//                }
            }
        });
    }
}