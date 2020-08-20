package com.utc.maschaparking.Fragments;

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
import com.utc.maschaparking.Adapter.AdapterListaParq;
import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ParqListFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    AdapterListaParq adapter = null;
    ListView listView1;
    ArrayList<Parqueadero> listaParq;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private SwipeRefreshLayout swipeRefreshLayout;


    public ParqListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parq_list, container, false);


        listaParq = new ArrayList<>();
        listView1 = (ListView) view.findViewById(R.id.listViewParq);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshListParq);
        request = Volley.newRequestQueue(getContext());
        progress = new ProgressDialog(getContext());
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


        String url = Validaciones.URL.toString() + "parqueadero/getParq.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Ups se ha producio un error, Verifique su conexión a internet!..", Toast.LENGTH_LONG).show();

        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {


        Parqueadero parqueadero;
        JSONArray json = response.optJSONArray("parqueadero");

        try {

            boolean res = response.getBoolean("success");
            if (!res) {

                Toast.makeText(getContext(), "No hay información que mostrar..!" , Toast.LENGTH_LONG).show();

                progress.hide();

                return;
            }

            for (int i = 0; i < json.length(); i++) {

                parqueadero = new Parqueadero();
                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                parqueadero.setIdParque(jsonObject.optInt("idParq"));
                parqueadero.setNombreParq(jsonObject.optString("nombreParq"));
                parqueadero.setCorreoParq(jsonObject.optString("correoParq"));
                parqueadero.setTelefonoParq(jsonObject.optString("telefonoParq"));
                parqueadero.setDireccionParq(jsonObject.optString("direccionParq"));
                parqueadero.setLatiParq(jsonObject.optDouble("latiParq"));
                parqueadero.setLongParq(jsonObject.optDouble("longParq"));
                parqueadero.setRucParq(jsonObject.optString("rucParq"));
                parqueadero.setAlquilerParq(jsonObject.optDouble("alquilerParq"));
                parqueadero.setFraccionParq(jsonObject.optDouble("fraccionParq"));
                parqueadero.setNumeroParq(jsonObject.optInt("numeroParq"));
                parqueadero.setPlazaParq(jsonObject.optInt("plazaParq"));
                parqueadero.setEnableParq(jsonObject.optString("enableParq"));


                listaParq.add(parqueadero);
            }
            progress.hide();

            adapter = new AdapterListaParq(this.getActivity(), listaParq);

            listView1.setAdapter(adapter);
            listView1.setDivider(null);


            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;


                    Parqueadero parqueadero1 = listaParq.get(pos);

                    Bundle extras = new Bundle();


                    extras.putString("idParq", parqueadero1.getIdParque().toString());
                    extras.putString("nombreParq", parqueadero1.getNombreParq().toString());
                    extras.putString("correoParq", parqueadero1.getCorreoParq().toString());
                    extras.putString("telefonoParq", parqueadero1.getTelefonoParq().toString());
                    extras.putString("direccionParq", parqueadero1.getDireccionParq().toString());
                    extras.putString("longParq", String.valueOf(parqueadero1.getLongParq()));
                    extras.putString("latiParq", String.valueOf(parqueadero1.getLatiParq()));
                    extras.putString("rucParq", String.valueOf(parqueadero1.getRucParq()));
                    extras.putString("plazaParq", String.valueOf(parqueadero1.getPlazaParq()));
                    extras.putString("enableParq", parqueadero1.getEnableParq().toString());
                    extras.putString("precioParq", String.valueOf(parqueadero1.getFraccionParq()));


                    ParqEditFragment vista = new ParqEditFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.esenario, vista).commit();
                    vista.setArguments(extras);

                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
