package com.example.splashpark;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.android.volley.toolbox.Volley;
import com.example.splashpark.Request.SQLiteCon;
import com.example.splashpark.Request.loginRequest;
import com.example.splashpark.library.Validaciones;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class inicioSesion extends AppCompatActivity {
    EditText pswd, user;
    ProgressBar progressBar;
    Button Btning;
    Switch switchA;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);


        relativeLayout = (RelativeLayout) findViewById(R.id.inicioSesion);

        user = (EditText) findViewById(R.id.LoginTxtUser);
        pswd = (EditText) findViewById(R.id.LoginTxtPswd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Btning = (Button) findViewById(R.id.loginBtnLogin);
        switchA = (Switch) findViewById(R.id.switch1);
        switchA.setChecked(true);
        progressBar.setVisibility(View.INVISIBLE);

        obtenerPermisoGps();
        disableAct();
        verificarSesion();








    }

    public void login(View view) {


        savePswd();
        logOn();


    }


    public void logOn() {


        Validaciones validaciones = new Validaciones();

        validaciones.isDataConnectionAvailable(this);

        if (!validaciones.isDataConnectionAvailable(this)) {


            Snackbar snack = Snackbar.make(relativeLayout, "No hay conección a Internet..!", Snackbar.LENGTH_LONG)
                    .setDuration(4500);
            View view = snack.getView();
            TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            view.setBackground(ContextCompat.getDrawable(this, R.drawable.designsnackbar));
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;


            view.setLayoutParams(params);

            snack.show();


            enableAct();
            return;

        }

        disableAct();

        final String userSt = user.getText().toString(), pswdSt = pswd.getText().toString();


        boolean isok = true;
        if (userSt.isEmpty()) {
            user.setError("Este campo es Obligatorio");

            SQLiteCon db = new SQLiteCon(inicioSesion.this, "login", null, 1); //instancia clase base
            SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db

            String sql = "DELETE  FROM saveUser";
            BaseDatos.execSQL(sql);

            isok = false;
        }

        if (pswdSt.isEmpty()) {
            pswd.setError("Este campo es Obligatorio");

            SQLiteCon db = new SQLiteCon(inicioSesion.this, "login", null, 1); //instancia clase base
            SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db

            String sql = "DELETE  FROM saveUser";
            BaseDatos.execSQL(sql);

            isok = false;
        }


        if (isok) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");
                        if (sucess == true) {

                            enableAct();
                            Toast toast = Toast.makeText(inicioSesion.this, "Bienvenido " + jsonResponse.getString("nombresAdmin") + " " + jsonResponse.getString("apellidosAdmin"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                            Bundle extras = new Bundle();
                            extras.putString("idAdmin", jsonResponse.getString("idAdmin").toString());
                            extras.putString("correoAdmin", jsonResponse.getString("correoAdmin").toString());
                            extras.putString("apellidosAdmin", jsonResponse.getString("apellidosAdmin").toString());
                            extras.putString("nombresAdmin", jsonResponse.getString("nombresAdmin").toString());


                            Intent intent = new Intent(inicioSesion.this, AdminMenuActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);

                            finish();


                        } else {

                            enableAct();
                            AlertDialog.Builder builder = new AlertDialog.Builder(inicioSesion.this);
                            builder.setMessage("Usuario o contraseña incorrectos..!")
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                            SQLiteCon db = new SQLiteCon(inicioSesion.this, "login", null, 1); //instancia clase base
                            SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db

                            String sql = "DELETE  FROM saveUser";
                            BaseDatos.execSQL(sql);

                        }


                    } catch (JSONException e) {

                        enableAct();
                        Toast toast = Toast.makeText(inicioSesion.this, "" + e, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();


                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast toast = Toast.makeText(inicioSesion.this, "Ups Error..!" + error, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();

                    enableAct();
                }

            };

            loginRequest loginRequest = new loginRequest(userSt, pswdSt, responseListener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(loginRequest);
        }else {
            enableAct();
        }

    }

    public void enableAct() {

        Btning.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
        user.setEnabled(true);
        pswd.setEnabled(true);
        switchA.setEnabled(true);

    }

    public void disableAct() {

        Btning.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        user.setEnabled(false);
        pswd.setEnabled(false);
        switchA.setEnabled(false);

    }


    public void savePswd() {

        SQLiteCon db = new SQLiteCon(inicioSesion.this, "login", null, 1); //instancia clase base
        SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db


        if (switchA.isChecked()) {


            ContentValues register = new ContentValues();
            register.put("user", user.getText().toString());
            register.put("pswd", pswd.getText().toString());


            BaseDatos.insert("saveUser", null, register);


        } else {

            String sql = "DELETE  FROM saveUser";
            BaseDatos.execSQL(sql);
        }


    }


    private void verificarSesion() {


        SQLiteCon db = new SQLiteCon(this, "login", null, 1); //instancia clase base
        SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db


        Cursor fila = BaseDatos.rawQuery("SELECT * FROM saveUser ;", null);

        if (fila.getCount() == 0) {

            enableAct();

        } else {


            if (fila.moveToFirst()) {

                user.setText(fila.getString(1).toString());
                pswd.setText(fila.getString(2).toString());

            }

            logOn();


        }

    }


    public void obtenerPermisoGps() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this, "No tiene permisos de usar el GPS", Toast.LENGTH_SHORT);
        } else {


        }
    }
}





