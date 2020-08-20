package com.utc.maschaparking;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.utc.maschaparking.Fragments.AdminAddFragment;
import com.utc.maschaparking.Fragments.AdminInicioFragment;
import com.utc.maschaparking.Fragments.CuentaFragment;
import com.utc.maschaparking.Fragments.ParqAddFragment;
import com.utc.maschaparking.Fragments.UserAddFragment;
import com.utc.maschaparking.FragmentsUserAdmin.AddCobUserFragment;
import com.utc.maschaparking.FragmentsUserAdmin.EditParqUserAdminFragment;
import com.utc.maschaparking.FragmentsUserAdmin.MakeCobroFragment;
import com.utc.maschaparking.FragmentsUserAdmin.TrabajadorListFragment;
import com.utc.maschaparking.FragmentsUserAdmin.UserAdminInicioFragment;
import com.utc.maschaparking.Request.SQLiteCon;
import com.utc.maschaparking.Validaciones.Validaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UserAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    ProgressDialog progress;
    String back = "";
    public  static  String idUser = "";

    String argPlus="";
    DrawerLayout drawer;
    Toolbar toolbar;
    TextView nombreMenu;
    public FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validaciones.typeUSer = "User";

        setContentView(R.layout.activity_user_admin);
        setSupportActionBar(toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabUserAdmin);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.esenarioUser, new UserAdminInicioFragment()).commit();


        toolbar = findViewById(R.id.toolbarUserAdmin);
        toolbar.setTitle("Inicio");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_viewUserAdmin);

        //----------------setea utilizar partes del banner---------------------
        View view = navigationView.getHeaderView(0);

        nombreMenu = (TextView) view.findViewById(R.id.nombreUserAdmin);

        nombreMenu.setText(getIntent().getExtras().getString("nombresUser") + " " + getIntent().getExtras().getString("apellidosUser"));
        //----------------setea utilizar partes del banner---------------------

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        idUser = getIntent().getExtras().getString("idAUser");


        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Saliendo Espere..");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_admin, menu);
        return true;
    }



    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();



        FragmentManager fm = getSupportFragmentManager();


        if (id == R.id.nav_inicioUserAdmin) {

            floatingActionButton.setVisibility(View.INVISIBLE);

            fm.beginTransaction().replace(R.id.esenarioUser, new MakeCobroFragment()).commit();

        }else if (id == R.id.nav_cobradorUserAdmin) {


            toolbar.setTitle("Trabajador");
            argPlus="addTrabajador";
            floatingActionButton.setVisibility(View.VISIBLE);
            fm.beginTransaction().replace(R.id.esenarioUser, new TrabajadorListFragment()).commit();


        } else if (id == R.id.nav_parqUserAdmin) {


            toolbar.setTitle("Mi Parqueadero");

            floatingActionButton.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.esenarioUser, new EditParqUserAdminFragment()).commit();


        }else if (id == R.id.nav_ConfigUserAdmin) {
            toolbar.setTitle("Cuenta");
            // setSupportActionBar(toolbar);
            floatingActionButton.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.esenarioUser, new CuentaFragment()).commit();
        } else if (id == R.id.nav_CloseUserAdmin) {


            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("Está seguro de cerrar sesión?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            progress.setCancelable(false);

                            progress.show();


                            closeSession();

                            finish();


                            Intent intent = new Intent(UserAdminActivity.this, inicioSesionActivity.class);
                            startActivity(intent);



                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





    @SuppressLint("RestrictedApi")
    public void addUser(View view) {

        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //       .setAction("Action", null).show();

        FragmentManager fm = getSupportFragmentManager();
        if(argPlus.equals("addTrabajador")){

            floatingActionButton.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.esenarioUser, new AddCobUserFragment()).commit();

        }


    }





    private void  closeSession(){

        SQLiteCon db = new SQLiteCon(UserAdminActivity.this, "login", null, 1); //instancia clase base
        SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db
        String sql = "DELETE  FROM saveUser";
        BaseDatos.execSQL(sql);
        this.finish();

    }
}
