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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.utc.maschaparking.Adapter.AdapterListaAdmin;
import com.utc.maschaparking.Adapter.AdapterListaVehi;
import com.utc.maschaparking.Fragments.AdminEditFragment;
import com.utc.maschaparking.Models.Admin;
import com.utc.maschaparking.Models.Vehiculo;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VehiculoClienteListFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject>{

    AdapterListaVehi adapter = null;
    ListView listView1;
    ArrayList<Vehiculo> listaVehi;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton add;

    public VehiculoClienteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_vehiculo_cliente_list, container, false);




        listaVehi = new ArrayList<>();
        listView1 = (ListView) view.findViewById(R.id.listViewVehi);
        add=(FloatingActionButton)view.findViewById(R.id.addVehi);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                VehiculoClienteAddFragment vista = new VehiculoClienteAddFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.esenarioClie, vista).commit();

            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshListVehi);

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




        return  view;
    }




    private void cargarWebService() {
        progress.show();


        String url = Validaciones.URL.toString() + "vehiculo/getVehi.php?idClie="+Validaciones.id;
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
        Vehiculo vehiculo = null;





        try {
            boolean res = response.getBoolean("success");
            if (!res) {

                Toast.makeText(getContext(), "No hay información que mostrar..!" , Toast.LENGTH_LONG).show();

                progress.hide();

                return;
            }


            JSONArray json = response.optJSONArray("vehiculo");
            for (int i = 0; i < json.length(); i++) {
         vehiculo = new Vehiculo();
                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                vehiculo.setIdVehi(jsonObject.optInt("idVehi"));
                vehiculo.setPlacaVehi(jsonObject.optString("placaVehi"));
                vehiculo.setColorVehi(jsonObject.optString("colorVehi"));
                vehiculo.setMarcaVehi(jsonObject.optString("marcaVehi"));
                vehiculo.setTipoVehi(jsonObject.optString("tipoVehi"));

                listaVehi.add(vehiculo);
            }


            progress.hide();

            adapter = new AdapterListaVehi(this.getActivity(), listaVehi);

            listView1.setAdapter(adapter);
            listView1.setDivider(null);


            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;
                    String resultado = "";


                    Vehiculo admi = listaVehi.get(pos);

                    Bundle extras = new Bundle();
                    extras.putString("idVehi", admi.getIdVehi().toString());
                    extras.putString("placaVehi", admi.getPlacaVehi().toString());
                    extras.putString("colorVehi", admi.getColorVehi().toString());
                    extras.putString("marcaVehi", admi.getMarcaVehi().toString());
                    extras.putString("tipoVehi", admi.getTipoVehi().toString());
                    extras.putString("accion", "editVehi");

                    VehiculoClienteEditFragment vista = new VehiculoClienteEditFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.beginTransaction().replace(R.id.esenarioClie, vista).commit();
                    vista.setArguments(extras);


                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
