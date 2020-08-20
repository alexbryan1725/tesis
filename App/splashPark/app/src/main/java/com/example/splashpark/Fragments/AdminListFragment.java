package com.example.splashpark.Fragments;

import android.app.ProgressDialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.splashpark.Models.Admin;
import com.example.splashpark.R;
import com.example.splashpark.adapter.AdapterListaAdmin;

import com.example.splashpark.library.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdminListFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    // RecyclerView recyclerUsuarios;

    AdapterListaAdmin adapter = null;
    ListView listView1;
    ArrayList<Admin> listaUsuarios;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private SwipeRefreshLayout swipeRefreshLayout;

    public AdminListFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_list, container, false);



        listaUsuarios = new ArrayList<>();
        listView1 = (ListView) view.findViewById(R.id.listViewAdmin);
        // recyclerUsuarios.setLayoutManager(new LinearLayoutManager(this.getContext()));
        // recyclerUsuarios.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshListAdmin);

        request = Volley.newRequestQueue(getContext());
        progress = new ProgressDialog(getContext());
        progress.setCancelable(false);
        progress.setMessage("Porfavor Espere...");



        cargarWebService();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                swipeRefreshLayout.setRefreshing(false);


                if(adapter!=null){
                    adapter.clear();
                }
                listView1.setAdapter(null);
                cargarWebService();

            }
        });


        return view;
    }

    private void cargarWebService() {
        progress.show();


        String url = Validaciones.URL.toString()+"admin/getAdmin.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar " + error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {

        Admin admin = null;

        JSONArray json = response.optJSONArray("usuario");

        try {

            for (int i = 0; i < json.length(); i++) {

                admin = new Admin();
                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                admin.setIdAdmin(jsonObject.optInt("idAdmin"));
                admin.setCedulaAdmin(jsonObject.optString("cedulaAdmin"));
                admin.setNombresAdmin(jsonObject.optString("nombresAdmin"));
                admin.setApelldioAdmin(jsonObject.optString("apelldiosAdmin"));
                admin.setTelefonoAdmin(jsonObject.optString("telefonoAdmin"));
                admin.setCorreoAdmin(jsonObject.optString("correoAdmin"));
                admin.setEnableAdmin(jsonObject.optString("enableAdmin"));
                listaUsuarios.add(admin);
            }
            progress.hide();

            adapter = new AdapterListaAdmin(this.getActivity(), listaUsuarios);

            listView1.setAdapter(adapter);
            listView1.setDivider(null);


            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;
                    String resultado = "";


                    Admin admi = listaUsuarios.get(pos);

                    Bundle extras = new Bundle();
                    extras.putString("idAdmin", admi.getIdAdmin().toString());
                    extras.putString("cedulaAdmin", admi.getCedulaAdmin().toString());
                    extras.putString("nombreAdmin", admi.getNombresAdmin().toString());
                    extras.putString("apellidoAdmin", admi.getApelldioAdmin().toString());
                    extras.putString("telefonoAdmin", admi.getTelefonoAdmin().toString());
                    extras.putString("CorreoAdmin", admi.getCorreoAdmin().toString());
                    extras.putString("EnableAdmin", admi.getEnableAdmin().toString());
                    extras.putString("accion", "editAdmin");


                    AdminEditFragment vista = new AdminEditFragment();
                    FragmentManager fm =  getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.esenario,  vista).commit();
                    vista.setArguments(extras);


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
