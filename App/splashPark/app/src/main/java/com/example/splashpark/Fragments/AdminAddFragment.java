package com.example.splashpark.Fragments;


import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.splashpark.R;
import com.example.splashpark.Request.AdminRequest;
import com.example.splashpark.inicioSesion;
import com.example.splashpark.library.Validaciones;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminAddFragment extends Fragment implements View.OnClickListener{

    EditText cedula, nombres, apellidos, telefono, correo, pswd;
    Button save;

    Validaciones validaciones = new Validaciones();


    public AdminAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_add, container, false);

        cedula = (EditText) view.findViewById(R.id.TxtAdminInsertCed);
        nombres = (EditText) view.findViewById(R.id.TxtAdminInsertNombres);
        apellidos = (EditText) view.findViewById(R.id.TxtAdminInsertApellidos);
        telefono = (EditText) view.findViewById(R.id.TxtAdminInsertTelefono);
        correo = (EditText) view.findViewById(R.id.TxtAdminInsertCorreo);
        pswd = (EditText) view.findViewById(R.id.TxtAdminInsertCed);


        save=(Button)view.findViewById(R.id.BtnAdminSave);
        save.setOnClickListener( this);

        return view;
    }


    private void registerAdmin() {


        String  cedulaGet = cedula.getText().toString(),
                nombresGet = nombres.getText().toString(),
                apellidosGet = apellidos.getText().toString(),
                telefonoGet = telefono.getText().toString(),
                correoGet = correo.getText().toString(),
                pswdGet = pswd.getText().toString();



        boolean isOk = true;

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
        if (pswdGet.isEmpty()) {
            pswd.setError("Este campo es Obligatorio");
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

                            Toast toast = Toast.makeText(getContext(), "Administrador Agregado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else {

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            };


            String api = Validaciones.URL.toString() + "admin/insertAdmin.php";

            AdminRequest adminRequest = new AdminRequest(api,
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    telefonoGet,
                    correoGet,
                    pswdGet,
                    responseListener,
                    errorListener);


            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(adminRequest);

        }

    }


    @Override
    public void onClick(View v) {
        if(v == save ){

          if(!validaciones.isDataConnectionAvailable(getContext())){
              Snackbar mySnackbar = Snackbar.make(getView(), "No tiene conección con Internet..!  ", Snackbar.LENGTH_LONG).
                      setDuration(2000);
              View view = mySnackbar.getView();
              TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

              txtv.setGravity(Gravity.CENTER_HORIZONTAL);

              txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
              view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.designsnackbar));
              mySnackbar.show();
              return;
            }



            registerAdmin();


        }


    }
}
