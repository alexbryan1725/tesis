package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.Models.Vehiculo;
import com.utc.maschaparking.R;

import java.util.ArrayList;

public class AdapterSpinnerReserva extends BaseAdapter {protected Activity activity;
    protected ArrayList<Vehiculo> items;


    public AdapterSpinnerReserva(Activity activity, ArrayList<Vehiculo> items){

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


    public void addAll(ArrayList<Vehiculo> category) {
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
            v = inf.inflate(R.layout.viewspinnerreserva, null);
        }

        Vehiculo dir = items.get(position);


        TextView txtNombre = (TextView) v.findViewById(R.id.TxtSpinnerPlaca);

        txtNombre.setText(dir.getPlacaVehi()+" / "+dir.getColorVehi());

        return v;
    }
}
