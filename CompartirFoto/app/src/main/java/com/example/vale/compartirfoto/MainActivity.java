package com.example.vale.compartirfoto;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        Uri uri = Uri.fromFile(new File("/storage/emulated/0/Pictures/VALE_PIC_20160823_193028.jpg"));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, "ENVIAR FOTO ... "));

        /**
         * PARA COMPARTIR UNA LISTA DE FOTOS
         *
         * ArrayList<Uri> imageUris = new ArrayList<Uri>();
         imageUris.add(imageUri1);
         imageUris.add(imageUri2);

         Intent shareIntent = new Intent();
         shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
         shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
         shareIntent.setType("image/*"); //modificar el tipo MIME por si desean compatir otro tipo de archivos
         startActivity(Intent.createChooser(shareIntent, "Compatir con.."));
         */
    }
}
