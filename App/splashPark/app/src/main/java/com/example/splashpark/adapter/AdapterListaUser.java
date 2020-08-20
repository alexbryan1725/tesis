package com.example.splashpark.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.splashpark.Models.Admin;
import com.example.splashpark.Models.User;
import com.example.splashpark.R;

import java.util.ArrayList;

public class AdapterListaUser extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<User> items;

    public AdapterListaUser(Activity activity, ArrayList<User> items){

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


    public void addAll(ArrayList<User> category) {
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

        User dir = items.get(position);

        TextView txtId = (TextView) v.findViewById(R.id.AdminListId);
        TextView txtCedula = (TextView) v.findViewById(R.id.AdminListCed);
        TextView txtNombres =(TextView) v.findViewById(R.id.AdminListNom);
        TextView txtTelefono = (TextView) v.findViewById(R.id.adminListTel);
        TextView txtCorreo= (TextView) v.findViewById(R.id.adminListEmail);



        txtId.setText("Id : "+dir.getIdUser().toString());
        txtCedula.setText("Cédula : "+dir.getCedulaUSer().toString());
        txtNombres.setText("Nombres: "+dir.getNombresUSer().toString());
        txtTelefono.setText("Teléfono: "+dir.getTelefonoUSer().toString());
        txtCorreo.setText("Correo: "+dir.getCedulaUSer().toString());


        return v;



    }
}
