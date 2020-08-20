package com.utc.maschaparking.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.utc.maschaparking.Validaciones.Validaciones;

import java.util.HashMap;
import java.util.Map;

public class loginRequest extends StringRequest {




    private  static final  String Rerg_url = Validaciones.URL.toString()+"index.php";

    private Map<String,String> params;

    public  loginRequest(String user , String pswd, Response.Listener<String> listener, Response.ErrorListener error){



        super(Request.Method.POST,Rerg_url,listener, error);

        params = new HashMap<>();
        params.put("user",user);
        params.put("pswd",pswd);

    }


    public  loginRequest(String api,
                         String cedula,
                         String nombres,
                         String apellidos,
                         String direccion,
                         String correo,
                         String telefono,
                         String pswd,

                         Response.Listener<String> listener,
                         Response.ErrorListener error){



        super(Request.Method.POST,api,listener, error);

        params = new HashMap<>();
        params.put("cedulaClie",cedula);
        params.put("nombresClie",nombres);
        params.put("apellidosClie",apellidos);
        params.put("correoClie",correo);
        params.put("telefonoClie",telefono);
        params.put("direccionClie",direccion);
        params.put("pswdClie",pswd);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
