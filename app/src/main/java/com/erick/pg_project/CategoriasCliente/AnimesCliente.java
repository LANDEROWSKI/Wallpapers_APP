package com.erick.pg_project.CategoriasCliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.erick.pg_project.CategoriasAdmin.AnimesA.AgregarAnime;
import com.erick.pg_project.CategoriasAdmin.AnimesA.Anime;
import com.erick.pg_project.CategoriasAdmin.AnimesA.AnimesA;
import com.erick.pg_project.CategoriasAdmin.AnimesA.ViewHolderAnime;
import com.erick.pg_project.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimesCliente extends AppCompatActivity {
    RecyclerView recyclerViewAnimeC;
    FirebaseDatabase miFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Anime, ViewHolderAnime> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Anime> options;
    SharedPreferences sharedPreferences;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animes_cliente);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Animes");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewAnimeC = findViewById(R.id.recyclerViewAnimeC);
        recyclerViewAnimeC.setHasFixedSize(true);

        miFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = miFirebaseDataBase.getReference("ANIMES");
        dialog = new Dialog(AnimesCliente.this);

        ListarImagenesAnimes();
    }

    private void ListarImagenesAnimes() {
        options = new FirebaseRecyclerOptions.Builder<Anime>().setQuery(mRef, Anime.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Anime, ViewHolderAnime>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAnime viewHolderAnime, int position, @NonNull Anime anime) {
                viewHolderAnime.SeteoAnimes(
                        getApplicationContext(),
                        anime.getNombre(),
                        anime.getVistas(),
                        anime.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderAnime onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime, parent, false);

                ViewHolderAnime viewHolderAnime = new ViewHolderAnime(itemView);

                viewHolderAnime.setOnClickListener(new ViewHolderAnime.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(AnimesCliente.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                    }
                });
                return viewHolderAnime;
            }
        };

        sharedPreferences = AnimesCliente.this.getSharedPreferences("ANIMES",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar", "DOS");

        //ELEGIR TIPO DE VISTA
        if(ordenar_en.equals("DOS")){
            recyclerViewAnimeC.setLayoutManager(new GridLayoutManager(AnimesCliente.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewAnimeC.setAdapter(firebaseRecyclerAdapter);
        } else if (ordenar_en.equals("TRES")) {
            recyclerViewAnimeC.setLayoutManager(new GridLayoutManager(AnimesCliente.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewAnimeC.setAdapter(firebaseRecyclerAdapter);
        }


    }

    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null)
        {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_vista, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Vista) {
            Ordenar_Imagenes();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes(){
        //Para la fuente que descargamos de Dafont
        String ubicacion = "fuentes/King_Rabbit.otf";
        Typeface tf = Typeface.createFromAsset(AnimesCliente.this.getAssets(),ubicacion);

        //DECLARADO LAS VISTAS
        TextView OrdenarTXT;
        Button Dos_Columnas, Tres_Columnas;

        //CONEXION CON EL CUADRO DE DIALOGO
        dialog.setContentView(R.layout.dialog_ordenar);
        //INICIALIZAR LAS VISTAS
        OrdenarTXT = dialog.findViewById(R.id.OrdenarTXT);
        Dos_Columnas = dialog.findViewById(R.id.Dos_Columnas);
        Tres_Columnas = dialog.findViewById(R.id.Tres_Columnas);
        //CAMBIO DE LA FUENTE DE LETRA
        OrdenarTXT.setTypeface(tf);
        Dos_Columnas.setTypeface(tf);
        Tres_Columnas.setTypeface(tf);

        //EVENTO 2 COLUMNAS
        Dos_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar","DOS");
                editor.apply();
                recreate();
                dialog.dismiss();

            }
        });

        Tres_Columnas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Ordenar", "TRES");
                editor.apply();
                recreate();
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}