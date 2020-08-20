package com.utc.maschaparking.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.adminRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;


public class AdminEditFragment extends Fragment implements View.OnClickListener {

    EditText editNombres, editApe, editTel, editCed, editCorr, editPswd, editDir;
    Switch aSwitch;
    Button save;

    Validaciones validaciones = new Validaciones();


    AdapterDialog adapterDialog = null;

    String id;

    public AdminEditFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_edit, container, false);


        editCed = (EditText) view.findViewById(R.id.TxtAdminEditCed);
        editNombres = (EditText) view.findViewById(R.id.TxtAdminEditNombres);
        editApe = (EditText) view.findViewById(R.id.TxtAdminEditApellidos);
        editCorr = (EditText) view.findViewById(R.id.TxtAdminEditCorreo);
        editTel = (EditText) view.findViewById(R.id.TxtAdminEditTelefono);
        editPswd = (EditText) view.findViewById(R.id.TxtAdminEditPswd);
        editDir = (EditText) view.findViewById(R.id.TxtAdminEditdir);


        aSwitch = (Switch) view.findViewById(R.id.SwitchAdminEditHab);

        save = (Button) view.findViewById(R.id.BtnEditarSave);
        save.setOnClickListener(this);


        id = getArguments().getString("idAdmin").toString();

        String ced = getArguments().getString("cedulaAdmin").toString();
        String nom = getArguments().getString("nombreAdmin").toString();
        String ap = getArguments().getString("apellidoAdmin").toString();
        String tel = getArguments().getString("telefonoAdmin").toString();
        String cor = getArguments().getString("CorreoAdmin").toString();
        String ena = getArguments().getString("EnableAdmin").toString();
        String dir = getArguments().getString("direccionAdmin").toString();


        editCed.setText(ced);
        editNombres.setText(nom);
        editApe.setText(ap);
        editCorr.setText(cor);
        editTel.setText(tel);
        editDir.setText(dir);

        if (ena.equals("TRUE")) {
            aSwitch.setChecked(true);

        } else {
            aSwitch.setChecked(false);

        }

        adapterDialog = new AdapterDialog(getContext());

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == save) {

            adapterDialog.show("Realizando Cambios","Espere Por Favor ..!");
            editFun();
        }

    }

    private void editFun() {
        String enableGet = "FALSE";

        if (aSwitch.isChecked()) {
            enableGet = "TRUE";
        }

        String cedulaGet = editCed.getText().toString(),
                nombresGet = editNombres.getText().toString(),
                apellidosGet = editApe.getText().toString(),
                telefonoGet = editTel.getText().toString(),
                direccionGet = editDir.getText().toString(),
                correoGet = editCorr.getText().toString(),
                pswdGet = editPswd.getText().toString();


        boolean isOk = true;

        if (cedulaGet.isEmpty()) {
            editCed.setError("Este campo es Obligatorio");
            isOk = false;
        } else {
            boolean ok = validaciones.cedulaIsOk(cedulaGet.toString());
            if (!ok) {

                editCed.setError("Incorrecto");
                isOk = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("El numero de cédula ingresado no es valida")
                        .setNegativeButton("Aceptar", null)
                        .create().show();
            }
        }

        if (nombresGet.isEmpty()) {
            editNombres.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (telefonoGet.isEmpty()) {
            editTel.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (correoGet.isEmpty()) {
            editCorr.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (direccionGet.isEmpty()) {
            editDir.setError("Este campo es Obligatorio");
            isOk = false;
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
                            Toast toast = Toast.makeText(getContext(), "Administrador Modificado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else if (sucess == false) {
                            adapterDialog.hide();
                            String error = jsonResponse.getString("error");
                            String msj = "";


                            if (error.equals("cedulaDupli")) {

                                editCed.setError("Ya existe este usuario registrado");
                                msj = " Ya existe este usuario registrado ..!";
                            }

                            if (error.equals("correoDupli")) {

                                editCorr.setError("Ya existe este correo registrado");
                                msj = " Ya existe este correo registrado ..!";
                            }


                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();


                        }


                    } catch (JSONException e) {

                        adapterDialog.hide();
                        e.printStackTrace();

                        Toast toast = Toast.makeText(getContext(), "Ups algo ha salido mal, reintente de nuevo" , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    adapterDialog.hide();
                    Toast toast = Toast.makeText(getContext(), "Ups algo ha salido mal, reintente de nuevo" , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();


                }
            };

            String api = Validaciones.URL.toString() + "admin/updateAdmin.php";

            adminRequest adminRequest = new adminRequest(api,
                    id,
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    telefonoGet,
                    correoGet,
                    direccionGet,
                    pswdGet,
                    enableGet,
                    responseListener,
                    errorListener);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(adminRequest);

        }else {
            adapterDialog.hide();
            return;
        }

    }
}
