package com.example.fiwareteste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

public class Custom extends BaseAdapter {

    private JsonArray jsonArray;
    private Context context;
    private int layout;
    //private JsonObject jsonObject;

    public Custom(JsonArray jsonArray, Context context, int layout) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.layout = layout;
    }
    /*public Custom(JsonObject jsonObject, Context context, int layout) {
        this.jsonObject = jsonObject;
        this.context = context;
        this.layout = layout;
    }*/

    @Override
    public int getCount() {
        return jsonArray.size();
    }

    @Override
    public Object getItem(int position) {
        return jsonArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        TextView idtxt,titletxt,bodytxt;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(layout,null);

        viewHolder.idtxt = view.findViewById(R.id.idtxt);
        viewHolder.titletxt = view.findViewById(R.id.titletxt);
        viewHolder.bodytxt = view.findViewById(R.id.bodytxt);

        JsonObject device = (JsonObject) jsonArray.get(position);
        //JsonObject device = new JsonObject();
        Devices devices = new Devices();
        devices.setId(device.get("device_id").toString());
        devices.setType(device.get("entity_type").toString());
        devices.setBody(device.get("attributes").toString());

        //Devices device = jsonArray.get(position);
        viewHolder.idtxt.setText("ID: " + devices.getId());
        viewHolder.titletxt.setText("Title: " + devices.getType());
        viewHolder.bodytxt.setText("Body: " + devices.getBody());

        return view;
    }
}
