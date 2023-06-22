package com.erick.pg_project.Categorias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.erick.pg_project.CategoriasCliente.AnimesCliente;
import com.erick.pg_project.CategoriasCliente.PeliculasCliente;
import com.erick.pg_project.CategoriasCliente.ProgramacionCliente;
import com.erick.pg_project.CategoriasCliente.VideojuegosCliente;
import com.erick.pg_project.R;

public class ControladorCD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlador_cd);

        String CategoriaRecuperada = getIntent().getStringExtra("Categoria");

        if(CategoriaRecuperada.equals("Animes")){
            startActivity(new Intent(ControladorCD.this, AnimesCliente.class));
            finish();
        }
        if(CategoriaRecuperada.equals("Peliculas")){
            startActivity(new Intent(ControladorCD.this, PeliculasCliente.class));
            finish();
        }
        if(CategoriaRecuperada.equals("Programacion")){
            startActivity(new Intent(ControladorCD.this, ProgramacionCliente.class));
            finish();
        }
        if(CategoriaRecuperada.equals("Videojuegos")){
            startActivity(new Intent(ControladorCD.this, VideojuegosCliente.class));
            finish();
        }
    }
}