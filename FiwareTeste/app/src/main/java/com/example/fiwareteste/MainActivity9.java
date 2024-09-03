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

public class MainActivity9 extends AppCompatActivity {
    EditText devName, devType, devId, devTransp, devEndP, devTimeZ;
    Button upDev;
    String baseUrl = "http://172.20.0.1:4041/iot/";
    RetrofitInterface retrofitInterface;
    Intent intent;
    String device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main9);

        devName = findViewById(R.id.upEntity_name);
        devType = findViewById(R.id.upEntity_type);
        devId = findViewById(R.id.upDevice_id);
        devTransp = findViewById(R.id.upTransport);
        devEndP = findViewById(R.id.upEndpoint);
        devTimeZ = findViewById(R.id.upTimezone);
        upDev = findViewById(R.id.upDevice);

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
        device = intent.getStringExtra("device");

        upDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                if (!devId.getText().toString().equals("")){
                    jsonObject.addProperty("device_id",devId.getText().toString());
                }
                if (!devName.getText().toString().equals("")){
                    jsonObject.addProperty("entity_name",devName.getText().toString());
                }
                if (!devType.getText().toString().equals("")){
                    jsonObject.addProperty("entity_type",devType.getText().toString());
                }
                if (!devTransp.getText().toString().equals("")){
                    jsonObject.addProperty("transport",devTransp.getText().toString());
                }
                if (!devEndP.getText().toString().equals("")){
                    jsonObject.addProperty("endpoint",devEndP.getText().toString());
                }
                if (!devTimeZ.getText().toString().equals("")){
                    jsonObject.addProperty("timezone",devTimeZ.getText().toString());
                }

                if (devId.getText().toString().equals("") && devName.getText().toString().equals("") && devType.getText().toString().equals("")
                && devTransp.getText().toString().equals("") && devEndP.getText().toString().equals("") && devTimeZ.getText().toString().equals("")){
                    Toast.makeText(MainActivity9.this, "Preencha o campo que deseja atualizar", Toast.LENGTH_SHORT).show();
                } else {
                    updateDevice(device, jsonObject);
                }
            }
        });
    }

    private void updateDevice(String device, JsonObject jsonObject){

        Call<JsonObject> call = retrofitInterface.updateDevice(device,jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity9.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Erro Update: ",response.message());
                    return;
                } else{
                    Toast.makeText(MainActivity9.this, "Update Feito", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Update: ",response.body().toString());
                    intent = new Intent(MainActivity9.this, MainActivity5.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity9.this, "DEU ERRO 2", Toast.LENGTH_SHORT).show();
                Log.i("Teste Erro Update: ",t.getMessage());
            }
        });

    }

}