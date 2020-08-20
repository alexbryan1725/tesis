package com.utc.maschaparking.FragmentsUserAdmin;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.utc.maschaparking.Adapter.AdapterAddFact;
import com.utc.maschaparking.Adapter.AdapterAskCli;
import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class MakeCobroFragment extends Fragment implements View.OnClickListener, Response.ErrorListener, Response.Listener<JSONObject>{
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    EditText placaEdit;
    Validaciones validaciones= new Validaciones();
    Button save;
    public MakeCobroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_make_cobro, container, false);
        request = Volley.newRequestQueue(getContext());
        placaEdit=(EditText)view.findViewById(R.id.TxtMakeCobroPlaca);
        save = (Button) view.findViewById(R.id.BtnCheckCobro);
       save.setOnClickListener(this);
        return  view;
    }




    private void cargarWebService() {

        String numPlaca = placaEdit.getText().toString();

        if(!validaciones.matriculaOk(numPlaca)){


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("El número de placa no es correcta Recuerde que debe tener el siguiente formato (ABC0000)")
                    .setNegativeButton("Aceptar", null)
                    .create().show();

            return;
        }


        String url = Validaciones.URL.toString() + "factura/checkRes.php?placaRes="+numPlaca+"&idParq="+Validaciones.idParq;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Ups se ha producido un error intente nuevamente..!", Toast.LENGTH_LONG).show();
        System.out.println();

    }

    @Override
    public void onResponse(JSONObject response) {

        String idClie, Nombres, placa,idRes;
        try {

            boolean sucess = response.getBoolean("success");
            String hora = response.getString("time");
            if (sucess == true) {
                JSONArray json = response.optJSONArray("res");

                for (int i = 0; i < json.length(); i++) {
                    JSONObject jsonObject = null;
                    jsonObject = json.getJSONObject(i);


                    String time = jsonObject.optString("fechaRes");


                    idClie= jsonObject.optString("idClie");
                    idRes = jsonObject.optString("idRes");
                    Nombres = jsonObject.optString("nombresClie") + " " + jsonObject.optString("apellidosClie");
                    placa = jsonObject.optString("placaRes");

                    AdapterAddFact adapterAddFact = null;
                    adapterAddFact = new AdapterAddFact(getContext(), idRes, idClie, Nombres, placa, time, true);
                }
            }
                else {




                String time = hora;
                idRes = "1";
                idClie = "1";
                Nombres = "1";
                placa = placaEdit.getText().toString();

                AdapterAddFact adapterAddFact = null;
                adapterAddFact = new AdapterAddFact(getContext(), idRes, idClie, Nombres, placa, time, false);
                }






        } catch (JSONException e) {
            Toast.makeText(getContext(), "Ups se ha producido un error intente nuevamente..!"+e.toString(), Toast.LENGTH_LONG).show();
            System.out.println();
        }

    }

    @Override
    public void onClick(View v) {
        if(v ==save){

            cargarWebService();
        }
    }
}
