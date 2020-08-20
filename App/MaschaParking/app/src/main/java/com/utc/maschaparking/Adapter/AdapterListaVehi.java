package com.utc.maschaparking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.AdminMenuActivity;
import com.utc.maschaparking.FragmentsCliente.VehiculoClienteAddFragment;
import com.utc.maschaparking.FragmentsCliente.VehiculoClienteEditFragment;
import com.utc.maschaparking.FragmentsCliente.VehiculoClienteListFragment;
import com.utc.maschaparking.Models.User;
import com.utc.maschaparking.Models.Vehiculo;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.clienteRequest;
import com.utc.maschaparking.Request.vehiculoRequest;
import com.utc.maschaparking.Validaciones.Validaciones;
import com.utc.maschaparking.inicioSesionActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterListaVehi extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Vehiculo> items;

    public AdapterListaVehi(Activity activity, ArrayList<Vehiculo> items ){

        this.activity = activity;
        this.items = items;

    }

    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }


    public void addAll(ArrayList<Vehiculo> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.viewvehiclie, null);
        }

        final Vehiculo dir = items.get(position);
        ImageView imageDelete = (ImageView)v.findViewById(R.id.eraseVehi);
        TextView txtPlaca = (TextView) v.findViewById(R.id.VehiListPlaca);
        TextView txtColor =(TextView) v.findViewById(R.id.VehiListColor);
        TextView txtModel = (TextView) v.findViewById(R.id.VehiListMarca);
        TextView txtTipo= (TextView) v.findViewById(R.id.VehiListTipo);

        txtPlaca.setText("Placa : "+dir.getPlacaVehi().toString());
        txtColor.setText("Color: "+dir.getColorVehi().toString());
        txtModel.setText("Marca: "+dir.getMarcaVehi().toString());
        txtTipo.setText("Tipo: "+dir.getTipoVehi().toString());


        imageDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {




                new AlertDialog.Builder(view.getContext())
                        .setTitle("Salir")
                        .setMessage("Está seguro de eliminar este vehículo?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {




                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        JSONObject jsonResponse = null;
                                        try {
                                            jsonResponse = new JSONObject(response);
                                            boolean sucess = jsonResponse.getBoolean("success");

                                            if (sucess == true) {


                                                Toast toast = Toast.makeText(view.getContext(), "Eliminado de la lista", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                                toast.show();

                                                VehiculoClienteListFragment vista = new VehiculoClienteListFragment();
                                                FragmentManager fm = ((FragmentActivity)view.getContext()).getSupportFragmentManager();
                                                fm.beginTransaction().replace(R.id.esenarioClie, vista).commit();

                                            }


                                        } catch (JSONException e) {

                                        }


                                    }
                                };


                                Response.ErrorListener errorListener = new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                };


                                String api = Validaciones.URL.toString() + "vehiculo/deleteVehiculo.php";

                                vehiculoRequest cliente = new vehiculoRequest(api,

                                        Validaciones.id,
                                        dir.getIdVehi().toString(),
                                        responseListener,
                                        errorListener);


                                RequestQueue queue = Volley.newRequestQueue(view.getContext());
                                queue.add(cliente);





                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();








            }






        });




        return v;



    }
}
