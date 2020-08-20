package com.utc.maschaparking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.Request.adminRequest;
import com.utc.maschaparking.Request.loginRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

public class registerActivity extends AppCompatActivity {

    EditText cedula, nombres, apellidos, telefono, correo, pswd,pswd2,direccion;
    AdapterDialog adapterDialog = null;
    RelativeLayout relativeLayout;

    Validaciones validaciones = new Validaciones();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        relativeLayout=(RelativeLayout)findViewById(R.id.relativeRegister) ;

        cedula = (EditText) findViewById(R.id.TxtAddClieCed);
        nombres = (EditText) findViewById(R.id.TxtAddClieNombres);
        apellidos = (EditText) findViewById(R.id.TxtAddClieApellidos);
        telefono = (EditText) findViewById(R.id.TxtAddClieTelefono);
        correo = (EditText) findViewById(R.id.TxtAddClieCorreo);
        pswd = (EditText) findViewById(R.id.TxtAddCliePswd);
        pswd2 = (EditText) findViewById(R.id.TxtAddCliePswd2);
        direccion = (EditText) findViewById(R.id.TxtAddCliedirec);
        adapterDialog = new AdapterDialog(this);

    }

    public void register(View view){
        adapterDialog.show("Registrando","Espere Por Favor ..!");

        if(!validaciones.isDataConnectionAvailable(registerActivity.this)){


            Snackbar mySnackbar = Snackbar.make(relativeLayout, "No tiene conección con Internet..!  ", Snackbar.LENGTH_LONG).
                    setDuration(2000);
            View view2 = mySnackbar.getView();
            TextView txtv = (TextView)view2.findViewById(R.id.snackbar_text);

            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            view2.setBackground(ContextCompat.getDrawable(registerActivity.this, R.drawable.designsnackbar));
            mySnackbar.show();


            adapterDialog.hide();
            return;
        }
        arg();


    }

    public void cancel(View view){

    }


    private void arg(){


        String  cedulaGet = cedula.getText().toString(),
                nombresGet = nombres.getText().toString(),
                apellidosGet = apellidos.getText().toString(),
                telefonoGet = telefono.getText().toString(),
                direccionGet = direccion.getText().toString(),
                correoGet = correo.getText().toString(),
                pswdGet = pswd.getText().toString(),
                pswdGet2 = pswd2.getText().toString();



        boolean isOk = true;

        if (cedulaGet.isEmpty()) {
            cedula.setError("Este campo es Obligatorio");
            isOk = false;
        }else{
            boolean ok =  validaciones.cedulaIsOk(cedulaGet.toString());
            if(!ok){
                cedula.setError("Incorrecto");
                isOk = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(registerActivity.this);
                builder.setMessage("El número de cédula ingresado no es valida")
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

        if (pswdGet.isEmpty()) {
            pswd.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (pswdGet2.isEmpty()) {
            pswd2.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (pswdGet.equals(pswdGet2.toString()) && pswdGet2.equals(pswdGet.toString())) {

        }else {
            pswd.setError("No coincide las contraseñas");
            pswd2.setError("No coincide las contraseñas");
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
                            Toast toast = Toast.makeText(registerActivity.this, "Registrado con éxito", Toast.LENGTH_SHORT);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(registerActivity.this);
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                        }


                    } catch (JSONException e) {
                        adapterDialog.hide();
                        Toast toast = Toast.makeText(registerActivity.this,e.toString() , Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(registerActivity.this,error.toString() , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            };


            String api = Validaciones.URL.toString() + "cliente/insertCliente.php";

            loginRequest registerRequest = new loginRequest(api,
                    cedulaGet,
                    nombresGet,
                    apellidosGet,
                    direccionGet,
                    correoGet,
                    telefonoGet,
                    pswdGet,
                    responseListener,
                    errorListener);


            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(registerRequest);

        }else {
            adapterDialog.hide();
            return;
        }

    }


}
