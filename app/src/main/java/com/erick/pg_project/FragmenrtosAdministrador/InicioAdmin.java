package com.erick.pg_project.FragmenrtosAdministrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.erick.pg_project.CategoriasAdmin.AnimesA;
import com.erick.pg_project.CategoriasAdmin.PeliculasA;
import com.erick.pg_project.CategoriasAdmin.ProgramacionA;
import com.erick.pg_project.CategoriasAdmin.VideosA;
import com.erick.pg_project.R;


public class InicioAdmin extends Fragment {

    Button Animes, Peliculas, Programacion, Videos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Animes = view.findViewById(R.id.Animes);
        Peliculas = view.findViewById(R.id.Pelicuas);
        Programacion = view.findViewById(R.id.Programacion);
        Videos = view.findViewById(R.id.Videojuegos);

        Animes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AnimesA.class));

            }
        });

        Peliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PeliculasA.class));


            }
        });

        Programacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProgramacionA.class));


            }
        });

        Videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VideosA.class));


            }
        });


        return view;
    }
}