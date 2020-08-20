package com.example.splashpark.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ParqRequest extends StringRequest {

    private Map<String, String> params;


    public ParqRequest(String apiRegister,
                       String idAdmin,
                       String nombreParq,
                       String correoParq,
                       String telefonoParq,
                       String direccionParq,
                       String longParq,
                       String latParq,
                       String rucParq,
                       String fraccionParq,
                       String numeroParq,

                       Response.Listener<String> listenerRegister,
                       Response.ErrorListener errorListenerReg) {



        super(Method.POST, apiRegister, listenerRegister, errorListenerReg);



        params = new HashMap<>();
        params.put("idAdmin",idAdmin);
        params.put("nombreParq",nombreParq);
        params.put("correoParq",correoParq);
        params.put("telefonoParq",telefonoParq);
        params.put("direccionParq",direccionParq);
        params.put("longParq",longParq);
        params.put("latiParq",latParq);
        params.put("rucParq",rucParq);
        params.put("fraccionParq",fraccionParq);
        params.put("alquilerParq","00");
        params.put("numeroParq",numeroParq);
    }


    public ParqRequest(String apiRegister,
                       String idParq,
                       String idAdmin,
                       String nombreParq,
                       String correoParq,
                       String telefonoParq,
                       String direccionParq,
                       String longParq,
                       String latParq,
                       String rucParq,
                       String fraccionParq,
                       String numeroParq,
                       String enableParq,

                       Response.Listener<String> listenerRegister,
                       Response.ErrorListener errorListenerReg) {



        super(Method.POST, apiRegister, listenerRegister, errorListenerReg);



        params = new HashMap<>();
        params.put("idAdmin",idAdmin);
        params.put("nombreParq",nombreParq);
        params.put("correoParq",correoParq);
        params.put("telefonoParq",telefonoParq);
        params.put("direccionParq",direccionParq);
        params.put("longParq",longParq);
        params.put("latiParq",latParq);
        params.put("rucParq",rucParq);
        params.put("fraccionParq",fraccionParq);
        params.put("alquilerParq","00");
        params.put("numeroParq",numeroParq);
        params.put("enableParq",enableParq);
        params.put("idParq",idParq);
    }






    @Override
    public Map<String, String> getParams() {

        return params;

    }
}
