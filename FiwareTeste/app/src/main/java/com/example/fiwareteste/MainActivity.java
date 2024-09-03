package com.example.fiwareteste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private EditText nome, senha;
    private Button button, button2;
    String baseUrl = "http://192.168.1.76:4041/iot/";
    String baseUrlUser = "http://192.168.2.7:3005/";
    RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.botao);
        button2 = findViewById(R.id.botao2);
        nome = findViewById(R.id.Name);
        senha = findViewById(R.id.Password);
       // usuario = new UsuarioDAO(this);

        //Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlUser)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = nome.getText().toString();
                String passw = senha.getText().toString();

                if(name.equals("") || passw.equals("")){
                    Toast.makeText(MainActivity.this, "Entre com usuario ou senha", Toast.LENGTH_SHORT).show();
                } else {
                    checkNameAndPassword(name, passw);
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MainActivity2.class);
                Log.i("Teste User: ", "AQUI");
                i.putExtra("usuario", "usuario");
                startActivity(i);
            }
        });

    }

    private void checkNameAndPassword(String name, String password){

        final boolean[] resposta = {false};
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("password", password);
        Call<JsonObject> call = retrofitInterface.getPermission(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.code() == 401){
                    Toast.makeText(MainActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    return;
                } else if (response.code() == 201){
                    /*if(name.equals("alice-the-admin@test.com")){
                        Intent i = new Intent(MainActivity.this, MainActivity5.class);
                        Log.i("Teste Admin: ", "AQUI");
                        //i.putExtra("usuario", user);
                        startActivity(i);
                    } else{
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        Log.i("Teste User: ", "AQUI");
                        i.putExtra("usuario", "usuario");
                        startActivity(i);
                    }*/
                    Intent i = new Intent(MainActivity.this, MainActivity5.class);
                    Log.i("Teste Admin: ", "AQUI");
                    //i.putExtra("usuario", user);
                    startActivity(i);


                }
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
                    Log.i("Deu erro 1: ", response.message());
                    Log.i("Deu erro 1 2: ", ""+ response.code());
                    return;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("Deu erro 1: ", t.getMessage());
            }
        });
    }
}