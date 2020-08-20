package com.example.splashpark.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.splashpark.library.Validaciones;

import java.util.HashMap;
import java.util.Map;

public class loginRequest extends StringRequest {




    private  static final  String Rerg_url = Validaciones.URL.toString()+"admin/index.php";

    private Map<String,String> params;

    public  loginRequest(String user , String pswd, Response.Listener<String> listener, Response.ErrorListener error){



        super(Request.Method.POST,Rerg_url,listener, error);

        params = new HashMap<>();
        params.put("user",user);
        params.put("pswd",pswd);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
