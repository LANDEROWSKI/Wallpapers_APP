package com.erick.pg_project.CategoriasAdmin.AnimesA;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erick.pg_project.R;
import com.squareup.picasso.Picasso;

public class ViewHolderAnime extends RecyclerView.ViewHolder{

    View mView;

    private ViewHolderAnime.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /*Admin presiona el item*/
        void OnItemLongClick(View view, int position); /*ADMIN mantiene presionado el item*/
    }

    public void setOnClickListener(ViewHolderAnime.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderAnime(@NonNull View itemView) {
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

    public void SeteoAnimes(Context context, String nombre, int vista, String imagen){
        ImageView ImagenAnime;
        TextView NombreImagenAnime;
        TextView VistaAnime;

        //CONEXION CON EL ITEM
        ImagenAnime = mView.findViewById(R.id.ImagenAnime);
        NombreImagenAnime = mView.findViewById(R.id.NombreImagenAnime);
        VistaAnime = mView.findViewById(R.id.VistaAnime);

        NombreImagenAnime.setText(nombre);

        //Convertir a string el parametro vista
        String VistaString = String.valueOf(vista);
        VistaAnime.setText(VistaString);

        //Controlar posibles errores
        try {
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenAnime);

        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImagenAnime);
        }
    }
}
