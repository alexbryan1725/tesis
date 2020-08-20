package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.R;

import java.util.ArrayList;

public class AdapterListaCP extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Parqueadero> items;


    public AdapterListaCP(Activity activity, ArrayList<Parqueadero> items){

        this.activity = activity;
        this.items = items;

    }



    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.viewcplistac, null);
        }
        Parqueadero dir = items.get(position);


        TextView txtNombres =(TextView) v.findViewById(R.id.nombreViewCP);

        TextView txtPlaza= (TextView) v.findViewById(R.id.plazaViewCP);
        TextView txtPrecio= (TextView) v.findViewById(R.id.preciovireCP);




        txtNombres.setText("Nombre: "+dir.getNombreParq().toString());

        txtPlaza.setText("Plaza: "+ (dir.getPlazaParq()-dir.getNumeroParq())+" / "+dir.getPlazaParq());
        txtPrecio.setText("$ "+dir.getFraccionParq());





        return v;
    }



}
