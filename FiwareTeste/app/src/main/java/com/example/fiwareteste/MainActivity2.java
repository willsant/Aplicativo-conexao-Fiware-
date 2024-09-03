package com.example.fiwareteste;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fiwareteste.databinding.ActivityMain2Binding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {

    private TextView textView;
    private ListView lv;
    private ActivityMain2Binding binding;
    Context context;
    Binder binder;
    int id;
    String baseUrl = "http://192.168.2.7:4041/iot/";
    //String baseUrlSubscription = "http://192.168.1.76:1026/v2/subscriptions/";
    String baseUrlSubscription = "http://192.168.2.7:1026/v2/subscriptions/";
    //String baseUrlListener = "ws://192.168.2.6" +
            //":3000/app/monitor/websocket";
    String baseUrlListener = "ws://192.168.2.7" +
            ":8080";
    //String baseUrlListener = "wss://demo.piesocket.com/v3/1?api_key=VCXCEuvhGcBDP7XhiJJUDvR1e1D3eiVjgZ9VRiaV&notify_self";
    //String baseUrlListener = "ws://192.168.1.76:3000/app/store/urn:ngsi-ld:Store:001";
    RetrofitInterface retrofitInterface;
    AlertDialog.Builder alert;
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    RetrofitInterface retrofitInterfaceSubs;
    WebSocket webSocket;
    Intent intent;
    String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_main2);
        setContentView(binding.getRoot());

        //textView = findViewById(R.id.textoResultado);
        lv = findViewById(R.id.lv);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Retrofit retrofitSubs = new Retrofit.Builder()
                .baseUrl(baseUrlSubscription)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterfaceSubs = retrofitSubs.create(RetrofitInterface.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Notificação", "Notificação", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        intent = getIntent();
        level = intent.getStringExtra("usuario");

        displayFiwareData();
        displaySocketConection();
        binding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    //displaySocketConection();
                    Object objeto= parent.getAdapter().getItem(position);
                    String dvc = objeto.toString();
                    JSONObject object = new JSONObject(dvc);
                    dvc = object.get("entity_name").toString() + "-" + object.get("entity_type").toString();
                    dvc = dvc.replaceAll(":","-");
                    dvc = dvc.toLowerCase();
                    Log.i("Teste List: ", dvc);
                    alert = new AlertDialog.Builder(MainActivity2.this);
                    String finalDevice = dvc;
                    String nameDevice = object.get("device_id").toString();
                    alert.setTitle(object.get("device_id").toString())
                            .setMessage("Qual a sua intenção? ")
                            .setCancelable(true)
                            .setPositiveButton("Gráfico", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Log.i("teste alerta 1", "Olhar");
                                    Intent in = new Intent(MainActivity2.this, MainActivity4.class);
                                    in.putExtra("device", finalDevice);
                                    startActivity(in);
                                }
                            })
                            .setNeutralButton("Deletar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(level.equals("admin")){
                                        deleteDevice(nameDevice);
                                        Intent in = new Intent(MainActivity2.this, MainActivity5.class);
                                        startActivity(in);
                                    } else {
                                        Toast.makeText(MainActivity2.this, "Usuário sem permissão", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("Atualizar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(level.equals("admin")){
                                        Intent in = new Intent(MainActivity2.this, MainActivity9.class);
                                        in.putExtra("device", nameDevice);
                                        startActivity(in);
                                    } else {
                                        Toast.makeText(MainActivity2.this, "Usuário sem permissão", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        //displaySocketConection();


        /*try {
            notificationFiwareData(serverSocket,socket);
            //notificationFiware(serverSocket,socket);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    private void displaySocketConection()  {
        OkHttpClient okHttpClient = new OkHttpClient();
        IotListener iotListener = new IotListener(this);
        //Request request1 = new Request.Builder().url(baseUrlListener).addHeader("Content-Length","<calculated when request is sent>").build();
        Request request1 = new Request.Builder().url(baseUrlListener).addHeader("Connection","Keep-alive").build();
        Log.i("teste", "mensagem listener 2\n" + request1);

        webSocket = okHttpClient.newWebSocket(request1, iotListener);
        //Log.i("teste", "mensagem listener 3\n");
        /*webSocket = new WebSocket.Factory() {
            @Override
            public WebSocket newWebSocket(Request request, WebSocketListener listener) {
                Log.i("teste", "mensagem certa");
                request = request1;
                listener = iotListener;
                return newWebSocket(request,listener);
            }
        }.newWebSocket(request1, iotListener);*/

    }


    public void notificationFiwareData(String resposta){
        //String subsBody = "";
        //retrofitInterfaceSubs.setSubscription(subsBody);
        //socket = serverSocket.accept();
        //dataInputStream = new DataInputStream(socket.getInputStream());
        /*String response = resposta;
        String[] id = response.split("device_id");
        String[] entity_type = response.split("entity_type");
        String[] attributes = response.split("attributes");*/
        try {
            JSONObject firstJson = new JSONObject(resposta);
            JSONArray jsonArray = firstJson.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                notificationJson(jsonArray.get(i).toString());
            }
            //Log.i("teste alerta", "Alerta criado");
            //dataInputStream.close();
            //socket.close();
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void notificationJson(String resposta){

        try {
            JSONObject jsonObject = new JSONObject(resposta);
            Devices device = new Devices();
            device.setId(jsonObject.get("id").toString());
            device.setType(jsonObject.get("type").toString());
            JSONObject innerJson = jsonObject.getJSONObject("Medicao");
            device.setBody(innerJson.get("value").toString());
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity2.this, "Notificação");
            builder.setContentTitle(device.getId());
            builder.setContentText(device.getBody());
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setAutoCancel(true);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity2.this);
            managerCompat.notify(1, builder.build());
            Log.i("teste notificação", "Notificação criada");

            /*alert = new AlertDialog.Builder(getBaseContext());
            alert.setTitle(device.getId())
                    .setMessage(device.getBody())
                    .setCancelable(true)
                    .setPositiveButton("Visto", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Log.i("teste alerta 1", "Olhar");
                            dialogInterface.cancel();
                        }
                    })
                    .setNeutralButton("Grafico", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(MainActivity2.this, MainActivity4.class);
                            startActivity(i);
                            dialog.cancel();
                        }
                    })
                    .show();*/
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void displayFiwareData() {
        Call<JsonObject> call = retrofitInterface.getDevices();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "DEU ERRO 3", Toast.LENGTH_SHORT).show();
                    textView.setText("Code: " + response.code());
                    textView.setText(response.message());
                    return;
                }

                //Toast.makeText(MainActivity.this, "DEU ERRO 2", Toast.LENGTH_SHORT).show();
                JsonObject object = response.body();
                JsonArray lista = (JsonArray) object.get("devices");
                JsonArray devices = new JsonArray();
                for (int i = 0; i < lista.size(); i++) {
                    JsonObject device = (JsonObject) lista.get(i);
                    //JsonObject atributo = (JsonObject) device.get("attributes");
                    Devices devices1 = new Devices();
                    //Log.i("Teste Device: ", device.get("entity_type").getAsString());
                    devices1.setType(device.get("entity_type").toString());
                    if (device.get("entity_type").getAsString().equals("Thing")){
                        devices.add(device);
                    }
                    //devices.add(device);
                }
                Custom custom = new Custom(devices, MainActivity2.this, R.layout.singleview);
                lv.setAdapter(custom);
                //String resp = devices.toString();
                //String resp = response.body().toString();
                String content = "";
                /*for (int i = 0; i < lista.size(); i++) {
                    JsonObject device = (JsonObject) lista.get(i);
                    Devices devices = new Devices();
                    devices.setId(device.get("device_id").toString());
                    devices.setType(device.get("entity_type").toString());
                    devices.setBody(device.get("attributes").toString());
                    textView.append(devices.toString());
                    //content += "ID: " + lista.get("device_id") + "\n";
                    // content += "User ID: " + lista.get(i).getAsString() + "\n";
                    //content += "Title: " + devices.getBody() + "\n";
                    content += "\n";
                }*/

                //textView.append(content);

                //textView.append(object.get("devices").toString());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity2.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
                textView.setText(t.getMessage());
            }
        });
    }


    private void deleteDevice(String device){

        Call<JsonObject> call = retrofitInterface.deleteDevice(device);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "Deu Erro Delete Device", Toast.LENGTH_SHORT).show();
                    Log.i("Delete Device Erro: ",response.message());
                    return;
                } else{
                    Toast.makeText(MainActivity2.this, "Device Deletado", Toast.LENGTH_SHORT).show();
                    Log.i("Device Deletado: ",response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public class IotListener extends WebSocketListener{
        MainActivity2 mainActivity2;

        public IotListener(MainActivity2 mainActivity2) {
            Log.i("teste", "mensagem listener");
            this.mainActivity2 = mainActivity2;
        }

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            super.onOpen(webSocket, response);
            webSocket.send("Hello, Will");

            Log.i("teste", "mensagem certa 2");
            /*mainActivity2.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("teste", "mensagem certa");
                    Toast.makeText(mainActivity2, "Conexão aberta", Toast.LENGTH_SHORT).show();
                }
            });*/

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.i("teste recepção",text);
            //mainActivity2.notificationFiwareData(text);

            /*Thread thread = new Thread(new ClientThread());
            //Toast.makeText(MainActivity2.this, "DEU ERRO", Toast.LENGTH_SHORT).show();
            thread.start();
            ClientThread clientThread = new ClientThread();
            clientThread.run();*/
            mainActivity2.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity2.notificationFiwareData(text);
                }
            });
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.i("teste", "mensagem fechada");
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.i("teste", "mensagem fechada 2");
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
            super.onFailure(webSocket, t, response);

            Log.i("teste", "mensagem listener falhou " + t.getMessage().toLowerCase(Locale.ROOT));
        }
    }
}


