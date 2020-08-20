package com.utc.maschaparking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.Request.ParqRequest;
import com.utc.maschaparking.Request.perfilRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

public class recoverpswdActivity extends AppCompatActivity {

    Validaciones validaciones = new Validaciones();
    EditText recover;
    AdapterDialog adapterDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recoverpswd);
        recover = (EditText) findViewById(R.id.recoverEdit);

        adapterDialog = new AdapterDialog(this);
    }


public void recpswd(View view){


    adapterDialog.show("Enviando requisito","Por favor Espere...");
    recoverPswd();

}



    private void recoverPswd() {

        boolean isOk = true;

        String     rec = recover.getText().toString();


        if (rec.isEmpty()) {
            recover.setError("Este campo es Obligatorio");
            isOk = false;
        }


        if(isOk){

            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    JSONObject jsonResponse = null;
                    try {
                        String msj = "Por favor revise su correo electrónico y siga las instrucciones.";
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");
                        String  info = jsonResponse.getString("info");

                        if (sucess == true) {
                            if(info.equals("Enviado")){

                                adapterDialog.hide();

                            }else {
                                msj = "Por favor revise su conexion a internet e intente nuevamente.";
                                adapterDialog.hide();
                            }



                        } else if(sucess == false){
                              msj = "Lo sentimos, este correo no esta vinculado en nuestros servicios !";
                            adapterDialog.hide();

                        }

                            AlertDialog.Builder builder = new AlertDialog.Builder(recoverpswdActivity.this);
                            builder.setMessage(msj)
                                    .setTitle("Recuperación de contraseña")
                                    .setIcon(R.drawable.ic_key)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();




                    } catch (JSONException e) {
                        adapterDialog.hide();
                        Toast toast = Toast.makeText(recoverpswdActivity.this, "Ups algo ha salido mal, intente nuevamente"+e, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    adapterDialog.hide();
                    Toast toast = Toast.makeText(recoverpswdActivity.this, "Ups algo ha salido mal, intente nuevamente"+error , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            };

            String api = Validaciones.URL.toString() + "recpsswd.php";
            perfilRequest recover = new perfilRequest(api, rec.toString(), responseListener, errorListener);

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(recover);

        }else {
            adapterDialog.hide();
            return;
        }
    }



}
