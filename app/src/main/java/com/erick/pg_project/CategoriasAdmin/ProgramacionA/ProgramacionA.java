package com.erick.pg_project.CategoriasAdmin.ProgramacionA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

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
import com.erick.pg_project.CategoriasAdmin.PeliculasA.PeliculasA;
import com.erick.pg_project.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ProgramacionA extends AppCompatActivity {

    RecyclerView recyclerViewProgramacion;
    FirebaseDatabase miFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Programacion, ViewHolderProramacion> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Programacion> options;

    SharedPreferences sharedPreferences;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacion);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Programación");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewProgramacion = findViewById(R.id.recyclerViewProgramacion);
        recyclerViewProgramacion.setHasFixedSize(true);

        miFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = miFirebaseDataBase.getReference("PROGRAMACION");
        dialog = new Dialog(ProgramacionA.this);


        ListarImagenesProgramacion();

    }

    private void ListarImagenesProgramacion() {
        options = new FirebaseRecyclerOptions.Builder<Programacion>().setQuery(mRef, Programacion.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Programacion, ViewHolderProramacion>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderProramacion viewHolderProramacion, int position, @NonNull Programacion programacion) {
                viewHolderProramacion.SeteoProgramacion(
                        getApplicationContext(),
                        programacion.getNombre(),
                        programacion.getVistas(),
                        programacion.getImagen()
                );
            }

            @NonNull
            @Override
            public ViewHolderProramacion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_programacion, parent, false);
                ViewHolderProramacion viewHolderProramacion = new ViewHolderProramacion(itemView);
                viewHolderProramacion.setOnClickListener(new ViewHolderProramacion.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(ProgramacionA.this, "ITEM CLICK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                        String Nombre = getItem(position).getNombre();
                        String Imagen = getItem(position).getImagen();
                        int Vista = getItem(position).getVistas();
                        final String VistaString = String.valueOf(Vista);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProgramacionA.this);
                        String[] opciones = {"Actualizar", "Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i == 0){
                                    Intent intent = new Intent(ProgramacionA.this, AgregarProgramacion.class);
                                    intent.putExtra("NombreEnviado", Nombre);
                                    intent.putExtra("ImagenEnviada",Imagen);
                                    intent.putExtra("VistaEnviada",VistaString);
                                    startActivity(intent);

                                }

                                if(i == 1){
                                    ELiminarDatos(Nombre, Imagen);
                                }
                            }
                        });
                        builder.create().show();
                    }
                });
                return viewHolderProramacion;
            }
        };
        sharedPreferences = ProgramacionA.this.getSharedPreferences("PROGRAMACION",MODE_PRIVATE);
        String ordenar_en = sharedPreferences.getString("Ordenar", "DOS");

        //ELEGIR TIPO DE VISTA
        if(ordenar_en.equals("DOS")){
            recyclerViewProgramacion.setLayoutManager(new GridLayoutManager(ProgramacionA.this,2));
            firebaseRecyclerAdapter.startListening();
            recyclerViewProgramacion.setAdapter(firebaseRecyclerAdapter);
        } else if (ordenar_en.equals("TRES")) {
            recyclerViewProgramacion.setLayoutManager(new GridLayoutManager(ProgramacionA.this,3));
            firebaseRecyclerAdapter.startListening();
            recyclerViewProgramacion.setAdapter(firebaseRecyclerAdapter);
        }
    }

    private void ELiminarDatos(final String NombreActual, final String ImagenActual){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProgramacionA.this);
        builder.setTitle("Eliminar");
        builder.setMessage("Desea Eliminar la Imagen?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ELIMINAR IMAGEN DE LA DB
                Query query = mRef.orderByChild("nombre").equalTo(NombreActual);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(ProgramacionA.this, "LA IMAGEN HA SIDO ELIMINADA", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProgramacionA.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProgramacionA.this, "ELIMINADO", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProgramacionA.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProgramacionA.this, "CANCELADO POR ADMINISTRADOR", Toast.LENGTH_SHORT).show();

            }
        });

        builder.create().show();
    }


    @Override
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
        menuInflater.inflate(R.menu.menu_agregar, menu);
        menuInflater.inflate(R.menu.menu_vista, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Agregar:
                //Toast.makeText(this, "Agregar Imagen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProgramacionA.this, AgregarProgramacion.class));
                finish();
                break;
            case R.id.Vista:
                Ordenar_Imagenes();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Ordenar_Imagenes(){
        //Para la fuente que descargamos de Dafont
        String ubicacion = "fuentes/King_Rabbit.otf";
        Typeface tf = Typeface.createFromAsset(ProgramacionA.this.getAssets(),ubicacion);

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