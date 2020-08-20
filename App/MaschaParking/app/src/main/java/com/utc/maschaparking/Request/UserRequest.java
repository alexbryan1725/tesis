package com.utc.maschaparking.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserRequest  extends StringRequest {


    private Map<String,String> params;


    public UserRequest( String api,
                        String idParq,
                        String cedulaRq,
                        String nombresRq,
                        String apellidoRq,
                        String telefonoRq,
                        String correoRq,
                        String direccionRq,
                        String tipoRq,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener,errorListener);

        params = new HashMap<>();

        params.put("idParq",idParq);
        params.put("cedulaUser",cedulaRq);
        params.put("nombresUser",nombresRq);
        params.put("apellidosUser",apellidoRq);
        params.put("telefonoUser",telefonoRq);
        params.put("direccionUser",direccionRq);
        params.put("correoUser",correoRq);
        params.put("tipoUser",tipoRq);

    }


    public UserRequest( String api,
                        String idUserq,
                        String idParq,
                        String cedulaRq,
                        String nombresRq,
                        String apellidoRq,
                        String telefonoRq,
                        String correoRq,
                        String direccionRq,
                        String enableRq,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

        super(Method.POST, api, listener,errorListener);

        params = new HashMap<>();



        params.put("idUser",idUserq);
        params.put("idParq",idParq);
        params.put("cedulaUser",cedulaRq);
        params.put("nombresUser",nombresRq);
        params.put("apellidosUser",apellidoRq);
        params.put("telefonoUser",telefonoRq);
        params.put("direccionUser",direccionRq);
        params.put("correoUser",correoRq);
        params.put("enableUser",enableRq);

    }


    @Override
    public Map<String, String> getParams() {

        return params;

    }

}
