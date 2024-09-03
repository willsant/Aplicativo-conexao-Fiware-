package com.example.fiwareteste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity4 extends AppCompatActivity {
    Button botao;
    String baseUrlElastic = "http://192.168.2.7:9200/";
    RetrofitInterface retrofitInterface;
    Intent intent;
    String device;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        botao = findViewById(R.id.Voltar);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlElastic)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        intent = getIntent();
        device = intent.getStringExtra("device");
        Date data = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        String dataForQuery = dateFormat.format(data);
        //String query = " select attrValue from  \"cygnus-openiot--" + device +'-' + dataForQuery + "\" where attrType='Integer' ";
        //String query = "{\"query\": \" select recvTime, attrValue from \\\"cygnus-openiot--urn-ngsi-ld-motion-001-motion-2023.11.23\\\" \"}";
        String query = " select attrValue from  \"cygnus-openiot--urn-ngsi-ld-thing-008-thing-2024.08.15\" where attrType='Integer' ";
        Log.i("Teste Query: ", query);
        //String query2 = " select recvTime, attrValue from \"cygnus-openiot--urn-ngsi-ld-motion-001-motion-2023.11.23\" ";
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("query",query);
            displayFiwareData(jsonObject);
        } catch (JsonIOException e) {
            Log.i("Deu erro 6: ", e.getMessage());
            e.printStackTrace();
        }


        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity4.this, MainActivity2.class);
                startActivity(i);
            }
        });

    }

    private void displayFiwareData(JsonObject query){
        //String verificar = jsonObject.toString();
        Log.i("Teste Verificação: ", query.toString());
        Call<JsonObject> call = retrofitInterface.getValues(query);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(MainActivity4.this, "DEU ERRO 4", Toast.LENGTH_SHORT).show();
                    Log.i("Deu erro 4: ", response.message());
                    Log.i("Deu erro 4 2: ", ""+ response.code());
                    return;
                }
                String vx = "";
                String vy = "";
                String resposta = response.body().toString();

                JsonParser jsonParser = new JsonParser();
                JsonObject object = response.body();
                //JsonArray lista = (JsonArray) jsonParser.parse(String.valueOf(response.body()));
                JsonArray lista = (JsonArray) object.get("rows");
                int[] valoresx = new int[lista.size()];
                vx = valoresx.length + "";
                Log.i("tamanho x: ", valoresx.length + "");
                for (int i = 0; i < lista.size(); i++) {
                    Log.i("Teste Resposta 4: ",lista.get(i).toString());
                    JsonArray lista2 = (JsonArray) lista.get(i);
                    valoresx[i] = lista.get(i).getAsInt();
                    //JsonArray lista = (JsonArray) object.get("rows");
                    Log.i("Teste Resposta 4: ",lista2.get(0).toString());
                    //JsonObject value = (JsonObject) lista2.get(0);
                    //vx += value.toString();
                }
                Log.i("Teste Gráfico: ", vx);
                drawGraph(valoresx);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("Deu erro 5: ", t.getMessage());
            }
        });
    }

    private void drawGraph(int[] points){
        /*DataPoint[] dataPoints;
        for (int i = 0; i < values.length; i++) {
            /*try{
                
            } catch (ioe){

            }
        }*/

        GraphView graph = (GraphView) findViewById(R.id.graph);
        DataPoint[] dataPoints = new DataPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            dataPoints[i] = new DataPoint(i,points[i]);
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                /*new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
        });*/
        graph.addSeries(series);

        series.setDrawDataPoints(true);
        graph.setTitle(device);
    }
}