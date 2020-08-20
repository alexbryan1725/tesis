package com.utc.maschaparking.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.AdminMenuActivity;
import com.utc.maschaparking.FragmentsCliente.InicioClieFragment;
import com.utc.maschaparking.MainActivity;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.adminRequest;
import com.utc.maschaparking.Request.perfilRequest;
import com.utc.maschaparking.Validaciones.Validaciones;
import com.utc.maschaparking.inicioSesionActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class CuentaFragment extends Fragment implements  View.OnClickListener{

    EditText cedula,nombres,apellidos,direccion,telefono,correo;
    Button addBtn,cancelBtn,pswdBtn;

    AdapterDialog adapterDialog = null;

    Validaciones validaciones = new Validaciones();
    public CuentaFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuenta, container, false);

        cedula=(EditText)view.findViewById(R.id.TxtCuentaInsertCed);
        nombres=(EditText)view.findViewById(R.id.TxtCuentaInsertNombres);
        apellidos=(EditText)view.findViewById(R.id.TxtCuentaInsertApellidos);
        direccion=(EditText)view.findViewById(R.id.TxtCuentaInsertdirec);
        telefono=(EditText)view.findViewById(R.id.TxtCuentaInsertTelefono);
        correo=(EditText)view.findViewById(R.id.TxtCuentaInsertCorreo);


        addBtn=(Button)view.findViewById(R.id.BtnCuentaSave);
        cancelBtn=(Button)view.findViewById(R.id.BtnCuentaCancel);
        pswdBtn=(Button)view.findViewById(R.id.BtnCuentaPswd);

        addBtn.setOnClickListener( this);
        cancelBtn.setOnClickListener( this);
        pswdBtn.setOnClickListener( this);

        getInfo();

        adapterDialog = new AdapterDialog(getContext());

        return view;
    }


    private  void getInfo(){
        cedula.setText(Validaciones.cedula);
        nombres.setText(Validaciones.nombres);
        apellidos.setText(Validaciones.apellidos);
        direccion.setText(Validaciones.direccion);
        correo.setText(Validaciones.correo);
        telefono.setText(Validaciones.telefono);

}


    @Override
    public void onClick(View v) {

        if (v==addBtn){

            new AlertDialog.Builder(getContext())
                    .setTitle("¿Está seguro de cambiar su información?")
                    .setMessage("Al aceptar se reiniciara la aplicacion para relaizar cambios.!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            editAcount();


                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
        if(v==cancelBtn){

            String t = Validaciones.typeUSer;

            if(t.equals("Admin")){

            }

            if(t.equals("Cliente")){
                InicioClieFragment vista = new InicioClieFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.esenarioClie, vista).commit();

            }



        }



    }

    private void editAcount() {
        adapterDialog.show("Realizando Cambios","Porfavor Espere... ");
        String  cedulaGet = cedula.getText().toString(),
                nombresGet = nombres.getText().toString(),
                apellidosGet = apellidos.getText().toString(),
                telefonoGet = telefono.getText().toString(),
                direccionGet = direccion.getText().toString(),
                correoGet = correo.getText().toString();



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
            }
        }

        if (nombresGet.isEmpty()) {
            nombres.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (direccionGet.isEmpty()) {
            direccion.setError("Este campo es Obligatorio");
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


        if(isOk) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {


                            Toast toast = Toast.makeText(getContext(), "Cuenta Actualizada", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                            getActivity().finish();
                            adapterDialog.hide();
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            startActivity(intent);


                        } else if(sucess == false){
                            String error = jsonResponse.getString("error");
                            String msj ="";

                            adapterDialog.hide();
                            if(error.equals("cedulaDupli")){

                                cedula.setError("Ya existe este usuario registrado");
                                msj =" Ya existe este usuario registrado ..!";

                            }

                            if(error.equals("correoDupli")){

                                correo.setError("Ya existe este correo registrado");
                                msj =" Ya existe este correo registrado ..!";

                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                        }


                    } catch (JSONException e) {
                        Toast toast = Toast.makeText(getContext(), "Ups algo ha salido mal, intente nuevamente", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                        adapterDialog.hide();
                        e.printStackTrace();
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


            String api = Validaciones.URL.toString() + "generic/editAcount.php";

            perfilRequest request = new perfilRequest(api,
                    Validaciones.typeUSer.toString(),
                    Validaciones.id.toString(),
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    direccionGet,
                    correoGet,
                    telefonoGet,
                    responseListener,
                    errorListener);


            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);

        }else {
            adapterDialog.hide();
            return;
        }




    }


}
