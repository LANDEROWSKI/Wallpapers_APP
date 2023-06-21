package com.erick.pg_project.CategoriasAdmin.ProgramacionA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.erick.pg_project.CategoriasAdmin.AnimesA.AgregarAnime;
import com.erick.pg_project.CategoriasAdmin.AnimesA.Anime;
import com.erick.pg_project.CategoriasAdmin.AnimesA.AnimesA;
import com.erick.pg_project.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AgregarProgramacion extends AppCompatActivity {

    TextView VistaProgramacions;
    EditText NombreProgramacions;
    ImageView ImagenAgregarProgramacion;
    Button PublicarProgramacion;

    String RutaDeAlmacenamiento = "Programacion_Subido/";
    String RutaDeBaseDeDatos = "PROGRAMACION";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;
    int CODIGO_DE_SOLICITUD_IMAGEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_programacion);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaProgramacions = findViewById(R.id.VistaProgramacions);
        NombreProgramacions = findViewById(R.id.NombreProgramacions);
        ImagenAgregarProgramacion = findViewById(R.id.ImagenAgregarProgramacion);
        PublicarProgramacion = findViewById(R.id.PublicarProgramacion);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarProgramacion.this);

        ImagenAgregarProgramacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "SELECCIONAR IMAGEN"), CODIGO_DE_SOLICITUD_IMAGEN);
            }
        });

        PublicarProgramacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirProgramacion();
            }
        });
    }

    private void SubirProgramacion() {

        if(RutaArchivoUri!=null){
            progressDialog.setTitle("ESPERE POR FAVOR");
            progressDialog.setMessage("SUBIENDO IMAGEN DE PROGRAMACION...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(RutaDeAlmacenamiento+System.currentTimeMillis()+"."+ObtenerExtensionDelArchivo(RutaArchivoUri));

            storageReference2.putFile(RutaArchivoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());

                    Uri downloadURI = uriTask.getResult();
                    String mNombre = NombreProgramacions.getText().toString();
                    String mVista = VistaProgramacions.getText().toString();
                    int VISTA = Integer.parseInt(mVista);

                    Anime anime = new Anime(downloadURI.toString(), mNombre, VISTA);
                    String ID_IMAGEN = DatabaseReference.push().getKey();

                    DatabaseReference.child(ID_IMAGEN).setValue(anime);

                    progressDialog.dismiss();
                    Toast.makeText(AgregarProgramacion.this, "SUBIDO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AgregarProgramacion.this, ProgramacionA.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AgregarProgramacion.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    progressDialog.setTitle("PUBLICANDO");
                    progressDialog.setCancelable(false);

                }
            });
        }
    }

    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //SDK31
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CODIGO_DE_SOLICITUD_IMAGEN
                && resultCode==RESULT_OK
                && data != null
                && data.getData() != null){
            RutaArchivoUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), RutaArchivoUri);
                ImagenAgregarProgramacion.setImageBitmap(bitmap);
            }catch (Exception e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }
}