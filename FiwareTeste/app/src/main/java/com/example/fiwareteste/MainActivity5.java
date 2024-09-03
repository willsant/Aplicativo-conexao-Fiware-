package com.example.fiwareteste;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fiwareteste.databinding.ActivityMain5Binding;

public class MainActivity5 extends AppCompatActivity {

    private Button listarUsuarios, cadastrarUsuario, listarDispositivo, cadastarDispositivo, deletarDispositivo, deletarUsuario;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        cadastrarUsuario = findViewById(R.id.cadastroUsuario);
        cadastarDispositivo = findViewById(R.id.cadastrarDispositivos);
        listarUsuarios = findViewById(R.id.listarUsuarios);
        listarDispositivo = findViewById(R.id.listarDispositivos);


        listarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity5.this, MainActivity6.class);
                startActivity(i);
            }
        });

        cadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity5.this, MainActivity3.class);
                startActivity(i);
            }
        });

        listarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i = new Intent(MainActivity5.this, MainActivity2.class);
                i.putExtra("usuario","admin");
                startActivity(i);
            }
        });

        cadastarDispositivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(MainActivity5.this, MainActivity7.class);
                startActivity(i);
            }
        });

    }

}