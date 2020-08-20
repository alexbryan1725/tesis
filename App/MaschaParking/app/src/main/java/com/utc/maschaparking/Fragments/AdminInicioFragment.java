package com.utc.maschaparking.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.utc.maschaparking.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminInicioFragment extends Fragment implements View.OnClickListener{

    CardView parq,prop,rep,admin ;
    public AdminInicioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_admin_inicio, container, false);

        parq = (CardView) view.findViewById(R.id.CardParqMenu);
        admin = (CardView) view.findViewById(R.id.CardAdminMenu);
        prop = (CardView) view.findViewById(R.id.CardPropMenu);
        rep = (CardView) view.findViewById(R.id.CardRepMenu);

        parq.setOnClickListener( this);
        admin.setOnClickListener( this);
        prop.setOnClickListener( this);
        rep.setOnClickListener( this);
        return  view;
    }


    @Override
    public void onClick(View v) {
        if(v == parq ){
            ParqListFragment vista = new ParqListFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.esenario, vista).commit();
        }
        if(v == admin ){
            AdminListFragment vista = new AdminListFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.esenario, vista).commit();
        }
        if(v == prop ){
            UserListFragment vista = new UserListFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.esenario, vista).commit();
        }
        if(v == rep ){
            AdminListFragment vista = new AdminListFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.esenario, vista).commit();
        }
    }
}
