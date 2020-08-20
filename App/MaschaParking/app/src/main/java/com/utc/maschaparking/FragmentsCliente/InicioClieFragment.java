package com.utc.maschaparking.FragmentsCliente;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.utc.maschaparking.Adapter.AdapterAskCli;
import com.utc.maschaparking.Adapter.AdapterListaCP;
import com.utc.maschaparking.Adapter.AdapterListaParq;
import com.utc.maschaparking.Fragments.AdminEditFragment;
import com.utc.maschaparking.Fragments.ParqEditFragment;
import com.utc.maschaparking.Models.Admin;
import com.utc.maschaparking.Models.Parqueadero;
import com.utc.maschaparking.Models.User;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;


public class InicioClieFragment extends Fragment implements  OnMapReadyCallback, SearchView.OnQueryTextListener, Response.ErrorListener, Response.Listener<JSONObject> {


    Marker mPositionMarker;
    ///mapa
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    MapView mapView;

    ArrayList<LatLng>arrayList = new ArrayList<LatLng>();
    ArrayList<String>imgVer = new ArrayList<String>();

    ArrayList<String> titulo=new ArrayList<String>();
    ArrayList<String> subtitle=new ArrayList<String>();



    //buscador y lista
    String textoSearch = "FALSE";
    int i = 0;
    ListView layoutAnimado;
    SearchView editsearch,search;
    int focus = 0;
    int contenido = 0;
    AdapterListaCP adapter = null;
    ArrayList<Parqueadero> listaParq;
    ArrayList<Parqueadero> buscador;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;




    public InicioClieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View nview = inflater.inflate(R.layout.fragment_inicio_clie, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapClie);
        if (mapFragment == null) {

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.mapClie, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
        layoutAnimado = (ListView) nview.findViewById(R.id.listas);
        editsearch = (SearchView) nview.findViewById(R.id.buscador);
        editsearch.setOnQueryTextListener(this);

        listaParq = new ArrayList<>();
        buscador = new ArrayList<>();
        request = Volley.newRequestQueue(getContext());




        cargarWebService();

        return nview;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        final int reload = 0;

        mMap = googleMap;



        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);




