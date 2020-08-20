package com.utc.maschaparking.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.AdminMenuActivity;
import com.utc.maschaparking.MapsAddParqActivity;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.ParqRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParqAddFragment extends  Fragment implements View.OnClickListener {


    FrameLayout frameLayout;
    Validaciones validaciones = new Validaciones();

    public static double latitud = 0, longitud = 0;

    Button getNow, findMap, save, cancel;

    private LocationManager ubicacion;
    Location location;
    TextView longitudLb, latitudLb;
    EditText ruc, nomParq, tel, plaza, precio, email, direccion;
    Geocoder geocoder = null;

    AdapterDialog adapterDialog = null;

    public ParqAddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_parq_add, container, false);


        longitudLb = (TextView) view.findViewById(R.id.LblParqInsertLong);
        latitudLb = (TextView) view.findViewById(R.id.LblParqInsertLati);
        direccion = (EditText) view.findViewById(R.id.TxtParqInsertDir);

        ruc = (EditText) view.findViewById(R.id.TxtParqEditRuc);
        nomParq = (EditText) view.findViewById(R.id.TxtParqInsertNom);
        tel = (EditText) view.findViewById(R.id.TxtParqInsertTel);
        plaza = (EditText) view.findViewById(R.id.TxtParqInsertPlaza);
        precio = (EditText) view.findViewById(R.id.TxtParqInsertPrecioFrac);
        email = (EditText) view.findViewById(R.id.TxtParqInsertEmail);


        getNow = (Button) view.findViewById(R.id.btnParqNow);
        findMap = (Button) view.findViewById(R.id.btnParqMaps);
        save = (Button) view.findViewById(R.id.BtnParqSave);
        cancel = (Button) view.findViewById(R.id.BtnParqSave);


        getNow.setOnClickListener(this);
        findMap.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        frameLayout = (FrameLayout) view.findViewById(R.id.frameAddParq);

        adapterDialog = new AdapterDialog(getContext());
        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == findMap) {


            if (!validaciones.isGPSProvider(getContext())) {

                Snackbar mySnackbar = Snackbar.make(getView(), "No tiene conección con GPS..!  ", Snackbar.LENGTH_LONG).
                        setDuration(2000);
                View view = mySnackbar.getView();
                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.desingbtnrelleno));

                mySnackbar.show();


            } else   if (!validaciones.isDataConnectionAvailable(getContext())) {

                Snackbar mySnackbar = Snackbar.make(getView(), "No tiene conección con Internet!  ", Snackbar.LENGTH_LONG).
                        setDuration(2000);
                View view = mySnackbar.getView();
                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.desingbtnrelleno));


                mySnackbar.show();

            }
            else {
                Bundle parmetros = new Bundle();
                parmetros.putString("typeLocate", "insert");
                Intent intent = new Intent(getContext(), MapsAddParqActivity.class);

                intent.putExtras(parmetros);
                startActivity(intent);
            }


        }

        if (v == getNow) {

            if (!validaciones.isGPSProvider(getContext())) {




                Snackbar mySnackbar = Snackbar.make(getView(), "No tiene conección con GPS..!  ", Snackbar.LENGTH_LONG).
                        setDuration(2000);
                View view = mySnackbar.getView();
                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.designsnackbar));
                mySnackbar.show();


            } else {


                getUbicacion();
            }

        }

        if (v == save) {


            adapterDialog.show("Agregando Parqueader","Por favor Espere...");

            longitudLb.setText(String.valueOf(longitud));
            latitudLb.setText(String.valueOf(latitud));


            if(!validaciones.isDataConnectionAvailable(getContext())){
                Snackbar mySnackbar = Snackbar.make(getView(), "No tiene conección con Internet..!  ", Snackbar.LENGTH_LONG).
                        setDuration(2000);
                View view = mySnackbar.getView();
                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.designsnackbar));
                mySnackbar.show();
                adapterDialog.hide();

                return;


            }

            generateNewParq();

        }

    }

    private void generateNewParq() {

        boolean isOk = true;

        String idNew = AdminMenuActivity.idUser.toString(),
                nombreNew = nomParq.getText().toString(),
                emailNew = email.getText().toString(),
                telefNew = tel.getText().toString(),
                dirNew = direccion.getText().toString(),
                longNew = String.valueOf(longitud).toString(),
                latNew = String.valueOf(latitud).toString(),
                rucNew = ruc.getText().toString(),
                precioNew = precio.getText().toString(),
                plazaNew = plaza.getText().toString();

        if (nombreNew.isEmpty()) {
            nomParq.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (emailNew.isEmpty()) {
            email.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (telefNew.isEmpty()) {
            tel.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (dirNew.isEmpty()) {
            direccion.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (rucNew.isEmpty()) {
            ruc.setError("Este campo es Obligatorio");
            isOk = false;
        }else {
             boolean ok = validaciones.rucIsOk(rucNew);
            if(!ok){
                ruc.setError("Incorrecto");
                isOk = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("El número de ruc ingresado no es valida")
                        .setNegativeButton("Aceptar", null)
                        .create().show();
            }
        }




        if (precioNew.isEmpty()) {
            precio.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (plazaNew.isEmpty()) {
            plaza.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (longitud == 0) {
            isOk = false;
            Snackbar mySnackbar = Snackbar.make(getView(), "No ha seleccionado una localización..!  ", Snackbar.LENGTH_LONG).
                    setDuration(2000);
            View view = mySnackbar.getView();
            TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.designsnackbar));
            mySnackbar.show();

            getNow.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.designbtnline));
            findMap.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.designbtnline));


        }


        if(isOk){

            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {
                            adapterDialog.hide();
                            Toast toast = Toast.makeText(getContext(), "Parqueadero Agregado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else if(sucess == false){
                            adapterDialog.hide();
                            String error = jsonResponse.getString("error");
                            String msj ="";


                            if(error.equals("cedulaDupli")){
                                ruc.setError("Ya existe este usuario registrado");
                                msj =" Ya existe este usuario registrado ..!";
                            }

                            if(error.equals("correoDupli")){
                                email.setError("Ya existe este correo registrado");
                                msj =" Ya existe este correo registrado ..!";
                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                        }


                    } catch (JSONException e) {
                        adapterDialog.hide();
                        Toast toast = Toast.makeText(getContext(), "Ups algo ha salido mal, intente nuevamente", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    adapterDialog.hide();
                    Toast toast = Toast.makeText(getContext(), "Ups algo ha salido mal, intente nuevamente", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            };

            String api = Validaciones.URL.toString() + "parqueadero/insertParq.php";
            ParqRequest parqRequest = new ParqRequest(api, idNew, nombreNew, emailNew, telefNew, dirNew, longNew, latNew,
                    rucNew, precioNew, plazaNew, responseListener, errorListener);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(parqRequest);

        }else {
            adapterDialog.hide();
            return;
        }
    }





    private void getUbicacion() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getContext(), "No tiene permisos de usar el GPS", Toast.LENGTH_SHORT);
        } else {

            ubicacion = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
            location = ubicacion.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                longitudLb.setText(String.valueOf(location.getLongitude()));
                latitudLb.setText(String.valueOf(location.getLatitude()));

                longitud = location.getLongitude();
                latitud = location.getLatitude();


            } else {
                Toast.makeText(getContext(), "NO HAY UBICACION", Toast.LENGTH_SHORT);
            }
        }
    }
}
