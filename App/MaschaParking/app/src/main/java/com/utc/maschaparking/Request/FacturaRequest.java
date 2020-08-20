package com.utc.maschaparking.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FacturaRequest extends StringRequest {


    private Map<String, String> params;


    public FacturaRequest(String api,
                        String idUser,
                        String idParq,
                        String idClie,
                        String placa,
                          String fecha,
                        String reservacion,

                        String idRes,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);

        params = new HashMap<>();
        params.put("idUser", idUser);
        params.put("idParq", idParq);
        params.put("idClie", idClie);
        params.put("placa", placa);
        params.put("fecha", fecha);
        params.put("reservacion", reservacion);
        params.put("idRes", idRes);
    }





    @Override
    public Map<String, String> getParams() {

        return params;

    }
}

