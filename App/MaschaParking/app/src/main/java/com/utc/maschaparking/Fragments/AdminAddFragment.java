package com.utc.maschaparking.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.adminRequest;
import com.utc.maschaparking.Validaciones.Validaciones;
import com.utc.maschaparking.inicioSesionActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class AdminAddFragment extends Fragment implements View.OnClickListener{

    EditText cedula, nombres, apellidos, telefono, correo, pswd,direccion;
    Button save;

    AdapterDialog adapterDialog = null;
    Validaciones validaciones = new Validaciones();

    public AdminAddFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_add, container, false);

        cedula = (EditText) view.findViewById(R.id.TxtAdminInsertCed);
        nombres = (EditText) view.findViewById(R.id.TxtAdminInsertNombres);
        apellidos = (EditText) view.findViewById(R.id.TxtAdminInsertApellidos);
        telefono = (EditText) view.findViewById(R.id.TxtAdminInsertTelefono);
        correo = (EditText) view.findViewById(R.id.TxtAdminInsertCorreo);
        pswd = (EditText) view.findViewById(R.id.TxtAdminInsertCed);
        direccion = (EditText) view.findViewById(R.id.TxtAdminInsertdirec);




        save=(Button)view.findViewById(R.id.BtnAdminSave);
        save.setOnClickListener( this);

        adapterDialog = new AdapterDialog(getContext());

        return view;
    }

    private void registerAdmin() {


        String  cedulaGet = cedula.getText().toString(),
                nombresGet = nombres.getText().toString(),
                apellidosGet = apellidos.getText().toString(),
                telefonoGet = telefono.getText().toString(),
                direccionGet = direccion.getText().toString(),
                correoGet = correo.getText().toString(),

                pswdGet = pswd.getText().toString();



        boolean isOk = true;

        if (cedulaGet.isEmpty()) {
            cedula.setError("Este campo es Obligatorio");
            isOk = false;
        }else{
            boolean ok =  validaciones.cedulaIsOk(cedulaGet.toString());
                if(!ok){
                    cedula.setError("Incorrecto");
                    isOk = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("El numero de cédula ingresado no es valida")
                            .setNegativeButton("Aceptar", null)
                            .create().show();

                    adapterDialog.hide();
                }
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
        if (apellidosGet.isEmpty()) {
            apellidos.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (pswdGet.isEmpty()) {
            pswd.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (direccionGet.isEmpty()) {
            direccion.setError("Este campo es Obligatorio");
            isOk = false;
        }



        if(isOk) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {

                            adapterDialog.hide();
                            Toast toast = Toast.makeText(getContext(), "Administrador Agregado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else if(sucess == false){
                                String error = jsonResponse.getString("error");
                                String msj ="";


                                if(error.equals("cedulaDupli")){
                                    cedula.setError("Ya existe este usuario registrado");
                                    msj =" Ya existe este usuario registrado ..!";
                                }

                                if(error.equals("correoDupli")){
                                    correo.setError("Ya existe este correo registrado");
                                    msj =" Ya existe este correo registrado ..!";
                                }

                            adapterDialog.hide();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                        }


                    } catch (JSONException e) {
                        adapterDialog.hide();
                        Toast toast = Toast.makeText(getContext(),e.toString() , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        e.printStackTrace();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    adapterDialog.hide();
                    Toast toast = Toast.makeText(getContext(),error.toString() , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            };


            String api = Validaciones.URL.toString() + "admin/insertAdmin.php";

            adminRequest adminRequest = new adminRequest(api,
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    telefonoGet,
                    correoGet,
                    direccionGet,
                    pswdGet,
                    responseListener,
                    errorListener);


            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(adminRequest);

        }else {
            adapterDialog.hide();
            return;
        }

    }


    @Override
    public void onClick(View v) {
        if(v == save ){


            adapterDialog.show("Añadiendo Administrador","Espere Por Favor ..!");

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



            registerAdmin();


        }


    }
}
