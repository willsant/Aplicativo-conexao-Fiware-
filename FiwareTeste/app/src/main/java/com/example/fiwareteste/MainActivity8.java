package com.example.fiwareteste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity8 extends AppCompatActivity {
    EditText name, password, email;
    Button atualizarUsuario;
    Intent intent;
    String baseUrl = "http://172.20.0.1:3005/";
    RetrofitInterface retrofitInterface;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);

        name = findViewById(R.id.upName);
        password = findViewById(R.id.upSenha);
        email = findViewById(R.id.upEmail);
        atualizarUsuario = findViewById(R.id.upUser);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        intent = getIntent();
        id = intent.getStringExtra("id");


        atualizarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                if (!name.getText().toString().equals("")){
                    jsonObject.addProperty("username",name.getText().toString());
                }
                if (!email.getText().toString().equals("")){
                    jsonObject.addProperty("email",email.getText().toString());
                }
                if (!password.getText().toString().equals("")){
                    jsonObject.addProperty("username",password.getText().toString());
                }
                if (name.getText().toString().equals("") && email.getText().toString().equals("") && password.getText().toString().equals("")){
                    Toast.makeText(MainActivity8.this, "Preencha o campo que deseja atualizar", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObject finalJson = new JsonObject();
                    finalJson.add("user",jsonObject);
                    setAtualizarUsuario(id, finalJson);
                }
            }
        });

    }

    private void setAtualizarUsuario(String id, JsonObject body){

        Call<JsonObject> call = retrofitInterface.updateUser(id,body);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity8.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Erro Update: ",response.message());
                    return;
                } else{
                    Toast.makeText(MainActivity8.this, "Update Feito", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Update: ",response.body().toString());
                    intent = new Intent(MainActivity8.this, MainActivity5.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity8.this, "DEU ERRO 2", Toast.LENGTH_SHORT).show();
                Log.i("Teste Erro Update: ",t.getMessage());
            }
        });
    }

}