package com.utc.maschaparking.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class adminRequest  extends StringRequest {


    private Map<String, String> params;


    public adminRequest(String api,
                        String idRq,
                        String cedulaRq,
                        String nombresRq,
                        String apellidoRq,
                        String telefonoRq,
                        String correoRq,
                        String direccionRq,
                        String pswdRq,
                        String enableRq,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);

        params = new HashMap<>();
        params.put("idAdmin", idRq);
        params.put("cedulaAdmin", cedulaRq);
        params.put("nombresAdmin", nombresRq);
        params.put("apellidoAdmin", apellidoRq);
        params.put("telefonoAdmin", telefonoRq);
        params.put("correoAdmin", correoRq);
        params.put("enableAdmin", enableRq);
        params.put("pswdAdmin", pswdRq);
        params.put("direccionAdmin", direccionRq);
    }


    public adminRequest(String api,
                        String cedulaRq,
                        String nombresRq,
                        String apellidoRq,
                        String telefonoRq,
                        String correoRq,
                        String direccionRq,
                        String pswdRq,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener, errorListener);


        params = new HashMap<>();
        params.put("cedulaAdmin", cedulaRq);
        params.put("nombresAdmin", nombresRq);
        params.put("apellidoAdmin", apellidoRq);
        params.put("telefonoAdmin", telefonoRq);
        params.put("correoAdmin", correoRq);
        params.put("pswdAdmin", pswdRq);
        params.put("direccionAdmin", direccionRq);

    }




    @Override
    public Map<String, String> getParams() {

        return params;

    }
}

