package com.example.fiwareteste;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.fiwareteste.databinding.ActivityMain2Binding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.fiwareteste.databinding.ActivityMain6Binding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity6 extends AppCompatActivity {

    private ListView lv;
    private ActivityMain6Binding binding;
    String baseUrl = "http://172.20.0.1:3005/";
    RetrofitInterface retrofitInterface;
    AlertDialog.Builder alert;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    RetrofitInterface retrofitInterfaceSubs;
    WebSocket webSocket;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain6Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lv = findViewById(R.id.listUsuarios);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        displayFiwareData();

        binding.listUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Object objeto = parent.getAdapter().getItem(position);
                    String user = objeto.toString();
                    Log.i("Teste Alerta: ", user);
                    JSONObject object = new JSONObject(user);
                    user = object.get("id").toString();
                    String userName = object.get("username").toString();
                    Log.i("Teste List: ", user);
                    alert = new AlertDialog.Builder(MainActivity6.this);
                    String finalUser = user;
                    alert.setTitle(userName)
                            .setMessage("O que deseja fazer?")
                            .setCancelable(true)
                            .setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Log.i("teste alerta 1", "Olhar");
                                    deleteUser(finalUser);
                                    Intent in = new Intent(MainActivity6.this, MainActivity5.class);
                                    startActivity(in);
                                }
                            })
                            .setNeutralButton("Voltar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Intent in = new Intent(MainActivity6.this, MainActivity5.class);
                                    startActivity(in);
                                }
                            })
                            .setNegativeButton("Atualizar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent in = new Intent(MainActivity6.this, MainActivity8.class);
                                    in.putExtra("id",finalUser);
                                    startActivity(in);
                                }
                            })
                            .show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void displayFiwareData(){

        Call<JsonObject> call = retrofitInterface.getUsers();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity6.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Erro 4: ",response.message());
                    return;
                }
                JsonObject object = response.body();
                JsonArray lista = (JsonArray) object.get("users");
                JsonArray usuarios = new JsonArray();
                for (int i = 0; i < lista.size(); i++) {
                    JsonObject usuario = (JsonObject) lista.get(i);
                    //JsonObject atributo = (JsonObject) device.get("attributes");
                    Usuario usua = new Usuario();
                    usua.setEmail(usuario.get("email").toString());
                    /*if (!devices1.getBody().equals("[]")){
                        devices.add(device);
                    }*/
                    usuarios.add(usuario);

                }
                CustomUsuario custom = new CustomUsuario(usuarios, MainActivity6.this, R.layout.singleview);
                lv.setAdapter(custom);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity6.this, "DEU ERRO 2", Toast.LENGTH_SHORT).show();
                Log.i("Teste Erro 4 2: ",t.getMessage());
            }
        });
    }

    private void deleteUser(String userId){
        Call<JsonObject> call = retrofitInterface.deleteUser(userId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity6.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
                    Log.i("Teste Delete User ",response.message());
                    return;
                } else {
                    Toast.makeText(MainActivity6.this, "Usuario deletado", Toast.LENGTH_SHORT).show();
                    Log.i("Usuario deletado: ",response.message());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity6.this, "DEU ERRO Delete User", Toast.LENGTH_SHORT).show();
                Log.i("Teste Delete Erro: ",t.getMessage());
            }
        });

    }

}