package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.Models.Vehiculo;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.UserRequest;
import com.utc.maschaparking.Request.clienteRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterGenerateReserv extends Dialog implements Response.ErrorListener, Response.Listener<JSONObject>, View.OnClickListener {
    Button cancelar, reservar;
    EditText placa;
    Context context2;
    Dialog dialog = null;
    String IdParq = "";
    double latitud = 0, longitud = 0;

    AdapterSpinnerReserva adapter;
    ArrayList<Vehiculo> listaVehi = null;
    Validaciones validaciones = new Validaciones();


    Activity activity;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    Spinner spinner;

    AdapterDialog adapterDialog = null;

    public AdapterGenerateReserv(Activity ac, Context context, String idParq, double lat, double Long) {


        super(context);
        this.activity = ac;
        this.context2 = context;
        listaVehi = new ArrayList<>();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.viewreservacion);

        cancelar = (Button) dialog.findViewById(R.id.BtnReserCancel);
        reservar = (Button) dialog.findViewById(R.id.BtnReservAdd);

        spinner = (Spinner) dialog.findViewById(R.id.SpinerReserSelect);
        placa = (EditText) dialog.findViewById(R.id.TxtReservPlaca);

        adapterDialog = new AdapterDialog(getContext());

        reservar.setOnClickListener(this);
        cancelar.setOnClickListener(this);

        request = Volley.newRequestQueue(context2);

        IdParq=idParq;
        latitud = lat;
        longitud = Long;
        cargarWebService();

        dialog.show();
    }


    @Override
    public void onClick(View v) {
        if (v == cancelar) {

            dialog.dismiss();
        }
        if (v == reservar) {


            adapterDialog.show("Generando Reservación","Por favor Espere...");




            if(!validaciones.isDataConnectionAvailable(context2)){
                Snackbar mySnackbar = Snackbar.make(v, "No tiene conección con Internet..!  ", Snackbar.LENGTH_LONG).
                        setDuration(2000);
                View view = mySnackbar.getView();
                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setBackground(ContextCompat.getDrawable(activity, R.drawable.designsnackbar));
                mySnackbar.show();

                adapterDialog.hide();

                return;


            }
           String placaGet = placa.getText().toString();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


            addReservacion();
        }
    }


    private void addReservacion() {


        String idClieGet = Validaciones.id.toString(),
                idParqGet = IdParq.toString(),
                placaGet = placa.getText().toString();

        boolean isOk = true;


        if (placaGet.isEmpty()) {
            placa.setError("Este campo es Obligatorio");
            isOk = false;
        }else {
            boolean ok =  validaciones.matriculaOk(placaGet.toString());
            if(!ok){
                placa.setError("Incorrecto (ABC1234)");
                isOk = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("El número de placa no es correcta Recuerde que debe tener el siguiente formato (ABC1234)")
                        .setNegativeButton("Aceptar", null)
                        .create().show();
            }
        }




        if (isOk) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {
                            adapterDialog.hide();

                            Toast toast = Toast.makeText(getContext(), "Se ha reservado un lugar..!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                            validaciones.goToNavigationDrive(context2,latitud,longitud);


                        } else if(sucess == false){
                            String msj ="";

                            boolean isReserved = jsonResponse.getBoolean("estadoReserva");

                            if(isReserved){
                                 msj ="No puede realizar una reservación ya que existe una pendiente...!";
                            }else {
                                 msj ="Lo sentimos, No hay disponibilidad..!";
                            }
                            adapterDialog.hide();


                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                        }


                    } catch (JSONException e) {
                        adapterDialog.hide();
                        Toast toast = Toast.makeText(getContext(), "Ups se ha producido un error, Intente nuevamente...!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    adapterDialog.hide();
                    Toast toast = Toast.makeText(getContext(), "Ups se ha producido un error, Intente nuevamente...!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                }
            };


            String api = Validaciones.URL.toString() + "cliente/addReserva.php";



            clienteRequest request = new clienteRequest(api,

                    placaGet.toString(),
                    idParqGet,
                    idClieGet,
                    "pendiente",
                    responseListener,
                    errorListener);


            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);

        }else {
            adapterDialog.hide();
            return;
        }

    }


    private void cargarWebService() {

        String url = Validaciones.URL.toString() + "vehiculo/getVehi.php?idClie=" + Validaciones.id.toString();
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


        Vehiculo vehiculo = null;

        try {


            boolean res = response.getBoolean("success");
            if (!res) {

                Toast.makeText(getContext(), "No hay vehículos agregados ..!", Toast.LENGTH_LONG).show();
                adapter = new AdapterSpinnerReserva(activity, listaVehi);

                spinner.setAdapter(adapter);
                return;
            }
            JSONArray json = response.optJSONArray("vehiculo");

            for (int i = 0; i < json.length(); i++) {
                vehiculo = new Vehiculo();

                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                vehiculo.setIdVehi(jsonObject.optInt("idVehi"));
                vehiculo.setPlacaVehi(jsonObject.optString("placaVehi"));
                vehiculo.setColorVehi(jsonObject.optString("colorVehi"));
                vehiculo.setMarcaVehi(jsonObject.optString("marcaVehi"));
                vehiculo.setTipoVehi(jsonObject.optString("tipoVehi"));

                listaVehi.add(vehiculo);
            }


            adapter = new AdapterSpinnerReserva(activity, listaVehi);

            spinner.setAdapter(adapter);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;
                    Vehiculo parq = listaVehi.get(pos);

                    placa.setText(parq.getPlacaVehi().toString());

                    //Toast.makeText(getContext(),parq.getIdParque().toString(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }


            });


        } catch (JSONException e) {

            Toast.makeText(getContext(), "No" + e.toString(), Toast.LENGTH_LONG).show();
            System.out.println();
            e.printStackTrace();
        }
    }
}
