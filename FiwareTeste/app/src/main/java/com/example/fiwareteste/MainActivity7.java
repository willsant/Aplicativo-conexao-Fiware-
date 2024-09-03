package com.example.fiwareteste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity7 extends AppCompatActivity {
    EditText entName, entType, devId, transp, endP, timeZ;
    Button cadDev;
    String baseUrl = "http://192.168.2.5:4041/iot/";
    RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        entName = findViewById(R.id.editEntity_name);
        entType = findViewById(R.id.editEntity_type);
        devId = findViewById(R.id.editDevice_id);
        transp = findViewById(R.id.editTransport);
        endP = findViewById(R.id.editEndpoint);
        timeZ = findViewById(R.id.editTimezone);
        cadDev = findViewById(R.id.cadastrarDevice);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        cadDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("device_id", devId.getText().toString());
                jsonObject.addProperty("entity_name", entName.getText().toString());
                jsonObject.addProperty("entity_type", entType.getText().toString());
                jsonObject.addProperty("transport", transp.getText().toString());
                jsonObject.addProperty("endpoint", endP.getText().toString());
                jsonObject.addProperty("timezone", timeZ.getText().toString());
                JsonArray atributes = new JsonArray();
                JsonObject attributes = new JsonObject();
                attributes.addProperty("object_id","c");
                attributes.addProperty("name", "count");
                attributes.addProperty("type", "Integer");
                atributes.add(attributes);
                //String atributos = '{ \"object_id\": \"c\", \"name\": \"count\", \"type\": \"Integer\" }';
                //atributes.add(atributos);
                jsonObject.add("attributes",atributes);
                JsonObject firstJ = new JsonObject();
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(jsonObject);
                firstJ.add("devices",jsonArray);
                cadastraDevice(firstJ);
            }
        });
    }

    private void cadastraDevice(JsonObject device){

        Call<JsonObject> call = retrofitInterface.createDevice(device);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity7.this, "DEU ERRO CADASTRO", Toast.LENGTH_SHORT).show();
                    Log.i("Deu Erro Device: ", response.message());
                    return;
                } else {
                    Toast.makeText(MainActivity7.this, "DEVICE CADASTRADO", Toast.LENGTH_SHORT).show();
                    Log.i("Device Cadastrado: ", response.message());
                    Intent intent = new Intent(MainActivity7.this, MainActivity5.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity7.this, "DEU ERRO CADASTRO 2", Toast.LENGTH_SHORT).show();
                Log.i("Deu Erro Device 2: ", t.getMessage());
            }
        });
    }


}