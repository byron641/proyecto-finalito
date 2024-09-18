package com.example.proyectofinalv2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Create extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button createButton;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        dbHelper = new MyDatabaseHelper(this);

        usernameEditText = findViewById(R.id.Usertxt); // Asegúrate de que el id coincide con el XML
        passwordEditText = findViewById(R.id.PasswordTxt); // Asegúrate de que el id coincide con el XML
        createButton = findViewById(R.id.BtnCrearUsuario);
        clearButton = findViewById(R.id.BtnLimpiarUsuario);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Create.this, "Por favor ingresa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.addUser(username, password);
                    if (isInserted) {
                        Toast.makeText(Create.this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Create.this, Login.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Create.this, "Error al crear usuario", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText.setText("");
                passwordEditText.setText("");
            }
        });
    }
}