package com.utc.maschaparking.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class vehiculoRequest extends StringRequest {


    private Map<String, String> params;


    public vehiculoRequest(String api,
                        String idUser,
                        String placa,
                        String color,
                        String marca,
                        String tipo,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);

        params = new HashMap<>();
        params.put("idClie", idUser);
        params.put("placaVehi", placa);
        params.put("colorVehi", color);
        params.put("marcaVehi", marca);
        params.put("tipoVehi", tipo);

    }


    public vehiculoRequest(String api,
                           String idUser,
                           String idVehi,
                           String placa,
                           String color,
                           String marca,
                           String tipo,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);

        params = new HashMap<>();
        params.put("idClie", idUser);
        params.put("idVehi", idVehi);
        params.put("placaVehi", placa);
        params.put("colorVehi", color);
        params.put("marcaVehi", marca);
        params.put("tipoVehi", tipo);

    }


    public vehiculoRequest(String api,
                           String idUser,
                           String idVehi,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);

        params = new HashMap<>();
        params.put("idClie", idUser);
        params.put("idVehi", idVehi);

    }





    @Override
    public Map<String, String> getParams() {

        return params;

    }
}

