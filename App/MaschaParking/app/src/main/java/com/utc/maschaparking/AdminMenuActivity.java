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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.utc.maschaparking.Fragments.AdminAddFragment;
import com.utc.maschaparking.Fragments.AdminInicioFragment;
import com.utc.maschaparking.Fragments.AdminListFragment;
import com.utc.maschaparking.Fragments.CuentaFragment;
import com.utc.maschaparking.Fragments.ParqAddFragment;
import com.utc.maschaparking.Fragments.ParqListFragment;
import com.utc.maschaparking.Fragments.UserAddFragment;
import com.utc.maschaparking.Fragments.UserListFragment;
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

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

public class AdminMenuActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{


    ProgressDialog progress;
    String back = "";
    public  static  String idUser = "";

    String argPlus="";
    DrawerLayout drawer;
    Toolbar toolbar;
    TextView nombreMenu;
    public FloatingActionButton floatingActionButton;



    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Validaciones.typeUSer = "Admin";
        setContentView(R.layout.activity_admin_menu);
        setSupportActionBar(toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.esenario, new AdminInicioFragment()).commit();


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Inicio");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);

        //----------------setea utilizar partes del banner---------------------
        View view = navigationView.getHeaderView(0);

        nombreMenu = (TextView) view.findViewById(R.id.nombreMenuAdmin);

        nombreMenu.setText(getIntent().getExtras().getString("nombresAdmin") + " " + getIntent().getExtras().getString("apellidosAdmin"));
        //----------------setea utilizar partes del banner---------------------

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        idUser = getIntent().getExtras().getString("idAdmin");


        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Saliendo Espere..");


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();



        FragmentManager fm = getSupportFragmentManager();


        if (id == R.id.nav_inicio) {



            floatingActionButton.setVisibility(View.INVISIBLE);

            fm.beginTransaction().replace(R.id.esenario, new AdminInicioFragment()).commit();

        } else if (id == R.id.nav_admin) {
            toolbar.setTitle("Administradores");
            // setSupportActionBar(toolbar);

            argPlus ="addAdmin";
            floatingActionButton.setVisibility(View.VISIBLE);
            fm.beginTransaction().replace(R.id.esenario, new AdminListFragment()).commit();

        }else if (id == R.id.nav_parq) {
            toolbar.setTitle("Parqueaderos");
            // setSupportActionBar(toolbar);
            argPlus = "addParq";
            floatingActionButton.setVisibility(View.VISIBLE);
            fm.beginTransaction().replace(R.id.esenario, new ParqListFragment()).commit();
        }else if (id == R.id.nav_Config) {
            toolbar.setTitle("Cuenta");
            // setSupportActionBar(toolbar);

            floatingActionButton.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.esenario, new CuentaFragment()).commit();
        }

        else if (id == R.id.nav_prop) {
            toolbar.setTitle("Propietarios");
            // setSupportActionBar(toolbar);
            argPlus ="addProp";
            floatingActionButton.setVisibility(View.VISIBLE);
            fm.beginTransaction().replace(R.id.esenario, new UserListFragment()).commit();
        } else if (id == R.id.nav_Close) {





            new AlertDialog.Builder(this)
                            .setTitle("Salir")
                            .setMessage("Está seguro de cerrar sesión?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            progress.setCancelable(false);

                            progress.show();


                            closeSession();

                            finish();


                            Intent intent = new Intent(AdminMenuActivity.this, inicioSesionActivity.class);
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
    public void add(View view) {

        // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //       .setAction("Action", null).show();

        FragmentManager fm = getSupportFragmentManager();
        if(argPlus.equals("addAdmin")){

            floatingActionButton.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.esenario, new AdminAddFragment()).commit();

        }
        if(argPlus.equals("addParq")){

            floatingActionButton.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.esenario, new ParqAddFragment()).commit();

        }
        if(argPlus.equals("addProp")){

            floatingActionButton.setVisibility(View.INVISIBLE);
            fm.beginTransaction().replace(R.id.esenario, new UserAddFragment()).commit();

        }


    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.esenario, new UserAddFragment()).commit();
    }



    private void  closeSession(){

        SQLiteCon db = new SQLiteCon(AdminMenuActivity.this, "login", null, 1); //instancia clase base
        SQLiteDatabase BaseDatos = db.getWritableDatabase(); //sette que hace la db
        String sql = "DELETE  FROM saveUser";
        BaseDatos.execSQL(sql);
        this.finish();

    }

}
