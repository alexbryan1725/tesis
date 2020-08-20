package com.utc.maschaparking.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.utc.maschaparking.R;

public class AdapterDialog {

    TextView titulo, mensaje;

     Dialog dialog = null;

    public  AdapterDialog(Context context){

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.viewprogress);
        titulo = (TextView)dialog.findViewById(R.id.titleProgress);
        mensaje = (TextView)dialog.findViewById(R.id.messageProgress);
    }


    public void show(String title ,String msj){
        titulo.setText(title);
        mensaje.setText(msj);
        dialog.show();
    }

    public void hide(){
        dialog.hide();
    }



}
