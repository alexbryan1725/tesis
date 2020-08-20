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
import com.utc.maschaparking.Adapter.AdapterListFact;
import com.utc.maschaparking.Adapter.AdapterListaAdmin;
import com.utc.maschaparking.Fragments.AdminEditFragment;
import com.utc.maschaparking.Models.Admin;
import com.utc.maschaparking.Models.FacturaParq;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClienteMovimientosFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    AdapterListFact adapter = null;
    ListView listView1;
    ArrayList<FacturaParq> listaFac;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ClienteMovimientosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cliente_movimientos, container, false);

        listaFac = new ArrayList<>();
        listView1 = (ListView) view.findViewById(R.id.listViewMovi);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshListMovi);

        request = Volley.newRequestQueue(getContext());
        progress = new ProgressDialog(getContext());
        progress.setCancelable(false);
        progress.setMessage("Porfavor Espere...");





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


                        if(adapter!=null){
                            adapter.clear();
                        }
                        listView1.setAdapter(null);
                        cargarWebService();

                    }
                });



                handler.postDelayed(this,5000);//se ejecutara


            }
        },100);










        return  view;
    }


    private void cargarWebService() {



        String url = Validaciones.URL.toString() + "cliente/getTransaccion.php?idClie="+Validaciones.id;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {

        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        FacturaParq facturaParq = null;




        try {
            boolean res = response.getBoolean("success");
            if (!res) {

               //Toast.makeText(getContext(), "No hay información que mostrar..!" , Toast.LENGTH_LONG).show();

                return;
            }
            JSONArray json = response.optJSONArray("parqueadero");
            for (int i = 0; i < json.length(); i++) {



                facturaParq = new FacturaParq();
                JSONObject jsonObject = null;


                jsonObject = json.getJSONObject(i);
                facturaParq.setIdFac(jsonObject.optInt("idParq"));
                facturaParq.setNombreParq(jsonObject.optString("nombreParq"));
                facturaParq.setIdUser(jsonObject.optString("usuario_idUser"));
                facturaParq.setNombreUsuario(jsonObject.optString("nombresUser"));
                facturaParq.setPlacaFac(jsonObject.optString("placaFac"));
                facturaParq.setFechaInicio(jsonObject.optString("fechaFac"));
                facturaParq.setFechaFin(jsonObject.optString("horaSalidaFac"));
                facturaParq.setPrecioFac(jsonObject.optString("precioFac"));
                facturaParq.setTotalFac(jsonObject.optString("totalFac"));
                facturaParq.setEstadoFac(jsonObject.optString("estadoFac"));

                listaFac.add(facturaParq);
            }
            progress.hide();

            adapter = new AdapterListFact(this.getActivity(), listaFac);

            listView1.setAdapter(adapter);
            listView1.setDivider(null);


        } catch (JSONException e) {

        }


    }




}
