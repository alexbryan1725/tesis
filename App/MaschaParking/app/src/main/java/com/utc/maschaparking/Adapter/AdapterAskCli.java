package com.utc.maschaparking.Adapter;


import android.app.Activity;
import android.app.Dialog;


import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;


import android.net.Uri;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import com.utc.maschaparking.ClienteMenuActivity;

import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.adminRequest;
import com.utc.maschaparking.Request.clienteRequest;
import com.utc.maschaparking.Validaciones.Validaciones;
import com.utc.maschaparking.inicioSesionActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class AdapterAskCli extends Dialog implements Response.ErrorListener, Response.Listener<JSONObject>, View.OnClickListener {

    TextView nombre, direccion, telefono, correo, precio, plaza;
    Button favoritos, cancelar, navegar,reservar;
    Dialog dialog = null;
    Context context2;
    String id="";
    double lat = 0;
    double longitud = 0;
    Activity activity;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public AdapterAskCli(Activity ac ,Context context, String idParq) {

        super(context);
        this.context2 = context;
        this.activity =ac;

        id = idParq;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.viewasknavigate);

        nombre = (TextView) dialog.findViewById(R.id.ViewAskNombre);
        direccion = (TextView) dialog.findViewById(R.id.ViewAskDir);
        telefono = (TextView) dialog.findViewById(R.id.ViewAskTel);
        correo = (TextView) dialog.findViewById(R.id.ViewAskCorreo);
        precio = (TextView) dialog.findViewById(R.id.ViewAskPrecio);
        plaza = (TextView) dialog.findViewById(R.id.ViewAskPlaza);

        favoritos = (Button) dialog.findViewById(R.id.ViewAskFav);
        cancelar = (Button) dialog.findViewById(R.id.ViewAskCancel);
        navegar = (Button) dialog.findViewById(R.id.ViewAskGo);
        reservar = (Button) dialog.findViewById(R.id.ViewAskReservar);


        favoritos.setOnClickListener(this);
        cancelar.setOnClickListener(this);
        navegar.setOnClickListener(this);
        reservar.setOnClickListener(this);


        favoritos.setVisibility(View.INVISIBLE);
        navegar.setVisibility(View.INVISIBLE);

        request = Volley.newRequestQueue(context2);

        cargarWebService();


        dialog.show();

    }


    private void cargarWebService() {

        String url = Validaciones.URL.toString() + "parqueadero/getParq.php?id=" + id.toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context2, "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();

    }

    @Override
    public void onResponse(JSONObject response) {




        try {

            boolean res = response.getBoolean("success");
            if (!res) {

                Toast.makeText(context2, "No hay información que mostrar..!", Toast.LENGTH_LONG).show();


                return;
            }

            JSONArray json = response.optJSONArray("parqueadero");
            for (int i = 0; i < json.length(); i++) {


                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                nombre.setText(jsonObject.optString("nombreParq"));
                telefono.setText(jsonObject.optString("telefonoParq"));
                correo.setText(jsonObject.optString("correoParq"));
                precio.setText("$"+jsonObject.optString("fraccionParq"));
                direccion.setText(jsonObject.optString("direccionParq"));
                int tot =( Integer.parseInt(jsonObject.optString("plazaParq")) - Integer.parseInt(jsonObject.optString("numeroParq")) );
                plaza.setText(tot + " / " + jsonObject.optString("plazaParq"));

                lat = jsonObject.optDouble("latiParq");
                longitud = jsonObject.optDouble("longParq");


                favoritos.setVisibility(View.VISIBLE);
                navegar.setVisibility(View.VISIBLE);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        if (v == cancelar) {

            dialog.dismiss();
        }

        if (v == navegar) {


            Validaciones validaciones = new Validaciones();

            String contiene = validaciones.appExists("com.google.android.apps.maps",context2);
            if(contiene.equals("Enable")){
             validaciones.goToNavigationDrive(context2,lat,longitud);
             dialog.dismiss();
            }

            if(contiene.equals("Disable")){
                Toast.makeText(context2, "La aplicacion Google Maps esta deshabilitada"  , Toast.LENGTH_LONG).show();
                validaciones.openAppInfo("com.google.android.apps.maps",context2);


            }

            if(contiene.equals("NoExists")){
                Toast.makeText(context2, "Su dispositivo no tiene la aplicacion de google Maps"  , Toast.LENGTH_LONG).show();
                validaciones.openPlayStore("com.google.android.apps.maps",context2);
            }


        }


        if (v == favoritos) {

            addFav();

        }


        if(v ==reservar){

            reservarPuesto();

        }
    }

    private  void  reservarPuesto(){

        AdapterGenerateReserv adapterGenerateReserv = new  AdapterGenerateReserv(activity, context2,id,lat,longitud);

        dialog.dismiss();



    }





    private void addFav( ){




            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {


                            Toast toast = Toast.makeText(getContext(), "Parqueadero añadido a Favoritos", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else if(sucess == false){

                            Toast toast = Toast.makeText(getContext(), "Este parqueadero ya está añadido a Favoritos", Toast.LENGTH_SHORT);
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


            String api = Validaciones.URL.toString() + "cliente/addFavorites.php";

            clienteRequest cliente = new clienteRequest(api,

                    id,
                    Validaciones.id,
                    responseListener,
                    errorListener);


            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(cliente);



    }


}