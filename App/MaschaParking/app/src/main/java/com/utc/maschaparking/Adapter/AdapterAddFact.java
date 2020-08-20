package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.FacturaRequest;
import com.utc.maschaparking.Request.clienteRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdapterAddFact extends Dialog implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {
    Context context2;
    Dialog dialog = null;
    TextView tipoLabel, horaLabel, placaLabel, nombresLabel;
    Button save, cancel;
    String idClieG = "", idResG = "", placaG = "", horaG = "";
    boolean reservedG = false;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    public AdapterAddFact(Context context, String idRes, String idClieGet, String nombres, String placa, String hora, Boolean reserved) {

        super(context);
        this.context2 = context;

        request = Volley.newRequestQueue(context2);

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.viewconfirmaddreserva);

        tipoLabel = (TextView) dialog.findViewById(R.id.ConfirmAddRes);
        horaLabel = (TextView) dialog.findViewById(R.id.ConfirmAddHora);
        placaLabel = (TextView) dialog.findViewById(R.id.ConfirmAddPlaca);
        nombresLabel = (TextView) dialog.findViewById(R.id.ConfirmAddNombre);


        save = (Button) dialog.findViewById(R.id.ConfirmAddHoraBtnAdd);
        cancel = (Button) dialog.findViewById(R.id.ConfirmAddHoraBtnCancel);


        save.setOnClickListener(this);
        cancel.setOnClickListener(this);


        tipoLabel.setText("No hay reserva con esta placa");
        if (reserved) {
            tipoLabel.setText("Este es un cupo reservado");
            horaLabel.setText(hora.toString());
            nombresLabel.setText(nombres.toString());
            placaLabel.setText(placa.toString());

            idClieG = idClieGet;
            idResG = idRes;
            placaG = placa;
            horaG = hora;
            reservedG = reserved;
        } else {
            horaLabel.setText(hora.toString());
            placaLabel.setText(placa.toString());
            cargarWebService(placa.toString());
            idClieG="1";
            horaG = hora;
            reservedG = reserved;
            placaG = placa;

        }


        dialog.show();

    }


    @Override
    public void onClick(View v) {

        if (v == cancel) {

            dialog.dismiss();
        }

        if (v == save) {
            addFact();
        }

    }


    private void cargarWebService(String placaGet) {

        String url = Validaciones.URL.toString() + "vehiculo/getPlaca.php?placa=" + placaGet.toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(context2, "Ups se ha producido un error intente nuevamente" + error.toString(), Toast.LENGTH_LONG).show();
        //    dialog.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {

        try {

            boolean res = response.getBoolean("success");
            if (!res) {


                return;
            }

            JSONArray json = response.optJSONArray("vehiculo");
            for (int i = 0; i < json.length(); i++) {


                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                nombresLabel.setText(jsonObject.optString("nombresClie").toString() + " " + jsonObject.optString("apellidosClie").toString());

                idClieG = jsonObject.optString("idClie");


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private void addFact() {


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(response);
                    boolean sucess = jsonResponse.getBoolean("success");
                    boolean existe = jsonResponse.getBoolean("existe");

                    if (sucess == true) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Se generó con exito")
                                .setNegativeButton("Aceptar", null)
                                .create().show();

                        dialog.dismiss();



                    } else if (sucess == false) {

                        String msj =" No hay disponibilidad de lugares libres..!";
                        if(existe){
                            msj ="Este vehículo ya se encuentra estacionado en un lugar de MashcaParking";
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(msj)
                                .setNegativeButton("Aceptar", null)
                                .create().show();

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

        String res = "Nuevo";
        if (reservedG) {
            res = "Reservado";
        }
        String api = Validaciones.URL.toString() + "factura/addFact.php";

        FacturaRequest facturaRequest = new FacturaRequest(api,
                Validaciones.id,
                Validaciones.idParq,
                idClieG,
                placaG,
                horaG,
                res,
                idResG,
                responseListener,
                errorListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(facturaRequest);


    }
}
