package com.example.splashpark.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.splashpark.R;

public class AdminEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

       getSupportActionBar().setTitle("Regresar");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);






    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }







}
