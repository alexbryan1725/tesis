package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.FragmentsCliente.FavoritosClienteFragment;
import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.clienteRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterFavorite extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Parqueadero> items;



    public AdapterFavorite(Activity activity, ArrayList<Parqueadero> items){

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
            v = inf.inflate(R.layout.viewfavorite, null);
        }
        final Parqueadero dir = items.get(position);


        ImageView imageDelete = (ImageView)v.findViewById(R.id.eraseFav);
        LinearLayout go =(LinearLayout)v.findViewById(R.id.irFav);

        TextView txtNombres =(TextView) v.findViewById(R.id.nombreFav);
        TextView txtTelefono = (TextView) v.findViewById(R.id.telefonoFav);
        TextView txtDireccion = (TextView) v.findViewById(R.id.direccionFav);
        TextView txtPlaza= (TextView) v.findViewById(R.id.puestosFav);
        TextView txtCosto= (TextView) v.findViewById(R.id.precioFav);


        txtNombres.setText( dir.getNombreParq().toString());
        txtTelefono.setText( dir.getTelefonoParq().toString());
        txtDireccion.setText(dir.getDireccionParq().toString());
        txtCosto.setText("$"+dir.getFraccionParq());
        txtPlaza.setText("Disponible:   "+(dir.getPlazaParq() - dir.getNumeroParq())+" de " + dir.getPlazaParq());

        imageDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {



                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JSONObject jsonResponse = null;
                            try {
                                jsonResponse = new JSONObject(response);
                                boolean sucess = jsonResponse.getBoolean("success");

                                if (sucess == true) {


                                    Toast toast = Toast.makeText(v.getContext(), "Eliminado de Favoritos", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.show();

                                }


                            } catch (JSONException e) {

                            }


                        }
                    };


                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    };


                    String api = Validaciones.URL.toString() + "cliente/deleteFavorites.php";

                    clienteRequest cliente = new clienteRequest(api,
                            "Delete",
                            dir.getIdParque().toString(),
                            Validaciones.id,
                            responseListener,
                            errorListener);


                    RequestQueue queue = Volley.newRequestQueue(v.getContext());
                    queue.add(cliente);



                }






        });

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Validaciones validaciones = new Validaciones();
                validaciones.goToNavigationDrive(v.getContext(),dir.getLatiParq(),dir.getLongParq());



            }
        });







        return v;
    }



}
