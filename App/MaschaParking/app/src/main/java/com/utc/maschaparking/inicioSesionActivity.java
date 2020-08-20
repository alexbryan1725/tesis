package com.utc.maschaparking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Request.SQLiteCon;
import com.utc.maschaparking.Request.loginRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;

public class inicioSesionActivity extends AppCompatActivity {
    EditText pswd, user;
    ProgressBar progressBar;
    Button Btning, newReg;
    Switch switchA;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        relativeLayout = (RelativeLayout)findViewById(R.id.viewInicio) ;


        user = (EditText) findViewById(R.id.LoginTxtUser);
        pswd = (EditText) findViewById(R.id.LoginTxtPswd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Btning = (Button) findViewById(R.id.loginBtnLogin);
        newReg  = (Button) findViewById(R.id.loginBtnRegister);
        switchA = (Switch) findViewById(R.id.switch1);
        switchA.setChecked(true);
        progressBar.setVisibility(View.INVISIBLE);




        obtenerPermisoGps();
        disableAct();
        verificarSesion();








    }

    public void register(View view) {

        Intent intent = new Intent(inicioSesionActivity.this, registerActivity.class);
        startActivity(intent);

    }

    public void login(View view) {


        savePswd();
        logOn();


    }


    public void logOn() {


        final Validaciones validaciones = new Validaciones();

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

            SQLiteCon db = new SQLiteCon(inicioSesionActivity.this, "login", null, 1); //instancia clase base
            SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db

            String sql = "DELETE  FROM saveUser";
            BaseDatos.execSQL(sql);

            isok = false;
        }

        if (pswdSt.isEmpty()) {
            pswd.setError("Este campo es Obligatorio");

            SQLiteCon db = new SQLiteCon(inicioSesionActivity.this, "login", null, 1); //instancia clase base
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

                            String type = jsonResponse.getString("type").toString();

                            if(type.equals("Admin")){

                            enableAct();
                            Toast toast = Toast.makeText(inicioSesionActivity.this, "Bienvenido " + jsonResponse.getString("nombres") + " " + jsonResponse.getString("apellidos"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                            Bundle extras = new Bundle();
                            extras.putString("idAdmin", jsonResponse.getString("id").toString());
                            extras.putString("correoAdmin", jsonResponse.getString("correo").toString());
                            extras.putString("apellidosAdmin", jsonResponse.getString("apellidos").toString());
                            extras.putString("nombresAdmin", jsonResponse.getString("nombres").toString());

                                Validaciones.typeUSer = "Admin";
                                Validaciones.id = jsonResponse.getString("id").toString();
                                Validaciones.cedula = jsonResponse.getString("cedula").toString();
                                Validaciones.nombres = jsonResponse.getString("nombres").toString();
                                Validaciones.apellidos = jsonResponse.getString("apellidos").toString();
                                Validaciones.direccion = jsonResponse.getString("direccion").toString();
                                Validaciones.telefono = jsonResponse.getString("telefono").toString();
                                Validaciones.correo = jsonResponse.getString("correo").toString();


                            Intent intent = new Intent(inicioSesionActivity.this, AdminMenuActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                            finish();
                            return;

                            }

                            if(type.equals("User")){

                                String validUser = jsonResponse.getString("userType");

                                enableAct();
                                Toast toast = Toast.makeText(inicioSesionActivity.this, "Bienvenido " + jsonResponse.getString("nombres") + " " + jsonResponse.getString("apellidos"), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();

                                Bundle extras = new Bundle();
                                extras.putString("idUser", jsonResponse.getString("id").toString());
                                extras.putString("correoUser", jsonResponse.getString("correo").toString());
                                extras.putString("apellidosUser", jsonResponse.getString("apellidos").toString());
                                extras.putString("nombresUser", jsonResponse.getString("nombres").toString());

                                Validaciones.typeUSer = "User";
                                Validaciones.idParq= jsonResponse.getString("idParq").toString();
                                Validaciones.id = jsonResponse.getString("id").toString();
                                Validaciones.cedula = jsonResponse.getString("cedula").toString();
                                Validaciones.nombres = jsonResponse.getString("nombres").toString();
                                Validaciones.apellidos = jsonResponse.getString("apellidos").toString();
                                Validaciones.direccion = jsonResponse.getString("direccion").toString();
                                Validaciones.telefono = jsonResponse.getString("telefono").toString();
                                Validaciones.correo = jsonResponse.getString("correo").toString();

                                if(validUser.equals("propietario")){

                                    Intent intent = new Intent(inicioSesionActivity.this, UserAdminActivity.class);
                                    intent.putExtras(extras);
                                    startActivity(intent);
                                    finish();

                                }else {



                                }





                            }

                            if(type.equals("Cliente")){



                                enableAct();
                                Toast toast = Toast.makeText(inicioSesionActivity.this, "Bienvenido " + jsonResponse.getString("nombres") + " " + jsonResponse.getString("apellidos"), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                toast.show();
                                Bundle extras = new Bundle();
                                extras.putString("idClie", jsonResponse.getString("id").toString());
                                extras.putString("correoClie", jsonResponse.getString("correo").toString());
                                extras.putString("apellidosClie", jsonResponse.getString("apellidos").toString());
                                extras.putString("nombresClie", jsonResponse.getString("nombres").toString());

                                Validaciones.typeUSer = "Cliente";
                                Validaciones.id = jsonResponse.getString("id").toString();
                                Validaciones.cedula = jsonResponse.getString("cedula").toString();
                                Validaciones.nombres = jsonResponse.getString("nombres").toString();
                                Validaciones.apellidos = jsonResponse.getString("apellidos").toString();
                                Validaciones.direccion = jsonResponse.getString("direccion").toString();
                                Validaciones.telefono = jsonResponse.getString("telefono").toString();
                                Validaciones.correo = jsonResponse.getString("correo").toString();

                                Intent intent = new Intent(inicioSesionActivity.this, ClienteMenuActivity.class);
                                intent.putExtras(extras);
                                startActivity(intent);
                                finish();
                            }





                        } else {

                            enableAct();
                            AlertDialog.Builder builder = new AlertDialog.Builder(inicioSesionActivity.this);
                            builder.setMessage("Usuario o contraseña incorrectos..!")
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                            SQLiteCon db = new SQLiteCon(inicioSesionActivity.this, "login", null, 1); //instancia clase base
                            SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db

                            String sql = "DELETE  FROM saveUser";
                            BaseDatos.execSQL(sql);

                        }


                    } catch (JSONException e) {

                        enableAct();
                        Toast toast = Toast.makeText(inicioSesionActivity.this, "Ups se ha producido un error, Intente nuevamente ...!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();


                    }

                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast toast = Toast.makeText(inicioSesionActivity.this, "Ups se ha producido un error, verifique su conexion e intente de nuevo..!", Toast.LENGTH_SHORT);
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
        newReg.setEnabled(true);
        switchA.setEnabled(true);

    }

    public void disableAct() {

        Btning.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        newReg.setEnabled(false);
        user.setEnabled(false);
        pswd.setEnabled(false);
        switchA.setEnabled(false);

    }


    public void savePswd() {

        SQLiteCon db = new SQLiteCon(inicioSesionActivity.this, "login", null, 1); //instancia clase base
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



    public void recover(View view){

        Intent intent = new Intent(inicioSesionActivity.this, recoverpswdActivity.class);
        startActivity(intent);

    }
}

