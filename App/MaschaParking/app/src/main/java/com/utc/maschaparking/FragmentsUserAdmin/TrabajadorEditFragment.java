package com.utc.maschaparking.FragmentsUserAdmin;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.Adapter.AdapterSpinner;
import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.UserRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrabajadorEditFragment extends Fragment implements View.OnClickListener{


    AdapterDialog adapterDialog =null;
    AdapterSpinner adapter;
    EditText cedula, nombres, apellidos, telefono, correo, direccion;
    Button save, cancel;
    String   idUser ="";
    Spinner spinner;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    Validaciones validaciones = new Validaciones();
    Switch aSwitch;


    public TrabajadorEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trabajador_edit, container, false);


        request = Volley.newRequestQueue(getContext());


        adapterDialog = new AdapterDialog(getContext());
        cedula = (EditText) view.findViewById(R.id.TxtTrabEditCed);
        nombres = (EditText) view.findViewById(R.id.TxtTrabEditNombres);
        direccion = (EditText) view.findViewById(R.id.TxtTrabEditdirec);
        apellidos = (EditText) view.findViewById(R.id.TxtTrabEditApellidos);
        telefono = (EditText) view.findViewById(R.id.TxtTrabEditTelefono);
        correo = (EditText) view.findViewById(R.id.TxtTrabEditCorreo);
        aSwitch=(Switch)view.findViewById(R.id.SwitchTrabEditHab);

        save = (Button) view.findViewById(R.id.BtnTrabEdit);
        cancel = (Button) view.findViewById(R.id.BtnTrabCancelEdit);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);

        idUser = getArguments().getString("idUser").toString();
        cedula.setText(getArguments().getString("cedulaUser").toString());
        nombres.setText(getArguments().getString("nombreUSer").toString());
        apellidos.setText(getArguments().getString("apellidoUser").toString());
        direccion.setText(getArguments().getString("direccionUser").toString());
        telefono.setText(getArguments().getString("telefonoUser").toString());
        correo.setText(getArguments().getString("CorreoUser").toString());

        if (getArguments().getString("enableUser").equals("TRUE")) {
            aSwitch.setChecked(true);

        } else {
            aSwitch.setChecked(false);

        }


        return  view;
    }




    @Override
    public void onClick(View v) {

        if (v == save) {

            adapterDialog.show("Realizando Cambios","Por Favor Espere...");
            updateAdmin();
        }

        if (v == cancel) {

        }

    }

    private void updateAdmin() {

        String enableGet = "FALSE";

        if (aSwitch.isChecked()) {
            enableGet = "TRUE";
        }

        String cedulaGet = cedula.getText().toString(),
                nombresGet = nombres.getText().toString(),
                apellidosGet = apellidos.getText().toString(),
                telefonoGet = telefono.getText().toString(),
                correoGet = correo.getText().toString(),
                direccionGet = direccion.getText().toString();






        boolean isOk = true;

        if (cedulaGet.isEmpty()) {
            cedula.setError("Este campo es Obligatorio");
            isOk = false;
        }else {
            boolean ok =  validaciones.cedulaIsOk(cedulaGet.toString());
            if(!ok){
                cedula.setError("Incorrecto");
                isOk = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("El número de cédula ingresado no es valida")
                        .setNegativeButton("Aceptar", null)
                        .create().show();
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
                            Toast toast = Toast.makeText(getContext(), "Propietario Modificado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else if(sucess == false){

                            adapterDialog.hide();
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


                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                        adapterDialog.hide();
                        Toast toast = Toast.makeText(getContext(), "Ups se ha producido un error, Intente nuevamente" , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    adapterDialog.hide();
                    Toast toast = Toast.makeText(getContext(), "Ups se ha producido un error, Intente nuevamente" , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();


                }
            };

            String api = Validaciones.URL.toString() + "user/edit.php";

            UserRequest userRequest = new UserRequest(api,
                    idUser,
                    Validaciones.idParq,
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    telefonoGet,
                    correoGet,
                    direccionGet,
                    enableGet,
                    responseListener,
                    errorListener);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(userRequest);

        }else {
            adapterDialog.hide();
            return;
        }

    }


}