class ClientThread implements Runnable{
    Socket socket ;
    ServerSocket serverSocket;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    Handler handler = new Handler();
    String notificação;
    MainActivity2 mainActivity2 = new MainActivity2();

    @Override
    public void run() {
        try {
            //Toast.makeText(mainActivity2, "DEU ERRO", Toast.LENGTH_SHORT).show();
            serverSocket = new ServerSocket(6000);
            Log.i("teste", "mensagem de teste");
            //socket = new Socket("192.168.1.76",3000);
            while (true) {

                socket = serverSocket.accept();
                Log.i("teste2", "mensagem de teste 2");
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                notificação = bufferedReader.readLine();
                //Log.i("teste3", "mensagem de teste 3");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity2.notificationFiwareData(notificação);
                    }
                });

                //JSONArray jsonArray = new JSONArray(notificação);
                /*JSONObject jsonObject = new JSONObject(notificação);
                Devices device = new Devices();
                device.setId(jsonObject.get("id").toString());
                device.setType(jsonObject.get("type").toString());
                device.setBody(jsonObject.get("value").toString());
                NotificationCompat.Builder builder = new NotificationCompat.Builder(mainActivity2, "Notificação");
                builder.setContentTitle(device.getId());
                builder.setContentText(device.getBody());
                builder.setSmallIcon(R.drawable.ic_launcher_background);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(mainActivity2);
                managerCompat.notify(1, builder.build());

                AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity2);
                alert.setTitle(device.getId())
                        .setMessage(device.getBody())
                        .setCancelable(true)
                        .setPositiveButton("Visto", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                //dataInputStream.close();*/
                socket.close();

            }

        } catch (IOException e){
            //Toast.makeText(mainActivity2, "DEU ERRO", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }



    }
}

