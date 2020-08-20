package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.utc.maschaparking.Models.FacturaParq;
import com.utc.maschaparking.Models.Vehiculo;
import com.utc.maschaparking.R;

import java.util.ArrayList;

public class AdapterListFact extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<FacturaParq> items;

    public AdapterListFact(Activity activity, ArrayList<FacturaParq> items){

        this.activity = activity;
        this.items = items;

    }

    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }


    public void addAll(ArrayList<FacturaParq> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
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
            v = inf.inflate(R.layout.viewlistfacturaclie, null);
        }

        FacturaParq dir = items.get(position);

        TextView placa = (TextView) v.findViewById(R.id.lblClieFacPlaca);
        TextView id =(TextView) v.findViewById(R.id.lblClieFacId);
        TextView nombre = (TextView) v.findViewById(R.id.lblClieFacNombreParq);
        TextView fechaIni= (TextView) v.findViewById(R.id.lblClieFacIngreso);
        TextView fechaFin = (TextView) v.findViewById(R.id.lblClieFacSalida);
       // TextView tiempo= (TextView) v.findViewById(R.id.lblClieFacTiempo);
        TextView title= (TextView) v.findViewById(R.id.lablFacTitle);
        TextView precio= (TextView) v.findViewById(R.id.lblClieFacPrecio);


        String x = dir.getEstadoFac().toString();
        if(x.equals("pendiente")){
            title.setText("En Progreso");
            title.setBackground(ContextCompat.getDrawable( v.getContext(), R.drawable.designtitlelistend));

        }

        placa.setText(dir.getPlacaFac().toString());
        id.setText( dir.getIdFac().toString());
        nombre.setText( dir.getNombreParq().toString());
        fechaIni.setText( dir.getFechaInicio().toString());
        fechaFin.setText( dir.getFechaFin().toString());
        //tiempo.setText("Marca: "+dir.getMarcaVehi().toString());
        precio.setText("$"+dir.getTotalFac().toString());


        return v;



    }
}
