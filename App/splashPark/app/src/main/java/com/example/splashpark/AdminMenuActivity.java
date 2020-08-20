package com.example.splashpark;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.splashpark.Fragments.AdminAddFragment;
import com.example.splashpark.Fragments.AdminInicioFragment;
import com.example.splashpark.Fragments.AdminListFragment;
import com.example.splashpark.Fragments.CuentaFragment;
import com.example.splashpark.Fragments.ParqAddFragment;
import com.example.splashpark.Fragments.ParqEditFragment;
import com.example.splashpark.Fragments.ParqListFragment;
import com.example.splashpark.Fragments.UserAddFragment;
import com.example.splashpark.Fragments.UserListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import java.util.List;

public class AdminMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    String back = "";
    public  static  String idUser = "";

    String argPlus="";
    DrawerLayout drawer;
    Toolbar toolbar;
    TextView nombreMenu;
    public FloatingActionButton floatingActionButton;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_admin_menu);


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



    }
}
