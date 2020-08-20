package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.R;

import java.util.ArrayList;

public class AdapterListaParqClie   extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Parqueadero> items;


    public AdapterListaParqClie(Activity activity, ArrayList<Parqueadero> items){

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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.viewparqueaderolist, null);
        }
        Parqueadero dir = items.get(position);

        int tot = dir.getPlazaParq();
        int act = dir.getNumeroParq();

        int end = dir.getPlazaParq() - dir.getNumeroParq() ;

        double porcentaje = (100*act)/tot;

        ImageView image =(ImageView)v.findViewById(R.id.imgListParq);




        TextView txtNombres =(TextView) v.findViewById(R.id.nombreListParq);
        TextView txtTelefono = (TextView) v.findViewById(R.id.telefonoListParq);
        TextView txtDireccion = (TextView) v.findViewById(R.id.direccionListParq);
        TextView txtPlaza= (TextView) v.findViewById(R.id.plazaListParq);
        TextView txtCosto= (TextView) v.findViewById(R.id.precioListParq);


        if(porcentaje <=50){
            image.setImageResource(R.drawable.pointgreen);

        }
        if(porcentaje >50 && porcentaje <=80 ){
            image.setImageResource(R.drawable.pointorange);
        }
        if(porcentaje >80 ){
            image.setImageResource(R.drawable.pointred);
        }


        txtNombres.setText(""+dir.getNombreParq().toString());
        txtTelefono.setText("Teléfono: "+dir.getTelefonoParq().toString());
        txtDireccion.setText("Dirección: "+dir.getDireccionParq().toString());
        txtCosto.setText(""+dir.getFraccionParq());
        txtPlaza.setText("Disponibles :    "+end+"/"+dir.getPlazaParq());



        return v;
    }


}
