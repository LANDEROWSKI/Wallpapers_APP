package com.erick.pg_project.CategoriasAdmin.ProgramacionA;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erick.pg_project.R;
import com.squareup.picasso.Picasso;

public class ViewHolderProramacion extends RecyclerView.ViewHolder{
    View mView;

    private ViewHolderProramacion.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /*Admin presiona el item*/
        void OnItemLongClick(View view, int position); /*ADMIN mantiene presionado el item*/
    }

    public void setOnClickListener(ViewHolderProramacion.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolderProramacion(@NonNull View itemView) {
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

    public void SeteoProgramacion(Context context, String nombre, int vista, String imagen){
        ImageView ImagenProgramacion;
        TextView NombreImagenProgramacion;
        TextView VistaProgramacion;

        //CONEXION CON EL ITEM
        ImagenProgramacion = mView.findViewById(R.id.ImagenProgramacion);
        NombreImagenProgramacion = mView.findViewById(R.id.NombreImagenProgramacion);
        VistaProgramacion = mView.findViewById(R.id.VistaProgramacion);

        NombreImagenProgramacion.setText(nombre);

        //Convertir a string el parametro vista
        String VistaString = String.valueOf(vista);
        VistaProgramacion.setText(VistaString);

        //Controlar posibles errores
        try {
            //SI LA IMAGEN FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(imagen).placeholder(R.drawable.categoria).into(ImagenProgramacion);

        }catch (Exception e){
            //SI LA IMAGEN NO FUE TRAIDA EXITOSAMENTE
            Picasso.get().load(R.drawable.categoria).into(ImagenProgramacion);
        }
    }
}
