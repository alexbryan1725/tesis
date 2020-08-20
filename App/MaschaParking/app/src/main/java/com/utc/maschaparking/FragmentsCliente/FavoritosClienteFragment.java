package com.utc.maschaparking.FragmentsCliente;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.utc.maschaparking.Adapter.AdapterAskCli;
import com.utc.maschaparking.Adapter.AdapterFavorite;
import com.utc.maschaparking.Adapter.AdapterListaParq;
import com.utc.maschaparking.Fragments.ParqEditFragment;
import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritosClienteFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    AdapterFavorite adapter = null;
    ListView listView1;
    ArrayList<Parqueadero> listaParq;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    private SwipeRefreshLayout swipeRefreshLayout;
    public FavoritosClienteFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final int reload = 0;

        View  view = inflater.inflate(R.layout.fragment_favoritos_cliente, container, false);

        listaParq = new ArrayList<>();
        listView1 = (ListView) view.findViewById(R.id.listViewFav);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshListFav);
        request = Volley.newRequestQueue(getContext());

        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (adapter != null) {
                    adapter.clear();
                }


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




                handler.postDelayed(this,4000);//se ejecutara


            }
        },1000);










        return view;
    }



    private void cargarWebService() {



        String url = Validaciones.URL.toString() + "cliente/getFavoritos.php?idClie="+Validaciones.id;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {


    }

    @Override
    public void onResponse(JSONObject response) {


        Parqueadero parqueadero;
        JSONArray json = response.optJSONArray("parqueadero");

        try {

            boolean res = response.getBoolean("success");
            if (!res) {
                if (adapter != null) {
                    adapter.clear();
                    listView1.setAdapter(adapter);
                }


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


            adapter = new AdapterFavorite(this.getActivity(), listaParq);

            listView1.setAdapter(adapter);
            listView1.setDivider(null);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
