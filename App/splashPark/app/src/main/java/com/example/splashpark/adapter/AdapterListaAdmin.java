package com.example.splashpark.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splashpark.Models.Admin;
import com.example.splashpark.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterListaAdmin extends BaseAdapter {


    protected Activity activity;
    protected ArrayList<Admin> items;

    public AdapterListaAdmin(Activity activity, ArrayList<Admin> items){

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


    public void addAll(ArrayList<Admin> category) {
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
            v = inf.inflate(R.layout.viewlistaadmin, null);
        }

        Admin dir = items.get(position);

        TextView txtId = (TextView) v.findViewById(R.id.AdminListId);
        TextView txtCedula = (TextView) v.findViewById(R.id.AdminListCed);
        TextView txtNombres =(TextView) v.findViewById(R.id.AdminListNom);
        TextView txtTelefono = (TextView) v.findViewById(R.id.adminListTel);
        TextView txtCorreo= (TextView) v.findViewById(R.id.adminListEmail);



        txtId.setText("Id : "+dir.getIdAdmin().toString());
        txtCedula.setText("Cédula : "+dir.getCedulaAdmin().toString());
        txtNombres.setText("Nombres: "+dir.getNombresAdmin().toString());
        txtTelefono.setText("Teléfono: "+dir.getTelefonoAdmin().toString());
        txtCorreo.setText("Correo: "+dir.getCorreoAdmin().toString());
        return v;
    }
}