        LatLng general = new LatLng(-0.933772, -78.615021);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(general, 16));




            final Handler handler= new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    arrayList.clear();
                    imgVer.clear();
                    titulo.clear();
                    subtitle.clear();

                    mMap.clear();

                    cargarWebService();
                    for (Parqueadero p : buscador) {

                        LatLng x = new LatLng(p.getLatiParq(),p.getLongParq());
                        titulo.add(p.getIdParque()+", "+p.getNombreParq());
                        subtitle.add((p.getPlazaParq() - p.getNumeroParq()) +" / "+p.getPlazaParq());
                        arrayList.add(x);
                        int tot = p.getPlazaParq();
                        int act = p.getNumeroParq();

                        double porcentaje = (100*act)/tot;

                        if(porcentaje <=50){
                            imgVer.add("green");
                        }
                        if(porcentaje >50 && porcentaje <=80 ){
                            imgVer.add("orange");
                        }
                        if(porcentaje >80 ){
                            imgVer.add("red");
                        }



                    }

                    for (int u = 0;u<arrayList.size();u++){


                        if(imgVer.get(u).equals("green")){
                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource
                                    (R.drawable.pointgreen)).anchor(0.5f, 1.0f).position(arrayList.get(u)).snippet(subtitle.get(u)).title(titulo.get(u)));

                        }

                        if(imgVer.get(u).equals("orange")){
                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource
                                    (R.drawable.pointorange)).anchor(0.5f, 1.0f).position(arrayList.get(u)).snippet(subtitle.get(u)).title(titulo.get(u)));

                        }

                        if(imgVer.get(u).equals("red")){
                            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource
                                    (R.drawable.pointred)).anchor(0.5f, 1.0f).position(arrayList.get(u)).snippet(subtitle.get(u)).title(titulo.get(u)));

                        }




                    }

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                        AdapterAskCli adapterAskCli = null;
                        @Override
                        public boolean onMarkerClick(Marker marker) {


                            String colores = marker.getTitle();
                            String[] arrayColores = colores.split(",");


                            adapterAskCli = new  AdapterAskCli(getActivity(),getContext(), arrayColores[0].toString());

                            return false;
                        }
                    });


                        handler.postDelayed(this,4000);//se ejecutara


                }
            },1000);




    }


    public void listar(View view) {

        animar(true);
        layoutAnimado.setVisibility(View.VISIBLE);


    }

    private void animar(boolean mostrar) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = null;
        if (mostrar) {
            //desde la esquina inferior derecha a la superior izquierda
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);

        } else {    //desde la esquina superior izquierda a la esquina inferior derecha
            animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        }
        //duración en milisegundos
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.25f);

        layoutAnimado.setLayoutAnimation(controller);
        layoutAnimado.startAnimation(animation);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {


        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {



        buscar(newText);


        textoSearch=newText;

        if (newText.isEmpty()) {


            textoSearch="FALSE";

            if(focus > 0) {
                animar(false);
                layoutAnimado.setVisibility(View.INVISIBLE);
                focus=0;
            }
            cargarWebService();

        }
        return false;
    }


    public void cargarWebService() {



        String url = Validaciones.URL.toString() + "parqueadero/getParq.php";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

        listaParq.clear();
        buscador.clear();

        Parqueadero parqueadero;
        JSONArray json = response.optJSONArray("parqueadero");

        try {

            boolean res = response.getBoolean("success");
            if (!res) {

                return;
            }

            for (int i = 0; i < json.length(); i++) {


                parqueadero = new Parqueadero();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                String isTrue = jsonObject.optString("enableParq");
                if (isTrue.equals("TRUE")) {
                    contenido++;
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
                    buscador.add(parqueadero);
                }



            }

            if(textoSearch.equals("FALSE")){
                adapter = new AdapterListaCP(this.getActivity(), listaParq);
                layoutAnimado.setAdapter(adapter);
                layoutAnimado.setDivider(null);
                layoutAnimado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final int pos = position;

                        AdapterAskCli adapterAskCli = null;

                        Parqueadero parqueadero = listaParq.get(pos);
                        adapterAskCli = new AdapterAskCli(getActivity(),getContext(), parqueadero.getIdParque().toString());
                    }
                });


            }else {
                buscar(textoSearch);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void buscar(String text) {


        if(text.isEmpty()){
            cargarWebService();
            return;
        }


        ArrayList<Parqueadero> filtrarLista = new ArrayList<>();


        Parqueadero pq;

        int i = 0;
        for (Parqueadero p : buscador) {

            pq = new Parqueadero();


            if (p.getNombreParq().toLowerCase().contains(text.toLowerCase())) {

                pq.setIdParque(p.getIdParque());
                pq.setNombreParq(p.getNombreParq());
                pq.setCorreoParq(p.getCorreoParq());
                pq.setTelefonoParq(p.getTelefonoParq());
                pq.setDireccionParq( p.getDireccionParq());
                pq.setLatiParq( p.getLatiParq());
                pq.setLongParq(p.getLongParq());
                pq.setRucParq( p.getRucParq());
                pq.setAlquilerParq( p.getAlquilerParq());
                pq.setFraccionParq( p.getFraccionParq());
                pq.setNumeroParq( p.getNumeroParq());
                pq.setPlazaParq(p.getPlazaParq() );
                pq.setEnableParq( p.getEnableParq());
                filtrarLista.add(pq);
                i++;
            }


        }

        if (i > 0) {
            if (adapter != null) {
                adapter.clear();
            }
            layoutAnimado.setAdapter(null);
            adapter = new AdapterListaCP(this.getActivity(), filtrarLista);
            layoutAnimado.setAdapter(adapter);
            layoutAnimado.setDivider(null);

            layoutAnimado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final int pos = position;

                    AdapterAskCli adapterAskCli = null;

                    Parqueadero parqueadero = buscador.get(pos);
                    adapterAskCli = new AdapterAskCli(getActivity(),getContext(), parqueadero.getIdParque().toString());




                }
            });








            focus++;

            if(focus==1){
                animar(true);
                layoutAnimado.setVisibility(View.VISIBLE);
            }




        } else {


            cargarWebService();

        }


    }


}
