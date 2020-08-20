package com.utc.maschaparking.FragmentsCliente;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.Adapter.AdapterDialog;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Request.adminRequest;
import com.utc.maschaparking.Request.vehiculoRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONException;
import org.json.JSONObject;


public class VehiculoClienteEditFragment extends Fragment  implements View.OnClickListener{

    EditText placa, color, tipo, marca;
    Button save;
    String idVehi = "";
    AdapterDialog adapterDialog = null;
    Validaciones validaciones= new Validaciones();
    public VehiculoClienteEditFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_vehiculo_cliente_edit, container, false);


        placa = (EditText) view.findViewById(R.id.TxtVehiEditPlaca);
        color = (EditText) view.findViewById(R.id.TxtVehiEditColor);
        tipo = (EditText) view.findViewById(R.id.TxtVehiEditTipo);
        marca = (EditText) view.findViewById(R.id.TxtVehiEditMarca);

        placa.setText(getArguments().getString("placaVehi").toString());
        color.setText(getArguments().getString("colorVehi").toString());
        tipo.setText(getArguments().getString("tipoVehi").toString());
        marca.setText(getArguments().getString("marcaVehi").toString());
        idVehi = getArguments().getString("idVehi").toString();

        adapterDialog = new AdapterDialog(getContext());

        save=(Button)view.findViewById(R.id.BtnVehiEditSave);
        save.setOnClickListener(this);

        return  view;
    }



    @Override
    public void onClick(View v) {

        if (v == save) {

            adapterDialog.show("Realizando Cambios","Espere Por Favor ..!");
            editFun();
        }
    }


    private void editFun() {

        String placaGet = placa.getText().toString(),
                colorGet = color.getText().toString(),
                tipoGet = tipo.getText().toString(),
                marcaGet = marca.getText().toString();



        boolean isOk = true;

        if (placaGet.isEmpty()) {
            placa.setError("Este campo es Obligatorio");
            isOk = false;
        } else {

            if(!validaciones.matriculaOk(placaGet)){


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("El número de placa no es correcta Recuerde que debe tener el siguiente formato (ABC0000)")
                        .setNegativeButton("Aceptar", null)
                        .create().show();

                isOk = false;
            }



        }

        if (marcaGet.isEmpty()) {
            marca.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (colorGet.isEmpty()) {
            color.setError("Este campo es Obligatorio");
            isOk = false;
        }
        if (tipoGet.isEmpty()) {
            tipo.setError("Este campo es Obligatorio");
            isOk = false;
        }

        if (isOk) {


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    JSONObject jsonResponse = null;
                    try {


                        jsonResponse = new JSONObject(response);
                        boolean sucess = jsonResponse.getBoolean("success");

                        if (sucess == true) {

                            adapterDialog.hide();
                            Toast toast = Toast.makeText(getContext(), "Vehículo Modificado", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();

                        } else if(sucess == false){
                            String error = jsonResponse.getString("error");
                            String msj ="";


                            if(error.equals("placaDupli")){
                                placa.setError("Ya existe esta placa registrada");
                                msj =" Ya existe esta placa registrado ..!";
                            }

                            adapterDialog.hide();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(msj)
                                    .setNegativeButton("Aceptar", null)
                                    .create().show();

                        }
                    } catch (JSONException e) {

                        adapterDialog.hide();
                        e.printStackTrace();

                        Toast toast = Toast.makeText(getContext(), "Ups algo ha salido mal, reintente de nuevo" , Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }


                }
            };


            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    adapterDialog.hide();
                    Toast toast = Toast.makeText(getContext(), "Ups algo ha salido mal, reintente de nuevo" , Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();


                }
            };

            String api = Validaciones.URL.toString() + "vehiculo/updateVehiculo.php";

            vehiculoRequest adminRequest = new vehiculoRequest(api,
                    Validaciones.id,
                    idVehi,
                    placaGet,
                    colorGet,
                    marcaGet,
                    tipoGet,
                    responseListener,
                    errorListener);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(adminRequest);

        }else {
            adapterDialog.hide();
            return;
        }

    }

}
