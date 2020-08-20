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

public class AdapterListaParq extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Parqueadero> items;


    public AdapterListaParq(Activity activity, ArrayList<Parqueadero> items){

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
            v = inf.inflate(R.layout.viewlistaparq, null);
        }
        Parqueadero dir = items.get(position);

        TextView txtId = (TextView) v.findViewById(R.id.ParqListId);
        TextView txtNombres =(TextView) v.findViewById(R.id.ParqListNom);
        TextView txtTelefono = (TextView) v.findViewById(R.id.ParqListTel);
        TextView txtDireccion = (TextView) v.findViewById(R.id.ParqListDir);
        TextView txtPlaza= (TextView) v.findViewById(R.id.ParqListPlaza);
        TextView txtCosto= (TextView) v.findViewById(R.id.ParqListCosto);


        txtId.setText("Id : "+dir.getIdParque().toString());
        txtNombres.setText("Nombre: "+dir.getNombreParq().toString());
        txtTelefono.setText("Teléfono: "+dir.getTelefonoParq().toString());
        txtDireccion.setText("Dirección: "+dir.getDireccionParq().toString());
        txtCosto.setText("Costo: $"+dir.getFraccionParq());
        txtPlaza.setText("Plaza: "+dir.getPlazaParq());





        return v;
    }



}
