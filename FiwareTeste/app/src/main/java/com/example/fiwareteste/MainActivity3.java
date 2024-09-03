package com.example.fiwareteste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity3 extends AppCompatActivity {
    //Devices device;
    String baseUrl = "http://172.20.0.1:3005/";
    RetrofitInterface retrofitInterface;
    EditText name, password, email;
    Button cadastro;
    String nome, senha, emai;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        name = findViewById(R.id.CadastroName);
        password = findViewById(R.id.CadastroSenha);
        email = findViewById(R.id.CadastroEmail);
        cadastro = findViewById(R.id.buttonCadastro);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);


        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = name.getText().toString();
                senha = password.getText().toString();
                emai = email.getText().toString();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("username",nome);
                jsonObject.addProperty("email",emai);
                jsonObject.addProperty("password",senha);
                JsonObject user = new JsonObject();
                user.add("user",jsonObject);
                cadastroUsuario(user);
            }
        });

    }

    private void cadastroUsuario(JsonObject jsonObject){

        Call<JsonObject> call = retrofitInterface.createUser(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity3.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Erro Cadastro: ",response.message());
                    return;
                } else if (response.code() == 201) {
                    Toast.makeText(MainActivity3.this, "Cadastro Feito", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Cadastro: ",response.body().toString());
                    intent = new Intent(MainActivity3.this, MainActivity5.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity3.this, "DEU ERRO 2", Toast.LENGTH_SHORT).show();
                Log.i("Teste Erro Cadastro: ",t.getMessage());
            }
        });
    }


}