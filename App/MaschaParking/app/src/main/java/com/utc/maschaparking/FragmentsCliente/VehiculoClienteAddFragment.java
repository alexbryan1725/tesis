package com.utc.maschaparking.FragmentsCliente;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.adminRequest;
import com.utc.maschaparking.Request.vehiculoRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;


public class VehiculoClienteAddFragment extends Fragment  implements View.OnClickListener {

    EditText placa, color, tipo, marca;
    Button save;

    AdapterDialog adapterDialog = null;
    Validaciones validaciones = new Validaciones();


    public VehiculoClienteAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_vehiculo_cliente_add, container, false);

        placa = (EditText) view.findViewById(R.id.TxtVehiInsertPlaca);
        color = (EditText) view.findViewById(R.id.TxtVehiInsertColor);
        tipo = (EditText) view.findViewById(R.id.TxtVehiInsertTipo);
        marca = (EditText) view.findViewById(R.id.TxtVehiInsertMarca);

        save=(Button)view.findViewById(R.id.BtnVehiSave);
        save.setOnClickListener(this);

        adapterDialog = new AdapterDialog(getContext());

        return  view;
    }


    private void registerVehi() {


        String  idClieGet = Validaciones.id.toString(),
                placaGet = placa.getText().toString(),
                colorGet = color.getText().toString(),
                marcaGet = marca.getText().toString(), tipoGet = tipo.getText().toString();



        boolean isOk = true;

        if (placaGet.isEmpty()) {
            placa.setError("Este campo es Obligatorio");
            isOk = false;
        }else{
            if(!validaciones.matriculaOk(placaGet)){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("El número de placa no es válida recuerde que debe tener el siguiente formato (ABC0000)")
                        .setNegativeButton("Aceptar", null)
                        .create().show();
                isOk = false;
            }

        }

        if (colorGet.isEmpty()) {
            color.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (tipoGet.isEmpty()) {
            tipo.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (marcaGet.isEmpty()) {
            marca.setError("Este campo es Obligatorio");
            isOk = false;
        }


        if(isOk) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {

                            adapterDialog.hide();
                            Toast toast = Toast.makeText(getContext(), "Vehículo Agregado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else if(sucess == false){
                            adapterDialog.hide();
                            Toast toast = Toast.makeText(getContext(), "Este vehículo ya esta registrado...!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                        }


                    } catch (JSONException e) {
                        adapterDialog.hide();
                        Toast toast = Toast.makeText(getContext(), e.toString() , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();

                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    adapterDialog.hide();
                    Toast toast = Toast.makeText(getContext(),error.toString() , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            };


            String api = Validaciones.URL.toString() + "vehiculo/addVehiculo.php";

            vehiculoRequest request = new vehiculoRequest(api,
                    idClieGet,
                    placaGet,
                    colorGet,
                    marcaGet,
                    tipoGet,
                    responseListener,
                    errorListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);

        }else {
            adapterDialog.hide();
            return;
        }

    }

    @Override
    public void onClick(View v) {
        if(v == save ){


            adapterDialog.show("Añadiendo Vehículo","Espere Por Favor ..!");

            if(!validaciones.isDataConnectionAvailable(getContext())){
                Snackbar mySnackbar = Snackbar.make(getView(), "No tiene conección con Internet..!  ", Snackbar.LENGTH_LONG).
                        setDuration(2000);
                View view = mySnackbar.getView();
                TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);

                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.designsnackbar));
                mySnackbar.show();
                adapterDialog.hide();
                return;
            }



            registerVehi();


        }
    }
}
