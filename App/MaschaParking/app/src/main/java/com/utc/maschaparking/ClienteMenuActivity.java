package com.utc.maschaparking;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.utc.maschaparking.Fragments.CuentaFragment;
import com.utc.maschaparking.FragmentsCliente.ClienteFindMpFragment;
import com.utc.maschaparking.FragmentsCliente.ClienteMovimientosFragment;
import com.utc.maschaparking.FragmentsCliente.FavoritosClienteFragment;
import com.utc.maschaparking.FragmentsCliente.InicioClieFragment;
import com.utc.maschaparking.FragmentsCliente.VehiculoClienteListFragment;
import com.utc.maschaparking.Request.SQLiteCon;
import com.utc.maschaparking.Validaciones.Validaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ClienteMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    String argPlus = "";
    DrawerLayout drawer;
    Toolbar toolbar;
    TextView nombreMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Validaciones.typeUSer = "Cliente";
        setContentView(R.layout.activity_cliente_menu);
        toolbar = findViewById(R.id.toolbarClie);

        setSupportActionBar(toolbar);


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.esenarioClie, new InicioClieFragment()).commit();

        toolbar.setTitle("Inicio");
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layoutClie);

        NavigationView navigationView = findViewById(R.id.nav_viewClie);

        View view = navigationView.getHeaderView(0);

        nombreMenu = (TextView) view.findViewById(R.id.nombreMenuClie);

         nombreMenu.setText(getIntent().getExtras().getString("nombresClie") + " " + getIntent().getExtras().getString("apellidosClie"));
        //----------------setea utilizar partes del banner---------------------

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cliente_menu, menu);

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        FragmentManager fm = getSupportFragmentManager();


        if (id == R.id.nav_inicioClie) {

            fm.beginTransaction().replace(R.id.esenarioClie, new InicioClieFragment()).commit();

        } else if (id == R.id.nav_CloseClie) {


            new AlertDialog.Builder(this)
                    .setTitle("Salir")
                    .setMessage("Está seguro de cerrar sesión?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            closeSession();
                            Intent intent = new Intent(ClienteMenuActivity.this, inicioSesionActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        }else if(id==R.id.nav_ConfigClie){

            toolbar.setTitle("Cuenta");
            fm.beginTransaction().replace(R.id.esenarioClie, new CuentaFragment()).commit();


        }else if (id == R.id.nav_favoritosClie) {
            toolbar.setTitle("Favoritos");
            fm.beginTransaction().replace(R.id.esenarioClie, new FavoritosClienteFragment()).commit();
        }
        else if (id == R.id.nav_vechiculosClie) {
            toolbar.setTitle("Mis Vehículos");
            fm.beginTransaction().replace(R.id.esenarioClie, new VehiculoClienteListFragment()).commit();
        }
        else if (id == R.id.nav_transaccionesClie) {
            toolbar.setTitle("Mis Movimientos");
            fm.beginTransaction().replace(R.id.esenarioClie, new ClienteMovimientosFragment()).commit();
        }
        else if (id == R.id.nav_lista) {
            toolbar.setTitle("Buscar Parqueadero");
            fm.beginTransaction().replace(R.id.esenarioClie, new ClienteFindMpFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layoutClie);
        drawer.closeDrawer(GravityCompat.START);
        return true;


    }


    private void closeSession() {

        SQLiteCon db = new SQLiteCon(ClienteMenuActivity.this, "login", null, 1); //instancia clase base
        SQLiteDatabase BaseDatos = db.getWritableDatabase();
        String sql = "DELETE  FROM saveUser";
        BaseDatos.execSQL(sql);

        this.finish();

    }

    @Override
    public void onBackPressed() {



    }

}
