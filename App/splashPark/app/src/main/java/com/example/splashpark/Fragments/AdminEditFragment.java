package com.example.splashpark.Fragments;


import android.content.Intent;
import android.os.Bundle;

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
import com.example.splashpark.R;
import com.example.splashpark.Request.AdminRequest;
import com.example.splashpark.library.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminEditFragment extends Fragment implements View.OnClickListener {

    EditText  editNombres, editApe, editTel, editCed, editCorr,editPswd;
    Switch aSwitch;
    Button save;

    Validaciones validaciones = new Validaciones();

    String id;

    public AdminEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_edit, container, false);


        editCed = (EditText) view.findViewById(R.id.TxtAdminEditCed);
        editNombres = (EditText) view.findViewById(R.id.TxtAdminEditNombres);
        editApe = (EditText) view.findViewById(R.id.TxtAdminEditApellidos);
        editCorr = (EditText) view.findViewById(R.id.TxtAdminEditCorreo);
        editTel = (EditText) view.findViewById(R.id.TxtAdminEditTelefono);
        editPswd = (EditText) view.findViewById(R.id.TxtAdminEditPswd);


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


        editCed.setText(ced);
        editNombres.setText(nom);
        editApe.setText(ap);
        editCorr.setText(cor);
        editTel.setText(tel);

        if (ena.equals("TRUE")) {
            aSwitch.setChecked(true);

        } else {
            aSwitch.setChecked(false);

        }


        return view;
    }

    @Override
    public void onClick(View v) {

        if (v == save) {

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
                correoGet = editCorr.getText().toString(),
                pswdGet = editPswd.getText().toString();





        boolean isOk = true;

        if (cedulaGet.isEmpty()) {
            editCed.setError("Este campo es Obligatorio");
            isOk = false;
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




        if(isOk) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {

                            Toast toast = Toast.makeText(getContext(), "Administrador Modificado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else {
                            Toast toast = Toast.makeText(getContext(), "No se pudo Modificar", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast toast = Toast.makeText(getContext(), "no se econtro el servidor  " + e, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast toast = Toast.makeText(getContext(), "no se econtro el servidor " + error, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();


                }
            };

            String api = Validaciones.URL.toString() + "admin/updateAdmin.php";

            AdminRequest adminRequest = new AdminRequest(api,
                    id,
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    telefonoGet,
                    correoGet,
                    pswdGet,
                    enableGet,
                    responseListener,
                    errorListener);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(adminRequest);

        }

    }
}
