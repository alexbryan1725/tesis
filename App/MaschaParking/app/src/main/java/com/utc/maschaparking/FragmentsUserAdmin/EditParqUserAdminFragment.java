package com.utc.maschaparking.FragmentsUserAdmin;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.AdminMenuActivity;
import com.utc.maschaparking.MapsAddParqActivity;
import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.ParqRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EditParqUserAdminFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject> {


    public static double latitudEdit = 0, longitudEdit = 0;

    Button getNow, findMap, save, cancel;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private LocationManager ubicacion;
    Location location;
    TextView longitudLb, latitudLb;
    EditText ruc, nomParq, tel, plaza, precio, email, direccion;
    Geocoder geocoder = null;
    String  idAdmin ="",  enableGet = "TRUE";
    AdapterDialog adapterDialog = null;


    Validaciones validaciones = new Validaciones();

    public EditParqUserAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_parq_user_admin, container, false);

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando Información...");
        progress.setCancelable(false);
        request = Volley.newRequestQueue(getContext());
        adapterDialog = new AdapterDialog(getContext());

        longitudLb = (TextView) view.findViewById(R.id.LblUserAdminEditLong);
        latitudLb = (TextView) view.findViewById(R.id.LblUserAdminEditLati);
        direccion = (EditText) view.findViewById(R.id.TxtUserAdminEditDir);

        ruc = (EditText) view.findViewById(R.id.TxtUserAdminEditRuc);
        nomParq = (EditText) view.findViewById(R.id.TxtUserAdminEditNom);
        tel = (EditText) view.findViewById(R.id.TxtUserAdminEditTel);
        plaza = (EditText) view.findViewById(R.id.TxtUserAdminEditPlaza);
        precio = (EditText) view.findViewById(R.id.TxtUserAdminEditPrecioFrac);
        email = (EditText) view.findViewById(R.id.TxtUserAdminEditEmail);

        getNow = (Button) view.findViewById(R.id.btnUserAdminNowEdit);
        findMap = (Button) view.findViewById(R.id.btnUserAdminEditMaps);
        save = (Button) view.findViewById(R.id.BtnUserAdminEdit);


        getNow.setOnClickListener(this);
        findMap.setOnClickListener(this);
        save.setOnClickListener(this);


        cargarWebService();
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


            } else if (!validaciones.isDataConnectionAvailable(getContext())) {

                Snackbar mySnackbar = Snackbar.make(getView(), "No tiene conección con Internet!  ", Snackbar.LENGTH_LONG).
                        setDuration(2000);
                View view = mySnackbar.getView();
                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.desingbtnrelleno));


                mySnackbar.show();

            } else {


                Bundle parmetros = new Bundle();
                parmetros.putString("typeLocate", "updateUserAdmin");
                parmetros.putString("long", ""+longitudEdit);
                parmetros.putString("lat", ""+latitudEdit);

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
                view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.desingbtnrelleno));

                mySnackbar.show();


            } else {
                getUbicacion();
            }
        }

        if (v == save) {


            adapterDialog.show("Realiando Cambios","Por favor Espere...");

            longitudLb.setText(String.valueOf(longitudEdit));
            latitudLb.setText(String.valueOf(latitudEdit));


            if (!validaciones.isDataConnectionAvailable(getContext())) {
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

            editParq();
        }





    }


    private void cargarWebService() {
        progress.show();


        String url = Validaciones.URL.toString() + "parqueadero/getParq.php?id=" + Validaciones.idParq.toString();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Ups se ha producio un error, Verifique su conexión a internet!..", Toast.LENGTH_LONG).show();

        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {


        JSONArray json = response.optJSONArray("parqueadero");

        try {

            boolean res = response.getBoolean("success");
            if (!res) {

                Toast.makeText(getContext(), "No hay información que mostrar..!", Toast.LENGTH_LONG).show();

                progress.hide();

                return;
            }

            for (int i = 0; i < json.length(); i++) {


                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                idAdmin =  jsonObject.optString("sistemaadmin_idAdmin");
                nomParq.setText(jsonObject.optString("nombreParq"));
                email.setText(jsonObject.optString("correoParq"));
                tel.setText(jsonObject.optString("telefonoParq"));
                direccion.setText(jsonObject.optString("direccionParq"));
                latitudLb.setText("" + jsonObject.optDouble("latiParq"));
                longitudLb.setText("" + jsonObject.optDouble("longParq"));
                ruc.setText(jsonObject.optString("rucParq"));
                precio.setText("" + jsonObject.optDouble("fraccionParq"));
                plaza.setText("" + jsonObject.optInt("plazaParq"));
                enableGet =(jsonObject.optString("enableParq"));
                latitudEdit = jsonObject.optDouble("latiParq");
                longitudEdit = jsonObject.optDouble("longParq");

                progress.hide();
            }

        } catch (
                JSONException e) {
            Toast.makeText(getContext(), "No hay información que mostrar..!", Toast.LENGTH_LONG).show();

            progress.hide();
        }

        progress.hide();
    }


    private void editParq() {

        boolean isOk = true;



        String idNew = idAdmin.toString(),
                nombreNew = nomParq.getText().toString(),
                emailNew = email.getText().toString(),
                telefNew = tel.getText().toString(),
                dirNew = direccion.getText().toString(),
                longNew = String.valueOf(longitudEdit).toString(),
                latNew = String.valueOf(latitudEdit).toString(),
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
        } else {
            boolean ok = validaciones.rucIsOk(rucNew);
            if (!ok) {
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
        if (longitudEdit == 0) {
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


        if (isOk) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {

                            cargarWebService();
                            adapterDialog.hide();
                            Toast toast = Toast.makeText(getContext(), "Parqueadero Modificado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();


                        } else if (sucess == false) {

                            adapterDialog.hide();
                            String error = jsonResponse.getString("error");
                            String msj = "";


                            if (error.equals("cedulaDupli")) {
                                ruc.setError("Ya existe este usuario registrado");
                                msj = " Ya existe este usuario registrado ..!";
                            }

                            if (error.equals("correoDupli")) {
                                email.setError("Ya existe este correo registrado");
                                msj = " Ya existe este correo registrado ..!";
                            }


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

            String api = Validaciones.URL.toString() + "parqueadero/updateParq.php";
            ParqRequest parqRequest = new ParqRequest(api, Validaciones.idParq, idNew, nombreNew, emailNew, telefNew, dirNew, longNew, latNew,
                    rucNew, precioNew, plazaNew, enableGet, responseListener, errorListener);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(parqRequest);
        } else {
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

                longitudEdit = location.getLongitude();
                latitudEdit = location.getLatitude();


            } else {
                Toast.makeText(getContext(), "No HAY UBICACION", Toast.LENGTH_SHORT);
            }
        }
    }


}
