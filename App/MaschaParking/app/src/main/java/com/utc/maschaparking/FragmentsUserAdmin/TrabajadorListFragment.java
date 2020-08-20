package com.utc.maschaparking.FragmentsUserAdmin;

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
import com.utc.maschaparking.Adapter.AdapterListaAdmin;
import com.utc.maschaparking.Adapter.AdapterListaUser;
import com.utc.maschaparking.Fragments.AdminEditFragment;
import com.utc.maschaparking.Fragments.UserEditFragment;
import com.utc.maschaparking.Models.Admin;
import com.utc.maschaparking.Models.User;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TrabajadorListFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    AdapterListaUser adapter = null;
    ListView listView1;
    ArrayList<User> listaUsuarios;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TrabajadorListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trabajador_list, container, false);


        listaUsuarios = new ArrayList<>();
        listView1 = (ListView) view.findViewById(R.id.listViewTrabajador);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshListTrabajador);

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


                if (adapter != null) {
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


        String url = Validaciones.URL.toString()+"user/get.php?tipo=trabajador";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Ups se ha producido un error verifica su conexión a internet !..", Toast.LENGTH_LONG).show();

        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {


        User user = null;

        JSONArray json = response.optJSONArray("usuario");

        try {

            boolean res = response.getBoolean("success");
            if (!res) {

                Toast.makeText(getContext(), "No hay información que mostrar..!", Toast.LENGTH_LONG).show();

                progress.hide();

                return;
            }


            for (int i = 0; i < json.length(); i++) {

                user = new User();
                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                user.setIdUser(jsonObject.optInt("idUser"));
                user.setCedulaUSer(jsonObject.optString("cedulaUser"));
                user.setNombresUSer(jsonObject.optString("nombresUser"));
                user.setApellidosUSer(jsonObject.optString("apellidosUser"));
                user.setTelefonoUSer(jsonObject.optString("telefonoUser"));
                user.setEmailUSer(jsonObject.optString("emailUser"));
                user.setDireccionUSer(jsonObject.optString("direccionUser"));
                user.setEnableUSer(jsonObject.optString("enableUser"));
                user.setParqueadero_idParq(jsonObject.optInt("parqueadero_idParq"));

                listaUsuarios.add(user);
            }
            progress.hide();
            adapter = new AdapterListaUser(this.getActivity(), listaUsuarios);

            listView1.setAdapter(adapter);
            listView1.setDivider(null);


            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;
                    String resultado = "";


                    User user = listaUsuarios.get(pos);

                    Bundle extras = new Bundle();
                    extras.putString("idUser", user.getIdUser().toString());
                    extras.putString("cedulaUser", user.getCedulaUSer().toString());
                    extras.putString("nombreUSer", user.getNombresUSer().toString());
                    extras.putString("apellidoUser", user.getApellidosUSer().toString());
                    extras.putString("telefonoUser", user.getTelefonoUSer().toString());
                    extras.putString("CorreoUser", user.getEmailUSer().toString());
                    extras.putString("enableUser", user.getEnableUSer().toString());
                    extras.putString("direccionUser", user.getDireccionUSer().toString());
                    extras.putString("parqueadero_idParq", user.getParqueadero_idParq().toString());

                    TrabajadorEditFragment vista = new TrabajadorEditFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.esenarioUser, vista).commit();
                    vista.setArguments(extras);


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}

