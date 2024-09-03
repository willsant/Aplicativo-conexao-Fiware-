package com.example.fiwareteste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class CustomUsuario extends BaseAdapter {
    private JsonArray jsonArray;
    private Context context;
    private int layout;

    public CustomUsuario(JsonArray jsonArray, Context context, int layout) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.layout = layout;
    }

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
        return 0;
    }

    private class ViewHolder{
        TextView idtxt,titletxt,bodytxt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CustomUsuario.ViewHolder viewHolder = new CustomUsuario.ViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout,null);

        viewHolder.idtxt = convertView.findViewById(R.id.idtxt);
        viewHolder.titletxt = convertView.findViewById(R.id.titletxt);
        viewHolder.bodytxt = convertView.findViewById(R.id.bodytxt);

        JsonObject usuarios = (JsonObject) jsonArray.get(position);
        //JsonObject device = new JsonObject();
        Usuario usuario = new Usuario();
        usuario.setId(usuarios.get("id").toString());
        usuario.setName(usuarios.get("username").toString());
        usuario.setEmail(usuarios.get("email").toString());

        //Devices device = jsonArray.get(position);
        viewHolder.idtxt.setText("ID: " + usuario.getId());
        viewHolder.titletxt.setText("Name: " + usuario.getName());
        viewHolder.bodytxt.setText("Email: " + usuario.getEmail());

        return convertView;
    }
}
