package com.erick.pg_project.Categorias.Cat_Dispositivo;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.erick.pg_project.R;
import com.squareup.picasso.Picasso;

public class ViewHolderCD extends RecyclerView.ViewHolder {
    View mView;

    private ViewHolderCD.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /*Admin presiona el item*/

    }

    public void setOnClickListener(ViewHolderCD.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderCD(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });




    }

    public void SeteoCategoriaD(Context context, String categoria , String imagen){
        ImageView ImagenCategoriaD;
        TextView NombreCategoriaD;


        //CONEXION CON EL ITEM
        ImagenCategoriaD = mView.findViewById(R.id.ImagenCategoriaD);
        NombreCategoriaD = mView.findViewById(R.id.NombreCategoriaD);

        NombreCategoriaD.setText(categoria);



        //Controlar posibles errores
        try {
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenCategoriaD);

        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            //Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Picasso.get().load(R.drawable.categoria).into(ImagenCategoriaD);

        }
    }
}
