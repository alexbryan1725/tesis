package com.example.splashpark.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.splashpark.library.Validaciones;

import java.util.HashMap;
import java.util.Map;

public class AdminRequest extends StringRequest {




    private Map<String,String> params;


    public AdminRequest(String api,
                        String idRq,
                        String cedulaRq,
                        String nombresRq,
                        String apellidoRq,
                        String telefonoRq,
                        String correoRq,
                        String pswdRq,
                        String enableRq,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener,errorListener);

        params = new HashMap<>();
        params.put("idAdmin",idRq);
        params.put("cedulaAdmin",cedulaRq);
        params.put("nombresAdmin",nombresRq);
        params.put("apellidoAdmin",apellidoRq);
        params.put("telefonoAdmin",telefonoRq);
        params.put("correoAdmin",correoRq);
        params.put("enableAdmin",enableRq);
        params.put("pswdAdmin",pswdRq);
    }


    public AdminRequest(String api,
                        String cedulaRq,
                        String nombresRq,
                        String apellidoRq,
                        String telefonoRq,
                        String correoRq,
                        String pswdRq,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener,errorListener);


        params = new HashMap<>();
        params.put("cedulaAdmin",cedulaRq);
        params.put("nombresAdmin",nombresRq);
        params.put("apellidoAdmin",apellidoRq);
        params.put("telefonoAdmin",telefonoRq);
        params.put("correoAdmin",correoRq);
        params.put("pswdAdmin",pswdRq);

    }



    public AdminRequest( String api,
                        String idRq,
                        String cedulaRq,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener,errorListener);


        params = new HashMap<>();
        params.put("idAdmin",idRq);
        params.put("cedulaAdmin",cedulaRq);


    }




    @Override
    public Map<String, String> getParams() {

            return params;

    }



}
