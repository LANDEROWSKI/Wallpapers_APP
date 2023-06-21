package com.erick.pg_project.CategoriasAdmin.VideosA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erick.pg_project.CategoriasAdmin.AnimesA.ViewHolderAnime;
import com.erick.pg_project.R;
import com.squareup.picasso.Picasso;

public class ViewHolderVideojuegos extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderVideojuegos.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /*Admin presiona el item*/
        void OnItemLongClick(View view, int position); /*ADMIN mantiene presionado el item*/
    }

    public void setOnClickListener(ViewHolderVideojuegos.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderVideojuegos(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.OnItemLongClick(view, getBindingAdapterPosition());
                return true;
            }
        });


    }

    public void SeteoVideojuegos(Context context, String nombre, int vista, String imagen){
        ImageView ImagenVideojuegos;
        TextView NombreImagenVideojuego;
        TextView VistaVideojuego;

        //CONEXION CON EL ITEM
        ImagenVideojuegos = mView.findViewById(R.id.ImagenVideojuegos);
        NombreImagenVideojuego = mView.findViewById(R.id.NombreImagenVideojuego);
        VistaVideojuego = mView.findViewById(R.id.VistaVideojuego);

        NombreImagenVideojuego.setText(nombre);

        //Convertir a string el parametro vista
        String VistaString = String.valueOf(vista);
        VistaVideojuego.setText(VistaString);

        //Controlar posibles errores
        try {
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).into(ImagenVideojuegos);

        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
