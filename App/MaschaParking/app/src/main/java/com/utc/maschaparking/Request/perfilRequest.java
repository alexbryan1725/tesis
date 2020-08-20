package com.utc.maschaparking.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class perfilRequest extends StringRequest {


    private Map<String,String> params;

    public perfilRequest(String api,
                         String type,
                         String id,
                         String cedula,
                         String nombres,
                         String apellidos,
                         String direccion,
                         String correo,
                         String telefono,
                         Response.Listener<String> listener,

                         Response.ErrorListener errorListener) {
        super(Method.POST, api, listener,errorListener);
        params = new HashMap<>();

        params.put("type",type);
        params.put("id",id);
        params.put("cedula",cedula);
        params.put("nombres",nombres);
        params.put("apellidos",apellidos);
        params.put("direccion",direccion);
        params.put("correo",correo);
        params.put("telefono",telefono);

    }

    public perfilRequest(String api,
                         String type,
                         String id,
                         String pswdAnterior,
                         String pwdNueva,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(Method.POST, api, listener,errorListener);
        params = new HashMap<>();

        params.put("type",type);
        params.put("id",id);
        params.put("pswdBack",pswdAnterior);
        params.put("pswdNew",pwdNueva);

    }


    public perfilRequest(String api,
                         String email,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(Method.POST, api, listener,errorListener);
        params = new HashMap<>();


        params.put("email",email);

    }





    @Override
    public Map<String, String> getParams() {

        return params;

    }

}
