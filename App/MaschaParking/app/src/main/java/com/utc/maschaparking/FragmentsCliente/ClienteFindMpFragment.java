package com.utc.maschaparking.FragmentsCliente;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.Adapter.AdapterAskCli;
import com.utc.maschaparking.Adapter.AdapterListaParq;
import com.utc.maschaparking.Adapter.AdapterListaParqClie;
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
public class ClienteFindMpFragment extends Fragment   implements Response.ErrorListener, Response.Listener<JSONObject>, SearchView.OnQueryTextListener{

    AdapterListaParqClie adapter = null;
    ListView listView1;
    ArrayList<Parqueadero> listaParq;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    SearchView finder ;

    private SwipeRefreshLayout swipeRefreshLayout;

    public ClienteFindMpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cliente_find_mp, container, false);


        listaParq = new ArrayList<>();
        listView1 = (ListView) view.findViewById(R.id.listViewClieMP);
        finder = (SearchView) view.findViewById(R.id.findClieMP);
        finder.setOnQueryTextListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshListClieMP);
        request = Volley.newRequestQueue(getContext());





        cargarWebService(finder.getQuery().toString());


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                swipeRefreshLayout.setRefreshing(false);


                if (adapter != null) {
                    adapter.clear();
                }

                listView1.setAdapter(null);
                cargarWebService(finder.getQuery().toString());

            }
        });


        return view;
    }



        private void cargarWebService(String find) {

        String url ="";
        if(find.isEmpty()){
            url = Validaciones.URL.toString() + "parqueadero/getParq.php";
        }else {
            url = Validaciones.URL.toString() + "parqueadero/getParq.php?finder="+find;
        }


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);

    }





    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Ups se ha producio un error, Verifique su conexión a internet!..", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResponse(JSONObject response) {

        Parqueadero parqueadero;
        JSONArray json = response.optJSONArray("parqueadero");

        try {

            boolean res = response.getBoolean("success");
            if (!res) {

                Toast.makeText(getContext(), "No hay información que mostrar..!" , Toast.LENGTH_LONG).show();



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
            adapter = new AdapterListaParqClie(this.getActivity(), listaParq);

            listView1.setAdapter(adapter);
            listView1.setDivider(null);


            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AdapterAskCli adapterAskCli = null;
                    final int pos = position;
                    Parqueadero parqueadero1 = listaParq.get(pos);
                      adapterAskCli = new  AdapterAskCli(getActivity(),getContext(), parqueadero1.getIdParque().toString());


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        cargarWebService(query.toString());
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {



        if (adapter != null) {
            adapter.clear();
        }

        listView1.setAdapter(null);
        cargarWebService(newText.toString());



        return false;
    }
}
