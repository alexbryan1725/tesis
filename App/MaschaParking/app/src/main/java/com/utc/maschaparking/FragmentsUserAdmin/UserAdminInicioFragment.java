package com.utc.maschaparking.FragmentsUserAdmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.utc.maschaparking.R;
import com.utc.maschaparking.Validaciones.Validaciones;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserAdminInicioFragment extends Fragment {

    public UserAdminInicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_admin_inicio, container, false);



        return view;
    }
}
