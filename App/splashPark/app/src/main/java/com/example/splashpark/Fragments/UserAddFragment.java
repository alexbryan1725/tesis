package com.example.splashpark.Fragments;


import android.os.Bundle;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.splashpark.AdminMenuActivity;

import com.example.splashpark.Models.Parqueadero;
import com.example.splashpark.R;
import com.example.splashpark.Request.AdminRequest;

import com.example.splashpark.Request.UserRequest;
import com.example.splashpark.adapter.adapterSpinner;
import com.example.splashpark.library.Validaciones;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserAddFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {


    adapterSpinner adapter;

    EditText cedula, nombres, apellidos, telefono, correo, direccion;
    Button save, cancel;

    String idParq = "0";

    Spinner spinner;

    ArrayList<Parqueadero> listaParq = null;


    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;


    Validaciones validaciones = new Validaciones();

    public UserAddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_add, container, false);

        request = Volley.newRequestQueue(getContext());
        listaParq = new ArrayList<>();

        spinner = (Spinner) view.findViewById(R.id.SpinerUserInsert);

        cedula = (EditText) view.findViewById(R.id.TxtUserInsertCed);
        nombres = (EditText) view.findViewById(R.id.TxtUserInsertNombres);
        direccion = (EditText) view.findViewById(R.id.TxtUserInsertdirec);
        apellidos = (EditText) view.findViewById(R.id.TxtUserInsertApellidos);
        telefono = (EditText) view.findViewById(R.id.TxtUserInsertTelefono);
        correo = (EditText) view.findViewById(R.id.TxtUserInsertCorreo);


        save = (Button) view.findViewById(R.id.BtnUserSave);
        cancel = (Button) view.findViewById(R.id.BtnUserCancel);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);

        cargarWebService();

        return view;
    }




    private void registerAdmin() {


        String cedulaGet = cedula.getText().toString(),
                nombresGet = nombres.getText().toString(),
                apellidosGet = apellidos.getText().toString(),
                telefonoGet = telefono.getText().toString(),
                correoGet = correo.getText().toString(),
                direccionGet = direccion.getText().toString();


        boolean isOk = true;

        if (direccionGet.isEmpty()) {
            direccion.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (cedulaGet.isEmpty()) {
            cedula.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (nombresGet.isEmpty()) {
            nombres.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (telefonoGet.isEmpty()) {
            telefono.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (correoGet.isEmpty()) {
            correo.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if(idParq.equals("0")){
            isOk=false;

            Toast.makeText(getContext(),"Debe seleccionar un parqueadero",Toast.LENGTH_LONG).show();
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

                            Toast toast = Toast.makeText(getContext(), "Propietario Agregado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else {
                            Toast toast = Toast.makeText(getContext(), "Fallo", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }


                    } catch (JSONException e) {
                        Toast toast = Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            };


            String api = Validaciones.URL.toString() + "user/insert.php";

            UserRequest userRequest = new UserRequest(api,
                    AdminMenuActivity.idUser.toString(),
                    idParq.toString(),
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    telefonoGet,
                    correoGet,
                    direccionGet,
                    responseListener,
                    errorListener);


            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(userRequest);

        }

    }



    private void cargarWebService() {

        String url = Validaciones.URL.toString()+"parqueadero/getParq.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onClick(View v) {

        if(v==save){
            registerAdmin();
        }

        if(v==cancel){

        }
    }

    @Override
    public void onResponse(JSONObject response) {


        Parqueadero parqueadero = null;


        JSONArray json = response.optJSONArray("parqueadero");

        parqueadero = new Parqueadero();

        parqueadero.setNombreParq("--Seleccione--");
        parqueadero.setIdParque(0);

        listaParq.add(parqueadero);

        try {

            for (int i = 0; i < json.length(); i++) {
                parqueadero = new Parqueadero();

                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                parqueadero.setIdParque(jsonObject.optInt("idParq"));
                parqueadero.setNombreParq(jsonObject.optString("nombreParq"));
                parqueadero.setCorreoParq(jsonObject.optString("correoParq"));
                parqueadero.setTelefonoParq(jsonObject.optString("telefonoParq"));
                parqueadero.setDireccionParq(jsonObject.optString("direccionParq"));
                parqueadero.setLatiParq(jsonObject.optDouble("latiParq"));
                parqueadero.setLongParq(jsonObject.optDouble("longParq"));
                parqueadero.setRucParq(jsonObject.optString("rucParq"));
                parqueadero.setAlquilerParq(jsonObject.optDouble("alquilerParq"));
                parqueadero.setFraccionParq(jsonObject.optDouble("fraccionParq"));
                parqueadero.setNumeroParq(jsonObject.optInt("numeroParq"));
                parqueadero.setPlazaParq(jsonObject.optInt("plazaParq"));
                parqueadero.setEnableParq(jsonObject.optString("enableParq"));

                listaParq.add(parqueadero);
            }


            adapter = new adapterSpinner(this.getActivity(), listaParq);

            spinner.setAdapter(adapter);
            spinner.setContentDescription("SELECCIONE");


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;
                    Parqueadero parq = listaParq.get(pos);
                    idParq= parq.getIdParque().toString();
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

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
    }
}
