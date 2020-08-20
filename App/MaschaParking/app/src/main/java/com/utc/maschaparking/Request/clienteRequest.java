package com.utc.maschaparking.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class clienteRequest extends StringRequest {

    private Map<String, String> params;


    public clienteRequest(String api,
                        String idParq,
                        String idClie,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);
        params = new HashMap<>();
        params.put("idParq", idParq);
        params.put("idClie", idClie);
    }


    public clienteRequest(String api,
                          String delete,
                          String idParq,
                          String idClie,
                          Response.Listener<String> listener,
                          Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);
        params = new HashMap<>();
        params.put("idParq", idParq);
        params.put("idClie", idClie);
    }


    public clienteRequest(String api,
                          String placa,
                          String idParq,
                          String idClie,
                          String estadoFac,
                          Response.Listener<String> listener,
                          Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);
        params = new HashMap<>();
        params.put("placaRes", placa);
        params.put("idParq", idParq);
        params.put("idClie", idClie);
        params.put("estado", estadoFac);
    }



    @Override
    public Map<String, String> getParams() {

        return params;

    }
}
